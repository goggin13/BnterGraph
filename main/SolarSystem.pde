class SolarSystem {
  float centerRadius = 40;
  float posX = (SCREEN_WIDTH) / 2;
  float posY = (SCREEN_HEIGHT) / 2;
  float padding = 30;
  color centerColor = color(208, 94, 52);
  int counter = 0;
  float maxPlanetWeight = 0;
  String root_nid;
  String root_url;
  HashMap rings;             // weight to rings
  ArrayList ringWeights;
  ArrayList planetList; 
  ArrayList planets;
  int ringCount = 10;
  
  SolarSystem (String root) {
    root_nid = root;
    rings = new HashMap(); 
    ringWeights = new ArrayList();
    planetList = new ArrayList();
    planets = new ArrayList();  
    makeRingBuckets();  
  }
  
  void drawCenter () {
    fill(centerColor);
    stroke(backgroundColor);
    ellipse(posX, posY, centerRadius, centerRadius);
  }

  void makeRingBuckets () {
    float ringRadius;
    for (int i = 0; i < ringCount; i++) {
       ringRadius = (SCREEN_WIDTH - padding) / ringCount * (i + 1);
       Ring r = new Ring(ringRadius);
       rings.put(i, r);
    }
  }
  
  void addPlanetToRing (Planet p) {
    float w = p.getWeight();
    float pct = w / maxPlanetWeight;
    Ring ring = getRingForWeight(w);
    ring.addPlanet(p);
    p.setRing(ring);
    p.multiplySize(1 + pct);
  }
  
  void drawRings () {
    Iterator it = rings.keySet().iterator();
    while (it.hasNext ()) {
      Ring r = (Ring)rings.get(it.next());
      r.draw();
    }
  }
  
  void flushRings () {
    Iterator it = rings.keySet().iterator();
    while (it.hasNext ()) {
      Ring ring = (Ring)rings.get(it.next());
      ring.flushPlanets();
    }
  }
  
  void rehashPlanets () {
    flushRings();
    Iterator it = planets.iterator();
    while (it.hasNext ()) {
      Planet p = (Planet)it.next();
      addPlanetToRing(p);
    }
  }
  
  Ring getRingForWeight (float weight) {
    // change weight to an int between 0 and ringCount
    float pct = weight / maxPlanetWeight * 10;
    int ringNum = ringCount - round(pct);
    if (ringNum == ringCount) {
      ringNum--;
    }
    return (Ring)rings.get(ringNum);
  }
  
  void addPlanet (String nid, String url, int weight) {
    if (nid.equals(root_nid) || planetList.contains(nid)) {
      root_url = url;
      return;
    }
    Planet p = new Planet(nid, url, weight, weight);
    planetList.add(nid);
    planets.add(p);
    if (weight > maxPlanetWeight) {
      maxPlanetWeight = weight;
      rehashPlanets();
    } else {
      addPlanetToRing(p);
    }
  }
  
  void draw () {
    //background(bg_img);
    if (!solarSystemIsPaused) {
      background(backgroundColor);
      drawCenter();
    }
    drawRings();
  } 
  
}
