package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.MessageDigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 09:39
 */
@Slf4j
public class Sha512 {
    /**
     * JDK原生SHA-512加密
     * a3decd0260c2be0e3eb1d6ed6e8366f7756db2af09d064f82aae228c797d15d3bf330002f4cdbd1724f667714fb1e874fd234b22f61a56fa29f0e7cd0b346009
     */
    @Test
    public void demo() {
        String plainText = "OnCloud9,SHA-512加密的明文测试";
        log.info("plainText : {}", plainText);
        /* 获取MD5算法对象 */
        String algorithm = "SHA-512";
        final String encodeText = MessageDigestUtil.doDigest(plainText, algorithm);
        log.info("encodeText : {}", encodeText);
    }

    /**
     * Apache-Commons-Codec工具类
     * a3decd0260c2be0e3eb1d6ed6e8366f7756db2af09d064f82aae228c797d15d3bf330002f4cdbd1724f667714fb1e874fd234b22f61a56fa29f0e7cd0b346009
     */
    @Test
    public void demo2() {
        String plainText = "OnCloud9,SHA-512加密的明文测试";
        final String sha512Hex = DigestUtils.sha512Hex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", sha512Hex);
    }
}
