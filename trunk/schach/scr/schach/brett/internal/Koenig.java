package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.system.NegativeConditionException;

public class Koenig extends AbstrakteFigur implements IKoenig {

	public Koenig(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.KOENIG);
	}
	
	public boolean istBedroht() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istInEinerRochade() {
		// TODO Auto-generated method stub
		return false;
	}

	public void rochiert(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean wurdeBewegt() throws NegativeConditionException {
		// TODO Auto-generated method stub
		return false;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

}
