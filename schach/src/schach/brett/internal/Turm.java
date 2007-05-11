package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.brett.ITurm;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Turm extends AbstrakteFigur implements ITurm {

	private boolean schonBewegt = false;
	private boolean sollentferntwerden;

	public Turm(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.TURM);
	}
	
	protected Turm(IFigur figur) {
		super(figur);
	}
	
	public void rochiert(IFeld ziel) throws NegativeConditionException {
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
		
		if (!koenig.istInEinerRochade())
			throw new NegativePreConditionException("König muss in einer Rochade stehen.");
		
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);
		
		schonBewegt = true;
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
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel, gegner).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
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

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(true, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
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
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		if(ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist besetzt.");
		
		testeZug(ziel);
		
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(false, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
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
	
	public void setzeSollEntferntWerden() {
		sollentferntwerden = true;
	}

	public void testeZug(IFeld ziel) throws NegativeConditionException {
		List<IFeld> zugfelder = null;
		try {
			if(position.gebeReihe().equals(ziel.gebeReihe()))
				zugfelder = Brett.getInstance().gebeFelderInReihe(position, ziel);
			else if(position.gebeLinie().equals(ziel.gebeLinie()))
				zugfelder = Brett.getInstance().gebeFelderInLinie(position, ziel);
		} catch (NegativePreConditionException e) { }
		
		if(zugfelder == null) // bei 1-Feld-abstand gibts eine leereliste, kein null!
			throw new NegativePreConditionException("Ungültiges Zielfeld");
		
		if(position.equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		
		if(!Brett.getInstance().sindAlleFelderFrei(zugfelder))
			throw new NegativePreConditionException("Der Zugweg ist nicht frei.");
	}
}
