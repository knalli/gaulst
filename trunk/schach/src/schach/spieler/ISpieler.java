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
	 * Gibt zurück, welche Farbe dieser Spieler hat.
	 * @return
	 */
	public Farbe gebeFarbe();
	
	/**
	 * Prüft, ob dieser Spieler zugberechtigt ist.
	 * @return
	 */
	public boolean istZugberechtigt();
	
	public Reihe gebeGrundreihe();
}
