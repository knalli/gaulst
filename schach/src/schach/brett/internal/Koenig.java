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
import schach.brett.Linie;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Koenig implements IKoenig {

	private boolean schonBewegt = false;
	private boolean istineinerrochade = false;
	private AbstrakteFigur figur = null;

	public Koenig(Farbe farbe, IFeld feld) {
		figur = new AbstrakteFigur(farbe, feld, Figurart.KOENIG);
	}
	
	public boolean istBedroht() {
		for(IFigur figur : AlleFiguren.getInstance().gebeAlleFiguren()){
			if(!figur.gebeFarbe().equals(gebeFarbe()) && figur.istAufDemSchachbrett()){
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
	
	public void setzeIstInEinerRochade(boolean status){
		istineinerrochade = status;
	}

	public void rochiert(IFeld ziel) throws NegativeConditionException {
		//b01
		if(!gehoertSpieler().istZugberechtigt()){
			Logger.test("B01 gehoertSpieler.istZugberechtigt = FALSE");
			throw new NegativePreConditionException("Spieler dieser Figur ist nicht zugberechtigt.");
		}
		Logger.test("B01 gehoertSpieler().istZugberechtigt() = TRUE");
		
		//b02
		if(Partiezustand.getInstance().istRemis()){
			Logger.test("B02 istRemis = TRUE");
			throw new NegativePreConditionException("Partie ist Remis");
		}
		Logger.test("B02 istRemis = FALSE");
		
		//b03
		if(Partiezustand.getInstance().istPatt()){
			Logger.test("B03 istPatt = TRUE");
			throw new NegativePreConditionException("Partie ist Patt");
		}
		Logger.test("B03 istPatt = FALSE");
		
		//b04
		if(Partiezustand.getInstance().istSchachmatt()){
			Logger.test("B04 istMatt = TRUE");
			throw new NegativePreConditionException("Partie ist Schachmatt");
		}
		Logger.test("B04 istMatt = FALSE");
		
		//b05
		if(istInEinerRochade()){
			Logger.test("B05 istInEinerRochade = TRUE");
			throw new NegativePreConditionException("Koenig ist in einer Rochade");
		}
		Logger.test("B05 istInEinerRochade = FALSE");
		
		//b06
		if(Brett.getInstance().istBauernUmwandlung()){
			Logger.test("B06 istBauernUmwandlung = TRUE");
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		}
		Logger.test("B06 istBauernUmwandlung = FALSE");
		
		//b07
		if(wurdeBewegt()){
			Logger.test("B07 Koenig.wurdeBewegt = TRUE");
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Köenig bewegt wurde.");
		}
		Logger.test("B07 Koenig.wurdeBewegt = FALSE");
		
		//b08
		try {
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel).istKoenigBedroht(gebeFarbe())){
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
			}
			Logger.test("B08 istKoenigBedroht = FALSE");
		} catch (IndexOutOfBoundsException e) {
			Logger.test("B08 Koenig.wurdeBewegt = UNKNOWN");
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		//b09
		boolean gueltig = false;
		try {
			//b09a
			gueltig = gebePosition().minusLinie(2).equals(ziel);
			Logger.test("B09 gueltigesZielfeld links = TRUE");
			Logger.test("B09 gueltigesZielfeld rechts = IRRELEVANT");
		} catch(NegativeConditionException e){
			Logger.test("B09 gueltigesZielfeld links = FALSE");
		}
		try {
			//b09b
			gueltig = gueltig || gebePosition().plusLinie(2).equals(ziel);
			Logger.test("B09 gueltigesZielfeld rechts = TRUE");
		} catch(NegativeConditionException e){
			Logger.test("B09 gueltigesZielfeld rechts = FALSE");
		}
		
		//b09final
		if(!gueltig){
			Logger.test("B09 gueltigesZielfeld = FALSE");
			throw new NegativePreConditionException("Ungültiges Zielfeld (Rochade).");
		}
		Logger.test("B09 gueltigesZielfeld = TRUE");
		
		IFeld turmfeld = null;
		IFeld turmzielfeld = null;
		//folge von 09
		if(ziel.gebeLinie().equals(Linie.G)){
			//ns01
			turmfeld = Brett.getInstance().gebeFeld(gebePosition().gebeReihe(), Linie.H);
		}
		else {
			//ns02
			turmfeld = Brett.getInstance().gebeFeld(gebePosition().gebeReihe(), Linie.A);
		}
		//folge von 09
		if(gebePosition().minusLinie(2).equals(ziel)){
			//ns03
			turmzielfeld = gebePosition().minusLinie(1);
		}
		else {
			//ns04
			turmzielfeld = gebePosition().plusLinie(1);
		}
		
		//b11
		//ns05
		IFigur figt = Brett.getInstance().gebeFigurVonFeld(turmfeld);
		if(figt == null){
			Logger.test("B11 Turmfeld ist besetzt = FALSE");
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Turm nicht mehr vorhanden.");
		}
		Logger.test("B04 Turmfeld ist besetzt = TRUE");
		
		//b12
		if(!figt.gebeArt().equals(Figurart.TURM)){
			Logger.test("B12 Turmfeld ist mit Turm besetzt = FALSE");
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Roachdepartner kein Turm ist.");
		}
		Logger.test("B12 Turmfeld ist mit Turm besetzt = TRUE");
		
		//b13 eigentlich irrelevant, weil dann es ein turm sein muss (s.o.) und der könig dann bedroht wäre (s.o.)!
		if(!figt.gehoertSpieler().istZugberechtigt())
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Roachdepartner kein Turm des Spielers ist.");
		
		// nur casting
		ITurm turm = (ITurm) figt;
		//b14
		if(turm.wurdeBewegt()){
			Logger.test("B14 Turm wurde nicht bewegt = FALSE");
			throw new NegativePreConditionException("Rochade nicht erlaubt, da Turm bewegt wurde.");
		}
		Logger.test("B14 Turm wurde nicht bewegt = TRUE");
		
		//ns06
		List<IFeld> zugweg = Brett.getInstance().gebeFelderInReihe(gebePosition(), turmfeld);
		//b15
		if(!Brett.getInstance().sindAlleFelderFrei(zugweg)){
			Logger.test("B15 Felder zwischen König und Turm sind frei = FALSE");
			throw new NegativePreConditionException("Die Felder zwischen König und Turm sind nicht frei.");
		}
		Logger.test("B15 Felder zwischen König und Turm sind frei = TRUE");
		
		//ns07
		zugweg.remove(ziel);
		//b16
		for(IFeld feld : zugweg){
			try {
				if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), feld).istKoenigBedroht(gebeFarbe())){
					Logger.test("B16 Zugweg des Königs sind unbedroht = FALSE");
					throw new NegativePreConditionException("König würde während der Roachde im Schach stehen.");
				}
			} catch (IndexOutOfBoundsException e) {
				throw new NegativePreConditionException("Upps, kein König mehr da?!");
			}
		}
		Logger.test("B16 Zugweg des Kˆnigs ist unbedroht = TRUE");
		
		
		//ns08-ns13
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(ziel);
		figur.gebePosition().istBesetzt(true);	
		schonBewegt = true;
		istineinerrochade = true;		
		
		//b17
		if(!Partiezustand.getInstance().istRemisAngebotVon(gehoertSpieler())){
			Logger.test("B17 kein Remisangebot des Spielers = TRUE");
			//ns14a
			Partie.getInstance().lehneRemisAb(gehoertSpieler());
		}else 
			Logger.test("B17 kein Remisangebot des Spielers = FALSE");
		
		//ns14,15
		turm.rochiert(turmzielfeld);
		istineinerrochade = false;
		
		//ns16a-h
		Partiehistorie.getInstance().protokolliereStellung(false, this); //rochade!
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, gebeFarbe())) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		//ns17
		Partie.getInstance().wechsleSpieler();
		
