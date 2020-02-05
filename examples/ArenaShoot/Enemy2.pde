import java.util.*;

class Enemy2 extends Actor
{
  //Configuration variables
  int scoreValue = 1;
  int damage = 2;
  int damageIncrement = 100;
  int trailColour = color(98, 255, 194);
  int trailThickness = 8;
  int trailPiecesToDraw = 3;
  float moveSpeed = 5f;
  //Runtime variables
  Green green;
  long moveUntilTime = 0;
  List<float[]> trailCoords = new ArrayList<float[]>();
  long lastDamageTime = 0;
  
  //Construct the actor at the start of the program
  public Enemy2(float x, float y)
  {
    super(x, y, loadImage("Sprites/enemy2.png"), 2.5f);
    green = Green.getInstance();
    setZ(3); //Draw over everything else
  }
  //Run every frame
  public void act(float deltaTime)
  {
    move(moveSpeed);
    //Apply damage to the player
    if(millis() >= lastDamageTime + damageIncrement)
    {
      lastDamageTime = millis();
      TankBase player = getOneIntersectingObject(TankBase.class);
      if(player != null)
      {
        player.applyDamage(damage);
      }
    }
  }
  //Use a custom draw method - in this case, to draw the trail
  public void draw()
  {
    //Draw the enemy
    rotate(radians(getRotation()));
    image(getImage(), -getImage().width / 2f, -getImage().height / 2f);
    rotate(-radians(getRotation()));
    //Draw the trail
    fill(trailColour);
    noStroke();
    float pX = getX(), pY = getY();
    for(int i = trailCoords.size() - 1; i >= max(0, trailCoords.size() - trailPiecesToDraw); i--)
    {
      translate(trailCoords.get(i)[0] - pX, trailCoords.get(i)[1] - pY);
      float pointsAngle = Green.getPointsAngle(trailCoords.get(i)[0], trailCoords.get(i)[1], pX, pY);
      rotate(pointsAngle);
      rect(0f, -trailThickness / 2, Green.getPointsDist(trailCoords.get(i)[0], trailCoords.get(i)[1], pX, pY), trailThickness / 2);
      rotate(-pointsAngle);
      pX = trailCoords.get(i)[0];
      pY = trailCoords.get(i)[1];
    }
  }
  //Update the movement direction
  public void updateMovement()
  {
    trailCoords.add(new float[] { getX(), getY() }); //Store the new pivot point in the trail list
    TankBase player = getWorld().getObjects(TankBase.class).get(0); //Not safe, but there should only be one player
    turnTowards(player); //Change direction to face the player again
  }
}
