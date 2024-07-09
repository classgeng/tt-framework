package cn.cloud9.server.struct.authority.user;

import cn.cloud9.server.struct.authority.annotation.NoLoginRequire;
import cn.cloud9.server.struct.authority.annotation.UserRequire;
import cn.cloud9.server.struct.authority.jwt.server.JwtTokenServerUtils;
import cn.cloud9.server.struct.authority.jwt.util.JwtToken;
import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.validator.groups.QueryCheck;
import cn.cloud9.server.system.rbac.dto.UserDTO;
import cn.cloud9.server.system.rbac.service.IUserService;
import cn.cloud9.server.test.model.UserModel;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年12月06日 下午 11:47
 */
@Slf4j
@RestController
@RequestMapping("${auth-server.api-path}/sys")
public class LoginController extends BaseController {

    @Resource
    private JwtTokenServerUtils jwtTokenServerUtils;
    @Resource
    private IUserService userService;

    /**
     * 用户登陆
     * @param user
     * @return
     */
    @NoLoginRequire
    @PostMapping("/login")
    public UserContext userLogin(@RequestBody @Validated UserDTO user) {
        return userService.userLogin(user);
    }

    /**
     * 使用Token直接登录系统
     * @return userContext
     */
    @NoLoginRequire
    @PostMapping("/login-by-token")
    public UserContext userLoginByToken() {
        return userService.userLoginByToken(request);
    }

    /**
     * 获取当前用户
     * @param userContext
     * @return
     */
    @GetMapping("/user-info")
    public UserContext userInfo(@UserRequire UserContext userContext) {
        return userContext;
    }



    @GetMapping("/logout")
    public void userLogout(@UserRequire UserContext userContext) {
        userService.userLogout(userContext);
    }





    /**
     * 获取当前用户信息
     * @param userContext
     * @return
     */
    @GetMapping("/currentUser")
    public UserContext getCurrentUser(@UserRequire UserContext userContext) {
        final UserContext userCtx = UserContextHolder.getUserContext();
        log.info("获取到currentUser: {} 对比持有器的user {} 是否相等？ {}", userContext, userCtx, userContext == userCtx);
        return userContext;
    }



    /**
     * 令牌生成测试
     * @param userModel 测试的用户模型
     * @return 用户令牌
     */
    @NoLoginRequire
    @GetMapping("/gen-token")
    public JwtToken genToken(@ModelAttribute UserModel userModel) {
        final String username = userModel.getUsername();
        final String password = userModel.getPassword();

        final UserContext userCtx = new UserContext();
        userCtx.setUserId(1001L);
        userCtx.setUserName("administrator");
        userCtx.setUserAccount(username);

        log.info("原始 userCtx {}", userCtx);

        final JwtToken jwtToken = jwtTokenServerUtils.generateUserToken(userCtx, null);
        log.info("token生成成功！ {}", JSON.toJSONString(jwtToken));

        final UserContext userContext = jwtTokenServerUtils.getUserContext(jwtToken.getToken());
        log.info("token解析后：userCtx {}", userContext);
        return jwtToken;
    }
}
