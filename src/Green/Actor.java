package Green;

import java.util.*;
import processing.core.*;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.atan2;
import static processing.core.PApplet.degrees;
import static processing.core.PApplet.radians;

/**
 * The base class for all objects in the library. It supports movement, rotation, opacity, custom sprites, custom frame-by-frame drawing, and render order.
 * It's position system is from top-left to bottom-right.
 * @author Zacchary Dempsey-Plante
 */
public abstract class Actor
{
	private Green green;
	private PApplet app;
	private UUID uuid;
	
	private float _x = 0;
	private float _y = 0;
	private float _z = 0;
	private float _rotation = 0;
	private PImage _sourceImage = null;
	private PImage _image = null;
	private int _width;
	private int _height;
	private float _opacity = 255;
	private int _resizeFormat = Green.NEAREST_NEIGHBOR;

	private static final int GREEN_COLOUR = 0xFF4D975B;
	
	//Constructors
	/**
	 * Creates the new actor at a position with a sprite.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param image The image to use with the actor.
	 */
	public Actor(float x, float y, PImage image) //TODO: Add GIF support
	{
		init();
		_x = x;
		_y = y;
		_sourceImage = image;
		_width = _sourceImage.width;
		_height = _sourceImage.height;
		scaleImage(_width, _height); //Just to update the _image variable
	}
	/**
	 * Creates the new actor at a position with a defined width and height.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param w The width to create the actor with.
	 * @param h The height to create the actor with.
	 */
	public Actor(float x, float y, int w, int h)
	{
		init();
		_x = x;
		_y = y;
		_width = w;
		_height = h;
	}
	/**
	 * Creates the new actor at a position with a sprite stretched to fit the defined width and height.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param image The image to use with the actor.
	 * @param w The width to create the actor with.
	 * @param h The height to create the actor with.
	 */
	public Actor(float x, float y, PImage image, int w, int h)
	{
		init();
		_x = x;
		_y = y;
		_sourceImage = image;
		_width = w;
		_height = h;
		scaleImage(_width, _height);
	}
	/**
	 * Creates the new actor at a position with a sprite scaled by {@code scaleMultiplier}.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param image The image to use with the actor.
	 * @param scaleMultiplier The multiplier to apply to {@code image}'s dimensions.
	 */
	public Actor(float x, float y, PImage image, float scaleMultiplier)
	{
		init();
		_x = x;
		_y = y;
		_sourceImage = image;
		_width = Math.round(_sourceImage.width * scaleMultiplier);
		_height = Math.round(_sourceImage.height * scaleMultiplier);
		scaleImage(_width, _height);
	}
	/**
	 * Creates the new actor at a position with a sprite, resized according to {@code resizeFormat} to fit the defined width and height.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param image The image to use with the actor.
	 * @param w The width to create the actor with.
	 * @param h The height to create the actor with.
	 * @param resizeFormat The image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public Actor(float x, float y, PImage image, int w, int h, int resizeFormat)
	{
		init();
		_x = x;
		_y = y;
		_sourceImage = image;
		_width = w;
		_height = h;
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		scaleImage(_width, _height);
	}
	/**
	 * Creates the new actor at a position with a sprite scaled by {@code scaleMultiplier}, resized according to {@code resizeFormat}.
	 * @param x The X-axis position to start the actor at.
	 * @param y The Y-axis position to start the actor at.
	 * @param image The image to use with the actor.
	 * @param scaleMultiplier The multiplier to apply to {@code image}'s dimensions.
	 * @param resizeFormat The image resize format to use. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public Actor(float x, float y, PImage image, float scaleMultiplier, int resizeFormat)
	{
		init();
		_x = x;
		_y = y;
		_sourceImage = image;
		_width = Math.round(_sourceImage.width * scaleMultiplier);
		_height = Math.round(_sourceImage.height * scaleMultiplier);
		setResizeFormat(resizeFormat); //To handle unknown resize formats
		scaleImage(_width, _height);
	}
	private void init()
	{
		uuid = UUID.randomUUID();
		green = Green.getInstance();
		app = green.getParent();
	}
	private void scaleImage(int w, int h)
	{
		if (_resizeFormat == Green.BILINEAR)
		{
			_image = _sourceImage.get();
			if(_sourceImage.width == w && _sourceImage.height == h)
				return;
			_image.resize(w, h);
		}
		else if (_resizeFormat == Green.NEAREST_NEIGHBOR)
		{
			_image = green.resizeNN(_sourceImage, w, h);
		}
		else if (_resizeFormat == Green.TILE)
		{
			_image = green.tileImage(_sourceImage, w, h);
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
		Actor other = (Actor) obj;
		if(uuid == null)
			return other.uuid == null;
		else return uuid.equals(other.uuid);
	}
	@Override
	public String toString()
	{
		return "Actor \"" + this.getClass().getSimpleName() + "\" #" + uuid;
	}
	
	//Getters
	/**
	 * Retrieves the {@link java.util.UUID} of the {@link Actor}. This value is unique to each {@link Actor} instance, and cannot be modified.
	 * @return The unique instance ID.
	 */
	public final UUID getUuid()
	{
		return uuid;
	}
	/**
	 * Retrieves the X-axis position of the {@link Actor}.
	 * @return The X-axis position.
	 */
	public final float getX()
	{
		return _x;
	}
	/**
	 * Retrieves the X-axis position of the {@link Actor}.
	 * @return The Y-axis position.
	 */
	public final float getY()
	{
		return _y;
	}
	/**
	 * Retrieves the Z-axis position of the {@link Actor}. This is only used to determine render order.
	 * @return The Z-axis position.
	 */
	public final float getZ()
	{
		return _z;
	}
	/**
	 * Retrieves the rotation value (in degrees) of the {@link Actor}.
	 * @return The rotation in degrees.
	 */
	public final float getRotation()
	{
		return degrees(_rotation);
	}
	/**
	 * Retrieves the width of the {@link Actor}.
	 * @return The width.
	 */
	public final int getWidth()
	{
		return _width;
	}
	/**
	 * Retrieves the height of the {@link Actor}.
	 * @return The height.
	 */
	public final int getHeight()
	{
		return _height;
	}
	/**
	 * Retrieves the original {@link processing.core.PImage} (before any scaling has been applied) associated with the {@link Actor}.
	 * @return The original associated image.
	 */
	public final PImage getSourceImage()
	{
		return _sourceImage;
	}
	/**
	 * Retrieves the {@link processing.core.PImage} associated with the {@link Actor}.
	 * @return The associated image.
	 */
	public final PImage getImage()
	{
		return _image;
	}
	/**
	 * Retrieves the opacity value (in the range 0 - 255) of the {@link Actor}.
	 * @return The opacity value in range 0 - 255.
	 */
	public final float getOpacity()
	{
		return _opacity;
	}
	/**
	 * Retrieves the image resize format of the {@link Actor}.
	 * @return The image resize format - either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 */
	public final int getResizeFormat()
	{
		return _resizeFormat;
	}
	/**
	 * Retrieves the current {@link World} this {@link Actor} is in. This is unsafe and only used internally.
	 * @return The world this {@link Actor} is a part of, that is currently loaded.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	private World getWorldUnsafe() throws NoWorldException
	{
		World currentWorld = Green.getWorld();
		if(currentWorld == null)
			throw new NoWorldException();
		if(currentWorld.hasObject(this))
			return currentWorld;
		throw new NoWorldException();
	}
	/**
	 * Retrieves the current {@link World} this {@link Actor} is in, or null otherwise.
	 * @return The world this {@link Actor} is a part of, that is currently loaded.
	 */
	public final World getWorld()
	{
		World currentWorld = Green.getWorld();
		if(currentWorld.hasObject(this))
			return currentWorld;
		return null;
	}
	/**
	 * Retrieves the current {@link World} this {@link Actor} is in as {@code type} if possible, or null otherwise.
	 * @param <W> The type of {@link World} to return if possible, as defined by {@code type}.
	 * @param type The type of {@link World} to return if possible.
	 * @return The world this {@link Actor} is a part of that is currently loaded.
	 */
	public static <W extends World> W getWorldOfType(Class<W> type)
	{
		//ClassCastException
		if(type.isInstance(Green.getWorld()))
			return (W) Green.getWorld();
		else
			return null; //throw new ClassCastException("The current world is not of the type specified.");
	}
	
