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
	
	
	private IFeld startfeld = null;
	private IFeld zielfeld = null;
	
	public void addCompleteAction(IFeld startFeld, IFeld zielFeld) {
		startfeld = startFeld;
		zielfeld = zielFeld;
		
		evaluateAction();
		clearState();
	}

	public void addCompleteAction(Reihe startReihe, Linie startLinie,
			Reihe zielReihe, Linie zielLinie) {
		IBrett brett = Brett.getInstance();
		addCompleteAction(brett.gebeFeld(startReihe, startLinie),brett.gebeFeld(zielReihe, zielLinie));
	}

	public void addSingleAction(IFeld feld) {
		if(startfeld==null){
			if(feld.istBesetzt())
				startfeld = feld;
		}
		else {
			zielfeld = feld;
			evaluateAction();
			clearState();
		}
	}

	public void addSingleAction(Reihe reihe, Linie linie) {
		addSingleAction(Brett.getInstance().gebeFeld(reihe, linie));
	}
	
	private void evaluateAction() {
		IBrett brett = Brett.getInstance();
		IFigur startfigur = brett.gebeFigurVonFeld(startfeld);
		IFigur zielfigur = brett.gebeFigurVonFeld(zielfeld);
		
		try {
			// steht da überhaupt wer?
			if(startfigur != null){
				throw new Exception("Auf dem Startfeld steht keine Schachfigur.");
			}
			
			// Ziehzug? (ohne spezialfälle)
			if(zielfigur == null){
				startfigur.zieht(zielfeld);
			}
			// Schlagzug? (ohne spezialfälle)
			else {
				// gegnerische Figur schlagbar
				if(zielfigur instanceof ISchlagbareFigur){
					startfigur.schlaegt(zielfeld, (ISchlagbareFigur)zielfigur);
				}
				// gegnerische Figur nicht schlagbar
				else {
					zielfeld = null;
					throw new Exception("Figur ist nicht schlagbar.");
				}
				
			}
			
		} catch (Exception e) {
			Logger.error(e.getMessage());
		}
	}
	
	private void clearState() {
		startfeld = null;
		zielfeld = null;
	}

}
