package schach.partie.internal;

import schach.brett.Farbe;
import schach.brett.IFeld;
import schach.brett.internal.AlleFiguren;
import schach.partie.IPartie;
import schach.spieler.ISpieler;
import schach.spieler.internal.Spieler;
import schach.system.ChessException;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.View;


public class Partie implements IPartie {
	private static IPartie instance = null;
	private Farbe aktuelleFarbe = Farbe.WEISS;
	private ISpieler aktuellerSpieler = null;
	
	private Partie() {}
	
	public static IPartie getInstance() {
		if(instance == null)
			instance = new Partie();
		
		return instance;
	}

	public ISpieler aktuellerSpieler() {
		// TODO Auto-generated method stub
		return new Spieler(Farbe.WEISS);
	}

	public void bieteRemisAn(ISpieler spieler) throws NegativeConditionException {
		// TODO Auto-generated method stub
		
	}

	public ISpieler gegnerischerSpieler() {
		// TODO Auto-generated method stub
		return null;
	}

	public void lehneRemisAb(ISpieler spieler) throws NegativeConditionException {
		// TODO Auto-generated method stub
		
	}

	public void nehmeRemisAn(ISpieler spieler) throws NegativeConditionException {
		// TODO Auto-generated method stub
		
	}

	public void start() {
		try {
			Logger.info("Figuren werden aufgestellt.");
			AlleFiguren.getInstance().stelleAlleFigurenAuf();
			Logger.info("Figuren wurden aufgestellt.");
			View.getView().update();
		} catch (ChessException e) {
			Logger.error("Ausnahme "+e.toString()+" aufgetreten."+e.getCause());
		}
	}

	public Farbe aktuelleFarbe() {
		return aktuelleFarbe;
	}


	public boolean simuliereStellung(IFeld start, IFeld ziel) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setzeAktuellenSpieler() {
		// TODO Auto-generated method stub
		
	}
}
