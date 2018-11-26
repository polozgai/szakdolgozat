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

    private int id;
    private Vertex vertex;
    private Consumer consumer;
    private Producer producer;


    public Client(int id, Vertex vertex){
        this.id=id;
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

            Algorithm.getMessages(textMessage.getText());
            //System.out.println(textMessage.getText());
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
        System.out.println(message.toString());
        String msg= (String) message.get("message");
        String producerName= (String) message.get("producerName");
        //vertex.deleteFromNeighbour(producerName);
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
            //System.out.println(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void partMessageProcessing(String key,String value){
        switch (key){
            case "SEND_ROUTES": //System.out.println(value);
                                vertex.processRoutes(value);
                                break;
        }
    }


}
