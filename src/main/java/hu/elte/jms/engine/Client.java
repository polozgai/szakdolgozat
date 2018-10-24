package hu.elte.jms.engine;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Socket server;

    public Client(String host, int port){
        try {
            server=new Socket(host,port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
