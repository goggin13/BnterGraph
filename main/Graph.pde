class Graph {
  ArrayList nodes;
  HashMap edgeKeys;
  ArrayList edgesToDraw;
  boolean paused = false;
  
  Graph() {
    nodes = new ArrayList();
    edgeKeys = new HashMap();
    edgesToDraw = new ArrayList();
  }

  void draw() {
    if (paused) {
      return;
    }
    background(backgroundColor);
    drawEdges();
    drawNodes();
  }

  void pause() {
    paused = true;
  }

  void start() {
    paused = false;
  }

  void drawEdges() {
    Iterator it = edgesToDraw.iterator();
    while (it.hasNext()) {
      Edge e = (Edge)it.next();
      e.draw();
    }
  }

  void drawNodes() {
    Iterator it = nodes.iterator();
    while (it.hasNext ()) {
      Node n = (Node)it.next();
      n.move();
    }
  }

  void addNode(Node n) {
    nodes.add(n);
  }

  Node getNode(String nid) {
    Iterator it = nodes.iterator();
    while (it.hasNext()) {
      Node n = (Node)it.next();
      if (n.getNid() == nid) {
        return n;
      }
    }
    return null;
  }

  Node addNodeIf(String nid, String url) {
    Node n = getNode(nid);
    if (n == null) {
      n = new Node(nid, url, g);
      nodes.add(n);
    }
    return n;
  }

  boolean containsEdge(String edgeKey) {
    return edgeKeys.containsKey(edgeKey);
  }

  void putEdge(Node n1, Node n2, int weight) {
    String edgeKey = n1.getNid() + n2.getNid();
    if (containsEdge(edgeKey)) {
      return;
    }
    Edge e = new Edge(n1, n2, weight);
    edgeKeys.put(edgeKey, e);
    edgesToDraw.add(e);
  }

  void addEdge(String nid1, String url1, String nid2, String url2, int weight) {
    Node n1 = addNodeIf(nid1, url1);
    Node n2 = addNodeIf(nid2, url2);
    putEdge(n1, n2, weight);
  }
}
