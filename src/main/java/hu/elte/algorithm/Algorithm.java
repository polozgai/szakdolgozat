package hu.elte.algorithm;

import hu.elte.graph.*;
import hu.elte.jms.client.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;


public class Algorithm {

    private static Graph graph;
    private List<Client> clients=new ArrayList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static Map<Vertex,String> messages=new HashMap<>();
    private LinkedList<Vertex> mainQueue=new LinkedList<>();
    private static LinkedList<Vertex> minRoute=new LinkedList<>();
    private static JSONObject jsonGraphByNode =new JSONObject();
    private static JSONObject jsonGraphByNodeForColorChange =new JSONObject();
    private static JSONObject jsonMinRouteForAnimation =new JSONObject();
    private static JSONObject allNodes=new JSONObject();
    private Double finalDistance;

    private static final String input= "public/graph.txt";

    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(String start, String end, boolean isSleeping){
        createClients();
        createQueues();
        minRoute.clear();
        for(Vertex i:graph.getVerticies()){
            if(i.getName().equals(start)){
                setStartVertex(i);
            }
            if(i.getName().equals(end)){
                setEndVertex(i);
            }
        }


        graphDiscovery(isSleeping);

        for (Vertex i:mainQueue){
            System.out.println(i.getName());
        }

        mapNeighbours(isSleeping);

        for(Vertex i:graph.getVerticies()){
            if(i.getName().equals(start)){
                for(VertexRoute j:i.getRoutes()){
                    if(j.getV2().getName().equals(end)){
                        System.out.println(j.toString());
                        finalDistance=j.getDistance();
                        minRoute.addAll(j.getPrevious());
                    }
                }
            }
        }

        minRoute.addFirst(startVertex);
        minRoute.addLast(endVertex);
        //printToFile();
        System.out.println(minRoute.toString());

        System.out.println("List.Messages");
        closeClients();
    }

    public static JSONObject allNodes(){
        JSONArray array=new JSONArray();
        for (Vertex i:graph.getVerticies()){
            array.add(i.getName());
        }
        allNodes.put("allNodes",array);
        return  allNodes;
    }

    public static JSONObject jsonRoute(){
        JSONObject object=new JSONObject();
        JSONArray array=new JSONArray();
        try{
            for(int i=0;i<minRoute.size();i++){
                array.add(minRoute.get(i).getName()+" "+minRoute.get(i+1).getName());
            }
        }catch (Exception e){ }
        object.put("route",array);
        return object;
    }

    public static JSONObject jsonGraphSize(){
        JSONObject object=new JSONObject();
        object.put("size", graph.getVerticies().size());
        return  object;
    }

    public static JSONObject jsonMinRouteForAnimation(){
        return jsonMinRouteForAnimation;
    }

    public static JSONObject jsonGraph() {
        return GraphReader.getObject();
    }

    public static JSONObject jsonGraphByNode(){
        return jsonGraphByNode;
    }

    public static JSONObject jsonGraphByNodeForColorChange(){
        return jsonGraphByNodeForColorChange;
    }


    private void mapNeighbours(boolean isSleeping){
        while(!mainQueue.isEmpty()){
            Vertex v=mainQueue.getLast();
            Client tempClient=getClient(v);
            for(Vertex i:v.getNeighbours()){
                Vertex real=Graph.getVertexByName(i.getName());
                if(real.isActive()){
                    tempClient.getProducer().send(v.routesToMessage(),i.getName());
                }
                //animation
                if(i.getName().equals(startVertex.getName())){
                    LinkedList<Vertex> tempRoute=new LinkedList<>();
                    //System.out.println(startVertex.getRoutes());
                    for(VertexRoute j:startVertex.getRoutes()){
                        if(j.getV2().getName().equals(endVertex.getName())){
                            tempRoute.addAll(j.getPrevious());
                        }
                    }
                    tempRoute.addFirst(startVertex);
                    tempRoute.addLast(endVertex);
                    JSONArray array=new JSONArray();
                    try {
                        for(int j=0;j<tempRoute.size();j++){
                            array.add(tempRoute.get(j).getName()+" "+tempRoute.get(j+1).getName());
                        }
                    }catch (Exception e){}
                    jsonMinRouteForAnimation.clear();
                    jsonMinRouteForAnimation.put("route",array);
                    //System.out.println(jsonMinRouteForAnimation.toString());
                }
            }
            v.setActive(false);
            mainQueue.removeLast();
            jsonGraphByNodeForColorChange.put("node",v.getName());
            sleep(jsonGraphByNodeForColorChange,isSleeping,2000);
        }
        jsonMinRouteForAnimation.clear();
    }


    //jo setParent-ből lett discovery
    private void graphDiscovery(boolean isSleeping){
        LinkedList<Vertex> queue=new LinkedList<>();
        queue.add(startVertex);
        mainQueue.add(startVertex);
        jsonGraphByNode.put("graph",startVertex.getName());
        sleep(jsonGraphByNode,isSleeping,3000);
        startVertex.setDiscovered(true);
        while (!queue.isEmpty()){
            Vertex v=queue.getFirst();
            for(Vertex i: v.getNeighbours()){
                if(!i.isDiscovered() && i.getNeighbours().size()!=0){
                    i.setDiscovered(true);
                    queue.add(i);
                    mainQueue.add(i);
                    jsonGraphByNode.put("graph",i.getName());
                    sleep(jsonGraphByNode,isSleeping,2000);
                }
            }
            queue.removeFirst();
        }
    }



    private Client getClient(Vertex v){
        for (Client client:clients){
            if(client.getVertex().equals(v)){
                return client;
            }
        }
        return null;
    }


    public static void getMessages(String msg){
        JSONParser parser=new JSONParser();
        try {
            JSONObject object=(JSONObject) parser.parse(msg);
            String producerName=(String) object.get("producerName");
            String text=(String) object.get("message");
            String consumerName=(String) object.get("consumerName");
            messages.put(new Vertex(consumerName),text);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void createQueues(){
        for (Client client : clients){
            client.createQueue();
        }
    }


    private void createClients(){
        for (int i = 0; i< graph.getVerticies().size(); i++){
            clients.add(new Client(i, graph.getVerticies().get(i)));
            //System.out.println(clients.get(i).toSting());
        }
    }



    private Graph readGraph() {
        try {
            return GraphReader.graphFromFile(input);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }


    @Override
    public String toString() {
        return startVertex.toString()+" "+endVertex.toString()+" Msg: "+messages.toString();
    }

    private void closeClients() {
        for(Client i:clients){
            i.getConsumer().close();
        }
    }

    public String getMinRouteWithWeight(){
        return minRoute.toString()+" "+finalDistance;
    }

    private void sleep(JSONObject object, boolean isSleeping, int time){
        if(isSleeping){
            try {
                System.out.println(object.toString());
                Thread.sleep(time);
                object.clear();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





}
