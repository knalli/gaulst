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
import schach.spieler.ISpieler;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Bauer implements IBauer {
	private boolean doppelschritt = false;
	private boolean sollentferntwerden = false;
	private AbstrakteFigur figur = null;

	public Bauer(Farbe farbe, IFeld feld) {
		figur = new AbstrakteFigur(farbe, feld, Figurart.BAUER);
	}
	
	public void entnehmen() throws NegativeConditionException {
		if(!gebePosition().gebeReihe().equals(Partie.getInstance().gegnerischerSpieler().gebeGrundreihe()))
			throw new NegativePreConditionException("Bauer steht nicht auf der gegnerischen Grundreihe.");
		
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Es besteht keine Bauerndumwandlung.");
	
		if(!istAufDemSchachbrett())
			throw new NegativePreConditionException("Figur steht nicht auf Schachbrett.");
		
		figur.setzeUmGrundposition(null);
		figur.speichereVorPosition();
		figur.setzeUmPosition(null);
	}

	public boolean letzteRundeDoppelschritt() {
		return doppelschritt;
	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		
		// b01
		if(!gehoertSpieler().istZugberechtigt()){
			Logger.test("B01 Spieler ist zugberechtigt = FALSE");	
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		}
		Logger.test("B01 Spieler ist zugberechtigt = TRUE");

		// b02
		if(Partiezustand.getInstance().istRemis()){
			Logger.test("B02 Partie ist Remis = TRUE");
			throw new NegativePreConditionException("Partie ist Remis");
		}
		Logger.test("B02 Partie ist Remis  = FALSE");
		
		// b03
		if(Partiezustand.getInstance().istPatt()) {
			Logger.test("B03 Partie ist Patt = TRUE");
			throw new NegativePreConditionException("Partie ist Patt");
		}
		Logger.test("B03 Partie ist Patt = FALSE");
		
		// b04
		if(Partiezustand.getInstance().istSchachmatt()){
			Logger.test("B04 Partie ist Matt = TRUE");
			throw new NegativePreConditionException("Partie ist Schachmatt");
		}
		Logger.test("B04 Partie ist Matt = FALSE");
		
		// b05
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, gebeFarbe()).get(0));
		if(koenig.istInEinerRochade()){
			Logger.test("B05 Koenig ist in einer Rochade = TRUE");
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		}
		Logger.test("B05 Koenig ist in einer Rochade = FALSE");
		
		// b06
		if(Brett.getInstance().istBauernUmwandlung()){
			Logger.test("B06 eine Bauernumwandlung steht an = TRUE");
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		}
		Logger.test("B06 eine Bauernumwandlung steht an = FALSE");
		
		// b07
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe())){
				Logger.test("B07 Koenig wird im naechsten Zug bedroht = TRUE");
				throw new NegativePreConditionException("Kˆnig w¸rde im n‰chsten Zug im Schach stehen.");
			}
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein Kˆnig mehr da?!");
		}
		Logger.test("B07 Koenig wird im naechsten Zug bedroht = FALSE");
		
		testeSchlagZug(ziel);
		
		// b08
		if(!ziel.istBesetzt()) {
			Logger.test("B08 Zielfed ist besetzt = TRUE");
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist nicht besetzt.");
		}
		Logger.test("B08 Zielfed ist besetzt = FALSE");
		
		// b09
		if(!(gegner instanceof ISchlagbareFigur)){
			Logger.test("B09 Figur ist nicht schlagbar = TRUE");
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");
		}
		Logger.test("B09 Figur ist nicht schlagbar = FALSE");
		
		// b10
		if(gegner.gebeFarbe().equals(gebeFarbe())) {
			Logger.test("B10 zu schlagende Figur hat eigene Farbe  = TRUE");
			throw new NegativePreConditionException("Zu schlagende Figur gehört nicht dem gegnerischen Spieler.");
		}
		Logger.test("B10 zu schlagende Figur hat eigene Farbe  = FALSE");
		
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

		if(!Brett.getInstance().istBauernUmwandlung()){
			Partiehistorie.getInstance().protokolliereStellung(true, this);
			Partie.getInstance().wechsleSpieler();
		}
		
