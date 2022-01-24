class LaserPowerup extends Actor
{
  //Configuration variables
  int duration = 15000;
  
  //Construct the actor at the start of the program
  public LaserPowerup(float x, float y)
  {
    super(x, y, loadImage("Sprites/powerUpLaserRf.png"), 1.875f);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
}
