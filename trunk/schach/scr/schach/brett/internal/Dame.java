package schach.brett.internal;
// hier versuch ich mich dann mal...
import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IDame;
import schach.brett.IFeld;
import schach.brett.ISchlagbareFigur;
import schach.system.NegativeConditionException;

public class Dame extends AbstrakteFigur implements IDame {

	public Dame(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.DAME);
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
