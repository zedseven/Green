import Green.*;

Green green;
// This is where each world should be defined
MainWorld world;

// Run at the start of the program, used to set everything up for the game
void setup()
{
  size(800, 600); // The size of the viewport used for the game - same as base Processing
  green = new Green(this);
  world = new MainWorld(width, height); // By default, the world boundaries will be set to the viewport dimensions
  green.loadWorld(world);
}
// Run every frame - don't touch the first four lines
void draw()
{
  green.handleAct();
  green.handleDraw();
  green.handleMousePosition(pmouseX, pmouseY, mouseX, mouseY);
  green.handleInput();
  // Add draw() code below here!
}
// Allows for easy input-checking - ignore everything below!
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
