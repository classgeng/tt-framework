package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.event.TestEvent;
import cn.cloud9.server.struct.file.dto.FileDTO;
import cn.cloud9.server.struct.file.mapper.FileMapper;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/test/AppEventListen")
public class AppEventListenController {


    @Resource
    private FileMapper fileMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    @GetMapping("/tx")
    public void txExceptionTest() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setCreator("aaa111");
        fileDTO.setCreateTime(LocalDateTime.now());
        fileDTO.setContentType("无");
        fileDTO.setFileSize(100L);
        fileDTO.setSpecFilename("sadasd");
        fileDTO.setSrcFilename("sad123asd");
        fileDTO.setSubDirectory("/sadas");
        int insert = fileMapper.insert(fileDTO);


        TestEvent testEvent = new TestEvent(this);
        testEvent.message = "事件测试";
        applicationEventPublisher.publishEvent(testEvent);
    }
}
