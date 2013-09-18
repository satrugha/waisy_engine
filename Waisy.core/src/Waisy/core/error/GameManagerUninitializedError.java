package Waisy.core.error;

public class GameManagerUninitializedError extends Error 
{
	private static final long serialVersionUID = 5244542002833467572L;

	@Override
	public String getMessage() 
	{
		return "The GameManager currently does not have a current state! " +
				"You must call Start() on the GameManager before use!";
	}

}
