package schach.brett;

import java.util.Arrays;
import java.util.List;

public enum Figurart {
	BAUER, DAME, KOENIG, LAEUFER, SPRINGER, TURM;
	

	public static List<Figurart> getAll(){
		return Arrays.asList(values());
	}
	
	public String gebeKuerzel() {
		switch(this){
		case DAME: return("D");
		case KOENIG: return("K");
		case LAEUFER: return("L");
		case SPRINGER: return("S");
		case TURM: return("T");
		}
		return "";
	}
}
