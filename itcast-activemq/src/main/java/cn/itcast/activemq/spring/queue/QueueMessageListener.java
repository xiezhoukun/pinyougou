package cn.itcast.activemq.spring.queue;

import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class QueueMessageListener extends AbstractAdaptableMessageListener {
    @Override
    public void onMessage(Message message, Session session) throws JMSException {
        //  判断消息类型是 TextMessage
        if (message instanceof TextMessage) {
//  如果是，则进行强转
            TextMessage textMessage = (TextMessage) message;
            try {
//  消费消息，打印消息内容
                String text = textMessage.getText();
                System.out.println("QueueMessageListener  消息监听器接收到消息；消息内容为：" + text);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
