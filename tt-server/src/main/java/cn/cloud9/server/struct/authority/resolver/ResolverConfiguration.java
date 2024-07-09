package cn.cloud9.server.struct.authority.resolver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author OnCloud9
 * @description 解析器注册配置类
 * @project tt-server
 * @date 2022年11月09日 上午 12:18
 */
@Configuration
public class ResolverConfiguration implements WebMvcConfigurer {

    @Resource
    private UserResolver userResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        /* 注册用户解析器 */
        resolvers.add(userResolver);
    }
}
