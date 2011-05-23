class VizManager {
  String graphType = "solar";
  Graph graph;
  SolarSystem solarSystem;
  
  VizManager () {
     graph = new Graph();
     solarSystem = new SolarSystem(yourUsername);
  }  
  
  void addEdge (String nid1, String url1, String nid2, String url2, int weight) {
    graph.addEdge(nid1, url1, nid2, url2, weight);
    // only add in edges for you, unlike the graph, solar system only shows your layer of
    // the network.  
    if (nid1.equals(yourUsername) || nid2.equals(yourUsername)) { 
      solarSystem.addPlanet(nid1, url1, weight);
      solarSystem.addPlanet(nid2, url2, weight);
    }
  }
  
  void setGraphType (String t) {
    graphType = t;
  }
  
  String getGraphType () {
    return graphType;
  }
  
  void draw () {
    if (graphType.equals("graph")) {
      graph.draw();
    } else {
      solarSystem.draw();
    }
  }
  
}
