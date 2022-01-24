class TopBar extends Actor
{
  //Configuration variables
  int healthBarDamageTime = 1000;
  int backgroundColour = color(112, 112, 112);
  int scoreColour = color(255, 255, 255);
  //HealthBar stuff
  float healthBarX = 600f;
  float healthBarY = 7f;
  int healthBarW = 125;
  int healthBarH = 25;
  int healthBarGray = color(74, 74, 74);
  int healthBarRed = color(207, 13, 42);
  int healthBarGreen = color(50, 181, 55);
  float scoreX = 10;
  float scoreY = 4;
  //Runtime variables
  TankBase player;
  
  //Construct the actor at the start of the program
  public TopBar()
  {
    super(0f, 0f, width, 40);
    setZ(5);
  }
  //Run when the actor is added to a world
  public void addedToWorld(World world)
  {
    player = world.getObjects(TankBase.class).get(0); //This isn't really a safe way of doing this
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
  //Override the draw method
  public void draw()
  {
    noStroke();
    //Background of the whole bar
    fill(backgroundColour);
    rect(0f, 0f, getWidth(), getHeight());
    //Score
    fill(scoreColour);
    textSize(28);
    textAlign(LEFT, TOP);
    text("Score: " + player.currentScore, scoreX, scoreY);
    //Health bar
    //Gray background
    fill(healthBarGray);
    rect(healthBarX, healthBarY, healthBarW, healthBarH);
    //Red damage indicator
    fill(healthBarRed);
    int redHealthBoost = 0;
    long currentTime = millis();
    for(long[] damageEvent : player.damageQueue)
      if(currentTime - damageEvent[1] <= healthBarDamageTime)
        redHealthBoost += damageEvent[0];
    rect(healthBarX, healthBarY, healthBarW * (((float) min(player.maxHealth, player.currentHealth + redHealthBoost)) / player.maxHealth), healthBarH);
    //Green health indicator
    fill(healthBarGreen);
    rect(healthBarX, healthBarY, healthBarW * (((float) player.currentHealth) / player.maxHealth), healthBarH);
  }
}
