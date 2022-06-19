package Green;

import java.util.*;
import processing.core.*;
import static processing.core.PApplet.floor;

/**
 * The base class for all worlds in the library. A world is its own area for a collection of {@link Actor} instances to interact in, though any given {@link Actor} can be within multiple worlds at a time.
 * Only one world may be active at a time however, which can be changed by calling {@link Green#loadWorld(World)}.
 * @author Zacchary Dempsey-Plante
 */
public abstract class World
{
	protected Green green;
	protected PApplet app;
	private UUID uuid;
	
	private int _width;
	private int _height;
	
	private int _backgroundColour = 0xFFFFFFFF;
	private int _outOfBoundsColour = 0xFF000000;
	private PImage _sourceBackgroundImage = null;
	private PImage _backgroundImage = null;
	private boolean _unbounded = false;
	private boolean _onlyDrawOnScreen = true;
	private int _resizeFormat = Green.TILE;
	private boolean _actorUnsafeRemove = false; //Mode for removing actors from the world safely or otherwise. In some small cases this may be necessary.
	
	private final List<Actor> _actors = new ArrayList<>();
	private Actor _camFollowActor = null;
	private final List<Actor> _actorRemoveQueue = new ArrayList<>();
	
	//Constructors
	/**
	 * Creates a new world using the screen dimensions.
	 */
	public World()
	{
		init();
		_width = app.width;
		_height = app.height;
	}
	/**
	 * Creates a new world using the screen dimensions with a background colour.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 */
	public World(int bgColor)
	{
		_backgroundColour = bgColor;
		init();
		_width = app.width;
		_height = app.height;
	}
	/**
	 * Creates a new world using the screen dimensions with a background image.
	 * @param bgImage The background image.
	 */
	public World(PImage bgImage)
	{
		init();
		_sourceBackgroundImage = bgImage;
		_width = bgImage.width;
		_height = bgImage.height;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world using the screen dimensions with a background image and a background colour to draw underneath.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 */
	public World(int bgColor, PImage bgImage)
	{
		init();
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		_width = bgImage.width;
		_height = bgImage.height;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world using the screen dimensions, set to be bounded or otherwise.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(boolean unbounded)
	{
		init();
		_unbounded = unbounded;
		_width = app.width;
		_height = app.height;
	}
	/**
	 * Creates a new world using the screen dimensions with a background colour, set to be bounded or otherwise.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(int bgColor, boolean unbounded)
	{
		init();
		_backgroundColour = bgColor;
		_unbounded = unbounded;
		_width = app.width;
		_height = app.height;
	}
	/**
	 * Creates a new world using the screen dimensions with a background image, set to be bounded or otherwise.
	 * @param bgImage The background image.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(PImage bgImage, boolean unbounded)
	{
		init();
		_sourceBackgroundImage = bgImage;
		_width = bgImage.width;
		_height = bgImage.height;
		scaleBackgroundImage(_width, _height);
		_unbounded = unbounded;
	}
	/**
	 * Creates a new world using the screen dimensions with a background image and a background colour to draw underneath, set to be bounded or otherwise.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(int bgColor, PImage bgImage, boolean unbounded)
	{
		init();
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		_width = bgImage.width;
		_height = bgImage.height;
		scaleBackgroundImage(_width, _height);
		_unbounded = unbounded;
	}
	/**
	 * Creates a new world with defined dimensions.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 */
	public World(int w, int h)
	{
		init();
		_width = w;
		_height = h;
	}
	/**
	 * Creates a new world with defined dimensions and a background colour.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 */
	public World(int w, int h, int bgColor)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
	}
	/**
	 * Creates a new world with defined dimensions and a background image.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgImage The background image.
	 */
	public World(int w, int h, PImage bgImage)
	{
		init();
		_width = w;
		_height = h;
		_sourceBackgroundImage = bgImage;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image with a background colour to draw underneath.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 */
	public World(int w, int h, int bgColor, PImage bgImage)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image. Also supplies a resizing format.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgImage The background image.
	 * @param resizeFormat The background image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public World(int w, int h, PImage bgImage, int resizeFormat)
	{
		init();
		_width = w;
		_height = h;
		_sourceBackgroundImage = bgImage;
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image with a background colour to draw underneath. Also supplies a resizing format.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 * @param resizeFormat The background image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public World(int w, int h, int bgColor, PImage bgImage, int resizeFormat)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions, set to be bounded or otherwise.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(int w, int h, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_unbounded = unbounded;
	}
	/**
	 * Creates a new world with defined dimensions and a background colour, set to be bounded or otherwise.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 */
	public World(int w, int h, int bgColor, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_unbounded = unbounded;
	}
	/**
	 * Creates a new world with defined dimensions and a background image, set to be bounded or otherwise.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgImage The background image.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public World(int w, int h, PImage bgImage, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_sourceBackgroundImage = bgImage;
		_unbounded = unbounded;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image, set to be bounded or otherwise. Also supplies a resizing format.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgImage The background image.
	 * @param resizeFormat The background image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public World(int w, int h, PImage bgImage, int resizeFormat, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_sourceBackgroundImage = bgImage;
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		_unbounded = unbounded;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image with a background colour to draw underneath, set to be bounded or otherwise.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied. 
	 */
	public World(int w, int h, int bgColor, PImage bgImage, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		_unbounded = unbounded;
		scaleBackgroundImage(_width, _height);
	}
	/**
	 * Creates a new world with defined dimensions and a background image with a background colour to draw underneath, set to be bounded or otherwise. Also supplies a resizing format.
	 * @param w The width to create the world with.
	 * @param h The height to create the world with.
	 * @param bgColor The background colour. Use the {@link processing.core.PApplet#color(int, int, int)} method to define it.
	 * @param bgImage The background image.
	 * @param resizeFormat The background image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @param unbounded Whether {@link Actor} instances should be able to go out of bounds.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied. 
	 */
	public World(int w, int h, int bgColor, PImage bgImage, int resizeFormat, boolean unbounded)
	{
		init();
		_width = w;
		_height = h;
		_backgroundColour = bgColor;
		_sourceBackgroundImage = bgImage;
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		_unbounded = unbounded;
		scaleBackgroundImage(_width, _height);
	}
	private void init()
	{
		uuid = UUID.randomUUID();
		green = Green.getInstance();
		app = green.getParent();
	}
	private void scaleBackgroundImage(int w, int h)
	{
		if (_resizeFormat == Green.BILINEAR)
		{
			_backgroundImage = _sourceBackgroundImage.get();
			if(_sourceBackgroundImage.width == w && _sourceBackgroundImage.height == h)
				return;
			_backgroundImage.resize(w, h);
		}
		else if (_resizeFormat == Green.NEAREST_NEIGHBOR)
		{
			_backgroundImage = green.resizeNN(_sourceBackgroundImage, w, h);
		}
		else if (_resizeFormat == Green.TILE)
		{
			_backgroundImage = green.tileImage(_sourceBackgroundImage, w, h);
		}
	}
	
	//Object class overrides
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		World other = (World) obj;
		if(uuid == null)
			return other.uuid == null;
		else return uuid.equals(other.uuid);
	}
	@Override
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
	 * Retrieves the original image supplied as the background image, before any scaling has been applied.
	 * @return The original image supplied as the background image, before any scaling has been applied.
	 */
	public final PImage getSourceBackgroundImage()
	{
		return _sourceBackgroundImage;
	}
	/**
	 * Retrieves the background image currently in use, with scaling applied.
	 * @return The background image currently in use, with scaling applied.
	 */
	public final PImage getBackgroundImage()
	{
		return _backgroundImage;
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
	 * Retrieves whether the {@link World} has 'collidable' boundaries that prevent {@link Actor} instances from going out-of-bounds.
	 * @return Whether the {@link World} has 'collidable' boundaries.
	 */
	public final boolean getUnbounded()
	{
		return _unbounded;
	}
	/**
	 * Retrieves whether the {@link World} should attempt to render {@link Actor} instances that are not on-screen. In normal use, there shouldn't be a reason for this to be modified.
	 * By default, the {@link World} does not render things that aren't on-screen.
	 * @return Whether the {@link World} attempts to render {@link Actor} instances that are not on-screen.
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
	 * Retrieves the current setting for whether the {@link World} should remove {@link Actor} instances safely.
	 * @return The current setting for whether the {@link World} should remove {@link Actor} instances safely.
	 */
	public final boolean getActorUnsafeRemove()
	{
		return _actorUnsafeRemove;
	}
	/**
	 * Retrieves the background image resize format of the {@link World}.
	 * @return The background image resize format - either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 */
	public final int getResizeFormat()
	{
		return _resizeFormat;
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
		List<A> retList = new ArrayList<>();
		for (Actor actor : _actors)
			if (type.isInstance(actor))
				retList.add((A) actor);
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
	 * Sets the width of the {@link World}. This operation may be expensive, depending on the number of {@link Actor} instances, whether {@link #setUnbounded(boolean)} has been used, etc.
	 * @param width The width to set.
	 */
	public final void setWidth(int width)
	{
		_width = width;
		scaleBackgroundImage(_width, _height);
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
	/**
	 * Sets the height of the {@link World}. This operation may be expensive, depending on the number of {@link Actor} instances, whether {@link #setUnbounded(boolean)} has been used, etc.
	 * @param height The height to set.
	 */
	public final void setHeight(int height)
	{
		_height = height;
		scaleBackgroundImage(_width, _height);
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
	/**
	 * Sets the dimensions of the {@link World}. This operation may be expensive, depending on the number of {@link Actor} instances, whether {@link #setUnbounded(boolean)} has been used, etc.
	 * @param width The width to set.
	 * @param height The height to set.
	 */
	public final void setDimensions(int width, int height)
	{
		_width = width;
		_height = height;
		scaleBackgroundImage(_width, _height);
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
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
	 * Sets the background image of the {@link World}.
	 * @param image The image to set as the background.
	 */
	public final void setBackgroundImage(PImage image)
	{
		_sourceBackgroundImage = image;
		scaleBackgroundImage(_width, _height);
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
	 * Sets whether the {@link World} has 'collidable' boundaries that prevent {@link Actor} instances from going out-of-bounds.
	 * @param unbounded Whether the {@link World} has 'collidable' boundaries.
	 */
	public final void setUnbounded(boolean unbounded)
	{
		_unbounded = unbounded;
		if(!_unbounded)
			for(Actor actor : _actors)
				actor.setLocation(actor.getX(), actor.getY());
	}
	/**
	 * Sets whether the {@link World} should attempt to render {@link Actor} instances that are not on-screen. In normal use, there shouldn't be a reason for this to be modified.
	 * By default, the {@link World} does not render things that aren't on-screen.
	 * @param onlyDrawOnScreen Whether the {@link World} should attempt to render {@link Actor} instances that are not on-screen.
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
	 * Sets whether the {@link World} should remove {@link Actor} instances safely. In some niche cases it may be handy to set this to {@code true}, but only if you know what you are doing.
	 * @param unsafeRemove Whether the {@link World} should remove {@link Actor} instances safely.
	 */
	public final void setActorUnsafeRemove(boolean unsafeRemove)
	{
		_actorUnsafeRemove = unsafeRemove;
		FlushActorRemoveQueue();
	}
	/**
	 * Sets the background image resize format of the {@link World}.
	 * @param format The background image resize format to set. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public final void setResizeFormat(int format)
	{
		if(format != Green.BILINEAR && format != Green.NEAREST_NEIGHBOR && format != Green.TILE)
			throw new UnknownResizeFormatException();
		_resizeFormat = format;
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
	 * Removes an {@link Actor} from the {@link World}. It either removes it right away (unsafe) or waits to remove it at the end of the frame (safe) based on the value set by {@link World#setActorUnsafeRemove(boolean)}. The default value is safe.
	 * @param obj The {@link Actor} to remove from the {@link World}.
	 * @throws IllegalArgumentException Thrown when {@code obj} is not in the {@link World}.
	 */
	public final void removeObject(Actor obj)
	{
		try
		{
			if(_actorUnsafeRemove)
			{
				_actors.remove(obj);
				obj.removedFromWorld(this);
			}
			else
			{
				_actorRemoveQueue.add(obj);
			}
		}
		catch(Exception e)
		{
			throw new IllegalArgumentException();
		}
	}
	
	//Utility Methods
	/**
	 * Determines whether the {@link World} contains {@code findActor}.
	 * @param findActor The {@link Actor} to check for the existence of.
	 * @return Whether the {@link World} contains {@code findActor}.
	 */
	public final boolean hasObject(Actor findActor)
	{
		List<Actor> actors = getObjects();
		for(Actor actor : actors)
			if(actor.equals(findActor))
				return true;
		return false;
	}
	
	//Functional Methods
	private void FlushActorRemoveQueue()
	{
		for(Actor actor : _actorRemoveQueue)
		{
			_actors.remove(actor);
			actor.removedFromWorld(this);
		}
		_actorRemoveQueue.clear();
	}
	
	//Base Methods
	/**
	 * Calls the {@link Actor#draw()} method on every {@link Actor} in the {@link World}, drawing all objects in the {@link World} to screen.
	 * @deprecated Please call {@link Green#handleDraw()} instead - this will still be called, but the parent method does additional important things.
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
			float camFollowX = _camFollowActor.getX();
			float camFollowY = _camFollowActor.getY();
			
			app.background(_outOfBoundsColour);
			app.translate(app.width / 2f - camFollowX, app.height / 2f - camFollowY);
			if(_backgroundImage == null)
			{
				app.fill(_backgroundColour);
				app.rect(0, 0, _width, _height);
			}
			if(_onlyDrawOnScreen)
			{
				float screenHalfWidth = app.width / 2f;
				float screenHalfHeight = app.height / 2f;
				screenMinX += camFollowX - screenHalfWidth;
				screenMinY += camFollowY - screenHalfHeight;
				screenMaxX += camFollowX - screenHalfWidth;
				screenMaxY += camFollowY - screenHalfHeight;
			}
		}
		else
		{
			app.background(_backgroundColour);
		}
		app.fill(0xFF000000);
		app.tint(255, 255);
		if(_backgroundImage != null)
			app.image(_backgroundImage, 0, 0, _width, _height);
		_actors.sort((a1, a2) -> Math.round(a1.getZ() - a2.getZ()));
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
	 * @deprecated Please call {@link Green#handleAct()} instead - this will still be called, but the parent method does additional important things.
	 */
	public final void handleAct()
	{
		float deltaTime = green.getDeltaTime();
		act(deltaTime);
		for(int i = _actors.size() - 1; i >= 0; i--) //for(Actor actor : _actors) <- This cannot be used here because if removeObject() is used within an act(), a ConcurrentModificationException is thrown
			_actors.get(i).act(deltaTime);
		FlushActorRemoveQueue();
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
