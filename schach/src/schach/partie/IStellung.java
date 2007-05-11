package schach.partie;


import schach.brett.Farbe;
import schach.brett.Figurart;

public interface IStellung {
	public boolean istSchlagzug();
	
	public Figurart ziehendeFigur();
	
//	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben);
	
//	public List<IFigur> gebeFiguren(Figurart figurarte, Farbe farbe);
	
	public boolean istKoenigBedroht(Farbe farbe);
}
