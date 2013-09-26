package Waisy.core.core;


import javax.swing.JFrame;

/**
 * A basic desktop window frame (JFrame)
 * @author waisy
 *
 */
public class DesktopGameFrame extends JFrame 
{

	/**
	 * Generated serialized ID
	 */
	private static final long serialVersionUID = -7844150565141844L;

	/**
	 * initialize a frame with the width and height set in GameSettings.
	 * sets the frame visible once its done. This also registers the 
	 * frame with the GameSettings.
	 */
	public void initialize()
	{
		initialize(GameSettings.SCREEN_WIDTH, GameSettings.SCREEN_HEIGHT);
	}
	
	/**
	 * Initialize a frame with specified width and height, which will 
	 * be stored in GameSettings for future use. This sets the frame
	 * visible once its done. This also registers the frame with the
	 * GameSettings.
	 * @param w width of the frame
	 * @param h height of the frame
	 */
	public void initialize(int w, int h)
	{
		setSize(w,h);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		//store parameters in GameSettings
		GameSettings.SCREEN_WIDTH = w;
		GameSettings.SCREEN_HEIGHT = h;
		
		//store the frame in the game settings
		GameSettings.mainframe = this;
	}
}
