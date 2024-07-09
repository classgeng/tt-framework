package cn.cloud9.server.struct.response;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.lang.reflect.AnnotatedElement;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:36
 */
@ControllerAdvice(annotations = {RestController.class})
@Order(99)
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @SuppressWarnings("all")
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        final AnnotatedElement ae = methodParameter.getAnnotatedElement();
        return null == ae || null == ae.getAnnotation(NoApiResult.class);
    }

    @SuppressWarnings("all")
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter mp, MediaType mt, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        final HttpHeaders headers = response.getHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

        boolean isResultType = body instanceof ApiResult;
        if (isResultType) return body;
        else if(body instanceof SseEmitter) {
            headers.setContentType(MediaType.TEXT_EVENT_STREAM);
            return body;
        } else if (body instanceof Void) return body;
        else return ApiResult.success(body);
    }
}
