package schach.partie.internal;

import schach.brett.Farbe;
import schach.partie.IPartiezustand;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiezustand implements IPartiezustand {
	private static IPartiezustand instance = null;
	private boolean inpartie = false;
	
	private Partiezustand() {
		inpartie = true;
	}
	
	public static IPartiezustand getInstance() {
		if(instance == null)
			instance = new Partiezustand();
		
		return instance;
	}
	
	public boolean inPartie() {
		return inpartie;
	}

	public boolean istPatt() {
		// TODO Zustand#istPatt
		return false;
	}

	public boolean istRemis() {
		// TODO Zustand#istRemis
		return false;
	}

	public boolean istRemisAngenommenVon(ISpieler spieler) {
		// TODO Zustand#istRemisAngenommen
		return false;
	}

	public boolean istRemisMoeglich() {
		// TODO Zustand#istRemisMoeglich
		return false;
	}

	public boolean istRemisangebotVon(ISpieler spieler) {
		// TODO Zustand#istRemisangebotVon
		return false;
	}

	public boolean istSchachmatt() {
		// TODO Zustand#istSchachmatt
		return false;
	}

	public void haltePartieAn() throws NegativeConditionException {
		if(!inpartie)
			throw new NegativePreConditionException("Partie kann nicht angehalten werden, da keine läuft.");
		inpartie = false;
	}

	public boolean istSchach(Farbe farbe) {
		try {
			return Partiehistorie.getInstance().gebeStellungen(1).get(0).istKoenigBedroht(farbe);
		}
		catch(IndexOutOfBoundsException e){}
		return false;
	}
}
