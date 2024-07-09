package cn.cloud9.encrpt;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 上午 10:43
 */
@Slf4j
public class UrlTest {

    @Test
    public void demo1() {
        String plainText = "OnCloud9, 明文测试";
        log.info("明文：{}", plainText);
        try {
            final String encode = URLEncoder.encode(plainText, StandardCharsets.UTF_8.name());
            log.info("Url编码后：{}", encode);

            final String decode = URLDecoder.decode(encode, StandardCharsets.UTF_8.name());
            log.info("Url解码后：{}", decode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
