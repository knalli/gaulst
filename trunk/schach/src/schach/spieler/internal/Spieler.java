package schach.spieler.internal;

import schach.brett.Farbe;
import schach.partie.internal.Partie;
import schach.spieler.ISpieler;

public class Spieler implements ISpieler {
	private Farbe farbe;
	
	public Spieler(Farbe farbe) {
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
		return this.gebeFarbe() == spieler.gebeFarbe();
	}

}
