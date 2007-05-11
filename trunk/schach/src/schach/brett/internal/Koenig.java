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
import schach.brett.ITurm;
import schach.brett.Linie;
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
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
		if(wurdeBewegt())
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Köenig bewegt wurde.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		boolean gueltig = false;
		try {
			gueltig = position.minusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || position.plusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		
		if(!gueltig)
			throw new NegativePreConditionException("Ungültiges Zielfeld (Rochade).");
		
		IFeld turmfeld = null;
		IFeld turmzielfeld = null;
		if(ziel.gebeLinie().equals(Linie.G)){
			turmfeld = Brett.getInstance().gebeFeld(gebePosition().gebeReihe(), Linie.H);
		}
		else {
			turmfeld = Brett.getInstance().gebeFeld(gebePosition().gebeReihe(), Linie.A);
		}
		if(position.minusLinie(2).equals(ziel)){
			turmzielfeld = position.minusLinie(1);
		}
		else {
			turmzielfeld = position.plusLinie(1);
		}
		
		ITurm turm = (ITurm) Brett.getInstance().gebeFigurVonFeld(turmfeld);
		if(turm == null)
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Turm nicht mehr vorhanden.");
		if(turm.wurdeBewegt())
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Turm bewegt wurde.");
		
		List<IFeld> zugweg = Brett.getInstance().gebeFelderInReihe(gebePosition(), turmfeld);
		if(!Brett.getInstance().sindAlleFelderFrei(zugweg))
			throw new NegativePreConditionException("Die Felder zwischen König und Turm sind nicht frei.");
		
		zugweg.remove(ziel);
//		simuliere Stellung
		for(IFeld feld : zugweg){
			try {
				if(Partiehistorie.getInstance().simuliereStellung(position, feld).istKoenigBedroht(farbe))
					throw new NegativePreConditionException("König würde während der Roachde im Schach stehen.");
			} catch (IndexOutOfBoundsException e) {
				throw new NegativePreConditionException("Upps, kein König mehr da?!");
			}
		}
		
		
		position.istBesetzt(false);
		speichereVorPosition();
		position = ziel;
		position.istBesetzt(true);
		schonBewegt = true;
		istineinerrochade = true;
		
		turm.rochiert(turmzielfeld);
		istineinerrochade = false;
		
		Partiehistorie.getInstance().protokolliereStellung(false, this); //rochade!
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
		setChanged();
		notifyObservers();
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
		speichereVorPosition();
		position = ziel;
		position.istBesetzt(true);
		schonBewegt = true;

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
		
		if(istInEinerRochade())
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
			throw new NegativePreConditionException("Zielfeld ist besetzt.");

		testeZug(ziel);
		
		position.istBesetzt(false);
		speichereVorPosition();
		position = ziel;
		position.istBesetzt(true);
		schonBewegt = true;

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
			throw new NegativePreConditionException("Ungültiges Zielfeld.");
		
		if(position.equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
	}
}
