package hu.elte.graph;


import java.util.*;

public class Graph {

    private static List<Vertex> verticies;

    public Graph(){
        verticies = new ArrayList<Vertex>();
    }

    public void addVertex( Vertex v1 ,Vertex v2){
        if(!verticies.contains(v1)){
            verticies.add(v1);
        }else{
            v1=verticies.get(verticies.indexOf(v1));
        }
        if(!verticies.contains(v2)){
            verticies.add(v2);
        }else{
            v2=verticies.get(verticies.indexOf(v2));
        }
        int v1_index=verticies.indexOf(v1);
        int v2_index=verticies.indexOf(v2);
        verticies.get(v1_index).getNeighbours().add(v2);
        verticies.get(v2_index).getNeighbours().add(v1);
    }


    public void addEdge(Edge e, Vertex v1, Vertex v2){
        VertexRoute p1=new VertexRoute(v1,v2,e.getWeight());
        VertexRoute p2=new VertexRoute(v2,v1,e.getWeight());
        int v1_index=verticies.indexOf(v1);
        int v2_index=verticies.indexOf(v2);
        verticies.get(v1_index).getEdges().put(v2,e);
        verticies.get(v2_index).getEdges().put(v1,e);
        //routes adding
        verticies.get(v1_index).getRoutes().add(p1);
        verticies.get(v2_index).getRoutes().add(p2);
    }

    public static Vertex getVertexByName(String name){
        for (Vertex i:verticies){
            if (i.getName().equals(name)){
                return  i;
            }
        }
        return null;
    }

    public List<Vertex> getVerticies(){
        return verticies;
    }

}
