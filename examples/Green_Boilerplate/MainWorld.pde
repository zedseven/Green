// A World is where the Actors are able to interact with one-another.
// For most games, you will only need one World.
class MainWorld extends World
{
  // Construct the world at the start of the program (run once)
  public MainWorld(int w, int h)
  {
    super(w, h, loadImage("Sprites/MainWorld.png"));
  }
  // Run once when the world is loaded
  // World positions are measured in pixels from the top left to the bottom right
  public void prepare()
  {
    // Note that every actor needs these two lines!
    ExampleActor actor1 = new ExampleActor(50, 50, 100, 100);
    addObject(actor1);
  }
  // Run every frame - this is where you might do things that aren't tied to a specific Actor, like handling scores
  public void act(float deltaTime)
  {
    
  }
}
