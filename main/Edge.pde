/*
 * An object of class Edge connects two nodes, and
 * draws that connecting line at the appropriate thickness
 * given it's weight */
class Edge {
  Node n1;
  Node n2;
  int newBornTimer = 150;                  // We want to flash the edge while it's new to the graph
  float weight;
  color edgeColor = color(255, 255, 255);
  color matureColor = color(255, 0, 255);  // color to use after newBornTimer expires
  
  // Pass the two Nodes, and the weight
  Edge(Node nn1, Node nn2, int w) {
    n1 = nn1;
    n2 = nn2;
    weight = w;
  }
  
  // reset the weight for this edge
  void setWeight(int w) {
    weight = w;
  }

  // Draw the edge
  void draw() {
    if (newBornTimer > 0) {  // different styling effects for new edges
      newBornTimer--;
      if (newBornTimer == 0) {
        edgeColor = matureColor;
      }
    } 
    stroke(edgeColor);   
    float sWeight = max((weight / maxEdgeWeight) * maxDisplayEdgeWeight, 1);
    strokeWeight(sWeight);
    line(n1.getX(), n1.getY(), n2.getX(), n2.getY());
  }

  // For debugging; return node ids of end points  
  String toString() {
    return n1.getNid() + " - " + n2.getNid();
  }
  
}
