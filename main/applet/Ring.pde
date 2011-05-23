class Ring {
  float width = 1;
  float radius;
  float centerX;
  float centerY;
  color ringColor1 = color(15, 25, 40);
  color ringColor2 = color(0, 0, 255);
  ArrayList planets;
  int counter = 0;
  int ringIndex = 0;
  int ringCount = 0;
  float ringPct;
  
  Ring (float ringRadius, int i, int c) {
    radius = ringRadius;
    centerX = SCREEN_WIDTH / 2;
    centerY = SCREEN_HEIGHT / 2;
    planets = new ArrayList();
    ringIndex = i;
    ringCount = c;
    ringPct = ringIndex / ringCount;
    println(ringPct);
  }
  
  void addPlanet (Planet p) {
    planets.add(p);
  }
  
  void drawMe () {
    noFill();
    stroke(lerpColor(ringColor1, ringColor2, 1));
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
