/*
 * A single point on the graph.  Identified by a unique 
 * string, in this case a username 
 */
class Node{
   float posX;
   float posY;
   float velX;
   float velY; 
   int radius = 12;
   int hoverRadius = 40;
   int xMin = 0 + radius;
   int xMax = SCREEN_WIDTH - radius;
   int yMin = 0 + radius;
   int yMax = SCREEN_HEIGHT - radius;
   String id = "noone";
   int infoWidth = 40;
   int infoHeight = 40;
   float maxVel = .2;
   color nodeColor = color(131, 245, 44);
   color matureColor = color(0, 0, 255);
   color hoverColor = backgroundColor;
   color youColor = color(255, 0, 0);
   color beenClickedColor = color(255, 255, 255);
   color speedUpColor = color(255, 0, 0);
   Graph graph;
   boolean isPaused = false;
   int speedUpTimer = 0;
   int newBornTimer = 100;
   boolean hasBeenClickedOn = false;
   boolean isYou = false;
   Info info;
   
   // The unique indentifier for this node, and the graph to put it on
   Node(String i, String url, Graph g, int conversations) {
     id = i;
     posX = random(xMin, xMax);
     posY = random(yMin, yMax);
     velX = random(-maxVel, maxVel); //(int)random(1, 1);
     velY = random(-maxVel, maxVel); //(int)random(1, 1);
     graph = g; 
     info = new Info(i, url, conversations);
     if (yourUsername.equals(i)) {
       matureColor = youColor;
       isYou = true;
       radius = radius * 2;
     }
   }
   
   // adjusts the nodes x,y based on its velocity, and then
   // calls draw
   void move() {
     if (speedUpTimer > 0){
       float oldVelX = velX;
       float oldVelY = velY;
       velX *= speedUpTimer / 5;
       velY *= speedUpTimer / 5;
       moveXY();
       velX = oldVelX;
       velY = oldVelY;
       speedUpTimer--;
     } else {
       moveXY();
     }
     if (posX < xMin || posX > xMax) {
       velX = -velX;
       moveX();
     }
     if (posY < yMin || posY > yMax) {
       velY = -velY;
       moveY();
     }
     draw();
  } 
  
  // display the information for this node
  void showInfo() {
    info.draw(posX, posY, radius);
  }

  boolean isClickedOn () {
    return clickedX > 0 && clickedY > 0 && abs(clickedX - posX) < hoverRadius && abs(clickedY - posY) < hoverRadius;
  }

  // true if the mouse is currently hovering on this node
  boolean isHoveredOn() {
    return (abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius)
            || info.isHoveredOn();
  }

  void draw() {
   if (!hasBeenClickedOn && isClickedOn()) {
     loadMoreFriends(getNid());
     hasBeenClickedOn = true;
     nodeColor = beenClickedColor;
   }
   noStroke();
   if (isHoveredOn()) {
     fill(hoverColor);
     showInfo();
     isPaused = true;
     speedUpTimer = 201;
   } else {
    isPaused = false;
    if (speedUpTimer > 0) {
      if (speedUpTimer == 200) {
        velX = -velX;
        velY = -velY;
      }
      float pct = (float)speedUpTimer / 200;
      fill(lerpColor(nodeColor, speedUpColor, pct));
    } else {
      if (newBornTimer > 0) {
        newBornTimer--;
        if (newBornTimer == 0) {
          nodeColor = matureColor;
        }
      }
      fill(nodeColor); 
    }
   }
   ellipse(posX, posY, radius, radius);
  }
  
  // adjust position based on velocity
  void moveXY() {
   if (isPaused) {
     return;
   }
   moveX();
   moveY();
  }
  
  // adjusts the x position based on x velocity
  void moveX() {
    posX = posX + velX;
  }
  
  // adjusts the y position based on y velocity
  void moveY() {
    posY = posY + velY;
  }
  
  /* GETTERS */
  
  float getX() {
    return posX;
  }
  
  float getY() {
    return posY;
  }
  
  String getNid() {
    return id;
  }
  
  String toString() {
    return getNid() + " : (" + getX() + ", " + getY() + ")";
  }
}
