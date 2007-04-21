package schach.brett.internal;

import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.system.NegativeConditionException;

public class Feld implements IFeld {
	private final Reihe reihe;
	private final Linie linie;
	private boolean besetzt = false;
	
	public Feld(Reihe reihe, Linie linie){
		if(reihe == null || linie == null){
			throw new NullPointerException();
		}
		else {
			this.reihe = reihe;
			this.linie = linie;
		}
	}
	
	public Linie gebeLinie() {
		return linie;
	}

	public Reihe gebeReihe() {
		return reihe;
	}

	public IFeld minusLinie(int n) throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeld minusReihe(int n) throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeld plusLinie(int n) throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

	public IFeld plusReihe(int n) throws NegativeConditionException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean istBesetzt() {
		return besetzt;
	}

	public boolean equals(IFeld feld){
		return reihe.equals(feld.gebeReihe()) && linie.equals(feld.gebeLinie());
	}

	public void istBesetzt(boolean status) {
		besetzt = status;
	}
	
	public String toString(){
		return ""+linie+reihe;
	}
}
