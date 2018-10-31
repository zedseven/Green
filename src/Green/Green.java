package Green;

import processing.core.*;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.tan;

public class Green
{
	private static Green instance;
	private PApplet parent;
	
	private static World _currentWorld;
	
	private final static String VERSION = "0.1.0";
	
	//Constructors
	public Green(PApplet theParent)
	{
		instance = this;
		parent = theParent;
		System.out.println("Green v" + VERSION + " initialized.");
	}
	
	//Getters
	public static Green getInstance()
	{
		return instance;
	}
	public static World getWorld()
	{
		return _currentWorld;
	}
	public PApplet getParent()
	{
		return parent;
	}
	public static String getVersion()
	{
		return VERSION;
	}
	public static float getPointsDist(float x1, float y1, float x2, float y2)
	{
		return (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
	}
	
	//Static Methods
	public static float csc(float angle)
	{
		return 1f / sin(angle);
	}
	public static float sec(float angle)
	{
		return 1f / cos(angle);
	}
	public static float cot(float angle)
	{
		return 1f / tan(angle);
	}
	
	//Utility Methods
	public static int getDigits(int value)
	{
		return String.valueOf(value).length();
	}
	
	//Base Methods
	public void loadWorld(World world)
	{
		_currentWorld = world;
		world.prepare();
	}
}

