class MainWorld extends World
{
  //Construct the world at the start of the program
  public MainWorld(int w, int h)
  {
    super(w, h, loadImage("<pathtobgimage>"));
  }
  //Run once when the world is loaded
  public void prepare()
  {
    ExampleActor actor1 = new ExampleActor(50, 50, 100, 100);
    addObject(actor1);
  }
  //Run every frame
  public void act(float deltaTime)
  {
    
  }
}
