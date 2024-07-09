package cn.cloud9.server.struct.file;

import lombok.Data;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月15日 下午 11:34
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperty {

    /* 文件基础目录位置 */
    private String baseDirectory;
}
