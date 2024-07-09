package cn.cloud9.server.tool.file;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/file-view")
public class FileViewController {

    @Resource
    private FileViewService fileViewService;

    @GetMapping("/path")
    public List<FileViewDTO> listFiles(@RequestParam("path") String path) {
        return fileViewService.listFiles(path);
    }
}
