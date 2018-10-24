package hu.elte.jms.engine;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private ServerSocket serverSocket;

    public Server(int port){
        try{
            serverSocket=new ServerSocket(port);
        }catch (Exception e ){
            e.printStackTrace();
        }
    }

    public void handleClients(){
        while(true){
            try {
                serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
