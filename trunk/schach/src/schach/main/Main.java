package schach.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.internal.Partie;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.View;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		View.setView(View.MULTI);
		Logger.appendLogger("console");
		Logger.debug("Main gestartet..");
		
		Partie.getInstance().start();

		Scanner s = new Scanner(System.in);
		s.nextLine();
		
		List<Figurart> a = new ArrayList<Figurart>();
		a.add(Figurart.BAUER);
		List<Farbe> b = new ArrayList<Farbe>();
		b.add(Farbe.SCHWARZ);
		try {
			AlleFiguren.getInstance().gebeFiguren(a,b).get(1).positionieren(Brett.getInstance().gebeFeld(Reihe.R4, Linie.B));
		} catch (NegativeConditionException e) {
			Logger.warning("Figur konnte nicht bewegt werden");
			e.printStackTrace();
		}
	}

}
