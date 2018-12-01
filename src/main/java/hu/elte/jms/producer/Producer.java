package hu.elte.jms.producer;


import hu.elte.jms.message.AlgorithmMessage;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Producer {

    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer messageProducer;
    private String producerName;

    public Producer(String producerName){
        this.producerName=producerName;
    }

    private void create(String queueName){
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection=activeMQConnectionFactory.createConnection();
            session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            destination=session.createQueue(queueName);
            messageProducer=session.createProducer(destination);
        }catch (JMSException e){}

    }

    private void close(){
        try {
            connection.close();
            messageProducer.close();
            session.close();
        } catch (JMSException e) { }
    }

    public void send(String message, String queueName){
        try{
            String msg=AlgorithmMessage.create(producerName,message,queueName);
            create(queueName);
            TextMessage textMessage=session.createTextMessage(msg);
            messageProducer.send(textMessage);
            close();
        }catch (JMSException e){}
    }

}
