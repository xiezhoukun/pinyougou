package cn.itcast.springboot.activemq.listener;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MessageListener {

    @JmsListener(destination = "itcast_sms_queue")
    public void receive(Map<String, Object> map) {
        System.out.println(map);
    }
}
