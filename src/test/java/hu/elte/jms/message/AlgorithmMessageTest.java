package hu.elte.jms.message;

import hu.elte.algorithm.Algorithm;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlgorithmMessageTest {

    @Test
    public void create() {
        JSONObject object=new JSONObject();
        String producerName="";
        String text="";
        String consumerName="";
        object.put("producerName",producerName);
        object.put("message", text);
        object.put("consumerName" , consumerName);
        String msg=AlgorithmMessage.create("","","");
        assertEquals(msg,object.toString());
    }
}