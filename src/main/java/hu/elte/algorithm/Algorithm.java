package hu.elte.algorithm;

import hu.elte.graph.Graph;
import hu.elte.graph.GraphReader;
import hu.elte.graph.Vertex;
import hu.elte.graph.VertexRoute;
import hu.elte.jms.client.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class Algorithm {

    private static Graph graph;
    private List<Client> clients=new LinkedList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static List<String> messages=new LinkedList<>();
    private LinkedList<Vertex> mainQueue=new LinkedList<>();
    private static LinkedList<Vertex> minRoute=new LinkedList<>();
    private static JSONObject jsonGraphByNode =new JSONObject();
    private static JSONObject jsonGraphByNodeForColorChange =new JSONObject();
    private static JSONObject jsonMinRouteForAnimation =new JSONObject();
    private static JSONObject allNodes=new JSONObject();
    private Double finalDistanceCost;

    private static final String input= "input/graph.txt";

    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(String start, String end, boolean isSleeping){
        createClients();
        createQueues();
        minRoute.clear();
        for(Vertex i:graph.getVertices()){
            if(i.getName().equals(start)){
                setStartVertex(i);
            }
            if(i.getName().equals(end)){
                setEndVertex(i);
            }
        }

        graphDiscovery(isSleeping);

        mapNeighbours(isSleeping);

        for(VertexRoute i:startVertex.getRoutes()){
            if(i.getEndVertex().equals(endVertex)){
                finalDistanceCost =i.getDistance();
                minRoute.addAll(i.getPrevious());
            }
        }

        minRoute.addFirst(startVertex);
        minRoute.addLast(endVertex);


        closeClients();

        writeAllMessagesToFile();
    }

    public static JSONObject allNodes(){
        JSONArray array=new JSONArray();
        for (Vertex i:graph.getVertices()){
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
        object.put("size", graph.getVertices().size());
        return  object;
    }

    public static JSONObject jsonMinRouteForAnimation(){
        return jsonMinRouteForAnimation;
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
                if(i.getName().equals(startVertex.getName())){
                    LinkedList<Vertex> tempRoute=new LinkedList<>();
                    for(VertexRoute j:startVertex.getRoutes()){
                        if(j.getEndVertex().getName().equals(endVertex.getName())){
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
                }
            }
            v.setActive(false);
            mainQueue.removeLast();
            jsonGraphByNodeForColorChange.put("node",v.getName());
            sleep(jsonGraphByNodeForColorChange,isSleeping,2000);
        }
        jsonMinRouteForAnimation.clear();
    }

    private void graphDiscovery(boolean isSleeping){
        LinkedList<Vertex> queue=new LinkedList<>();
        queue.add(startVertex);
        mainQueue.add(startVertex);
        jsonGraphByNode.put("graph",startVertex.getName());
        sleep(jsonGraphByNode,isSleeping,3000);
        startVertex.setActive(true);
        while (!queue.isEmpty()){
            Vertex v=queue.getFirst();
            for(Vertex i: v.getNeighbours()){
                if(!i.isActive() && i.getNeighbours().size()!=0){
                    i.setActive(true);
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

    private void createQueues(){
        for (Client client : clients){
            client.createQueue();
        }
    }

    private void createClients(){
        for (int i = 0; i< graph.getVertices().size(); i++){
            clients.add(new Client(graph.getVertices().get(i)));
        }
    }

    private Graph readGraph() {
        try {
            return GraphReader.graphFromFile(input);
        } catch (Exception e) {

        }
        return null;
    }

    public void setEndVertex(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }


    private void closeClients() {
        for(Client i:clients){
            i.getConsumer().close();
        }
    }

    public String getMinRouteWithWeight(){
        return minRoute.toString()+" "+ finalDistanceCost;
    }

    public static List<String> getMessages() {
        return messages;
    }

    private void writeAllMessagesToFile() {
        try(PrintWriter printWriter=new PrintWriter(new FileWriter("output/messages.txt",true))){
            printWriter.println("All messages for: "+startVertex.getName()+" - "+endVertex.getName());
            printWriter.println(new Date().toString());
            for(String i:messages){
                printWriter.println(i);
            }
            printWriter.println("Route: "+ minRoute.toString());
            printWriter.println("Cost: "+ finalDistanceCost);
            printWriter.println("Minimal spanning tree ([first vertex] [second vertex] [cost] [previous list]): "+startVertex.getRoutes().toString());
            messages.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sleep(JSONObject object, boolean isSleeping, int time){
        if(isSleeping){
            try {
                Thread.sleep(time);
                object.clear();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }





}
