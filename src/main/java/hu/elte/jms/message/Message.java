package hu.elte.jms.message;

import java.io.Serializable;


public class Message implements Serializable {

    private long id;
    private String text;

    private Message(long id,String text){
        this.id=id;
        this.text=text;
    }

    public static Message create(long id,String text){
        return new Message(id,text);
    }

}