	//Setters
	/**
	 * Sets the X-axis position of the {@link Actor}.
	 * @param x The X-axis position to set.
	 */
	public final void setX(float x)
	{
		World world = getWorld();
		if(world != null && !world.getUnbounded())
			_x = Math.max(0, Math.min(world.getWidth(), x));
		else
			_x = x;
	}
	/**
	 * Sets the Y-axis position of the {@link Actor}.
	 * @param y The Y-axis position to set.
	 */
	public final void setY(float y)
	{
		World world = getWorld();
		if(world != null && !world.getUnbounded())
			_y = Math.max(0, Math.min(world.getHeight(), y));
		else
			_y = y;
	}
	/**
	 * Sets the Z-axis position of the {@link Actor}. This is only used to determine render order.
	 * @param z The Z-axis position to set.
	 */
	public final void setZ(float z)
	{
		_z = z;
	}
	/**
	 * Sets the X-axis and Y-axis positions of the {@link Actor}.
	 * @param x The X-axis position to set.
	 * @param y The Y-axis position to set.
	 */
	public final void setLocation(float x, float y)
	{
		World world = getWorld();
		if(world != null && !world.getUnbounded())
		{
			_x = Math.max(0, Math.min(world.getWidth(), x));
			_y = Math.max(0, Math.min(world.getHeight(), y));
		}
		else
		{
			_x = x;
			_y = y;
		}
	}
	/**
	 * Sets the rotation of the {@link Actor} in degrees.
	 * @param rotation The rotation value to set in degrees.
	 */
	public final void setRotation(float rotation)
	{
		_rotation = radians(rotation);
	}
	/**
	 * Sets the width of the {@link Actor}.
	 * @param w The width value to set.
	 */
	public final void setWidth(int w)
	{
		_width = w;
		scaleImage(_width, _height);
	}
	/**
	 * Sets the height of the {@link Actor}.
	 * @param h The height value to set.
	 */
	public final void setHeight(int h)
	{
		_height = h;
		scaleImage(_width, _height);
	}
	/**
	 * Sets the width and height of the {@link Actor}.
	 * @param w The width value to set.
	 * @param h The height value to set.
	 */
	public final void setDimensions(int w, int h)
	{
		_width = w;
		_height = h;
		scaleImage(_width, _height);
	}
	/**
	 * Sets the image of the {@link Actor}.
	 * @param image The {@link processing.core.PImage} to set.
	 */
	public final void setImage(PImage image)
	{
		_sourceImage = image;
		scaleImage(_width, _height);
	}
	/**
	 * Sets the opacity value (in the range 0 - 255) of the {@link Actor}.
	 * @param opacity The opacity value to set, in range 0 - 255.
	 */
	public final void setOpacity(float opacity)
	{
		_opacity = opacity;
	}
	/**
	 * Sets the image resize format of the {@link Actor}.
	 * @param format The image resize format to set. It must be either {@link Green#BILINEAR}, {@link Green#NEAREST_NEIGHBOR}, or {@link Green#TILE}.
	 * @throws UnknownResizeFormatException Thrown when an unknown resize format is supplied.
	 */
	public final void setResizeFormat(int format)
	{
		if(format != Green.BILINEAR && format != Green.NEAREST_NEIGHBOR && format != Green.TILE)
			throw new UnknownResizeFormatException();
		_resizeFormat = format;
	}
	
