class ShieldPowerup extends Actor
{
  //Configuration variables
  int duration = 15000;
  
  //Construct the actor at the start of the program
  public ShieldPowerup(float x, float y)
  {
    super(x, y, loadImage("Sprites/powerUpShieldRf.png"), 1.875f);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
}
