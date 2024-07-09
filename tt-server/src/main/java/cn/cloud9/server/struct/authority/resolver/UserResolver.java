package cn.cloud9.server.struct.authority.resolver;

import cn.cloud9.server.struct.authority.annotation.UserRequire;
import cn.cloud9.server.struct.authority.user.UserContext;
import cn.cloud9.server.struct.authority.user.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月08日 下午 11:43
 */
@Slf4j
@Component
public class UserResolver implements HandlerMethodArgumentResolver {

    /**
     * 判断是否使用这个类型，且标记了指定的注解
     * @param methodParameter 方法参数？
     * @return boolean 是否解析
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(UserRequire.class)
                && methodParameter.getParameterType().equals(UserContext.class);
    }

    /**
     * 参数解析
     * @param methodParameter 方法参数
     * @param modelAndViewContainer
     * @param nativeWebRequest
     * @param webDataBinderFactory
     * @return 解析后的参数对象
     */
    @Override
    public Object resolveArgument(
            MethodParameter methodParameter,
            ModelAndViewContainer modelAndViewContainer,
            NativeWebRequest nativeWebRequest,
            WebDataBinderFactory webDataBinderFactory
    ) {
        final UserContext userContext = UserContextHolder.getUserContext();
        return userContext;
    }
}
