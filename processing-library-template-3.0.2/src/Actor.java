package Green;

import java.util.*;
import processing.core.*;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.atan;
import static processing.core.PApplet.degrees;
import static processing.core.PApplet.radians;

public abstract class Actor
{
	private Green green;
	private PApplet app;
	
	private float _x = 0;
	private float _y = 0;
	private float _rotation = 0;
	private PImage _image;
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
	public final float getCenterX()
	{
		return _x + _width / 2f;
	}
	public final float getCenterY()
	{
		return _y + _height / 2f;
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
	public static World getWorld()
	{
		return Green.getWorld();
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
		return (getCenterX() < 0 || getCenterX() > world.getWidth() || getCenterY() < 0 || getCenterY() > world.getHeight());
	}
	public final Actor getOneIntersectingObject(Class type) //Compares rects of images
	{
		//Coords system is down-right +
		//TODO: Add in rotation support
		List<Actor> actors = Green.getWorld().getObjects(type);
		for(Actor actor : actors)
		{
			if(actor == this)
				continue;
			if(((actor.getX() > _x && actor.getX() < _x + _width) || (actor.getX() + actor.getWidth() > _x && actor.getX() + actor.getWidth() < _x + _width)) &&
					((actor.getY() > _y && actor.getY() < _y + _height) || (actor.getY() + actor.getHeight() > _y && actor.getY() + actor.getHeight() < _y + _height)))
			{
				return actor;
			}
		}
		return null;
	}
	public final List<Actor> getObjectsInRange(float range, Class type)
	{
		List<Actor> actors = Green.getWorld().getObjects(type);
		List<Actor> retList = new ArrayList<Actor>();
		for(int i = 0; i < actors.size(); i++)
			if(Green.getPointsDist(getCenterX(), actors.get(i).getCenterX(), getCenterY(), actors.get(i).getCenterY()) <= range)
				retList.add(actors.get(i));
		return retList;
	}
	public final void turnTowards(float x, float y)
	{
		if(x == _x && y == _y)
			return;
		_rotation = degrees(atan((x - getCenterX()) / (y - getCenterY())));
	}
	public final void turnTowards(Actor obj)
	{
		if(obj == this || (obj.getCenterX() == getCenterX() && obj.getCenterY() == getCenterY()))
			return;
		_rotation = degrees(atan((obj.getCenterX() - getCenterX()) / (obj.getCenterY() - getCenterY())));
	}
	
	//Base Methods
	public final void draw()
	{
		app.pushMatrix();
		app.smooth();
		//app.translate(width / 2, height / 2);
		app.translate(_x + _width / 2, _y + _height / 2);
		app.rotate(radians(_rotation));
		app.translate(-_x - _width / 2, -_y - _height / 2);
		app.tint(255, _transparency);
		app.image(_image, _x, _y, _width, _height);
		app.popMatrix();
	}
	
	public abstract void act();
}