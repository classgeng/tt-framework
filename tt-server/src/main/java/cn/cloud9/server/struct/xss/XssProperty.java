package cn.cloud9.server.struct.xss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月18日 下午 08:29
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xss")
public class XssProperty {
    private List<String> excludes;
    private List<String> properties;
    private boolean enabled;
}
