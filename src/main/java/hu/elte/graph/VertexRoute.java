package hu.elte.graph;

import java.util.LinkedList;

public class VertexRoute {

    private Vertex v1;
    private Vertex v2;
    private Double distance;
    private LinkedList<Vertex> previous;

    public VertexRoute(Vertex v1, Vertex v2, Double distance){
        this.v1=v1;
        this.v2=v2;
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

    public Vertex getV1(){
        return v1;
    }

    public Vertex getV2(){
        return v2;
    }

    public String toString(){
        return v1.toString()+" " + v2.toString()+" "+distance+" "+previous.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexRoute that = (VertexRoute) o;

        if (!v1.equals(that.v1)) return false;
        return v2.equals(that.v2);
    }

    @Override
    public int hashCode() {
        int result = v1.hashCode();
        result = 31 * result + v2.hashCode();
        return result;
    }
}
