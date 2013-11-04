/**
 * 
 */
package Waisy.core.graphics;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An extension of a basic, single-frame sprite, BasicAnimatedSprite is a single-animation
 * class which uses a sprite sheet to animate an image. Sprite sheets can be one or more rows
 * of animations but can only contain one animation of sprites all the same size to properly
 * work.
 * 
 * The animation cuts out each frame and uses that to display the current frame.
 * 
 * @author waisy
 *
 */
public class BasicAnimatedSprite extends BasicSprite {

	//animation variables
	/**
	 * number of frames in this animation
	 */
	protected int framecount = 0;
	/**
	 * Current frame the animation is on
	 */
	protected int currFrame = 0;
	protected BufferedImage currFrameSprite = null;
	/**
	 * How many miliseconds per frame (constant rate). Done by time
	 * to keep the animations consistent even if the game starts lagging
	 * a bit. (The game loop will try to keep a constant update)
	 */
	protected int msPerFrame = 0;
	/**
	 * miliseconds passed since the last frame change.
	 * It is automatically updated during the update cycle.
	 * warning: It is advised not to manipulate this variable outside
	 * the update cycle.
	 */
	protected int msCount = 0;
	/**
	 * how many rows are on the sprite sheet. Typically
	 * there will be 1 row and framecount # of columns.
	 */
	protected int rows = 1;
	/**
	 * how many columns (individual images are in a row).
	 */
	protected int cols = 1;
	
	//calculated animation components
	/**
	 * calculated width of each frame (constant width across all frames).
	 * It is calculated by the width of the sprite
	 * sheet and how many columns in one row.
	 * Can also be preset before the first update
	 */
	protected int framewidth = 0;
	/**
	 * Calculated height of each frame (constant height across all frames).
	 * It is calculated by the height of the sprite and how many rows in the set.
	 * Can also be preset before the first update
	 */
	protected int frameheight = 0;
	
	//animation modifiers
	/**
	 * pause the animation.
	 */
	protected boolean paused = false;
	/**
	 * reverse the animation, going backwards from finish to start
	 */
	protected boolean reverse = false;
	/**
	 * is this a repeating (true) or a one-time (false) animation?
	 */
	protected boolean repeat = true;
	/**
	 * Has the animation completed? Only turns true once an animation is either 
	 * stopped or hits the last frame on a one-time animation.
	 */
	protected boolean stopped = false;
	
	/**
	 * 
	 */
	public BasicAnimatedSprite() 
	{
		super();
	}
	
	/**
	 * Load a single-rowed animation.
	 * @param imglocation location of the spritemap to be loaded
	 * @param frameCount number of frames the animation has
	 * @param milisecondsPerFrame how many miliseconds for each frame's duration
	 * (constant rate)
	 * @throws IOException if there is trouble loading the image
	 */
	public BasicAnimatedSprite(String imglocation, int frameCount, int milisecondsPerFrame)
				throws IOException
	{

		super();
		init(imglocation, frameCount, milisecondsPerFrame, 1, framecount);
	}

	/**
	 * load a multi-row animation sheet
	 * @param imglocation location of the spritemap to be loaded
	 * @param frameCount number of frames the animation has. If you want the function to 
	 * calculate this for you, set this to 0. If you have a different number of 
	 * sprites in this animation than rows x cols, set the number here so you don't
	 * end up with blank frames.
	 * @param milisecondsPerFrame how many miliseconds for each frame's duration
	 * (constant rate)
	 * @param rowcount how many rows of sprites on the sheet (horizontal rows)
	 * @param colcount how many columns of sprites on this sheet (vertical columns)
	 * @throws IOException
	 */
	public BasicAnimatedSprite(String imglocation, int frameCount, int milisecondsPerFrame,
			int rowcount, int colcount)
				throws IOException
	{
		super();
		init(imglocation, frameCount, milisecondsPerFrame, rowcount, colcount);
	}
	
