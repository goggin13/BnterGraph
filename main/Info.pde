/*
 * An instance of class Info is a graphical object that
 * displays its information in a styled box when info.draw()
 * is called */
class Info {
 PImage infoImage;                        // image associated with this info display
 String username;                         // user name to display in this info
 int conversation_count;                  // number of conversations they've had
 float pointerWidth = 10;                 // width of the triangle pointer at the caller
 float infoWidth = 100;                    
 float infoHeight = 120;
 color infoFill = color(255, 255, 255);
 color fontColor = color(200, 0, 0);
 float imgSize = 50;
 float fontSize = 12;

 // (the username, the url of the image to display, the number of conversations they've had)
 Info (String u, String img_url, int c) {
   infoImage = loadImage(img_url);
   username = u;
   conversation_count = c;
 }
 
 // Primary method for Info; draws the display box
 void draw (float posX, float posY) {
   float infoX = posX + pointerWidth;
   float infoY = posY - pointerWidth - 5;
   float imgX = infoX + 2;
   float imgY = infoY + 2;
   float textX = imgX;
   float textY = imgY + imgSize + 5 + fontSize / 2;
   
   setStrokeFill();
   drawPointer(posX, posY);
   drawOutline(infoX, infoY);
   image(infoImage, imgX, imgY);
   //fill(color(180, 45, 190));
   //rect(imgX, imgY, imgSize, imgSize);
   drawInfo(textX, textY);
 }
 
 // Draws the pointer, a triangle which points from
 // the display box towards the relevant object
 void drawPointer (float posX, float posY) {
   float X2 = posX + pointerWidth;
   float X3 = X2;
   float Y2 = posY + pointerWidth;
   float Y3 = posY - pointerWidth;
   
   triangle(posX, posY, X2, Y2, X3, Y3);
 }
 
 // Draws the lines of text following the user image
 void drawInfo (float posX, float posY) {
  fill(fontColor);
  text(username, posX, posY);
  posY = posY + fontSize + 2;
  text(conversation_count + " conversations", posX, posY); 
  posY = posY + fontSize + 2;
  text("See my Bnter Profile", posX, posY);
  posY = posY + fontSize + 2;
  text("See my Bnter System", posX, posY);
 }
 
 // Set the stroke and fill for the shapes used
 void setStrokeFill () {
   noStroke();
   fill(infoFill);
 }
 
 // Draw the outline of the display box
 void drawOutline (float posX, float posY) {
   rect(posX, posY, infoWidth, infoHeight);
 }
 
}
