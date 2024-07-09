package cn.cloud9.server.struct.authority.interceptor;

import cn.cloud9.server.struct.authority.jwt.server.AuthServerProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description 拦截器配置
 * @project tt-server
 * @date 2022年11月06日 下午 05:51
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AuthServerProperty authServerProperty;

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        final String basePath = "/" + authServerProperty.getApiPath();
        registry.addInterceptor(authInterceptor).addPathPatterns(basePath + "/**");
    }
}
