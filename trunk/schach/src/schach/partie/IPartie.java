package schach.partie;

import schach.brett.Farbe;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;

/**
 * Eine Schachpartie mit ihren Zuständen.
 * 
 * @author Jan Philipp
 *
 */
public interface IPartie {
	/**
	 * Enthält den aktuell zugberechtigten Spieler.
	 * @return
	 */
	public ISpieler aktuellerSpieler();
	
	/**
	 * Enthält den aktuell zugberechtigten Spieler.
	 * @return
	 */
	public Farbe aktuelleFarbe();
	
	/**
	 * Enthält den aktuell nicht zugberechtigen Spieler.
	 * @return
	 */
	public ISpieler gegnerischerSpieler();
	
	/**
	 * Der Spieler bietet ein Remis an.
	 * @param spieler
	 * @throws NegativeConditionException
	 */
	public boolean bieteRemisAn(ISpieler spieler) throws NegativeConditionException;
	
	/**
	 * Der Spieler lehnt ein Remis ab.
	 * @param spieler
	 * @throws NegativeConditionException
	 */
	public void lehneRemisAb(ISpieler spieler) throws NegativeConditionException;
	
	/**
	 * Der Spieler nimmt ein Remis an.
	 * @param spieler
	 * @throws NegativeConditionException
	 */
	public void nehmeRemisAn(ISpieler spieler) throws NegativeConditionException;
	
	/**
	 * pr¸ft von welchem Spieler das Remisangebot kommt
	 * @param Spieler
	 * @return
	 * @throws NegativeConditionException
	 */
	public boolean istRemisAngebotVon(ISpieler Spieler) throws NegativeConditionException;
	/**
	 * Startet die Programmlogik (kein Partiebeginn!)
	 *
	 */
	/***
	 * 
	 */

	public boolean istRemisMoeglich() throws NegativeConditionException;
	public void start();
	
	public void wechsleSpieler();
	
	public void setzeFarbe(Farbe farbe);
}
