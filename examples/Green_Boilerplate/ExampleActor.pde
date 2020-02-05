// An Actor is a solitary item, object, or character acting in your game.
// Anything that needs to do something on it's own should have it's own Actor class.
class ExampleActor extends Actor
{
  // Construct the actor at the start of the program (run once)
  public ExampleActor(float x, float y, int w, int h)
  {
    super(x, y, loadImage("Sprites/CoolBall.png"), w, h);
  }
  // Run every frame - this is where most of the actor code might go
  // deltaTime is the number of seconds since the last frame (decimal) and it's very useful for smooth movement
  //   For instance: move(movementSpeed * deltaTime) will appear smooth no matter how jittery the framerate is 
  public void act(float deltaTime)
  {
    
  }
}
