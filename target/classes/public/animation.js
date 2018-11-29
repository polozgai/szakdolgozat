

function Get(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}

var sizeJson=Get("http://localhost:4567/size");
var size=JSON.parse(sizeJson);
size=size.size;
console.log(size);

var x=100;
var y=100;
var array=new vis.DataSet();
var realEdges=new vis.DataSet();
var temp=new Array();
var count=0;
var map=new Map();


  var container = document.getElementById('mynetwork');
  var data = {
    nodes: array,
    edges: realEdges
  };
  var options = {
        nodes : {
            shape: 'circle',
            size: 10,
        }

  };

 var network = new vis.Network(container, data, options);

network.moveTo({
        position:{x:100,y:100},
        scale:0.8
    });








function graph(){
    var id=setInterval(function(){

        var graphJson=Get("http://localhost:4567/graphByNode");
        var graph=JSON.parse(graphJson);
        graph=graph.graph;

        console.log(graph);

        if(typeof graph !== 'undefined' && !temp.includes(graph)){
            temp.push(graph);
            map.set(count,graph);
            count++;
            //x=Math.floor(Math.random()*350);
            //y=Math.floor(Math.random()*350);
            if(count%2==0){
                x+=20;
                y+=20
            }else{
                x+=20;
                y-=20;
            }
            /*if(count%4==0){
                x+=100;
                y+=100;
            }else if(count%4==1){
                x+=100;
                y-=100;
            }else if(count%4==2){
                x-=100;
                y+=100;
            }else{
                x-=100;
                y-=100;
            }*/
            array.add({
                id:array.length, label:graph, x:x, y:y,color:{background:'#3FF31B'}
            });
            //console.log(array);
        }

        addEdges();

        if(array.length==size){
        console.log("graph_vege");
            clearInterval(id);
            //console.log(map);
            console.log(temp);
            console.log(network);
            colorChange();
        }
    },2000);
}


function addEdges(){
    var graphJsonByNode=Get("http://localhost:4567/graph");
    var graphByNode=JSON.parse(graphJsonByNode);
    graphByNode=graphByNode.graph;
    var arr=new Array();
    for(var i=0;i<graphByNode.length;i++){
        arr.push(graphByNode[i].split(" "));
    }
    //console.log(arr);
        for(var i=0;i<arr.length;i++){
            var from=getId(arr[i][0]);
            var to=getId(arr[i][1]);
            if(typeof from==='undefined' || typeof to==='undefined'){
                continue;
            }else{
                try{
                    realEdges.add({
                        id:i, from: getId(arr[i][0]), to: getId(arr[i][1]), physics: true, color:{color:'blue'},
                        label:arr[i][2]
                    });
                }catch(err){}
            }

        }
}

function getId(param){
    for(var [key, val] of map.entries()){
        if(val.valueOf()==param.valueOf()){
            return key;
        }
    }
}

var colorArray=new Array();
var routeArray=new Array();

function colorChange(){
    var id=setInterval(function(){
        var colorJson=Get("http://localhost:4567/graphByNodeForColorChange");
        var colors=JSON.parse(colorJson);
        colors=colors.node;
        if(typeof colors!=='undefined'){
            colorArray.push(colors);
        }

        for(var i=0;i<array.length;i++){
            if(array._data[i].label==colors){
                //console.log(getId(colors));
                array.update({id:getId(colors), color:{background:'#f42525'}})
            }
        }

        var routeJson=Get("http://localhost:4567/routeStepByStep");
        var route=JSON.parse(routeJson);
        route=route.route;
        //console.log(route);

        if(typeof route!=='undefined'){
            edgeChange(route);
        }


        if(colorArray.length==size){
        console.log("color_vege");
            clearInterval(id);
            console.log(array);
            console.log(realEdges);
            for(var i=0;i<array.length;i++){
                if(array._data[i].label==colors){
                    //console.log(getId(colors));
                    array.update({id:getId(colors), color:{background:'#f42525'}})
                }
            }
            document.getElementsByName("button")[0].disabled = false;
        }
    },2000);
}




function edgeChange(param){

    for(var i=0;i<realEdges.length;i++){
        realEdges.update({id:i, color:{color:'blue'}});
    }

    var array=new Array();
    for(var i=0;i<param.length;i++){
        array.push(param[i].split(" "));
    }


    for(var i=0;i<array.length;i++){
        for(var j=0;j<array[i].length;j++){
            array[i][j]=getId(array[i][j]);
        }
    }

    for(var i=0;i<realEdges.length;i++){
        for(var j=0;j<array.length;j++){
            if((realEdges._data[i].to==array[j][0] || realEdges._data[i].to==array[j][1])
            && (realEdges._data[i].from==array[j][0] || realEdges._data[i].from==array[j][1])){

                realEdges.update({id:i, color:{color:'red'}});
            }
        }
    }

    //console.log(array);
}








graph();












