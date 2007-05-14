package schach.partie.internal;

import java.util.Arrays;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartie;
import schach.partie.IStellung;
import schach.system.Logger;
import schach.system.NegativeConditionException;

public class Stellung implements IStellung {
	private boolean schlagzug = false;
	private Figurart ziehenderFigurart = null;
	private int halbzug = 0;
	private boolean weisserKoenigBedroht = false;
	private boolean schwarzerKoenigBedroht = false;
	private boolean patt = false;
	private boolean remis = false;
	private boolean schachmatt = false; 
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur){
		this(istSchlagzug, ziehendeFigur, 0);
	}
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur, int zug){
		ziehenderFigurart = ziehendeFigur.gebeArt();
		
		this.schlagzug = istSchlagzug;
		this.halbzug = zug;
		IPartie partie = Partie.getInstance();
		Farbe jetztFarbe = partie.aktuelleFarbe();
		
		boolean externeSimulationNichtAktiv = false;
		if(!Partiehistorie.getInstance().istEineSimulation()){
			externeSimulationNichtAktiv = true;
			Partiehistorie.getInstance().setzeSimulation(true);
		}

		for(Farbe farbe : Farbe.values()){
			for(IFigur f : AlleFiguren.getInstance().gebeAlleFiguren()){
				System.out.println(f.toString()+", ");
			}
			System.out.println("!!");
			IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe)).get(0);
			for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
				if(!figur.gebeFarbe().equals(farbe) && figur.istAufDemSchachbrett()){
					try {
						// zum testen müssen wir kurz die sichtweise ändern
						Logger.debug("Prüfe, ob "+figur+" schlägt "+koenig);
						partie.setzeFarbe(farbe.andereFarbe());
						figur.testeZug(koenig.gebePosition());
						partie.setzeFarbe(farbe);
						Logger.debug(figur+" schlägt den "+koenig);
						
						if(farbe.equals(Farbe.WEISS))
							weisserKoenigBedroht = true;
						else 
							schwarzerKoenigBedroht = true;
						
					} catch (NegativeConditionException e) {
						Logger.debug("Negativ");
					}
				}
			}
		}
		partie.setzeFarbe(jetztFarbe);
		if(externeSimulationNichtAktiv)
			Partiehistorie.getInstance().setzeSimulation(false);
		
//		if(externeSimulationNichtAktiv){
//			// in simulation derzeit keine patt/schach/matt findung
//			
//			Logger.info("Berechne Matt..");
//			for(Farbe farbe : Farbe.values()){
//				if(istKoenigBedroht(farbe)){
//					schachmatt = true;
//					for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Arrays.asList(new Farbe[] {farbe}))){
//						for(Reihe reihe : Reihe.values()){
//							for(Linie linie : Linie.values()){
//								IFeld feld = Brett.getInstance().gebeFeld(reihe, linie);
//								IFigur figur2 = Brett.getInstance().gebeFigurVonFeld(feld);
//								if(figur2 == null || figur2.gebeFarbe().equals(farbe.andereFarbe())){
//									IStellung stellung;
//									try {
//										stellung = Partiehistorie.getInstance().simuliereStellung(figur.gebePosition(), feld, figur2);
//										if(!stellung.istKoenigBedroht(farbe)){
//											schachmatt = false;
//											break;
//										}
//									} catch (NegativeConditionException e) {
//										Logger.error("Fehler bei istPatt: Stellung simulieren.");
//									}
//								}
//								if(!schachmatt) break;
//							}
//							if(!schachmatt) break;
//						}
//						if(!schachmatt) break;
//					}
//				}
//				if(schachmatt) break;
//			}
//			Logger.info("!Berechne Matt: " + (schachmatt?"ist matt":"ist nicht matt"));
//		}
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

	public Figurart ziehendeFigur() {
		return ziehenderFigurart;
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

	public boolean istPatt() {
		return patt;
	}

	public boolean istRemis() {
		return remis;
	}

	public boolean istSchachmatt() {
		return schachmatt;
	}
}
