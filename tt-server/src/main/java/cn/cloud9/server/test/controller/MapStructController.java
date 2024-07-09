package cn.cloud9.server.test.controller;

import cn.cloud9.server.test.mapstruct.DemoConverter;
import cn.cloud9.server.test.mapstruct.DemoDTO;
import cn.cloud9.server.test.mapstruct.DemoEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test/map-struct")
public class MapStructController {

    @Resource
    private DemoConverter demoConverter;

    @GetMapping("/get")
    public DemoEntity converterTest() {
        DemoDTO build = DemoDTO.builder()
                .fieldA(1001)
                .fieldB(Boolean.TRUE)
                .fieldC("TEST")
                .fieldG(3003L)
                .build();
        return demoConverter.dto2entity(build);
    }
}
