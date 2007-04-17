package schach.brett;
import schach.system.NegativeConditionException;

/**
 * Ein Bauer in einer Schachpartie.
 * 
 * @author Jan Philipp
 *
 */
public interface IBauer extends ISchlagbareFigur {
	/**
	 * Gibt zurück, ob der Bauer in der letzten Runde einen Doppelschritt gemacht hat.
	 * @return
	 */
	public boolean letzteRundeDoppelschritt();
	
	/**
	 * Der Bauer wird vom Schachbrett für eine Umwandlung entnommen.
	 * @throws NegativeConditionException
	 */
	public void entnehmen() throws NegativeConditionException;
	
	/**
	 * Der Bauer zieht auf das <code>ziel</code>-Feld und schlägt dabei die gegnerische <code>gegner</code>-Figur.
	 * @param ziel Zielfeld
	 * @param gegner gegnerische Figur
	 * @throws NegativeConditionException
	 */
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner) throws NegativeConditionException;
	
	/**
	 * Der Bauer zieht auf das <code>ziel</code>-Feld und schlägt dabei einen gegnerischen Bauern en passent.
	 * @param ziel Zielfeld
	 * @throws NegativeConditionException
	 */
	public void schlaegtEnPassant(IFeld ziel) throws NegativeConditionException;
	
	/**
	 * Der Bauer zieht auf das <code>ziel</code>-Feld.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void zieht(IFeld ziel) throws NegativeConditionException;
}
