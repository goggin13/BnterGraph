class Edge {
  Node n1;
  Node n2;
  float weight;
  color edgeColor = color(255, 255, 255);
  color matureColor = color(255, 0, 255);
  int newBornTimer = 150;
  
  Edge(Node nn1, Node nn2, int w) {
    n1 = nn1;
    n2 = nn2;
    weight = w;
  }
    
  void setWeight(int w) {
    weight = w;
  }

  void addWeight(int add) {
    weight += add;
  }

  void draw() {
    if (newBornTimer > 0) {
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
  
  String toString() {
    return n1.getNid() + " - " + n2.getNid();
  }
  
}