//		informiere die Beobachter, dass sich etwas geändert hat
		//ns18
		erzwingeUpdate();
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
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel, gegner).istKoenigBedroht(gebeFarbe()))
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
		
		if(gegner.gebeFarbe().equals(gebeFarbe()))
			throw new NegativePreConditionException("Zu schlagende Figur gehört nicht dem gegnerischen Spieler.");

		ISchlagbareFigur gegner2 = (ISchlagbareFigur) gegner;
		gegner2.setzeSollEntferntWerden();
		gegner2.geschlagenWerden();
		
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(ziel);
		figur.gebePosition().istBesetzt(true);	
		schonBewegt = true;		
		
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
			if(Partiehistorie.getInstance().simuliereStellung(gebePosition(), ziel).istKoenigBedroht(gebeFarbe()))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
		if(ziel.istBesetzt())
			throw new NegativePreConditionException("Zielfeld ist besetzt.");

		testeZug(ziel);
		
		figur.gebePosition().istBesetzt(false);
		figur.speichereVorPosition();
		figur.setzeUmPosition(ziel);
		figur.gebePosition().istBesetzt(true);	
		schonBewegt = true;		
		
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

	public void testeZug(IFeld ziel) throws NegativePreConditionException {
		List<IFeld> zugfelder = null;
		try {
			if(gebePosition().gebeReihe().equals(ziel.gebeReihe()))
				zugfelder = Brett.getInstance().gebeFelderInReihe(gebePosition(), ziel);
			else if(gebePosition().gebeLinie().equals(ziel.gebeLinie()))
				zugfelder = Brett.getInstance().gebeFelderInLinie(gebePosition(), ziel);
			else
				zugfelder = Brett.getInstance().gebeFelderInDiagonalen(gebePosition(), ziel);
		} catch (NegativeConditionException e) { }
		
		if(zugfelder == null || zugfelder.size() != 0) // bei 1-Feld-abstand gibts eine leereliste, kein null!
			throw new NegativePreConditionException("Ungültiges Zielfeld.");
		
		if(gebePosition().equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
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
		throw new NegativePreConditionException("Für den König ist dieser Zug nicht möglich.");
	}

	public void simuliereBrettzug(IFeld start) {
		figur.simuliereBrettzug(start);
	}	
	public String toString(){
		return figur.toString();
	}
}
