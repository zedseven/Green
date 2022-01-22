package Green;

/**
 * A class for storing a key input for (primarily internal) use. It stores both the {@code char} {@code key} and the {@code int} {@code keyCode} of the input.
 * @author Zacchary Dempsey-Plante
 */
public class InputKey
{
	private final char _key;
	private final int _keyCode;
	
	/**
	 * Creates the storage class representing the pressed key.
	 * @param key The {@code char} representation of the pressed key.
	 * @param keyCode The keyCode of the pressed key.
	 */
	public InputKey(char key, int keyCode)
	{
		_key = key;
		_keyCode = keyCode;
	}
	
	/**
	 * Retrieves the {@code char} representation of the key that was pressed (or {@link processing.core.PConstants#CODED} if the key doesn't have one)
	 * @return The {@code char} representation of the key that was pressed (or {@link processing.core.PConstants#CODED} if the key doesn't have one)
	 */
	public char getKey()
	{
		return _key;
	}
	/**
	 * Retrieves the keycode of the key that was pressed. 
	 * @return The keycode of the key that was pressed.
	 * @see <a href="https://docs.oracle.com/javase/6/docs/api/java/awt/event/KeyEvent.html">Java KeyEvent reference</a>
	 */
	public int getKeyCode()
	{
		return _keyCode;
	}
	
	//Object class overrides
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + _key;
		result = prime * result + _keyCode;
		return result;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		InputKey other = (InputKey) obj;
		if(_key != other._key)
			return false;
		return _keyCode == other._keyCode;
	}
	@Override
	public String toString()
	{
		return "InputKey key: '" + _key + "' keyCode: " + _keyCode;
	}
}
