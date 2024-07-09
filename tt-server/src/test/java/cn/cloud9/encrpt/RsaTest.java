package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月27日 上午 08:39
 */
@Slf4j
public class RsaTest {



    @Test
    public void demo() {
        String plainText = "OnCloud9, RSA加密测试明文";
        log.info("plainText:{}", plainText);

        final String rsaEncrypt = RsaUtil.rsaEncrypt(plainText, RsaUtil.getPrivateKey());
        final String rsaDecrypt = RsaUtil.rsaDecrypt(rsaEncrypt, RsaUtil.getPublicKey());
        log.info("rsaEncrypt:{}", rsaEncrypt);
        log.info("rsaDecrypt:{}", rsaDecrypt);


    }



}