	//General Methods
	/**
	 * Moves the {@link Actor} {@code amount} units in the direction of the actor's rotation.
	 * @param amount Number of units to move.
	 */
	public final void move(float amount)
	{
		setLocation(_x + cos(_rotation) * amount, _y + sin(_rotation) * amount);
	}
	/**
	 * Moves the {@link Actor} {@code x} units on the X-axis and {@code y} units on the Y-axis.
	 * @param x Number of units to move in the X-axis.
	 * @param y Number of units to move in the Y-axis.
	 */
	public final void moveGlobal(float x, float y)
	{
		setLocation(_x + x, _y + y);
	}
	/**
	 * Rotates the {@link Actor} {@code degrees} amount in degrees.
	 * @param degrees The amount to turn in degrees.
	 */
	public final void turn(float degrees)
	{
		_rotation += radians(degrees);
	}
	
	//Utility Functions
	/**
	 * Checks to see if the {@link Actor} is at the edge of the {@link World} it is currently in.
	 * @return Whether the {@link Actor} is at the edge of it's {@link World}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final boolean isAtEdge() throws NoWorldException
	{
		World world = getWorldUnsafe();
		return (_x <= 0 || _x >= world.getWidth() || _y <= 0 || _y >= world.getHeight());
	}
	/**
	 * Checks to see whether the {@link Actor} intersects another {@link Actor}, using basic rect comparison.
	 * @param actor The other {@link Actor} to check against.
	 * @return Whether this {@link Actor} intersects the other, or false if the same {@link Actor} is supplied.
	 */
	public final boolean intersects(Actor actor)
	{
		//Coords system is down-right +
		if(actor == this)
			return false;
		
		float rot1Sin = sin(_rotation);
		float rot1Cos = cos(_rotation);
		float edge1X = _width / 2f;
		float edge1Y = _height / 2f;
		float rot2Sin = sin(radians(actor.getRotation()));
		float rot2Cos = cos(radians(actor.getRotation()));
		float edge2X = actor.getWidth() / 2f;
		float edge2Y = actor.getHeight() / 2f;
		
		float c1RDX = (edge1X * rot1Cos - edge1Y * rot1Sin) + _x;
		float c1RDY = (edge1X * rot1Sin + edge1Y * rot1Cos) + _y;
		float c1LDX = (-edge1X * rot1Cos - edge1Y * rot1Sin) + _x;
		float c1LDY = (-edge1X * rot1Sin + edge1Y * rot1Cos) + _y;
		float c1RUX = (edge1X * rot1Cos + edge1Y * rot1Sin) + _x;
		float c1RUY = (edge1X * rot1Sin - edge1Y * rot1Cos) + _y;
		float c1LUX = (-edge1X * rot1Cos + edge1Y * rot1Sin) + _x;
		float c1LUY = (-edge1X * rot1Sin - edge1Y * rot1Cos) + _y;
		
		float c2RDX = (edge2X * rot2Cos - edge2Y * rot2Sin) + actor.getX();
		float c2RDY = (edge2X * rot2Sin + edge2Y * rot2Cos) + actor.getY();
		float c2LDX = (-edge2X * rot2Cos - edge2Y * rot2Sin) + actor.getX();
		float c2LDY = (-edge2X * rot2Sin + edge2Y * rot2Cos) + actor.getY();
		float c2RUX = (edge2X * rot2Cos + edge2Y * rot2Sin) + actor.getX();
		float c2RUY = (edge2X * rot2Sin - edge2Y * rot2Cos) + actor.getY();
		float c2LUX = (-edge2X * rot2Cos + edge2Y * rot2Sin) + actor.getX();
		float c2LUY = (-edge2X * rot2Sin - edge2Y * rot2Cos) + actor.getY();
		
		return (
				Green.getLinesIntersect(c1LDX, c1LDY, c1LUX, c1LUY, c2LDX, c2LDY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RDX, c1RDY, c1LDX, c1LDY, c2LDX, c2LDY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1LUX, c1LUY, c1RUX, c1RUY, c2LDX, c2LDY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RUX, c1RUY, c1RDX, c1RDY, c2LDX, c2LDY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1LDX, c1LDY, c1LUX, c1LUY, c2RDX, c2RDY, c2LDX, c2LDY) || 
				Green.getLinesIntersect(c1RDX, c1RDY, c1LDX, c1LDY, c2RDX, c2RDY, c2LDX, c2LDY) || 
				Green.getLinesIntersect(c1LUX, c1LUY, c1RUX, c1RUY, c2RDX, c2RDY, c2LDX, c2LDY) || 
				Green.getLinesIntersect(c1RUX, c1RUY, c1RDX, c1RDY, c2RDX, c2RDY, c2LDX, c2LDY) || 
				Green.getLinesIntersect(c1LDX, c1LDY, c1LUX, c1LUY, c2LUX, c2LUY, c2RDX, c2RDY) || 
				Green.getLinesIntersect(c1RDX, c1RDY, c1LDX, c1LDY, c2LUX, c2LUY, c2RDX, c2RDY) || 
				Green.getLinesIntersect(c1LUX, c1LUY, c1RUX, c1RUY, c2LUX, c2LUY, c2RDX, c2RDY) || 
				Green.getLinesIntersect(c1RUX, c1RUY, c1RDX, c1RDY, c2LUX, c2LUY, c2RDX, c2RDY) || 
				Green.getLinesIntersect(c1LDX, c1LDY, c1LUX, c1LUY, c2RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RDX, c1RDY, c1LDX, c1LDY, c2RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1LUX, c1LUY, c1RUX, c1RUY, c2RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RUX, c1RUY, c1RDX, c1RDY, c2RUX, c2RUY, c2LUX, c2LUY)
			);
	}
	/**
	 * Checks to see if a point defined by {@code pX} and {@code pY} is within the {@link Actor}'s rotated rect.
	 * @param pX The X-axis coordinate of the point to check.
	 * @param pY The Y-axis coordinate of the point to check.
	 * @return Whether the point is within the bounds of the {@link Actor}.
	 */
	public final boolean pointInBounds(float pX, float pY)
	{
		float rotSin = sin(_rotation);
		float rotCos = cos(_rotation);
		float edgeX = _width / 2f;
		float edgeY = _height / 2f;
		
		float cRDX = (edgeX * rotCos - edgeY * rotSin) + _x;
		float cRDY = (edgeX * rotSin + edgeY * rotCos) + _y;
		float cLDX = (-edgeX * rotCos - edgeY * rotSin) + _x;
		float cLDY = (-edgeX * rotSin + edgeY * rotCos) + _y;
		float cRUX = (edgeX * rotCos + edgeY * rotSin) + _x;
		float cRUY = (edgeX * rotSin - edgeY * rotCos) + _y;
		float cLUX = (-edgeX * rotCos + edgeY * rotSin) + _x;
		float cLUY = (-edgeX * rotSin - edgeY * rotCos) + _y;
		
		return Green.pointInRectangle(cLUX, cLUY, cRUX, cRUY, cRDX, cRDY, cLDX, cLDY, pX, pY);
	}
	/**
	 * Retrieves a list of all objects matching {@code type} in the {@link World} that intersect with the {@link Actor}, using basic rect comparison.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param type The type of other {@link Actor} to compare against.
	 * @return The list of intersecting objects matching {@code type}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> List<A> getIntersectingObjects(Class<A> type) throws NoWorldException //Compares rects of images
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		List<A> retList = new ArrayList<>();
		for(A actor : actors)
			if(intersects(actor))
				retList.add(actor);
		return retList;
	}
	/**
	 * Retrieves the first object matching {@code type} in the {@link World} that intersects with the {@link Actor}, using basic rect comparison.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param type The type of other {@link Actor} to compare against.
	 * @return The first intersecting object matching {@code type}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> A getOneIntersectingObject(Class<A> type) throws NoWorldException //Compares rects of images
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		for(A actor : actors)
			if(intersects(actor))
				return actor;
		return null;
	}
	/**
	 * Retrieves a list of all objects matching {@code type} within {@code range} of an offset.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param oX The X-axis value of the offset.
	 * @param oY The Y-axis value of the offset.
	 * @param type The type of {@link Actor} to match.
	 * @param range The maximum distance an {@link Actor} can be from the offset point to match.
	 * @return A list of all objects in the {@link World} matching {@code type} at ({@code oX}, {@code oY}) from the {@link Actor}'s position.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> List<A> getObjectsAtOffset(float oX, float oY, Class<A> type, float range) throws NoWorldException
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		List<A> retList = new ArrayList<>();
		float tX = getX() + oX;
		float tY = getY() + oY;
		range = Math.abs(range);
		for(A actor : actors)
			if(Green.getPointsDist(tX, tY, actor.getX(), actor.getY()) <= range)
				retList.add(actor);
		return retList;
	}
	/**
	 * Retrieves a list of all objects matching {@code type} within 1 unit of an offset.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param oX The X-axis value of the offset.
	 * @param oY The Y-axis value of the offset.
	 * @param type The type of {@link Actor} to match.
	 * @return A list of all objects in the {@link World} matching {@code type} at ({@code oX}, {@code oY}) from the {@link Actor}'s position.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> List<A> getObjectsAtOffset(float oX, float oY, Class<A> type) throws NoWorldException
	{
		return getObjectsAtOffset(oX, oY, type, 1f);
	}
	/**
	 * Retrieves the first object matching {@code type} within {@code range} of an offset.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param oX The X-axis value of the offset.
	 * @param oY The Y-axis value of the offset.
	 * @param type The type of {@link Actor} to match.
	 * @param range The maximum distance an {@link Actor} can be from the offset point to match.
	 * @return The first object in the {@link World} matching {@code type} at ({@code oX}, {@code oY}) from the {@link Actor}'s position.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> A getOneObjectAtOffset(float oX, float oY, Class<A> type, float range) throws NoWorldException
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		float tX = getX() + oX;
		float tY = getY() + oY;
		range = Math.abs(range);
		for(A actor : actors)
			if(Green.getPointsDist(tX, tY, actor.getX(), actor.getY()) <= range)
				return actor;
		return null;
	}
	/**
	 * Retrieves the first object matching {@code type} within 1 unit of an offset.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param oX The X-axis value of the offset.
	 * @param oY The Y-axis value of the offset.
	 * @param type The type of {@link Actor} to match.
	 * @return The first object in the {@link World} matching {@code type} at ({@code oX}, {@code oY}) from the {@link Actor}'s position.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> A getOneObjectAtOffset(float oX, float oY, Class<A> type) throws NoWorldException
	{
		return getOneObjectAtOffset(oX, oY, type, 1f);
	}
	/**
	 * Retrieves a list of all objects in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param type The type of {@link Actor} to match.
	 * @param range The maximum distance an {@link Actor} can be from the offset point to match.
	 * @return A list of all objects in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> List<A> getObjectsInRange(Class<A> type, float range) throws NoWorldException
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		List<A> retList = new ArrayList<>();
		range = Math.abs(range);
		for(A actor : actors)
			if(Green.getPointsDist(_x, _y, actor.getX(), actor.getY()) <= range)
				retList.add(actor);
		return retList;
	}
	/**
	 * Retrieves the first object in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param type The type of {@link Actor} to match.
	 * @param range The maximum distance an {@link Actor} can be from the offset point to match.
	 * @return The first object in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> A getOneObjectInRange(Class<A> type, float range) throws NoWorldException
	{
		World world = getWorldUnsafe();
		List<A> actors = world.getObjects(type);
		range = Math.abs(range);
		for(A actor : actors)
			if(Green.getPointsDist(_x, _y, actor.getX(), actor.getY()) <= range)
				return actor;
		return null;
	}
	/**
	 * Retrieves a list of all objects in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * Due to the difference in how this library and Greenfoot work, this is just an alias to {@link #getObjectsInRange(Class, float)}.
	 * @param <A> The type of {@link Actor} to return if possible, as defined by {@code type}.
	 * @param type The type of {@link Actor} to match.
	 * @param range The maximum distance an {@link Actor} can be from the offset point to match.
	 * @return A list of all objects in the {@link World} matching {@code type} within {@code range} of the {@link Actor}.
	 * @throws NoWorldException Thrown when the method is called and the {@link Actor} is not part of a {@link World}.
	 */
	public final <A extends Actor> List<A> getNeighbours(Class<A> type, float range) throws NoWorldException
	{
		return getObjectsInRange(type, range);
	}
	/**
	 * Rotates the {@link Actor} to face it's right side towards the position ({@code x}, {@code y}). Does nothing if the position provided is the position of the {@link Actor}.
	 * @param x The X-axis position to turn towards.
	 * @param y The Y-axis position to turn towards.
	 */
	public final void turnTowards(float x, float y)
	{
		if(x == _x && y == _y) return; //Because it is impossible to turn towards where you already are
		_rotation = atan2(y - _y, x - _x);
	}
	/**
	 * Rotates the {@link Actor} to face it's right side towards another {@link Actor}.
	 * @param obj The other {@link Actor} to turn towards.
	 */
	public final void turnTowards(Actor obj)
	{
		if(obj == this || (obj.getX() == _x && obj.getY() == _y)) return; //Because it is impossible to turn towards where you already are
		_rotation = atan2(obj.getY() - _y, obj.getX() - _x);
	}
	
