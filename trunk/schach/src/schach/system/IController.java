package schach.system;

import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;

/**
 * Der Schachwächter steuert alle Aufgaben und prüft notwendige Parameterinhalte.
 * 
 * @author knalli
 */
public interface IController {
	/**
	 * Informiert den Wächter über eine einfache Aktionshandlung auf einem Feld. 
	 * @param feld
	 */
	public void addSingleAction(IFeld feld);
	
	/**
	 * Informiert den Wächter über eine einfache Aktionshandlung auf einem Feld. 
	 * @param reihe
	 * @param linie
	 */
	public void addSingleAction(Reihe reihe, Linie linie);
	
	/**
	 * Informiert den Wächter über eine komplette Aktionshandlung über Start- und Zielfeld.
	 * @param startfeld
	 * @param zielFeld
	 */
	public void addCompleteAction(IFeld startFeld, IFeld zielFeld);
	
	/**
	 * Informiert den Wächter über eine komplette Aktionshandlung über Start- und Zielfeld.
	 * @param startReihe
	 * @param startLinie
	 * @param zielReihe
	 * @param zielLinie
	 */
	public void addCompleteAction(Reihe startReihe, Linie startLinie, Reihe zielReihe, Linie zielLinie);
}
