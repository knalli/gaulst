package schach.main;

import schach.partie.IPartie;
import schach.partie.IPartiezustand;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		View.setView("text");
		IView viewer = View.getView();
		
		Partie.getInstance().start();
	}

}
