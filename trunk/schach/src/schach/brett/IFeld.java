package schach.brett;

import schach.system.NegativeConditionException;

/**
 * Ein Feld auf einem Schachbrett.
 * @author Jan Philipp
 *
 */
public interface IFeld {
	/**
	 * Gibt das Feld zurück, was <code>n</code> Reihen Richtung Gegner ist.
	 * @param n
	 * @return
	 * @throws NegativeConditionException
	 */
	public IFeld plusReihe(int n) throws NegativeConditionException;
	
	/**
	 * Gibt das Feld zurück, was <code>n</code> Linien nach rechts aus der Sicht des aktuellen Spielers ist.
	 * @param n
	 * @return
	 * @throws NegativeConditionException
	 */
	public IFeld plusLinie(int n) throws NegativeConditionException;
	
	/**
	 * Gibt das Feld zurück, was <code>n</code> Reihen Richtung des aktuellen Spielers ist. 
	 * @param n
	 * @return
	 * @throws NegativeConditionException
	 */
	public IFeld minusReihe(int n) throws NegativeConditionException;
	
	/**
	 * Gibt das Feld zurück, was <code>n</code> Linien nach links aus der Sicht des aktuellen Spielers ist.
	 * @param n
	 * @return
	 * @throws NegativeConditionException
	 */
	public IFeld minusLinie(int n) throws NegativeConditionException;
	
	/**
	 * Gibt die Reihe des Feldes zurück.
	 * @return
	 */
	public Reihe gebeReihe();
	
	/**
	 * Gibt die Linie des Feldes zurück.
	 * @return
	 */
	public Linie gebeLinie();
	
	/**
	 * Gibt die diagonalen Felder zur¸ck auf der sich die Firgur
	 * zur Zeit befindet
	 * 
	 * @return
	 */
	//public Diagonale gebeDiagonale();
	
	/**
	 * Gibt zurück, ob das Feld besetzt ist.
	 * @return
	 */
	public boolean istBesetzt();
	
	/**
	 * Setzt das Feld (un)besetzt.
	 * @param status
	 * @throws NegativeConditionException 
	 */
	public void istBesetzt(boolean status);
	
	public IFeld clone();
}
