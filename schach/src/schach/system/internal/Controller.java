package schach.system.internal;

import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.ISchlagbareFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Brett;
import schach.system.IController;
import schach.system.Logger;

public class Controller implements IController {
	private static IController instance = null;
	
	public static IController getInstance() {
		if(instance == null)
			instance = new Controller();
		return instance;
	}
	
	private Controller() { }
	
	
	private IFigur figur = null;
	private IFeld ziel = null;
	
	private boolean evaluateAction() {
		Logger.debug("evaluiere: "+figur + " -> "+ziel);
		IBrett brett = Brett.getInstance();
		IFigur zielfigur = brett.gebeFigurVonFeld(ziel);
		
		try {
			// steht da überhaupt wer?
			if(figur == null){
				throw new Exception("Auf dem Startfeld steht keine Schachfigur.");
			}
			
			// Ziehzug? (ohne spezialfälle)
			if(zielfigur == null){
				figur.zieht(ziel);
			}
			// Schlagzug? (ohne spezialfälle)
			else {
				// gegnerische Figur schlagbar
				if(zielfigur instanceof ISchlagbareFigur){
					figur.schlaegt(ziel, (ISchlagbareFigur)zielfigur);
				}
				// gegnerische Figur nicht schlagbar
				else {
					throw new Exception("Figur ist nicht schlagbar.");
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage());
			return false;
		}
		return true;
	}
	
	private void clearState() {
		figur = null;
		ziel = null;
	}

	public void setzeFigur(IFeld feld) {
		setzeFigur(feld.gebeReihe(), feld.gebeLinie());
	}

	public void setzeFigur(Reihe reihe, Linie linie) {
		IBrett brett = Brett.getInstance();
		Logger.debug("Suche auf "+linie.toString()+reihe.toString());
		figur = brett.gebeFigurVonFeld(reihe, linie);
		ziel = null;
	}

	public void setzeZielFeld(IFeld feld) {
		ziel = feld;
		if(evaluateAction()){
			
			clearState();
		}
		else{
			ziel = null;
		}
	}

	public void setzeZielFeld(Reihe reihe, Linie linie) {
		setzeZielFeld(Brett.getInstance().gebeFeld(reihe, linie)); 
	}
}
