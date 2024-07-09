package cn.cloud9.server.struct.cxf.rs;

import cn.cloud9.server.struct.cxf.abbr.service.JaxRsAbbrWordService;
import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月28日 下午 09:20
 */
@Configuration
public class JaxRsServerConfig {

    @Resource
    Bus bus;

    @Resource
    private JaxRsAbbrWordService abbrWordService;

    @Bean
    public Server createServer() {
        final JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setAddress("/jax-rs/wordService");
        factoryBean.setBus(bus);
        factoryBean.setServiceBean(abbrWordService);
        return factoryBean.create();
    }
}
