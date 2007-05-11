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
import schach.system.Logger;
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
			throw new NegativePreConditionException("Bauer steht nicht auf der gegnerischen Grundreihe.");
		
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Es besteht keine Bauerndumwandlung.");
	
		if(!istAufDemSchachbrett())
			throw new NegativePreConditionException("Figur steht nicht auf Schachbrett.");
		
		grundposition = null;
		position.istBesetzt(false);
		position = null;
	}

	public boolean letzteRundeDoppelschritt() {
		return doppelschritt;
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
		
//		if(!gebePosition().plusReihe(1).minusLinie(1).equals(ziel) && !gebePosition().plusReihe(1).plusLinie(1).equals(ziel))
//			throw new NegativePreConditionException("Ungültiges Zielfeld");
		testeSchlagZug(ziel);
		
		if(!ziel.istBesetzt())
			throw new NegativePreConditionException("Schlagzug: Zielfeld ist nicht besetzt.");
		
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
		
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partiehistorie.getInstance().protokolliereStellung(true, this);
			Partie.getInstance().wechsleSpieler();
		}
		
//		informiere die Beobachter, dass sich etwas geändert hat
		setChanged();
		notifyObservers();
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
			
			IKoenig koenig = (IKoenig)(AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe).get(0));
			if(koenig.istInEinerRochade())
				throw new NegativePreConditionException("Koenig ist in einer Rochade");
			
			if(Brett.getInstance().istBauernUmwandlung())
				throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
			
			IFigur gegner = Brett.getInstance().gebeFigurVonFeld(ziel.minusReihe(1));
			
			if(!(gegner instanceof IBauer))
				throw new NegativePreConditionException("Zu schlagende Figur ist kein Bauer.");

			IBauer gegner2 = (IBauer) gegner;
			
//			simuliere Stellung
			try {
				if(Partiehistorie.getInstance().simuliereStellung(position, ziel, gegner).istKoenigBedroht(farbe))
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
			
			position.istBesetzt(false);
			position = ziel;
			position.istBesetzt(true);
//			per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
			for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
				((IBauer) fig).letzteRundeDoppelschritt(false);
			}
			
			if(!Brett.getInstance().istBauernUmwandlung()){
				Partiehistorie.getInstance().protokolliereStellung(true, this);
				Partie.getInstance().wechsleSpieler();
			}
			
//			informiere die Beobachter, dass sich etwas geändert hat
			setChanged();
			notifyObservers();
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
		
		if(Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Eine Bauernumwandlung steht an.");
		
//		simuliere Stellung
		try {
			if(Partiehistorie.getInstance().simuliereStellung(position, ziel).istKoenigBedroht(farbe))
				throw new NegativePreConditionException("König würde im nächsten Zug im Schach stehen.");
		} catch (IndexOutOfBoundsException e) {
			throw new NegativePreConditionException("Upps, kein König mehr da?!");
		}
		
//		if(!position.plusReihe(1).equals(ziel) && !(position.plusReihe(2).equals(ziel) && !doppelschritt))
//			throw new NegativePreConditionException("Ungültiges Ziel");
		testeZiehZug(ziel);
		
		if(ziel.istBesetzt())
			throw new NegativePreConditionException("Ziel ist besetzt");
			
		if(position.plusReihe(2).equals(ziel) && !wurdeBewegt()) {
			// rest in testeZug geprüft
			macheEinenDoppelschritt = true;
		}
			
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);
//		per se, alle Bauern haben erstmal keinen Doppelschritt gemacht (false positive ausschließen)
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		
		if(!Brett.getInstance().istBauernUmwandlung()){
			Partiehistorie.getInstance().protokolliereStellung(false, this);
			Partie.getInstance().wechsleSpieler();
		}
		
//		dieser Bauer hat doch einen Doppelschritt gemacht?
		doppelschritt = macheEinenDoppelschritt;
		
//		informiere die Beobachter, dass sich etwas geändert hat
		setChanged();
		notifyObservers();
	}

	public void geschlagenWerden() throws NegativeConditionException {
		if(!sollentferntwerden || !istAufDemSchachbrett()){
			throw new NegativePreConditionException("Figur darf nicht entfernt werden.");
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
		if(position.equals(ziel))
			throw new NegativePreConditionException("Zielfeld kann nicht Startfeld sein.");
		
		boolean gueltig = false;
		try {
			gueltig = position.plusReihe(1).equals(ziel);
		} catch(NegativeConditionException e){}
		try {
			gueltig = gueltig || (position.plusReihe(2).equals(ziel) && !wurdeBewegt());
		} catch(NegativeConditionException e){}
		
		if(!gueltig)
			throw new NegativePreConditionException("Ungültige Gangart (Ziehen).");
		
		if(position.plusReihe(2).equals(ziel) && !wurdeBewegt()) {
			if(position.plusReihe(1).istBesetzt() || ziel.istBesetzt())
				throw new NegativePreConditionException("Ziel oder Zugweg ist besetzt.");
		}
	}
	
	private void testeSchlagZug(IFeld ziel) throws NegativeConditionException{
		if(position.equals(ziel))
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
			if(ziel.minusReihe(1).istBesetzt() && (Brett.getInstance().gebeFigurVonFeld(ziel.minusReihe(1))) instanceof IBauer){
				// alles okay
			}
			else 
				throw new NegativePreConditionException("Erkannter Enpassent Schlagzug: Kein Bauer auf ep-Feld.");
		}
	}
}
