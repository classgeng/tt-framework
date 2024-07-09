package cn.cloud9.server.struct.init;

import cn.cloud9.server.struct.dict.cache.CacheManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("commandLineRunner executed ... ");
        CacheManager.refreshCache();
    }
}
