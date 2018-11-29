package hu.elte.graph;



import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;

public class Vertex {

    private String name;
    private List<Vertex> neighbours=new ArrayList<>();
    private boolean active = false;
    private LinkedList<VertexRoute> routes=new LinkedList<>();

    public Vertex(String name){
        this.name=name;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public Vertex getRouteByName(String name){
        for (VertexRoute i:routes){
            if(i.getEndVertex().getName().equals(name)){
                return i.getEndVertex();
            }
        }
        return null;
    }

    public Double getRouteWeightByName(String name){
        for(VertexRoute i:routes){
            if(i.getEndVertex().getName().equals(name)){
                return i.getDistance();
            }
        }
        return null;
    }

    public LinkedList<Vertex> getRoutePreviousByName(String name){
        for(VertexRoute i: routes){
            if(i.getEndVertex().getName().equals(name)){
                return i.getPrevious();
            }
        }
        return null;
    }

    public String getName(){
        return name;
    }

    public String toString(){
        return name;
    }

    public List<Vertex> getNeighbours(){
        return neighbours;
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
        for(VertexRoute i:routes){
            JSONObject obj=new JSONObject();
            obj.put("key",i.getStartVertex()+" "+i.getEndVertex());
            obj.put("value",i.getDistance());
            obj.put("previous",i.getPrevious().toString());
            list.add(obj);
        }
        object.put("SEND_ROUTES",list);
        return object.toString();
    }

    public void processRoutes(String msg){
        //System.out.println("elotte: \n"+routes.toString());
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
                if(arr[1].equals(name)){
                    continue;
                }
                String array1= (String) innnerObject.get("previous");
                array1=array1.substring(1,array1.length()-1);
                String[] array2=array1.split(",");
                //System.out.println(name+" - "+array1.toString());
                Vertex vertex=getRouteByName(arr[1]);
                Vertex neighbour=Graph.getVertexByName(arr[0]);
                Double halfRoute=getRouteWeightByName(arr[0]);
                LinkedList<Vertex> prev=getRoutePreviousByName(arr[0]);
                if(vertex!=null){
                    int index_of=getRouteIndexByName(vertex.getName());
                    Double previous=routes.get(index_of).getDistance();
                    if(halfRoute+value<previous){
                        setRouteDistence(name,arr[1],neighbour,halfRoute+value,array2,prev);
                    }
                }else{
                    VertexRoute route=new VertexRoute(this,Graph.getVertexByName(arr[1]),halfRoute+value);
                    route.getPrevious().add(neighbour);

                    for (String i:array2){
                        i=i.trim();
                        if(!i.equals("")){
                            Vertex v=Graph.getVertexByName(i);
                            if(!route.getPrevious().contains(v)){
                                route.getPrevious().add(v);
                            }
                        }
                    }
                    deletePrevious(name,arr[1]);
                    routes.add(route);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(routes.toString());
    }


    private int getRouteIndexByName(String name) {
        for(int i=0;i<routes.size();i++){
            if(routes.get(i).getEndVertex().getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    private void deletePrevious(String name, String s) {
        for(VertexRoute i:routes){
            if(i.getStartVertex().getName().equals(name) && i.getEndVertex().getName().equals(s)){
                routes.remove(i);
                break;
            }
        }
    }

    private void setRouteDistence(String s, String s1, Vertex vertex, Double distance, String[] array2, LinkedList<Vertex> prev){
        for(VertexRoute i:routes){
            if(i.getStartVertex().getName().equals(s) && i.getEndVertex().getName().equals(s1)){
                i.getPrevious().clear();
                i.setDistance(distance);
                i.getPrevious().add(vertex);
                i.getPrevious().addAll(0,prev);
                for(String j:array2){
                    j=j.trim();
                    if(!j.equals("")){
                        Vertex v=Graph.getVertexByName(j);
                        if(!i.getPrevious().contains(v)){
                            i.getPrevious().add(v);
                        }
                    }
                }
                break;
            }
        }
    }

}
