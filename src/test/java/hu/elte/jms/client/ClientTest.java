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
    private Client clientA;
    private Client clientB;
    private Edge aToB;
    private Edge aToC;

    @Before
    public void setUp() throws Exception {
        graph=new Graph();
        a=new Vertex("a");
        b=new Vertex("b");
        c=new Vertex("c");
        aToB =new Edge(1.0);
        aToC =new Edge(2.0);
        clientA =new Client(a);
        clientB =new Client(b);
        graph.addEdge(aToB,a,b);
        graph.addEdge(aToC,a,c);
    }


    @Test
    public void onMessage() {
        String msg="[b a 1.0 [], b c 3.0 [a]]";
        clientB.createQueue();
        clientA.getProducer().send(a.routesToMessage(),b.getName());
        clientB.getConsumer().close();
        assertEquals(msg,b.getRoutes().toString());
    }

    @Test
    public void createQueue() {
        clientA.createQueue();
        try {
            assertNotNull(clientA.getConsumer().getMessageConsumer().getMessageListener());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Test (expected = NullPointerException.class)
    public void createQueueException(){
        try {
            clientA.getConsumer().getMessageConsumer().getMessageListener();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}