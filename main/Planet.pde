class Planet {
  String planet_id;
  String planet_url;
  color planetColor = color(255, 0, 0);
  color outlineColor = color(15, 25, 255);
  Float radius = 15.0;
  float actualRadius = radius;
  float hoverRadius = 30;
  Float posX;
  float posY;
  float weight;
  float orbitRadius;
  float orbitAngle = 0;
  PImage nodeImage;
  float velocity;
  boolean iAmPausing = false;
  Info info;
  
  Planet (String id, String url, float w, int conversations) {
    planet_id = id;
    planet_url = url;
    weight = w;
    orbitAngle = random(1, 628) / 100; 
    velocity = random(-1, 1) > 0 ? .01 : -.01;
    info = new Info(planet_id, planet_url, conversations);
    move();
  }
  
  void multiplySize (float pct) {
    actualRadius = radius * pct;
  }
  
  float getRadius () {
    return radius;
  }
  
  void setRadius (float r) {
    orbitRadius = r;
  }
  
  void setRing (Ring ring) {
    orbitRadius = ring.getRadius();
  }
  
  // true if the mouse is currently hovering on this node
  boolean isHoveredOn() {
    return abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius;
  }
  
  boolean isClickedOn () {
    return clickedX > 0 && clickedY > 0 && abs(clickedX - posX) < hoverRadius && abs(clickedY - posY) < hoverRadius;
  }
  
  void move () {
    orbitAngle += velocity;
    posX = CENTER_X + orbitRadius * cos(orbitAngle); 
    posY = CENTER_Y + orbitRadius * sin(orbitAngle);    
  }
  
  float getWeight () {
    return weight;
  }
  
  void drawPlanet () {
    stroke(outlineColor);
    fill(planetColor);
    ellipse(posX, posY, actualRadius, actualRadius);  
  }
  
  void makeCenter () {
    String url = "/?user_name=" + planet_id + "&hide_welcome=1"; 
    link(url); 
  }
  
  void draw () {
    drawPlanet();
    if (isHoveredOn()) {
      info.draw(posX + actualRadius / 2, posY);
      solarSystemIsPaused = true;
      iAmPausing = true;
    } else {
      if (iAmPausing && solarSystemIsPaused) {
        solarSystemIsPaused = false;
        iAmPausing = false;
      }
      if (!solarSystemIsPaused) {
        move();
      }
      
    }
    if (isClickedOn()) {
      makeCenter();  
    }
  }
}

