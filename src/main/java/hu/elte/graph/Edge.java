package hu.elte.graph;


public class Edge {

    private double weight;

    public Edge(double weight){
        this.weight=weight;
    }

    public double getWeight(){
        return weight;
    }

    public String toString(){
        return String.valueOf(weight);
    }

}
