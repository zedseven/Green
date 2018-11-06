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
	
	//Math Methods
	public static float getPointsDist(float x1, float y1, float x2, float y2)
	{
		return (float) (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
	}
	public static boolean getLinesIntersect(float a1x, float a1y, float a2x, float a2y, float b1x, float b1y, float b2x, float b2y)
	{		
		float iX;
		float iY;
		
		float aM = (a2y - a1y) / (a2x - a1x);
		float aB = a1y - aM * a1x;
		float bM = (b2y - b1y) / (b2x - b1x);
		float bB = b1y - bM * b1x;
		
		if (a1x == a2x) iX = a1x;
		else if (b1x == b2x) iX = b1x;
		else
		{
			if (aM == bM) return false;

			iX = (bB - aB) / (aM - bM);
			//float iY = aM * iX + aB;
		}
		iY = !Float.isNaN(aM) && !Float.isInfinite(aM) ? aM * iX + aB : bM * iX + bB;
		
		return Math.min(a1x, a2x) <= iX && iX <= Math.max(a1x, a2x) && Math.min(b1x, b2x) <= iX && iX <= Math.max(b1x, b2x) &&
		       Math.min(a1y, a2y) <= iY && iY <= Math.max(a1y, a2y) && Math.min(b1y, b2y) <= iY && iY <= Math.max(b1y, b2y);
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
		//parent.setSize(world.getWidth(), world.getHeight());
		world.prepare();
	}
}

