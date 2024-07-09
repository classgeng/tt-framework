package cn.cloud9.server.test.controller;

import cn.cloud9.server.test.model.MyType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author OnCloud9
 * @description 枚举类自定义获取接口
 * @project tt-server
 * @date 2022年11月23日 下午 09:55
 */
@RestController
@RequestMapping("/test/enum")
public class EnumController {


    @GetMapping("/cate")
    public List<Map<String, String>> getEnumCateList(@RequestParam("cate") String cate) {
        return MyType.getItemListByCate(cate);
    }

    @GetMapping("/name")
    public String getEnumCateList(@RequestParam("cate") String cate, @RequestParam("code") Integer code) {
        return MyType.getNameBy(cate, code);
    }
}
