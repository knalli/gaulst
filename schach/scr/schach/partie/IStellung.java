package schach.partie;


import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFigur;

public interface IStellung {
	public boolean istSchlagzug();
	
	public IFigur ziehendeFigur();
	
	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben);
}
