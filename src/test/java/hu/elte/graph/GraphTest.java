package hu.elte.graph;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {

    private Graph graph;
    private Vertex a;
    private Vertex b;
    private Vertex c;
    private Edge edge;

    @Before
    public void setUp(){
        graph=new Graph();
        a=new Vertex("a");
        b=new Vertex("b");
        c=new Vertex("c");
        edge=new Edge(1);
    }


    @Test
    public void addEdge() {
        graph.addEdge(edge,a,b);
        assertTrue(graph.getVertices().get(graph.getVertices().indexOf(a)).getRoutes().size()>0);
        assertTrue(graph.getVertices().get(graph.getVertices().indexOf(b)).getRoutes().size()>0);
        VertexRoute route=new VertexRoute(a,b,edge.getWeight());
        assertTrue(graph.getVertices().get(graph.getVertices().indexOf(a)).getRoutes().getFirst().equals(route));
    }

    @Test
    public void getVertexByName() {
        graph.addEdge(edge,a,b);
        Vertex v=Graph.getVertexByName("a");
        assertNotNull(v);
        Vertex v1=Graph.getVertexByName("");
        assertNull(v1);
    }
}