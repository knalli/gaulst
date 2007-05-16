package schach.partie.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.partie.IPartiezustand;
import schach.partie.IStellung;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiezustand implements IPartiezustand {
	private static IPartiezustand instance = null;
	private boolean inpartie = false;
	private ISpieler istRemisAngenommenVon=null;
	private boolean istRemisMoeglich=false;
	private ISpieler istRemisangebotVon=null;
	
	
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
		List<IStellung> stellung = Partiehistorie.getInstance().gebeStellungen(1);
		return stellung.size() > 0 && stellung.get(0).istPatt();
	}

	public boolean istRemis() {
		List<IStellung> stellung = Partiehistorie.getInstance().gebeStellungen(1);
		return stellung.size() > 0 && stellung.get(0).istRemis();
	}

	public boolean istRemisAngenommenVon(ISpieler spieler) {
		return istRemisMoeglich() && istRemisAngenommenVon.equals(spieler);
	}

	public boolean istRemisMoeglich() {
		List<IStellung> stellung = Partiehistorie.getInstance().gebeStellungen(1);
		return istRemisMoeglich || (stellung.size() > 0 && stellung.get(0).istRemisMoeglich());
	}

	public boolean istRemisangebotVon(ISpieler spieler) {
		return istRemisMoeglich() && istRemisangebotVon.equals(spieler);
	}

	public boolean istSchachmatt() {
		List<IStellung> stellung = Partiehistorie.getInstance().gebeStellungen(1);
		return stellung.size() > 0 && stellung.get(0).istSchachmatt();
	}

	public void haltePartieAn() throws NegativeConditionException {
		if(!inpartie)
			throw new NegativePreConditionException("Partie kann nicht angehalten werden, da keine läuft.");
		inpartie = false;
	}

	public boolean istSchach(Farbe farbe) {
		List<IStellung> stellung = Partiehistorie.getInstance().gebeStellungen(1);
		return stellung.size() > 0 && stellung.get(0).istKoenigBedroht(farbe);
	}
}
