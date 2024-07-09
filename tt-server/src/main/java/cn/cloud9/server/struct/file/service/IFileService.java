package cn.cloud9.server.struct.file.service;

import cn.cloud9.server.struct.file.dto.FileResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IFileService {


    FileResult fileUpload(MultipartFile[] multipartFiles, String subDir) throws IOException;

    void fileDownload(String subDir, String filename, String originFilename, HttpServletResponse response);

}
