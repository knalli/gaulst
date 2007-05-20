package schach.system.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.SwingUtilities;

import schach.system.IView;
import schach.system.Logger;

public class MultiView implements IView {
	private List<IView> views = new ArrayList<IView>();
	public MultiView(){
		Logger.error("Multiview");
		views.add(new TextView());
		
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					GuiView.getInstance();					
				}
			});
			views.add( GuiView.getInstance() );
		} catch (Exception e) {
			Logger.error("MultiView: Fehler beim Starten der grafischen Oberfläche");
		}	
		
		views.add(new NetView());
	}

	public void update() {
		for(IView view : views)
			view.update();
	}

	public void update(Observable o, Object arg) {
		for(IView view : views)
			view.update(o,arg);
	}
}
