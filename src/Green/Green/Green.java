package Green;

import java.util.*;
import java.util.stream.Collectors;

import processing.core.*;
import static processing.core.PApplet.sin;
import static processing.core.PApplet.cos;
import static processing.core.PApplet.tan;

public class Green
{
	private static Green instance;
	private PApplet parent;
	
	private static World _currentWorld;
	
	private Set<InputKey> _keysDown = new HashSet<InputKey>();
	
	//Constructors
	public Green(PApplet theParent)
	{
		instance = this;
		parent = theParent;
	}
	
	//Getters
	/**
	 * Retrieves the current instance of the library that is running.
	 * @return The current instance of the library that is running.
	 */
	public static Green getInstance()
	{
		return instance;
	}
	/**
	 * Retrieves the currently-loaded {@link World}.
	 * @return The currently-loaded {@link World}.
	 */
	public static World getWorld()
	{
		return _currentWorld;
	}
	/**
	 * Retrieves the parent instance of Processing.
	 * @return The parent instance of Processing.
	 */
	public PApplet getParent()
	{
		return parent;
	}
	
	//Math Methods
	/**
	 * Calculates the distance between two points (({@code x1}, {@code y1}) and ({@code x2}, {@code y2})).
	 * @param x1 The X-axis position of point 1.
	 * @param y1 The Y-axis position of point 1.
	 * @param x2 The X-axis position of point 2.
	 * @param y2 The Y-axis position of point 2.
	 * @return The distance between the two points.
	 */
	public static float getPointsDist(float x1, float y1, float x2, float y2)
	{
		return (float) Math.hypot(x2 - x1, y2 - y1);
	}
	/**
	 * Determines whether two line segments ([({@code a1x}, {@code a1y}) - ({@code a2x}, {@code a2y})] and [({@code b1x}, {@code b1y}) - ({@code b2x}, {@code b2y})]) intersect.
	 * @param a1x The X-axis position of point 1 of line segment A.
	 * @param a1y The Y-axis position of point 1 of line segment A.
	 * @param a2x The X-axis position of point 2 of line segment A.
	 * @param a2y The Y-axis position of point 2 of line segment A.
	 * @param b1x The X-axis position of point 1 of line segment B.
	 * @param b1y The Y-axis position of point 1 of line segment B.
	 * @param b2x The X-axis position of point 2 of line segment B.
	 * @param b2y The Y-axis position of point 2 of line segment B.
	 * @return Whether or not the two line segments intersect.
	 */
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
	/**
	 * Calculates the cosecant of {@code angle} in radians.
	 * @param angle The angle in radians to calculate the cosecant of.
	 * @return The cosecant of {@code angle}.
	 */
	public static float csc(float angle)
	{
		return 1f / sin(angle);
	}
	/**
	 * Calculates the secant of {@code angle} in radians.
	 * @param angle The angle in radians to calculate the secant of.
	 * @return The secant of {@code angle}.
	 */
	public static float sec(float angle)
	{
		return 1f / cos(angle);
	}
	/**
	 * Calculates the cotangent of {@code angle} in radians.
	 * @param angle The angle in radians to calculate the cotangent of.
	 * @return The cotangent of {@code angle}.
	 */
	public static float cot(float angle)
	{
		return 1f / tan(angle);
	}
	
	//Utility Methods
	/**
	 * Calculates the number of digits in a given integer.
	 * @param value The integer to measure the number of digits of.
	 * @return The number of digits in {@code value}.
	 */
	public static int getDigits(int value)
	{
		return String.valueOf(value).length();
	}
	
	//Base Methods
	/**
	 * Loads a {@link World} so to make it the one currently in use.
	 * @param world The {@link World} to load.
	 */
	public void loadWorld(World world)
	{
		_currentWorld = world;
		//parent.setSize(world.getWidth(), world.getHeight());
		world.prepare();
	}
	/**
	 * Calls the {@link Actor#draw()} method on every {@link Actor}, drawing all objects in the currently-loaded {@link World} to screen.
	 * @throws NoWorldException Thrown when the method is called and there is no {@link World} loaded.
	 */
	public void handleDraw() throws NoWorldException
	{
		World world = getWorld();
		if(world == null) throw new NoWorldException();
		world.handleDraw();
	}
	/**
	 * Calls the {@link Actor#act(float)} method on every {@link Actor} in the currently-loaded {@link World}.
	 * @throws NoWorldException Thrown when the method is called and there is no {@link World} loaded.
	 */
	public void handleAct() throws NoWorldException
	{
		World world = getWorld();
		if(world == null) throw new NoWorldException();
		world.handleAct();
	}
	public final void handleInputDown(char key, int keyCode)
	{
		_keysDown.add(new InputKey(key, keyCode));
	}
	public final void handleInputUp(char key, int keyCode)
	{
		_keysDown.remove(new InputKey(key, keyCode));
	}
	public final boolean isKeyDown(char key)
	{
		List<InputKey> result = _keysDown.stream()
				.filter(item -> item.getKey() == key)
				.collect(Collectors.toList());
		return result.size() > 0;
	}
	public final boolean isKeyDown(int keyCode)
	{
		List<InputKey> result = _keysDown.stream()
				.filter(item -> item.getKeyCode() == keyCode)
				.collect(Collectors.toList());
		return result.size() > 0;
	}
	public final boolean isKeyDown(InputKey key)
	{
		return _keysDown.contains(key);
	}
}

