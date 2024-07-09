package cn.cloud9.server.test.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author OnCloud9
 * @description Controller接口基础测试
 * @project tt-server
 * @date 2022年11月06日 下午 05:15
 */
@RestController
@RequestMapping("/test/api")
public class ApiController {


    @GetMapping("/get")
    public String getAction() {
        return "GET 请求成功";
    }

    @PostMapping("/add")
    public String postAction(@RequestBody Map<String, String> body) {
        return "Post 请求成功";
    }

    @PutMapping("/update")
    public String putAction(@RequestBody Map<String, String> body) {
        return "Put 请求成功";
    }

    @DeleteMapping("/delete")
    public String deleteAction(@RequestBody Map<String, String> body) {
        return "Delete 请求成功";
    }
}
