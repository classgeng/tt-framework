package cn.cloud9.server.test.strategy;

import cn.cloud9.server.struct.enums.state.ServiceIdent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Qualifier
@ServiceFlag(flagName = "b", serviceIdent = ServiceIdent.SERVICE_A)
@Service("serviceB")
public class ServiceB implements TestStrategy {
    @Override
    public String strategyMethod() {
        return "ServiceB exec";
    }
}

