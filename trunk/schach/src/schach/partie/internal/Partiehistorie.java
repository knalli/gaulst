package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.NegativeConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	private List<IStellung> stellungen=new ArrayList<IStellung>();
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
		return new Stellung(new ArrayList<IFigur>(),false,null);
	}

	public List<IStellung> gebeStellungen(int n) {
		List<IStellung> lastStellung=new ArrayList<IStellung>(); 
		
		for (int i=0;i<n;i++){
			lastStellung.add(stellungen.get(i));
		}
		return lastStellung;
		
	}

	public boolean istZugProtokolliert() {
		return stellungen.get(stellungen.size()-1).equals(gebeStellung());
	}

	public IStellung simuliereStellung(IFeld start, IFeld ziel)
			throws NegativeConditionException {
		List<IFigur> neuefiguren = new ArrayList<IFigur>();
		IFigur ziehendeFigur = Brett.getInstance().gebeFigurVonFeld(start);
		IFigur neueziehendefigur = null;
		
		for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Farbe.getAll())){
			IFeld neuesfeld = figur.gebePosition().clone();
			IFigur neuefigur = figur.clone(neuesfeld);
			neuefiguren.add(neuefigur);
			
			if(figur.equals(ziehendeFigur))
				neueziehendefigur = neuefigur;
		}
		
		return new Stellung(neuefiguren,ziel.istBesetzt(),neueziehendefigur);
	}

	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur) {
		List<IFigur> neuefiguren = new ArrayList<IFigur>();
		IFigur neueziehendefigur = null;
		
		for(IFigur figur : AlleFiguren.getInstance().gebeFiguren(Figurart.getAll(), Farbe.getAll())){
			IFeld neuesfeld = figur.gebePosition().clone();
			IFigur neuefigur = figur.clone(neuesfeld);
			neuefiguren.add(neuefigur);
			
			if(figur.equals(ziehendeFigur))
				neueziehendefigur = neuefigur;
		}
		
		stellungen.add(new Stellung(neuefiguren,schlagzug,neueziehendefigur, stellungen.size()+1));
	}

}
