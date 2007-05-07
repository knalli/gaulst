package schach.partie.internal;

import schach.brett.Farbe;
import schach.brett.internal.AlleFiguren;
import schach.partie.IPartie;
import schach.partie.IPartiehistorie;
import schach.partie.IPartiezustand;
import schach.spieler.ISpieler;
import schach.spieler.internal.Spieler;
import schach.system.ChessException;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;
import schach.system.View;


public class Partie implements IPartie {
	private static IPartie instance = null;
	private Farbe aktuelleFarbe = Farbe.WEISS;
	private ISpieler aktuellerSpieler = null;
	private ISpieler gegnerischerSpieler = null;
	private boolean istRemisAngebot = false;
	private ISpieler istRemisAngebotVon = null;
	
	private IPartiezustand zustand = null;
	private IPartiehistorie historie = null;
	
	private Partie() {
		// objekte sollten schon mal erstellt werden= = sorgt nur dafür, dass sie da sind.. ist sauberer
		zustand = Partiezustand.getInstance();
		historie = Partiehistorie.getInstance();
		
		aktuelleFarbe = Farbe.WEISS;
		aktuellerSpieler = Spieler.getInstance(aktuelleFarbe);
		gegnerischerSpieler = Spieler.getInstance(aktuelleFarbe.andereFarbe());
	}
	
	public static IPartie getInstance() {
		if(instance == null)
			instance = new Partie();
		
		return instance;
	}

	public ISpieler aktuellerSpieler() {
		return aktuellerSpieler;   
	}

	public boolean bieteRemisAn(ISpieler spieler) throws NegativeConditionException {
		if(!zustand.inPartie())
			throw new NegativePreConditionException("Es läuft keine Partie.");
	
		if (istRemisAngebotVon(aktuellerSpieler))
			throw new NegativePreConditionException("Es liegt bereits ein Remisangebot des gegnerischen Spielers vor.");
		
		istRemisAngebot = true;
		istRemisAngebotVon = spieler;
		return true;
	}

	public ISpieler gegnerischerSpieler() {
		return gegnerischerSpieler;
	}

	public void lehneRemisAb(ISpieler spieler) throws NegativeConditionException {
		if(!istRemisMoeglich() || !istRemisAngebotVon(gegnerischerSpieler)){
			throw new NegativePreConditionException("Remisablehnung: Kein Remis verfügbar."); 
		}
		istRemisAngebot=false;
	}

	public void nehmeRemisAn(ISpieler spieler) throws NegativeConditionException {
		if(!istRemisMoeglich() || !istRemisAngebotVon(gegnerischerSpieler)){
			throw new NegativePreConditionException("Remisannahme: Kein Remis verfügbar."); 
		}
		
		haltePartieAn();
	}

	private void haltePartieAn() throws NegativeConditionException {
		zustand.haltePartieAn();
	}

	public void start() {
		try {
			Logger.info("Figuren werden aufgestellt.");
			AlleFiguren.getInstance().stelleAlleFigurenAuf();
			Logger.info("Figuren wurden aufgestellt.");
			View.getView().update();
			
			aktuellerSpieler = Spieler.getInstance(Farbe.WEISS);
			gegnerischerSpieler = Spieler.getInstance(Farbe.SCHWARZ);
			
		} catch (ChessException e) {
			Logger.error("Ausnahme "+e.toString()+" aufgetreten."+e.getCause());
		}
	}

	public Farbe aktuelleFarbe() {
		return aktuelleFarbe;
	}
	
	public void wechsleSpieler() {
		aktuellerSpieler = gegnerischerSpieler;
		gegnerischerSpieler = Spieler.getInstance(aktuelleFarbe);
		aktuelleFarbe = aktuelleFarbe.andereFarbe();
	}

	public boolean istRemisAngebotVon(ISpieler spieler) throws NegativeConditionException {
		if(!zustand.inPartie())
			throw new NegativePreConditionException("Es läuft keine Partie");
		return spieler.equals(istRemisAngebotVon);
	}

	public boolean istRemisMoeglich() throws NegativeConditionException {
		return istRemisAngebot;
	}




}
