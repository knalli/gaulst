package schach.system;

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
		viewer = new TextView();
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
