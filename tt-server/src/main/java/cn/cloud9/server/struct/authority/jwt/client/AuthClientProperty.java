package cn.cloud9.server.struct.authority.jwt.client;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 客户端认证配置
 */

@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = AuthClientProperty.CLIENT_PREFIX)
public class AuthClientProperty {
    public static final String CLIENT_PREFIX = "auth-client";
    private TokenInfo user;
    @Data
    public static class TokenInfo {
        /**
         * 请求头名称
         */
        private String headerName;
        /**
         * 解密 网关使用
         */
        private String pubKey;
    }
}
