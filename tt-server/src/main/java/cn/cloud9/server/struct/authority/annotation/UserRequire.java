package cn.cloud9.server.struct.authority.annotation;

import java.lang.annotation.*;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月08日 下午 11:52
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRequire  {
}
