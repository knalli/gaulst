package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Koenig extends AbstrakteFigur implements IKoenig {

	IBrett brett = Brett.getInstance();
	private boolean schonBewegt = false;
	private boolean istineinerrochade = false;

	public Koenig(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.KOENIG);
	}
	
	protected Koenig(IFigur figur) {
		super(figur);
	}
	
	public boolean istBedroht() {
		for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
			if(!figur.gebeFarbe().equals(farbe) && figur.istAufDemSchachbrett()){
				try {
					figur.testeZug(gebePosition());
					
					return true; // schade, wird bedroht
					
				} catch (NegativeConditionException e) { 
					// wird nicht bedroht
				}
			}
		}
		return false;
	}

	public boolean istInEinerRochade() {
		return istineinerrochade ;
	}

	public void rochiert(IFeld ziel) throws NegativeConditionException {
		// TODO Koenig#rochiert
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));

		if (!koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig muss in einer Rochade stehen.");
		// wtf.. in welcher methode sind wir? :P

		if(!gehoertSpieler().istZugberechtigt() && 
				Partiezustand.getInstance().istRemis() &&
				Partiezustand.getInstance().istPatt() &&
				Partiezustand.getInstance().istSchachmatt() &&
				koenig.wurdeBewegt()){
			
			throw new NegativePreConditionException();
			
		}
		
		if (ziel!=this.position.minusLinie(2) || ziel!=this.position.plusLinie(2)){
			throw new NegativePreConditionException();
		}
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("K�nig w�rde im n�chsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein K�nig mehr da?!");
		}
		
		if (ziel.equals(this.position.minusLinie(2))) {
			
			// TODO Koenig rochiert -- irgendwie wissen wie nicht, wie wir auf istBedroht abfragen sollen...
			
			
//			if(!Brett.getInstance().sindAlleFelderFrei(Brett.getInstance().gebeFelderInReihe(Brett.getInstance().gebeFeld(Reihe.R2, Linie.A), this.position.minusLinie(1)))&&
//					this.position.minusLinie(1).){
//				
//			}
		}
		else if(ziel==this.position.plusLinie(2)) {
			
		}
		
		Partie.getInstance().wechsleSpieler();
		this.position=ziel;
		Partiehistorie.getInstance().protokolliereStellung(true, this);
		
		// TODO  post Partie.istZugProtokolliert()		=  false  <-- laut OCL...
		
		this.schonBewegt =true;
	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		if(!gehoertSpieler().istZugberechtigt())
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		
		if(Partiezustand.getInstance().istRemis())
			throw new NegativePreConditionException("Partie ist Remis");
		
		if(Partiezustand.getInstance().istPatt())
			throw new NegativePreConditionException("Partie ist Patt");
		
		if(Partiezustand.getInstance().istSchachmatt())
			throw new NegativePreConditionException("Partie ist Schachmatt");
		
		if(istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel, gegner).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("K�nig w�rde im n�chsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein K�nig mehr da?!");
		}
		
		if(!ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist nicht besetzt.");
		
		if(!(gegner instanceof ISchlagbareFigur))
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");

		testeZug(ziel);
		
		if(!(gegner instanceof ISchlagbareFigur))
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");

		ISchlagbareFigur gegner2 = (ISchlagbareFigur) gegner;
		gegner2.setzeSollEntferntWerden();
		gegner2.geschlagenWerden();
		
		
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(true, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas ge�ndert hat
		setChanged();
		notifyObservers();
	}

	public boolean wurdeBewegt() throws NegativeConditionException {
		return schonBewegt;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		if(!gehoertSpieler().istZugberechtigt())
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		
		if(Partiezustand.getInstance().istRemis())
			throw new NegativePreConditionException("Partie ist Remis");
		
		if(Partiezustand.getInstance().istPatt())
			throw new NegativePreConditionException("Partie ist Patt");
		
		if(Partiezustand.getInstance().istSchachmatt())
			throw new NegativePreConditionException("Partie ist Schachmatt");
		
		if(istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("K�nig w�rde im n�chsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein K�nig mehr da?!");
		}
		
		if(!ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist nicht besetzt.");

		testeZug(ziel);
		
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(false, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas ge�ndert hat
		setChanged();
		notifyObservers();
	}

	public void testeZug(IFeld ziel) throws NegativePreConditionException {
		List<IFeld> zugfelder = null;
		try {
			if(position.gebeReihe().equals(ziel.gebeReihe()))
				zugfelder = Brett.getInstance().gebeFelderInReihe(position, ziel);
			else if(position.gebeLinie().equals(ziel.gebeLinie()))
				zugfelder = Brett.getInstance().gebeFelderInLinie(position, ziel);
			else
				zugfelder = Brett.getInstance().gebeFelderInDiagonalen(position, ziel);
		} catch (NegativeConditionException e) { }
		
		if(zugfelder == null || zugfelder.size() != 0) // bei 1-Feld-abstand gibts eine leereliste, kein null!
			throw new NegativePreConditionException("Ung�ltiges Zielfeld.");
		
		if(position.equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
	}
}
