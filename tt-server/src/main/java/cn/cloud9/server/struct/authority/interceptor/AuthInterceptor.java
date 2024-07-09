package cn.cloud9.server.struct.authority.interceptor;

import cn.cloud9.server.struct.authority.annotation.NoLoginRequire;
import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.authority.jwt.client.AuthClientProperty;
import cn.cloud9.server.struct.authority.jwt.client.JwtTokenClientUtils;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.response.ApiResult;
import cn.cloud9.server.struct.util.IpUtil;
import cn.cloud9.server.struct.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * @author OnCloud9
 * @description 授权拦截器
 * @project tt-server
 * @date 2022年11月20日 下午 02:47
 */
@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private AuthClientProperty authClientProperty;

    @Resource
    private JwtTokenClientUtils jwtTokenClientUtils;

    @Resource
    private RedisTemplate<String, UserContext> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        /* 1、校验是否为需要拦截的接口 */
        final boolean isTrue = handler.getClass().isAssignableFrom(HandlerMethod.class);
        if (!isTrue) {
            final StringBuffer requestURL = request.getRequestURL();
            log.info("非拦截接口： {}", requestURL);
            return true;
        }

        /* 2、校验接口是否需要忽略执行 */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        final NoLoginRequire noLoginRequire = handlerMethod.getMethodAnnotation(NoLoginRequire.class);
        if (Objects.nonNull(noLoginRequire)) return true;

        /* 3、抓取请求接口携带的token, 这里需要支持多个存储位置获取 */
        final String tokenKey = authClientProperty.getUser().getHeaderName();
        String tokenValue = request.getHeader(tokenKey);

        final boolean isEmptyInHeader = StringUtils.isBlank(tokenValue);
        if (isEmptyInHeader) tokenValue = request.getParameter(tokenKey);

        final boolean isEmptyInParameter = StringUtils.isBlank(tokenValue);
        if (isEmptyInParameter) {
            final Cookie[] cookies = request.getCookies();
            if (!Objects.isNull(cookies)) {
                final Optional<Cookie> first = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(tokenKey)).findFirst();
                final Cookie cookie = first.orElse(null);
                tokenValue = Objects.isNull(cookie) ? "" : cookie.getValue();
            }
        }

        final boolean isEmptyInCookie = StringUtils.isBlank(tokenValue);
        if (isEmptyInCookie) {
            log.info("未能找到请求携带的Token! {}", request.getRequestURL());
            ServletUtil.sendJsonResult(response, ApiResult.error(ResultMessage.UNAUTHORIZED));
            return false;
        }

        /* 这里有两种方式，直接按令牌解析获取用户信息，保证用户信息， 第二种是按key来获取用户对象 */

        /* 4、用解析令牌的方式, 校验是否授权 */
        // UserContext userContext = jwtTokenClientUtils.getUserContext(tokenValue);
        // boolean isAuthorized = Objects.nonNull(userContext);
        // if (!isAuthorized) {
        //     log.info("token失效，或者用户尚未登录 {}", request.getRequestURL());
        //     ServletUtil.sendJsonResult(response, ApiResult.error(ResultMessage.UNAUTHORIZED));
        //     return false;
        // }

        /* 4.1 可以追加Redis获取 */
        final ValueOperations<String, UserContext> valOps = redisTemplate.opsForValue();
        final String ipAddr = IpUtil.getIpAddr(request);
        UserContext userContext = valOps.get(Constant.REDIS_USER_CACHE + Constant.SEPARATOR + tokenValue + Constant.SEPARATOR + ipAddr);

        boolean isAuthorized = Objects.nonNull(userContext);
        if (!isAuthorized) {
            log.info("token失效，或者用户尚未登录 {}", request.getRequestURL());
            ServletUtil.sendJsonResult(response, ApiResult.error(ResultMessage.UNAUTHORIZED));
            return false;
        }

        /* 5、校验用户是否为超级管理员 */
        final boolean isAdmin = userContext.isAdmin();
        if (isAdmin) {
            UserContextHolder.setUserContext(userContext);
            return true;
        }

        /* 6、校验接口是否有权限控制 */
        final PermissionRequire permissionRequire = handlerMethod.getMethodAnnotation(PermissionRequire.class);
        boolean noPermissionControl = Objects.isNull(permissionRequire);
        if (noPermissionControl) {
            UserContextHolder.setUserContext(userContext);
            return true;
        }

        final String requiredPermission = permissionRequire.permission();
        final List<String> permitList = userContext.getPermitList();
        final boolean contains = permitList.contains(requiredPermission);
        if (contains) {
            UserContextHolder.setUserContext(userContext);
            return true;
        }

        final String apiPath = request.getRequestURL().toString();
        log.info("用户没有此操作权限！ 路径：{}, 权限值：{}", apiPath, requiredPermission);
        ServletUtil.sendJsonResult(response, ApiResult.error(ResultMessage.UNAUTHORIZED, apiPath, requiredPermission));
        return false;
    }

    /**
     * 完成后销毁用户信息
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContextHolder.removeUserContext();
    }
}
