class Enemy3 extends Actor
{
  //Configuration variables
  int scoreValue = 2;
  float moveSpeed = 3f;
  float turnSpeed = 2f;
  //Runtime variables
  Green green;
  float moveDirection = 0f;
  
  //Construct the actor at the start of the program
  public Enemy3(float x, float y)
  {
    super(x, y, loadImage("Sprites/enemy3.png"), 2.5f);
    green = Green.getInstance();
  }
  //Run every frame
  public void act(float deltaTime)
  {
    turn(turnSpeed);
    float currentRotation = getRotation();
    setRotation(moveDirection);
    move(moveSpeed);
    setRotation(currentRotation);
  }
  public void shoot()
  {
    for(int i = 0; i < 4; i++)
    {
      Enemy3Laser laser = new Enemy3Laser(getX(), getY());
      getWorld().addObject(laser);
      laser.setRotation(getRotation() + i * 90f);
      laser.move(30f);
    }
  }
}
