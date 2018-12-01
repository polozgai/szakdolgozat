package hu.elte;

import hu.elte.server.Server;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Main {

    public static void main(String[] args){

        try(PrintWriter printWriter=new PrintWriter("output/messages.txt")){
            printWriter.print("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Server server=new Server();
        server.show();

    }
}
