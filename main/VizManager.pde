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
    solarSystem.addPlanet(nid1, url1, weight);
    solarSystem.addPlanet(nid2, url2, weight);
  }
  
  void setGraphType (String t) {
    graphType = t;
  }
  
  void draw () {
    if (graphType.equals("graph")) {
      graph.draw();
    } else {
      solarSystem.draw();
    }
  }
  
}
