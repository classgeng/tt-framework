package cn.cloud9.server.struct.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 11:06
 */
public class DesUtil {
    private static final String ALGORITHM_DES = "DES";
    /**
     * DES加密
     * @param plainText DES加密的明文
     * @param salt DES加密的盐
     * @return DES密文
     */
    public static String desEncrypt(String plainText, String salt) {
        try {
            final Cipher cipher = MessageDigestUtil.getCipher(ALGORITHM_DES, Cipher.ENCRYPT_MODE, salt);
            final byte[] encodeBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(encodeBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES解密
     * @param encodeText DES密文
     * @param salt DES加密的盐
     * @return DES加密的明文
     */
    public static String desDecrypt(String encodeText, String salt) {
        try {
            final Cipher cipher = MessageDigestUtil.getCipher(ALGORITHM_DES, Cipher.DECRYPT_MODE, salt);
            final byte[] decodeBase64Bytes = Base64.decodeBase64(encodeText.getBytes(StandardCharsets.UTF_8));
            final byte[] decodeBytes = cipher.doFinal(decodeBase64Bytes);
            return new String(decodeBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
