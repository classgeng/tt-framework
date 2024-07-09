package cn.cloud9.server.test.model;

import com.wf.captcha.base.Captcha;
import lombok.Data;

import java.awt.*;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月25日 下午 08:50
 */
@Data
public class CaptchaParam {

    /* 设置宽度，高度，码数长度 */
    private int width = 200;
    private int height = 80;
    private int length = 6;

    /* 字体设置 */
    private String fontName = "Verdana";
    private int fontStyle = Font.PLAIN;
    private int fontSize = 32;

    /**
     * 验证码字符类型设置
     * TYPE_DEFAULT = 1;
     * TYPE_ONLY_NUMBER = 2;
     * TYPE_ONLY_CHAR = 3;
     * TYPE_ONLY_UPPER = 4;
     * TYPE_ONLY_LOWER = 5;
     * TYPE_NUM_AND_UPPER = 6;
     */
    private int charType = Captcha.TYPE_ONLY_NUMBER;

    /* 是否使用base64 默认不使用 */
    private boolean base64 = false;

    /* 是否使用Redis 默认不使用 */
    private boolean useRedis = false;
}
