package Green;

import processing.core.*;

/**
 * This is a template class and can be used to start a new processing Library.
 * Make sure you rename this class as well as the name of the example package 'template' 
 * to your own Library naming convention.
 * 
 * (the tag example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 * @example Hello 
 */

public class Green
{
	private static Green instance;
	private PApplet parent;
	
	private static World _currentWorld;
	
	private final static String VERSION = "##library.prettyVersion##";
	
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
	
	//Base Methods
	public void loadWorld(World world)
	{
		_currentWorld = world;
		world.prepare();
	}
}

