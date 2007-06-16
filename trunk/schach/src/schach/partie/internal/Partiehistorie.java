package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	private List<IStellung> stellungen = null;
	private boolean simuliere = false;
	private Partiehistorie() {
		restart();
	}
	private List<String> algebraischeNotation = null;
	
	public List<String> gebeBisherigeNotationen() {
		return algebraischeNotation;
	}
	
	public static IPartiehistorie getInstance() {
		if(instance == null)
			instance = new Partiehistorie();
		
		return instance;
	}
	public List<IStellung> gebeAlleStellungen() {
		return stellungen;
	}

	public IStellung gebeStellung() {
		// TODO Historie#gebeStellung
		return new Stellung(false,null,stellungen.size()+1);
	}

	public List<IStellung> gebeStellungen(int n) {
		if(n > stellungen.size())
			return gebeAlleStellungen();
		
		List<IStellung> lastStellung=new ArrayList<IStellung>(); 
		for (int i=stellungen.size()-n;i<stellungen.size();i++){
			lastStellung.add(stellungen.get(i));
		}
		return lastStellung;
		
	}

	public boolean istZugProtokolliert() {
		return stellungen.get(stellungen.size()-1).equals(gebeStellung());
	}
	
	public IStellung simuliereStellung(IFeld start, IFeld ziel) throws NegativeConditionException {
		return simuliereStellung(start, ziel, null);
	}

	public IStellung simuliereStellung(IFeld start, IFeld ziel, IFigur zuschlagendeFigur)
			throws NegativeConditionException {
		IFigur ziehendeFigur = Brett.getInstance().gebeFigurVonFeld(start);
		IFeld zuschlagendeFigurFeld = null;
		
		if(ziehendeFigur == null)
			throw new NegativePreConditionException("Es wurde keine Figur ausgewŠhlt.");
		
		simuliere  = true;
		Logger.debug("Simulation Start..");
		
		start.istBesetzt(false);
		ziehendeFigur.simuliereBrettzug(ziel);
		
		if(zuschlagendeFigur != null){
			zuschlagendeFigurFeld = zuschlagendeFigur.gebePosition();
			zuschlagendeFigur.simuliereBrettzug(null);
			zuschlagendeFigurFeld.istBesetzt(false);
		}
		ziel.istBesetzt(true);
		
		Stellung simulation = new Stellung(ziel.istBesetzt(),ziehendeFigur);
		
		start.istBesetzt(true);
		ziehendeFigur.simuliereBrettzug(start);
		ziel.istBesetzt(false);
		if(zuschlagendeFigur != null){
			zuschlagendeFigur.simuliereBrettzug(zuschlagendeFigurFeld);
			zuschlagendeFigurFeld.istBesetzt(true);
		}

		simuliere = false;
		Logger.debug("Simulation Ende..");
		
		/* quick & dirty fix */
		for(Reihe r : Reihe.values()){
			for(Linie l : Linie.values()){
				Brett.getInstance().gebeFeld(r, l).istBesetzt(false);
			}
		}
		for(IFigur f : AlleFiguren.getInstance().gebeAlleFiguren()){
			if(f.istAufDemSchachbrett()){
				f.gebePosition().istBesetzt(true);
			}
		}
		/* quick & dirty fix */
		
		return simulation;
	}

	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur) {
		protokolliereStellung(schlagzug, ziehendeFigur, null);
	}
	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur, IFigur neueFigur) {
		stellungen.add(new Stellung(schlagzug,ziehendeFigur, stellungen.size()+1));
		algebraischeNotation.add(bildeAlgebraischeNotation(ziehendeFigur, schlagzug, neueFigur));
//		Logger.info(algebraischeNotation.get(algebraischeNotation.size()-1));
	}

	public boolean istEineSimulation() {
		return simuliere;
	}

	public void setzeSimulation(boolean b) {
		simuliere = b;
	}
	
	private String bildeAlgebraischeNotation(IFigur figur, boolean istSchlagzug, IFigur neueFigur){
		if(figur.gebeArt().equals(Figurart.KOENIG)){
			if(figur.gebeVorPosition().equals(figur.gebeGrundposition())){
				if(figur.gebePosition().gebeLinie().equals(Linie.G)){
					return "0-0";
				}
				if(figur.gebePosition().gebeLinie().equals(Linie.C)){
					return "0-0-0";
				}
			}
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(figur.gebeArt().gebeKuerzel());
		sb.append(figur.gebeVorPosition().toString().toLowerCase());
		
		if(istSchlagzug)
			sb.append('x');
		else 
			sb.append('-');
		
		if(neueFigur == null)
			sb.append(figur.gebePosition().toString().toLowerCase());
		else 
			sb.append(neueFigur.gebePosition().toString().toLowerCase());
		
		if(figur.gebeArt().equals(Figurart.BAUER) && neueFigur != null){
			if((figur.gebeFarbe().equals(Farbe.WEISS) && neueFigur.gebePosition().gebeReihe().equals(Reihe.R8)) || (figur.gebeFarbe().equals(Farbe.SCHWARZ) && neueFigur.gebePosition().gebeReihe().equals(Reihe.R1))){
				sb.append(figur.gebeArt().gebeKuerzel());
			}
		}
	
		
		if(Partiezustand.getInstance().istSchach(figur.gebeFarbe().andereFarbe()))
			sb.append('+');
		
		if(Partiezustand.getInstance().istSchachmatt())
			sb.append('+');
		
		try {
			if(figur instanceof IBauer && !figur.gebePosition().gebeLinie().equals(figur.gebeVorPosition().gebeLinie()) && istSchlagzug){
				for(IFigur suchfigur : AlleFiguren.getInstance().gebeAlleFiguren()){
					try {
						if(suchfigur.gebeArt().equals(Figurart.BAUER) && suchfigur.gebeFarbe().equals(figur.gebeFarbe().andereFarbe())){
//							Logger.debug("en passent check: "+suchfigur+" auf Schachbrett: "+suchfigur.istAufDemSchachbrett()+" mit Grundposition "+suchfigur.gebeGrundposition()+" was mit -R "+suchfigur.gebeGrundposition().minusReihe(1)+" gleich "+figur.gebePosition()+" ist und die Vorposition "+suchfigur.gebeVorPosition()+" gleich "+figur.gebePosition()+" mit -R "+figur.gebePosition().minusReihe(1)+" ist");
							if(!suchfigur.istAufDemSchachbrett() && suchfigur.gebeGrundposition().minusReihe(1).equals(figur.gebePosition()) && suchfigur.gebeVorPosition().equals(figur.gebePosition().minusReihe(1))){
								sb.append(" e.p.");
							}
						}
					} catch (NegativeConditionException e) {}
				}
			}
		} catch (NullPointerException e) {} // keine figurposi bei umwandlung
		
		if(Partiezustand.getInstance().istRemisMoeglich())
			sb.append(" (=)");
		
		return sb.toString();
	}
	
	public void restart() {
		algebraischeNotation = new ArrayList<String>();
		stellungen=new ArrayList<IStellung>();
		simuliere = false;
	}
}
