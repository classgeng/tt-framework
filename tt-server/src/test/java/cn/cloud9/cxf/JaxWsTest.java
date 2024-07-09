package cn.cloud9.cxf;

import cn.cloud9.server.struct.cxf.abbr.AbbrWordDTO;
import cn.cloud9.server.struct.cxf.abbr.service.JaxWsAbbrWordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Test;

import java.util.List;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月28日 下午 10:07
 */
@Slf4j
public class JaxWsTest {
    @Test
    public void wsClient() {
        final JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
        factoryBean.setAddress("http://localhost:8080/demo/services/jax-ws/wordService");
        factoryBean.setServiceClass(JaxWsAbbrWordService.class);
        final Object o = factoryBean.create();
        JaxWsAbbrWordService abbrWordService = (JaxWsAbbrWordService) o;


        final List<AbbrWordDTO> words = abbrWordService.listWords();
        for (AbbrWordDTO word : words) {
            log.info("word: {}", word);
        }

    }
}
