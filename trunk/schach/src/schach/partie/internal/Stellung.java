package schach.partie.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private boolean remismoeglich = false;
	private boolean schachmatt = false; 
	private String stellungshash = null;
	
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
		
		StringBuilder sb = new StringBuilder(96);
		for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
			switch(figur.gebeArt()){
			case BAUER: sb.append('B'); break;
			case DAME: sb.append('D'); break;
			case KOENIG: sb.append('K'); break;
			case LAEUFER: sb.append('L'); break;
			case SPRINGER: sb.append('S'); break;
			case TURM: sb.append('T'); break;
			}
			sb.append(figur.gebePosition());
		}
		stellungshash = sb.toString();

		for(Farbe farbe : Farbe.values()){
			IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe)).get(0);
			for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
				if(!figur.gebeFarbe().equals(farbe) && figur.istAufDemSchachbrett()){
					try {
						// zum testen müssen wir kurz die sichtweise ändern
//						Logger.debug("Prüfe, ob "+figur+" schlägt "+koenig);
						partie.setzeFarbe(farbe.andereFarbe());
						figur.testeZug(koenig.gebePosition());
						partie.setzeFarbe(farbe);
//						Logger.debug(figur+" schlägt den "+koenig);
						
						if(farbe.equals(Farbe.WEISS))
							weisserKoenigBedroht = true;
						else 
							schwarzerKoenigBedroht = true;
						
					} catch (NegativeConditionException e) {
//						Logger.debug("Negativ");
					}
				}
			}
		}
		partie.setzeFarbe(jetztFarbe);
		if(externeSimulationNichtAktiv)
			Partiehistorie.getInstance().setzeSimulation(false);
		
		if(externeSimulationNichtAktiv){
			// in simulation derzeit keine patt/schach/matt findung
			
//			Logger.info("Berechne Matt..");
//			for(Farbe farbe : Farbe.values()){
//				if(istKoenigBedroht(farbe)){
//					schachmatt = true;
//					for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Arrays.asList(new Farbe[] {farbe}))){
//						for(Reihe reihe : Reihe.values()){
//							for(Linie linie : Linie.values()){
//								IFeld feld = Brett.getInstance().gebeFeld(reihe, linie);
//								Logger.debug("Teste: "+feld+" mit "+figur);
//								if(feld.equals(figur.gebePosition())) continue;
//								try {
//									Logger.debug(farbe+" Teste zug: "+figur+" auf "+feld);
//									partie.setzeFarbe(farbe);
//									figur.testeZug(feld);
//									partie.setzeFarbe(jetztFarbe);
//								} catch (NegativeConditionException e1) {
//									Logger.debug("geht nicht. weiter");
//									continue;
//								}
//								Logger.debug(farbe+" Teste zug: "+figur+" auf "+feld+" weiter");
//								IFigur figur2 = Brett.getInstance().gebeFigurVonFeld(feld);
//								if(figur2 == null || figur2.gebeFarbe().equals(farbe.andereFarbe())){
//									IStellung stellung;
//									try {
//										stellung = Partiehistorie.getInstance().simuliereStellung(figur.gebePosition(), feld, figur2);
//										if(!stellung.istKoenigBedroht(farbe)){
//											Logger.debug("Kein Schachmatt, weil "+figur+" nach "+feld);
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
			
			
			// Remisregel letzten 50 Zügen bzw. 100 Halbzüge
			List<IStellung> l50stellungen = Partiehistorie.getInstance().gebeStellungen(100);
			remismoeglich = true;
			for(IStellung stellung : l50stellungen){
				if(stellung.ziehendeFigur().equals(Figurart.BAUER) || stellung.istSchlagzug()){
					remismoeglich = false;
					break;
				}
			}
			if(!remismoeglich){
				Map<String, Integer> stellungszaehler = new HashMap<String, Integer>();
				for(IStellung stellung : Partiehistorie.getInstance().gebeAlleStellungen()){
					if(!stellungszaehler.containsKey(stellung.gebeHashwert())){
						stellungszaehler.put(stellung.gebeHashwert(), 1);
					}
					else {
						if(stellungszaehler.get(stellung.gebeHashwert()) == 2) {
							remismoeglich = true;
							Logger.debug("Remis möglich nach 3 gleichen Stellungen");
							break;
						}
						stellungszaehler.put(stellung.gebeHashwert(), stellungszaehler.get(stellung.gebeHashwert())+1);
					}
				}
			}
		}
		partie.setzeFarbe(jetztFarbe);
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
	
	public boolean istRemisMoeglich(){
		return remismoeglich;
	}
	
	public boolean istGleicheStellung(IStellung stellung){
		return gebeHashwert().equals(stellungshash);
	}

	public String gebeHashwert() {
		return stellungshash;
	}
}
