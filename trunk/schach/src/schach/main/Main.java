package schach.main;

import schach.partie.internal.Partie;
import schach.system.Logger;
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

	}

}