	//Mouse Methods
	/**
	 * Retrieves whether a specific mouse button is currently down on this {@link Actor}.
	 * @param mouseButton The mouse button to check for - either {@link PConstants#LEFT}, {@link PConstants#CENTER}, or {@link PConstants#RIGHT}.
	 * @return Whether {@code mouseButton} is down.
	 */
	public final boolean isMouseButtonDownHere(int mouseButton)
	{
		return green.isMouseButtonDown(mouseButton) && pointInBounds(app.mouseX, app.mouseY);
	}
	/**
	 * Retrieves whether a specific mouse button was pressed this frame on this {@link Actor}.
	 * @param mouseButton The mouse button to check for - either {@link PConstants#LEFT}, {@link PConstants#CENTER}, or {@link PConstants#RIGHT}.
	 * @return Whether {@code mouseButton} was pressed in this frame.
	 */
	public final boolean isMouseButtonDownThisFrameHere(int mouseButton)
	{
		return green.isMouseButtonDownThisFrame(mouseButton) && pointInBounds(app.mouseX, app.mouseY);
	}
	/**
	 * Retrieves whether a specific mouse button was released this frame on this {@link Actor}.
	 * @param mouseButton The mouse button to check for - either {@link PConstants#LEFT}, {@link PConstants#CENTER}, or {@link PConstants#RIGHT}.
	 * @return Whether {@code mouseButton} was released in this frame.
	 */
	public final boolean isMouseButtonUpThisFrameHere(int mouseButton)
	{
		return green.isMouseButtonUpThisFrame(mouseButton) && pointInBounds(app.mouseX, app.mouseY);
	}
	/**
	 * Retrieves whether the mouse is currently scrolling on this {@link Actor}.
	 * @return Whether the mouse is currently scrolling.
	 */
	public final boolean isMouseScrollingHere()
	{
		return green.isMouseScrolling() && pointInBounds(app.mouseX, app.mouseY);
	}
	
