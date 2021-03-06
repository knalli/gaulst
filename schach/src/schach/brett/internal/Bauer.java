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
import schach.brett.Linie;
import schach.brett.Reihe;

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
	
		//b01
		if(!gehoertSpieler().istZugberechtigt()){
			Logger.test("B01 gehoertSpieler.istZugberechtigt = FALSE -> Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		}
		Logger.test("B01 gehoertSpieler.istZugberechtigt = TRUE -> Exception wird NICHT ausgef�hrt!");
		//b02
		if(Partiezustand.getInstance().istRemis()){
			Logger.test("B02 istRemis = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Partie ist Remis");
		}
		Logger.test("B02 istRemis = FALSE ->Exception wird NICHT ausgef�hrt!");
		
		//b03
		if(Partiezustand.getInstance().istPatt()){
			Logger.test("B03 istPatt = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Partie ist Patt");
		}
		Logger.test("B03 istPatt = FALSE ->Exception wird NICHT ausgef�hrt!");
		//b04
		if(Partiezustand.getInstance().istSchachmatt()){
			Logger.test("B04 istMatt = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Partie ist Schachmatt");
		}
		Logger.test("B04 istMatt = FALSE ->Exception wird NICHT ausgef�hrt!");
		
		//b05
		IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, gebeFarbe()).get(0));
		if(koenig.istInEinerRochade()){
			Logger.test("B05 istInEinerRochade = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		}
		Logger.test("B05 istInEinerRochade = FALSE ->Exception wird NICHT ausgef�hrt!");
		//b06
		if(Brett.getInstance().istBauernUmwandlung()){
			Logger.test("B06 istBauernUmwandlung = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		}
		Logger.test("B06 istBauernUmwandlung = FALSE ->Exception wird NICHT ausgef�hrt!");
		//simuliere Stellung
		//b07
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe())){
				Logger.test("B07 ist k�nig bedroht = TRUE ->Exception wird ausgef�hrt!");
				throw new NegativePreConditionException("K�nig w�rde im n�chsten Zug im Schach stehen.");
			}
			Logger.test("B07 ist k�nig bedroht = FALSE ->Exception wird NICHT ausgef�hrt!");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein K�nig mehr da?!");
		}
		
		//b08
		if(!gebePosition().plusReihe(1).minusLinie(1).equals(ziel) && !gebePosition().plusReihe(1).plusLinie(1).equals(ziel)){
			Logger.test("B08 G�ltige Gangart = FALSE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Ung�ltiges Zielfeld");
		}
		Logger.test("B08 G�ltige Gangart = TRUE ->Exception wird NICHT ausgef�hrt!");
		testeSchlagZug(ziel);
		
		//b09
		if(!ziel.istBesetzt()){
			Logger.test("B09 ziel ist nicht Besetzt = TRUE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist nicht besetzt.");
		}
		Logger.test("B09 ziel ist nicht Besetzt = FALSE ->Exception wird NICHT ausgef�hrt!");
		
		//b10
		if(!(gegner instanceof ISchlagbareFigur)){
			Logger.test("B10 zu schlagende Figur Schlagbar = FALSE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Zu schlagende Figur ist nicht schlagbar.");
		}
		Logger.test("B10 zu schlagende Figur Schlagbar = TRUE ->Exception wird NICHT ausgef�hrt!");
		
		//b11
		if(gegner.gebeFarbe().equals(gebeFarbe())){
			Logger.test("B11 Farbe des Schlagenden == Farbed es Zuschlagende Figur = FALSE ->Exception wird ausgef�hrt!");
			throw new NegativePreConditionException("Zu schlagende Figur geh�rt nicht dem gegnerischen Spieler.");
		}
		Logger.test("B11 Farbe des Schlagenden == Farbed es Zuschlagende Figur = TRUE ->Exception wird NICHT ausgef�hrt!");
		
		
		ISchlagbareFigur gegner2 = (ISchlagbareFigur) gegner;
		//ns0
		gegner2.setzeSollEntferntWerden();
		Logger.test("Operation ns0 wird ausgef�hrt-> Gegner wird entfernt");
		//ns1
		gegner2.geschlagenWerden();
		Logger.test("Operation ns1 wird ausgef�hrt-> Gegner wird endg�ltig geschlagen");
		//ns2
		figur.gebePosition().istBesetzt(false);
		//ns3
		figur.speichereVorPosition();
		//ns3
		figur.setzeUmPosition(ziel);
		//ns4
		figur.gebePosition().istBesetzt(true);
		Logger.test("Operation ns2 - ns4 wird ausgef�hrt-> Figur wird umpositioniert und besetzt das Neue Feld");
		
		if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler()))
			Partie.getInstance().lehneRemisAb(gehoertSpieler());
		
		//per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		if(!Brett.getInstance().istBauernUmwandlung()){
			
			Partiehistorie.getInstance().protokolliereStellung(true, this);
			Partie.getInstance().wechsleSpieler();
		}
		
		//informiere die Beobachter, dass sich etwas ge�ndert hat
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
				throw new NegativePreConditionException("Zu schlagende Figur geh�rt nicht dem gegnerischen Spieler.");

			IBauer gegner2 = (IBauer) gegner;
			
