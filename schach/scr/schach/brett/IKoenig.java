package schach.brett;

import schach.system.NegativeConditionException;

/**
 * Ein Koenig in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface IKoenig extends IFigur {
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
	
	/**
	 * Gibt zur�ck, ob der K�nig aktuell bedroht ist.
	 * @return
	 */
	public boolean istBedroht();
	
	/**
	 * Gibt zur�ck, ob der K�nig aktuell in einer Rochade ist.
	 * @return
	 */
	public boolean istInEinerRochade();
	
	/**
	 * Der K�nig rochiert.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void rochiert(IFeld ziel) throws NegativeConditionException;
	
	/**
	 * Gibt an, ob der K�nig in dieser Partie bereits einmal bewegt wurde.
	 * @return
	 * @throws NegativeConditionException
	 */
	public boolean wurdeBewegt() throws NegativeConditionException;
}
