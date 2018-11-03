package hu.elte.algorithm;

import hu.elte.graph.*;
import hu.elte.jms.engine.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;

public class Algorithm {

    private Graph graph;
    private List<Client> clients=new ArrayList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static Map<Vertex,String> messages=new HashMap<>();
    private LinkedList<Vertex> mainQueue=new LinkedList<>();



    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(Vertex start, Vertex end){
        setStartVertex(start);
        setEndVertex(end);

        //init();

        setParent();
        //createQueues();
        for(Vertex v:graph.getVerticies()){
            System.out.println(v.toString()+" "+v.getParent()+" "+v.getDistance()+" "+v.getMessagesToChildrenNumber());
        }

        for (Vertex i:mainQueue){
            //System.out.println(i.getName());
        }

        mapNeighbours();
        /*try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String val=messages.get(graph.getVerticies().get(2));
        System.out.println("val: "+val);
        graph.getVerticies().get(2).processRoutes(val);
        System.out.println(graph.getVerticies().get(2).getRoutes().toString());
        */
        VertexRoute temp=new VertexRoute(start,end);
        System.out.println(startVertex.getRoutes().get(temp));

        System.out.println("List.Messages");

}




    public void mapNeighbours(){
        while(!mainQueue.isEmpty()){
            Vertex v=mainQueue.getLast();
            Client tempClient=getClient(v);
            if(!v.equals(v.getParent())){
                tempClient.getProducer().send(v.routesToMessage(),v.getParent().getName());
            }
            mainQueue.removeLast();
        }
        /*for(Vertex v:graph.getVerticies()){
            Client tempClient=getClient(v);
            Vertex parent=v.getParent();
            if(v.equals(parent)){
                continue;
            }
            tempClient.getProducer().send(v.routesToMessage(),parent.getName());
        }*/
    }


    //nem kell benne van a setParentben a Clientben a feldolgozásban
    public void setRoutes(){
        for(Vertex v:graph.getVerticies()){
            Vertex parent=v.getParent();
            if(v.equals(parent)){
                continue;
            }
            Iterator<Map.Entry<VertexRoute,Double>> it=v.getRoutes().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<VertexRoute,Double> entry=it.next();
                if(entry.getKey().contains(parent)){
                    it.remove();
                }
            }
            //System.out.println(v.getRoutes().toString());
        }
    }

    //jo
    public void setParent(){
        LinkedList<Vertex> queue=new LinkedList<>();
        queue.add(startVertex);
        mainQueue.add(startVertex);
        startVertex.setParent(startVertex);
        while (!queue.isEmpty()){
            Vertex v=queue.getFirst();
            for(Vertex i: v.getNeighbours()){
                if(i.getParent()==null && i.getNeighbours().size()!=0){
                    Client tempClient=getClient(i);
                    tempClient.getProducer().send(i.distanceToMessage(v),i.getName());
                    tempClient.getProducer().send(i.parentToMessage(v),i.getName());
                    queue.add(i);
                    mainQueue.add(i);
                    //i.setDistance(v.getEdges().get(i).getWeight());
                    //i.setDistance(i.getEdges().get(v).getWeight());
                    //i.setParent(v);
                    v.increaseMessagesToChildrenNumber();
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



    private Graph readGraph(){
        GraphReader gr=new GraphReader("graph.txt");
        Graph g = gr.graphFromFile();
        return g;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public List<Client> getClients() {
        return clients;
    }

    public Graph getGraph() {
        return graph;
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
