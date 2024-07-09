package cn.cloud9.server.system.rbac.service.impl;

import cn.cloud9.server.struct.authority.jwt.client.AuthClientProperty;
import cn.cloud9.server.struct.authority.jwt.server.JwtTokenServerUtils;
import cn.cloud9.server.struct.authority.jwt.util.JwtToken;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.PwdErrorUtil;
import cn.cloud9.server.struct.constant.Constant;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.cloud9.server.struct.util.Assert;
import cn.cloud9.server.struct.util.IpUtil;
import cn.cloud9.server.struct.util.ServletUtil;
import cn.cloud9.server.struct.util.tree.TreeUtil;
import cn.cloud9.server.system.rbac.dto.MenuDTO;
import cn.cloud9.server.system.rbac.dto.RoleDTO;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.mapper.UserMapper;
import cn.cloud9.server.system.rbac.service.IMenuService;
import cn.cloud9.server.system.rbac.service.IPermitService;
import cn.cloud9.server.system.rbac.service.IRoleService;
import cn.cloud9.server.system.rbac.service.IUserService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 下午 06:13
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDTO> implements IUserService {

    @Resource
    private JwtTokenServerUtils jwtTokenServerUtils;

    @Resource
    private AuthClientProperty authClientProperty;

    @Resource
    private IRoleService roleService;

    @Resource
    private IMenuService menuService;

    @Resource
    private IPermitService permitService;

    @Override
    public UserDTO newUser(UserDTO user) {
        final List<UserDTO> list = this.lambdaQuery().eq(UserDTO::getUserUserName, user.getUserUserName()).list();
        Assert.isTrue(CollectionUtils.isNotEmpty(list), ResultMessage.ALREADY_EXIST, "用户账户：" + user.getUserUserName());
        user.setCreator(user.getUserUserName());
        user.setCreateTime(LocalDateTime.now());
        baseMapper.insert(user);
        return user;
    }

    @Override
    public UserContext userLogin(UserDTO user) {
        /* 1、密码错误上限检查 */
        PwdErrorUtil.pwdErrorCheck(user.getUserUserName());

        /* 2、校验用户是否存在 */
        final List<UserDTO> list = lambdaQuery().eq(UserDTO::getUserUserName, user.getUserUserName()).list();
        Assert.isTrue(CollectionUtils.isEmpty(list), ResultMessage.NOT_FOUND_ERROR, user.getUserUserName());

        final UserDTO thisUser = list.get(0);

        /* 3、校验密码是否错误，错误次数计数 */
        boolean isEqualsPassword = thisUser.getUserPassword().equals(user.getUserPassword());
        if (!isEqualsPassword) {
            PwdErrorUtil.pwdErrorCountIncrease(user.getUserUserName());
            Assert.isFalse(isEqualsPassword, ResultMessage.WRONG_PASSWORD);
        }

        /* 4、签发JwtToken，包装用户信息 */
        UserContext userContext = signingJwtTokenForThisUser(thisUser);
        PwdErrorUtil.resetPwdErrorCount(thisUser.getUserUserName());

        log.info("用户：{} 登录成功！ 时间：{}", thisUser.getUserName(), LocalDateTime.now());
        return userContext;
    }

    @Override
    public void userLogout(UserContext userContext) {
        final String token = userContext.getToken();
        final StringRedisTemplate template = SpringContextHolder.getBean(StringRedisTemplate.class);
        final String hKey = token + Constant.SEPARATOR + IpUtil.getIpAddr(ServletUtil.getRequest());
        template.delete(Constant.REDIS_USER_CACHE + Constant.SEPARATOR + hKey);
        log.info("用户：{} 退出了系统！ 时间：{}", userContext.getUserName(), LocalDateTime.now());
    }

    @Override
    public UserContext userLoginByToken(HttpServletRequest request) {
        final String tokenKey = authClientProperty.getUser().getHeaderName();
        String tokenValue = request.getHeader(tokenKey);

        boolean isEmptyInHeader = StringUtils.isBlank(tokenValue);
        if (isEmptyInHeader) tokenValue = request.getParameter(tokenKey);

        boolean isEmptyInUrlParam = StringUtils.isBlank(tokenValue);
        if (isEmptyInUrlParam) {
            final Map<String, String> parameterMap = ServletUtil.getParameterMap(request);
            tokenValue = parameterMap.get(tokenKey);
        }

        boolean isEmptyInParaMap = StringUtils.isBlank(tokenValue);
        if (isEmptyInParaMap) {
           try {
               final String requestBodyJson = ServletUtil.getRequestBodyJson(request);
               final Map<String, String> map = JSON.parseObject(requestBodyJson, Map.class);
               tokenValue = map.get(tokenKey);
           } catch (Exception e) {
               log.error("请求体数据转换失败！", e);
               tokenValue = null;
           }
        }

        boolean isEmptyInJsonBody = StringUtils.isBlank(tokenValue);
        if (isEmptyInJsonBody) {
            final Cookie[] cookies = request.getCookies();
            final boolean isEmptyCookies = Objects.isNull(cookies);
            if (isEmptyCookies) tokenValue = null;
            else {
                final Optional<Cookie> first = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(tokenKey)).findFirst();
                final Cookie cookie = first.orElse(null);
                if (Objects.isNull(cookie)) tokenValue = null;
                else tokenValue = cookie.getValue();
            }
        }

        boolean tokenNotExist = StringUtils.isBlank(tokenValue);
        if (tokenNotExist) {
            log.error("未携带Token, 登录失败！");
            return null;
        }

        final RedisTemplate<String, UserContext> template = SpringContextHolder.getBean("redisTemplate", RedisTemplate.class);
        final ValueOperations<String, UserContext> valOps = template.opsForValue();
        final String hKey = tokenValue + Constant.SEPARATOR + IpUtil.getIpAddr(ServletUtil.getRequest());
        final UserContext userContext = valOps.get(Constant.REDIS_USER_CACHE + Constant.SEPARATOR + hKey);

        /* 如果Token失效，则需要重新登录 */
        Assert.isTrue(Objects.isNull(userContext), ResultMessage.UNAUTHORIZED);
        return userContext;
    }

    @Override
    public IPage<UserDTO> queryUserPage(UserDTO dto) {
        return lambdaQuery()
                .ne(UserDTO::getId, 0)
                .like(StringUtils.isNotBlank(dto.getUserName()), UserDTO::getUserName, dto.getUserName())
                .like(StringUtils.isNotBlank(dto.getUserUserName()), UserDTO::getUserUserName, dto.getUserUserName())
                .between(
                        StringUtils.isNotBlank(dto.getStartCreateTime()) && StringUtils.isNotBlank(dto.getEndCreateTime()),
                        UserDTO::getCreateTime,
                        dto.getStartCreateTime(),
                        dto.getEndCreateTime()
                )
                .orderByDesc(UserDTO::getCreateTime)
                .page(dto.getPage());
    }

    /**
     * 为此用户签发令牌
     * @param thisUser 此用户
     * @return 用户信息
     */
    private UserContext signingJwtTokenForThisUser(UserDTO thisUser) {
        final UserContext userContext = new UserContext();
        userContext.setUserId(thisUser.getId().longValue());
        userContext.setUserName(thisUser.getUserName());
        userContext.setUserAccount(thisUser.getUserUserName());

        /* 2、开始签发JwtToken */
        final JwtToken jwtToken = jwtTokenServerUtils.generateUserToken(userContext, null);
        userContext.setToken(jwtToken.getToken());

        /* 3、查询该用户拥有的权限，角色等等，在这里写逻辑，然后赋值 */
        grantPrivileges(userContext);

        /* 4、最后存入Redis, 为单点登录做凭证保管 */
        final RedisTemplate<String, UserContext> template = SpringContextHolder.getBean("redisTemplate", RedisTemplate.class);
        final ValueOperations<String, UserContext> valOps = template.opsForValue();
        final String hKey = jwtToken.getToken() + Constant.SEPARATOR + IpUtil.getIpAddr(ServletUtil.getRequest());
        valOps.set(Constant.REDIS_USER_CACHE + Constant.SEPARATOR + hKey, userContext, jwtToken.getExpire(), TimeUnit.SECONDS);

        return userContext;
    }

    /**
     * 赋予此用户对应的权限
     * @param userContext 此用户
     */
    private void grantPrivileges(UserContext userContext) {
        if (userContext.isAdmin()) {
            /* 1、获取所有菜单 */
            final List<MenuDTO> menuList = menuService.lambdaQuery().orderByAsc(MenuDTO::getMenuSort).list();
            userContext.setMenus(menuList);

            /* 2、超级管理员不设置角色 */
            List<RoleDTO> roleList = null;

            /* 3、获取所有权限 */
            List<String> permits = permitService.getPermitList(userContext, roleList, menuList);
            userContext.setPermitList(permits);
        } else {
            /* 1、获取所有菜单 */
            List<MenuDTO> menuList = menuService.getMenuList(userContext);
            userContext.setMenus(menuList);

            /* 2、获取所有角色 */
            List<RoleDTO> roleList = roleService.getRoleList(userContext);

            /* 3、转换后赋值 */
            final List<String> roleValues = roleList.stream().map(RoleDTO::getRoleValue).collect(Collectors.toList());
            userContext.setRoles(roleValues);

            /* 4、获取所有权限 */
            List<String> permits = permitService.getPermitList(userContext, roleList, menuList);
            userContext.setPermitList(permits);
        }
    }


}
