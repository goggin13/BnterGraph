class Ring {
  float width = 1;
  float radius;
  float centerX;
  float centerY;
  color ringColor2 = color(0, 255, 100);
  color ringColor1 = color(0, 100, 255);
  ArrayList planets;
  float ringIndex = 0;
  float ringCount = 0;
  float ringPct;
  int counter = 0;
  
  Ring (float ringRadius,float i, float c) {
    radius = ringRadius;
    centerX = SCREEN_WIDTH / 2;
    centerY = SCREEN_HEIGHT / 2;
    planets = new ArrayList();
    ringIndex = i;
    ringCount = c;
    ringPct = ringIndex / ringCount;
  }
  
  void addPlanet (Planet p) {
    planets.add(p);
  }
  
  void drawMe () {
    noFill();
    stroke(lerpColor(ringColor1, ringColor2, ringPct));
    strokeWeight(width);
    ellipse(centerX, centerY, radius, radius );
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
