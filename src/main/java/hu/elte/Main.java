package hu.elte;

import hu.elte.server.Server;
import org.apache.activemq.broker.BrokerService;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    private static BrokerService brokerService;

    public static void main(String[] args){
        brokerService=new BrokerService();
        try {
            brokerService.addConnector("tcp://localhost:61616");
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try(PrintWriter printWriter=new PrintWriter("output/messages.txt")){
            printWriter.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Server server=new Server();
        server.show();

    }

    public static BrokerService getBrokerService() {
        return brokerService;
    }
}
