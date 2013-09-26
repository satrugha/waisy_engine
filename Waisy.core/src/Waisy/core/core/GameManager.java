package Waisy.core.core;

import java.awt.Graphics;

import Waisy.core.error.GameManagerUninitializedError;
import Waisy.core.graphics.BasicSprite;
import Waisy.core.graphics.SpriteManager;
import Waisy.core.structures.BasicGameState;
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
	
	/**
	 * is the game manager currently paused?
	 */
	protected boolean isPaused = false;
	
	/**
	 * The active state in the game
	 */
	protected BasicGameState currState = null;
	/**
	 * previous state in the game. There is currently only one
	 * previous state however a stack may be added in the future/
	 * child classes
	 */
	protected BasicGameState prevState = null;
	
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
		
		currState = null;
		prevState = null;
	}
	
	/**
	 * Perform any sort of initializations for starting up
	 * the game manager in prep for gameplay. By default,
	 * this function starts up the update clock with the current
	 * time and initializes the passed (or a default) state.
	 * 
	 * After you start the GameManager, don't delay too long before
	 * starting the game loop, calling update and render, else the 
	 * GameManager's update calculations will be thrown off for 
	 * the first update cycle. If the first update isn't important
	 * (e.g. this is a static start screen), you can call update whenever
	 * you want after Start.
	 * @param state BasicGameState to start. leave as null if you 
	 * want to use a generic default state
	 */
	public void Start(BasicGameState state)
	{
		//if the parameter is null OR the current state has not been set
		//send it off to setStartingState to create one.
		if ((state != null) || (currState == null))
			setStartingState(state);
		Start();
	}
	
	/**
	 * If you wish to set up the game state and start the 
	 * game manager at different times, first use setStartingState
	 * then Start() (no parameters). If you want to do both steps as one,
	 * and you plan on using the GameManager right afterwards, then use
	 * Start(state);
	 * 
	 * You would wish to do both steps separately if you plan on setting
	 * up the states but not starting the update process until later.
	 * @param state starting game state. set to null if you wish to start
	 * with a generic BasicGameState.
	 */
	public void setStartingState(BasicGameState state)
	{
		if(currState != null) //safety check
			currState.end();
		if (prevState != null)
			prevState.end();
		
		//add a gamestate
		if (state == null)
		{
			state = new BasicGameState();
		}
		//set state variables
		currState = state;
		prevState = null;
		
		//start the state
		currState.start();
	}
	
	/**
	 * starts the current state and the update clock. Call
	 * setStartingState(state) before this function. If there is
	 * no starting state set, the GameManager will create and use
	 * a generic BasicGameState.
	 */
	public void Start()
	{
		//always ensure a state is present.
		if (currState == null)
		{
			currState = new BasicGameState();
			currState.start();
		}
		
		//if the state isn't started, make sure to start it up.
		//if you start updating with an unstarted state, the game
		//will throw errors all over the place.
		if (!currState.isStateValid())
			currState.start();
		
		//correct the update time variable to ensure that 
		//deltaTime used with the update function is correctly set.
		setUpdateTime();
	}
	
	/**
	 * retrieve the current game state. 
	 * 
	 * WARNING: Ending a state through anything
	 * other than a state manager or the GameManager can 
	 * yield unpredictable results.
	 * @return The current state or null if one has not been set
	 */
	public BasicGameState getCurrentGameState()
	{
		return currState;
	}
	
	//------------ pause & resume
	
	/**
	 * Pauses the entire game. When paused, the game will not update
	 * but it is capable of rendering. Only use this function to pause
	 * EVERYTHING, not for a "game pause." Prime example for using this
	 * is when the user clicks away from the window and you want to 
	 * pause updates to free up memory for whatever else the user 
	 * is doing.
	 * 
	 * for game pausing, use pauseCurrentState();
	 */
	public void Pause()
	{
		isPaused = true;
		currState.pause();
	}
	
	/**
	 * Resume the game from a paused state. Use this for unpausing
	 * EVERYTHING. This also corrects the update time. for unpausing a
	 * game pause, use resumeCurrentState();
	 */
	public void Resume()
	{
		isPaused=false;
		currState.resume();
		setUpdateTime();
	}
	
	/**
	 * pauses the current and active state. A paused state will not
	 * update but it will render. This is equivalent of a game pause.
	 */
	public void pauseCurrentState()
	{
		currState.pause();
	}
	
	/**
	 * resume normal operations of the current state.
	 */
	public void resumeCurrentState()
	{
		currState.resume();
	}
	
	//-------------- updating
	
	/**
	 * update all sprites in the current state.
	 * @throws error GameManagerUninitializedError when there
	 * is no current state. This occurs when a GameManager is updated
	 * before starting.
	 */
	public void update()
	{
		if (isPaused)
			return;
		
		//calculate delta time since our last update
		long currtime = System.currentTimeMillis();
		float dT = (float) (currtime - previoustime);

		if (currState == null)
			throw new GameManagerUninitializedError();
		
		//all clear
		currState.update(dT);
		
		//reset previous update values.
		previoustime = currtime;
	}
	
	/**
	 * adjusts the update time (used to calculate delta time)
	 * to the current system time
	 */
	protected void setUpdateTime()
	{
		previoustime = System.currentTimeMillis();
	}
	
	/**
	 * render the current set of graphics, depending on the game's state.
	 * Also render any UI elements
	 * @param g Graphics object of the main frame
	 * @throws error GameManagerUninitializedError when there
	 * is no current state. This occurs when a GameManager is rendered
	 * before starting.
	 */
	public void render(Graphics g)
	{
		//paint using the current sprite manager
		//depending on the current state active.
		
		if (currState == null)
			throw new GameManagerUninitializedError();
		
		//all clear
		currState.render(g);
		
		//TODO: render UI
	}
	
	//----------- state handling
	/**
	 * Change the current state. If you choose to keep the old (current) state,
	 * the GameManager will hold on to it and pause it, but keep in mind,
	 * the GM can only hold one previous state. This is to prevent the game
	 * from becoming too large in memory.
	 * 
	 * For example, you're running through world 3-3 (state33) and you
	 * jump into a pipe. Now suddenly you're in pipe 3-3-a (state33a).
	 * You want to keep state33 because you'll be leaving the pipe and back
	 * to world 3-3, so keepOldState = true;
	 * 
	 * Once you leave the pipe (state33a), there's no reason to keep it around
	 * as the chances of you going back into pipe 3-3-a are slim to none, and
	 * the state is probably fast/easy to load. So keepOldState = false;
	 * 
	 * For more complex world mapping, such as in the case of Super Princess
	 * Peach and RPGs with sections, consider a world map system which maps
	 * how each section connects to one another and preloads sections nearby
	 * on a separate load thread. (I will probably write this later).
	 * @param newstate a new, uninitialized state. State can be pre-loaded.
	 * @param keepOldState keep hold the current state?
	 */
	public void changeState(BasicGameState newstate, boolean keepOldState)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();
		
		//handle the backup of the current state
		if (keepOldState)
		{
			currState.pause();
			
			//clear out any previously held states
			if (prevState != null)
			{
				prevState.end();
				prevState = null;
			}
			
			//keep current currState as previous
			prevState = currState;
		}
		
		//start up new state
		currState = newstate;
		currState.start();
	}
	
	/**
	 * 
	 * @return true if the reversion was a success. False if it wasn't.
	 * The only reason to fail is if the game manager isn't in the proper
	 * active state or a previous state does not exist.
	 */
	public boolean revertToPreviousState()
	{
		if (currState == null)
			throw new GameManagerUninitializedError();
		
		if (prevState != null)
		{
			//stop current state and dispose
			currState.end();
			currState = null;
			
			//restore previous state
			currState = prevState;
			currState.resume();
			
			//clear out prev state since it's now the current state
			prevState = null;
			
			return true; //we successfully reverted
		}
		
		return false; //the reversion failed.
	}
	
	/**
	 * Check to see if the game has a previous state stored
	 * @return true if a previous state is present.
	 */
	public boolean hasPreviousState()
	{
		return prevState != null;
	}
	
	
	//------- sprite handling
	
	/**
	 * add a sprite to the current state's renderer at the 
	 * specified layer.
	 * @param s sprite to add
	 * @param layer layer. Use sprite manager's constants for 
	 * easy numbering.
	 * @throws error GameManagerUninitializedError when there
	 * is no current state.
	 */
	public void addSpriteToCurrentState(BasicSprite s, int layer)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, layer);
	}
	
	/**
	 *  Adds the sprite to the background render list.
	 * In general, background sprites are just background.
	 * for interactive enviroments, it is recommended to utilize
	 * the midgroundEnvironment list.
	 * @param s background sprite
	 * @throws error GameManagerUninitializedError when there
	 * is no current state.
	 */
	public void addBackgroundSprite(BasicSprite s)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, SpriteManager.BACKGROUND);
	}
	

	/**
	 * Adds the player sprite to the current state.
	 * Player sprites are automatically added to the
	 * CHARACTER layer within the sprite manager.
	 * @param s player
	 * @throws error GameManagerUninitializedError when there
	 * is no current state.
	 */
	public void addPlayerSprite(BasicSprite s)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, SpriteManager.CHARACTERS);
	}
	
	/**
	 * Add a non-enemy character to the render list.
	 * Generally this is for npcs. This makes checking for
	 * conversations with nearby npcs a bit easier than
	 * checking through a master list of all characters on
	 * the screen. Same goes for collision detection and other
	 * miscellaneous non-enemy character actions.
	 * @param s npc or non-enemy character to render
	 * @throws error GameManagerUninitializedError when there
	 * is no current state.
	 */
	public void addNonEnemyCharacterSprite(BasicSprite s)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, SpriteManager.CHARACTERS);
		
	}

	/**
	 * Adds the sprite to the midground environment list.
	 * It is recommended to put all interactive environment 
	 * objects on this list.
	 * Convenience wrapper for the addSprite function
	 * @param s sprite to add to midground layer
	 * @throws error GameManagerUninitializedError when there
	 * is no current state.
	 */
	public void addSpriteToMidgroundEnvironment(BasicSprite s)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, SpriteManager.MIDGROUND_ENVIRONMENT);
	}
	
	/**
	 * Adds the sprite to the foreground render list.
	 * In general, foreground sprites are just things that sit at the front.
	 * for interactive enviroments, it is recommended to utilize
	 * the midgroundEnvironment list.
	 * Convenience wrapper for the addSprite function
	 * @param s sprite to add to the foreground layer
	 */
	public void addSpriteToForeground(BasicSprite s)
	{
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.addSprite(s, SpriteManager.FOREGROUND);
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
		if (currState == null)
			throw new GameManagerUninitializedError();

		//all clear
		currState.setStaticGameBackground(s);
	}
}
