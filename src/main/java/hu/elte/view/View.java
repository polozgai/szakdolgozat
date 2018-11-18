package hu.elte.view;

import hu.elte.algorithm.Algorithm;
import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFiles;

public class View {


    public View(){
    }

    public void show() {
        staticFiles.location("/public");
        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html")));

        get("/route", (req,res) -> Algorithm.jsonRoute());

        get("/graph",(req,res) -> Algorithm.jsonGraph());

        get("/size", (req,res)-> Algorithm.jsonGraphSize());

        get("/graphByNode", (req,res) -> Algorithm.jsonGraphByNode());

        get("/graphByNodeForColorChange",(req,res)-> Algorithm.jsonGraphByNodeForColorChange());

        get("/routeStepByStep",(req,res) -> Algorithm.jsonMinRouteForAnimation());
    }

}
