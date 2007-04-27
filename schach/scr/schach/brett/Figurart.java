package schach.brett;

import java.util.Arrays;
import java.util.List;

public enum Figurart {
	BAUER, DAME, KOENIG, LAEUFER, SPRINGER, TURM;
	

	public static List<Figurart> getAll(){
		return Arrays.asList(values());
	}
}
