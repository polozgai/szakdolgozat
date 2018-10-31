package hu.elte.graph;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
        JSONArray list=new JSONArray();
        for(Map.Entry<VertexRoute,Double> i:routes.entrySet()){
            JSONObject obj=new JSONObject();
            obj.put("key",i.getKey().getV1()+" "+i.getKey().getV2());
            obj.put("value",i.getValue());
            list.add(obj);
        }
        return list.toString();
    }

    public void processRoutes(String msg){
        JSONParser parser=new JSONParser();
        try{
            Object object=parser.parse(msg);
            JSONArray array=(JSONArray) object;
            //System.out.println("array: "+array.toString());
            Iterator<JSONObject> it=array.iterator();
            while(it.hasNext()){
                JSONObject temp=it.next();
                Double value=(Double) temp.get("value");
                String key=(String) temp.get("key");
                String[] arr=key.split(" ");
                VertexRoute tempRoute=new VertexRoute(new Vertex(arr[0]),new Vertex(arr[1]));
                Set<VertexRoute> keySet=routes.keySet();
                VertexRoute tempKey=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV2().getName()));
                VertexRoute tempKey2=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV1().getName()));
                Double halfRoute=routes.get(tempKey2);
                Double previous=routes.get(tempKey);
                if(keySet.contains(tempKey)){
                    //VertexRoute key=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV2().getName()));
                    if(halfRoute+value<previous){
                        //ha intellij-ben vagy nem hiba ha pirossal aláhúzza!!!
                        routes.replace(tempKey,previous,halfRoute+value);
                        tempKey.getPrivious().add(tempRoute.getV1());
                    }
                }else{
                    //halfroute
                    VertexRoute r=new VertexRoute(new Vertex(name),new Vertex(tempRoute.getV2().getName()));
                    routes.put(r,halfRoute+value);
                    r.getPrivious().add(tempRoute.getV1());
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }








}
