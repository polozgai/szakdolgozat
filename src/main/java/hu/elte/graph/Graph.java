package hu.elte.graph;


import java.util.*;

public class Graph {

    private static List<Vertex> verticies;
    private List<Vertex> staticVerticies;
    private List<VertexRoute> vertexRoutes;



    public Graph(){
        verticies = new ArrayList<Vertex>();
        vertexRoutes = new ArrayList<VertexRoute>();
        staticVerticies=new ArrayList<>();
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
        VertexRoute p1=new VertexRoute(v1,v2,e.getWeight());
        VertexRoute p2=new VertexRoute(v2,v1,e.getWeight());
        if(!vertexRoutes.contains(p1)){
           vertexRoutes.add(p1);
        }
        int v1_index=verticies.indexOf(v1);
        int v2_index=verticies.indexOf(v2);
        verticies.get(v1_index).getEdges().put(v2,e);
        verticies.get(v2_index).getEdges().put(v1,e);
        //routes adding
        verticies.get(v1_index).getRoutes().add(p1);
        verticies.get(v2_index).getRoutes().add(p2);
    }

    public List<Vertex> getVerticies(){
        return verticies;
    }

    public List<Vertex> getNeighbours(Vertex v){
        List<Vertex> neighbours=new ArrayList<>();
        for(VertexRoute p : vertexRoutes){
            if(p.contains(v)){
                neighbours.add(p.anotherVertex(v));
            }
        }
        return neighbours;
    }


    public static Vertex getVertexByName(String name){
        for (Vertex i:verticies){
            if (i.getName().equals(name)){
                return  i;
            }
        }
        return null;
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

        System.out.println("Routes");
        for(Vertex v: verticies){
            System.out.println(v.getRoutes().toString());
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
