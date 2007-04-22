package schach.system;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import schach.system.internal.GuiView;
import schach.system.internal.TextView;

public class View {
	private static IView viewer = null;
	
	/**
	 * Erstellt ein neues View-Objekt. 
	 * Hinweis: Vorhandene Objekte werden überschrieben, im Regelfall nur einmal ausführen.
	 * 
	 * @param name
	 */
	public static void setView(String name){
		if(name.equals("gui")){
			
			try {
				SwingUtilities.invokeAndWait(new Runnable() {
					public void run() {
						GuiView.getInstance();					
					}
				});
			} catch (Exception e) {
				Logger.error("Fehler beim Starten der grafischen Oberfläche - nutze stattdessen die einfache!");
				setView("text");
			}
			viewer = GuiView.getInstance();		
		}
		else {
			viewer = new TextView();
		}
	}
	
	public static void setView() {
		setView("text");
	}
	
	public static IView getView() {
		if(viewer == null)
			setView();
		return viewer;
	}
}
