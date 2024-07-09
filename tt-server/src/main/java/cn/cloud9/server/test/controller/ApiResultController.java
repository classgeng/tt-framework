package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.response.NoApiResult;
import cn.cloud9.server.test.model.MyType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 05:45
 */
@RestController
@RequestMapping("/test/result")
public class ApiResultController {

    /**
     * 测试 @NoApiResult注解能否取消API包裹
     * @return
     */
    @NoApiResult
    @GetMapping("/no")
    public String noApiResult() {
        return "没有API格式包裹响应！";
    }

    /**
     * 测试 FastJson转换配置的特性，和枚举转换结果
     * @return
     */
    @GetMapping("/converter")
    public Map<String, Object> converterFeaturesTest() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("list", null);
//        testMap.put("myType", MyType.TYPE1);
//        testMap.put("usingMethodGet", MyType.TYPE1.getItem());
        testMap.put("object", null);

        return testMap;
    }
}