	//Base Methods
	/**
	 * Renders the {@link Actor} to the screen. By default, this method renders the {@link Actor} with its image, position, rotation, and opacity taken into account, but it can be overridden for further use.
	 * When drawing, everything is relative to the {@link Actor}'s position.
	 */
	public void draw() //Overridable
	{
		if(_image != null)
		{
			app.smooth();
			app.tint(255, _opacity);
			//app.translate(_x, _y);
			app.rotate(_rotation);
			app.translate(-_width / 2f, -_height / 2f);
			app.image(_image, 0, 0, _width, _height);
		}
		else
		{
			app.fill(GREEN_COLOUR);
			app.rect(-_width / 2f, -_height / 2f, _width, _height);
		}
	}
	/**
	 * Called when the {@link Actor} is added to any {@link World}. By default, this method does nothing.
	 * @param world The {@link World} the {@link Actor} was added to.
	 */
	public void addedToWorld(World world) {}
	/**
	 * Called when the {@link Actor} is removed from any {@link World}. By default, this method does nothing.
	 * @param world The {@link World} the {@link Actor} was removed from.
	 */
	public void removedFromWorld(World world) {}
	
	/**
	 * Called once per frame. This is likely where most work with the {@link Actor} will be done.
	 * @param deltaTime The time, in seconds, since the last time this method was called.
	 */
	public abstract void act(float deltaTime);
}
