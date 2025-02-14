package cn.cloud9.server.struct.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitializingBeanImpl implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("initializingBean executed ... ");
    }
}
