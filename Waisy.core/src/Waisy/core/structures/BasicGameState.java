package Waisy.core.structures;

import java.awt.Graphics;

import Waisy.core.error.InvalidStateError;
import Waisy.core.graphics.BasicSprite;
import Waisy.core.graphics.SpriteManager;

/**
 * A basic game state utilized by the GameManager (the state manager).
 * 
 * Each state has its own status and can be paused and resumed when needed.
 * 
 * Each state also has its own sprite manager, to make swapping between
 * states for rendering much faster and easier, reducing the load/wait times.
 * If multiple states require many sprites (causing the game to grow too
 * large in memory), consider combining states or destroying states on a 
 * more regular basis.
 * @author waisy
 *
 */
public class BasicGameState 
{
	/**
	 * Each state has its own private sprite manager.
	 */
	protected SpriteManager spriteManager = null;
	
	/**
	 * the current state of the state.
	 */
	protected int state;
	
	/**
	 * indicates this state is currently unusable/uninitialized
	 */
	public static int STATE_INVALID = -1;
	/**
	 * indicates this state is actively rendering and updating
	 */
	public static int STATE_ACTIVE = 0;
	/**
	 * indicates the state is paused. Drawing may occur but
	 * updating will not.
	 */
	public static int STATE_PAUSED = 1;
	/**
	 * indicates the state is no longer needed
	 */
	public static int STATE_ENDED = 2;
	
	
	/**
	 * create a basic game state
	 */
	public BasicGameState()
	{
		state = STATE_INVALID;
	}
	
	/**
	 * checks to see if the state is in a valid state
	 * @return true if the state is NOT invalid or ended
	 */
	public boolean isStateValid()
	{
		return ((state != STATE_INVALID) && (state != STATE_ENDED));
	}
	
	//----------------- state management
	
	/**
	 * initializes the state and makes it active.
	 * This also creates the sprite manager.
	 */
	public void start()
	{
		//allow ended states to be restarted in case there is a
		//state recycler created.
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
		{
			spriteManager = new SpriteManager();
			state = STATE_ACTIVE;
		}
	}
	
	
	/**
	 * Pause the state. This only pauses update. If you wish,
	 * you can continue calling render. Animations will not update,
	 * sprites will not move. Physics will not be updated.
	 * @throws InvalidStateError throws if the state hasn't been initialized.
	 * initialize the state with Start();
	 */
	public void pause()
	{
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
			throw new InvalidStateError();
		
		state = STATE_PAUSED;
	}
	
	
	/**
	 * Resume a paused state.
	 * @throws InvalidStateError throws if the state hasn't been initialized.
	 * initialize the state with Start();
	 */
	public void resume()
	{
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
			throw new InvalidStateError();
		
		state = STATE_ACTIVE;
	}
	
	
	/**
	 * ends the state and prepares it for deletion
	 */
	public void end()
	{
		state = STATE_ENDED;
		
		//TODO: clear out sprite manager
		
		//remove reference to mark it for garbage collection.
		spriteManager = null;
	}
	
	//-------------- render
	/**
	 * Renders the current spriteManager. State will render even if
	 * state is currently paused.
	 * @param g
	 * @throws InvalidStateError throws if the state hasn't been initialized.
	 * initialize the state with Start();
	 */
	public void render(Graphics g)
	{
		//this state isn't valid
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
			throw new InvalidStateError();
	
		//all clear
		if (spriteManager != null) //should always be valid with a valid state
			spriteManager.renderList(g);
	}
	
	//------------------ update
	/**
	 * update the current state. The state will not update if 
	 * it is currently paused.
	 * @param dT deltaTime calculated by the game manager
	 * @throws InvalidStateError throws if the state hasn't been initialized.
	 * initialize the state with Start();
	 */
	public void update(float dT)
	{
		//this state isn't valid
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
			throw new InvalidStateError();
		
		//do not update if the state is paused
		if (state == STATE_PAUSED)
			return; //just return. don't throw any errors.

		//all clear
		if (spriteManager != null) //should always be valid with a valid state
			spriteManager.updateList(dT);
	}
	
	
	//-------- sprite handling
	/**
	 * add a sprite to the state's sprite manager. this is a basic function
	 * and requires knowing which layer goes to what. Use the GameManager's
	 * wrapper functions to make adding sprites easier.
	 * @param s sprite to add
	 * @param layer layer to add the sprite. Use the SpriteManager's
	 * final ints in this variable.
	 */
	public void addSprite(BasicSprite s, int layer)
	{

		//this state isn't valid
		if ((state == STATE_INVALID) || (state == STATE_ENDED))
			throw new InvalidStateError();

		//all clear
		if (spriteManager != null) //should always be valid with a valid state
			spriteManager.addSprite(s, layer);
	}
	
	/**
	 * Sets the static background sprite for the current state.
	 * Static doesn't mean unanimated, it simply means it does not
	 * pan/move when the player moves. The static background is 
	 * capable of animating, as the SpriteManager calls its update
	 * function. For a slightly moving background, use a parallax.
	 * @param s Sprite to render as the background. Set this to
	 * null to remove the static background.
	 */
	public void setStaticGameBackground(BasicSprite s)
	{
		spriteManager.setStaticBackground(s);
	}
	
	//---------------------- load & save
	
	public void loadStateFromXML(String xml)
	{
		//TODO:loading from xml
	}
	
	public void loadStateFromSave()
	{
		
	}
	
	public void saveState()
	{
		//TODO: save state. finish when creating save functionality
	}
}
