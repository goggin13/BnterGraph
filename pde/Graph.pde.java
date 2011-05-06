class Graph {
  HashMap nodes;
  HashMap edges;
  
  Graph() {
    nodes = new HashMap();
    edges = new HashMap();
  }
  
  void draw() {
    drawEdges();
		drawNodes();
  }
    
  void drawEdges() {
    Iterator it = edges.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry)it.next();
      Edge e = (Edge)pairs.getValue();
      e.draw();
    }  
  }
  
  void drawNodes() {
    Iterator it = nodes.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry)it.next();
      Node n = (Node)pairs.getValue();
      n.move();
    }  
  }
  
  Node getNode(int nid) {
    return (Node)nodes.get(nid);
  }
  
  Node addNodeIf(int nid) {
    if (!containsNode(nid)) {
      addNode(new Node(nid));
    }
    return getNode(nid);
  }

  void putEdge(Edge e, String edgeKey) {
    edges.put(edgeKey, e);
  }
  
  void addEdge(int nid1, int nid2) {
     String edgeKey = max(nid1, nid2) + min(nid1, nid2);
     if (containsEdge(edgeKey)) {
		   Edge e = edges.get(edgeKey);
		   e.addWeight(1);
       return;
     }
     Node n1 = addNodeIf(nid1);
     Node n2 = addNodeIf(nid2);
     Edge e = new Edge(n1, n2);
     putEdge(e, edgeKey);     
  }
  
}
