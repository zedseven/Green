package Green;

import java.util.*;
import processing.core.*;
import static processing.core.PApplet.floor;
import static processing.core.PApplet.ceil;

public abstract class World
{
	private Green green;
	private PApplet app;
	private UUID uuid = UUID.randomUUID();
	
	private int _width;
	private int _height;
	
	private int _backgroundColour = -1;//app.color(255, 255, 255);
	private int _outOfBoundsColour = -16777216;
	private PImage _backgroundImage = null;
	private boolean _unbounded = false;
	private boolean _onScreenDraw = true;
	
	private List<Actor> _actors = new ArrayList<Actor>();
	private Actor _camFollowActor = null;
	
	private long _lastMillis = 0;
	
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
	public World(int w, int h, int bgColor, PImage bgImage)
	{
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_backgroundImage = bgImage;
		init();
	}
	public World(int w, int h, boolean unbounded)
	{
		_width = w;
		_height = h;
		_unbounded = unbounded;
		init();
	}
	public World(int w, int h, int bgColor, boolean unbounded)
	{
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_unbounded = unbounded;
		init();
	}
	public World(int w, int h, PImage bgImage, boolean unbounded)
	{
		_width = w;
		_height = h;
		_backgroundImage = bgImage;
		_unbounded = unbounded;
		init();
	}
	public World(int w, int h, int bgColor, PImage bgImage, boolean unbounded)
	{
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_backgroundImage = bgImage;
		_unbounded = unbounded;
		init();
	}
	private void init()
	{
		green = Green.getInstance();
		app = green.getParent();
	}
	public String toString()
	{
		return "World \"" + this.getClass().getSimpleName() + "\" #" + uuid + " (" + _width + ", " + _height + ")";
	}
	
	//Getters
	public final UUID getUuid()
	{
		return uuid;
	}
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
	public final int getOutOfBoundsColor()
	{
		return _outOfBoundsColour;
	}
	public final boolean getUnbounded()
	{
		return _unbounded;
	}
	public final boolean getOnScreenDraw()
	{
		return _onScreenDraw;
	}
	public final Actor getCamFollowActor()
	{
		return _camFollowActor;
	}
	public final List<Actor> getObjects()
	{
		return _actors;
	}
	public final <A extends Actor> List<A> getObjects(Class<A> type)
	{
		List<A> retList = new ArrayList<A>();
		for(int i = 0; i < _actors.size(); i++)
			if(type.isInstance(_actors.get(i)))
				retList.add((A) _actors.get(i));
		return retList;
	}
	public final <A extends Actor> A getRandomObject(Class<A> type)
	{
		List<A> actorList = getObjects(type);
		if(actorList.size() <= 0)
			return null;
		return actorList.get((int) Math.floor(Math.random() * actorList.size()));
	}
	
	//Setters
	public final void setBackgroundColor(int newColour)
	{
		_backgroundColour = newColour;
	}
	public final void setBackgroundColor(int newColourR, int newColourG, int newColourB)
	{
		_backgroundColour = app.color(newColourR, newColourG, newColourB);
	}
	public final void setOutOfBoundsColor(int newColourR, int newColourG, int newColourB)
	{
		_outOfBoundsColour = app.color(newColourR, newColourG, newColourB);
	}
	public final void setUnbounded(boolean unbounded)
	{
		_unbounded = unbounded;
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
	public final void setOnScreenDraw(boolean onScreenDraw)
	{
		_onScreenDraw = onScreenDraw;
	}
	public final void setCamFollowActor(Actor newActor)
	{
		_camFollowActor = newActor;
	}
	public final void addObject(Actor obj)
	{
		_actors.add(obj);
		obj.addedToWorld(this);
	}
	public final void removeObject(Actor obj)
	{
		try
		{
			_actors.remove(obj);
			obj.removedFromWorld(this);
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
	{//check if should draw 
		if(_width < app.width || _height < app.height)
		{
			app.background(_outOfBoundsColour);
			app.translate(floor(app.width / 2f - _width / 2f) - 1, floor(app.height / 2f - _height / 2f) - 1);
			app.fill(_backgroundColour);
			app.rect(0, 0, _width + 1, _height + 1);
		}
		else if((_width > app.width || _height > app.height) && _camFollowActor != null)
		{
			app.background(_backgroundColour);
			app.translate(app.width / 2f - _camFollowActor.getX(), app.height / 2f - _camFollowActor.getY());
		}
		else
		{
			app.background(_backgroundColour);
		}
		app.fill(-16777216); //app.color(0, 0, 0)
		if(_backgroundImage != null)
			app.image(_backgroundImage, 0, 0, _width, _height);
		float screenMinX = 0f;
		float screenMinY = 0f;
		float screenMaxX = 0f;
		float screenMaxY = 0f;
		if(_onScreenDraw)
		{
			screenMinX = _camFollowActor.getX() - app.width / 2f;
			screenMinY = _camFollowActor.getY() - app.height / 2f;
			screenMaxX = _camFollowActor.getX() + app.width / 2f;
			screenMaxY = _camFollowActor.getY() + app.height / 2f;
		}
		Collections.sort(_actors, (a1, a2) -> { return Math.round(a1.getZ() - a2.getZ()); });
		for(Actor actor : _actors)
		{
			if(_onScreenDraw)
			{
				float maxDim = Math.max(actor.getWidth(), actor.getHeight());
				if(actor.getX() + maxDim < screenMinX || actor.getX() - maxDim > screenMaxX || actor.getY() + maxDim < screenMinY || actor.getY() - maxDim > screenMaxY)
					continue;
			}
			app.pushMatrix();
			app.translate(actor.getX(), actor.getY());
			actor.draw();
			app.popMatrix();
		}
	}
	public final void handleAct()
	{
		long currentMillis = app.millis();
		float deltaTime = (currentMillis - _lastMillis) / 1000f;
		_lastMillis = currentMillis;
		act(deltaTime);
		for(Actor actor : _actors)
			actor.act(deltaTime);
	}
	
	public abstract void prepare();
	public abstract void act(float deltaTime);
}