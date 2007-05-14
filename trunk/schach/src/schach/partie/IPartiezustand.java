package schach.partie;

import schach.brett.Farbe;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;

public interface IPartiezustand {
	public boolean inPartie();
	
	public boolean istRemisangebotVon(ISpieler spieler) throws NegativeConditionException;
	
	public boolean istRemisAngenommenVon(ISpieler spieler);
	
	public boolean istRemis();
	
	public boolean istPatt();
	
	public boolean istSchachmatt();
	
	public boolean istRemisMoeglich() throws NegativeConditionException;

	public void haltePartieAn() throws NegativeConditionException;

	public boolean istSchach(Farbe farbe);
}
