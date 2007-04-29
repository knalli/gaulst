package schach.spieler;

import schach.brett.Farbe;
import schach.brett.Reihe;

/**
 * Der Schachspieler in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface ISpieler {
	/**
	 * Gibt zur�ck, welche Farbe dieser Spieler hat.
	 * @return
	 */
	public Farbe gebeFarbe();
	
	/**
	 * Pr�ft, ob dieser Spieler zugberechtigt ist.
	 * @return
	 */
	public boolean istZugberechtigt();
	
	public Reihe gebeGrundreihe();
}
