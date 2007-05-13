package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Figurart;
import schach.brett.Farbe;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
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
	private List<IStellung> stellungen=new ArrayList<IStellung>();
	private boolean simuliere = false;
	private Partiehistorie() {}
	private List<String> algebraischeNotation = new ArrayList<String>();
	private boolean istSchachmatt=false;
	private boolean istRemis=false;
	private boolean istPatt=false;
	
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
		
		if(neueFigur == null)
			sb.append(figur.gebePosition().toString().toLowerCase());
		else 
			sb.append(neueFigur.gebePosition().toString().toLowerCase());
		
		if(figur instanceof IBauer && neueFigur != null){
			if((figur.gebeFarbe().equals(Farbe.WEISS) && neueFigur.gebePosition().gebeReihe().equals(Reihe.R8)) || (figur.gebeFarbe().equals(Farbe.SCHWARZ) && neueFigur.gebePosition().gebeReihe().equals(Reihe.R1))){
				switch(neueFigur.gebeArt()){
				case DAME: sb.append('D'); break;
				case LAEUFER: sb.append('L'); break;
				case SPRINGER: sb.append('S'); break;
				case TURM: sb.append('T'); break; 
				}
			}
		}
	
		
		if(Partiehistorie.getInstance().gebeStellungen(1).get(0).istKoenigBedroht(figur.gebeFarbe()))
			sb.append('+');
		
		if(Partiezustand.getInstance().istPatt())
			sb.append('+');
		
		try {
			if(figur instanceof IBauer && !figur.gebePosition().gebeLinie().equals(figur.gebeVorPosition().gebeLinie()) && istSchlagzug){
				for(IFigur suchfigur : AlleFiguren.getInstance().gebeAlleFiguren()){
					try {
						if(suchfigur.gebeArt().equals(Figurart.BAUER) && suchfigur.gebeFarbe().equals(figur.gebeFarbe().andereFarbe())){
							Logger.debug("en passent check: "+suchfigur+" auf Schachbrett: "+suchfigur.istAufDemSchachbrett()+" mit Grundposition "+suchfigur.gebeGrundposition()+" was mit -R "+suchfigur.gebeGrundposition().minusReihe(1)+" gleich "+figur.gebePosition()+" ist und die Vorposition "+suchfigur.gebeVorPosition()+" gleich "+figur.gebePosition()+" mit -R "+figur.gebePosition().minusReihe(1)+" ist");
							if(!suchfigur.istAufDemSchachbrett() && suchfigur.gebeGrundposition().minusReihe(1).equals(figur.gebePosition()) && suchfigur.gebeVorPosition().equals(figur.gebePosition().minusReihe(1))){
								sb.append(" e.p.");
							}
						}
					} catch (NegativeConditionException e) {}
				}
			}
		} catch (NullPointerException e) {} // keine figurposi bei umwandlung
		return sb.toString();
	}
	

	public boolean istPatt() {
		// TODO Auto-generated method stub
		return istPatt;
	}

	public boolean istRemis() {
		// TODO Auto-generated method stub
		return istRemis;
	}

	public boolean istSchachmatt() {
		// TODO Auto-generated method stub
		return istSchachmatt;
	}	
}