//			simuliere Stellung
			try {
				if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe()))
					throw new NegativePreConditionException("K�nig w�rde im n�chsten Zug im Schach stehen.");
			} catch (IndexOutOfBoundsException e) {
				throw new NegativePreConditionException("Upps, kein K�nig mehr da?!");
			}
			
//			if(!gebePosition().plusReihe(1).minusLinie(1).equals(ziel) && !gebePosition().plusReihe(1).plusLinie(1).equals(ziel))
//				throw new NegativePreConditionException("Ung�ltiges Zielfeld");
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
			
//			per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
			for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
				((IBauer) fig).letzteRundeDoppelschritt(false);
			}
			
			if(!Brett.getInstance().istBauernUmwandlung()){
				Partiehistorie.getInstance().protokolliereStellung(true, this);
				Partie.getInstance().wechsleSpieler();
			}		
			
//			informiere die Beobachter, dass sich etwas ge�ndert hat
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
		
		// b08
		try{
			testeZiehZug(ziel);
		} catch (NegativePreConditionException e) {
			Logger.test("B08 Teste Zug gueltig = FALSE");
			throw new NegativePreConditionException("ZiehZug nicht in Ordnung");			
		}
		Logger.test("B08 Teste Zug gueltig = TRUE");
		
		
		// b09
		if(ziel.istBesetzt()){
			
			Logger.test("B09 Zielfed ist besetzt = TRUE");
			throw new NegativePreConditionException("Ziel ist besetzt");
		}
		Logger.test("B09 Zielfed ist besetzt = FALSE");
			
			
		boolean dps = false;
		try {
			dps = gebePosition().plusReihe(2).equals(ziel);
		} catch (NegativePreConditionException e) {}
		if(dps && !wurdeBewegt()) {
			// rest in testeZug geprueft
			macheEinenDoppelschritt = true;
		}
			
		//ns0
		figur.gebePosition().istBesetzt(false);
		//ns1
		figur.speichereVorPosition();
		//ns2
		figur.setzeUmPosition(ziel);
		//ns3
		figur.gebePosition().istBesetzt(true);
		Logger.test("Operation ns0 - ns3 wird ausgef�hrt-> Figur wird umpositioniert und besetzt das Neue Feld");	
		
		if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler()))
			Partie.getInstance().lehneRemisAb(gehoertSpieler());
		
//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschlie�en)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partiehistorie.getInstance().protokolliereStellung(false, this);
			Partie.getInstance().wechsleSpieler();
		}		
		
//		dieser Bauer hat doch einen Doppelschritt gemacht?
		doppelschritt = macheEinenDoppelschritt;
		
//		informiere die Beobachter, dass sich etwas ge�ndert hat
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
		//Logger.test("f�hre aus: zieht nach "+ziel);
		//Logger.test("figur auf C3: "+ Brett.getInstance().gebeFigurVonFeld(Brett.getInstance().gebeFeld(Reihe.R3, Linie.C)));
		if(gebePosition().equals(ziel)){
		//	Logger.test("B09 Zielfeld != Startfeld = FALSE");
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		}
		//Logger.test("B09 Zielfeld != Startfeld = TRUE");
		
		boolean gueltig = false;
		try {
			gueltig = gebePosition().plusReihe(1).equals(ziel) && !ziel.istBesetzt();
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || (gebePosition().plusReihe(2).equals(ziel) && !wurdeBewegt() && !ziel.istBesetzt());
		} catch(NegativeConditionException e){}
		
		if(!gueltig){
		//	Logger.test("B10 Gangart ist gueltig = FALSE");
			throw new NegativePreConditionException("Ungueltige Gangart (Ziehen).");
		}
		//Logger.test("B10 Gangart ist gueltig = TRUE");
		
		boolean dps = false;
		try {
			dps = gebePosition().plusReihe(2).equals(ziel);
		} catch (NegativePreConditionException e) {}
		if(dps && !wurdeBewegt()) {
			if(gebePosition().plusReihe(1).istBesetzt() || ziel.istBesetzt()){
			//	Logger.test("B11 Ziel und Zugweg sind frei = FALSE");	
				throw new NegativePreConditionException("Ziel oder Zugweg ist besetzt.");
			}
			//Logger.test("B11 Ziel und Zugweg sind frei = TRUE");
		}
	}
	
	private void testeSchlagZug(IFeld ziel) throws NegativeConditionException{
		if(gebePosition().equals(ziel)){
		//	Logger.test("B11 Zielfeld != Startfeld = FALSE");
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		}
		//Logger.test("B11 Zielfeld != Startfeld = TRUE");
			
		
		boolean gueltig = false;
		try {
			gueltig = gebePosition().plusReihe(1).minusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || gebePosition().plusReihe(1).plusLinie(1).equals(ziel);
		} catch(NegativeConditionException e){}
		
		if(!gueltig){
		//	Logger.test("B12 Gangart ist gueltig = FALSE");
			throw new NegativePreConditionException("Ung�ltige Gangart (schlagen).");
		}
		//Logger.test("B12 Gangart ist gueltig = TRUE");
		
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
		throw new NegativePreConditionException("F�r den Bauern ist dieser Zug nicht m�glich.");
	}

	public void simuliereBrettzug(IFeld start) {
		figur.simuliereBrettzug(start);
	}
	
	public String toString(){
		return figur.toString();
	}
}
