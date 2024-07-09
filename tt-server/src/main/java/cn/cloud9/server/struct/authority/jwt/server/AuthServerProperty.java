package cn.cloud9.server.struct.authority.jwt.server;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import static cn.cloud9.server.struct.authority.jwt.server.AuthServerProperty.SERVER_PREFIX;
/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 上午 11:37
 */
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = SERVER_PREFIX)
public class AuthServerProperty {
    public static final String SERVER_PREFIX = "auth-server";

    private TokenInfo user;
    private String apiPath;

    @Data
    public static class TokenInfo {
        /**
         * 过期时间
         */
        private Integer expire = 7200;
        /**
         * 加密 服务使用
         */
        private String priKey;
        /**
         * 解密
         */
        private String pubKey;
    }
}
