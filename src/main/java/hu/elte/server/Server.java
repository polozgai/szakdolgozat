package hu.elte.server;

import hu.elte.Main;
import hu.elte.algorithm.Algorithm;
import hu.elte.graph.GraphReader;
import org.apache.activemq.broker.BrokerService;
import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.*;

public class Server {

    private String start;
    private String end;

    public Server(){ }

    public void show() {
        staticFiles.location("/public");
        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html")));

        get("/route", (req,res) -> Algorithm.jsonRoute());

        get("/graph",(req,res) -> GraphReader.getObject());

        get("/size", (req,res)-> Algorithm.jsonGraphSize());

        get("/graphByNode", (req,res) -> Algorithm.jsonGraphByNode());

        get("/graphByNodeForColorChange",(req,res)-> Algorithm.jsonGraphByNodeForColorChange());

        get("/routeStepByStep",(req,res) -> Algorithm.jsonMinRouteForAnimation());

        get("/allNodes",(req,res) ->{
            Algorithm algorithm=new Algorithm();
            return Algorithm.allNodes();
        });

        post("/animation",(req,res)->{
            String requestBody=req.body();
            boolean sleep=Boolean.parseBoolean(requestBody);
            startAlgorithm(sleep);
            return "";
        });

        post("/basic",(req,res)->{
            String requestBody=req.body();
            boolean sleep=Boolean.parseBoolean(requestBody);
            startAlgorithm(sleep);
            return "";
        });

        post("/start",(req,res)->{
            String requestBody=req.body();
            String[] array=requestBody.split("&");
            String[] startArray=array[0].split("=");
            String[] endArray=array[1].split("=");
            start=startArray[1];
            end=endArray[1];
            return "";
        });


        post("/stop",(req,res)->{
            stop();
            Main.getBrokerService().stop();
            return "";
        });

    }

    private void startAlgorithm(boolean sleep){
        Algorithm algorithm=new Algorithm();
        algorithm.computeAlgorithm(start,end,sleep);
    }


}
