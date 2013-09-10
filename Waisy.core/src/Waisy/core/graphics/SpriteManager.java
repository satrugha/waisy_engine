package Waisy.core.graphics;

import java.util.Vector;

/**
 * This is the most basic renderer used within the engine.
 * Major modules may make their own child version of this 
 * renderer to do custom rendering.
 * 
 * For example, the platformer module will have a renderer 
 * capable of drawing backgrounds in grid format. The RPG module 
 * (possibly others with similar modules) will have a renderer 
 * capable of flipping canvases to flip between battle and map 
 * modes when adventuring (with the possibility of other modes
 * included).
 * 
 * The sprite manager uses a basic layering technique. 
 * The screen is broken up into layers to allow for easy 
 * access to foreground, midground, and background. a layer
 * for a parallaxing background will be added as soon as the
 * parallax class is created.
 * 
 * In general, layering is best as follows:
 * Background: anything you can walk in front of or over
 * Midground: player and enemies.
 *  oftentimes anything you can't walk through.
 *  For a platformer, your grid (blocks, platforms, etc) 
 *  would go here.
 * Foreground: anything you can walk behind.
 * This is a general rule of thumb and may vary depending on 
 * the module and how the game is written.
 * 
 * 
 * Note: the Sprite manager does not handle any sort of
 * collision detection
 * 
 * Note2: All objects in this class must stem from the 
 * BasicSprite class or be a BasicSprite.
 * 
 * @author waisy
 */
public class SpriteManager 
{
	/**
	 * This class is a singleton. Hold a reference
	 * to the instance of the class
	 */
	private static SpriteManager instance = null;
	
	
	//TODO: add parallax element(s)
	
	/**
	 * All sprites to be rendered within the foreground
	 */
	protected Vector<BasicSprite> foreground;
	/**
	 * All sprites to be rendered within the midground.
	 */
	protected Vector<BasicSprite> midground;
	/**
	 * All sprites to be rendered within the background
	 */
	protected Vector<BasicSprite> background;
	
	//TODO: foreground parallax?
	
	
	/**
	 * Private initializer.
	 */
	private SpriteManager()
	{
		//contructor: Vector(int size, int increment)
		foreground = new Vector<BasicSprite>(1,2);
		midground = new Vector<BasicSprite>(5,5);
		background = new Vector<BasicSprite>(1,2);
	}
	
	
	/**
	 * return an instance to the sprite manager
	 */
	public static SpriteManager getInstance()
	{
		if (instance == null)
			instance = new SpriteManager();
		
		return instance;
	}
}
