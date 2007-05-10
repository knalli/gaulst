package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.internal.AlleFiguren;
import schach.partie.IStellung;
import schach.system.NegativeConditionException;

public class Stellung implements IStellung {
	private List<IFigur> figuren = null;
	private boolean schlagzug = false;
	private IFigur ziehendeFigur = null;
	private int halbzug = 0;
	private boolean weisserKoenigBedroht = false;
	private boolean schwarzerKoenigBedroht = false; 
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur){
		this(istSchlagzug, ziehendeFigur, 0);
	}
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur, int zug){
		List<IFigur> originalfiguren = AlleFiguren.getInstance().gebeAlleFiguren();
		figuren = new ArrayList<IFigur>(originalfiguren.size());
		IFeld feld;
		for(IFigur figur : originalfiguren){
			if(figur.istAufDemSchachbrett()){
				feld = figur.gebePosition().clone();
			}
			else {
				feld = null;
			}
			figuren.add(figur.clone(feld));
			
			if(figur.equals(ziehendeFigur))
				this.ziehendeFigur = figuren.get(figuren.size()-1);
		}
		
		this.schlagzug = istSchlagzug;
		this.halbzug = zug;
		
		for(Farbe farbe : Farbe.values()){
			IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe)).get(0);
			for(IFigur figur : figuren){
				if(!figur.gebeFarbe().equals(farbe) && figur.istAufDemSchachbrett()){
					try {
						figur.testeZug(koenig.gebePosition());
						
						if(farbe.equals(Farbe.WEISS))
							weisserKoenigBedroht = true;
						else 
							schwarzerKoenigBedroht = true;
						
					} catch (NegativeConditionException e) { }
				}
			}
		}
	}
	
//	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben) {
//		List<IFigur> figuren2 = new ArrayList<IFigur>();
//		
//		for(IFigur figur : this.figuren){
//			if(farben.contains(figur.gebeFarbe()) && figurarten.contains(figur.gebeArt())){
//				figuren2.add(figur);
//			}
//		}
//		
//		return figuren2;
//	}

	public boolean istSchlagzug() {
		return schlagzug;
	}

	public IFigur ziehendeFigur() {
		return ziehendeFigur;
	}

	public boolean istKoenigBedroht(Farbe farbe) {
		if(farbe.equals(Farbe.WEISS))
			return weisserKoenigBedroht;
		return schwarzerKoenigBedroht;
	}

//	public List<IFigur> gebeFiguren(Figurart figurart, Farbe farbe) {
//		List<Figurart> figurarten = new ArrayList<Figurart>(1);
//		List<Farbe> farben = new ArrayList<Farbe>(1);
//		figurarten.add(figurart);
//		farben.add(farbe);
//		return gebeFiguren(figurarten,farben);
//	}

	public boolean equals(Stellung stellung){
		return this.halbzug == stellung.halbzug;
	}
}
