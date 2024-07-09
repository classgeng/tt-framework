package cn.cloud9.server.struct.masking.annotation;

import cn.cloud9.server.struct.masking.enums.MaskingType;
import cn.cloud9.server.struct.masking.intf.CustomMasking;

import java.lang.annotation.*;

/**
 * 标记需要脱敏的字段，
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface MaskingField {
    /* 声明脱敏的字段来源 */
    String srcField();

    /* 声明脱敏的类型, 默认无类型 */
    MaskingType maskingType() default MaskingType.NONE;

    /* 自定义脱敏接口 */
    Class<? extends CustomMasking> custom() default CustomMasking.class;

    /* 是否使用自定义 */
    boolean usingCustom() default false;

    /* 是否隐藏源数据 */
    boolean hideSrcData() default false;
}
