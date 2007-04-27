package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFigur;
import schach.partie.IStellung;

public class Stellung implements IStellung {
	private List<IFigur> figuren = null;
	private boolean schlagzug = false;
	private IFigur ziehendeFigur = null;
	
	public Stellung(List<IFigur> figuren, boolean istSchlagzug, IFigur ziehendeFigur){
		this.figuren = figuren;
		this.schlagzug = istSchlagzug;
		this.ziehendeFigur = ziehendeFigur;
	}
	
	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben) {
		List<IFigur> figuren2 = new ArrayList<IFigur>();
		
		for(IFigur figur : this.figuren){
			if(farben.contains(figur.gebeFarbe()) && figurarten.contains(figur.gebeArt())){
				figuren2.add(figur);
			}
		}
		
		return figuren2;
	}

	public boolean istSchlagzug() {
		return schlagzug;
	}

	public IFigur ziehendeFigur() {
		return ziehendeFigur;
	}

	public boolean istKoenigBedroht(Farbe farbe) {
		// TODO Auto-generated method stub
		return false;
	}

}
