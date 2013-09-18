package Waisy.core.graphics;

import java.awt.Graphics;
import java.util.Vector;

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
	
	//TODO: add parallax element(s)
	
	/**
	 * Static, unpannable background. Background will
	 * update with manager's main update loop if it
	 * is a valid sprite. This allows for animated
	 * backgrounds.
	 */
	protected BasicSprite staticBackground = null;
	
	
	/**
	 * All sprites to be rendered within the game
	 */
	protected Vector<Vector<BasicSprite>> spriteList;
	
	//TODO: foreground parallax?
	
	public static final int BACKGROUND = 0;
	public static final int MIDGROUND_ENVIRONMENT = 1;
	public static final int ENEMIES = 2;
	public static final int CHARACTERS = 3;
	public static final int FOREGROUND = 4;
	
	

	public SpriteManager()
	{
		spriteList = new Vector<Vector<BasicSprite>>(3);
		//contructor: Vector(int size, int increment)
		spriteList.add(new Vector<BasicSprite>(2,2)); //background
		
		//midground will probably hold more sprites so we give
		//a larger increment and size to layers with larger requirements
		spriteList.add(new Vector<BasicSprite>(5,5)); //midground environment
		spriteList.add(new Vector<BasicSprite>(5,5)); //enemy list
		spriteList.add(new Vector<BasicSprite>(2,2)); //character/player list
		
		spriteList.add(new Vector<BasicSprite>(2,2)); //foreground
	}
	
	
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
	 * tells the renderer to render the static background.
	 * The renderer will let the sprite do all the placement
	 * calculations. The renderer will also call update
	 * on this sprite if you want to create animated static
	 * backgrounds for your game.
	 * 
	 * @param bkg 
	 */
	public void setStaticBackground(BasicSprite bkg)
	{
		staticBackground = bkg;
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
		//render the static background
		if (staticBackground != null)
			staticBackground.paint(g);
		
		//TODO: render background parallax
		
		//render the sprite list
		for (int i = 0; i < spriteList.size(); i++)
		{
			//if there are no sprites in the list,
			//simply jump to the next one in the masterlist.
			if (spriteList.get(i).size() < 1)
				continue;
			
			for (int j = 0; j < spriteList.get(i).size(); j++)
			{
				BasicSprite s = spriteList.get(i).get(j);
				if (s != null)
				{
					//TODO: add off-screen check
					
					//this is a valid, on-screen sprite
					spriteList.get(i).get(j).paint(g);
				}
			}
		}
		
		//TODO: render foreground parallax
	}
	
	public void updateList(float dT)
	{
		if (staticBackground != null)
			staticBackground.update(dT);
		
		//TODO: update parallax

		//update the sprite list
		for (int i = 0; i < spriteList.size(); i++)
		{
			//if there are no sprites in the list,
			//simply jump to the next one in the masterlist.
			if (spriteList.get(i).size() < 1)
				continue;
			
			for (int j = 0; j < spriteList.get(i).size(); j++)
			{
				BasicSprite s = spriteList.get(i).get(j);
				if (s != null)
				{
					//TODO: add off-screen check
					
					//this is a valid, on-screen sprite
					spriteList.get(i).get(j).update(dT);
				}
			}
		}
	}
}
