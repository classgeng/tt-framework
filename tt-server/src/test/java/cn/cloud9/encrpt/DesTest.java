package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.AesUtil;
import cn.cloud9.server.struct.util.encrypt.DesUtil;
import cn.cloud9.server.struct.util.encrypt.MessageDigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 10:17
 */
@Slf4j
public class DesTest {

    /* 盐的字符长度必须达到8位 */
    private static final String SALT = "-Cloud9-";

    /**
     * 22:35:24.149 [main] INFO cn.cloud9.encrpt.DesTest - plainText:OnCloud9, DES加密的明文
     * 22:35:24.338 [main] INFO cn.cloud9.encrpt.DesTest - encrypt:nESFgnrpzAIIx3zyBG6bk6qjtWeSIZ4F8HbqOXZrnpg=
     * 22:35:24.339 [main] INFO cn.cloud9.encrpt.DesTest - decrypt:OnCloud9, DES加密的明文
     */
    @Test
    public void demo() {
        String plainText = "OnCloud9, DES加密的明文";
        log.info("plainText:{}", plainText);
        final String encrypt = DesUtil.desEncrypt(plainText, SALT);
        log.info("encrypt:{}", encrypt);
        final String decrypt = DesUtil.desDecrypt(encrypt, SALT);
        log.info("decrypt:{}", decrypt);
    }

}
