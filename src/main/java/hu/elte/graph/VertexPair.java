package hu.elte.graph;

public class VertexPair {

    private Vertex v1;
    private Vertex v2;

    public VertexPair(Vertex v1, Vertex v2){
        this.v1=v1;
        this.v2=v2;
    }

    public boolean contains(Vertex v){
        if(v.equals(v1) || v.equals(v2)){
            return true;
        }
        return false;
    }

    public Vertex anotherVertex(Vertex v){
        if(v.equals(v1)){
            return v2;
        }else{
            return v1;
        }
    }


    public Vertex getV1(){
        return v1;
    }

    public Vertex getV2(){
        return v2;
    }

    public String toString(){
        return v1.toString()+" " + v2.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VertexPair that = (VertexPair) o;

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
