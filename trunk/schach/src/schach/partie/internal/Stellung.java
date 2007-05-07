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
	private int zug = 0; 
	
	public Stellung(List<IFigur> figuren, boolean istSchlagzug, IFigur ziehendeFigur){
		this(figuren, istSchlagzug, ziehendeFigur, 0);
	}
	
	public Stellung(List<IFigur> figuren, boolean istSchlagzug, IFigur ziehendeFigur, int zug){
		// @TODO Stellung#Stellung
		this.figuren = figuren;
		this.schlagzug = istSchlagzug;
		this.ziehendeFigur = ziehendeFigur;
		this.zug = zug;
		
		// hier fehlt noch die berechnung von wei§bedroht/schwarzbedroht (K…NIG)
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
		// TODO Stellung#KoenigBedroht
		return false;
	}

	public List<IFigur> gebeFiguren(Figurart figurart, Farbe farbe) {
		List<Figurart> figurarten = new ArrayList<Figurart>(1);
		List<Farbe> farben = new ArrayList<Farbe>(1);
		figurarten.add(figurart);
		farben.add(farbe);
		return gebeFiguren(figurarten,farben);
	}

	public boolean equals(Stellung stellung){
		return this.zug == stellung.zug;
	}
}
