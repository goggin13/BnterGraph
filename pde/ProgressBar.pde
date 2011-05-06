int SCREEN_WIDTH = 1000;
int SCREEN_HEIGHT = 1000;
color backgroundColor = color(0, 0, 0);
MyProgressBar progressBar;
int p = 0;

void setup() {
  size(SCREEN_WIDTH, SCREEN_HEIGHT);
  background(backgroundColor);
  progressBar = new MyProgressBar(1000);
}
 
void draw() {
  progressBar.updatePB(p);
  p = p + 10;
}





    

