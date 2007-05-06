package schach.partie.internal;

import java.util.ArrayList;
import java.util.List;

import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	private boolean protokoll=false;
	private List<IStellung> stellungen=new ArrayList<IStellung>();
	private Partiehistorie() {}
	
	public static IPartiehistorie getInstance() {
		if(instance == null)
			instance = new Partiehistorie();
		
		return instance;
	}
	public List<IStellung> gebeAlleStellungen() {
		// TODO Auto-generated method stub
		
		return stellungen;
	}

	public IStellung gebeStellung() {
		// TODO Auto-generated method stub
		return new Stellung(new ArrayList<IFigur>(),false,null);
	}

	public List<IStellung> gebeStellungen(int n) {
		// TODO Auto-generated method stub
		return stellungen; 
	}

	public boolean istZugProtokolliert() {
		// TODO Auto-generated method stub
		return protokoll;
	}

	public IStellung simuliereStellung(IFeld start, IFeld ziel)
			throws NegativeConditionException {
		// TODO Auto-generated method stub
		if (start.istBesetzt()){
			return (new Stellung(new ArrayList<IFigur>(),false,null));
		}else{
			throw new NegativePreConditionException();
		}
	}

	public void protokolliereStellung() {
		protokoll=true;
		stellungen.add(new Stellung(new ArrayList<IFigur>(),false,null));
	}

}
