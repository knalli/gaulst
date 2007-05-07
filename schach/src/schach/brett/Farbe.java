package schach.brett;

import java.util.Arrays;
import java.util.List;

/**
 * Eine Schachfarbe.
 * @author Jan Philipp
 *
 */
public enum Farbe {
	WEISS, SCHWARZ;
	

	public static List<Farbe> getAll(){
		return Arrays.asList(values());
	}
	
	public Farbe andereFarbe() {
		if(this.equals(WEISS))
			return SCHWARZ;
		return WEISS;
	}
}
