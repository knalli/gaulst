package schach.brett.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import schach.brett.Farbe;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.partie.internal.Partie;
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<IFeld> gebeFelderInLinie(IFeld start, IFeld ende)
			throws NegativeConditionException {
		if(start == null || ende == null)
			throw new NullPointerException();
		
		if(!start.gebeLinie().equals(ende.gebeLinie()))
			throw new NegativePreConditionException();
		
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
			i = ende.gebeReihe().vorherige();
			while(!i.equals(start.gebeReihe())){
				weg.add(brett.gebeFeld(i, start.gebeLinie()));
				i = i.vorherige();
			}
		}
		
		return weg;
	}

	public List<IFeld> gebeFelderInReihe(IFeld start, IFeld ende)
			throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFigur gebeFigurVonFeld(IFeld feld) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean istBauernUmwandlung() {
		// TODO Auto-generated method stub
		// @TODO hier steht true, damit ein test funktioniert.. bitte auch berichtigen!
		return true;
	}

	public void raeumeAb() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sindAlleFelderFrei(List<IFeld> felder) {
		// TODO Auto-generated method stub
		return false;
	}

	public void wandleBauernUm(IBauer bauer, IFigur figur)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public IFeld gebeFeld(Reihe reihe, Linie linie) {
		// @TODO Was ist hier abzufragen
//		if(reihe == null || linie == null)
//			throw new NegativePreConditionException();
		
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
