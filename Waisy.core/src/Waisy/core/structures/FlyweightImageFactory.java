package Waisy.core.structures;

import java.io.IOException;
import java.util.HashMap;

import Waisy.core.graphics.BasicSprite;

/**
 * A flyweight factory for creating and retrieving images.
 * Flyweight factories help save on memory and I/O time
 * so you don't need to reload an image every time it appears
 * on screen. This is especially useful for tile map generation
 * and spawning repeat enemies.
 * 
 * If you are spawning a boss that shows up once, you don't need
 * to add it to the factory. Only add things you'll see more than 
 * once. You can flush the factory after every level if the assets
 * will not carry over to the next level or there aren't enough
 * similarities to bother keeping all the assets.
 * 
 * This uses a hash map to store data. The key is a string. I recommend
 * using the file name or something similar as an identification so 
 * it's much easier to keep track of what you have loaded.
 * Keys must be unique.
 * 
 * Create a child class to customize.
 * 
 * As this is a factory containing the most basic sprite structure,
 * if the factory does not contain the sprite you require, you'll
 * have to make it and add it yourself, so that you have all the sprites
 * in the proper class. It may be easier to simply preload all the 
 * sprites you need at the start of a level and simply retrieve
 * sprites when they are needed. This is fairly simple with a map-based
 * level but may prove more complicated with larger levels.
 * 
 * @author waisy
 * @see http://en.wikipedia.org/wiki/Flyweight_pattern
 * @see http://docs.oracle.com/javase/7/docs/api/java/util/HashMap.html
 */
public class FlyweightImageFactory 
{
	/**
	 * HashMap to hold all the keys and the sprites.
	 * This map can hold any child class of the basic sprite,
	 * including tiles, enemies, dialogs, and 9-patches.
	 * Please note that a hashmap is not thread safe
	 */
	protected HashMap<String, BasicSprite> map;
	
	/**
	 * Constructor. Initializes an empty map.
	 */
	public FlyweightImageFactory()
	{
		map = new HashMap<>();
	}
	
	/**
	 * Retrieves a sprite within the factory map.
	 * The key must be unique.
	 * @param key String paired with a sprite
	 * @return a BasicSprite if one exists, null if none is found.
	 */
	public BasicSprite getSprite(String key)
	{
		return map.get(key);
	}
	
	/**
	 * Add a sprite to the factory
	 * @param key A unique string to be paired with the sprite.
	 * Keep track of this as it will be needed to retrieve
	 * the loaded sprite in the future!
	 * @param s BasicSprite or child class
	 */
	public void addSprite(String key, BasicSprite s)
	{
		map.put(key, s);
	}
	
	/**
	 * Gets a basic sprite. This function will also
	 * add a basic sprite to the factory if it doesn't exist.
	 * it does all the steps at once.
	 * @param key string key for identification
	 * @param path path to the basic sprite in case the factory
	 * needs to load a new one
	 * @return the matching BasicSprite
	 * @throws IOException Thrown from loading the BasicSprite from file
	 */
	public BasicSprite getBasicSprite(String key, String path)
			throws IOException
	{
		BasicSprite s = map.get(key);
		
		if (s == null) //sprite does not yet exist
		{
			s = new BasicSprite(path);
			map.put(key, s);
			//the constructor can potentially throw the IOException
		}
		
		return s;
	}
	
	/**
	 * flushes all stored sprites and marks them for garbage collection
	 */
	public void flushFactory()
	{
		map.clear();
	}
	
	/**
	 * get the number of sprites currently in the map
	 * @return number of sprites
	 */
	public int getSpriteCount()
	{
		return map.size();
	}
}
