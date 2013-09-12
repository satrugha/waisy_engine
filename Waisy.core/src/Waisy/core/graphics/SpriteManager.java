package Waisy.core.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import Waisy.core.core.GameSettings;

/**
 * This is the most basic renderer used within the engine.
 * Major modules may make their own child version of this 
 * renderer to do custom rendering.
 * 
 * For example, the platformer module will have a renderer 
 * capable of drawing backgrounds in grid format.
 * 
 * To have multiple renderers, use a game manager to flip
 * between canvases or flush and populate sprites. 
 * For example, in RPGs, the game manager will be 
 * capable of flipping canvases to flip between battle and map 
 * modes when adventuring (with the possibility of other modes
 * included).
 * 
 * The manager uses a basic layering technique. 
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
 * the module and how the game is written. Note that anything
 * held within these layers is capable of being panned when the
 * "camera" moves.
 * 
 * If you don't want the background to move, put the sprite in
 * "staticBackground".
 * 
 * 
 * Note: All objects in this class must stem from the 
 * BasicSprite class or be a BasicSprite.
 * 
 * @author waisy
 */
public class SpriteManager 
{
//	/**
//	 * This class is a singleton. Hold a reference
//	 * to the instance of the class
//	 */
//	private static SpriteManager instance = null;
	
	
	//TODO: add parallax element(s)
	
	/**
	 * Static, unpannable background.
	 */
	protected BasicSprite staticBackground = null;
	
	/**
	 * if you want a static color in the background
	 * (think SMB1 with its standard one-color background),
	 * set this to true and the renderer will draw a primitive
	 * rectangle covering the entire screen 
	 * in the background of your game.
	 */
	protected boolean staticBackgroundColor = false;
	
	
	/**
	 * All sprites to be rendered within the game
	 */
	protected Vector<Vector<BasicSprite>> spriteList;
	
	//TODO: foreground parallax?
	
	public static final int BACKGROUND = 0;
	public static final int MIDGROUND = 1;
	public static final int FOREGROUND = 2;
	
	

	public SpriteManager()
	{
		spriteList = new Vector<Vector<BasicSprite>>(3);
		//contructor: Vector(int size, int increment)
		spriteList.add(new Vector<BasicSprite>(1,2)); //background
		spriteList.add(new Vector<BasicSprite>(5,5)); //midground
		spriteList.add(new Vector<BasicSprite>(1,2)); //foreground
	}
	
//	
//	/**
//	 * return an instance to the sprite manager
//	 */
//	public static SpriteManager getInstance()
//	{
//		if (instance == null)
//			instance = new SpriteManager();
//		
//		return instance;
//	}
	
	
	/**
	 * Add a sprite to a particular layer
	 * @param s BasicSprite or any child class. 
	 * @param layer The layer to add the sprite. 
	 * Use the static constants included in this class
	 * for the layer variable.
	 */
	public void addSprite(BasicSprite s, int layer)
	{
		if ((s != null)
				&& (spriteList != null)
				&& (spriteList.get(layer) != null))//safety check
		{
			spriteList.get(layer).addElement(s);
		}
	}
	
	/**
	 * Add the player character to the front of the 
	 * render list. Doing this will update the player 
	 * and all calculations before calculating anything
	 * else.
	 * @param s Player object
	 */
	public void addPlayerCharacter(BasicSprite s)
	{
		if ((s != null)
				&& (spriteList != null)
				&& (spriteList.get(SpriteManager.MIDGROUND) != null))
			//safety check
		{
			spriteList.get(SpriteManager.MIDGROUND).add(0, s);
		}
	}
	
	
	/**
	 * Render the available sprites.
	 * Rendering is done using the painter's algorithm,
	 * which means back to front. Anything wholly offscreen
	 * is skipped to save rendering time and calculations.
	 * @param g
	 */
	public void renderList(Graphics g)
	{
		//render the static color first
		if (staticBackgroundColor)
		{
			//cast into Graphics2D else we won't
			//be able to use geometric primitives
			Graphics2D g2d = (Graphics2D) g;
			Color c = g2d.getColor();
			
			//set the paint color
			g2d.setColor(GameSettings.COLOR_BACKGROUND);
			
			//fill the entire background
			g2d.fill(new Rectangle2D.Double(0,0,
					GameSettings.SCREEN_WIDTH,
					GameSettings.SCREEN_HEIGHT));
			
			//reset paint color
			g2d.setColor(c);
		}
		
		//then render the static background
		if (staticBackground != null)
			staticBackground.paint(g);
		
		//TODO: render background parallax
		
		//render the sprite list
		for (int i = 0; i < spriteList.capacity(); i++)
		{
			//if there are no sprites in the list,
			//simply jump to the next one in the masterlist.
			if (spriteList.get(i).capacity() < 1)
				continue;
			
			for (int j = 0; j < spriteList.get(i).capacity(); j++)
			{
				//TODO: add off-screen check
				
				//this is a valid, on-screen sprite
				spriteList.get(i).get(j).paint(g);
			}
		}
		
		//TODO: render foreground parallax
	}
	
	public void updateList(float dT)
	{
		
	}
}
