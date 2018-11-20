package hu.elte.jms.producer;

import hu.elte.jms.consumer.Consumer;
import hu.elte.jms.message.AlgorithmMessage;
import org.junit.Before;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProducerTest {

    private Producer producer;
    private Consumer consumer;
    private String consumerName;
    private String producerName;

    @Before
    public void setUp(){
        producerName="producer";
        consumerName="test";
        consumer=new Consumer(consumerName);
        producer=new Producer(producerName);
    }

    @Test
    public void send() {
        consumer.create(consumerName);
        String other= AlgorithmMessage.create(producerName,"",consumerName);
        String[] msg = {""};
        try {
            consumer.getMessageConsumer().setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    TextMessage textMessage=(TextMessage) message;
                    try {
                        msg[0] = textMessage.getText();
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
        producer.send("",consumerName);
        consumer.close();
        System.out.println(msg[0]);
        assertEquals(msg[0],other);
    }

    @Test (expected = IllegalArgumentException.class)
    public void sendTestException(){
        producer.send("","");
    }
}