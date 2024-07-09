package cn.cloud9.server.struct.api;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemApi {
    String description() default "";
}
