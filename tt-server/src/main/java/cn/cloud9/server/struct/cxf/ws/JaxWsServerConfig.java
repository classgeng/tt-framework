package cn.cloud9.server.struct.cxf.ws;

import cn.cloud9.server.struct.cxf.abbr.service.JaxWsAbbrWordService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月28日 下午 09:20
 */
@Configuration
public class JaxWsServerConfig {
    /**
     * 业务总线
     */
    @Resource
    private Bus bus;

    @Resource
    private JaxWsAbbrWordService abbrWordService;

    @Bean
    public Endpoint createEndpoint() {
        final EndpointImpl endpoint = new EndpointImpl(bus, abbrWordService);
        endpoint.publish("/jax-ws/wordService");
        return endpoint;
    }
}
