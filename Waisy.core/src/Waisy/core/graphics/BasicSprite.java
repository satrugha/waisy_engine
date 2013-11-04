package Waisy.core.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Basic sprite class used to stick a sprite on screen.
 * This is a single-frame sprite with basic rendering capabilities.
 * 
 * BasicSprite currently uses a BufferedImage, loaded through
 * ImageIO, which has built-in support for GIF, PNG, JPEG,
 *  BMP, and WBMP. 
 *  
 * Currently BasicSprite allows for transparent backgrounds in 
 * the original file. 
 * TODO: setting visible colors to be rendered as transparent.
 * @author waisy
 *
 */
public class BasicSprite extends Object
{
	/**
	 * The image itself. Currently this is a buffered image,
	 * as that has most of the features required for a sprite.
	 */
	protected BufferedImage sprite = null;

	//keep float for location for better calculations of movement.
	protected float x = 0f;
	protected float y = 0f;
	
	protected boolean visible = true;
	
	public BasicSprite()
	{
		sprite = null;
	}
	
	/**
	 * Overloaded constructor. Shortcut.
	 * Base constructor + load sprite function
	 * @param relativeLocation
	 * @throws IOException
	 */
	public BasicSprite(String relativeLocation)
			throws IOException
	{
		sprite = null;
		loadSprite(relativeLocation);
	}
	
	/**
	 * Load a sprite from a location within the main game
	 * @param relativeLocation relative location of the
	 * image file
	 * @throws IOException failed.
	 */
	public void loadSprite(String relativeLocation)
			throws IOException
	{
		if (sprite != null)
			sprite = null; //remove previous sprite ref.
		
		//get the resource stream.
		//input stream allows us to find resources within a
		//different project path.
		InputStream stream = this.getClass().getClassLoader().
                getResourceAsStream(relativeLocation);
		
		//load the image. This'll throw the IOException
		//if something doesn't work right.
		sprite = ImageIO.read(stream);
	    
		//just some debug stuff to let us know
		//it worked right.
	    System.out.println("Image " + relativeLocation
	    		+ "successfully loaded");
	}
	
	public float getX() 		{	return x;	}
	public void setX(float x) 	{	this.x = x; }

	public float getY() 		{ 	return y;	}
	public void setY(float y) 	{	this.y = y;	}
	
	/**
	 * render this sprite. This simply renders the
	 * sprite at x,y. Child classes that need
	 * special rendering should overload this function.
	 * @param g Graphics object.
	 */
	public void paint(Graphics g)
	{
		//TODO: add any sort of displacement?
		//this may be moved elsewhere depending on 
		//how player movement is handled.
		//may involve adding displacement factors.
		//highly tentative.
		
		//draw image requires int coordinates, 
		//so make sure to cast the floats to ints.
		//the null is the ImageObserver, which we can
		//ignore because we have no need to track image loading.
		//(it is primarily used for loading over the internet)
		if (visible)
			g.drawImage(sprite, (int)x, (int)y, null);
	}
	
	
	/**
	 * update the sprite according to time changed since last update.
	 * For the base sprite, this is an empty function. For child classes,
	 * use this function to update anything which requires timing, including
	 * scripted AI, movement, rotations, and animation.
	 * @param dT
	 */
	public void update(float dT)
	{
		
	}
}
