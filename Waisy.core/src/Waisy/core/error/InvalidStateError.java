package Waisy.core.error;

public class InvalidStateError extends Error 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1987280138302087755L;

	@Override
	public String getMessage() 
	{
		return "The state you are trying to access is currently marked as" +
				"STATE_INVALID or STATE_ENDED. Initialize it or throw it away.";	
	}

}
