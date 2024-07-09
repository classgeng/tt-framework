package cn.cloud9.server.struct.masking.enums;

import cn.hutool.core.util.DesensitizedUtil;
import lombok.Getter;

import java.util.function.Function;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月26日 下午 11:24
 */
@Getter
public enum MaskingType {
    NONE("不脱敏", data -> data),
    ID_CARD("身份证", MaskingType::maskingIdCard),
    PHONE("手机号", DesensitizedUtil::mobilePhone),
    NAME("姓名", DesensitizedUtil::chineseName),
    ADDRESS("地址", MaskingType::maskingAddress),
    EMAIL("邮箱", DesensitizedUtil::email),
    LICENSE_PLATE("车牌号", DesensitizedUtil::carLicense),
    PASSWORD("密码", DesensitizedUtil::password),
    BANKCARD("银行卡", DesensitizedUtil::bankCard),
   ;

    private final String define;
    private final Function<String, String> maskingFunc;

    MaskingType(String define, Function<String, String> maskingFunc) {
        this.define = define;
        this.maskingFunc = maskingFunc;
    }

    public static String maskingIdCard(String idCardNo) {
        return DesensitizedUtil.idCardNum(idCardNo, 4, 4);
    }

    public static String maskingAddress(String address) {
        return DesensitizedUtil.address(address, 8);
    }
}
