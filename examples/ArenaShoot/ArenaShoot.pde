import Green.*;

/*
 Name: Zacchary Dempsey-Plante
 Date: 2018-12-09
 Description: A driving arena shooter. This is a port of a game I originally made for school that was finished 2016-01-28. This is definitely not my cleanest code.
   The original game can be found at https://github.com/zedseven/CS-2015-Final-Project.
 */

Green green;
Arena world;

//Run at the start of the program, used to set everything up for the session
void setup()
{
  size(800, 640);
  green = new Green(this);
  world = new Arena(width, height);
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
