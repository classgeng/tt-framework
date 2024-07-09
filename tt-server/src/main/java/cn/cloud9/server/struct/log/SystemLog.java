package cn.cloud9.server.struct.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 操作日志标记注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemLog {
    /* 业务类型 */
    BusinessType businessType() default BusinessType.QUERY;
    /* 模块，功能描述 */
    String module();
}
