package hu.elte.graph;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class Vertex {

    private String name;
    private List<Vertex> neighbours=new ArrayList<>();
    private Map<Vertex,Edge> edges=new HashMap<>();
    private Vertex parent=null;
    private int messagesToChildrenNumber =0;
    private double distance=0;

    private LinkedList<VertexRoute> routes=new LinkedList<>();


    public Vertex(String name){
        this.name=name;
    }

    public void increaseMessagesToChildrenNumber() {
         messagesToChildrenNumber++;
    }

    public int getMessagesToChildrenNumber() {
        return messagesToChildrenNumber;
    }

    public Vertex getNeighbourByName(String name){
        for (Vertex i:neighbours){
            if(i.getName().equals(name)){
                return i;
            }
        }
        return null;
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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String toString(){
        return name;
    }


    public List<Vertex> getNeighbours(){
        return neighbours;
    }

    public Map<Vertex, Edge> getEdges(){
        return edges;
    }

    public LinkedList<VertexRoute> getRoutes() {
        return routes;
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
        JSONObject object=new JSONObject();
        JSONArray list=new JSONArray();
        JSONArray prev=new JSONArray();
        for(VertexRoute i:routes){
            JSONObject obj=new JSONObject();
            obj.put("key",i.getV1()+" "+i.getV2());
            obj.put("value",i.getDistance());
            /*for(Vertex j:i.getPrevious()){
                prev.add(j.getName());
            }*/
            prev.add(i.getPrevious().toString());
            obj.put("previous",i.getPrevious().toString());
            list.add(obj);
        }
        object.put("SEND_ROUTES",list);
        return object.toString();
    }

    public void processRoutes(String msg){
        JSONParser parser=new JSONParser();
        try{
            Object object=parser.parse(msg);
            JSONArray array=(JSONArray) object;
            //System.out.println("array: "+array.toString());
            Iterator<JSONObject> it=array.iterator();
            while(it.hasNext()){
                JSONObject innnerObject=it.next();
                Double value=(Double) innnerObject.get("value");
                String key=(String) innnerObject.get("key");
                String[] arr=key.split(" ");
                String array1= (String) innnerObject.get("previous");
                array1=array1.substring(1,array1.length()-1);
                String[] array2=array1.split(",");
                //System.out.println(name+" - "+array1.toString());
                Vertex vertex=getNeighbourByName(arr[1]);
                Vertex neighbour=getNeighbourByName(arr[0]);
                Double halfRoute=getEdgeByName(arr[0]);
                if(vertex!=null){
                    Double previous=getEdgeByName(arr[1]);
                    if(halfRoute+value<previous){
                        setRouteDistence(name,arr[1],neighbour,halfRoute+value);
                    }
                }else{
                    VertexRoute route=new VertexRoute(new Vertex(name),new Vertex(arr[1]),halfRoute+value);
                    route.getPrevious().add(neighbour);
                    for (String i:array2){
                        if(!i.equals("")){
                            route.getPrevious().add(Graph.getVertexByName(i));
                        }
                    }
                    deletePrevious(name,arr[1]);
                    routes.add(route);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deletePrevious(String name, String s) {
        for(VertexRoute i:routes){
            if(i.getV1().getName().equals(name) && i.getV2().getName().equals(s)){
                routes.remove(i);
                break;
            }
        }
    }

    private void setRouteDistence(String s, String s1,Vertex vertex, Double distance){
        for(VertexRoute i:routes){
            if(i.getV1().getName().equals(s) && i.getV2().getName().equals(s1)){
                i.setDistance(distance);
                i.getPrevious().add(vertex);
                break;
            }
        }
    }

    private Double getEdgeByName(String s) {
        Vertex temp=new Vertex(s);
        return edges.get(temp).getWeight();
    }


    public String distanceToMessage(Vertex v) {
        JSONObject object=new JSONObject();
        object.put("SET_DISTANCE",edges.get(v).getWeight());
        return  object.toString();
    }

    public String parentToMessage(Vertex v) {
        JSONObject object=new JSONObject();
        int index_of=neighbours.indexOf(v);
        object.put("SET_PARENT",neighbours.get(index_of).getName());
        return  object.toString();
    }

    public void deleteParentFromRoutes(){
        if(!this.equals(parent)){
            for(VertexRoute i:routes){
                if(i.getV2().equals(parent) || i.getV1().equals(parent)){
                    routes.remove(i);
                    break;
                }
            }
        }
    }

    public void decreaseMessagesToChildrenNumber() {
        messagesToChildrenNumber--;
    }
}
