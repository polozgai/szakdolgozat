package hu.elte.view;

import hu.elte.algorithm.Algorithm;
import org.json.simple.JSONObject;
import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class View {

    private JSONObject graph;
    private JSONObject route;

    public View(){
        this.route=Algorithm.jsonRoute();
        this.graph=Algorithm.jsonGraph();
        //System.out.println(route);
        //System.out.println(graph);
    }

    public void show() {
        staticFiles.location("/public");
        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html")));

        get("/route", (req,res) -> {
            return route;
        });

        get("/graph",(req,res) -> {
           return graph;
        });
    }

}
