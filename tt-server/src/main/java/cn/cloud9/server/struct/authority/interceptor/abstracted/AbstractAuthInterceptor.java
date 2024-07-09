package cn.cloud9.server.struct.authority.interceptor.abstracted;

import cn.cloud9.server.struct.authority.jwt.client.AuthClientProperty;
import cn.cloud9.server.struct.authority.annotation.NoLoginRequire;
import cn.cloud9.server.struct.authority.annotation.PermissionRequire;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.response.ApiResult;
import cn.cloud9.server.struct.util.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * @description 抽象拦截器
 * @project tt-server
 * @date 2022年11月20日 下午 05:31
 */
@Slf4j
public abstract class AbstractAuthInterceptor extends HandlerInterceptorAdapter {


    @Resource
    private AuthClientProperty authClientProperty;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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

        /* 4、解析令牌, 校验是否授权, 交给服务自定义方式实现获取 */
        final UserContext userContext = this.getUserContext(tokenValue, request, response, handler);
        boolean isAuthorized = Objects.nonNull(userContext);
        if (!isAuthorized) {
            log.info("token失效，或者用户尚未登录 {}", request.getRequestURL());
            ServletUtil.sendJsonResult(response, ApiResult.error(ResultMessage.UNAUTHORIZED));
            return false;
        }

        /* 5、校验用户是否为超级管理员 */
        final Object adminFlag = userContext.get("isAdministrator");
        boolean isAdmin = Objects.nonNull(adminFlag) && "true".equalsIgnoreCase(String.valueOf(adminFlag));
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
        final Object permissionList = userContext.get("permissionList");
        List<String> pList = (List<String>) permissionList;
        final boolean contains = pList.contains(requiredPermission);
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


    public abstract UserContext getUserContext(String tokenValue, HttpServletRequest request, HttpServletResponse response, Object handler);
}
