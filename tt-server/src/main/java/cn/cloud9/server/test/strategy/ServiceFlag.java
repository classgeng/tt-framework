package cn.cloud9.server.test.strategy;

import cn.cloud9.server.struct.enums.state.ServiceIdent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceFlag {
    String flagName();
    ServiceIdent serviceIdent() default ServiceIdent.SERVICE_B;
}
