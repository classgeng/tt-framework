package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.AesUtil;
import cn.cloud9.server.struct.util.encrypt.MessageDigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 10:38
 */
@Slf4j
public class AesTest {

    /* AES 盐的字符长度必须达到16位 */
    private static final String SALT = "8642-Cloud9-2468-12423432";

    /**
     * 22:54:10.510 [main] INFO cn.cloud9.encrpt.AesTest - plainText:OnCloud9, AES加密的明文
     * 22:54:10.704 [main] INFO cn.cloud9.encrpt.AesTest - encrypt:a28b2963a71e166c50dc27301d5b3c17e5ecd0a2b6c95f6db235537497064b2a
     * 22:54:10.704 [main] INFO cn.cloud9.encrpt.AesTest - decrypt:OnCloud9, AES加密的明文
     */
    @Test
    public void demo() {
        String plainText = "OnCloud9, AES加密的明文";
        log.info("plainText:{}", plainText);
        final String encrypt = AesUtil.aesEncrypt(plainText, SALT);
        log.info("encrypt:{}", encrypt);
        final String decrypt = AesUtil.aesDecrypt(encrypt, SALT);
        log.info("decrypt:{}", decrypt);
    }

    @Test
    public void demo2() {
        String plainText = "OnCloud9, AES加密的明文";
        log.info("plainText:{}", plainText);
        final String encrypt = AesUtil.aesEncrypt(plainText, SALT);
        log.info("encrypt:{}", encrypt);
        final String decrypt = AesUtil.aesDecrypt(encrypt, SALT);
        log.info("decrypt:{}", decrypt);
    }
}
