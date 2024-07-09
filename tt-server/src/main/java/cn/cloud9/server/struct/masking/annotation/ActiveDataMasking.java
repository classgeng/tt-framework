package cn.cloud9.server.struct.masking.annotation;

import java.lang.annotation.*;

/**
 * 标记Controller是否开启脱敏处理
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface ActiveDataMasking {
}

