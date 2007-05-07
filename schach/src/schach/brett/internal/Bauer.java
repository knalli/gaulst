package schach.brett.internal;

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
	
	protected Bauer(IFigur figur){
		super(figur);
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
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		
		if(Partiezustand.getInstance().istRemis())
			throw new NegativePreConditionException("Partie ist Remis");
		
		if(Partiezustand.getInstance().istPatt())
			throw new NegativePreConditionException("Partie ist Patt");
		
		if(Partiezustand.getInstance().istSchachmatt())
			throw new NegativePreConditionException("Partie ist Schachmatt");
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
//		@TODO Simuliere Stellung
//		if(((IKoenig)(Partiehistorie.getInstance().simuliereStellung(position, ziel).gebeFiguren(Figurart.KOENIG, farbe).get(0))).istBedroht())
//			throw new NegativePreConditionException();
		
		if(position.plusReihe(1).equals(ziel)){
			if(ziel.istBesetzt())
				throw new NegativePreConditionException("Ziel ist besetzt");
			
		} else if(position.plusReihe(2).equals(ziel) && istAufGrundposition()) {
			if(position.plusReihe(1).istBesetzt() || ziel.istBesetzt())
				throw new NegativePreConditionException("Ziel ist besetzt oder ung�ltig");
			macheEinenDoppelschritt = true;
			
		} else
			throw new NegativePreConditionException("Ung�ltiges Ziel");
		
		position = ziel;
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partie.getInstance().wechsleSpieler();
			Partiehistorie.getInstance().protokolliereStellung(false, this);
		}
		
//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
//		dieser Bauer hat doch einen Doppelschritt gemacht?
		doppelschritt = macheEinenDoppelschritt;
		
//		informiere die Beobachter, dass sich etwas ge�ndert hat
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
		
//		keine Information an die Beobachter, da geschlagenWerden Bestandteil 
//		eines elementaren Operators wie ziehen oder schlagen ist
	}

	public boolean sollEntferntWerden() {
		return sollentferntwerden;
	}

	public void letzteRundeDoppelschritt(boolean b) {
		doppelschritt = b;
	}

	public boolean wurdeBewegt() {
		return istAufGrundposition();
	}
}
