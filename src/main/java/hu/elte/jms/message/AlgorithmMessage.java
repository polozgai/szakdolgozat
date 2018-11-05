package hu.elte.jms.message;

import org.json.simple.JSONObject;

import java.io.Serializable;


public class AlgorithmMessage implements Serializable {

    private static JSONObject object= new JSONObject();

    public static String create(String producerName,String text,String consumerName){
        object.put("producerName",producerName);
        object.put("message", text);
        object.put("consumerName" , consumerName);
        return object.toString();
    }

    public String toString(){
        return object.toString();
    }


}
