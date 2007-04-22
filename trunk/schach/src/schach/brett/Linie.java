package schach.brett;

/**
 * Eine Linie auf einem Schachbrett (A bis H).
 * @author Jan Philipp
 *
 */
public enum Linie {
	A,B,C,D,E,F,G,H;
	
	public Linie naechste() {
		return ordinal() < 7 ? values()[ordinal()+1] : null;
	}
	
	public Linie vorherige() {
		return ordinal() > 0 ? values()[ordinal()-1] : null;
	}
}
