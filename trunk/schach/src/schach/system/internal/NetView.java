package schach.system.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Brett;
import schach.partie.IPartiezustand;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;
import schach.system.IView;

public class NetView implements IView {
	

	public void update() {
		System.out.println("NetView Updatw wurde erstellt.");
		
		Map<String,String> schachbrett = new HashMap<String,String>(64);
		Map<String,Boolean> partie = new HashMap<String,Boolean>(8);
		IBrett brett = Brett.getInstance();
		
		for(Reihe reihe : Reihe.values()){
			for(Linie linie : Linie.values()){
				schachbrett.put(linie.toString()+reihe.toString(), generiereFeldBeschreibung(brett, reihe, linie));
			}
		}
		
		IPartiezustand zustand = Partiezustand.getInstance();
		partie.put("SCHACHWEISS", zustand.istSchach(Farbe.WEISS));
		partie.put("SCHACHSCHWARZ", zustand.istSchach(Farbe.SCHWARZ));
		partie.put("MATT", zustand.istSchachmatt());
		partie.put("PATT", zustand.istPatt());
		partie.put("REMIS", zustand.istRemis());
		partie.put("REMISMOEGLICH", zustand.istRemisMoeglich());
		partie.put("WEISSAMZUG", Partie.getInstance().aktuelleFarbe().equals(Farbe.WEISS));
	}
	
	private String generiereFeldBeschreibung(IBrett brett, Reihe reihe, Linie linie) {
		IFeld feld = brett.gebeFeld(reihe, linie);
		if(feld.istBesetzt()){
			IFigur figur = brett.gebeFigurVonFeld(feld);
			switch(figur.gebeArt()){
			case BAUER:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WB";
				else 
					return "SB";
			case DAME:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WD";
				else 
					return "SD";
			case KOENIG:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WK";
				else 
					return "SK";
			case LAEUFER:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WL";
				else 
					return "SL";
			case SPRINGER:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WS";
				else 
					return "SS";
			case TURM:
				if(figur.gebeFarbe().equals(Farbe.WEISS))
					return "WT";
				else 
					return "ST";
			}
		}
		return "";
	}

	public void update(Observable o, Object arg) {
		update();
	}
}
