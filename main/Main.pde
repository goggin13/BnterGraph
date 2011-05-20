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
int maxEdgeWeight = 10;
int maxDisplayEdgeWeight = 5;
String yourUsername = "matt";
VizManager manager;
float CENTER_X = (SCREEN_WIDTH) / 2;
float CENTER_Y = (SCREEN_HEIGHT) / 2;
String bg_url = "http://lh5.ggpht.com/BJd07kKZyj7L7-Y0KH54Osqm8vRZJ7giQJIHqQBusPjfqtG-Ezjg5nPrcjksOfwRV9kAK0YT_3tGmAk";
PImage bg_img;


void setup() {
  bg_url += "=s" + SCREEN_WIDTH + "-c";
  bg_img = loadImage(bg_url, "png");
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  manager = new VizManager();
  
  manager.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "lauren", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 2);
  manager.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "kdiver", "http://bnter.com/web/assets/images/7483__w50_h50.png", 5);
  manager.addEdge("matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "patrick", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 10);
  manager.addEdge("lalando", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 
            "matt", "http://bnter.com/web/assets/images/8996__w50_h50.jpg", 3);
}

void loadMoreFriends(String name) {
  if (name.length() > 0) {
  }
}

void mouseClicked() {
  clickedX = mouseX;
  clickedY = mouseY;
}

void draw() {
  manager.draw();
  clickedX = 0;
  clickedY = 0;
}


