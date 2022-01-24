enum EnemySide
{
  Top,
  Bottom,
  Left,
  Right
}
class Enemy1 extends Actor
{
  //Configuration variables
  int topOffset = 40; //TopBar height
  int scoreValue = 1;
  float edgeOffset = 10f; //The distance from the edge to place it
  float moveSpeed = 3f;
  //Runtime variables
  Green green;
  EnemySide anchorSide;
  long moveUntilTime = 0;
  
  //Construct the actor at the start of the program
  public Enemy1(EnemySide side, World world)
  {
    super(0, 0, loadImage("Sprites/enemy1L.png"), 2f);
    green = Green.getInstance();
    anchorSide = side;
    float randomMultiplier = random(0f, 1f);
    switch(side)
    {
      case Top:
        setLocation(world.getWidth() * randomMultiplier, edgeOffset + topOffset);
        turn(90f);
        break;
      case Bottom:
        setLocation(world.getWidth() * randomMultiplier, world.getHeight() - edgeOffset);
        turn(-90f);
        break;
      case Left:
        setLocation(edgeOffset, world.getHeight() * randomMultiplier);
        //turn(0f); //Since it wouldn't do anything anyways
        break;
      case Right:
        setLocation(world.getWidth() - edgeOffset, world.getHeight() * randomMultiplier);
        turn(180f);
        break;
    }
  }
  //Run every frame
  public void act(float deltaTime)
  {
    if(millis() <= moveUntilTime)
    {
      if(anchorSide == EnemySide.Top || anchorSide == EnemySide.Bottom)
        moveGlobal(moveSpeed, 0f);
      else //if(anchorSide == EnemySide.Left || anchorSide == EnemySide.Right)
        moveGlobal(0f, moveSpeed);
      if(isAtEdge())
        moveSpeed = -moveSpeed;
    }
  }
  public void shoot()
  {
    Enemy1Laser laser = new Enemy1Laser(getX(), getY());
    world.addObject(laser);
    laser.setRotation(this.getRotation());
    laser.move(25f);
  }
}
