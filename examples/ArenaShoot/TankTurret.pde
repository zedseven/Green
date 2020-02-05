class TankTurret extends Actor
{
  //Configuration variables
  int shootSpeed = 500; //Every 500 milliseconds
  PImage baseImage = null;
  PImage poweredImage = loadImage("Sprites/playerBlasterLPowerUppedL.png");
  PImage shieldedImage = loadImage("Sprites/shieldedPlayerBlasterL.png");
  PImage poweredShieldedImage = loadImage("Sprites/shieldedPlayerBlasterLPowerUppedL.png");
  //Runtime variables
  Green green;
  TankBase player;
  boolean powered = false;
  long poweredUntil = 0;
  long lastShotTime = 0;
  
  //Construct the actor at the start of the program
  public TankTurret()
  {
    super(0, 0, loadImage("Sprites/playerBlasterL.png"), 2.5f);
    baseImage = getSourceImage();
    green = Green.getInstance();
    setZ(2); //Draw over the tank base
  }
  //Run when the actor is added to a world
  public void addedToWorld(World world)
  {
    player = world.getObjects(TankBase.class).get(0); //This isn't really a safe way of doing this
    player.turret = this;
  }
  //Run every frame
  public void act(float deltaTime)
  {
    setLocation(player.getX(), player.getY());
    turnTowards(mouseX, mouseY);
    if(green.isMouseButtonDown(LEFT) && millis() - lastShotTime >= shootSpeed)
      shoot();
    //Handle powerups wearing off
    if(powered && millis() >= poweredUntil)
    {
      powered = false;
      updateImage();
    }
  }
  //Update the sprite with the correct image
  public void updateImage()
  {
    if(powered && player.shielded)
      setImage(poweredShieldedImage);
    else if(player.shielded)
      setImage(shieldedImage);
    else if(powered)
      setImage(poweredImage);
    else
      setImage(baseImage);
  }
  //Shoots from the barrel
  private void shoot()
  {
    lastShotTime = millis();
    PlayerLaser laser = new PlayerLaser(getX(), getY(), powered);
    getWorld().addObject(laser);
    laser.setRotation(getRotation());
    laser.move(40);
  }
}
