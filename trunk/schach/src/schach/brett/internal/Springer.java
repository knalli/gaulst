package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.ISchlagbareFigur;
import schach.brett.ISpringer;
import schach.system.NegativeConditionException;

public class Springer extends AbstrakteFigur implements ISpringer {

	public Springer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.SPRINGER);
	}

	protected Springer(IFigur figur){
		super(figur);
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
