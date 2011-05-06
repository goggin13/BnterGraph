
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
  
  String getRandom () {
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
  float width = SCREEN_WIDTH * .8;
  float height = 50;
  color outerColor = color(50, 50, 50);
  color innerStartColor = color(0, 0, 20);
  color innerEndColor = color(0, 0, 255);
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
  
  void updateCaption(String c) {
    // Set the font and its size (in units of pixels)  
    //textFont(fontA, 32); 
 		//fill(backgroundColor);
		//rect(posX, posY - 15, 200, 200);
    textSize(32);
    fill(255);
    // Use fill() to change the value or color of the text   
    text(c, posX, posY - 15);
  }
  
  void updatePB (float p) {
    if (p > max) {
      return;
    }
    current = p;
    draw();
  }
  
  void drawOuter () {
    fill(outerColor);
    rect(posX, posY, width, height);
  }
  
  void drawInner () {
    float completedPct = (current / max);
    float completed = completedPct * increment * 1000;
    fill(lerpColor(innerStartColor, innerEndColor, completedPct));
    while (posX2 + blockWidth <= posX + padding + completed) {
      rect(posX2, posY2, blockWidth, innerHeight);
      posX2 = posX2 + blockWidth + innerPadding;
    } 
  }

  void draw () {
    drawInner();
  }

}

