package cn.cloud9.server.tool.file;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FileViewService {

    public List<FileViewDTO> listFiles(String path) {
        FileViewDTO instance = FileViewDTO.getInstance(path);

        boolean isEmpty = !instance.isExists();
        if (isEmpty) return Collections.emptyList();
        boolean isFile = instance.isFile();
        if (isFile) return Collections.singletonList(instance);

        return instance.list();
    }
}
