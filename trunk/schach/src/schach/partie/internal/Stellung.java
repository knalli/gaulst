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
	private final boolean schlagzug;
	private final Figurart ziehenderFigurart;
	private final int halbzug;
	private boolean weisserKoenigBedroht = false;
	private boolean schwarzerKoenigBedroht = false;
	private boolean patt = false;
	private boolean remis = false;
	private boolean remismoeglich = false;
	private boolean schachmatt = false; 
	private final String stellungshash;
	private boolean remismoeglichdurchwiederholung = false;
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur){
		this(istSchlagzug, ziehendeFigur, 0);
	}
	
	public Stellung(boolean istSchlagzug, IFigur ziehendeFigur, int zug){
		ziehenderFigurart = ziehendeFigur.gebeArt();
		
		schlagzug = istSchlagzug;
		halbzug = zug;
		IPartie partie = Partie.getInstance();
		Farbe jetztFarbe = partie.aktuelleFarbe();
		
		boolean externeSimulationNichtAktiv = false;
		if(!Partiehistorie.getInstance().istEineSimulation()){
			externeSimulationNichtAktiv = true;
			Partiehistorie.getInstance().setzeSimulation(true);
		}
		
		StringBuilder sb = new StringBuilder(96);
		for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
			sb.append(figur.gebeArt().gebeKuerzel());
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
			
			
			Farbe gegnerfarbe = Partie.getInstance().aktuelleFarbe().andereFarbe();
			if(istKoenigBedroht(gegnerfarbe)){
				Logger.info("Berechne Matt.."+Partie.getInstance().aktuelleFarbe());
				
				Partiehistorie.getInstance().setzeSimulation(true);
				partie.setzeFarbe(gegnerfarbe);
				
				Logger.debug(gegnerfarbe+" ist potenziell matt..");
				Logger.debug(gegnerfarbe.andereFarbe()+ " << andere farbe");
				schachmatt = true;
				for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Arrays.asList(new Farbe[] {gegnerfarbe}))){
					
					for(Reihe reihe : Reihe.values()){
						for(Linie linie : Linie.values()){
							
							IFeld feld = Brett.getInstance().gebeFeld(reihe, linie);
							
							if(feld.equals(figur.gebePosition())) continue;
							
							try {
//								Logger.debug(farbe+" Teste zug: "+figur+" auf "+feld);
								figur.testeZug(feld);
							} catch (NegativeConditionException e1) {
//								Logger.debug("geht nicht. weiter");
								continue;
							}
//							Logger.debug(farbe+" Teste zug: "+figur+" auf "+feld+" weiter");
							IFigur figur2 = Brett.getInstance().gebeFigurVonFeld(feld);
							if(figur2 == null || figur2.gebeFarbe().equals(gegnerfarbe.andereFarbe())){
								IStellung stellung;
								try {
									stellung = Partiehistorie.getInstance().simuliereStellung(figur.gebePosition(), feld, figur2);
									Partiehistorie.getInstance().setzeSimulation(true);
									if(!stellung.istKoenigBedroht(gegnerfarbe)){
										Logger.debug("Kein Schachmatt, weil "+figur+" nach "+feld);
										schachmatt = false;
										break;
									}
								} catch (NegativeConditionException e) {
									Logger.error("Fehler bei istMatt: Stellung simulieren.");
								}
							}
							
							if(!schachmatt) break;
						} // linie
						
						if(!schachmatt) break;
					} // reihe
					
					if(!schachmatt) break;
				} // figur
				
				partie.setzeFarbe(gegnerfarbe.andereFarbe());
				Partiehistorie.getInstance().setzeSimulation(false);
				
				Logger.info("!Berechne Matt: " + (schachmatt?"ist matt":"ist nicht matt")+" "+Partie.getInstance().aktuelleFarbe());
			}
			
			
			// Remisregel letzten 50 Zügen bzw. 100 Halbzüge
			List<IStellung> l100stellungen = Partiehistorie.getInstance().gebeStellungen(100);
			if(l100stellungen.size() >= 100){
				remismoeglich = true;
				for(IStellung stellung : l100stellungen){
					if(stellung.ziehendeFigur().equals(Figurart.BAUER) || stellung.istSchlagzug()){
						remismoeglich = false;
						break;
					}
				}
			}
			
			if(!Partiezustand.getInstance().warBereitsStellungswiederholung()){
				Map<String, Integer> stellungszaehler = new HashMap<String, Integer>();
				for(IStellung stellung : Partiehistorie.getInstance().gebeAlleStellungen()){
					if(!stellungszaehler.containsKey(stellung.gebeHashwert())){
						stellungszaehler.put(stellung.gebeHashwert(), 1);
					}
					else {
						if(stellungszaehler.get(stellung.gebeHashwert()) == 2) {
							remismoeglich = true;
							remismoeglichdurchwiederholung  = true;
							Logger.debug("Remis möglich nach 3 gleichen Stellungen");
							break;
						}
						stellungszaehler.put(stellung.gebeHashwert(), stellungszaehler.get(stellung.gebeHashwert())+1);
					}
				}
			}
			
			if(!schachmatt && !istKoenigBedroht(gegnerfarbe)){
				Logger.info("Berechne Matt.."+Partie.getInstance().aktuelleFarbe());
				
				Partiehistorie.getInstance().setzeSimulation(true);
				partie.setzeFarbe(gegnerfarbe);
				
				Logger.debug(gegnerfarbe+" ist potenziell patt..");
				Logger.debug(gegnerfarbe.andereFarbe()+ " << andere farbe");
				patt = true;
				for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Arrays.asList(new Farbe[] {gegnerfarbe}))){
					
					for(Reihe reihe : Reihe.values()){
						for(Linie linie : Linie.values()){
							
							IFeld feld = Brett.getInstance().gebeFeld(reihe, linie);
							
							if(feld.equals(figur.gebePosition())) continue;
							
							try {
								figur.testeZug(feld);
							} catch (NegativeConditionException e1) {
								continue;
							}
							IFigur figur2 = Brett.getInstance().gebeFigurVonFeld(feld);
							if(figur2 == null || figur2.gebeFarbe().equals(gegnerfarbe.andereFarbe())){
								IStellung stellung;
								try {
									stellung = Partiehistorie.getInstance().simuliereStellung(figur.gebePosition(), feld, figur2);
									Partiehistorie.getInstance().setzeSimulation(true);
									if(!stellung.istKoenigBedroht(gegnerfarbe)){
										Logger.debug("Kein Patt, weil "+figur+" nach "+feld);
										patt = false;
										break;
									}
								} catch (NegativeConditionException e) {
									Logger.error("Fehler bei istPatt: Stellung simulieren.");
								}
							}
							
							if(!patt) break;
						} // linie
						
						if(!patt) break;
					} // reihe
					
					if(!patt) break;
				} // figur
				
				partie.setzeFarbe(gegnerfarbe.andereFarbe());
				Partiehistorie.getInstance().setzeSimulation(false);
				
				Logger.info("!Berechne Patt: " + (patt?"ist patt":"ist nicht patt")+" "+Partie.getInstance().aktuelleFarbe());
			}
		}
		
		if(remismoeglich){
			try {
				Partie.getInstance().bieteRemisAn(Partie.getInstance().aktuellerSpieler());
			} catch (NegativeConditionException e) {}
		}
	}
	
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
	
	public boolean istRemisMoeglichDurchStellungswiederholung(){
		return remismoeglichdurchwiederholung;
	}
	
	public boolean istGleicheStellung(IStellung stellung){
		return gebeHashwert().equals(stellungshash);
	}

	public String gebeHashwert() {
		return stellungshash;
	}
}
