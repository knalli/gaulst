package schach.brett;


import java.util.List;

import schach.system.ChessException;


public interface IAlleFiguren {
	public void aufstellen();
	
	/**
	 * Gibt alle Figuren der Partie zurück.
	 * 
	 * @param figuren
	 * @param farben
	 * @return
	 */
	public List<IFigur> gebeFiguren(List<Figurart> figuren, List<Farbe> farben);
	
	/**
	 * Stellt alle Figuren auf dem Brett auf.
	 * 
	 * @throws ChessException
	 */
	public void stelleAlleFigurenAuf() throws ChessException;
}
