package cn.cloud9.server.struct.file.controller;

import cn.cloud9.server.struct.response.NoApiResult;
import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.file.dto.FileResult;
import cn.cloud9.server.struct.file.service.IFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月15日 下午 11:17
 */
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController extends BaseController {

    @Resource
    private IFileService fileService;

    /**
     * 必须 post请求 + multipart/form-data
     * 文件上传 /file/upload
     *
     * @param multipartFiles 文件数组
     * @param subDir 上级目录
     * @return 上传成功的文件信息集合
     */
    @NoApiResult
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FileResult fileUpload(
        @RequestParam("files") MultipartFile[] multipartFiles,
        @RequestParam(value = "subDir", required = false) String subDir
    ) {
        try {
            return fileService.fileUpload(multipartFiles, subDir);
        } catch (Exception e) {
            log.error("文件上传异常：{}", e.getMessage());
            return FileResult.fileError(ResultMessage.SYSTEM_ERROR);
        }
    }

    /**
     * 文件下载
     * @param subDir 上级目录
     * @param filename 文件名
     * @param originFilename 源文件名
     */
    @GetMapping(value = "/download")
    public void fileDownload(
            @RequestParam("subDir") String subDir,
            @RequestParam("filename") String filename,
            @RequestParam(value = "srcFilename", required = false) String originFilename
    ) {
        fileService.fileDownload(subDir, filename, originFilename, response);
    }
}
