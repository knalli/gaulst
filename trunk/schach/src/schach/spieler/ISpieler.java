package schach.spieler;

import schach.brett.Farbe;

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
}
