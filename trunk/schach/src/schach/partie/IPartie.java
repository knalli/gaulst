package schach.partie;

import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;

/**
 * Eine Schachpartie mit ihren Zust�nden.
 * 
 * @author Jan Philipp
 *
 */
public interface IPartie {
	/**
	 * Enth�lt den aktuell zugberechtigten Spieler.
	 * @return
	 */
	public ISpieler aktuellerSpieler();
	
	/**
	 * Enth�lt den aktuell nicht zugberechtigen Spieler.
	 * @return
	 */
	public ISpieler gegnerischerSpieler();
	
	/**
	 * Der Spieler bietet ein Remis an.
	 * @param spieler
	 * @throws NegativeConditionException
	 */
	public void bieteRemisAn(ISpieler spieler) throws NegativeConditionException;
	
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
	 * Startet die Programmlogik (kein Partiebeginn!)
	 *
	 */
	public void start();
}
