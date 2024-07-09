package cn.cloud9.server.struct.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Cors跨域访问配置
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 05:55
 */
@Configuration
public class CorsConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity security) throws Exception {
        security.authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().cors().configurationSource(corsFilter())
                /* 允许iframe访问本服务资源 */
                .and().headers().frameOptions().disable()
                /* 关闭CSRF攻击阻挡 */
                .and().csrf().disable();
    }

    private CorsConfigurationSource corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedHeader(CorsConfiguration.ALL);
        config.setAllowCredentials(true);
        config.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return configSource;
    }


}
