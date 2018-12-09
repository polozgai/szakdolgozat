package hu.elte.jms.consumer;

import hu.elte.jms.message.AlgorithmMessage;
import hu.elte.jms.producer.Producer;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.*;

public class ConsumerTest {
    

    private Consumer consumer;
    private String consumerName;
    private String producerName;
    private Producer producer;
    private BrokerService brokerService;

    @Before
    public void setUp() throws Exception {
        brokerService=new BrokerService();
        brokerService.addConnector("tcp://localhost:61616");
        brokerService.start();
        consumerName="consumer";
        producerName="producer";
        consumer=new Consumer(consumerName);
        producer=new Producer(producerName);
    }

    @After
    public void tearDown() throws Exception {
        brokerService.stop();
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