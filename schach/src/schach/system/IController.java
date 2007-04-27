package schach.system;

import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;

/**
 * Der Schachw�chter steuert alle Aufgaben und pr�ft notwendige Parameterinhalte.
 * 
 * @author knalli
 */
public interface IController {
	/**
	 * Informiert den W�chter �ber eine einfache Aktionshandlung auf einem Feld. 
	 * @param feld
	 */
	public void addSingleAction(IFeld feld);
	
	/**
	 * Informiert den W�chter �ber eine einfache Aktionshandlung auf einem Feld. 
	 * @param reihe
	 * @param linie
	 */
	public void addSingleAction(Reihe reihe, Linie linie);
	
	/**
	 * Informiert den W�chter �ber eine komplette Aktionshandlung �ber Start- und Zielfeld.
	 * @param startfeld
	 * @param zielFeld
	 */
	public void addCompleteAction(IFeld startFeld, IFeld zielFeld);
	
	/**
	 * Informiert den W�chter �ber eine komplette Aktionshandlung �ber Start- und Zielfeld.
	 * @param startReihe
	 * @param startLinie
	 * @param zielReihe
	 * @param zielLinie
	 */
	public void addCompleteAction(Reihe startReihe, Linie startLinie, Reihe zielReihe, Linie zielLinie);
}
