package hu.elte.algorithm;

import hu.elte.graph.Graph;
import hu.elte.graph.GraphReader;
import hu.elte.graph.Vertex;
import hu.elte.graph.VertexState;
import hu.elte.jms.engine.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Algorithm {

    private Graph graph;
    private List<Client> clients=new ArrayList<>();
    private Vertex startVertex;
    private Vertex endVertex;
    private static List<String> messages=new LinkedList<>();

    public Algorithm(){
        this.graph=readGraph();
    }

    public void computeAlgorithm(Vertex start, Vertex end){
        setStartVertex(start);
        setEndVertex(end);
        recursive(startVertex);
        /*Client startClient=getClient(startVertex);
        for(int i=0;i<startVertex.getNeighbours().size();i++){
            Vertex temp=startVertex.getNeighbours().get(i);
            startClient.getProducer().send(startVertex.getEdges().get(temp).toString(),temp.getName());
        }*/
    }

    public void recursive(Vertex v){
        v.setState(VertexState.ACTIVE);
        Client tempClient=getClient(v);
        Vertex tempVertex=null;
        for(int i=0;i<v.getNeighbours().size();i++){
            tempVertex=v.getNeighbours().get(i);
            if(tempVertex.getState()==VertexState.PASSIVE){
                tempClient.getProducer().send(v.getEdges().get(tempVertex).toString(),tempVertex.getName());
                recursive(tempVertex);
            }
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
        messages.add(msg);
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
