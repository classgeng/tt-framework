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
 * @date 2022年11月26日 上午 11:33
 */
@Slf4j
public class Sha256 {

    /**
     * JDK原生SHA-256加密
     * b9c7992b600f8d46d655b1a350546df8cff851e93f61dab50a5976751be3de85
     */
    @Test
    public void demo() {
        String plainText = "OnCloud9,SHA-256加密的明文测试";
        log.info("plainText : {}", plainText);
        /* 获取MD5算法对象 */
        String algorithm = "SHA-256";
        final String encodeText = MessageDigestUtil.doDigest(plainText, algorithm);
        log.info("encodeText : {}", encodeText);
    }

    /**
     * Apache-Commons-Codec工具类
     * b9c7992b600f8d46d655b1a350546df8cff851e93f61dab50a5976751be3de85
     */
    @Test
    public void demo2() {
        String plainText = "OnCloud9,SHA-256加密的明文测试";
        final String sha256Hex = DigestUtils.sha256Hex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", sha256Hex);
    }
}
