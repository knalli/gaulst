package schach.partie.internal;

import schach.partie.IPartie;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;


public class Partie implements IPartie {
	private static IPartie instance = null;
	
	private Partie() {}
	
	public static IPartie getInstance() {
		if(instance == null)
			instance = new Partie();
		
		return instance;
	}

	public ISpieler aktuellerSpieler() {
		// TODO Auto-generated method stub
		return null;
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
		
	}
}
