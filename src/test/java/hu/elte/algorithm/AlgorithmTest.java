package hu.elte.algorithm;

import hu.elte.graph.Edge;
import hu.elte.graph.Graph;
import hu.elte.graph.Vertex;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlgorithmTest {

    @Test
    public void computeAlgorithm() {
        Algorithm algorithm=new Algorithm();
        algorithm.computeAlgorithm("a","c",false);
        String msg="[a, b, c] 2.0";
        assertEquals(msg,algorithm.getMinRouteWithWeight());
    }
}