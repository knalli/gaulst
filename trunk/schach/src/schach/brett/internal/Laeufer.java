package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.ILaeufer;
import schach.brett.ISchlagbareFigur;
import schach.partie.IStellung;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Laeufer extends AbstrakteFigur implements ILaeufer {
	
	IBrett brett = Brett.getInstance();
	
	public Laeufer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.LAEUFER);
	}
	
	protected Laeufer(IFigur figur){
		super(figur);
	}
	
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();

		List <IFeld> k_diagonale = Brett.getInstance().gebeFelderInDiagonalen(position, ziel);	
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_diagonale))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
		}
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
			|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
		
		List <IFeld> k_diagonale = Brett.getInstance().gebeFelderInDiagonalen(position, ziel);	
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_diagonale))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
		}	
	}
	public void geschlagenWerden() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sollEntferntWerden() {
		// TODO Auto-generated method stub
		return false;
	}

}
