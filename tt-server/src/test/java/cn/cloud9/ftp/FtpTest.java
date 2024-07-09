package cn.cloud9.ftp;

import cn.cloud9.server.MainApplication;
import cn.cloud9.server.struct.file.FileProperty;
import cn.cloud9.server.tool.file.FileViewDTO;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.File;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MainApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FtpTest {

    @Resource
    private FileProperty fileProperty;

    @SneakyThrows
    @Test
    public void printFileInfo() {
         File file = new File(fileProperty.getBaseDirectory() + File.separator + "backup" + File.separator + "db-my-info2023年01月27日11时33分59秒.sql");
        // File file = new File("C:\\");
        log.info("toPackageFileDTO -> {}", JSON.toJSONString(FileViewDTO.getInstance(file)));
        String userHomePath = FileUtil.getUserHomePath();
        log.info("userHomePath -> {}", userHomePath);

    }
}
