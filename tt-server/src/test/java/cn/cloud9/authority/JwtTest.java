package cn.cloud9.authority;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月20日 上午 09:58
 */
@Slf4j
public class JwtTest {

    /**
     * JWT使用样例
     */
    @Test
    public void createSimpleJwtToken() {
        /* 1、生成Token  */
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "none"); /* 不设置签名算法 */
        headerMap.put("typ", "JWT");  /* 令牌类型为JWT */

        /* 封装Body信息 */
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("userId", "1001");
        bodyMap.put("account", "admin");
        bodyMap.put("role", "administrator");

        final String uuid = UUID.randomUUID(true).toString();

        final String token = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(bodyMap)
                .setId(uuid)
                .compact();

        log.info("token生成 : {}", token);

        /* 2、解析Token */
        final Jwt parse = Jwts.parser().parse(token);
        final Header header = parse.getHeader();
        final Object body = parse.getBody();
        log.info("token解析 header信息 : {}", JSON.toJSONString(header));
        log.info("token解析 body信息 : {}", JSON.toJSONString(body));
    }

    /**
     * 使用HS256加密
     */
    @Test
    public void createSimpleJwtTokenUsingHS256() {

        /* 1、生成Token  */
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", SignatureAlgorithm.HS256.getValue()); /* 设置签名算法是HS256 */
        headerMap.put("typ", "JWT");  /* 令牌类型为JWT */

        /* 封装Body信息 */
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("userId", "1001");
        bodyMap.put("account", "admin");
        bodyMap.put("role", "administrator");

        final String uuid = UUID.randomUUID(true).toString();

        final String signKey = "this is hs256 key";

        final String token = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(bodyMap)
                .setId(uuid)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .compact();

        log.info("token生成 : {}", token);

        /* 2、解析Token */
        final Jwt parse = Jwts
                .parser()
                /* 解析时提供对应的密钥 */
                .setSigningKey(signKey)
                .parse(token);
        final Header header = parse.getHeader();
        final Object body = parse.getBody();
        log.info("token解析 header信息 : {}", JSON.toJSONString(header));
        log.info("token解析 body信息 : {}", JSON.toJSONString(body));
    }


    /**
     * 创建一对公钥私钥 用于RS256算法
     */
    @Test
    @SneakyThrows
    public void createRS256Keys() {
        /* 自定义随机密码 */
        String password = "OnCloud9";

        /* 创建生成器对象 */
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        final SecureRandom secureRandom = new SecureRandom(password.getBytes(StandardCharsets.UTF_8));
        generator.initialize(1024, secureRandom);
        final KeyPair keyPair = generator.genKeyPair();

        /* 私钥字节串 */
        final byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        /* 公钥字节串 */
        final byte[] publicKeyBytes = keyPair.getPublic().getEncoded();

        /* 创建目录 */
        String storeDirectory = "C:\\Users\\Administrator\\Desktop\\RS-256-KEYS";
        final File storeDir = new File(storeDirectory);
        final boolean exists = storeDir.exists();
        if (!exists) storeDir.mkdirs();

        /* 写入目标文件中 */
        FileUtils.writeByteArrayToFile(new File(storeDir, "pri.key"), privateKeyBytes);
        FileUtils.writeByteArrayToFile(new File(storeDir, "pub.key"), publicKeyBytes);
    }

    /**
     * 默认放在/resource/keys下面
     */
    @Test
    @SneakyThrows
    public void createSimpleJwtTokenUsingRS256() {
        /* 读取密钥文件 */
        final ClassLoader classLoader = this.getClass().getClassLoader();
        final InputStream priKeyStream = classLoader.getResourceAsStream("./jwt/pri.key");
        final InputStream pubKeyStream = classLoader.getResourceAsStream("./jwt/pub.key");

        final byte[] priKeyBytes = new byte[priKeyStream.available()];
        final byte[] pubKeyBytes = new byte[pubKeyStream.available()];

        final DataInputStream priDis = new DataInputStream(priKeyStream);
        final DataInputStream pubDis = new DataInputStream(pubKeyStream);
        priDis.readFully(priKeyBytes);
        pubDis.readFully(pubKeyBytes);


        /* 生成私钥对象 */
        final PKCS8EncodedKeySpec priKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
        final KeyFactory priFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = priFactory.generatePrivate(priKeySpec);

        /* 生成公钥对象 */
        final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
        final KeyFactory pubFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = pubFactory.generatePublic(pubKeySpec);

        /* 1、生成Token  */
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", SignatureAlgorithm.RS256.getValue());
        headerMap.put("typ", "JWT");  /* 令牌类型为JWT */

        /* 封装Body信息 */
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("userId", "1001");
        bodyMap.put("account", "admin");
        bodyMap.put("role", "administrator");

        final String uuid = UUID.randomUUID(true).toString();

        final String token = Jwts.builder()
                .setHeader(headerMap)
                .setClaims(bodyMap)
                .setId(uuid)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();

        log.info("token生成 : {}", token);

        /* 2、解析Token */
        final Jwt parse = Jwts.parser()
                .setSigningKey(publicKey)
                .parse(token);
        final Header header = parse.getHeader();
        final Object body = parse.getBody();
        log.info("token解析 header信息 : {}", JSON.toJSONString(header));
        log.info("token解析 body信息 : {}", JSON.toJSONString(body));
    }

}
