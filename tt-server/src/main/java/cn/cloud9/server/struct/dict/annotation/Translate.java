package cn.cloud9.server.struct.dict.annotation;

import java.lang.annotation.*;

/**
 * 标记此注解时翻译PO对象
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Translate {
}
