package hu.elte.algorithm;

import hu.elte.graph.*;
import hu.elte.jms.engine.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;


public class Algorithm {

    private Graph graph;
    private List<Client> clients=new ArrayList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static Map<Vertex,String> messages=new HashMap<>();
    private LinkedList<Vertex> mainQueue=new LinkedList<>();
    private LinkedList<Vertex> minRoute=new LinkedList<>();

    private static final String input="graph.txt";

    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(String start, String end){
        for(Vertex i:graph.getVerticies()){
            if(i.getName().equals(start)){
                setStartVertex(i);
            }
            if(i.getName().equals(end)){
                setEndVertex(i);
            }
        }


        graphDiscovery();

        for (Vertex i:mainQueue){
            System.out.println(i.getName());
        }

        mapNeighbours();

        for(Vertex i:graph.getVerticies()){
            if(i.getName().equals(start)){
                for(VertexRoute j:i.getRoutes()){
                    if(j.getV2().getName().equals(end)){
                        System.out.println(j.toString());
                        minRoute.addAll(j.getPrevious());
                    }
                }
            }
        }

        minRoute.addFirst(startVertex);
        minRoute.addLast(endVertex);
        printToFile();
        System.out.println(startVertex.getRoutes().toString());

        System.out.println("List.Messages");

}


    public void printToFile(){
        try(PrintWriter printWriter=new PrintWriter("src/main/resources/route.txt","UTF-8");){
            for(int i=0;i<minRoute.size();i++){
                printWriter.println(minRoute.get(i).getName()+" "+minRoute.get(i+1).getName());
            }
        } catch (Exception e) { }
    }




    public void mapNeighbours(){
        while(!mainQueue.isEmpty()){
            Vertex v=mainQueue.getLast();
            Client tempClient=getClient(v);
            for(Vertex i:v.getNeighbours()){
                Vertex real=Graph.getVertexByName(i.getName());
                if(real.isActive()){
                    tempClient.getProducer().send(v.routesToMessage(),i.getName());
                }
            }
            v.setActive(false);
            mainQueue.removeLast();
        }
    }


    //jo setParent-ből lett discovery
    public void graphDiscovery(){
        LinkedList<Vertex> queue=new LinkedList<>();
        queue.add(startVertex);
        mainQueue.add(startVertex);
        startVertex.setDiscovered(true);
        while (!queue.isEmpty()){
            Vertex v=queue.getFirst();
            for(Vertex i: v.getNeighbours()){
                if(!i.isDiscovered() && i.getNeighbours().size()!=0){
                    i.setDiscovered(true);
                    queue.add(i);
                    mainQueue.add(i);
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

    public void createQueues(){
        for (Client client : clients){
            client.createQueues();
        }
    }


    public void createClients(){
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
        return startVertex.toString()+" "+endVertex.toString()+" Msg size: "+messages.size();
    }

    public void closeClients() {
        for(Client i:clients){
            i.getConsumer().close();
        }
    }
}
