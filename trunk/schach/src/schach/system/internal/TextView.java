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
import schach.system.IView;
import schach.system.Logger;

public class TextView implements IView {
	private Map<IFeld,IFigur> besetzteFelder = new HashMap<IFeld,IFigur>();
	private static IAlleFiguren allefiguren = null;
	private static List<Figurart> listeFigurarten = Arrays.asList(Figurart.values());
	private static List<Farbe> listeFarben = Arrays.asList(Farbe.values());
	
	public TextView() {
		Logger.debug("TextView Konstruktor");
		allefiguren = AlleFiguren.getInstance();
	}

	public void update(Observable o, Object arg) {
		update();
	}

	public void update() {
		besetzteFelder.clear();
		for(IFigur figur : allefiguren.gebeFiguren(listeFigurarten,listeFarben)){
			besetzteFelder.put(figur.gebePosition(),figur);
//			Logger.debug("View: "+figur+" steht auf "+figur.gebePosition());
		}
		
		// zeichne Brett
		System.out.println("+--+--+--+--+--+--+--+--+");
		IFeld feld;
		IFigur figur;
		IBrett brett = Brett.getInstance();
		for(Reihe r : Reihe.values()){
			StringBuilder sb = new StringBuilder("|");
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
			System.out.println(sb.toString());
			System.out.println("+--+--+--+--+--+--+--+--+");
		}
	}

}
