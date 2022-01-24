import java.util.*;

class Arena extends World
{
  //Configuration variables
  int topOffset = 40; //TopBar height
  int textColour = color(255, 255, 255);
  int gameOverColour = color(50, 50, 50);
  //Enemy1
  int enemy1SpawnTimeBase = 1000;
  int enemy1SpawnTimeRange = 1000;
  int enemy1GlobalMoveTime = 500;
  int enemy1MoveDurationBase = 1000;
  int enemy1MoveDurationRange = 4000;
  int enemy1ShootTimeBase = 1500;
  int enemy1ShootTimeRange = 250;
  //Enemy2
  int enemy2SpawnTimeBase = 7000;
  int enemy2SpawnTimeRange = 8000;
  int enemy2GlobalMoveTime = 500;
  //Enemy3
  int enemy3SpawnTimeBase = 1000;
  int enemy3SpawnTimeRange = 1000;
  int enemy3GlobalMoveTime = 500;
  int enemy3ShootTimeBase = 1500;
  int enemy3ShootTimeRange = 250;
  //Spawn caps
  int enemy1Cap = 6;
  int enemy2Cap = 1;
  int enemy3Cap = 2;
  //Powerups
  //Laser powerup
  int lPowerSpawnTimeBase = 10000;
  int lPowerSpawnTimeRange = 20000;
  //Shield powerup
  int sPowerSpawnTimeBase = 10000;
  int sPowerSpawnTimeRange = 20000;
  //Health powerup
  int hPowerSpawnTimeBase = 10000;
  int hPowerSpawnTimeRange = 20000;
  
  //Runtime variables
  long enemy1NextSpawnTime = enemy1SpawnTimeBase + round(enemy1SpawnTimeRange * random(-1f, 1f));
  long enemy2NextSpawnTime = enemy2SpawnTimeBase + round(enemy2SpawnTimeRange * random(-1f, 1f));
  long enemy3NextSpawnTime = enemy3SpawnTimeBase + round(enemy3SpawnTimeRange * random(-1f, 1f));
  long enemy1NextShootTime = enemy1ShootTimeBase + round(enemy1ShootTimeRange * random(-1f, 1f));
  long enemy3NextShootTime = enemy3ShootTimeBase + round(enemy3ShootTimeRange * random(-1f, 1f));
  int enemy1Count = 0;
  int enemy2Count = 0;
  int enemy3Count = 0;
  long lastEnemy1Move = 0;
  long lastEnemy2Move = 0;
  long lastEnemy3Move = 0;
  boolean lPowerExists = false;
  boolean sPowerExists = false;
  boolean hPowerExists = false;
  long lPowerNextSpawnTime = lPowerSpawnTimeBase + round(lPowerSpawnTimeRange * random(0f, 1f));
  long sPowerNextSpawnTime = sPowerSpawnTimeBase + round(sPowerSpawnTimeRange * random(0f, 1f));
  long hPowerNextSpawnTime = hPowerSpawnTimeBase + round(hPowerSpawnTimeRange * random(0f, 1f));
  
