package hu.elte.jms.message;

import org.json.simple.JSONObject;


public class AlgorithmMessage {

    public static String create(String producerName,String text,String consumerName){
        JSONObject object=new JSONObject();
        object.put("producerName",producerName);
        object.put("message", text);
        object.put("consumerName" , consumerName);
        return object.toString();
    }

}
