package hu.elte.jms.consumer;

import hu.elte.graph.Vertex;
import hu.elte.jms.engine.Client;
import hu.elte.jms.message.AlgorithmMessage;
import hu.elte.jms.producer.Producer;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import java.util.Set;

import static org.junit.Assert.*;

public class ConsumerTest {
    

    private Consumer consumer;
    private String consumerName;
    private String producerName;
    private Producer producer;

    @Before
    public void setUp(){
        consumerName="consumer";
        producerName="producer";
        consumer=new Consumer(consumerName);
        producer=new Producer(producerName);
    }

    @Test
    public void create() {
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
        assertEquals(msg[0],other);
    }

    @Test
    public void close() {
        consumer.create(consumerName);
        try {
            assertNull(consumer.getMessageConsumer().getMessageListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    
}