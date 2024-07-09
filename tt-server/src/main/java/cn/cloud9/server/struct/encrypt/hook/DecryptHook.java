package cn.cloud9.server.struct.encrypt.hook;

import cn.cloud9.server.struct.dict.annotation.DisableTranslate;
import cn.cloud9.server.struct.encrypt.annotation.ConsumeDecrypt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;

/**
 * @author OnCloud9
 * @description 接口解密器
 * @project tt-server
 * @date 2022年11月22日 下午 09:39
 */
@Slf4j
@ControllerAdvice
public class DecryptHook implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        final Class<ConsumeDecrypt> cdClass = ConsumeDecrypt.class;

        /* 1、判断是否在类上标记 */
        ConsumeDecrypt consumeDecrypt = methodParameter.getContainingClass().getAnnotation(cdClass);
        boolean isMarkOnClass = Objects.nonNull(consumeDecrypt);

        /* 2、判断是否在方法上标记 */
        consumeDecrypt = methodParameter.getMethod().getAnnotation(cdClass);
        boolean isMarkOnMethod = Objects.nonNull(consumeDecrypt);

        /* 3、只要在类或者方法上标记，则表示使用解密 */
        return isMarkOnClass || isMarkOnMethod;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {


        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return body;
    }
}
