class Ring {
  float width = 1;
  float radius;
  float centerX;
  float centerY;
  color ringColor = color(15, 25, 255);
  ArrayList planets;
  int counter = 0;
  
  Ring (float ringRadius) {
    radius = ringRadius;
    centerX = SCREEN_WIDTH / 2;
    centerY = SCREEN_HEIGHT / 2;
    planets = new ArrayList();
  }
  
  void addPlanet (Planet p) {
    planets.add(p);
  }
  
  void drawMe () {
    noFill();
    stroke(ringColor);
    strokeWeight(width);
    ellipse(centerX, centerY, radius, radius);
  }
  
  void drawPlanets () {
    Iterator it = planets.iterator();
    while (it.hasNext ()) {
      Planet p = (Planet)it.next();
      p.draw();
    }
  }
  
  void flushPlanets () {
    planets.clear();
  }
  
  float getRadius () {
    return radius / 2;
  }
  
  void draw () {
    if (planets.size() == 0) {
      return;
    }
    
    if (!solarSystemIsPaused) {
      drawMe();
    }
    drawPlanets();
  }
  
}
