package Green;

import java.util.*;
import processing.core.*;
import static processing.core.PApplet.floor;
import static processing.core.PApplet.ceil;

public abstract class World
{
	private Green green;
	private PApplet app;
	
	private int _width;
	private int _height;
	
	private int _backgroundColour = -1;//app.color(255, 255, 255);
	private PImage _backgroundImage = null;
	
	private List<Actor> actors = new ArrayList<Actor>();
	
	//Constructors
	public World(int w, int h)
	{
		_width = w;
		_height = h;
		init();
	}
	public World(int w, int h, int bgColor)
	{
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		init();
	}
	public World(int w, int h, PImage bgImage)
	{
		_width = w;
		_height = h;
		_backgroundImage = bgImage;
		init();
	}
	private void init()
	{
		green = Green.getInstance();
		app = green.getParent();
	}
	
	//Getters
	public final int getWidth()
	{
		return _width;
	}
	public final int getHeight()
	{
		return _height;
	}
	public final int getBackgroundColor()
	{
		return _backgroundColour;
	}
	public final List<Actor> getObjects()
	{
		return actors;
	}
	public final List<Actor> getObjects(Class<?> type)
	{
		List<Actor> retList = new ArrayList<Actor>();
		for(int i = 0; i < actors.size(); i++)
			if(type.isInstance(actors.get(i)))
				retList.add(actors.get(i));
		return retList;
	}
	public final Actor getRandomObject(Class<?> type)
	{
		List<Actor> actorList = getObjects(type);
		if(actorList.size() <= 0)
			return null;
		return actorList.get((int) Math.floor(Math.random() * actorList.size()));
	}
	
	//Setters
	public final void setBackgroundColor(int newColor)
	{
		_backgroundColour = newColor;
	}
	public final void setBackgroundColor(int newColorR, int newColorG, int newColorB)
	{
		_backgroundColour = app.color(newColorR, newColorG, newColorB);
	}
	public final void addObject(Actor obj)
	{
		actors.add(obj);
		obj.addedToWorld(this);
	}
	public final void removeObject(Actor obj)
	{
		try
		{
			actors.remove(obj);
		}
		catch(Exception e)
		{
			System.out.println("The object to remove is not in the world actor list.");
		}
	}
	
	//Utility Methods
	public final boolean hasObject(Actor findActor)
	{
		List<Actor> actors = getObjects();
		for(Actor actor : actors)
			if(actor.equals(findActor))
				return true;
		return false;
	}
	
	//Base Methods
	public final void handleDraw()
	{
		app.background(-16777216);
		app.fill(_backgroundColour);
		app.translate(floor(app.width / 2f - _width / 2f) - 1, floor(app.height / 2f - _height / 2f) - 1);
		app.rect(0, 0, _width + 1, _height + 1);
		app.fill(-16777216); //app.color(0, 0, 0)
		if(_backgroundImage != null)
			app.image(_backgroundImage, 0, 0, _width, _height);
		Collections.sort(actors, (a1, a2) -> { return Math.round(a1.getZ() - a2.getZ()); });
		for(Actor actor : actors)
		{
			app.pushMatrix();
			app.translate(actor.getX(), actor.getY());
			actor.draw();
			app.popMatrix();
		}
	}
	public final void handleAct()
	{
		act();
		for(Actor actor : actors)
			actor.act();
	}
	
	public abstract void prepare();
	public abstract void act();
}