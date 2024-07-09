package cn.cloud9.server.test.controller;

import cn.cloud9.server.struct.common.BaseController;
import cn.cloud9.server.struct.response.NoApiResult;
import cn.cloud9.server.struct.spring.SpringContextHolder;
import cn.cloud9.server.struct.util.IpUtil;
import cn.cloud9.server.test.model.CaptchaParam;
import com.wf.captcha.SpecCaptcha;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author OnCloud9
 * @description 验证码测试类
 * @project tt-server
 * @date 2022年11月07日 下午 09:11
 */
/* 内嵌了Spring静态资源获取，不要立即加载这个Bean */
@Lazy
@RestController
@RequestMapping("/test/captcha")
public class CaptchaController extends BaseController {
    private static final String captchaKey = "CAPTCHA";
    private static final String separator = "@";
    private static final StringRedisTemplate redisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
    private static final ValueOperations<String, String> valOps = redisTemplate.opsForValue();

    /**
     * 获取验证码图片
     * @param param 验证码生成参数对象
     * @throws IOException IO异常
     */
    @NoApiResult
    @GetMapping("get")
    public void createCaptcha(@ModelAttribute CaptchaParam param) throws IOException {
        /* 三个参数分别为宽、高、位数 */
        SpecCaptcha specCaptcha = new SpecCaptcha(param.getWidth(), param.getHeight(), param.getLength());
        /* 设置字体 */
        specCaptcha.setFont(new Font(param.getFontName(), param.getFontStyle(), param.getFontSize()));  // 有默认字体，可以不用设置
        /* 设置类型，纯数字、纯字母、字母数字混合 */
        specCaptcha.setCharType(param.getCharType());

        /* 设置验证码 */
        final String text = specCaptcha.text().toUpperCase();
        final String ipAddr = IpUtil.getIpAddr(request);
        final String key = captchaKey + separator + ipAddr;
        final boolean isUseRedis = param.isUseRedis();
        if (isUseRedis)  valOps.set(key, text, 5 * 60, TimeUnit.SECONDS);
        else request.getSession().setAttribute(key, text);

        /* 输出验证码图片 */
        final boolean isBase64 = param.isBase64();
        if (isBase64) {
            final String base64 = specCaptcha.toBase64();
            response.getWriter().write(base64);
        } else {
            /* 设置请求头为输出图片类型 */
            response.setContentType("image/gif");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            /* 输出验证码图片 */
            specCaptcha.out(response.getOutputStream());
        }
    }

    /**
     * 验证图片验证码 （可以是验证码失效，错误，这里简单判断为true和false）
     * @param verifyCode 图片验证码
     * @param useRedis 是否使用Redis缓存
     * @return 验证结果
     */
    @PostMapping("verify")
    public boolean verifyCaptcha(@RequestParam String verifyCode, @RequestParam(required = false) boolean useRedis) {
        String storeCode = null;
        final String ipAddr = IpUtil.getIpAddr(request);
        final String key = captchaKey + separator + ipAddr;
        if (useRedis) {
            storeCode = valOps.get(key);
        } else {
            final Object attribute = request.getSession().getAttribute(key);
            storeCode = Objects.isNull(attribute) ? "" : String.valueOf(attribute);
        }
        return verifyCode.equalsIgnoreCase(storeCode);
    }
}
