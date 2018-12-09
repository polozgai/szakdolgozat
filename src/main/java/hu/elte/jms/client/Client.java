package hu.elte.jms.client;


import hu.elte.algorithm.Algorithm;
import hu.elte.graph.Vertex;
import hu.elte.jms.consumer.Consumer;
import hu.elte.jms.producer.Producer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Set;


public class Client implements MessageListener {

    private Vertex vertex;
    private Consumer consumer;
    private Producer producer;


    public Client(Vertex vertex){
        this.vertex=vertex;
        this.consumer=new Consumer(vertex.getName());
        this.producer=new Producer(vertex.getName());
    }

    @Override
    public synchronized void onMessage(Message message) {
        TextMessage textMessage=(TextMessage) message;
        try {
            String text=textMessage.getText();
            JSONParser parser=new JSONParser();
            JSONObject object=(JSONObject) parser.parse(text);
            fullMessageProcessing(object);
            Algorithm.getMessages().add(object.toString());
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void createQueue() {
        consumer.create(vertex.getName());
        try {
            consumer.getMessageConsumer().setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


    private void fullMessageProcessing(JSONObject message){
        String msg= (String) message.get("message");
        String producerName= (String) message.get("producerName");
        JSONParser parser=new JSONParser();
        try {
            JSONObject object=(JSONObject) parser.parse(msg);
            Set<String> set=object.keySet();
            String key="";
            String value="";
            for(String i:set){
                key=i;
            }
            value= object.get(key).toString();
            partMessageProcessing(key,value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void partMessageProcessing(String key,String value){
        switch (key){
            case "SEND_ROUTES": vertex.processRoutes(value);
                                break;
        }
    }


}
