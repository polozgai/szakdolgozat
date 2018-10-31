package hu.elte.algorithm;

import hu.elte.graph.*;
import hu.elte.jms.engine.Client;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Algorithm {

    private Graph graph;
    private List<Client> clients=new ArrayList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static Map<Vertex,String> messages=new HashMap<>();



    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(Vertex start, Vertex end){
        setStartVertex(start);
        setEndVertex(end);

        //init();

        setParent();
        for(Vertex v:graph.getVerticies()){
            //System.out.println(v.toString()+" "+v.getParent()+" "+v.getDistance());
        }
        setRoutes();

        mapNeighbours();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String val=messages.get(graph.getVerticies().get(2));
        System.out.println("val: "+val);
        graph.getVerticies().get(2).processRoutes(val);
        System.out.println(graph.getVerticies().get(2).getRoutes().toString());





        //System.out.println("List.Messages");

    }


    public void mapNeighbours(){
        for(Vertex v:graph.getVerticies()){
            Client tempClient=getClient(v);
            Vertex parent=v.getParent();
            if(v.equals(parent)){
                continue;
            }
            tempClient.getProducer().send(v.routesToMessage(),parent.getName());
        }
    }



    public void setRoutes(){
        for(Vertex v:graph.getVerticies()){
            Vertex p=v.getParent();
            if(v.equals(p)){
                continue;
            }
            Iterator<Map.Entry<VertexRoute,Double>> it=v.getRoutes().entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<VertexRoute,Double> entry=it.next();
                if(entry.getKey().contains(p)){
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
        startVertex.setParent(startVertex);
        while (!queue.isEmpty()){
            Vertex v=queue.getFirst();
            Client tempClient=getClient(v);
            for(Vertex i: v.getNeighbours()){
                if(i.getParent()==null){
                    queue.add(i);
                    i.setDistance(v.getEdges().get(i).getWeight());
                    i.setParent(v);
                }
            }
            queue.removeFirst();
        }
    }

    public void init(){
        LinkedList<Vertex> queue=new LinkedList<>();
        while(!queue.isEmpty()){
            Vertex v=queue.getFirst();
            Client tempClient=getClient(v);
            for (Vertex i: v.getNeighbours()){
                if (i.getState()==VertexState.PASSIVE){
                    queue.add(i);
                    tempClient.getProducer().send(v.getEdges().get(i).toString(),i.getName());
                }
            }
            v.setState(VertexState.ACTIVE);
            for(Vertex i:queue){
                i.setNeighboursState(v,VertexState.ACTIVE);
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
        /*String[] arr=msg.split(":");
        String[] arr2=arr[0].split("->");
        messages.put(new Vertex(arr2[1]),arr[1]);*/
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
}
