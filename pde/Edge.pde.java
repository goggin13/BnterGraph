class Edge {
  Node n1;
  Node n2;
  float weight;
  color edgeColor = color(255, 0, 255);
  color highlightColor = color(255, 0, 0);
  
  Edge(Node nn1, Node nn2) {
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
	  color c = color(100 + (155 / 10 * weight), 0, 0)
    stroke(c);
    strokeWeight(weight);
    line(n1.getX(), n1.getY(), n2.getX(), n2.getY());
  }
  
}
