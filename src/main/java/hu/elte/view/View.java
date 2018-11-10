package hu.elte.view;

import spark.Spark;
import spark.utils.IOUtils;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;
import static spark.Spark.staticFiles;

public class View {

    public View(){}

    public void show() {
        staticFiles.location("/");
        get("/", (req, res) -> IOUtils.toString(Spark.class.getResourceAsStream("/index.html")));
    }

}
