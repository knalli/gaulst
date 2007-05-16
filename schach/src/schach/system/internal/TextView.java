package schach.system.internal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IAlleFiguren;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartie;
import schach.partie.IPartiezustand;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;
import schach.system.IView;
import schach.system.Logger;

public class TextView implements IView {
	private Map<IFeld,IFigur> besetzteFelder = new HashMap<IFeld,IFigur>();
	private static IAlleFiguren allefiguren = null;
	private static List<Figurart> listeFigurarten = Arrays.asList(Figurart.values());
	private static List<Farbe> listeFarben = Arrays.asList(Farbe.values());
	
	public TextView() {
		Logger.error("TextView Konstruktor");
		allefiguren = AlleFiguren.getInstance();
	}

	public void update(Observable o, Object arg) {
		update();
	}

	public void update() {
		besetzteFelder.clear();
		IPartie partie = Partie.getInstance();
		IPartiezustand partiezustand = Partiezustand.getInstance();

		for(IFigur figur : allefiguren.gebeFiguren(listeFigurarten,listeFarben)){
			besetzteFelder.put(figur.gebePosition(),figur);
//			Logger.debug("View: "+figur+" steht auf "+figur.gebePosition());
		}
		
		// zeichne Brett
		if(!partiezustand.inPartie()){
			System.out.println("Partie läuft nicht.");
		}
		else {
			System.out.println("Aktueller Spieler: "+(partie.aktuellerSpieler().toString()));
		}
		
		
		System.out.println(" |AA|BB|CC|DD|EE|FF|GG|HH| ");
		System.out.println(" +--+--+--+--+--+--+--+--+ ");
		IFeld feld;
		IFigur figur;
		IBrett brett = Brett.getInstance();
		Reihe[] reihen = {Reihe.R8,Reihe.R7,Reihe.R6,Reihe.R5,
				Reihe.R4,Reihe.R3,Reihe.R2,Reihe.R1};
		for(Reihe r : reihen){
			StringBuilder sb = new StringBuilder(r.toString()+"|");
			for(Linie l : Linie.values()){
				feld = brett.gebeFeld(r, l);
				if(besetzteFelder.containsKey(feld)){
					figur = besetzteFelder.get(feld);
					
					switch(figur.gebeFarbe()){
					case SCHWARZ:
						sb.append('S');
						break;
					case WEISS:
					default:
						sb.append('W');
					}
					
					switch(figur.gebeArt()){
					case BAUER:
						sb.append('B');
						break;
					case DAME:
						sb.append('D');
						break;
					case KOENIG:
						sb.append('K');
						break;
					case LAEUFER:
						sb.append('L');
						break;
					case SPRINGER:
						sb.append('S');
						break;
					case TURM:
						sb.append('T');
						break;
					}
					sb.append('|');
				}
				else {
					sb.append("  |");
				}
			}
			System.out.println(sb.append(r.toString()).toString());
			System.out.println(" +--+--+--+--+--+--+--+--+ ");
		}
		System.out.println(" |AA|BB|CC|DD|EE|FF|GG|HH| ");
		
		if(partiezustand.istPatt())
			System.out.println("Partie ist Schachmatt! Partie zu Ende.");
		else if(partiezustand.istSchach(Farbe.WEISS))
			System.out.println("Der weiße König steht im Schach.");
		else if(partiezustand.istSchach(Farbe.SCHWARZ))
			System.out.println("Der schwarze König steht im Schach.");
		else if(partiezustand.istRemisMoeglich())
			System.out.println("Remis ist möglich! Annehmen?");
	}

}
