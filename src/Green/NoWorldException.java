package Green;

import java.io.Serial;

/**
 * Thrown when a method is called that requires a {@link World} to be loaded, and there is no {@link World} loaded. A {@link World} may be loaded by calling {@link Green#loadWorld(World)}.
 * @author Zacchary Dempsey-Plante
 */
public class NoWorldException extends RuntimeException
{
	@Serial
	private static final long serialVersionUID = 1L;
}
