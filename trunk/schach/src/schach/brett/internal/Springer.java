package schach.brett.internal;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.brett.ISpringer;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Springer implements ISpringer {

	private boolean sollentferntwerden;
	private AbstrakteFigur figur = null;

	public Springer(Farbe farbe, IFeld feld) {
		figur = new AbstrakteFigur(farbe, feld, Figurart.SPRINGER);
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
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, gebeFarbe()).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe()))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		if(!ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld "+ziel+" ist nicht besetzt.");
		
		if(!(gegner instanceof ISchlagbareFigur))
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");
		
		if(!(gegner instanceof ISchlagbareFigur))
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");
		
		testeZug(ziel);
		
		if(gegner.gebeFarbe().equals(gebeFarbe()))
			throw new NegativePreConditionException("Zu schlagende Figur gehört nicht dem gegnerischen Spieler.");

		ISchlagbareFigur gegner2 = (ISchlagbareFigur) gegner;
		gegner2.setzeSollEntferntWerden();
		gegner2.geschlagenWerden();
		
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(ziel);
		figur.gebePosition().istBesetzt(true);		
		
		if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler()))
			Partie.getInstance().lehneRemisAb(gehoertSpieler());

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(true, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
		erzwingeUpdate();
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
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, gebeFarbe()).get(0));
		if(koenig.istInEinerRochade())
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel).istKoenigBedroht(gebeFarbe()))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		if(ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist besetzt.");
		
		testeZug(ziel);
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(ziel);
		figur.gebePosition().istBesetzt(true);		
		
		if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler()))
			Partie.getInstance().lehneRemisAb(gehoertSpieler());

//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		Partiehistorie.getInstance().protokolliereStellung(false, this);
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
		erzwingeUpdate();
	}

	public void geschlagenWerden() throws NegativeConditionException {
		if(!sollentferntwerden || !istAufDemSchachbrett()){
			throw new NegativePreConditionException();
		}
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(null);
		figur.setzeUmGrundposition(null);
	}

	public boolean sollEntferntWerden() {
		return sollentferntwerden;
	}
	
	public void setzeSollEntferntWerden() {
		sollentferntwerden = true;
	}

	public void testeZug(IFeld ziel) throws NegativeConditionException {
//		Logger.debug("Teste Zugart für "+Partie.getInstance().aktuelleFarbe()+": "+this+" nach "+ziel);
		
		if(gebePosition().equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		
		boolean gueltigerZug = false;
		
		try {
			gueltigerZug = gebePosition().plusReihe(1).plusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltigerZug = gueltigerZug || gebePosition().plusReihe(1).minusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		
		try {
			gueltigerZug = gueltigerZug || gebePosition().minusReihe(1).plusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltigerZug = gueltigerZug || gebePosition().minusReihe(1).minusLinie(2).equals(ziel);
		} catch(NegativeConditionException e){}
		
		try {
			gueltigerZug = gueltigerZug || gebePosition().plusReihe(2).plusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltigerZug = gueltigerZug || gebePosition().plusReihe(2).minusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		
		try {
			gueltigerZug = gueltigerZug || gebePosition().minusReihe(2).plusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltigerZug = gueltigerZug || gebePosition().minusReihe(2).minusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
				
		if(!gueltigerZug){
			Logger.debug("Zugart ungültig.");
			throw new NegativePreConditionException("Ungültiges Zielfeld.");
		}
		Logger.debug("Zugart gültig.");
	}

	public void aufstellen(IFeld feld) throws NegativeConditionException {
		figur.aufstellen(feld);
	}

	public void erzwingeUpdate() {
		figur.erzwingeUpdate();
	}

	public Figurart gebeArt() {
		return figur.gebeArt();
	}

	public Farbe gebeFarbe() {
		return figur.gebeFarbe();
	}

	public IFeld gebeGrundposition() {
		return figur.gebeGrundposition();
	}

	public IFeld gebePosition() {
		return figur.gebePosition();
	}

	public IFeld gebeVorPosition() {
		return figur.gebeVorPosition();
	}

	public ISpieler gehoertSpieler() {
		return figur.gehoertSpieler();
	}

	public boolean istAufDemSchachbrett() {
		return figur.istAufDemSchachbrett();
	}

	public boolean istAufGrundposition() {
		return figur.istAufGrundposition();
	}

	public void positionieren(IFeld feld) throws NegativeConditionException {
		figur.positionieren(this, feld);
	}

	public void simuliereBrettzug(IFeld start) {
		figur.simuliereBrettzug(start);
	}
}
