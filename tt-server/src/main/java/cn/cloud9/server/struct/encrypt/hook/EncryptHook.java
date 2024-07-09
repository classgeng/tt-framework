package cn.cloud9.server.struct.encrypt.hook;

import cn.cloud9.server.struct.encrypt.annotation.ProduceEncrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author OnCloud9
 * @description 接口响应加密工具
 * @project tt-server
 * @date 2022年11月22日 下午 09:42
 */
@Slf4j
@Order(98)
@ControllerAdvice(annotations = { RestController.class })
public class EncryptHook implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        final Class<ProduceEncrypt> peClass = ProduceEncrypt.class;

        /* 1、判断是否在类上标记 */
        ProduceEncrypt produceEncrypt = methodParameter.getContainingClass().getAnnotation(peClass);
        boolean isMarkOnClass = Objects.nonNull(produceEncrypt);

        /* 2、判断是否在方法上标记 */
        produceEncrypt = methodParameter.getMethod().getAnnotation(peClass);
        boolean isMarkOnMethod = Objects.nonNull(produceEncrypt);

        /* 3、只要在类或者方法上标记，则表示使用解密 */
        return isMarkOnClass || isMarkOnMethod;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {


        return body;
    }
}
