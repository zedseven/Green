package Green;

import java.util.*;
import processing.core.*;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.atan2;
import static processing.core.PApplet.degrees;
import static processing.core.PApplet.radians;

public abstract class Actor
{
	private Green green;
	private PApplet app;
	
	private float _x = 0;
	private float _y = 0;
	private float _z = 0;
	private float _rotation = 0;
	private PImage _image = null;
	private float _width;
	private float _height;
	private float _transparency = 255;
	
	//Constructors
	public Actor(float x, float y, PImage image) //TODO: Add GIF support
	{
		_x = x;
		_y = y;
		_image = image;
		_width = _image.width;
		_height = _image.height;
		init();
	}
	public Actor(float x, float y, float w, float h)
	{
		_x = x;
		_y = y;
		_width = w;
		_height = h;
		init();
	}
	public Actor(float x, float y, PImage image, float w, float h)
	{
		_x = x;
		_y = y;
		_image = image;
		_width = w;
		_height = h;
		init();
	}
	private void init()
	{
		green = Green.getInstance();
		app = green.getParent();
	}
	
	//Getters
	public final float getX()
	{
		return _x;
	}
	public final float getY()
	{
		return _y;
	}
	public final float getZ()
	{
		return _z;
	}
	public final float getRotation()
	{
		return _rotation;
	}
	public final float getWidth()
	{
		return _width;
	}
	public final float getHeight()
	{
		return _height;
	}
	public final PImage getImage()
	{
		return _image;
	}
	public final float getTransparency()
	{
		return _transparency;
	}
	public final World getWorld()
	{
		World currentWorld = Green.getWorld();
		if(currentWorld.hasObject(this))
			return currentWorld;
		return null;
	}
	public static <W extends World> W getWorldOfType(Class<W> type)
	{
		//ClassCastException
		if(type.isInstance(Green.getWorld()))
			return (W) Green.getWorld();
		else
			throw new ClassCastException("The current world is not of the type specified.");
	}
	
	//Setters
	public final void setX(float x)
	{
		_x = x;
	}
	public final void setY(float y)
	{
		_y = y;
	}
	public final void setZ(float z)
	{
		_z = z;
	}
	public final void setLocation(float x, float y)
	{
		_x = x;
		_y = y;
	}
	public final void setRotation(float rotation)
	{
		_rotation = rotation;
	}
	public final void setWidth(float w)
	{
		_width = w;
	}
	public final void setHeight(float h)
	{
		_height = h;
	}
	public final void setDimensions(float w, float h)
	{
		_width = w;
		_height = h;
	}
	public final void setImage(PImage image)
	{
		_image = image;
	}
	public final void setTransparency(float transparency)
	{
		_transparency = transparency;
	}
	
	//General Methods
	/**
	 * Moves {@code amount} units in the direction of the actor's rotation.
	 * @param amount Number of units to move.
	 */
	public final void move(float amount)
	{
		_x += cos(radians(_rotation)) * amount;
		_y += sin(radians(_rotation)) * amount;
	}
	public final void moveGlobal(float x, float y)
	{
		_x += x;
		_y += y;
	}
	public final void turn(float degrees)
	{
		_rotation += degrees;
	}
	
	//Utility Functions
	public final boolean isAtEdge()
	{
		World world = Green.getWorld();
		return (_x < 0 || _x > world.getWidth() || _y < 0 || _y > world.getHeight());
	}
	public final boolean intersects(Actor actor)
	{
		//Coords system is down-right +
		if(actor == this)
			return false;
		
		float rot1Sin = sin(radians(_rotation));
		float rot1Cos = cos(radians(_rotation));
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
				Green.getLinesIntersect(c1LDX, c1LDY, c1LUX, c1LUY, c1RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RDX, c1RDY, c1LDX, c1LDY, c1RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1LUX, c1LUY, c1RUX, c1RUY, c1RUX, c2RUY, c2LUX, c2LUY) || 
				Green.getLinesIntersect(c1RUX, c1RUY, c1RDX, c1RDY, c1RUX, c2RUY, c2LUX, c2LUY)
				);
		
		/*return (((actor.getX() >= _x && actor.getX() <= _x + _width) || (actor.getX() + actor.getWidth() >= _x && actor.getX() + actor.getWidth() <= _x + _width)) &&
				((actor.getY() >= _y && actor.getY() <= _y + _height) || (actor.getY() + actor.getHeight() >= _y && actor.getY() + actor.getHeight() <= _y + _height)));*/
	}
	public final <A extends Actor> A getOneIntersectingObject(Class<A> type) //Compares rects of images
	{
		List<A> actors = (List<A>) getWorld().getObjects(type);
		for(A actor : actors)
			if(intersects(actor))
				return actor;
		return null;
	}
	public final <A extends Actor> List<A> getObjectsAtOffset(float oX, float oY, Class<A> type)
	{
		List<Actor> actors = getWorld().getObjects(type);
		List<A> retList = new ArrayList<A>();
		float tX = getX() + oX;
		float tY = getY() + oY;
		for(Actor actor : actors)
			if(Green.getPointsDist(tX, actor.getX(), tY, actor.getY()) <= 1f /*CHANGE HERE LATER*/)
				retList.add((A) actor);
		return retList;
	}
	public final <A extends Actor> A getOneObjectAtOffset(float oX, float oY, Class<A> type)
	{
		List<Actor> actors = getWorld().getObjects(type);
		float tX = getX() + oX;
		float tY = getY() + oY;
		for(Actor actor : actors)
			if(Green.getPointsDist(tX, actor.getX(), tY, actor.getY()) <= 1f /*CHANGE HERE LATER*/)
				return (A) actor;
		return null;
	}
	public final <A extends Actor> List<A> getObjectsInRange(float range, Class<A> type)
	{
		List<Actor> actors = getWorld().getObjects(type);
		List<A> retList = new ArrayList<A>();
		for(Actor actor : actors)
			if(Green.getPointsDist(_x, actor.getX(), _y, actor.getY()) <= range)
				retList.add((A) actor);
		return retList;
	}
	public final <A extends Actor> A getOneObjectInRange(float range, Class<A> type)
	{
		List<Actor> actors = getWorld().getObjects(type);
		for(Actor actor : actors)
			if(Green.getPointsDist(_x, actor.getX(), _y, actor.getY()) <= range)
				return (A) actor;
		return null;
	}
	public final <A extends Actor> List<A> getNeighbours(float range, Class<A> type)
	{
		return getObjectsInRange(range, type);
	}
	public final void turnTowards(float x, float y)
	{
		if(x == _x && y == _y) return;
		_rotation = degrees(atan2(y - _y, x - _x));
	}
	public final void turnTowards(Actor obj)
	{
		if(obj == this || (obj.getX() == _x && obj.getY() == _y)) return; //Because it is impossible to turn towards where you already are
		_rotation = degrees(atan2(obj.getY() - _y, obj.getX() - _x));
	}
	
	//Base Methods
	public void draw() //Overridable
	{
		if(_image == null) return;
		app.smooth();
		app.tint(255, _transparency);
		//app.translate(_x, _y);
		app.rotate(radians(_rotation));
		app.translate(-_width / 2f, -_height / 2f);
		app.image(_image, 0, 0, _width, _height);
	}
	public void addedToWorld(World world) {}
	public void removedFromWorld(World world) {}
	
	public abstract void act(float deltaTime);
}