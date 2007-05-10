package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.internal.Brett;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	private List<IStellung> stellungen=new ArrayList<IStellung>();
	private boolean simuliere = false;
	private Partiehistorie() {}
	
	public static IPartiehistorie getInstance() {
		if(instance == null)
			instance = new Partiehistorie();
		
		return instance;
	}
	public List<IStellung> gebeAlleStellungen() {
		return stellungen;
	}

	public IStellung gebeStellung() {
		// TODO Historie#gebeStellung
		return new Stellung(false,null,stellungen.size()+1);
	}

	public List<IStellung> gebeStellungen(int n) {
		if(n > stellungen.size())
			return gebeAlleStellungen();
		
		List<IStellung> lastStellung=new ArrayList<IStellung>(); 
		for (int i=stellungen.size()-n;i<stellungen.size();i++){
			lastStellung.add(stellungen.get(i));
		}
		return lastStellung;
		
	}

	public boolean istZugProtokolliert() {
		return stellungen.get(stellungen.size()-1).equals(gebeStellung());
	}
	
	public IStellung simuliereStellung(IFeld start, IFeld ziel) throws NegativeConditionException {
		return simuliereStellung(start, ziel, null);
	}

	public IStellung simuliereStellung(IFeld start, IFeld ziel, IFigur zuschlagendeFigur)
			throws NegativeConditionException {
		IFigur ziehendeFigur = Brett.getInstance().gebeFigurVonFeld(start);
		IFeld zuschlagendeFigurFeld = null;
		
		if(ziehendeFigur == null)
			throw new NegativePreConditionException("Es wurde keine Figur ausgewŠhlt.");
		
		simuliere  = true;
		
		start.istBesetzt(false);
		ziehendeFigur.simuliereBrettzug(ziel);
		
		if(zuschlagendeFigur != null){
			zuschlagendeFigurFeld = zuschlagendeFigur.gebePosition();
			zuschlagendeFigur.simuliereBrettzug(null);
			zuschlagendeFigurFeld.istBesetzt(false);
		}
		ziel.istBesetzt(true);
		
		Stellung simulation = new Stellung(ziel.istBesetzt(),ziehendeFigur);
		
		start.istBesetzt(true);
		ziehendeFigur.simuliereBrettzug(start);
		ziel.istBesetzt(false);
		if(zuschlagendeFigur != null){
			zuschlagendeFigur.simuliereBrettzug(zuschlagendeFigurFeld);
			zuschlagendeFigurFeld.istBesetzt(true);
		}

		simuliere = false;
		return simulation;
	}

	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur) {
		stellungen.add(new Stellung(schlagzug,ziehendeFigur, stellungen.size()+1));
	}

	public boolean istEineSimulation() {
		return simuliere;
	}
}
