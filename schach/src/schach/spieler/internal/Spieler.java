package schach.spieler.internal;

import schach.brett.Farbe;
import schach.brett.Reihe;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;

public class Spieler implements ISpieler {
	private Farbe farbe;
	private Reihe reihe;
	private static ISpieler instanceWEISS = null;
	private static ISpieler instanceSCHWARZ = null;
	
	public static ISpieler getInstance(Farbe farbe){
		if(instanceWEISS == null){
			instanceWEISS = new Spieler(Farbe.WEISS, Reihe.R1);
			instanceSCHWARZ = new Spieler(Farbe.SCHWARZ, Reihe.R8);
		}
		
		if(farbe.equals(Farbe.WEISS)){
			return instanceWEISS;
		}
		else {
			return instanceSCHWARZ;
		}
	}
		
	private Spieler(Farbe farbe, Reihe reihe) {
		this.farbe = farbe;
		this.reihe = reihe;
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
		return Partiezustand.getInstance().inPartie() && Partie.getInstance().aktuellerSpieler().equals(this);
	}
	
	public boolean equals(Spieler spieler){
		return farbe.equals(spieler.gebeFarbe());
	}

	public Reihe gebeGrundreihe() {
		return reihe;
	}
	
	public String toString() {
		return "Spieler "+farbe;
	}
}
