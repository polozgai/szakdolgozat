package hu.elte.jms.message;

import hu.elte.algorithm.Algorithm;

import java.io.Serializable;


public class AlgorithmMessage implements Serializable {

    private String producerName;
    private String text;
    private String consumerName;

    private AlgorithmMessage(String producerName, String text,String consumerName){
        this.producerName=producerName;
        this.text=text;
        this.consumerName=consumerName;
    }

    public static String create(String producerName,String text,String consumerName){
        AlgorithmMessage algorithmMessage=new AlgorithmMessage(producerName,text,consumerName);
        return algorithmMessage.toString();
    }

    public String toString(){
        return producerName+"->"+consumerName+":"+text;
    }

}
