package hu.elte;

import hu.elte.algorithm.Algorithm;

public class Main {

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
        algorithm.computeAlgorithm("a","g");
        algorithm.closeClients();
        System.out.println(algorithm.toString());

        //algorithm.getGraph().tooString();


    }


}
