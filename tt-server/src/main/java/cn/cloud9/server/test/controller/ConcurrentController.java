package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.file.dto.FileDTO;
import cn.cloud9.server.struct.file.mapper.FileMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/concurrent")
public class ConcurrentController {

    @Resource
    private FileMapper fileMapper;

    @GetMapping("/test")
    public void test() {
        FileDTO fileDTO = fileMapper.selectById(10);
        fileDTO.setCreator("OnCloud9");
        fileMapper.updateById(fileDTO);

        CompletableFuture<FileDTO> finalFileDTO = CompletableFuture.supplyAsync(() -> {

            FileDTO f2 = fileMapper.selectById(10);
            fileDTO.setCreator("OnCloud9-222222");
            fileMapper.updateById(fileDTO);

            return f2;
        }).thenApply(f3 -> {
            f3 = fileMapper.selectById(10);
            fileDTO.setCreator("OnCloud9-333333");
            fileMapper.updateById(fileDTO);

            return f3;
        });


    }
}