  //Construct the world at the start of the program
  public Arena(int w, int h)
  {
    super(w, h, loadImage("Sprites/ground.png"));
  }
  //Run once when the world is loaded
  public void prepare()
  {
    TankBase player = new TankBase(getWidth() / 2f, getHeight() / 2f);
    addObject(player);
    TankTurret playerTurret = new TankTurret();
    addObject(playerTurret);
    TopBar topBar = new TopBar();
    addObject(topBar);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    handleEnemy1s();
    handleEnemy2s();
    handleEnemy3s();
    handlePowerups();
  }
  //Final 'Game Over' screen
  public void gameOver(int finalScore)
  {
    println("Game over! Final score: " + finalScore);
    exit();
  }
  private void handleEnemy1s()
  {
    //Spawn
    if(enemy1Count < enemy1Cap && millis() >= enemy1NextSpawnTime)
    {
      EnemySide side = EnemySide.Top;
      switch(floor(random(0, 4f)))
      {
        case 0:
          side = EnemySide.Top;
          break;
        case 1:
          side = EnemySide.Bottom;
          break;
        case 2:
          side = EnemySide.Left;
          break;
        case 3:
          side = EnemySide.Right;
          break;
      }
      Enemy1 newEnemy = new Enemy1(side, this);
      addObject(newEnemy);
      enemy1Count++;
      enemy1NextSpawnTime = millis() + enemy1SpawnTimeBase + round(enemy1SpawnTimeRange * random(-1f, 1f));
    }
    //Shoot
    List<Enemy1> enemy1s = getObjects(Enemy1.class); //Note that this file imports 'java.util.*';
    if(millis() >= enemy1NextShootTime && enemy1s.size() > 0)
    {
      enemy1NextShootTime = millis() + enemy1ShootTimeBase + round(enemy1ShootTimeRange * random(-1f, 1f)); 
      enemy1s.get(floor(random(0f, enemy1s.size()))).shoot();
    }
    //Move
    if(millis() >= lastEnemy1Move + enemy1GlobalMoveTime)
    {
      lastEnemy1Move = millis();
      for(Enemy1 enemy : enemy1s)
      {
        if(random(0f, 1f) < 0.25f)
        {
          enemy.moveUntilTime = millis() + enemy1MoveDurationBase + round(enemy1MoveDurationRange * random(0f, 1f));
          if(random(0f, 1f) < 0.5f)
            enemy.moveSpeed = -enemy.moveSpeed;
        }
      }
    }
  }
  private void handleEnemy2s()
  {
    //Spawn
    if(enemy2Count < enemy2Cap && millis() >= enemy2NextSpawnTime)
    {
      Enemy2 newEnemy = new Enemy2(getWidth() * random(0f, 1f), topOffset + ((getHeight() - topOffset) * random(0f, 1f)));
      addObject(newEnemy);
      enemy2Count++;
      enemy2NextSpawnTime = millis() + enemy2SpawnTimeBase + round(enemy2SpawnTimeRange * random(-1f, 1f));
    }
    //Move
    if(millis() >= lastEnemy2Move + enemy2GlobalMoveTime)
    {
      lastEnemy2Move = millis();
      List<Enemy2> enemy2s = getObjects(Enemy2.class);
      for(Enemy2 enemy : enemy2s)
        enemy.updateMovement();
    }
  }
  private void handleEnemy3s()
  {
    //Spawn
    if(enemy3Count < enemy3Cap && millis() >= enemy3NextSpawnTime)
    {
      Enemy3 newEnemy = new Enemy3(getWidth() * random(0f, 1f), topOffset + ((getHeight() - topOffset) * random(0f, 1f)));
      addObject(newEnemy);
      enemy3Count++;
      enemy3NextSpawnTime = millis() + enemy3SpawnTimeBase + round(enemy3SpawnTimeRange * random(-1f, 1f));
    }
    //Shoot
    List<Enemy3> enemy3s = getObjects(Enemy3.class);
    if(millis() >= enemy3NextShootTime && enemy3s.size() > 0)
    {
      enemy3NextShootTime = millis() + enemy3ShootTimeBase + round(enemy3ShootTimeRange * random(-1f, 1f)); 
      enemy3s.get(floor(random(0f, enemy3s.size()))).shoot();
    }
    //Move
    if(millis() >= lastEnemy3Move + enemy3GlobalMoveTime)
    {
      lastEnemy3Move = millis();
      for(Enemy3 enemy : enemy3s)
        if(random(0f, 1f) < 0.25f)
          enemy.moveDirection = random(0f, 360f);
    }
  }
  private void handlePowerups()
  {
    if(!lPowerExists && millis() >= lPowerNextSpawnTime)
    {
      LaserPowerup powerup = new LaserPowerup(getWidth() * random(0f, 1f), topOffset + ((getHeight() - topOffset) * random(0f, 1f)));
      addObject(powerup);
      lPowerExists = true;
    }
    if(!sPowerExists && millis() >= sPowerNextSpawnTime)
    {
      ShieldPowerup powerup = new ShieldPowerup(getWidth() * random(0f, 1f), topOffset + ((getHeight() - topOffset) * random(0f, 1f)));
      addObject(powerup);
      sPowerExists = true;
    }
    if(!hPowerExists && millis() >= hPowerNextSpawnTime)
    {
      HealthPowerup powerup = new HealthPowerup(getWidth() * random(0f, 1f), topOffset + ((getHeight() - topOffset) * random(0f, 1f)));
      addObject(powerup);
      hPowerExists = true;
    }
  }
  //Remove an enemy from the world - this is a separate method so we can keep track of enemy counts
  public void removeEnemy1(Enemy1 enemy)
  {
    enemy1Count--;
    removeObject(enemy);
  }
  public void removeEnemy2(Enemy2 enemy)
  {
    enemy2Count--;
    removeObject(enemy);
  }
  public void removeEnemy3(Enemy3 enemy)
  {
    enemy3Count--;
    removeObject(enemy);
  }
  //Removes a powerup and prepares the next one to spawn
  public void removeLaserPowerup(LaserPowerup powerup)
  {
    lPowerNextSpawnTime = millis() + lPowerSpawnTimeBase + round(lPowerSpawnTimeRange * random(0f, 1f));
    lPowerExists = false;
    removeObject(powerup);
  }
  public void removeShieldPowerup(ShieldPowerup powerup)
  {
    sPowerNextSpawnTime = millis() + sPowerSpawnTimeBase + round(sPowerSpawnTimeRange * random(0f, 1f));
    sPowerExists = false;
    removeObject(powerup);
  }
  public void removeHealthPowerup(HealthPowerup powerup)
  {
    hPowerNextSpawnTime = millis() + hPowerSpawnTimeBase + round(hPowerSpawnTimeRange * random(0f, 1f));
    hPowerExists = false;
    removeObject(powerup);
  }
}