//		informiere die Beobachter, dass sich etwas geändert hat
		figur.erzwingeUpdate();
	}

	public void schlaegtEnPassant(IFeld ziel)
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
			
			IFigur gegner = Brett.getInstance().gebeFigurVonFeld(ziel.minusReihe(1));
			
			if(!(gegner instanceof IBauer))
				throw new NegativePreConditionException("Zu schlagende Figur ist kein Bauer.");
			
			if(gegner.gebeFarbe().equals(gebeFarbe()))
				throw new NegativePreConditionException("Zu schlagende Figur gehört nicht dem gegnerischen Spieler.");

			IBauer gegner2 = (IBauer) gegner;
			
//			simuliere Stellung
			try {
				if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe()))
					throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
			} catch (IndexOutOfBoundsException e) {
				throw new NegativePreConditionException("Upps, kein König mehr da?!");
			}
			
//			if(!gebePosition().plusReihe(1).minusLinie(1).equals(ziel) && !gebePosition().plusReihe(1).plusLinie(1).equals(ziel))
//				throw new NegativePreConditionException("Ungültiges Zielfeld");
			testeSchlagZug(ziel);

			if(ziel.istBesetzt())
				throw new NegativePreConditionException("Schlagzug: Zielfeld ist besetzt.");

			
			if(!gegner2.letzteRundeDoppelschritt())
				throw new NegativePreConditionException("Zu schlagender Bauer hat in der letzten Runde keinen Doppelschritt gemacht.");
			
			gegner2.setzeSollEntferntWerden();
			gegner2.geschlagenWerden();
			
			figur.gebePosition().istBesetzt(false);
			figur.speichereVorPosition();
			figur.setzeUmPosition(ziel);
			figur.gebePosition().istBesetzt(true);	
			
			if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler()))
				Partie.getInstance().lehneRemisAb(gehoertSpieler());
			
//			per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
			for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
				((IBauer) fig).letzteRundeDoppelschritt(false);
			}
			
			if(!Brett.getInstance().istBauernUmwandlung()){
				Partiehistorie.getInstance().protokolliereStellung(true, this);
				Partie.getInstance().wechsleSpieler();
			}		
			
//			informiere die Beobachter, dass sich etwas geändert hat
			figur.erzwingeUpdate();
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		boolean macheEinenDoppelschritt = false;
		
		// b01
		if(!gehoertSpieler().istZugberechtigt()){
			Logger.test("B01 Spieler ist zugberechtigt = FALSE");
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		}
		Logger.test("B01 Spieler ist zugberechtigt = TRUE");	
		
		// b02
		if(Partiezustand.getInstance().istRemis()){
			Logger.test("B02 Partie ist Remis = TRUE");
			throw new NegativePreConditionException("Partie ist Remis");
		}
		Logger.test("B02 Partie ist Remis  = FALSE");
		
		// b03
		if(Partiezustand.getInstance().istPatt()) {
			Logger.test("B03 Partie ist Patt = TRUE");
			throw new NegativePreConditionException("Partie ist Patt");
		}
		Logger.test("B03 Partie ist Patt = FALSE");
		
		// b04
		if(Partiezustand.getInstance().istSchachmatt()){
			Logger.test("B04 Partie ist Matt = TRUE");
			throw new NegativePreConditionException("Partie ist Schachmatt");
		}
		Logger.test("B04 Partie ist Matt = FALSE");
		
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, gebeFarbe()).get(0));
		
		// b05
		if(koenig.istInEinerRochade()){
			Logger.test("B05 Koenig ist in einer Rochade = TRUE");
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		}
		Logger.test("B05 Koenig ist in einer Rochade = FALSE");
		
		// b06
		if(Brett.getInstance().istBauernUmwandlung()){
			Logger.test("B06 eine Bauernumwandlung steht an = TRUE");
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		}
		Logger.test("B06 eine Bauernumwandlung steht an = FALSE");
		
