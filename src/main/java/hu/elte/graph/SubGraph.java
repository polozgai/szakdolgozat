package hu.elte.graph;

import java.util.LinkedList;
import java.util.List;


public class SubGraph {
    
    private List<Vertex> verticies;
    
    public SubGraph(){
        verticies=new LinkedList<>();
    }
    
    public List<Vertex> getVerticies(){
        return verticies;
    }
}
