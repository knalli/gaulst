package schach.partie.internal;

import schach.brett.Farbe;
import schach.brett.internal.AlleFiguren;
import schach.partie.IPartie;
import schach.partie.IPartiehistorie;
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
	
	private Partiezustand zustand = null;
	private IPartiehistorie historie = null;
	
	private Partie() {
		restart();
	}
	
	public static IPartie getInstance() {
		if(instance == null)
			instance = new Partie();
		
		return instance;
	}

	public ISpieler aktuellerSpieler() {
		return aktuellerSpieler;   
	}

	public void bieteRemisAn(ISpieler spieler) throws NegativeConditionException {
		if(!zustand.inPartie())
			throw new NegativePreConditionException("Es läuft keine Partie.");
	
		if (zustand.istRemisMoeglich())
			throw new NegativePreConditionException("Es liegt bereits ein Remisangebot vor.");
		
		zustand.setzeRemisVon(spieler);
	}

	public ISpieler gegnerischerSpieler() {
		return gegnerischerSpieler;
	}

	public void lehneRemisAb(ISpieler spieler) throws NegativeConditionException {
		if(zustand.istRemisAngebotVon(spieler))
			throw new NegativePreConditionException("Das Remis kann nicht abgelehnt werden."); 
		
		zustand.setzeRemisVon(null);
	}

	public void nehmeRemisAn(ISpieler spieler) throws NegativeConditionException {
		if(zustand.istRemisAngebotVon(spieler))
			throw new NegativePreConditionException("Das Remis kann nicht angenommen werden."); 
	
		zustand.setzeRemisVon(spieler);
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

	public void setzeFarbe(Farbe farbe) {
		if(historie.istEineSimulation()){
			aktuelleFarbe = farbe;
			aktuellerSpieler = Spieler.getInstance(farbe);
			gegnerischerSpieler = Spieler.getInstance(farbe.andereFarbe());
		}
	}
	
	public void restart() {
//		 objekte sollten schon mal erstellt werden= = sorgt nur dafür, dass sie da sind.. ist sauberer
		zustand = (Partiezustand) Partiezustand.getInstance();
		historie = Partiehistorie.getInstance();
		
		aktuelleFarbe = Farbe.WEISS;
		aktuellerSpieler = Spieler.getInstance(aktuelleFarbe);
		gegnerischerSpieler = Spieler.getInstance(aktuelleFarbe.andereFarbe());
	}
}
