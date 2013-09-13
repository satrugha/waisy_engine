package Waisy.core.core;

import java.awt.Graphics;

import Waisy.core.graphics.BasicSprite;
import Waisy.core.graphics.SpriteManager;

/**
 * 
 * @author waisy
 *
 */
public class GameManager 
{
	/**
	 * Instance of the game manager. Only one can exist in a game.
	 */
	protected static GameManager instance = null;
	
	/**
	 * previous system time (in ms) update
	 */
	protected long previoustime = 0;
	
	//TEMPORARY for testing. will be changed into state management
	protected SpriteManager mgr;
	
	/**
	 * Retrieve the instance of the game manager.
	 * @return instance of the game manager
	 */
	public static GameManager getInstance()
	{
		if (instance == null)
			instance = newInstance();
		
		return instance;
	}
	
	/**
	 * Creates a new instance of the game manager. This is 
	 * separate from getInstance so when making a child class,
	 * you only need to overload this function and allow
	 * getInstance to do its singleton logic for you.
	 * Polymorphism will take care of the proper casting
	 * when returning the child class.
	 * @return
	 */
	protected static GameManager newInstance()
	{
		return new GameManager();
	}
	
	
	/**
	 * private constructor
	 */
	private GameManager()
	{
		//initialize any master variables
		
		//TODO: temp
		mgr = new SpriteManager();
	}
	
	/**
	 * render the current set of graphics, depending on the game's state.
	 * Also render any UI elements
	 * @param g Graphics object of the main frame
	 */
	public void render(Graphics g)
	{
		//paint using the current sprite manager
		//depending on the current state active.
		
		//TODO: TEMP
		mgr.renderList(g);
		
		//TODO: render UI
	}
	
	/**
	 * add a sprite to the current state's renderer at the 
	 * specified layer.
	 * @param s sprite to add
	 * @param layer layer. Use sprite manager's constants for 
	 * easy numbering.
	 */
	public void addSpriteToCurrentState(BasicSprite s, int layer)
	{
		//TODO: temp
		mgr.addSprite(s, layer);
	}
	

	/**
	 * Adds the player sprite to the current state.
	 * Using this function adds the players to the front of the
	 * render/update lists automatically on the midground layer.
	 * It is possible to have more than one player sprite on
	 * this list.
	 * @param s player
	 */
	public void addPlayerSpriteToCurrentState(BasicSprite s)
	{
		//TODO: temp
		mgr.addPlayerCharacter(s);
	}
	
	/**
	 * Sets the static background sprite for the current state.
	 * Static doesn't mean unanimated, it simply means it does not
	 * pan/move when the player moves. The static background is 
	 * capable of animating, as the SpriteManager calls its update
	 * function. For a slightly moving background, use a parallax.
	 * @param s background sprite.
	 */
	public void setStaticGameBackgroundOnCurrentState(BasicSprite s)
	{
		//TODO: temp
		mgr.setStaticBackground(s);
	}
	
	/**
	 * update all sprites in the current state
	 */
	public void update()
	{
		//calculate delta time since our last update
		long currtime = System.currentTimeMillis();
		float dT = (float) (currtime - previoustime);
		
		//TODO: TEMP
		mgr.updateList(dT);
		
		//reset previous update values.
		previoustime = currtime;
	}
	
	/**
	 * adjusts the update time (used to calculate delta time)
	 * to the current system time
	 */
	public void setUpdateTime()
	{
		previoustime = System.currentTimeMillis();
	}
}
