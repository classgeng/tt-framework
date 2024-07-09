package cn.cloud9.server.struct.dict.annotation;

import java.lang.annotation.*;

/**
 * 禁用字典翻译标记
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface DisableTranslate {
}
