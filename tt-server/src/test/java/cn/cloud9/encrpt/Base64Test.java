package cn.cloud9.encrpt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 上午 10:32
 */
@Slf4j
public class Base64Test {

    @Test
    public void demo1() {

        /* 明文 */
        String plainText = "OnCloud9,Base64测试明文";
        log.info("明文：{}", plainText);
        /* 1、转UTF-8字节数组 */
        final byte[] bytes = plainText.getBytes(StandardCharsets.UTF_8);

        /* 2、获取Base64编码器 */
        final Base64.Encoder encoder = Base64.getEncoder();

        /* 3、调用编码器将字节数组转Base64字码串 */
        final String encodedText = encoder.encodeToString(bytes);
        log.info("base64编码后：{}", encodedText);


        /* - - - - - 解码操作 - - - - - - - */
        final byte[] encodeBytes = encodedText.getBytes(StandardCharsets.UTF_8);
        /* 1、获取Base64解码器 */
        final Base64.Decoder decoder = Base64.getDecoder();
        /* 2、解码成Base64解码字节数组 */
        final byte[] decodeBytes = decoder.decode(encodeBytes);
        /* 3、用String重新构建解码字节数组 */
        final String decodedText = new String(decodeBytes, StandardCharsets.UTF_8);

        log.info("base64解码后：{}", decodedText);
    }

}
