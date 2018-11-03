package hu.elte.jms.consumer;

import hu.elte.algorithm.Algorithm;
import hu.elte.jms.engine.Client;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer /*implements MessageListener*/{

    private Connection connection;
    private Session session;
    public MessageConsumer messageConsumer;
    private String consumerName;
    public MessageListener messageListener;

    public Consumer(String consumerName){
        this.consumerName=consumerName;
    }

    public void create(String queueName){
        try {
            ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://localhost:61616");
            connection=activeMQConnectionFactory.createConnection();
            session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
            Destination destination=session.createQueue(queueName);
            messageConsumer=session.createConsumer(destination);
            //messageConsumer.setMessageListener(messageListener);
            connection.start();
        }catch (JMSException e){ }
    }

    public void close(){
        try {
            connection.close();
            session.close();
            messageConsumer.close();
        } catch (JMSException e) {

        }
    }

   /* @Override
    public void onMessage(Message message) {
        TextMessage textMessage=(TextMessage) message;
        try {
            Algorithm.getMessages(textMessage.getText());
            Client.getMessages().add(textMessage.getText());
            System.out.println(textMessage.getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }*/


    public MessageConsumer getMessageConsumer() {
        return messageConsumer;
    }
}
