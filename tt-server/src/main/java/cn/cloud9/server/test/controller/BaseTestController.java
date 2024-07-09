package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 08:06
 */
@Slf4j
@RestController
@RequestMapping("/test/base")
public class BaseTestController extends BaseController {

    /**
     * /test/base/inner
     * 测试BaseController能否拿到Request实例
     * @return
     */
    @GetMapping("/inner")
    public String innerInstanceTest() {
        final String servletPath = this.request.getServletPath();
        log.info(servletPath);
        return servletPath;
    }
}
