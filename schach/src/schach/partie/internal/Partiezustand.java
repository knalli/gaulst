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
	private boolean istRemisAngenommenVon=false;
	private boolean istRemisMoeglich=false;
	private boolean istRemisangebotVon=false;
	
	
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

	if(Partie.getInstance().istRemisangenommen()){
		istRemisAngenommenVon=true;
	}else{
		istRemisAngenommenVon= false;
	}
	return istRemisAngenommenVon;
		
	}

	public boolean istRemisMoeglich() throws NegativeConditionException {
		// TODO Zustand#istRemisMoeglich
		if(Partie.getInstance().istRemisMoeglich()){
			istRemisMoeglich=true;
		}else{
			istRemisMoeglich=false;		}
		return istRemisMoeglich;
	}

	public boolean istRemisangebotVon(ISpieler spieler) throws NegativeConditionException{

		if(Partie.getInstance().istRemisAngebotVon(spieler)){
			istRemisangebotVon=true;
		}else{
			istRemisangebotVon=false;
		}
		return istRemisangebotVon;
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
		//Partiehistorie.getInstance().gebeStellungen(1).get(0).);
		try {
			return Partiehistorie.getInstance().gebeStellungen(1).get(0).istKoenigBedroht(farbe);
		}
		catch(IndexOutOfBoundsException e){}
		return false;
	}
}
