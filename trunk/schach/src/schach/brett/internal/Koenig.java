package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.partie.IStellung;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Koenig extends AbstrakteFigur implements IKoenig {

	IBrett brett = Brett.getInstance();

	public Koenig(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.KOENIG);
	}
	
	protected Koenig(IFigur figur) {
		super(figur);
	}
	
	public boolean istBedroht() {
		// TODO Koenig#istBedroht
		return false;
	}

	public boolean istInEinerRochade() {
		// TODO Koenig#istInEinerRochade
		return false;
	}

	public void rochiert(IFeld ziel) throws NegativeConditionException {
		// TODO Koenig#rochiert

	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		if(!position.gebeLinie().equals(ziel.gebeLinie()) && !position.gebeReihe().equals(ziel.gebeReihe())){
			throw new NegativePreConditionException();
		}
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
			
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
		
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(position, ziel);
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_linie))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_reihe))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
		}
	}

	public boolean wurdeBewegt() throws NegativeConditionException {
		// TODO Koenig#bewegt
		return false;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
			|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
		
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(position, ziel);
		
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_linie))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_reihe))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
		}		

	}


}
