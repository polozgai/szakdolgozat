package hu.elte;

import hu.elte.algorithm.Algorithm;
import hu.elte.graph.Graph;
import hu.elte.graph.GraphReader;
import hu.elte.jms.engine.Client;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class Main {

     private static List<Client> clients=new ArrayList<>();
     private static Graph graph;
     private static List<String> msgs=new LinkedList<>();

    public Main(){}


    public static void main(String[] args){

        //Main main=new Main();
        //graph = main.readGraph();
        //graph.tooString();

        //main.createClients();
        //main.createQueues();
        //clients.get(0).getProducer().send("ddddddddd","b");
        //clients.get(0).getProducer().send("halo","a");
        //for(Client client:clients){
            //System.out.println(client.getConsumer().getMessage()+" "+client.toSting());
        //}
        //System.out.println(msgs.size());

        Algorithm algorithm=new Algorithm();
        algorithm.createClients();
        algorithm.createQueues();
        //toString
        //algorithm.getGraph().tooString();
        algorithm.computeAlgorithm(algorithm.getGraph().getVerticies().get(0),
                algorithm.getGraph().getVerticies().get(5));
        algorithm.closeClients();
        System.out.println(algorithm.toString());

        //algorithm.getGraph().tooString();

        /*Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread i:threadSet){
            System.out.println(i.getName());
            i.stop();
        }*/

        //System.exit(0);


    }














    private Graph readGraph(){
        GraphReader gr=new GraphReader("graph.txt");
        Graph g = gr.graphFromFile();
        return g;
    }

    private void createQueues(){
        for (Client client : clients){
            client.createQueues();
        }
    }

    private void createClients(){
        for (int i = 0; i< graph.getVerticies().size(); i++){
            clients.add(new Client(i, graph.getVerticies().get(i)));
            //System.out.println(clients.get(i).toSting());
        }
    }

    public static void recieve(String msg){
        msgs.add(msg);
    }







}
