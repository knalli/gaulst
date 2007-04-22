package schach.brett;

/**
 * Eine Reihe auf einem Schachbrett (1-8).
 * @author Jan Philipp
 *
 */
public enum Reihe {
	R1,R2,R3,R4,R5,R6,R7,R8;
	
	public Reihe naechste() {
		return ordinal() < 7 ? values()[ordinal()+1] : null;
	}
	
	public Reihe vorherige() {
		return ordinal() > 0 ? values()[ordinal()-1] : null;
	}
	
	public String toString() {
		switch(this){
		case R1: return "1";
		case R2: return "2";
		case R3: return "3";
		case R4: return "4";
		case R5: return "5";
		case R6: return "6";
		case R7: return "7";
		case R8: return "8";
		}
		return "X";
	}
}
