package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.Linie;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	private List<IStellung> stellungen=new ArrayList<IStellung>();
	private boolean simuliere = false;
	private Partiehistorie() {}
	private List<String> algebraischeNotation = new ArrayList<String>();
	
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
			throw new NegativePreConditionException("Es wurde keine Figur ausgew�hlt.");
		
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
		return simulation;
	}

	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur) {
		protokolliereStellung(schlagzug, ziehendeFigur, null);
	}
	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur, IFigur neueFigur) {
		stellungen.add(new Stellung(schlagzug,ziehendeFigur, stellungen.size()+1));
		algebraischeNotation.add(bildeAlgebraischeNotation(ziehendeFigur, schlagzug, neueFigur));
		Logger.info(algebraischeNotation.get(algebraischeNotation.size()-1));
	}

	public boolean istEineSimulation() {
		return simuliere;
	}

	public void setzeSimulation(boolean b) {
		simuliere = b;
	}
	
	private String bildeAlgebraischeNotation(IFigur figur, boolean istSchlagzug, IFigur neueFigur){
		if(figur instanceof IKoenig){
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
		switch(figur.gebeArt()){
		case DAME: sb.append('D'); break;
		case KOENIG: sb.append('K'); break;
		case LAEUFER: sb.append('L'); break;
		case SPRINGER: sb.append('S'); break;
		case TURM: sb.append('T'); break;
		}
		sb.append(figur.gebeVorPosition().toString().toLowerCase());
		
		if(istSchlagzug)
			sb.append('x');
		else 
			sb.append('-');
		
		sb.append(figur.gebePosition().toString().toLowerCase());
		
		if(neueFigur != null){
			switch(neueFigur.gebeArt()){
			case DAME: sb.append('D'); break;
			case LAEUFER: sb.append('L'); break;
			case SPRINGER: sb.append('S'); break;
			case TURM: sb.append('T'); break;
			}
		}
		
		if(Partiehistorie.getInstance().gebeStellungen(1).get(0).istKoenigBedroht(figur.gebeFarbe()))
			sb.append('+');
		
		if(Partiezustand.getInstance().istPatt())
			sb.append('+');
		
		if(figur instanceof IBauer && !figur.gebePosition().gebeLinie().equals(figur.gebeVorPosition().gebeLinie()) && istSchlagzug){
			for(IFigur suchfigur : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, figur.gebeFarbe().andereFarbe())){
				try {
					if(!suchfigur.istAufDemSchachbrett() && ((IBauer)figur).letzteRundeDoppelschritt() && suchfigur.gebeVorPosition().equals(figur.gebePosition().minusReihe(1))){
						sb.append(" e.p.");
					}
				} catch (NegativeConditionException e) {}
			}
		}
		
		// @TODO bildeAlgebraiischeNotation
		// Bauernumwandlung Buchstabe hintendran
		
		return sb.toString();
	}
}
