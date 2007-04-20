package schach.brett;

import schach.system.NegativeConditionException;

/**
 * Ein Koenig in einer Schachpartie.
 * @author Jan Philipp
 *
 */
public interface IKoenig extends IFigur {
	/**
	 * Der König zieht auf das <code>ziel</code>-Feld und schlägt dabei die gegnerische <code>gegner</code>-Figur.
	 * @param ziel Zielfeld
	 * @param gegner gegnerische Figur
	 * @throws NegativeConditionException
	 */
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner) throws NegativeConditionException;
	
	/**
	 * Der König zieht auf das <code>ziel</code>-Feld.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void zieht(IFeld ziel) throws NegativeConditionException;
	
	/**
	 * Gibt zurück, ob der König aktuell bedroht ist.
	 * @return
	 */
	public boolean istBedroht();
	
	/**
	 * Gibt zurück, ob der König aktuell in einer Rochade ist.
	 * @return
	 */
	public boolean istInEinerRochade();
	
	/**
	 * Der König rochiert.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void rochiert(IFeld ziel) throws NegativeConditionException;
	
	/**
	 * Gibt an, ob der König in dieser Partie bereits einmal bewegt wurde.
	 * @return
	 * @throws NegativeConditionException
	 */
	public boolean wurdeBewegt() throws NegativeConditionException;
}
