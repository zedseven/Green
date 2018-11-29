package Green;
public class InputKey
{
	private final char _key;
	private final int _keyCode;
	
	public InputKey(char key, int keyCode)
	{
		_key = key;
		_keyCode = keyCode;
	}
	
	public char getKey()
	{
		return _key;
	}
	public int getKeyCode()
	{
		return _keyCode;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _key;
		result = prime * result + _keyCode;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InputKey other = (InputKey) obj;
		if (_key != other._key)
			return false;
		if (_keyCode != other._keyCode)
			return false;
		return true;
	}
}