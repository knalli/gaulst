package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.ILaeufer;
import schach.brett.ISchlagbareFigur;
import schach.system.NegativeConditionException;

public class Laeufer extends AbstrakteFigur implements ILaeufer {

	public Laeufer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.LAEUFER);
	}
	
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
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

}
