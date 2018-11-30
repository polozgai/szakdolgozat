package hu.elte.graph;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class GraphReader {

    private static JSONObject object;

    public static Graph graphFromFile(String fileName){
        object=new JSONObject();
        JSONArray jsonArray=new JSONArray();
        Graph graph=new Graph();
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is= classloader.getResourceAsStream(fileName);
        try {
            is=new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("GraphReader.java: Hiba! Nem lehet elérni a grapht.txt-t");
        }
        Scanner sc=new Scanner(is);
        while(sc.hasNextLine()){
            String line=sc.nextLine();
            jsonArray.add(line);
            String[] data=line.split(" ");
            Vertex v1 = new Vertex(data[0]);
            Vertex v2 = new Vertex(data[1]);
            Edge e = new Edge(Double.parseDouble(data[2]));
            graph.addEdge(e,v1,v2);
        }

        object.put("graph",jsonArray);
        return graph;
    }

    public static JSONObject getObject() {
        return object;
    }
}
