package hu.elte.jms.message;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class MessageQueue {

    private PriorityQueue messages;

    public MessageQueue(){
        this.messages=new PriorityQueue();
    }

    public PriorityQueue getMessages(){
        return messages;
    }


}
