package cn.cloud9.server.struct.file.controller;

import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.struct.file.FileUtil;
import cn.cloud9.server.struct.file.dto.Chuck;
import cn.cloud9.server.struct.file.dto.ChuckFile;
import cn.cloud9.server.struct.file.service.impl.ChuckFileService;
import cn.cloud9.server.struct.file.service.impl.ChuckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @author OnCloud9
 * @description 大型文件上传
 * @project tt-server
 * @date 2022年11月18日 下午 10:58
 */
@Slf4j
@RestController
@RequestMapping("/chuck-file")
public class ChuckFileController extends BaseController {

    private static final String CHUCK_DIR = "chuck";

    @Resource
    private FileProperty fileProperty;

    @Resource
    private ChuckFileService chuckFileService;

    @Resource
    private ChuckService chuckService;

    /**
     * 上传分块文件
     * @param chuck 分块文件
     * @return 响应结果
     */
    @PostMapping(value = "/chuck", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object uploadFileChuck(@ModelAttribute Chuck chuck) {
        final MultipartFile mf = chuck.getFile();
        log.info("收到文件块 文件块名：{} 块编号：{}", mf.getOriginalFilename(), chuck.getChunkNumber());
        try {

            /* 1、准备存储位置  根目录 + 分块目录 + 无类型文件名的目录  */
            final String pureName = FileUtil.getFileNameWithoutTypeSuffix(mf.getOriginalFilename());
            String storagePath = fileProperty.getBaseDirectory() + File.separator + CHUCK_DIR + File.separator + pureName;
            final File storePath = new File(storagePath);
            if (!storePath.exists()) storePath.mkdirs();

            /* 2、准备分块文件的规范名称 [无类型文件名 -分块号] */
            String chuckFilename = pureName + "-" + chuck.getChunkNumber();

            /* 3、向存储位置写入文件 */
            final File targetFile = new File(storePath, chuckFilename);
            mf.transferTo(targetFile);
            log.debug("文件 {} 写入成功, uuid:{}", chuck.getFilename(), chuck.getIdentifier());

            chuck = chuckService.saveChuck(chuck);
        } catch (Exception e) {
            log.error("文件上传异常：{}, {}", chuck.getFilename(), e.getMessage());
            return e.getMessage();
        }
        return chuck;
    }

    /**
     * 检查该分块文件是否上传了
     * @param chuck 分块文件
     * @return 304 | 分块文件 has-chucked
     */
    @GetMapping("/chuck")
    public Object checkHasChucked(@ModelAttribute Chuck chuck) {
        final boolean hasChucked = chuckService.checkHasChucked(chuck);
        if (hasChucked) response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        return chuck;
    }

    /**
     * 文件合并
     * @param chuckFile 分块文件合并信息
     */
    @PostMapping("/merge")
    public void mergeChuckFile(@ModelAttribute ChuckFile chuckFile) {
        /* 获取存储位置 */
        final String pureName = FileUtil.getFileNameWithoutTypeSuffix(chuckFile.getFilename());
        String storagePath = fileProperty.getBaseDirectory() + File.separator + CHUCK_DIR + File.separator + pureName;
        chuckFileService.mergeChuckFile(storagePath, chuckFile);

        chuckFile.setLocation(storagePath);
        chuckFileService.addChuckFile(chuckFile);
    }
}
