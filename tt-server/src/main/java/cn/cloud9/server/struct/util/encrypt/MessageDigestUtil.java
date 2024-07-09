package cn.cloud9.server.struct.util.encrypt;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 上午 11:41
 */
@Slf4j
public class MessageDigestUtil {

    private static final String ALGORITHM_AES = "AES";


    /**
     * 消息摘要计算
     * @param plainText 明文
     * @param algorithm 摘要算法
     * @return 摘要信息
     */
    public static String doDigest(String plainText, String algorithm) {
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
            final byte[] encodeBytes = md.digest(plainTextBytes);

            /* 得到的字节数组需要按16进制进行转换 */
            return HexUtil.bytes2hex(encodeBytes);
        } catch (Exception e) {
            log.error("加密异常 {}", e.getMessage());
        }
        return null;
    }

    /**
     * MAC消息摘要计算
     * @param plainText 明文
     * @param key 摘要的密钥
     * @param algorithm 摘要算法
     * @return MAC摘要信息
     */
    public static String doMacDigest(String plainText, String key, String algorithm) {
        try {
            /* 根据指定摘要算法创建MAC对象 */
            final Mac mac = Mac.getInstance(algorithm);

            /* 创建对应密钥配置对象？ */
            final SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);

            /* mac初始化密钥配置 */
            mac.init(keySpec);

            /* 明文字节数组 */
            final byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);

            /* 摘要字节数组 */
            final byte[] bytes = mac.doFinal(plainTextBytes);

            /* 得到的字节数组需要按16进制进行转换 */
            return HexUtil.bytes2hex(bytes);
        } catch (Exception e) {
            log.error("加密异常 {}", e.getMessage());
        }
        return null;
    }

    /**
     * Cipher对象获取方法
     * @param algorithm 算法名称
     * @param cipherType 配置类型
     * @param seed 盐
     * @return Cipher对象
     */
    public static Cipher getCipher(String algorithm, int cipherType, String seed) {
        try {
            /* 创建DES加密算法对象 */
            final Cipher cipher = Cipher.getInstance(algorithm);

            /* 创建算法的密钥配置 */
            final SecretKeySpec secretKeySpec = new SecretKeySpec(seed.getBytes(StandardCharsets.UTF_8), algorithm);

            /* 初始化 */
            cipher.init(cipherType, secretKeySpec);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Cipher对象获取方法 (配置定长盐值)
     * @param algorithm 算法名称
     * @param cipherType 配置类型
     * @param seed 盐
     * @param secureRandom 安全的随机名称？
     * @return Cipher对象
     */
    public static Cipher getCipher(String algorithm, int cipherType, String seed, String secureRandom) {
        try {
            final byte[] seedBytes = seed.getBytes(StandardCharsets.UTF_8);
            /* 创建DES加密算法对象 */
            final Cipher cipher = Cipher.getInstance(algorithm);

            /* key生成器对象配置 */
            final SecureRandom sr = SecureRandom.getInstance(secureRandom);
            sr.setSeed(seedBytes);
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(128, sr);

            /* 生成器根据指定长度生成密钥 */
            final SecretKey secretKey = keyGenerator.generateKey();
            final byte[] encoded = secretKey.getEncoded();

            /* 创建算法的密钥配置 */
            final SecretKeySpec secretKeySpec = new SecretKeySpec(encoded, algorithm);

            /* 初始化 */
            cipher.init(cipherType, secretKeySpec);
            return cipher;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
