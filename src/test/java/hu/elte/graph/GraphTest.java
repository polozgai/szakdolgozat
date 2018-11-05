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
    public void init(){
        graph=new Graph();
        a=new Vertex("a");
        b=new Vertex("b");
        c=new Vertex("c");
        edge=new Edge(1);
    }

    @Test
    public void addVertex() {
        graph.addVertex(a,b);
        assertTrue(graph.getVerticies().size()==2);
        graph.addVertex(a,c);
        assertTrue(graph.getVerticies().size()==3);
    }

    @Test
    public void addEdge() {
        graph.addVertex(a,b);
        graph.addEdge(edge,a,b);
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(a)).getRoutes().size()>0);
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(b)).getRoutes().size()>0);
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(a)).getEdges().size()>0);
        VertexRoute route=new VertexRoute(a,b,edge.getWeight());
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(a)).getRoutes().getFirst().equals(route));
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(b)).getRoutes().getFirst().equals(route));
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(a)).getEdges().get(b).getWeight()==edge.getWeight());
        assertTrue(graph.getVerticies().get(graph.getVerticies().indexOf(b)).getEdges().get(a).getWeight()==edge.getWeight());
    }

    @Test
    public void getVertexByName() {
        graph.addVertex(a,b);
        Vertex v=Graph.getVertexByName("a");
        assertNotNull(v);
        Vertex v1=Graph.getVertexByName("");
        assertNull(v1);
    }
}