package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.authority.annotation.UserRequire;
import cn.cloud9.server.struct.authority.user.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author OnCloud9
 * @description 拦截器测试
 * @project tt-server
 * @date 2022年11月20日 下午 04:54
 */
@Slf4j
@RestController
@RequestMapping("${auth-server.api-path}/test/interceptor")
public class InterceptorController {

    @GetMapping("/get")
    public String interceptorTest() {
        return "访问成功！";
    }


    @GetMapping("/get-resolve")
    public UserContext getUserContextByResolver(@UserRequire UserContext userCtx) {
        log.info("用户解析器获取结果： {}", userCtx);
        return userCtx;
    }
}
