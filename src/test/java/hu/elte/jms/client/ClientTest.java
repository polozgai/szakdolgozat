package hu.elte.jms.client;

import hu.elte.graph.Edge;
import hu.elte.graph.Graph;
import hu.elte.graph.Vertex;
import org.junit.Before;
import org.junit.Test;

import javax.jms.*;

import static org.junit.Assert.*;

public class ClientTest {

    private Vertex a;
    private Vertex b;
    private Vertex c;
    private Graph graph;
    private Client client_a;
    private Client client_b;
    private Edge a_b;
    private Edge a_c;

    @Before
    public void setUp() throws Exception {
        graph=new Graph();
        a=new Vertex("a");
        b=new Vertex("b");
        c=new Vertex("c");
        a_b=new Edge(1.0);
        a_c=new Edge(2.0);
        client_a=new Client(0,a);
        client_b=new Client(1,b);
        graph.addVertex(a,b);
        graph.addVertex(c,b);
        graph.addEdge(a_b,a,b);
        graph.addEdge(a_c,a,c);
    }


    @Test
    public void onMessage() {
        String msg="[b a 1.0 [], b c 3.0 [a]]";
        client_b.createQueue();
        client_a.getProducer().send(a.routesToMessage(),b.getName());
        client_b.getConsumer().close();
        assertEquals(msg,b.getRoutes().toString());
    }

    @Test
    public void createQueue() {
        client_a.createQueue();
        try {
            assertNotNull(client_a.getConsumer().getMessageConsumer().getMessageListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = NullPointerException.class)
    public void createQueueException(){
        try {
            client_a.getConsumer().getMessageConsumer().getMessageListener();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}