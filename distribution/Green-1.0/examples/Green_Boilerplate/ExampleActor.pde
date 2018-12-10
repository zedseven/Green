class ExampleActor extends Actor
{
  //Construct the actor at the start of the program
  public ExampleActor(float x, float y, int w, int h)
  {
    super(x, y, loadImage("<pathtosprite>"), w, h);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
}
