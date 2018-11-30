package hu.elte.graph;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class VertexTest {

    private Vertex a;
    private Vertex b;
    private Vertex c;
    private Vertex d;
    private VertexRoute ab;
    private VertexRoute ac;
    private VertexRoute bd;
    private Graph graph;

    @Before
    public void setUp() throws Exception {
        graph=new Graph();
        a=new Vertex("a");
        b=new Vertex("b");
        c=new Vertex("c");
        d=new Vertex("d");
        ab=new VertexRoute(a,b,1.0);
        ac=new VertexRoute(a,c,2.0);
        bd=new VertexRoute(b,d,0.0);
        a.getRoutes().add(ab);
        a.getRoutes().add(ac);
        ab.getPrevious().add(d);
        b.getRoutes().add(bd);
    }

    @Test
    public void getRouteByName() {
        assertEquals(ab.getEndVertex(),a.getRouteByName("b"));
        assertEquals(ac.getEndVertex(),a.getRouteByName("c"));
        assertNull(a.getRouteByName(""));
    }

    @Test
    public void getRouteWeightByName() {
        assertEquals(ab.getDistance(),a.getRouteWeightByName("b"));
        assertEquals(ac.getDistance(),a.getRouteWeightByName("c"));
        assertNull(a.getRouteWeightByName(""));
    }

    @Test
    public void getRoutePreviousByName() {
        LinkedList<Vertex> temp=new LinkedList<>();
        temp.add(d);
        assertEquals(temp,a.getRoutePreviousByName("b"));
        assertNull(a.getRoutePreviousByName(""));
    }

    @Test
    public void routesToMessage() {
        String msg_a="{\"SEND_ROUTES\":[{\"previous\":\"[d]\",\"value\":1.0,\"key\":\"a b\"},{\"previous\":\"[]\"," +
                "\"value\":2.0,\"key\":\"a c\"}]}";
        assertEquals(msg_a,a.routesToMessage());
        String msg_c="{\"SEND_ROUTES\":[]}";
        assertEquals(msg_c,c.routesToMessage());
    }

    @Test
    public void processRoutes() {
        String msg=b.routesToMessage();
        msg=msg.substring(15,msg.length()-1);
        a.processRoutes(msg);
    }
}