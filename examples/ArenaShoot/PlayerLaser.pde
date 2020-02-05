class PlayerLaser extends Actor
{
  //Configuration variables
  float moveSpeed = 3f;
  //Runtime variables
  Green green;
  Arena world;
  TankBase player;
  boolean poweredUp = false;
  
  //Construct the actor at the start of the program
  public PlayerLaser(float x, float y, boolean powered)
  {
    super(x, y, 37, 14); //1.85f scale
    poweredUp = powered;
    if(!powered)
      setImage(loadImage("Sprites/laser1Rf.png"));
    else
      setImage(loadImage("Sprites/lPowerUppedLaser1Rf.png"));
    green = Green.getInstance();
  }
  //Called when this is added to a world
  public void addedToWorld(World w)
  {
    world = (Arena) w; //This is not a safe cast, but since Arena is the only World, it should be okay
    player = world.getObjects(TankBase.class).get(0); //This isn't really a safe way of doing this
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
    //Check for hit enemies and lasers
    
    //Enemy1Laser
    Enemy1Laser hitEnemy1Laser = getOneIntersectingObject(Enemy1Laser.class);
    if(hitEnemy1Laser != null)
    {
      world.removeObject(hitEnemy1Laser);
      if(!poweredUp)
      {
        world.removeObject(this);
        return;
      }
    }
    //Enemy3Laser
    Enemy3Laser hitEnemy3Laser = getOneIntersectingObject(Enemy3Laser.class);
    if(hitEnemy3Laser != null)
    {
      world.removeObject(hitEnemy3Laser);
      if(!poweredUp)
      {
        world.removeObject(this);
        return;
      }
    }
    //Enemy1
    Enemy1 hitEnemy1 = getOneIntersectingObject(Enemy1.class);
    if(hitEnemy1 != null)
    {
      player.currentScore += hitEnemy1.scoreValue;
      world.removeEnemy1(hitEnemy1);
      if(!poweredUp)
      {
        world.removeObject(this);
        return;
      }
    }
    //Enemy2
    Enemy2 hitEnemy2 = getOneIntersectingObject(Enemy2.class);
    if(hitEnemy2 != null)
    {
      player.currentScore += hitEnemy2.scoreValue;
      world.removeEnemy2(hitEnemy2);
      if(!poweredUp)
      {
        world.removeObject(this);
        return;
      }
    }
    //Enemy3
    Enemy3 hitEnemy3 = getOneIntersectingObject(Enemy3.class);
    if(hitEnemy3 != null)
    {
      player.currentScore += hitEnemy3.scoreValue;
      world.removeEnemy3(hitEnemy3);
      if(!poweredUp)
      {
        world.removeObject(this);
        return;
      }
    }
  }
}
