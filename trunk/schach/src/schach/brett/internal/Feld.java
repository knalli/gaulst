package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.partie.internal.Partie;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

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
//		wenn wei§ am zug, dann einfach "normal"
		Farbe farbe = Partie.getInstance().aktuelleFarbe();
		if(farbe.equals(Farbe.WEISS)){
			if(linie.vorherige() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe, linie.vorherige());
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.minusLinie(n-1);
			else
				return feld;
		}
//		wenn schwarz am zug, dann muss alles umgedreht werden
		else
		{
			if(linie.naechste() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe, linie.naechste());
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.minusLinie(n-1);
			else
				return feld;
		}
	}

	public IFeld minusReihe(int n) throws NegativeConditionException {
//		wenn wei§ am zug, dann einfach "normal"
		Farbe farbe = Partie.getInstance().aktuelleFarbe();
		if(farbe.equals(Farbe.WEISS)){
			if(reihe.vorherige() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe.vorherige(), linie);
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.minusReihe(n-1);
			else
				return feld;
		}
//		wenn schwarz am zug, dann muss alles umgedreht werden
		else
		{
			if(reihe.naechste() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe.naechste(), linie);
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.minusReihe(n-1);
			else
				return feld;
		}
	}

	public IFeld plusLinie(int n) throws NegativeConditionException {
//		wenn wei§ am zug, dann einfach "normal"
		Farbe farbe = Partie.getInstance().aktuelleFarbe();
		if(farbe.equals(Farbe.WEISS)){
			if(linie.naechste() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe, linie.naechste());
			
			if(n > 1)
				return feld.plusLinie(n-1);
			else
				return feld;
		}
//		wenn schwarz am zug, dann muss alles umgedreht werden
		else
		{
			if(linie.vorherige() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe, linie.vorherige());
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.plusLinie(n-1);
			else
				return feld;
		}
	}

	public IFeld plusReihe(int n) throws NegativeConditionException {
//		wenn wei§ am zug, dann einfach "normal"
		Farbe farbe = Partie.getInstance().aktuelleFarbe();
		if(farbe.equals(Farbe.WEISS)){
			if(reihe.naechste() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe.naechste(), linie);
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.plusReihe(n-1);
			else
				return feld;
		}
//		wenn schwarz am zug, dann muss alles umgedreht werden
		else
		{
			if(reihe.vorherige() == null)
				throw new NegativePreConditionException();
			
			IFeld feld = Brett.getInstance().gebeFeld(reihe.vorherige(), linie);
			Logger.debug(farbe + ": "+this+"->"+feld);
			
			if(n > 1)
				return feld.plusReihe(n-1);
			else
				return feld;
		}
	}

	public boolean istBesetzt() {
		return besetzt;
	}

	public boolean equals(IFeld feld){
		return reihe.equals(feld.gebeReihe()) && linie.equals(feld.gebeLinie()) && (istBesetzt() == feld.istBesetzt());
	}

	public void istBesetzt(boolean status) throws NegativeConditionException{
		if((besetzt && status) || (!besetzt && !status))
			throw new NegativePreConditionException();
		
		besetzt = status;
	}
	
	public String toString(){
		return ""+linie+reihe;
	}
	
	public IFeld clone(){
		IFeld feld = new Feld(reihe, linie);
		try {
			feld.istBesetzt(besetzt);
		} catch (NegativeConditionException e) {}
		return feld;
	}
}
