package schach.brett;


import java.util.List;

import schach.system.ChessException;


public interface IAlleFiguren {
	public void aufstellen();
	
	/**
	 * Gibt festgelegte Figuren der Partie zurück.
	 * 
	 * @param figuren
	 * @param farben
	 * @return
	 */
	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben);
	
	/**
	 * Gibt festgelegte Figuren der Partie zurück.
	 * 
	 * @param figuren
	 * @param farben
	 * @return
	 */
	public List<IFigur> gebeFiguren(Figurart figurart, Farbe farbe);
	
	/**
	 * Stellt alle Figuren auf dem Brett auf.
	 * 
	 * @throws ChessException
	 */
	public void stelleAlleFigurenAuf() throws ChessException;
}
