class Edge {
  Node n1;
  Node n2;
  float weight;
  color edgeColor = color(255, 255, 255);
  color matureColor = color(255, 0, 255);
  int newBornTimer = 250;
  
  Edge(Node nn1, Node nn2, int w) {
    n1 = nn1;
    n2 = nn2;
    weight = 1;
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
    strokeWeight(weight);
    line(n1.getX(), n1.getY(), n2.getX(), n2.getY());
  }
  
  String toString() {
    return n1.getNid() + " - " + n2.getNid();
  }
  
}
