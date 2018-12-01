function Get(yourUrl){
    var Httpreq = new XMLHttpRequest(); // a new request
    Httpreq.open("GET",yourUrl,false);
    Httpreq.send(null);
    return Httpreq.responseText;
}
    var graph=Get("http://localhost:4567/graph");

    var obj=JSON.parse(graph);

    var linesArray=obj.graph;
    var itemArray=new Array();
    for(var i=0;i<linesArray.length;i++){
        itemArray.push(linesArray[i].split(" "));
    }

    var nodes2 = new Array();


    for(var i=0;i<itemArray.length;i++){
        for(var j=0;j<itemArray[i].length;j++){
            if((j==0 || j==1) && !nodes2.includes(itemArray[i][j])){
                nodes2.push(itemArray[i][j]);
            }
        }
    }


    var realNodes=new Array();

    var map = new Map();

    for(var i=0;i<nodes2.length;i++){
        realNodes.push(
        {id: i,label: nodes2[i]}
        );
        map.set(i,nodes2[i]);
    }

    function getId(param){
        for(var [key, val] of map.entries()){
            if(val.valueOf()==param.valueOf()){
                return key;
            }
        }
    }


    var getRoute=Get("http://localhost:4567/route");
    var objRoute=JSON.parse(getRoute);
    var route=objRoute.route;


    var color=new Array();

    for(var i=0;i<route.length;i++){
        var temp=route[i].split(" ");
        color.push(temp);
    }

    function getColor(param1,param2){
        var temp=new String(param1+" "+param2);
        var temp2=new String(param2+" "+param1);
        for(var i=0;i<route.length;i++){
            var r=new String(route[i].trim());
            if(temp.valueOf()==r.valueOf() || temp2.valueOf()==r.valueOf()){
                return 'red';
                break;
            }
        }
        return 'blue';
    }

    var realEdges=new Array();

     for(var i=0;i<itemArray.length;i++){
        realEdges.push(
            {id: i,from: getId(itemArray[i][0]), to: getId(itemArray[i][1]), label: itemArray[i][2],
            color:{color:getColor(itemArray[i][0],itemArray[i][1])}}
        );
    }



  // create a network
  var container = document.getElementById('mynetwork');
  var data = {
    nodes: realNodes,
    edges: realEdges
  };
  var options = {
    nodes : {
      shape: 'circle',
      size: 10
    }
  };
 var network = new vis.Network(container, data, options);

    document.getElementsByName("button")[0].disabled = false;
    document.getElementsByName("basic")[0].disabled=false;
