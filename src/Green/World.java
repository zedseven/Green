package Green;

import java.util.*;
import processing.core.*;
import static processing.core.PApplet.floor;
import static processing.core.PApplet.ceil;

public abstract class World
{
	private Green green;
	private PApplet app;
	private UUID uuid;
	
	private int _width;
	private int _height;
	
	private int _backgroundColour = -1;//app.color(255, 255, 255);
	private int _outOfBoundsColour = -16777216;
	private PImage _backgroundImage = null;
	private boolean _unbounded = false;
	private boolean _onlyDrawOnScreen = true;
	
	private List<Actor> _actors = new ArrayList<Actor>();
	private Actor _camFollowActor = null;
	
	private long _lastMillis = 0;
	
	//Constructors
	public World()
	{
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(int bgColor)
	{
		_backgroundColour = bgColor;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(PImage bgImage)
	{
		_backgroundImage = bgImage;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(int bgColor, PImage bgImage)
	{
		_backgroundColour = bgColor;
		_backgroundImage = bgImage;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(boolean unbounded)
	{
		_unbounded = unbounded;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(int bgColor, boolean unbounded)
	{
		_backgroundColour = bgColor;
		_unbounded = unbounded;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(PImage bgImage, boolean unbounded)
	{
		_backgroundImage = bgImage;
		_unbounded = unbounded;
		init();
		_width = app.width;
		_height = app.height;
	}
	public World(int bgColor, PImage bgImage, boolean unbounded)
	{
		_backgroundColour = bgColor;
		_backgroundImage = bgImage;
		_unbounded = unbounded;
		init();
		_width = app.width;
		_height = app.height;
	}
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
		uuid = UUID.randomUUID();
		green = Green.getInstance();
		app = green.getParent();
	}
	public String toString()
	{
		return "World \"" + this.getClass().getSimpleName() + "\" #" + uuid + " (" + _width + ", " + _height + ")";
	}
	
	//Getters
	/**
	 * Retrieves the {@link java.util.UUID} of the {@link World}. This value is unique to each {@link World} instance, and cannot be modified.
	 * @return The unique instance ID.
	 */
	public final UUID getUuid()
	{
		return uuid;
	}
	/**
	 * Retrieves the width of the {@link World}.
	 * @return The width.
	 */
	public final int getWidth()
	{
		return _width;
	}
	/**
	 * Retrieves the height of the {@link World}.
	 * @return The height.
	 */
	public final int getHeight()
	{
		return _height;
	}
	/**
	 * Retrieves the background colour of the {@link World}. This is drawn-over if a background image is set.
	 * @return The background colour of the {@link World}.
	 */
	public final int getBackgroundColor()
	{
		return _backgroundColour;
	}
	/**
	 * Retrieves the out-of-bounds colour of the {@link World}. This is only applicable if {@link #setCamFollowActor(Actor)} is in use, or the screen size is larger than the size of the {@link World}.
	 * @return The out-of-bounds colour of the {@link World}.
	 */
	public final int getOutOfBoundsColor()
	{
		return _outOfBoundsColour;
	}
	/**
	 * Retrieves whether or not the {@link World} has 'collideable' boundaries that prevent {@link Actor} instances from going out-of-bounds.
	 * @return Whether or not the {@link World} has 'collideable' boundaries.
	 */
	public final boolean getUnbounded()
	{
		return _unbounded;
	}
	/**
	 * Retrieves whether or not the {@link World} should attempt to render {@link Actor} instances that are not on-screen. In normal use, there shouldn't be a reason for this to be modified.
	 * By default, the {@link World} does not render things that aren't on-screen.
	 * @return Whether or not the {@link World} attempts to render {@link Actor} instances that are not on-screen.
	 */
	public final boolean getOnlyDrawOnScreen()
	{
		return _onlyDrawOnScreen;
	}
	/**
	 * Retrieves the {@link Actor} the camera should follow, if it is set.
	 * @return The {@link Actor} the camera should follow, if it is set.
	 */
	public final Actor getCamFollowActor()
	{
		return _camFollowActor;
	}
	/**
	 * Retrieves a list of all {@link Actor} instances in the {@link World}.
	 * @return A list of all {@link Actor} instances in the {@link World}.
	 */
	public final List<Actor> getObjects()
	{
		return _actors;
	}
	/**
	 * Retrieves a list of all {@link Actor} instances matching {@code type} in the {@link World}.
	 * @param <A> The type of {@link Actor} to filter by, as defined by {@code type}.
	 * @param type The type of {@link Actor} to filter by.
	 * @return A list of all {@link Actor} instances matching {@code type}.
	 */
	public final <A extends Actor> List<A> getObjects(Class<A> type)
	{
		List<A> retList = new ArrayList<A>();
		for(int i = 0; i < _actors.size(); i++)
			if(type.isInstance(_actors.get(i)))
				retList.add((A) _actors.get(i));
		return retList;
	}
	/**
	 * Retrieves a single random {@link Actor} matching {@code type} in the {@link World}, or null if the {@link World} has no objects.
	 * @param <A> The type of {@link Actor} to filter by, as defined by {@code type}.
	 * @param type The type of {@link Actor} to filter by.
	 * @return A single random {@link Actor} matching {@code type}, or null if the {@link World} has no objects.
	 */
	public final <A extends Actor> A getRandomObject(Class<A> type)
	{
		List<A> actorList = getObjects(type);
		if(actorList.size() <= 0)
			return null;
		return actorList.get((int) Math.floor(Math.random() * actorList.size()));
	}
	
	//Setters
	/**
	 * Sets the background colour of the {@link World}. This is drawn-over if a background image is set.
	 * @param newColour The colour to set as the background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 */
	public final void setBackgroundColor(int newColour)
	{
		_backgroundColour = newColour;
	}
	/**
	 * Sets the background colour of the {@link World}. This is drawn-over if a background image is set.
	 * @param newColourR The value in range 0 - 255 to use as the red channel in the background colour.
	 * @param newColourG The value in range 0 - 255 to use as the green channel in the background colour.
	 * @param newColourB The value in range 0 - 255 to use as the blue channel in the background colour.
	 */
	public final void setBackgroundColor(int newColourR, int newColourG, int newColourB)
	{
		_backgroundColour = app.color(newColourR, newColourG, newColourB);
	}
	/**
	 * Sets the out-of-bounds colour of the {@link World}. This is only applicable if {@link #setCamFollowActor(Actor)} is in use, or the screen size is larger than the size of the {@link World}.
	 * @param newColour The colour to set as the out-of-bounds colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 */
	public final void setOutOfBoundsColor(int newColour)
	{
		_outOfBoundsColour = newColour;
	}
	/**
	 * Sets the out-of-bounds colour of the {@link World}. This is only applicable if {@link #setCamFollowActor(Actor)} is in use, or the screen size is larger than the size of the {@link World}.
	 * @param newColourR The value in range 0 - 255 to use as the red channel in the out-of-bounds colour.
	 * @param newColourG The value in range 0 - 255 to use as the green channel in the out-of-bounds colour.
	 * @param newColourB The value in range 0 - 255 to use as the blue channel in the out-of-bounds colour.
	 */
	public final void setOutOfBoundsColor(int newColourR, int newColourG, int newColourB)
	{
		_outOfBoundsColour = app.color(newColourR, newColourG, newColourB);
	}
	/**
	 * Sets whether or not the {@link World} has 'collideable' boundaries that prevent {@link Actor} instances from going out-of-bounds.
	 * @param unbounded Whether or not the {@link World} has 'collideable' boundaries.
	 */
	public final void setUnbounded(boolean unbounded)
	{
		_unbounded = unbounded;
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
	/**
	 * Sets whether or not the {@link World} should attempt to render {@link Actor} instances that are not on-screen. In normal use, there shouldn't be a reason for this to be modified.
	 * By default, the {@link World} does not render things that aren't on-screen.
	 * @param onlyDrawOnScreen Whether or not the {@link World} should attempt to render {@link Actor} instances that are not on-screen.
	 */
	public final void setOnlyDrawOnScreen(boolean onlyDrawOnScreen)
	{
		_onlyDrawOnScreen = onlyDrawOnScreen;
	}
	/**
	 * Sets the {@link Actor} the camera should follow.
	 * @param newActor The {@link Actor} the camera should follow.
	 */
	public final void setCamFollowActor(Actor newActor)
	{
		_camFollowActor = newActor;
	}
	
	/**
	 * Adds an {@link Actor} to the {@link World}.
	 * @param obj The {@link Actor} to add to the {@link World}.
	 */
	public final void addObject(Actor obj)
	{
		_actors.add(obj);
		obj.addedToWorld(this);
	}
	/**
	 * Removes an {@link Actor} from the {@link World}.
	 * @param obj The {@link Actor} to remove from the {@link World}.
	 * @throws IllegalArgumentException Thrown when {@code obj} is not in the {@link World}.
	 */
	public final void removeObject(Actor obj)
	{
		try
		{
			_actors.remove(obj);
			obj.removedFromWorld(this);
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException();
		}
	}
	
	//Utility Methods
	/**
	 * Determines whether or not the {@link World} contains {@code findActor}.
	 * @param findActor The {@link Actor} to check for the existence of.
	 * @return Whether or not the {@link World} contains {@code findActor}.
	 */
	public final boolean hasObject(Actor findActor)
	{
		List<Actor> actors = getObjects();
		for(Actor actor : actors)
			if(actor.equals(findActor))
				return true;
		return false;
	}
	
	//Base Methods
	/**
	 * Calls the {@link Actor#draw()} method on every {@link Actor} in the {@link World}, drawing all objects in the {@link World} to screen.
	 */
	public final void handleDraw()
	{
		float screenMinX = 0f;
		float screenMinY = 0f;
		float screenMaxX = app.width;
		float screenMaxY = app.height;
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
			if(_onlyDrawOnScreen)
			{
				float screenHalfWidth = app.width / 2f;
				float screenHalfHeight = app.height / 2f;
				screenMinX += _camFollowActor.getX() - screenHalfWidth;
				screenMinY += _camFollowActor.getY() - screenHalfHeight;
				screenMaxX += _camFollowActor.getX() - screenHalfWidth;
				screenMaxY += _camFollowActor.getY() - screenHalfHeight;
			}
		}
		else
		{
			app.background(_backgroundColour);
		}
		app.fill(-16777216); //app.color(0, 0, 0)
		if(_backgroundImage != null)
			app.image(_backgroundImage, 0, 0, _width, _height);
		Collections.sort(_actors, (a1, a2) -> { return Math.round(a1.getZ() - a2.getZ()); });
		for(Actor actor : _actors)
		{
			if(_onlyDrawOnScreen)
			{
				float maxDim = Math.max(actor.getWidth(), actor.getHeight()) / 2f;
				if(actor.getX() + maxDim < screenMinX || actor.getX() - maxDim > screenMaxX || actor.getY() + maxDim < screenMinY || actor.getY() - maxDim > screenMaxY)
					continue;
			}
			app.pushMatrix();
			app.translate(actor.getX(), actor.getY());
			actor.draw();
			app.popMatrix();
		}
	}
	/**
	 * Calls the {@link Actor#act(float)} method on every {@link Actor} in the {@link World}.
	 */
	public final void handleAct()
	{
		long currentMillis = app.millis();
		float deltaTime = (currentMillis - _lastMillis) / 1000f;
		_lastMillis = currentMillis;
		act(deltaTime);
		for(int i = _actors.size() - 1; i >= 0; i--) //for(Actor actor : _actors) <- This cannot be used here because if removeObject() is used within an act(), a ConcurrentModificationException is thrown
			_actors.get(i).act(deltaTime);
	}
	
	/**
	 * Called when the {@link World} is loaded via the {@link Green#loadWorld(World)} method.
	 */
	public abstract void prepare();
	/**
	 * Called once per frame.
	 * @param deltaTime The time, in seconds, since the last time this method was called.
	 */
	public abstract void act(float deltaTime);
}