package hu.elte.jms.consumer;

import hu.elte.Main;
import hu.elte.algorithm.Algorithm;
import hu.elte.jms.engine.Client;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer implements MessageListener{

    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private String consumerName;

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
            messageConsumer.setMessageListener(this);
            connection.start();
        }catch (JMSException e){ }
    }

    public void close(){
        try {
            connection.close();
        } catch (JMSException e) {

        }
    }
    //nem kell
    public String getMessage(){
        try {
            Message message=messageConsumer.receive();
            if(message!=null){
                TextMessage textMessage=(TextMessage) message;
                return textMessage.getText();
            }
        }catch (JMSException e){}
        return "";
    }

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage=(TextMessage) message;
        try {
            Algorithm.getMessages(textMessage.getText());
            System.out.println(textMessage.getText()+" "+consumerName);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
