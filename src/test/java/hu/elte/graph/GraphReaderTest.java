package hu.elte.graph;

import org.junit.Test;

import static org.junit.Assert.*;

public class GraphReaderTest {

    @Test
    public void graphFromFileNotNull() throws Exception {
        assertNotNull(GraphReader.graphFromFile("input/graph.txt"));
    }

    @Test
    public void graphFromFileNull() throws Exception {
        assertNull(GraphReader.graphFromFile(""));
    }

}