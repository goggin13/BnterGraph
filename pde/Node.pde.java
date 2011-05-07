class Node{
   float posX;
   float posY;
   float velX;
   float velY; 
   int radius = 12;
   int xMin = 0 + radius;
   int xMax = SCREEN_WIDTH - radius;
   int yMin = 0 + radius;
   int yMax = SCREEN_HEIGHT - radius;
   int id = 0;
   int infoWidth = 40;
   int infoHeight = 40;
   color nodeColor = color(0, 0, 255);
   color hoverColor = color(0, 255, 0);
	 Graph parent;
	
   Node(int i, Graph g) {
     id = i;
     posX = random(xMin, xMax);
     posY = random(yMin, yMax);
     velX = random(-.1, .1); //(int)random(1, 1);
     velY = random(-.1, .1); //(int)random(1, 1);
     parent = g;
   }
   
   void move() {
     moveXY();
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
  
  void showInfo() {
		rect(posX, posY, infoWidth, infoHeight);
  }

	boolean isHoveredOn() {
		return abs(mouseX - posX) < radius && abs(mouseY - posY) < radius;
	}

  void draw() {
   noStroke();
   if (isHoveredOn()) {
	   fill(hoverColor);
		 showInfo();
		 //parent.pause();
	 } else {
		//parent.start();
		fill(nodeColor);
	 }
   ellipse(posX, posY, radius, radius);
  }
  
  void moveXY() {
   moveX();
   moveY();
  }
  
  void moveX() {
    posX = posX + velX;
  }
  
  void moveY() {
    posY = posY + velY;
  }
  
  float getX() {
    return posX;
  }
  
  float getY() {
    return posY;
  }
  
  int getNid() {
    return id;
  }
  
  String toString() {
    return "(" + getX() + ", " + getY() + ")";
  }
}
