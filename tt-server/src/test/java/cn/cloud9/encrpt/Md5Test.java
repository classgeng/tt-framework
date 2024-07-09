package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月23日 下午 10:55
 */
@Slf4j
public class Md5Test {

    /**
     * JDK原生API实现MD5加密
     * @throws Exception
     */
    @Test
    public void demo() throws Exception {
        String plainText = "OnCloud9,MD5加密的明文测试";
        log.info("plainText : {}", plainText);
        /* 获取MD5算法对象 */
        String algorithm = "MD5";
        final MessageDigest md = MessageDigest.getInstance(algorithm);

        /* 转成字节数组后进行加密，得到加密后的字节数组 */
        final byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
        final byte[] encodeBytes = md.digest(plainTextBytes);

        /* 得到的字节数组需要按16进制进行转换 */
        final String hex = HexUtil.bytes2hex(encodeBytes);
        log.info("encodeText : {}", hex);
    }

    /**
     * 使用Apache-Codec
     */
    @Test
    public void demo2() {
        String plainText = "OnCloud9, Apache-Codec包 MD5加密的明文测试";
        log.info("plainText : {}", plainText);
        final String md5Hex = DigestUtils.md5Hex(plainText.getBytes(StandardCharsets.UTF_8));
        log.info("encodeText : {}", md5Hex);
    }
}
