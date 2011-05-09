int SCREEN_WIDTH = 1000;
int SCREEN_HEIGHT = 1000;
BufferedReader reader;
String line;
ArrayList conversations;
ArrayList nodes;
Graph g;
color backgroundColor = color(0, 0, 0);
String activeNode = "";
int clickedX = 0;
int clickedY = 0;

void setup() {
  g = new Graph();
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  background(backgroundColor);
  g.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "lauren", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 2);
  g.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "kdiver", "http://bnter.com/web/assets/images/7483__w50_h50.png", 5);
  g.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "patrick", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 5);
  g.addEdge("lalando", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 3);
}

void loadMoreFriends(String name) {
  if (name.length() > 0) {
    println(name);
  }
}

void mouseClicked() {
  clickedX = mouseX;
  clickedY = mouseY;
}

void draw() {
  g.draw();
  clickedX = 0;
  clickedY = 0;
}


