package schach.brett;
import schach.system.NegativeConditionException;

/**
 * Eine Schachfigur in einer Schachpartie, die geschlagen werden kann.
 * 
 * @author Jan Philipp
 *
 */
public interface ISchlagbareFigur extends IFigur {
	/**
	 * Die Schachfigur wird vom Schachbrett entfernt.
	 * @throws NegativeConditionException
	 */
	public void geschlagenWerden() throws NegativeConditionException;
	
	/**
	 * Gibt zurück, ob die Schachfigur vom Schachbrett entfernt werden soll.
	 * @return
	 */
	public boolean sollEntferntWerden();
	
	public void setzeSollEntferntWerden();
}
