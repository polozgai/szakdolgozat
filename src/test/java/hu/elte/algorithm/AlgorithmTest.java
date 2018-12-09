package hu.elte.algorithm;

import hu.elte.graph.Edge;
import hu.elte.graph.Graph;
import hu.elte.graph.Vertex;
import org.apache.activemq.broker.BrokerService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AlgorithmTest {

    @Test
    public void computeAlgorithm() {
        BrokerService brokerService=new BrokerService();
        try {
            brokerService.addConnector("tcp://localhost:61616");
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Algorithm algorithm=new Algorithm();
        algorithm.computeAlgorithm("a","c",false);
        String msg="[a, b, c] 2.0";
        try {
            brokerService.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(msg,algorithm.getMinRouteWithWeight());
    }
}