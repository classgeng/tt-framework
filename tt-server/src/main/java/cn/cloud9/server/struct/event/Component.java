package cn.cloud9.server.struct.event;

import cn.cloud9.server.struct.file.dto.FileDTO;
import cn.cloud9.server.struct.file.mapper.FileMapper;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class Component {

    @Resource
    private FileMapper fileMapper;

    @EventListener
    public void receiveTestEvent(TestEvent testEvent) {
        System.out.println(testEvent.message + " receiveTestEvent1接收");

        FileDTO fileDTO = new FileDTO();
        fileDTO.setCreator("aaa222");
        fileDTO.setCreateTime(LocalDateTime.now());
        fileDTO.setContentType("无");
        fileDTO.setFileSize(100L);
        fileDTO.setSpecFilename("sadasd");
        fileDTO.setSrcFilename("sad123asd");
        fileDTO.setSubDirectory("/sadas");
        int insert = fileMapper.insert(fileDTO);

    }

    @EventListener
    public void receiveTestEvent2(TestEvent testEvent) {
        System.out.println(testEvent.message + " receiveTestEvent2接收");
    }
}
