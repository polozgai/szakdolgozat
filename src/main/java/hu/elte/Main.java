package hu.elte;

import hu.elte.graph.Graph;
import hu.elte.graph.GraphReader;
import hu.elte.jms.engine.Client;
import hu.elte.jms.engine.Server;

public class Main {

    public static void main(String[] args){

        //GraphReader gr=new GraphReader("graph.txt");
        //Graph g = gr.graphFromFile();
        //g.tooString();
        Server server=new Server(12345);
        server.handleClients();
        Client client=new Client("localhost", 12345);


    }
}
