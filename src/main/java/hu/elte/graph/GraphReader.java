package hu.elte.graph;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class GraphReader {

    private String fileName;

    public GraphReader(String fileName){
        this.fileName=fileName;
    }

    public Graph graphFromFile(){
        Graph g=new Graph();
        try{
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is= classloader.getResourceAsStream(fileName);
            Scanner sc=new Scanner(is);
            while(sc.hasNextLine()){
                String line=sc.nextLine();
                String[] data=line.split(" ");
                Vertex v1 = new Vertex(data[0]);
                Vertex v2 = new Vertex(data[1]);
                Edge e = new Edge(Double.parseDouble(data[2]));
                g.addVertex(v1,v2);
                g.addEdge(e,v1,v2);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return g;
    }
}
