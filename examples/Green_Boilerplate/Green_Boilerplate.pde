import Green.*;

Green green;
MainWorld world;

//Run at the start of the program, used to set everything up for the session
void setup()
{
  size(800, 600);
  green = new Green(this);
  world = new MainWorld(width, height);
  green.loadWorld(world);
}
//Run every frame
void draw()
{
  green.handleAct();
  green.handleDraw();
  green.handleMousePosition(pmouseX, pmouseY, mouseX, mouseY);
  green.handleInput();
}
//Allows for easy input-checking
void mousePressed()
{
  green.handleMouseDown(mouseButton);
}
void mouseReleased()
{
  green.handleMouseUp(mouseButton);
}
void mouseWheel(MouseEvent event)
{
  green.handleMouseWheel(event.getCount());
}
void keyPressed()
{
  green.handleKeyDown(key, keyCode);
}
void keyReleased()
{
  green.handleKeyUp(key, keyCode);
}
