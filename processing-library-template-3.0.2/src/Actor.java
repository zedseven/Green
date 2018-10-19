package Green;

import java.util.*;
import processing.core.*;

import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
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
	public Actor(float x, float y, PImage image)
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
		return (_x + _width / 2 < 0 || _x + _width / 2 > world.getWidth() || _y + _height / 2 < 0 || _y + _height / 2 > world.getHeight());
	}
	//Coordssystem is down-right +
	public final Actor getOneIntersectingObject(Class type) //Compares rects of images
	{
		List<Actor> actors = Green.getWorld().getObjects(type);
		for(Actor actor : actors)
		{
			if(actor == this)
				continue;
			//if((actors.get(i).getX() - actors.get(i).getWidth() > _x || actors.get(i).getX() < _x + _width) &&
			//		(actors.get(i).getY() + actors.get(i).getHeight() > _y || actors.get(i).getY() < _y + _height))
			if(((actor.getX() - actor.getWidth() / 2 > _x - _width / 2 && actor.getX() - actor.getWidth() / 2 < _x + _width / 2) || actor.getX() + actor.getWidth() / 2 < _x + _width / 2)
				&& (actor.getY() - actor.getHeight() / 2 > _y - _height / 2 || actor.getY() + actor.getHeight() / 2 < _y + _height / 2))
			{
				return actor;
			}
		}
		return null;
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