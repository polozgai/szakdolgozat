package hu.elte.graph;


import java.util.*;

public class Graph {

    private static List<Vertex> vertices;

    public Graph(){
        vertices = new ArrayList<Vertex>();
    }

    public void addEdge(Edge edge, Vertex vertexOne, Vertex vertexTwo){
        if(!vertices.contains(vertexOne)){
            vertices.add(vertexOne);
        }else{
            vertexOne= vertices.get(vertices.indexOf(vertexOne));
        }
        if(!vertices.contains(vertexTwo)){
            vertices.add(vertexTwo);
        }else{
            vertexTwo= vertices.get(vertices.indexOf(vertexTwo));
        }
        int vertexOneIndex= vertices.indexOf(vertexOne);
        int vertexTwoIndex= vertices.indexOf(vertexTwo);
        vertices.get(vertexOneIndex).getNeighbours().add(vertexTwo);
        vertices.get(vertexTwoIndex).getNeighbours().add(vertexOne);
        VertexRoute routeOne=new VertexRoute(vertexOne,vertexTwo,edge.getWeight());
        VertexRoute routeTwo=new VertexRoute(vertexTwo,vertexOne,edge.getWeight());
        vertices.get(vertexOneIndex).getRoutes().add(routeOne);
        vertices.get(vertexTwoIndex).getRoutes().add(routeTwo);
    }

    public static Vertex getVertexByName(String name){
        for (Vertex i: vertices){
            if (i.getName().equals(name)){
                return  i;
            }
        }
        return null;
    }

    public List<Vertex> getVertices(){
        return vertices;
    }

}
