package hu.elte.graph;


import java.util.*;

public class Vertex {

    private String name;
    private VertexState state;
    private List<Vertex> neighbours=new ArrayList<>();
    private Map<Vertex,Edge> edges=new HashMap<>();
    private Vertex parent=null;
    private double distance=0;

    private Map<VertexRoute,Double> routes=new LinkedHashMap<>();


    public Vertex(String name){
        this.name=name;
        this.state=VertexState.PASSIVE;
    }


    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public Map<VertexRoute, Double> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<VertexRoute, Double> routes) {
        this.routes = routes;
    }

    public void setNeighboursState(Vertex v, VertexState vertexState){
        for(Vertex i:neighbours){
            if(i.equals(v)){
                i.setState(vertexState);
            }
        }
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


    public String routesToMessage(){
        String s="";
        int j=0;
        for(Map.Entry<VertexRoute,Double> i:routes.entrySet()){
            if(j==routes.size()-1){
                s+=i.getKey()+" "+i.getValue();
            }else{
                s+=i.getKey()+" "+i.getValue()+";";
            }
            j++;
        }
        return s;
    }

    public void processRoutes(String msg){
        try {
            String[] array=msg.split(";");
            for(String i:array){
                String[] arr=i.split(" ");
                VertexRoute tempRoute=new VertexRoute(new Vertex(arr[0]),new Vertex(arr[1]));
                Double tempDouble=Double.parseDouble(arr[2]);
                //routes.put(tempRoute,tempDouble);
                Set<VertexRoute> keySet=routes.keySet();
                VertexRoute tempKey=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV2().getName()));
                VertexRoute tempKey2=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV1().getName()));
                Double previous=routes.get(tempKey);
                if(keySet.contains(tempKey)){
                    Double halfRoute=routes.get(tempKey2);
                    VertexRoute key=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV2().getName()));
                    if(halfRoute+tempDouble<previous){
                        //ha intellij-ben vagy nem hiba ha pirossal aláhúzza!!!
                        routes.replace(key,previous,halfRoute+tempDouble);
                        key.getPrivious().add(tempRoute.getV1());
                    }
                }
            }
        }catch (Exception e){}
    }








}
