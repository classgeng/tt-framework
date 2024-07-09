package cn.cloud9.server;

import cn.cloud9.server.struct.validator.EnableFormValidator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author OnCloud9
 * @description
 * @project tt-server
 * @date 2022年11月06日 下午 04:18
 */
@Slf4j
@EnableTransactionManagement
@MapperScan(basePackages = {
        "cn.cloud9.server.struct",
        "cn.cloud9.server.system",
        "cn.cloud9.server.tool",
})
@EnableFormValidator
@SpringBootApplication
public class MainApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext cac = SpringApplication.run(MainApplication.class, args);
        log.info("ConfigurableApplicationContext {}", cac);
    }
}
