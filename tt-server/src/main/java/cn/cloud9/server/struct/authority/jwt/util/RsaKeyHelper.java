package cn.cloud9.server.struct.authority.jwt.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Rsa key 帮助类
 */
public class RsaKeyHelper {

    private static final String INSTANCE_KEY = "RSA";
    /**
     * 获取公钥,用于解析token
     * @param filename 文件名称
     * @return 公钥对象
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException 没有此算法异常
     * @throws InvalidKeySpecException 非法KeySpec异常
     */
    public static PublicKey getPublicKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        /* 1、获取资源输入流 */
        InputStream resourceAsStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(filename);
        try (DataInputStream dis = new DataInputStream(resourceAsStream)) {

            /* 2、读取公钥文件内容转换为字节数组 */
            byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);

            /* 3、使用Key工厂生成公钥对象 */
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(INSTANCE_KEY);
            return kf.generatePublic(spec);
        }
    }

    /**
     * 获取密钥 用于生成token
     * @param filename 文件名称
     * @return 私钥对象
     * @throws IOException IO异常
     * @throws NoSuchAlgorithmException 没有此算法异常
     * @throws InvalidKeySpecException 非法KeySpec异常
     */
    public static PrivateKey getPrivateKey(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        /* 1、获取资源输入流 */
        InputStream resourceAsStream = RsaKeyHelper.class.getClassLoader().getResourceAsStream(filename);
        try (DataInputStream dis = new DataInputStream(resourceAsStream)) {

            /* 2、读取公钥文件内容转换为字节数组 */
            byte[] keyBytes = new byte[resourceAsStream.available()];
            dis.readFully(keyBytes);

            /* 3、使用Key工厂生成公钥对象 */
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance(INSTANCE_KEY);
            return kf.generatePrivate(spec);
        }
    }
}