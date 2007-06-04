package schach.system.internal;

import java.util.Observable;

import schach.system.IView;
import schach.system.Logger;

public class DevNullView implements IView {
	
	public DevNullView() {
		Logger.error("DevNullView Konstruktor");
	}

	public void update(Observable o, Object arg) {
		update();
	}

	public void update() {}

}
