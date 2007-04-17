package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.ISchlagbareFigur;
import schach.system.NegativeConditionException;

public class Bauer extends AbstrakteFigur implements IBauer {

	public Bauer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.BAUER);
	}

	public void entnehmen() throws NegativeConditionException {
		// TODO Auto-generated method stub
//		if(!Brett.getInstance().istBauernUmwandlung())
//			throw new NegativePreConditionException();
//		
	}

	public boolean letzteRundeDoppelschritt() {
		// TODO Auto-generated method stub
		return false;
	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void schlaegtEnPassant(IFeld ziel)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void geschlagenWerden() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sollEntferntWerden() {
		// TODO Auto-generated method stub
		return false;
	}

	public Figurart gebeArt() {
		// TODO Auto-generated method stub
		return null;
	}

}
