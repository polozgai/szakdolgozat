package hu.elte.graph;

import java.util.LinkedList;

public class VertexRoute {

    private Vertex startVertex;
    private Vertex endVertex;
    private Double distance;
    private LinkedList<Vertex> previous;

    public VertexRoute(Vertex startVertex, Vertex endVertex, Double distance){
        this.startVertex = startVertex;
        this.endVertex = endVertex;
        this.distance=distance;
        this.previous=new LinkedList<>();
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public LinkedList<Vertex> getPrevious() {
        return previous;
    }

    public Double getDistance() {
        return distance;
    }

    public Vertex getStartVertex(){
        return startVertex;
    }

    public Vertex getEndVertex(){
        return endVertex;
    }

    public String toString(){
        return startVertex.toString()+" " + endVertex.toString()+" "+distance+" "+previous.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexRoute that = (VertexRoute) o;

        if (!startVertex.equals(that.startVertex)) return false;
        return endVertex.equals(that.endVertex);
    }

    @Override
    public int hashCode() {
        int result = startVertex.hashCode();
        result = 31 * result + endVertex.hashCode();
        return result;
    }
}
