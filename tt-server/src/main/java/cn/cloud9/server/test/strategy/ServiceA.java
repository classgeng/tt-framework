package cn.cloud9.server.test.strategy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier
@ServiceFlag(flagName = "a")
@Service("serviceA")
public class ServiceA implements TestStrategy {
    @Override
    public String strategyMethod() {
        return "ServiceA exec";
    }
}
