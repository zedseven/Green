class Enemy3Laser extends Actor
{
  //Configuration variables
  int damage = 5;
  float moveSpeed = 3f;
  //Runtime variables
  Green green;
  Arena world;
  
  //Construct the actor at the start of the program
  public Enemy3Laser(float x, float y)
  {
    super(x, y, loadImage("Sprites/laser3Rf.png"), 1.85f);
    green = Green.getInstance();
  }
  //Called when this is added to a world
  public void addedToWorld(World w)
  {
    world = (Arena) w; //This is not a safe cast, but since Arena is the only World, it should be okay
  }
  //Run every frame
  public void act(float deltaTime)
  {
    move(moveSpeed);
    if(isAtEdge())
    {
      world.removeObject(this);
      return; //Since this no longer 'exists'
    }
    //Check for whether the player has been hit
    TankBase player = getOneIntersectingObject(TankBase.class);
    if(player != null) //Hit the player!
    {
      player.applyDamage(damage);
      world.removeObject(this);
      return;
    }
  }
}
