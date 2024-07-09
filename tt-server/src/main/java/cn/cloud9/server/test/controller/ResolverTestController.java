package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.authority.annotation.UserRequire;
import cn.cloud9.server.test.model.UserModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月09日 上午 12:09
 */
@RestController
@RequestMapping("/test/resolve")
public class ResolverTestController {

    /**
     * 参数解析器测试接口
     * @param userModel
     * @return
     */
    @GetMapping("/user")
    public UserModel userResolve(@UserRequire UserModel userModel) {
        return userModel;
    }
}
