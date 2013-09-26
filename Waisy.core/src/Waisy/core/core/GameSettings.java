package Waisy.core.core;

import java.awt.Color;

import javax.swing.JFrame;

import Waisy.core.structures.FlyweightImageFactory;

/**
 * This is a class of static variables for settings which 
 * can be referenced throughout the game. For custom settings, 
 * overload this class.
 * @author waisy
 *
 */
public class GameSettings 
{
	//screen dimensions. default is 800x600
	public static int SCREEN_WIDTH = 800;
	public static int SCREEN_HEIGHT = 600;
	
	//render settings.
	/*
	 *  desired fps. To fool the eye into believing motion and
	 *  animation, FPS>20. FPS>25 is preferable.
	 */
	public static int FPS = 50;
	/*
	 *  maximum number of frames to be skipped
	 */
	public static int MAX_FRAME_SKIPS = 5;
	
	//color settings
	public static Color COLOR_BACKGROUND = Color.black;
	public static Color COLOR_FONT = Color.black;
	
	//main manager references, for those which are not singletons
	public static FlyweightImageFactory imageFactory = new FlyweightImageFactory();
	//game manager
	public static GameManager gm = null;// = GameManager.getInstance();
	//reference to the main frame of the game
	public static JFrame mainframe = null;
}