//		simuliere Stellung
		// b07
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel).istKoenigBedroht(gebeFarbe())){
				Logger.test("B07 Koenig wird im naechsten Zug bedroht = TRUE");
				throw new NegativePreConditionException("Koenig wuerde im naechsten Zug im Schach stehen.");
			}
				
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein Koenig mehr da?!");
		}
		Logger.test("B07 Koenig wird im naechsten Zug bedroht = FALSE");
		
//		if(!position.plusReihe(1).equals(ziel) && !(position.plusReihe(2).equals(ziel) && !doppelschritt))
//			throw new NegativePreConditionException("Ungültiges Ziel");
		testeZiehZug(ziel);
		
		// b08
		if(ziel.istBesetzt()){
			Logger.test("B08 Zielfed ist besetzt = TRUE");
			throw new NegativePreConditionException("Ziel ist besetzt");
		}
		Logger.test("B08 Zielfed ist besetzt = FALSE");
			
			
		boolean dps = false;
		try {
			dps = gebePosition().plusReihe(2).equals(ziel);
		} catch (NegativePreConditionException e) {}
		if(dps && !wurdeBewegt()) {
			// rest in testeZug geprueft
			macheEinenDoppelschritt = true;
		}
			
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
		
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partiehistorie.getInstance().protokolliereStellung(false, this);
			Partie.getInstance().wechsleSpieler();
		}		
		
//		dieser Bauer hat doch einen Doppelschritt gemacht?
		doppelschritt = macheEinenDoppelschritt;
		
//		informiere die Beobachter, dass sich etwas geändert hat
		figur.erzwingeUpdate();
	}

	public void geschlagenWerden() throws NegativeConditionException {
		if(!sollentferntwerden || !istAufDemSchachbrett()){
			throw new NegativePreConditionException("Figur darf nicht entfernt werden.");
		}
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(null);
//		grundposition = null;
		
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
		return !istAufGrundposition();
	}

	public void setzeSollEntferntWerden() {
		sollentferntwerden = true;
	}

	public void testeZug(IFeld ziel) throws NegativeConditionException  {
		try {
			testeZiehZug(ziel);
		} catch (NegativeConditionException e) {
			testeSchlagZug(ziel);
		}
	}
	
	private void testeZiehZug(IFeld ziel) throws NegativeConditionException{
		if(gebePosition().equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		
		boolean gueltig = false;
		try {
			gueltig = gebePosition().plusReihe(1).equals(ziel) && !ziel.istBesetzt();
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || (gebePosition().plusReihe(2).equals(ziel) && !wurdeBewegt() && !ziel.istBesetzt());
		} catch(NegativeConditionException e){}
		
		if(!gueltig)
			throw new NegativePreConditionException("Ungueltige Gangart (Ziehen).");
		
		boolean dps = false;
		try {
			dps = gebePosition().plusReihe(2).equals(ziel);
		} catch (NegativePreConditionException e) {}
		if(dps && !wurdeBewegt()) {
			if(gebePosition().plusReihe(1).istBesetzt() || ziel.istBesetzt())
				throw new NegativePreConditionException("Ziel oder Zugweg ist besetzt.");
		}
	}
	
	private void testeSchlagZug(IFeld ziel) throws NegativeConditionException{
		if(gebePosition().equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		
		boolean gueltig = false;
		try {
			gueltig = gebePosition().plusReihe(1).minusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || gebePosition().plusReihe(1).plusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		
		if(!gueltig)
			throw new NegativePreConditionException("Ungültige Gangart (schlagen).");
		
		if(!ziel.istBesetzt()){
			IFigur potbauer = Brett.getInstance().gebeFigurVonFeld(ziel.minusReihe(1));
			if(ziel.minusReihe(1).istBesetzt() && potbauer != null && potbauer.gebeArt().equals(Figurart.BAUER) && ((IBauer)potbauer).letzteRundeDoppelschritt()){
				// alles okay
			}
			else 
				throw new NegativePreConditionException("Erkannter Enpassent Schlagzug: Kein Bauer auf ep-Feld.");
		}
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
		throw new NegativePreConditionException("Für den Bauern ist dieser Zug nicht möglich.");
	}

	public void simuliereBrettzug(IFeld start) {
		figur.simuliereBrettzug(start);
	}
	
	public String toString(){
		return figur.toString();
	}
}
