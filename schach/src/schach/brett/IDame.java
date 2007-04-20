package schach.brett;

import schach.system.NegativeConditionException;

/**
 * Eine Dame in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface IDame extends ISchlagbareFigur {
	/**
	 * Die Dame zieht auf das <code>ziel</code>-Feld und schlägt dabei die gegnerische <code>gegner</code>-Figur.
	 * @param ziel Zielfeld
	 * @param gegner gegnerische Figur
	 * @throws NegativeConditionException
	 */
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner) throws NegativeConditionException;
	
	/**
	 * Die Dame zieht auf das <code>ziel</code>-Feld.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void zieht(IFeld ziel) throws NegativeConditionException;
}
