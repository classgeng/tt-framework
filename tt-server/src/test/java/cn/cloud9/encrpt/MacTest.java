package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.MessageDigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 09:43
 */
@Slf4j
public class MacTest {

    /**
     * JDK原生MAC加密
     * 22:05:14.137 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : ffe0087fb3e84e21df62de8baf0257bb
     * 22:05:14.137 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : 659e1e7d422cf2e43d09225621ef15bdae755e7618c519e773316da6c13a2682
     * 22:05:14.138 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : b5d206d19f94cc1d969623ec35fb740b20eafecdfdf437c4f6b0583cb3b77edbe6756a34fa2b31e08a70fa5f4c8b4edf9d8c4d2f0621762c06dad98f9120a869
     */
    @Test
    public void demo() {
        String key = "HyIte";
        String plainText = "OnCloud9,MAC加密的明文测试";
        log.info("plainText : {}", plainText);

        String algorithm = "HmacMD5";
        String encodeText = MessageDigestUtil.doMacDigest(plainText, key, algorithm);
        log.info("encodeText : {}", encodeText);

        algorithm = "HmacSHA256";
        encodeText = MessageDigestUtil.doMacDigest(plainText, key, algorithm);
        log.info("encodeText : {}", encodeText);

        algorithm = "HmacSHA512";
        encodeText = MessageDigestUtil.doMacDigest(plainText, key, algorithm);
        log.info("encodeText : {}", encodeText);
    }

    /**
     * Apache-Commons-Codec工具类
     * 22:01:31.583 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : ffe0087fb3e84e21df62de8baf0257bb
     * 22:01:31.588 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : 659e1e7d422cf2e43d09225621ef15bdae755e7618c519e773316da6c13a2682
     * 22:01:31.590 [main] INFO cn.cloud9.encrpt.MacTest - encodeText : b5d206d19f94cc1d969623ec35fb740b20eafecdfdf437c4f6b0583cb3b77edbe6756a34fa2b31e08a70fa5f4c8b4edf9d8c4d2f0621762c06dad98f9120a869
     */
    @Test
    public void demo2() {
        String key = "HyIte";
        String plainText = "OnCloud9,MAC加密的明文测试";

        String hmacMd5Hex = new HmacUtils(HmacAlgorithms.HMAC_MD5, key.getBytes(StandardCharsets.UTF_8))
                .hmacHex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", hmacMd5Hex);

        String hmacSha256Hex = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key.getBytes(StandardCharsets.UTF_8))
                .hmacHex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", hmacSha256Hex);

        String hmacSha512Hex = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, key.getBytes(StandardCharsets.UTF_8))
                .hmacHex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", hmacSha512Hex);
    }
}
