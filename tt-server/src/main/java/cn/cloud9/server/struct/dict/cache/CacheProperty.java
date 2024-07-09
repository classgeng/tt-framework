package cn.cloud9.server.struct.dict.cache;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 缓存配置读取类，用于读取需要Redis装载的缓存
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月13日 下午 10:10
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "cache")
public class CacheProperty {
    private Map<String, String> sqlMap;
}
