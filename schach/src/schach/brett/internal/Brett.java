package schach.brett.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Brett implements IBrett {
	private static IBrett instance = null;
	private Map<IFeld,IFigur> felder = new HashMap<IFeld,IFigur>();
	private Brett(){
		for(Reihe r : Reihe.values()){
			for(Linie l : Linie.values()){
				felder.put(new Feld(r,l), null);
			}
		}
	};
	
	public static IBrett getInstance() {
		if(instance == null)
			instance = new Brett();
		
		return instance;
	}

	public List<IFeld> gebeFelderInDiagonalen(IFeld start, IFeld ende)
			throws NegativeConditionException {
		if(start == null || ende == null){
			throw new NegativePreConditionException("Diagonaleigenschaft stimmt nicht überein.");
		}
		
		// wir müssen herausfinden, welcher weg es ist
		// formel: für ein feld x,y gilt: für x' und y' muss gelten: abs(x-x') == abs(y-y')
		if(!(Math.abs(start.gebeReihe().ordinal()-ende.gebeReihe().ordinal()) == (Math.abs(start.gebeLinie().ordinal()-ende.gebeLinie().ordinal())))){
			throw new NegativePreConditionException("Diagonaleigenschaft stimmt nicht überein.");
		}
		IFeld i;		
		List<IFeld> weg = new ArrayList<IFeld>(8);
		if(start.gebeReihe().ordinal() < ende.gebeReihe().ordinal()){
			// ende liegt also rechts
			if(start.gebeLinie().ordinal() < ende.gebeLinie().ordinal()){
				i = gebeFeld(start.gebeReihe().naechste(), start.gebeLinie().naechste());
				while(i != null && !i.equals(ende)){
					weg.add(i);
					i = gebeFeld(i.gebeReihe().naechste(), i.gebeLinie().naechste());
				}
			} 
			else {
				i = gebeFeld(start.gebeReihe().naechste(), start.gebeLinie().vorherige());
				while(i != null && !i.equals(ende)){
					weg.add(i);
					i = gebeFeld(i.gebeReihe().naechste(), i.gebeLinie().vorherige());
				}
			}
		}
		else {
			weg = gebeFelderInDiagonalen(ende, start);
			Collections.reverse(weg);
		}
		
		return weg;
	}

	public List<IFeld> gebeFelderInLinie(IFeld start, IFeld ende)
			throws NegativeConditionException {
		if(start == null || ende == null)
			throw new NullPointerException();
		
		if(!start.gebeLinie().equals(ende.gebeLinie()))
			throw new NegativePreConditionException("Felder sind nicht in einer Linie.");
		
		List<IFeld> weg = new ArrayList<IFeld>(8);
		IBrett brett = Brett.getInstance();
		Reihe i;
		if(start.gebeReihe().ordinal() < ende.gebeReihe().ordinal()){
			i = start.gebeReihe().naechste();
			while(!i.equals(ende.gebeReihe())){
				weg.add(brett.gebeFeld(i, start.gebeLinie()));
				i = i.naechste();
			}
		}
		else {
			weg = gebeFelderInLinie(ende, start);
			Collections.reverse(weg);
		}
		
		return weg;
	}

	public List<IFeld> gebeFelderInReihe(IFeld start, IFeld ende)
			throws NegativeConditionException {
		if(start == null || ende == null)
			throw new NullPointerException();
		
		if(!start.gebeReihe().equals(ende.gebeReihe()))
			throw new NegativePreConditionException("Felder sind nicht in einer Reihe.");
		
		List<IFeld> weg = new ArrayList<IFeld>(8);
		IBrett brett = Brett.getInstance();
		Linie i;
		if(start.gebeLinie().ordinal() < ende.gebeLinie().ordinal()){
			i = start.gebeLinie().naechste();
			while(!i.equals(ende.gebeLinie())){
				weg.add(brett.gebeFeld(start.gebeReihe(), i));
				i = i.naechste();
			}
		}
		else {
			weg = gebeFelderInReihe(ende, start);
			Collections.reverse(weg);
		}
		
		return weg;
	}

	public IFigur gebeFigurVonFeld(IFeld feld) {
		// wenn das feld leer ist, dann gibt es keine geschenke
		if(feld == null){
			return null;
		}
		
		return gebeFigurVonFeld(feld.gebeReihe(), feld.gebeLinie());
	}
	
	public IFigur gebeFigurVonFeld(Reihe reihe, Linie linie){
		if(reihe == null || linie == null)
			return null;
		
		IFeld feld;
		for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
			
			if(!figur.istAufDemSchachbrett())
				continue;
			
			feld = figur.gebePosition();
			if(feld != null){
				if(feld.gebeLinie().equals(linie) && feld.gebeReihe().equals(reihe)){
					if(!gebeFeld(reihe, linie).istBesetzt())
						gebeFeld(reihe, linie).istBesetzt(true);
					return figur;
				}
			}
		}
		return null;
	}

	public boolean istBauernUmwandlung() {
		// alle bauern der aktuellen Partie
		Farbe farbe = Partie.getInstance().aktuelleFarbe();
		List<IFigur> bauern = AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe);
		
		// falls ein weißer bauer auf r8 oder ein schwarzer bauer auf r1 steht, true
		for(IFigur figur : bauern) {
			if(!figur.istAufDemSchachbrett())
				continue;
			
			if(farbe.equals(Farbe.WEISS)){
				if(figur.gebePosition().gebeReihe().equals(Reihe.R8)){
					return true;
				}
			}
			else {
				if(figur.gebePosition().gebeReihe().equals(Reihe.R1)){
					return true;
				}
			}
		}
		return false;
	}

	public void raeumeAb() throws NegativeConditionException {
		AlleFiguren.getInstance().entferneFiguren();
		for(IFeld feld : felder.keySet()){
			felder.remove(feld);
		}
	}

	public boolean sindAlleFelderFrei(List<IFeld> felder) {
		for(IFeld feld : felder){
			if(feld.istBesetzt()){
				return false;
			}
		}
		return true;
	}

	public void wandleBauernUm(IBauer bauer, IFigur figur)
			throws NegativeConditionException {
		
		if(!istBauernUmwandlung())
			throw new NegativePreConditionException("Es besteht keine Bauernumwandlung.");
		
		if(bauer == null || figur == null)
			throw new NegativePreConditionException("Mindestens eine Figur ist nicht vorhanden.");
		
		if(!bauer.gehoertSpieler().istZugberechtigt()){
			throw new NegativePreConditionException("Der Spieler ist nicht zugberechtigt.");
		}
		
		if(figur.istAufDemSchachbrett()){
			throw new NegativePreConditionException("Die Figur steht nicht auf dem Schachbrett.");
		}
		
		if(figur.gebeArt().equals(Figurart.KOENIG) || figur.gebeArt().equals(Figurart.BAUER)){
			throw new NegativePreConditionException("Dieser Figurentyp steht nicht zur Auswahl und ist ungültig.");
		}
		
		if(((IKoenig) (AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Partie.getInstance().aktuelleFarbe()).get(0))).istInEinerRochade()) {
			throw new NegativePreConditionException("Eigener König ist einer Rochade.");
		}
		
		if(!bauer.gebeFarbe().equals(Partie.getInstance().aktuelleFarbe()) || !figur.gebeFarbe().equals(Partie.getInstance().aktuelleFarbe())) {
			throw new NegativePreConditionException("Farben stimmen nicht überein.");
		}
		
//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, bauer.gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		figur.positionieren(bauer.gebePosition());
		bauer.entnehmen();
		
		Partiehistorie.getInstance().protokolliereStellung(false, bauer, figur);
		Partie.getInstance().wechsleSpieler();
	}

	public IFeld gebeFeld(Reihe reihe, Linie linie) {
		for(IFeld feld : felder.keySet()){
			if(feld.gebeReihe().equals(reihe)){
				if(feld.gebeLinie().equals(linie)){
					return feld;
				}
			}
		}
		return null;
	}
}
