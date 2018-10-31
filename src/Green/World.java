package Green;

import java.util.*;
import processing.core.*;

public abstract class World
{
	private Green green;
	private PApplet app;
	
	private int _width;
	private int _height;
	
	private int _backgroundColor;
	
	private List<Actor> actors = new ArrayList<Actor>();
	
	//Constructors
	public World(int w, int h)
	{
		_width = w;
		_height = h;
		init();
		_backgroundColor = app.color(255, 255, 255);
	}
	public World(int w, int h, int bgColor)
	{
		_width = w;
		_height = h;
		_backgroundColor = bgColor;
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
		return _backgroundColor;
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
		_backgroundColor = newColor;
	}
	public final void setBackgroundColor(int newColorR, int newColorG, int newColorB)
	{
		_backgroundColor = app.color(newColorR, newColorG, newColorB);
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
	
	//Base Methods
	public final void handleDraw()
	{
		app.background(_backgroundColor);
		for(int i = 0; i < actors.size(); i++)
		{
			app.pushMatrix();
			app.translate(actors.get(i).getX(), actors.get(i).getY());
			actors.get(i).draw();
			app.popMatrix();
		}
	}
	public final void handleAct()
	{
		act();
		for(int i = 0; i < actors.size(); i++)
			actors.get(i).act();
	}
	
	public abstract void prepare();
	public abstract void act();
}