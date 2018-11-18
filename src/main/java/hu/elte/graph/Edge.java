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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return Double.compare(edge.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(weight);
        return (int) (temp ^ (temp * 32));
    }
}
