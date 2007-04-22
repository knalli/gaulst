package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.ISchlagbareFigur;
import schach.brett.ITurm;
import schach.system.NegativeConditionException;

public class Turm extends AbstrakteFigur implements ITurm {

	public Turm(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.TURM);
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

	public void geschlagenWerden() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sollEntferntWerden() {
		// TODO Auto-generated method stub
		return false;
	}

}