package schach.system;

import javax.swing.SwingUtilities;

import schach.system.internal.GuiView;
import schach.system.internal.MultiView;
import schach.system.internal.TextView;

public class View {
	private static IView viewer = null;
	
	public static final int TEXT = 0;
	public static final int GUI = 1;
	public static final int MULTI = 2;
	
	/**
	 * Erstellt ein neues View-Objekt. 
	 * Hinweis: Vorhandene Objekte werden überschrieben, im Regelfall nur einmal ausführen.
	 * 
	 * @param name
	 */
	public static void setView(int type){
		if(type==MULTI){
			viewer = new MultiView();
		}
		else if(type==GUI){
			
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						GuiView.getInstance();					
					}
				});	
			} catch (Exception e) {
				Logger.error("Fehler beim Starten der grafischen Oberfläche - nutze stattdessen die einfache TextView!");
				setView(TEXT);
				return;
			}
			viewer = GuiView.getInstance();	
		}
		else if(type==TEXT) {
			viewer = new TextView();
		}
		else {
			Logger.error("Unbekannte View angegeben.. starte Textview!");
			setView(TEXT);
		}
	}
	
	public static void setView() {
		Logger.error("Nutze Text, da nichts angegeben.");
		setView(TEXT);
	}
	
	public static IView getView() {
		if(viewer == null)
			setView();
		return viewer;
	}
}
