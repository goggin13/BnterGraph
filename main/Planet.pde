class Planet {
  String planet_id;
  String planet_url;
  color planetColor = color(255, 0, 0);
  Float radius = 15.0;
  float hoverRadius = 30;
  Float posX;
  float posY;
  float weight;
  float orbitRadius;
  PImage nodeImage;
  
  Planet (String id, String url, float w) {
    planet_id = id;
    planet_url = url;
    weight = w;
    nodeImage = loadImage(url);
  }
  
  void setRing (Ring o) {
    orbitRadius = o.getRadius();
    // r^2 = x^2 + y^2
    if (random(-1, 1) > 0) {
      posX = CENTER_X + (random(-1, 1) > 0 ? 1 : -1) * orbitRadius;
      posY = CENTER_Y;
    } else {
      posY = CENTER_Y + (random(-1, 1) > 0 ? 1 : -1) * orbitRadius;
      posX = CENTER_X;
    }
  }
  
  // true if the mouse is currently hovering on this node
  boolean isHoveredOn() {
    return abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius;
  }
  
  void getNextX () {
    
  }

  void getNextY () {
   
  }

  void erase () {
    
  }
  
  float getWeight () {
    return weight;
  }
  
  void showInfo () {
    float imgX = posX;
    float imgY = posY;
    int imgSize = 50;
    image(nodeImage, imgX, imgY);
  }

  void drawPlanet () {
    stroke(planetColor);
    fill(planetColor);
    //println(planet_id + " : (" + posX + ", " + posY + ")");
    ellipse(posX, posY, radius, radius);  
  }
  
  void draw () {
    if (isHoveredOn()) {
      showInfo();
    }
    drawPlanet();
  }
}

