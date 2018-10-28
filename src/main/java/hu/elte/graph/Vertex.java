package hu.elte.graph;


import java.util.*;

public class Vertex {

    private String name;
    private VertexState state;
    private List<Vertex> neighbours=new LinkedList<>();
    private Map<Vertex,Edge> edges=new HashMap<>();
    private List<Edge> cuttedEdges=new LinkedList<>();
    private Vertex parent=null;


    public Vertex(String name){
        this.name=name;
        this.state=VertexState.PASSIVE;
    }


    public void setParent(Vertex parent) {
        this.parent = parent;
    }

    public Vertex getParent() {
        return parent;
    }

    public void setState(VertexState state) {
        this.state = state;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String toString(){
        return name;
    }

    public VertexState getState() {
        return state;
    }

    public List<Vertex> getNeighbours(){
        return neighbours;
    }

    public Map<Vertex, Edge> getEdges(){
        return edges;
    }
    
    public List<Edge> getCuttedEdges(){
        return cuttedEdges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return name.equals(vertex.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