	/**
	 * initializes an animation sprite sheet, loading it and setting all the proper variables.
	 * If you use an overloaded constructor (not the default), then you do not need to call
	 * this function. the constructor will handle this for you and the animation will be ready
	 * for use on the next update cycle.
	 * @param imglocation location of the spritemap to be loaded
	 * @param frameCount number of frames the animation has. If you want the function to 
	 * calculate this for you, set this to 0. If you have a different number of 
	 * sprites in this animation than rows x cols, set the number here so you don't
	 * end up with blank frames.
	 * @param milisecondsPerFrame how many miliseconds for each frame's duration
	 * (constant rate)
	 * @param rowcount how many rows of sprites on the sheet (horizontal rows)
	 * @param colcount how many columns of sprites on this sheet (vertical columns)
	 * @throws IOException
	 */
	public void init(String imglocation, int frameCount, int milisecondsPerFrame,
			int rowcount, int colcount)
					throws IOException
	{
		//load the image file. this might take some time, so we'll do our calculations later.
		sprite = null;
		loadSprite(imglocation); //if there's a problem loading, this throws the IOException
		
		//set the known information
		if ((frameCount == 0) && (colcount != 0))
		{
			framecount = rowcount * colcount;
		}
		rows= rowcount;
		cols = colcount;
		msPerFrame = milisecondsPerFrame;
		
		//the frame width/height will be calculated on first update.
	}
	
	//------------- getters
	public int getFrameCount() 		{ return framecount; }
	public boolean hasStopped()		{ return stopped; }
	public boolean isPaused()		{ return paused; }
	public boolean isRepeating()	{ return repeat; }
	public boolean isReversed()		{ return reverse; }

	
	
	//----------- update
	
	/**
	 * Update the animation. The base update for an animated sprite simply advances
	 * the frame according to modifiers (reverse, paused, etc) set.
	 */
	@Override
	public void update(float dT) 
	{
		
	}
	/**
	 * Calculates the row and column of where to find the individual sprite
	 * for the current frame on a large sprite sheet, with 0,0 being the first
	 * row,col of any sprite sheet. If you only have one row, you can skip
	 * this calculation and go straight to getCurrentFrameSprite using row=0;
	 * @param framenum current frame number in the animation
	 * @return a point of (rownum, colnum) for use with getCurrentFrameSprite
	 */
	protected Point getFrameRowCol(int framenum)
	{
		Point p = new Point(0,0);
		if (rows == 1) //there's only one row in the sprite sheet
		{
			p.x = 0; //when cutting up the sprite sheet, the first row is labeled 0
			p.y = framenum;
		}
		else //calculate the row and col numbers
		{
			
		}
		
		return p;
	}
	
	/**
	 * Stores the single frame of animation from the sprite sheet for rendering.
	 * This requires the location on the sprite sheet (located like coordinates in a grid).
	 * This sets the variable currFrameSprite. Wrapper function for
	 * setCurrentFrameSprite(row,col);
	 * @param p point in the form of (row,col), usually formed by getFrameRowCol()
	 */
	protected void setCurrentFrameSprite(Point p)
	{
		setCurrentFrameSprite(p.x, p.y);
	}

	/**
	 * Stores the single frame of animation from the sprite sheet for rendering.
	 * This requires the location on the sprite sheet (located like coordinates in a grid).
	 * If you only have one row, this is an easy calculation like row=0, col=frame#
	 * This sets the variable currFrameSprite
	 * @param row
	 * @param col
	 */
	protected void setCurrentFrameSprite(int row, int col)
	{
		//double check that we have the frame size set before proceeding.
		if ((framewidth == 0) || (frameheight == 0))
			calculateFrameWH();
		
		if (sprite != null) //if we have the sprite sheet loaded
		{
			//sub image ( 							x, 				y, 					width, 		height)
			currFrameSprite =  sprite.getSubimage(col * framewidth, row * frameheight,framewidth, frameheight);
		}
		else
		{
			currFrameSprite = null;
		}
	}
	
	protected void calculateFrameWH()
	{
		//if the sprite still isn't loaded or failed loading,
		//keep the width/height = 0 to trigger the WH calculation on 
		//the next update. For the most part, this shouldn't
		//be a true statement.
		if (sprite == null)
		{
			framewidth = 0;
			frameheight = 0;
		}
		else
		{
			//we have a properly loaded sprite.
			//use the row and col count to calculate the w/h.
			//rows and cols should always be >=1
			//default is rows=1, cols=1
			frameheight = sprite.getHeight()/rows;
			framewidth = sprite.getWidth()/cols;
			
		}
	}
}
