package cn.cloud9.server.test.controller;

import cn.cloud9.server.test.strategy.ServiceFlag;
import cn.cloud9.server.test.strategy.StrategyUtil;
import cn.cloud9.server.test.strategy.TestStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/strategy")
public class StrategyController {

    private static Map<String, TestStrategy> strategyMap;

    /**
     * qualifier用法 https://juejin.cn/post/6959759591835959326
     * @param strategyList
     */
    public StrategyController(@Qualifier List<TestStrategy> strategyList) {
        strategyMap = StrategyUtil.getStrategyMap(strategyList, ServiceFlag.class, ServiceFlag::flagName);
    }

    /**
     * strategy/exec
     * @param key Bean标识
     * @return String
     */
    @GetMapping("/exec")
    public String executeStrategy(@RequestParam("key") String key) {
        log.info("strategyMap {}", strategyMap);
        TestStrategy strategy = StrategyUtil.getStrategyByKey(strategyMap, key, "未能查找到此策略Bean! flag");
        return strategy.strategyMethod();
    }

}
