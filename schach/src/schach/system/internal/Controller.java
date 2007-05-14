package schach.system.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.internal.Partie;
import schach.system.IController;
import schach.system.Logger;
import schach.system.NegativeConditionException;

public class Controller implements IController {
	private static IController instance = null;
	
	public static IController getInstance() {
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	
	private Controller() { }
	
	
	private IFigur figur = null;
	private IFeld ziel = null;
	private String message = "";
	
	public String getMessage() {
		return message;
	}
	
	private boolean evaluateAction() {
		Logger.debug("evaluiere: "+figur + " -> "+ziel);
		IBrett brett = Brett.getInstance();
		IFigur zielfigur = brett.gebeFigurVonFeld(ziel);
		
		try {
			// steht da überhaupt wer?
			if(figur == null){
				throw new Exception("Auf dem Startfeld steht keine Schachfigur.");
			}
			
			// Ziehzug? (ohne spezialfälle)
			if(zielfigur == null){
				try {
					if(figur instanceof IKoenig){
						if(figur.istAufGrundposition() && (ziel.gebeLinie().equals(Linie.C) || ziel.gebeLinie().equals(Linie.G))){
							((IKoenig)figur).rochiert(ziel);
						}
						else {
							Logger.info(figur + " zieht nach "+ ziel);
							figur.zieht(ziel);
						}
					}
					else if(figur instanceof IBauer && !figur.gebePosition().gebeLinie().equals(ziel.gebeLinie()) && ziel.minusReihe(1).istBesetzt()) {
						Logger.info(figur + " schlägt en passent "+ ziel);
						((IBauer)figur).schlaegtEnPassant(ziel);
					}
					else {
						Logger.info(figur + " zieht nach "+ ziel);
						figur.zieht(ziel);
					}
				} catch (NegativeConditionException e) {
					Logger.info(figur + " zieht nach "+ ziel + " (1 kontrollierter Fehler)");
					figur.zieht(ziel);
				}
			}
			// Schlagzug? (ohne spezialfälle)
			else {
				// gegnerische Figur schlagbar
				if(zielfigur instanceof ISchlagbareFigur){
					Logger.info(figur + " schlägt "+ zielfigur);
					figur.schlaegt(ziel, (ISchlagbareFigur)zielfigur);
				}
				// gegnerische Figur nicht schlagbar
				else {
					throw new Exception("Figur ist nicht schlagbar.");
				}
			}
		} catch (NegativeConditionException e) {
			Logger.error("Fehler: "+e.getMessage());
			message  = e.getMessage();
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			Logger.error("Schwerer Fehler: "+e.getMessage());
			message  = e.getMessage();
			e.printStackTrace();
			return false;
		}
		
		message = "Erfolgreich ausgeführt.";
		return true;
	}
	
	private void clearState() {
		figur = null;
		ziel = null;
	}

	public void setzeFigur(IFeld feld) {
		setzeFigur(feld.gebeReihe(), feld.gebeLinie());
	}

	public void setzeFigur(Reihe reihe, Linie linie) {
		IBrett brett = Brett.getInstance();
		Logger.debug("Suche auf "+linie.toString()+reihe.toString());
		figur = brett.gebeFigurVonFeld(reihe, linie);
		ziel = null;
	}

	public void setzeZielFeld(IFeld feld) {
		ziel = feld;
		if(evaluateAction()){
			
			clearState();
		}
		else{
			ziel = null;
		}
	}

	public void setzeZielFeld(Reihe reihe, Linie linie) {
		setzeZielFeld(Brett.getInstance().gebeFeld(reihe, linie)); 
	}

	public boolean parseInputString(String text) {
		if(text.length() != 4)
			return false;
		
		Logger.debug("Parse nun "+text);
		
		if(text.equals("DAME")){
			neueFigur(Figurart.DAME, Partie.getInstance().aktuelleFarbe());
			return true;
		}
		if(text.equals("LAEUFER")){
			neueFigur(Figurart.LAEUFER, Partie.getInstance().aktuelleFarbe());
			return true;
		}
		if(text.equals("SPRINGER")){
			neueFigur(Figurart.SPRINGER, Partie.getInstance().aktuelleFarbe());
			return true;
		}
		if(text.equals("TURM")){
			neueFigur(Figurart.TURM, Partie.getInstance().aktuelleFarbe());
			return true;
		}
		
		String e1,e2,e3,e4;
		e1 = text.substring(0, 1);
		e2 = text.substring(1, 2);
		e3 = text.substring(2, 3);
		e4 = text.substring(3);
		
		Reihe r1,r2;
		Linie l1,l2;
		
		l1 = ermittleLinie(e1.toUpperCase());
		r1 = ermittleReihe(e2);
		l2 = ermittleLinie(e3.toUpperCase());
		r2 = ermittleReihe(e4);
		
		if(l1 == null || l2 == null || r1 == null || r2 == null){
			Logger.debug("Konnte nicht erfolgreich ermittelt werden.");
			return false;
		}
		Logger.debug("Ermittelte Felder: "+l1+r1+" nach "+l2+r2);
		
		setzeFigur(r1, l1);
		setzeZielFeld(r2, l2);
		
		return true;
	}

	private Reihe ermittleReihe(String e) {
		switch(e.charAt(0)){
			case '1': return Reihe.R1;
			case '2': return Reihe.R2;
			case '3': return Reihe.R3;
			case '4': return Reihe.R4;
			case '5': return Reihe.R5;
			case '6': return Reihe.R6;
			case '7': return Reihe.R7;
			case '8': return Reihe.R8;
		}
		return null;
	}

	private Linie ermittleLinie(String e) {
		switch(e.charAt(0)){
		case 'A': return Linie.A;
		case 'B': return Linie.B;
		case 'C': return Linie.C;
		case 'D': return Linie.D;
		case 'E': return Linie.E;
		case 'F': return Linie.F;
		case 'G': return Linie.G;
		case 'H': return Linie.H;
	}
	return null;
	}

	public void neueFigur(Figurart art, Farbe farbe) {
		IFigur figur = AlleFiguren.getInstance().erstelleFigur(art, farbe);
		IBauer bauer = null;
		for(IFigur suchfigur : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)){
			if(suchfigur.gebePosition().gebeReihe().equals( farbe.equals(Farbe.WEISS)?Reihe.R8:Reihe.R1 )){
				bauer = (IBauer) suchfigur;
			}
		}
		try {
			Brett.getInstance().wandleBauernUm(bauer, figur);
			message = "Erfolgreich ausgeführt.";
		} catch (NegativeConditionException e) {
			message = e.getMessage();
		}
	}
}
