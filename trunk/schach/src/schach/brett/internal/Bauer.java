package schach.brett.internal;

import quicktime.std.image.NearestPointInfo;
import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Bauer extends AbstrakteFigur implements IBauer {
	private boolean doppelschritt = false;
	private boolean sollentferntwerden = false;

	public Bauer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.BAUER);
	}

	public void entnehmen() throws NegativeConditionException {
		if(!position.gebeReihe().equals(Partie.getInstance().gegnerischerSpieler().gebeGrundreihe()))
			throw new NegativePreConditionException();
		
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException();
	
		if(!istAufDemSchachbrett())
			throw new NegativePreConditionException();
		
		grundposition = null;
		position.istBesetzt(false);
		position = null;
	}

	public boolean letzteRundeDoppelschritt() {
		return doppelschritt;
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
		boolean macheEinenDoppelschritt = false;
		if(!gehoertSpieler().istZugberechtigt())
			throw new NegativePreConditionException();
		
		if(Partiezustand.getInstance().istRemis())
			throw new NegativePreConditionException();
		
		if(Partiezustand.getInstance().istPatt())
			throw new NegativePreConditionException();
		
		if(Partiezustand.getInstance().istSchachmatt())
			throw new NegativePreConditionException();
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException();
		
		if(((IKoenig)(Partiehistorie.getInstance().simuliereStellung(position, ziel).gebeFiguren(Figurart.KOENIG, farbe).get(0))).istBedroht())
			throw new NegativePreConditionException();
		
		if(position.plusReihe(1).equals(ziel)){
			if(ziel.istBesetzt())
				throw new NegativePreConditionException();
			
		} else if(position.plusReihe(2).equals(ziel) && istAufGrundposition()) {
			if(position.plusReihe(1).istBesetzt() || ziel.istBesetzt())
				throw new NegativePreConditionException();
			macheEinenDoppelschritt = true;
			
		} else
			throw new NegativePreConditionException();
		
		position = ziel;
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partie.getInstance().wechsleSpieler();
			Partiehistorie.getInstance().protokolliereStellung();
		}
		
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		doppelschritt = macheEinenDoppelschritt;
		
		setChanged();
		notifyObservers();
	}

	public void geschlagenWerden() throws NegativeConditionException {
		if(!sollentferntwerden || !istAufDemSchachbrett()){
			throw new NegativePreConditionException();
		}
		
		position.istBesetzt(false);
		position = null;
		grundposition = null;
	}

	public boolean sollEntferntWerden() {
		return sollentferntwerden;
	}

	public void letzteRundeDoppelschritt(boolean b) {
		if(Partie.getInstance().aktuelleFarbe().equals(farbe)){
			doppelschritt = b;
		}
	}

	public boolean wurdeBewegt() {
		return istAufGrundposition();
	}
}
