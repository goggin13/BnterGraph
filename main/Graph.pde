/*
 * An object of class Graph represents a collection of nodes
 * and edges that it draws to the canvas */ 
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

  // Draw the graph, unless it's paused
  void draw() {
    if (paused) {
      return;
    }
    background(backgroundColor);
    drawEdges();
    drawNodes();
  }
  
  // pause the graph, stopping motion
  void pause() {
    paused = true;
  }

  // start the graph if it is paused
  void start() {
    paused = false;
  }

  // draw all the edges in this graph
  void drawEdges() {
    Iterator it = edgesToDraw.iterator();
    while (it.hasNext()) {
      Edge e = (Edge)it.next();
      e.draw();
    }
  }

  // draw all the nodes in this graph
  void drawNodes() {
    Iterator it = nodes.iterator();
    while (it.hasNext ()) {
      Node n = (Node)it.next();
      n.move();
    }
  }

  // Return the node in this Graph with NodeID = nid, 
  // or null if there are none
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

  // Create and add a new Node with ID = nid, and image url,
  // unless it is already in the graph
  Node addNodeIf(String nid, String url) {
    Node n = getNode(nid);
    if (n == null) {
      n = new Node(nid, url, this);
      nodes.add(n);
    }
    return n;
  }

  // Add a single edge to the graph, connecting Nodes n1 and n2,
  // with the given weight, unless those nodes have an existing edge
  // already
  void putEdge(Node n1, Node n2, int weight) {
    String edgeKey = n1.getNid() + n2.getNid();
    if (edgeKeys.containsKey(edgeKey)) { // does this edge already exist?
      return;
    }
    Edge e = new Edge(n1, n2, weight);
    edgeKeys.put(edgeKey, e);
    edgesToDraw.add(e);
  }

  // Create the objects to add an edge between the two nodes.  Creates the nodes
  // if they don't exist, and adds an edge between them.
  void addEdge(String nid1, String url1, String nid2, String url2, int weight) {
    Node n1 = addNodeIf(nid1, url1);
    Node n2 = addNodeIf(nid2, url2);
    putEdge(n1, n2, weight);
  }
}
