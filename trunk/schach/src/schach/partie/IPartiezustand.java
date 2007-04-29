package schach.partie;

import schach.spieler.ISpieler;

public interface IPartiezustand {
	public boolean inPartie();
	
	public boolean istRemisangebotVon(ISpieler spieler);
	
	public boolean istRemisAngenommenVon(ISpieler spieler);
	
	public boolean istRemis();
	
	public boolean istPatt();
	
	public boolean istSchachmatt();
	
	public boolean istRemisMoeglich();
	

}
