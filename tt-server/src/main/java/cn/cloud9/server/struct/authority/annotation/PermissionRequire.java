package cn.cloud9.server.struct.authority.annotation;

import java.lang.annotation.*;

/**
 * 接口权限控制
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequire {
    /**
     * 权限值
     */
    String permission();
}
