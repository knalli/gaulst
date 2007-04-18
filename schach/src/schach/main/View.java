package schach.main;

import schach.main.internal.TextView;

public class View {
	private static IView viewer = null;
	
	public static void setView(String name){
		viewer = new TextView();
	}
	
	public static IView getView() {
		if(viewer == null)
			throw new Error();
		return viewer;
	}
}
