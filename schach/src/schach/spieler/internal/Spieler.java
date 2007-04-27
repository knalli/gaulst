package schach.spieler.internal;

import schach.brett.Farbe;
import schach.partie.internal.Partie;
import schach.spieler.ISpieler;

public class Spieler implements ISpieler {
	private Farbe farbe;
	private static ISpieler instanceWEISS = null;
	private static ISpieler instanceSCHWARZ = null;
	
	public static ISpieler getInstance(Farbe farbe){
		if(instanceWEISS == null){
			instanceWEISS = new Spieler(Farbe.WEISS);
			instanceSCHWARZ = new Spieler(Farbe.SCHWARZ);
		}
		
		if(farbe.equals(Farbe.WEISS)){
			return instanceWEISS;
		}
		else {
			return instanceSCHWARZ;
		}
	}
		
	private Spieler(Farbe farbe) {
		this.farbe = farbe;
	}
	
	/**
	 * Enthält die Farbe des Spielers
	 */
	public Farbe gebeFarbe() {
		return farbe;
	}

	/**
	 * Prüft, ob dieser Spieler zugberechtigt ist.
	 */
	public boolean istZugberechtigt() {
		return Partie.getInstance().aktuellerSpieler().equals(this);
	}
	
	public boolean equals(Spieler spieler){
		return this.gebeFarbe().equals(spieler.gebeFarbe());
	}

}
