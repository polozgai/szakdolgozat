package hu.elte.jms.engine;


import hu.elte.graph.Vertex;
import hu.elte.jms.consumer.Consumer;
import hu.elte.jms.producer.Producer;

import java.util.LinkedList;
import java.util.List;


public class Client {

    private int id;
    private Vertex vertex;
    private Consumer consumer;
    private Producer producer;
    private List<Client> neighbours;


    public Client(int id, Vertex vertex){
        this.id=id;
        this.vertex=vertex;
        this.consumer=new Consumer(vertex.getName());
        this.producer=new Producer(vertex.getName());
        this.neighbours=new LinkedList<>();
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setVertex(Vertex vertex){
        this.vertex=vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public String toSting(){
        return vertex.getName();
    }

    public void createQueues() {
        consumer.create(vertex.getName());
    }

}
