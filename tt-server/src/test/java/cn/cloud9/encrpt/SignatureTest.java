package cn.cloud9.encrpt;

import cn.cloud9.server.struct.util.encrypt.HexUtil;
import cn.cloud9.server.struct.util.encrypt.RsaUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * @author OnCloud9
 * @description 数字签名？
 * @project tt-server
 * @date 2022年11月27日 上午 10:09
 */
@Slf4j
public class SignatureTest {

    private static final String SIGNATURE_ALGORITHM = "sha256withrsa";

    @Test
    public void demo() {
        String content = "密文内容";
        log.info("content {}", content);

        final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);

        /* 获取签名 */
        final String sign = signature(bytes);
        log.info("signature {}", sign);

        boolean isSigned = verify(bytes, sign);
        log.info("isSigned {}", isSigned);
    }


    /**
     * 签名操作
     * @param data
     * @return
     */
    @SneakyThrows
    private static String signature(byte[] data) {
        final PrivateKey privateKey = RsaUtil.getPrivateKey();
        final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);
        return HexUtil.bytes2hex(signature.sign());
    }

    /**
     * 签名验证
     * @param data
     * @return
     */
    @SneakyThrows
    private static boolean verify(byte[] data, String sign) {
        final PublicKey publicKey = RsaUtil.getPublicKey();
        final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        final byte[] bytes = HexUtil.hex2bytes(sign);
        return signature.verify(bytes);
    }
}
