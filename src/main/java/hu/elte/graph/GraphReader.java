package hu.elte.graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.util.Scanner;

public class GraphReader {

    private static JSONObject object;

    public static Graph graphFromFile(String fileName){
        object=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        Graph graph=new Graph();
        try(
            InputStream is=new FileInputStream(fileName);
            Scanner sc=new Scanner(is);
        ){
            while(sc.hasNextLine()){
                String line=sc.nextLine();
                jsonArray.add(line);
                String[] data=line.split(" ");
                Vertex v1 = new Vertex(data[0]);
                Vertex v2 = new Vertex(data[1]);
                Edge e = new Edge(Double.parseDouble(data[2]));
                graph.addEdge(e,v1,v2);
            }
        } catch (Exception e) {
            System.out.println("GraphReader.java Hiba a beolvasasnal.");
        }

        object.put("graph",jsonArray);
        return graph;
    }

    public static JSONObject getObject() {
        return object;
    }
}
