package cn.cloud9.server.struct.util.encrypt;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月27日 上午 08:57
 */
public class RsaUtil {
    private static final String ALGORITHM_RSA = "RSA";
    private static final String publicKeyPath = "./jwt/base64-pub.key";
    private static final String privateKetPath = "./jwt/base64-pri.key";

    /**
     * RSA单词最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA单词最大解密明文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * RSA加密
     * @param plainText 明文内容
     * @param key 私钥对象
     * @return RSA密文
     */
    @SneakyThrows
    public static String rsaEncrypt(String plainText, Key key) {
        final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        final byte[] encodeBytes = doCodec(cipher, plainText.getBytes(StandardCharsets.UTF_8), MAX_ENCRYPT_BLOCK);
        return Base64.encodeBase64String(encodeBytes);
    }

    /**
     * RSA解密
     * @param encryptText RSA密文内容
     * @param key 公钥对象
     * @return 明文内容
     */
    @SneakyThrows
    public static String rsaDecrypt(String encryptText, Key key) {
        final byte[] bytes = Base64.decodeBase64(encryptText);
        final Cipher cipher = Cipher.getInstance(ALGORITHM_RSA);
        cipher.init(Cipher.DECRYPT_MODE, key);
        final byte[] doCodecBytes = doCodec(cipher, bytes, MAX_DECRYPT_BLOCK);
        return new String(doCodecBytes, StandardCharsets.UTF_8);
    }

    @SneakyThrows
    public static byte[] doCodec(Cipher cipher, byte[] bytes, int blockMaxSize) {
        int length = bytes.length, offset = 0, idx = 0;
        byte[] cache;
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((length - offset) > 0) {
            cache = cipher.doFinal(bytes, offset, Math.min((length - offset), blockMaxSize));
            idx ++;
            baos.write(cache, 0, cache.length);
            offset = idx * blockMaxSize;
        }
        final byte[] codecBytes = baos.toByteArray();
        baos.close();
        return codecBytes;
    }

    /**
     * 创建密钥对文件
     */
    @SneakyThrows
    public static void createRsaKeyPairFile() {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        keyPairGenerator.initialize(1024);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();

        final PublicKey publicKey = keyPair.getPublic();
        final byte[] publicKeyEncoded = publicKey.getEncoded();
        final String encodeBase64Public = Base64.encodeBase64String(publicKeyEncoded);

        final PrivateKey privateKey = keyPair.getPrivate();
        final byte[] privateKeyEncoded = privateKey.getEncoded();
        final String encodeBase64Private = Base64.encodeBase64String(privateKeyEncoded);

        /* 写入指定文件中 */
        FileUtils.writeStringToFile(new File(publicKeyPath), encodeBase64Public, StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(privateKetPath), encodeBase64Private, StandardCharsets.UTF_8);
    }

    /**
     * 创建公钥对象
     * @return 公钥对象
     */
    public static PublicKey getPublicKey() {
        try {
            final String base64Str = FileUtils.readFileToString(new File(publicKeyPath), StandardCharsets.UTF_8);
            final byte[] decodeBase64 = Base64.decodeBase64(base64Str);

            /* 配置X509的公钥规则 */
            final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decodeBase64);
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);

            /* 创建 */
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建私钥对象
     * @return 私钥对象
     */
    public static PrivateKey getPrivateKey() {
        try {
            final String base64Str = FileUtils.readFileToString(new File(privateKetPath), StandardCharsets.UTF_8);
            final byte[] decodeBase64 = Base64.decodeBase64(base64Str);

            /* 配置PKCS8的私钥规则 */
            final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodeBase64);
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_RSA);

            /* 创建 */
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
