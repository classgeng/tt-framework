package cn.cloud9.server.struct.authority.annotation;

import java.lang.annotation.*;

/**
 * 标记该接口不需要授权即可使用
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLoginRequire {
}
