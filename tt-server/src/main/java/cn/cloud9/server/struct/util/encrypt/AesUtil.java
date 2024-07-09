package cn.cloud9.server.struct.util.encrypt;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 11:06
 */
public class AesUtil {
    private static final String ALGORITHM_AES = "AES";
    private static final String SECURE_RANDOM_SHA1PRNG = "SHA1PRNG";
    /**
     * AES加密
     * @param plainText AES加密的明文
     * @param salt AES加密的盐
     * @return AES密文
     */
    public static String aesEncrypt(String plainText, String salt) {
        try {
            final Cipher cipher = MessageDigestUtil.getCipher(ALGORITHM_AES, Cipher.ENCRYPT_MODE, salt, SECURE_RANDOM_SHA1PRNG);
            final byte[] encodeBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return HexUtil.bytes2hex(encodeBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     * @param encodeText AES密文
     * @param salt AES加密的盐
     * @return AES加密的明文
     */
    public static String aesDecrypt(String encodeText, String salt) {
        try {
            final byte[] encodeBytes = HexUtil.hex2bytes(encodeText);
            final Cipher cipher = MessageDigestUtil.getCipher(ALGORITHM_AES, Cipher.DECRYPT_MODE, salt, SECURE_RANDOM_SHA1PRNG);
            final byte[] decodeBytes = cipher.doFinal(encodeBytes);
            return new String(decodeBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
