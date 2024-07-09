package cn.cloud9.server.struct.encrypt.annotation;

import java.lang.annotation.*;

/**
 * @author OnCloud9
 * @description 接口加密标识注解
 * @project tt-server
 * @date 2022年11月22日 下午 09:44
 */
@Target({ElementType.METHOD , ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProduceEncrypt {
}
