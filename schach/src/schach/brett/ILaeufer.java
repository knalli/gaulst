package schach.brett;

import schach.system.NegativeConditionException;

/**
 * Ein L�ufer in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface ILaeufer extends ISchlagbareFigur {
	/**
	 * Der K�nig zieht auf das <code>ziel</code>-Feld und schl�gt dabei die gegnerische <code>gegner</code>-Figur.
	 * @param ziel Zielfeld
	 * @param gegner gegnerische Figur
	 * @throws NegativeConditionException
	 */
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner) throws NegativeConditionException;
	
	/**
	 * Der K�nig zieht auf das <code>ziel</code>-Feld.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void zieht(IFeld ziel) throws NegativeConditionException;
}
