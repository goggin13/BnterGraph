import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class main extends PApplet {

/*
 * An object of class Edge connects two nodes, and
 * draws that connecting line at the appropriate thickness
 * given it's weight */
class Edge {
  Node n1;
  Node n2;
  int newBornTimer = 150;                  // We want to flash the edge while it's new to the graph
  float weight;
  int edgeColor = color(255, 255, 255);
  int matureColor = color(255, 0, 255);  // color to use after newBornTimer expires
  
  // Pass the two Nodes, and the weight
  Edge(Node nn1, Node nn2, int w) {
    n1 = nn1;
    n2 = nn2;
    weight = w;
  }
  
  // reset the weight for this edge
  public void setWeight(int w) {
    weight = w;
  }

  // Draw the edge
  public void draw() {
    if (newBornTimer > 0) {  // different styling effects for new edges
      newBornTimer--;
      if (newBornTimer == 0) {
        edgeColor = matureColor;
      }
    } 
    stroke(edgeColor);   
    float sWeight = max((weight / maxEdgeWeight) * maxDisplayEdgeWeight, 1);
    strokeWeight(sWeight);
    line(n1.getX(), n1.getY(), n2.getX(), n2.getY());
  }

  // For debugging; return node ids of end points  
  public String toString() {
    return n1.getNid() + " - " + n2.getNid();
  }
  
}
/*
 * An object of class Graph represents a collection of nodes
 * and edges that it draws to the canvas */ 
class Graph {
  ArrayList nodes;
  HashMap edgeKeys;
  ArrayList edgesToDraw;
  boolean paused = false;
  
  Graph() {
    nodes = new ArrayList();
    edgeKeys = new HashMap();
    edgesToDraw = new ArrayList();
  }

  // Draw the graph, unless it's paused
  public void draw() {
    if (paused) {
      return;
    }
    background(backgroundColor);
    drawEdges();
    drawNodes();
  }
  
  // pause the graph, stopping motion
  public void pause() {
    paused = true;
  }

  // start the graph if it is paused
  public void start() {
    paused = false;
  }

  // draw all the edges in this graph
  public void drawEdges() {
    Iterator it = edgesToDraw.iterator();
    while (it.hasNext()) {
      Edge e = (Edge)it.next();
      e.draw();
    }
  }

  // draw all the nodes in this graph
  public void drawNodes() {
    Iterator it = nodes.iterator();
    while (it.hasNext ()) {
      Node n = (Node)it.next();
      n.move();
    }
  }

  // Return the node in this Graph with NodeID = nid, 
  // or null if there are none
  public Node getNode(String nid) {
    Iterator it = nodes.iterator();
    while (it.hasNext()) {
      Node n = (Node)it.next();
      if (n.getNid() == nid) {
        return n;
      }
    }
    return null;
  }

  // Create and add a new Node with ID = nid, and image url,
  // unless it is already in the graph
  public Node addNodeIf(String nid, String url, int weight) {
    Node n = getNode(nid);
    if (n == null) {
      n = new Node(nid, url, this, weight);
      nodes.add(n);
    }
    return n;
  }

  // Add a single edge to the graph, connecting Nodes n1 and n2,
  // with the given weight, unless those nodes have an existing edge
  // already
  public void putEdge(Node n1, Node n2, int weight) {
    String edgeKey = n1.getNid() + n2.getNid();
    if (edgeKeys.containsKey(edgeKey)) { // does this edge already exist?
      return;
    }
    Edge e = new Edge(n1, n2, weight);
    edgeKeys.put(edgeKey, e);
    edgesToDraw.add(e);
  }

  // Create the objects to add an edge between the two nodes.  Creates the nodes
  // if they don't exist, and adds an edge between them.
  public void addEdge(String nid1, String url1, String nid2, String url2, int weight) {
    Node n1 = addNodeIf(nid1, url1, weight);
    Node n2 = addNodeIf(nid2, url2, weight);
    putEdge(n1, n2, weight);
  }
}
/*
 * An instance of class Info is a graphical object that
 * displays its information in a styled box when info.draw()
 * is called */
class Info {
 PImage infoImage;                        // image associated with this info display
 String username;                         // user name to display in this info
 int conversation_count;                  // number of conversations they've had
 float pointerWidth = 10;                 // width of the triangle pointer at the caller
 float infoWidth = 120;                    
 float infoHeight = 120;
 int infoFill = color(255, 255, 255);
 int fontColor = color(200, 0, 0);
 float imgSize = 50;
 float fontSize = 12;
 boolean isHovered = false;
 boolean isClicked = false;
 
 // (the username, the url of the image to display, the number of conversations they've had)
 Info (String u, String img_url, int c) {
   infoImage = loadImage(img_url);
   username = u;
   conversation_count = c;
 }
 
 // Primary method for Info; draws the display box
 // objWidth is the width of the object we are pointing at; so we 
 // can flip to the other side if we need to. 
 public void draw (float posX, float posY, float objWidth) {
   posX = posX + objWidth / 2;
   float infoX = posX + pointerWidth;
   float infoY = posY - pointerWidth - 5;
   float pointerX = posX;
   float pointerY = posY;

   boolean isRight = true;
   if (infoX + infoWidth > SCREEN_WIDTH) {
     isRight = false;
     pointerX = posX - objWidth;
     infoX = posX - infoWidth - pointerWidth - objWidth;
   } 
   
   if (infoY + infoHeight > SCREEN_HEIGHT) {
     infoY = posY - infoHeight + pointerWidth + 5; 
   }
   
   float imgX = infoX + 2;
   float imgY = infoY + 2;
   float textX = imgX;
   float textY = imgY + imgSize + 5 + fontSize / 2;
   
   setStrokeFill();
   drawPointer(pointerX, pointerY, isRight);
   drawOutline(infoX, infoY);
   image(infoImage, imgX, imgY);
   //fill(color(180, 45, 190));
   //rect(imgX, imgY, imgSize, imgSize);
   drawInfo(textX, textY);
   setHoveredOn(infoX, infoY);
   setClickedOn(infoX, infoY);
   if (isClickedOn()) {
     link("http://bnter.com/" + username, "_blank");
   }
 }
 
 public boolean isHoveredOn () {
   return isHovered;
 } 
 
 // returns true if the clickedX is within this info box
 public boolean isClickedOn () {
   return isClicked;
 }
 
 public boolean inRange (float posX, float posY, float target_x, float target_y) {
   return target_x > posX 
          && target_x < posX + infoWidth 
          && target_y > posY
          && target_y < posY + infoHeight;
 }
 
 public void setHoveredOn (float posX, float posY) {
   isHovered = inRange(posX, posY, mouseX, mouseY);
 }
 
 public void setClickedOn (float posX, float posY) {
   isClicked = isHovered && inRange(posX, posY, clickedX, clickedY);
 }
 
 // Draws the pointer, a triangle which points from
 // the display box towards the relevant object
 public void drawPointer (float posX, float posY, boolean isRight) {
   float X2 = posX + pointerWidth * (isRight ? 1 : -1);
   float X3 = X2;
   float Y2 = posY + pointerWidth;
   float Y3 = posY - pointerWidth;
   
   triangle(posX, posY, X2, Y2, X3, Y3);
 }
 
 // Draws the lines of text following the user image
 public void drawInfo (float posX, float posY) {
  fill(fontColor);
  text(username, posX, posY);
  posY = posY + fontSize + 2;
  text(conversation_count + " conversations", posX, posY); 
  posY = posY + fontSize + 2;
  text("Click to see my profile", posX, posY);
 }
 
 // Set the stroke and fill for the shapes used
 public void setStrokeFill () {
   noStroke();
   fill(infoFill);
 }
 
 // Draw the outline of the display box
 public void drawOutline (float posX, float posY) {
   rect(posX, posY, infoWidth, infoHeight);
 }
 
}
/* 
 * This is the main script for development, in the Processing environment.
 * For production, in a web browser, see Main_web.pde.js */
int SCREEN_WIDTH = 1000;
int SCREEN_HEIGHT = 1000;
int backgroundColor = color(0, 0, 0);
int clickedX = 0;
int clickedY = 0;
int maxEdgeWeight = 10;
int maxDisplayEdgeWeight = 5;
String yourUsername = "matt";
VizManager manager;
float CENTER_X = (SCREEN_WIDTH) / 2;
float CENTER_Y = (SCREEN_HEIGHT) / 2;
//String bg_url = "http://lh5.ggpht.com/BJd07kKZyj7L7-Y0KH54Osqm8vRZJ7giQJIHqQBusPjfqtG-Ezjg5nPrcjksOfwRV9kAK0YT_3tGmAk";
//PImage bg_img;
boolean solarSystemIsPaused = false;

public void setup() {
  //bg_url += "=s" + SCREEN_WIDTH + "-c";
  //bg_img = loadImage(bg_url, "png");
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

public void loadMoreFriends(String name) {
  if (name.length() > 0) {
  }
}

public void mouseClicked() {
  clickedX = mouseX;
  clickedY = mouseY;
}

public void draw() {
  manager.draw();
  clickedX = 0;
  clickedY = 0;
}


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
   float maxVel = .2f;
   int nodeColor = color(131, 245, 44);
   int matureColor = color(0, 0, 255);
   int hoverColor = backgroundColor;
   int youColor = color(255, 0, 0);
   int beenClickedColor = color(255, 255, 255);
   int speedUpColor = color(255, 0, 0);
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
   public void move() {
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
  public void showInfo() {
    info.draw(posX, posY, radius);
  }

  public boolean isClickedOn () {
    return clickedX > 0 && clickedY > 0 && abs(clickedX - posX) < hoverRadius && abs(clickedY - posY) < hoverRadius;
  }

  // true if the mouse is currently hovering on this node
  public boolean isHoveredOn() {
    return (abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius)
            || info.isHoveredOn();
  }

  public void draw() {
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
  public void moveXY() {
   if (isPaused) {
     return;
   }
   moveX();
   moveY();
  }
  
  // adjusts the x position based on x velocity
  public void moveX() {
    posX = posX + velX;
  }
  
  // adjusts the y position based on y velocity
  public void moveY() {
    posY = posY + velY;
  }
  
  /* GETTERS */
  
  public float getX() {
    return posX;
  }
  
  public float getY() {
    return posY;
  }
  
  public String getNid() {
    return id;
  }
  
  public String toString() {
    return getNid() + " : (" + getX() + ", " + getY() + ")";
  }
}

class LoadingMessages {
  ArrayList messages;
  
  LoadingMessages () {
    messages = new ArrayList();
    messages.add("a few bits tried to escape, but we caught them");
    messages.add("checking the gravitational constant in your locale...");
    messages.add("we love you just the way you are");
    messages.add("we're testing your patience...");
    messages.add("don't think of purple hippos");
    messages.add("follow the white rabbit...");
    messages.add("the bits are flowing slowly today...");
    messages.add("it's still faster than you could draw it!!!");
    messages.add("Hang on a sec, I know your data is here somewhere");
    messages.add("Are we there yet?");
    messages.add("Have you lost weight?");
    messages.add("Searching for Answer to Life, the Universe, and Everything...");
    messages.add("It's not you. It's me.");
  }
  
  public String getRandom () {
    return (String)messages.get((int)random(0, messages.size()));
  }
}

class MyProgressBar{
  float max;
  float posX;
  float posY;
  float posX2;
  float posY2;
  float current;
  float increment;
  float blockWidth = 20;
  float width = SCREEN_WIDTH * .8f;
  float height = 50;
  int outerColor = color(50, 50, 50);
  int innerStartColor = color(0, 0, 20);
  int innerEndColor = color(0, 0, 255);
  int padding = 3;
  int innerPadding = 1;
  float innerHeight = height - padding * 2;
  //PFont fontA = loadFont("Arial"); 
  
  MyProgressBar (float m) {
    max = m;
    posX = (SCREEN_WIDTH - width) / 2;
    posY = (SCREEN_HEIGHT - height) / 2;
    increment = (width - (2 * padding)) / max;
    posX2 = posX + padding;
    posY2 = posY + padding;
    drawOuter();
    LoadingMessages lm = new LoadingMessages();
    updateCaption(lm.getRandom());
  }
  
  public void updateCaption(String c) {
    // Set the font and its size (in units of pixels)  
    //textFont(fontA, 32); 
 		//fill(backgroundColor);
		//rect(posX, posY - 15, 200, 200);
    textSize(32);
    fill(255);
    // Use fill() to change the value or color of the text   
    text(c, posX, posY - 15);
  }
  
  public void updatePB (float p) {
    if (p > max) {
      return;
    }
    current = p;
    draw();
  }
  
  public void drawOuter () {
    fill(outerColor);
    rect(posX, posY, width, height);
  }
  
  public void drawInner () {
    float completedPct = (current / max);
    float completed = completedPct * increment * 1000;
    fill(lerpColor(innerStartColor, innerEndColor, completedPct));
    while (posX2 + blockWidth <= posX + padding + completed) {
      rect(posX2, posY2, blockWidth, innerHeight);
      posX2 = posX2 + blockWidth + innerPadding;
    } 
  }

  public void draw () {
    drawInner();
  }

}

class Planet {
  String planet_id;
  String planet_url;
  int planetColor = color(255, 0, 0);
  int outlineColor = color(15, 25, 255);
  Float radius = 15.0f;
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
    velocity = random(-1, 1) > 0 ? .01f : -.01f;
    info = new Info(planet_id, planet_url, conversations);
    move();
  }
  
  public void multiplySize (float pct) {
    actualRadius = radius * pct;
  }
  
  public float getRadius () {
    return radius;
  }
  
  public void setRadius (float r) {
    orbitRadius = r;
  }
  
  public void setRing (Ring ring) {
    orbitRadius = ring.getRadius();
  }
  
  // true if the mouse is currently hovering on this node
  public boolean isHoveredOn() {
    return (abs(mouseX - posX) < hoverRadius && abs(mouseY - posY) < hoverRadius)
            || info.isHoveredOn();
  }
  
  public boolean isClickedOn () {
    return clickedX > 0 && clickedY > 0 && abs(clickedX - posX) < hoverRadius && abs(clickedY - posY) < hoverRadius;
  }
  
  public void move () {
    orbitAngle += velocity;
    posX = CENTER_X + orbitRadius * cos(orbitAngle); 
    posY = CENTER_Y + orbitRadius * sin(orbitAngle);    
  }
  
  public float getWeight () {
    return weight;
  }
  
  public void drawPlanet () {
    stroke(outlineColor);
    fill(planetColor);
    ellipse(posX, posY, actualRadius, actualRadius);
  }
  
  public void makeCenter () {
    String url = "/?user_name=" + planet_id + "&hide_welcome=1"; 
    link(url); 
  }
  
  public void draw () {
    if (isHoveredOn()) {
      info.draw(posX, posY, actualRadius);
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
    if (!solarSystemIsPaused || iAmPausing) {
      drawPlanet();
    }
    if (isClickedOn()) {
      makeCenter();  
    }
  }
}

class Ring {
  float width = 1;
  float radius;
  float centerX;
  float centerY;
  int ringColor1 = color(15, 25, 40);
  int ringColor2 = color(0, 0, 255);
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
  
  public void addPlanet (Planet p) {
    planets.add(p);
  }
  
  public void drawMe () {
    noFill();
    stroke(lerpColor(ringColor1, ringColor2, 1));
    strokeWeight(width);
    ellipse(centerX, centerY, radius, radius );
  }
  
  public void drawPlanets () {
    Iterator it = planets.iterator();
    while (it.hasNext ()) {
      Planet p = (Planet)it.next();
      p.draw();
    }
  }
  
  public void flushPlanets () {
    planets.clear();
  }
  
  public float getRadius () {
    return radius / 2;
  }
  
  public void draw () {
    if (planets.size() == 0) {
      return;
    }
    
    if (!solarSystemIsPaused) {
      drawMe();
    }
    drawPlanets();
  }
  
}
class SolarSystem {
  float centerRadius = 40;
  float posX = (SCREEN_WIDTH) / 2;
  float posY = (SCREEN_HEIGHT) / 2;
  float padding = 30;
  int centerColor = color(208, 94, 52);
  int counter = 0;
  float maxPlanetWeight = 0;
  String root_nid;
  String root_url = "";
  PImage root_image;
  HashMap rings;             // weight to rings
  ArrayList ringWeights;
  ArrayList planetList; 
  ArrayList planets;
  int ringCount = 10;
  int imgSize = 50;
  
  SolarSystem (String root) {
    root_nid = root;
    rings = new HashMap(); 
    ringWeights = new ArrayList();
    planetList = new ArrayList();
    planets = new ArrayList();  
    makeRingBuckets();  
  }
  
  public void drawCenter () {
    if (root_url.equals("")) {
      fill(centerColor);
      stroke(backgroundColor);
      ellipse(posX, posY, centerRadius, centerRadius);
    } else{
      image(root_image, posX, posY); 
    }
  }

  public void makeRingBuckets () {
    float ringRadius;
    for (int i = 0; i < ringCount; i++) {
       ringRadius = (SCREEN_WIDTH - padding) / ringCount * (i + 1);
       Ring r = new Ring(ringRadius, i, ringCount);
       rings.put(i, r);
    }
  }
  
  public void addPlanetToRing (Planet p) {
    float w = p.getWeight();
    float pct = w / maxPlanetWeight;
    Ring ring = getRingForWeight(w);
    ring.addPlanet(p);
    p.setRing(ring);
    p.multiplySize(1 + pct);
  }
  
  public void drawRings () {
    Iterator it = rings.keySet().iterator();
    while (it.hasNext ()) {
      Ring r = (Ring)rings.get(it.next());
      r.draw();
    }
  }
  
  public void flushRings () {
    Iterator it = rings.keySet().iterator();
    while (it.hasNext ()) {
      Ring ring = (Ring)rings.get(it.next());
      ring.flushPlanets();
    }
  }
  
  public void rehashPlanets () {
    flushRings();
    Iterator it = planets.iterator();
    while (it.hasNext ()) {
      Planet p = (Planet)it.next();
      addPlanetToRing(p);
    }
  }
  
  public Ring getRingForWeight (float weight) {
    // change weight to an int between 0 and ringCount
    float pct = weight / maxPlanetWeight * 10;
    int ringNum = ringCount - round(pct);
    if (ringNum == ringCount) {
      ringNum--;
    }
    return (Ring)rings.get(ringNum);
  }
  
  public void addPlanet (String nid, String url, int weight) {
    if (nid.equals(root_nid) || planetList.contains(nid)) {
      if (root_url.equals("")) {
        posX = posX - imgSize / 2;
        posY = posY - imgSize / 2;
        root_url = url;
        root_image = loadImage(root_url);
      }
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
  
  public void draw () {
    //background(bg_img);
    if (!solarSystemIsPaused) {
      background(backgroundColor);
      drawCenter();
    }
    drawRings();
  } 
  
}
class VizManager {
  String graphType = "solar";
  Graph graph;
  SolarSystem solarSystem;
  
  VizManager () {
     graph = new Graph();
     solarSystem = new SolarSystem(yourUsername);
  }  
  
  public void addEdge (String nid1, String url1, String nid2, String url2, int weight) {
    graph.addEdge(nid1, url1, nid2, url2, weight);
    // only add in edges for you, unlike the graph, solar system only shows your layer of
    // the network.  
    if (nid1.equals(yourUsername) || nid2.equals(yourUsername)) { 
      solarSystem.addPlanet(nid1, url1, weight);
      solarSystem.addPlanet(nid2, url2, weight);
    }
  }
  
  public void setGraphType (String t) {
    graphType = t;
  }
  
  public String getGraphType () {
    return graphType;
  }
  
  public void draw () {
    if (graphType.equals("graph")) {
      graph.draw();
    } else {
      solarSystem.draw();
    }
  }
  
}
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "main" });
  }
}
