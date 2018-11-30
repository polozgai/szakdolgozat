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
        JSONParser parser=new JSONParser();
        try{
            Object object=parser.parse(msg);
            JSONArray array=(JSONArray) object;
            Iterator<JSONObject> it=array.iterator();
            while(it.hasNext()){
                JSONObject innerObject=it.next();
                Double value=(Double) innerObject.get("value");
                String key=(String) innerObject.get("key");
                String[] keys=key.split(" ");
                if(keys[1].equals(name)){
                    continue;
                }
                String previousString= (String) innerObject.get("previous");
                previousString=previousString.substring(1,previousString.length()-1);
                String[] previousStringByComma=previousString.split(",");
                Vertex receivedVertex=getRouteByName(keys[1]);
                Vertex neighbour=Graph.getVertexByName(keys[0]);
                Double neighbourDistance=getRouteWeightByName(keys[0]);
                LinkedList<Vertex> previousList=getRoutePreviousByName(keys[0]);
                if(receivedVertex!=null){
                    int indexOf=getRouteIndexByName(receivedVertex.getName());
                    Double previous=routes.get(indexOf).getDistance();
                    if(neighbourDistance+value<previous){
                        setRouteDistance(indexOf,neighbour,neighbourDistance+value,previousStringByComma, previousList);
                    }
                }else{
                    VertexRoute route=new VertexRoute(this,Graph.getVertexByName(keys[1]),neighbourDistance+value);
                    route.getPrevious().add(neighbour);

                    for (String i:previousStringByComma){
                        i=i.trim();
                        if(!i.equals("")){
                            Vertex v=Graph.getVertexByName(i);
                            if(!route.getPrevious().contains(v)){
                                route.getPrevious().add(v);
                            }
                        }
                    }

                    routes.add(route);

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private int getRouteIndexByName(String name) {
        for(int i=0;i<routes.size();i++){
            if(routes.get(i).getEndVertex().getName().equals(name)){
                return i;
            }
        }
        return -1;
    }

    private void setRouteDistance(int indexOf, Vertex neighbour, Double distance, String[] previousVertexArray, LinkedList<Vertex> previousList){
        VertexRoute route=routes.get(indexOf);
        route.setDistance(distance);
        route.getPrevious().clear();
        route.getPrevious().add(neighbour);
        route.getPrevious().addAll(0,previousList);
        for(String j:previousVertexArray){
            j=j.trim();
            if(!j.equals("")){
                Vertex v=Graph.getVertexByName(j);
                if(!route.getPrevious().contains(v)){
                    route.getPrevious().add(v);
                }
            }
        }
    }

}
