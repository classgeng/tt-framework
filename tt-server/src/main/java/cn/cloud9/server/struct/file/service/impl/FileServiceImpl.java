package cn.cloud9.server.struct.file.service.impl;

import cn.cloud9.server.struct.constant.ResultMessage;
import cn.cloud9.server.struct.exception.ServiceException;
import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.struct.file.dto.FileDTO;
import cn.cloud9.server.struct.file.dto.FileResult;
import cn.cloud9.server.struct.file.mapper.FileMapper;
import cn.cloud9.server.struct.file.service.IFileService;
import cn.cloud9.server.struct.util.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月16日 上午 12:18
 */
@Slf4j
@Service("fileService")
public class FileServiceImpl extends ServiceImpl<FileMapper, FileDTO> implements IFileService {

    @Resource
    private FileProperty fileProperty;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class, IOException.class, ServiceException.class })
    public FileResult fileUpload(MultipartFile[] multipartFiles, String subDir) throws IOException {

        final ArrayList<FileDTO> fileInfo = new ArrayList<>(multipartFiles.length);

        final String subDirectory = StringUtils.isBlank(subDir) ? FileUtil.createDirPathByYearMonth() : subDir;
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile.isEmpty()) continue;

            /* 获取文件信息 */
            final String originalFilename = multipartFile.getOriginalFilename();
            final long fileSize = multipartFile.getSize();
            final String contentType = multipartFile.getContentType();
            final String name = multipartFile.getName();

            /* 准备规范名称 */
            String specFilename = Instant.now().toEpochMilli() + "." + FileUtil.getFileTypeSuffix(originalFilename);

            /* 准备存储位置 */
            String storagePath = fileProperty.getBaseDirectory() + File.separator + subDirectory;
            final File storePath = new File(storagePath);
            if (!storePath.exists()) storePath.mkdirs();

            /* 向存储位置写入文件 */
            final File targetFile = new File(storePath, specFilename);
            multipartFile.transferTo(targetFile);

            /* 记录文件上传信息 */
            final FileDTO dto = recordUploadInfo(originalFilename, specFilename, fileSize, contentType, subDirectory);
            fileInfo.add(dto);
        }
        return FileResult.fileSuccess(fileInfo);
    }

    @Override
    public void fileDownload(String subDir, String filename, String originFilename, HttpServletResponse response)  {
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            /* 文件信息校验 */
            final String baseDirectory = fileProperty.getBaseDirectory();
            File targetFile = new File(baseDirectory + File.separator + subDir + File.separator + filename);
            Assert.isTrue(!targetFile.exists(), ResultMessage.NO_API, subDir + File.separator + filename);

            /* 准备下载的响应信息设置 */
            FileUtil.setDownloadResponseInfo(response, targetFile, originFilename);

            /* 调用IO工具类输出文件 */
            inputStream = new FileInputStream(targetFile);
            outputStream = response.getOutputStream();
            final int copy = IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            log.error("文件下载异常：{}", e.getMessage());
            try {
                if (Objects.nonNull(inputStream)) inputStream.close();
                if (Objects.nonNull(outputStream)) outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 记录文件上传信息
     * @param originalFilename 源文件名
     * @param specFilename 规范文件名
     * @param fileSize 文件大小 （字节单位）
     * @param contentType 内容类型
     * @param subDirectory 上级目录
     * @return 文件上传记录
     */
    private FileDTO recordUploadInfo(String originalFilename, String specFilename, long fileSize, String contentType, String subDirectory) {
        final FileDTO dto = new FileDTO();
        dto.setSrcFilename(originalFilename);
        dto.setSpecFilename(specFilename);
        dto.setFileSize(fileSize);
        dto.setSubDirectory(subDirectory);
        dto.setContentType(contentType);
        dto.setCreator("admin");
        dto.setCreateTime(LocalDateTime.now());
        baseMapper.insert(dto);
        return dto;
    }
}
