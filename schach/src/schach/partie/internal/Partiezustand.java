package schach.partie.internal;

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
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istRemis() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istRemisAngenommenVon(ISpieler spieler) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istRemisMoeglich() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istRemisangebotVon(ISpieler spieler) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean istSchachmatt() {
		// TODO Auto-generated method stub
		return false;
	}

	public void haltePartieAn() throws NegativeConditionException {
		if(!inpartie)
			throw new NegativePreConditionException("Partie kann nicht angehalten werden, da keine l�uft.");
		inpartie = false;
	}
}
