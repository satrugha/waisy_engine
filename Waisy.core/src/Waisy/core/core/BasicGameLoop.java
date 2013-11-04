package Waisy.core.core;

import java.awt.Graphics;

/**
 * A basic game loop. This loop is a separate thread
 * so create it then call run(). This loop relies on
 * the desktop window frame's graphics object and on 
 * the game manager. You can either set up each manually
 * before calling this class, or use the constructors in 
 * this class to set them all in one step.
 * 
 * A graphics object comes from the game window after it
 * is set to visible. Your window must be a JFrame.
 * jFrameObject.setVisible(true);
 * jFrameObject.getGraphics(); <- send this graphics object
 * to the BasicGameLoop.
 * 
 * Also note, there should only be one game loop. The loop relies
 * on a GameManager to update and render. The game manager is set
 * in the GameSettings as GameSettings.gm.
 * 
 * This game loop is set to maintain a constant frame rate. If the
 * loop starts falling behind, the game will do just updates for
 * up to allowed frames (GameSettings.MAX_FRAME_SKIPS) 
 * before returning to the usual render/update cycle.
 * 
 * This game loop relies on GameSettings.MAX_FRAME_SKIPS and 
 * GameSettings.FPS. Set these before running the loop. Setting them
 * after running the loop can have unpredictable results.
 * @author waisy
 *
 */
public class BasicGameLoop implements Runnable 
{	
	private boolean running = true;
	
	private Graphics g;
	
	/**
	 * Create a game loop using GameManager and JFrame
	 * set in GameSettings. if GameSettings.mainframe is null,
	 * this loop will create and initialize a DesktopGameFrame
	 * using the width and height specified in GameSettings.
	 */
	public BasicGameLoop()
	{
		if (GameSettings.mainframe == null)
		{
			DesktopGameFrame dgf = new DesktopGameFrame();
			dgf.initialize();
			GameSettings.mainframe = dgf;
		}
		Graphics gr = GameSettings.mainframe.getGraphics();
		init(gr, null);
	}
	
	/**
	 * Creates a game loop with the GameManager set in 
	 * GameSettings.gm. Creates a basic GameManager
	 * if one is not set in GameSettings.
	 * @param gr graphics object (the canvas) 
	 */
	public BasicGameLoop(Graphics gr)
	{
		init(gr, null);
	}
	
	/**
	 * Creates a game manager with the specified parameters
	 * @param gr Graphics object from the main game window frame
	 * @param gm A Game manager (GameManager or child class). If
	 * this is set to null, the constructor will create a GameManager
	 * for you. This also stores the GameManager in 
	 * GameSettings.gm.
	 */
	public BasicGameLoop(Graphics gr, GameManager gm)
	{
			init(gr,gm);
	}
	
	/**
	 * initialize loop parameters. Called from the constructors
	 * @param gr Graphics object
	 * @param gm the GameManager to use. This sets GameSettings.gm.
	 * if set to null, use the GameManager which is already set
	 * in the GameSettings. If a GameManager does not yet exist,
	 * this function will automatically generate a GameManager object
	 * of class Waisy.core.core.GameManager. 
	 */
	protected void init(Graphics gr, GameManager gm)
	{
		//store the graphics object for rendering.
		g = gr;
		
		//set the GameManager. if null, the GameManager
		//has probably been set by another constructor
		if (gm != null)
			GameSettings.gm = gm;
		
		//but let's double check anyway to make sure we
		//have a valid GameManager
		if (GameSettings.gm == null)
			GameSettings.gm = GameManager.getInstance();
	}

	@Override
	public void run()
	{
		int    FRAME_PERIOD = 1000 / GameSettings.FPS;
		
		long beginTime;     // the time when the cycle begun
		long timeDiff;      // the time it took for the cycle to execute
		int sleepTime;      // ms to sleep (<0 if we're behind)
		int framesSkipped;  // number of frames being skipped
		
		//start the game manager's clock
		//GameManager.getInstance().setUpdateTime();
		GameManager.getInstance().Start(null);

		sleepTime = 0;

		while (running) 
		{
			beginTime = System.currentTimeMillis();
			
			framesSkipped = 0;	// resetting the frames skipped
			// update game state 
			GameManager.getInstance().render(g);
			g.dispose();
			// render state to the screen
			// draws the canvas on the panel
			GameManager.getInstance().update();				
			// calculate how long did the cycle take
			timeDiff = System.currentTimeMillis() - beginTime;
			// calculate sleep time
			sleepTime = (int)(FRAME_PERIOD - timeDiff);

			if (sleepTime > 0) 
			{
				// if sleepTime > 0 we're OK
				try 
				{
					// send the thread to sleep for a short period
					Thread.sleep(sleepTime);	
				} 
				catch (InterruptedException e) {}
			}

			while (sleepTime < 0 && framesSkipped < GameSettings.MAX_FRAME_SKIPS) 
			{
				// we need to catch up
				// update without rendering
				GameManager.getInstance().update();
				// add frame period to check if in next frame
				sleepTime += FRAME_PERIOD;	
				framesSkipped++;
			}
		}
	}

}
