package schach.system;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;

/**
 * Der Schachw�chter steuert alle Aufgaben und pr�ft notwendige Parameterinhalte.
 * 
 * @author knalli
 */
public interface IController {
	public void setzeFigur(IFeld feld);
	public void setzeFigur(Reihe reihe, Linie linie);
	public void setzeZielFeld(IFeld feld);
	public void setzeZielFeld(Reihe reihe, Linie linie);	
	public void setzeZielFeld(IFeld feld, boolean throwexceptions) throws NegativeConditionException;
	public void setzeZielFeld(Reihe reihe, Linie linie, boolean throwexceptions) throws NegativeConditionException;
	public void neueFigur(Figurart art, Farbe farbe);
	public boolean parseInputString(String text);
	public boolean parseInputString(String text, boolean throwexceptions) throws NegativeConditionException;
	public String getMessage();
}
