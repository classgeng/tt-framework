package cn.cloud9.server.struct.response;

import java.lang.annotation.*;

/**
 * @author OnCloud9
 * @description 不需要Api响应格式包裹
 * @project tt-server
 * @date 2022年11月06日 下午 04:36
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface NoApiResult {
}
