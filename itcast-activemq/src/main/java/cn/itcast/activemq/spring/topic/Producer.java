package cn.itcast.activemq.spring.topic;


import org.apache.activemq.command.ActiveMQTextMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.*;

public class Producer {

    public static void main(String[] args) {
        //创建spring容器
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-activemq-topic-producer.xml");

        //从spring容器中获取JMSTemplate，这个对象是用于发送消息的
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);

        //创建消息模式
        Destination destination = (Destination) context.getBean("topicDestination");

        //使用JMSTemplate发送消息
        jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = new ActiveMQTextMessage();
                textMessage.setText("你好！黑马。--spring-topic的方式发送");
                System.out.println("已发送消息...");

                return textMessage;
            }
        });
    }
}
