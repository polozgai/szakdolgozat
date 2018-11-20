package hu.elte;

import hu.elte.algorithm.Algorithm;
import hu.elte.view.View;

public class Main {

    public Main(){}

    public static void main(String[] args){


        View v=new View();
        v.show();

        Algorithm algorithm=new Algorithm();
        //algorithm.createClients();
        //algorithm.createQueues();
        algorithm.computeAlgorithm("a","g");
        //algorithm.closeClients();
        System.out.println(algorithm.toString());




    }


}
