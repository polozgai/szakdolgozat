package hu.elte.graph;


import java.util.*;

public class Graph {

    private List<Vertex> verticies;
    private List<VertexPair> vertexPairs;
    private Map<VertexPair, Edge> edges;

    public Graph(){
        verticies = new ArrayList<Vertex>();
        vertexPairs = new ArrayList<VertexPair>();
        edges = new HashMap<VertexPair, Edge>();
    }

    public void addVertex( Vertex v1 ,Vertex v2){
        if(!verticies.contains(v1)){
            verticies.add(v1);
        }
        if(!verticies.contains(v2)){
            verticies.add(v2);
        }
        int v1_index=verticies.indexOf(v1);
        int v2_index=verticies.indexOf(v2);
        verticies.get(v1_index).getNeighbours().add(v2);
        verticies.get(v2_index).getNeighbours().add(v1);
    }


    public void addEdge(Edge e, Vertex v1, Vertex v2){
        VertexPair p=new VertexPair(v1,v2,e);
        if(!vertexPairs.contains(p)){
           vertexPairs.add(p);
        }
        if(!edges.containsValue(p)){
            edges.put(p,e);
        }
        int v1_index=verticies.indexOf(v1);
        int v2_index=verticies.indexOf(v2);
        verticies.get(v1_index).getEdges().put(v2,e);
        verticies.get(v2_index).getEdges().put(v1,e);
    }

    public List<Vertex> getVerticies(){
        return verticies;
    }


    public Map<VertexPair,Edge> getEdges(){
        return edges;
    }

    public List<Vertex> getNeighbours(Vertex v){
        List<Vertex> neighbours=new ArrayList<>();
        for(VertexPair p : vertexPairs){
            if(p.contains(v)){
                neighbours.add(p.anotherVertex(v));
            }
        }
        return neighbours;
    }


    public void cutGraphByEdges(){
        
    }






    //ToString
    public void tooString(){
        System.out.println("Verticies");
        for(Vertex v: verticies){
            System.out.println(v.toString());
        }
        System.out.println("Edges");
        for(Vertex v:verticies){
            System.out.println(v.toString()+": ");
            for(Map.Entry<Vertex,Edge> entry: v.getEdges().entrySet()){
                System.out.println(entry.getKey() + "/" + entry.getValue());
            }
        }

        System.out.println("Pairs");
        for(VertexPair pair: vertexPairs){
            System.out.println(pair.toString());
        }
        System.out.println("Neighbours");
        Vertex v=verticies.get(1);
        System.out.println(v.getNeighbours().size());
        for(Vertex p: v.getNeighbours()) {
            System.out.println(p.toString());
        }
        System.out.println("Queues");
        for(Vertex k:verticies){

        }
    }

}
