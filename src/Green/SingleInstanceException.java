package Green;

import java.io.Serial;

/**
 * Thrown when more than one instance of {@link Green} is attempted to be created.
 * @author Zacchary Dempsey-Plante
 */
public class SingleInstanceException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 1L;
}
