package schach.brett.internal;

import java.util.List;

import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.system.NegativeConditionException;

public class Brett implements IBrett {
	private static IBrett instance = null;
	private Brett(){};
	
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
		// TODO Auto-generated method stub
		return null;
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
		return false;
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

	public IFeld gebeFeld(Reihe reihe, Linie linie) throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

}
