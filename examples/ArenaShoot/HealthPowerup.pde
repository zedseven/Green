class HealthPowerup extends Actor
{
  //Configuration variables
  int health = 60;
  
  //Construct the actor at the start of the program
  public HealthPowerup(float x, float y)
  {
    super(x, y, loadImage("Sprites/powerUpHealth.png"), 1.875f);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
}
