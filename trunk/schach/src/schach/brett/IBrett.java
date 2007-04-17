package schach.brett;


import java.util.List;

import schach.system.NegativeConditionException;

/**
 * Das Schachbrett in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface IBrett {

	/**
	 * Gibt die Schachfigur zur�ck, die auf dem angegebenen <code>feld</code> steht.
	 * @param feld
	 * @return
	 */
	public IFigur gebeFigurVonFeld(IFeld feld);
	
	/**
	 * Pr�ft, ob aktuell eine Bauernumwandlung bevorsteht.
	 * @return
	 */
	public boolean istBauernUmwandlung();
	
	/**
	 * Pr�ft, ob die angebenen Felder unbesetzt sind. 
	 * @return
	 */
	public boolean sindAlleFelderFrei(List<IFeld> felder);
	
	/**
	 * Gibt die Felder zwischen 2 Feldern in einer Reihe zur�ck.
	 * @param start Startfeld
	 * @param ende Zielfeld
	 * @return
	 * @throws NegativeConditionException
	 */
	public List<IFeld> gebeFelderInReihe(IFeld start, IFeld ende) throws NegativeConditionException;
	
	/**
	 * Gibt die Felder zwischen 2 Feldern in einer Linie zur�ck.
	 * @param start Startfeld
	 * @param ende Zielfeld
	 * @return
	 * @throws NegativeConditionException
	 */
	public List<IFeld> gebeFelderInLinie(IFeld start, IFeld ende) throws NegativeConditionException;

	/**
	 * Gibt die Felder zwischen 2 Feldern in einer Diagonalen zur�ck.
	 * @param start Startfeld
	 * @param ende Zielfeld
	 * @return
	 * @throws NegativeConditionException
	 */
	public List<IFeld> gebeFelderInDiagonalen(IFeld start, IFeld ende) throws NegativeConditionException;

	/**
	 * R�umt alle Schachfiguren vom Schachbrett ab.
	 * @throws NegativeConditionException
	 */
	public void raeumeAb() throws NegativeConditionException;
	
	/**
	 * Wandelt den Bauern in eine neue Figur um (Bauerndumwandlung).
	 * @param bauer
	 * @param figur
	 * @throws NegativeConditionException
	 */
	public void wandleBauernUm(IBauer bauer, IFigur figur) throws NegativeConditionException;
	
	/**
	 * Gibt das Feld zur�ck.
	 * 
	 * @param reihe
	 * @param linie
	 * @return
	 * @throws NegativeConditionException
	 */
	public IFeld gebeFeld(Reihe reihe, Linie linie) throws NegativeConditionException;
}
