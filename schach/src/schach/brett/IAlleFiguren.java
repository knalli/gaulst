package schach.brett;


import java.util.List;

import schach.system.ChessException;
import schach.system.NegativeConditionException;


public interface IAlleFiguren {
	public void aufstellen();
	
	/**
	 * Gibt festgelegte Figuren der Partie auf dem Schachbrett zurück.
	 * 
	 * @param figuren
	 * @param farben
	 * @return
	 */
	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben);
	
	/**
	 * Gibt festgelegte Figuren der Partie auf dem Schachbrett zurück.
	 * 
	 * @param figur
	 * @param farbe
	 * @return
	 */
	public List<IFigur> gebeFiguren(Figurart figurart, Farbe farbe);
	
	/**
	 * Stellt alle Figuren auf dem Brett auf.
	 * 
	 * @throws ChessException
	 */
	public void stelleAlleFigurenAuf() throws ChessException;

	/**
	 * Entfernt alle Figuren aus der Partie.
	 *
	 */
	public void entferneFiguren() throws NegativeConditionException;

	public void fuegeFigurAn(IFigur figur) throws NegativeConditionException;
}
