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
  float orbitAngle = 0;
  PImage nodeImage;
  float velocity;
  
  Planet (String id, String url, float w) {
    planet_id = id;
    planet_url = url;
    weight = w;
    nodeImage = loadImage(url);
    orbitAngle = random(1, 628) / 100; 
    velocity = random(-1, 1) > 0 ? .01 : -.01;
    move();
  }
  
  void setRing (Ring ring) {
    orbitRadius = ring.getRadius();
  }
  
  // true if the mouse is currently hovering on this node
  boolean isHoveredOn() {
    return abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius;
  }
  
  void move () {
    orbitAngle += velocity;
    posX = CENTER_X + orbitRadius * cos(orbitAngle); 
    posY = CENTER_Y + orbitRadius * sin(orbitAngle);    
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
    ellipse(posX++, posY++, radius, radius);  
  }
  
  void draw () {
    if (isHoveredOn()) {
      showInfo();
    } else {
      move();
      drawPlanet();
    }
    
  }
}

