package cn.cloud9.server.struct.event;

import org.springframework.context.ApplicationEvent;


public class TestEvent extends ApplicationEvent {

    public TestEvent(Object source) {
        super(source);
    }

    public String message;
}
