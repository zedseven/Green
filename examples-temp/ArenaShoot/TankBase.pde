import java.util.*;

class TankBase extends Actor
{
  //Configuration variables
  int maxHealth = 100;
  float moveSpeed = 2f;
  float turnSpeed = 1f;
  PImage baseImage = null;
  PImage shieldedImage = loadImage("Sprites/shieldedPlayerBase.png");
  //Runtime variables
  Green green;
  TankTurret turret = null;
  List<long[]> damageQueue = new ArrayList<long[]>();
  int currentHealth = maxHealth;
  int currentScore = 0;
  //Powerup effects
  boolean shielded = false;
  long shieldedUntil = 0;
  
  //Construct the actor at the start of the program
  public TankBase(float x, float y)
  {
    super(x, y, loadImage("Sprites/playerBase.png"), 2.5f);
    baseImage = getSourceImage();
    green = Green.getInstance();
  }
  //Run every frame
  public void act(float deltaTime)
  {
    if(green.isKeyDown('a'))
      turn(-turnSpeed);
    if(green.isKeyDown('d'))
      turn(turnSpeed);
    if(green.isKeyDown('w'))
      move(moveSpeed);
    if(green.isKeyDown('s'))
      move(-moveSpeed);
    LaserPowerup lPower = getOneIntersectingObject(LaserPowerup.class);
    if(lPower != null)
      applyLaser(lPower);
    ShieldPowerup sPower = getOneIntersectingObject(ShieldPowerup.class);
    if(sPower != null)
      applyShield(sPower);
    HealthPowerup hPower = getOneIntersectingObject(HealthPowerup.class);
    if(hPower != null)
      applyHealth(hPower);
    //Handle powerups wearing off
    if(shielded && millis() >= shieldedUntil)
    {
      shielded = false;
      updateImage();
      turret.updateImage();
    }
  }
  public void applyDamage(int damage)
  {
    int appliedDamage = (shielded ? (int) (damage / 2f) : damage);
    damageQueue.add(new long[] { appliedDamage, millis() });
    currentHealth = max(0, currentHealth - appliedDamage); //Apply the damage, taking shield into account
    if(currentHealth <= 0)
      ((Arena) getWorld()).gameOver(currentScore);
  }
  public void updateImage()
  {
    if(shielded)
      setImage(shieldedImage);
    else
      setImage(baseImage);
  }
  //Powerups
  public void applyLaser(LaserPowerup laser)
  {
    turret.powered = true;
    turret.poweredUntil = millis() + laser.duration;
    turret.updateImage();
    ((Arena) getWorld()).removeLaserPowerup(laser);
  }
  public void applyShield(ShieldPowerup shield)
  {
    shielded = true;
    shieldedUntil = millis() + shield.duration;
    updateImage();
    turret.updateImage();
    ((Arena) getWorld()).removeShieldPowerup(shield);
  }
  public void applyHealth(HealthPowerup health)
  {
    currentHealth = min(maxHealth, currentHealth + health.health);
    ((Arena) getWorld()).removeHealthPowerup(health);
  }
}
