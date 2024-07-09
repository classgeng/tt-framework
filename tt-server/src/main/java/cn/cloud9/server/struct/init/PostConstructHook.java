package cn.cloud9.server.struct.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class PostConstructHook {

    @PostConstruct
    public void postConstruct() {
        log.info("postConstruct executed ... ");
    }
}
