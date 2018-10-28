package hu.elte.jms.message;

import hu.elte.algorithm.Algorithm;

import java.io.Serializable;


public class AlgorithmMessage implements Serializable {

    private String producerName;
    private String text;

    private AlgorithmMessage(String producerName, String text){
        this.producerName=producerName;
        this.text=text;
    }

    public static String create(String producerName,String text){
        AlgorithmMessage algorithmMessage=new AlgorithmMessage(producerName,text);
        return algorithmMessage.toString();
    }

    public String toString(){
        return producerName+" -> "+text;
    }

}
