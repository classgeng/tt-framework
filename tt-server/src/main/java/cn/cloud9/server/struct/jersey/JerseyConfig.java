package cn.cloud9.server.struct.jersey;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

// @Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        //构造函数，在这里注册需要使用的内容，（过滤器，拦截器，API等）

        //注册类的方式
        register(JerseyDemo.class);

        // 注册包的方式 项目打为jar包启动时，不能使用包注册的方式，否则会报FileNotFound异常
        // packages("com.demo.web");
    }
}
