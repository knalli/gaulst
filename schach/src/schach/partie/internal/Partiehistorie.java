package schach.partie.internal;

import java.util.List;

import schach.brett.IFeld;
import schach.partie.IPartiehistorie;
import schach.partie.IStellung;
import schach.system.NegativeConditionException;

public class Partiehistorie implements IPartiehistorie {
	private static IPartiehistorie instance = null;
	
	private Partiehistorie() {}
	
	public static IPartiehistorie getInstance() {
		if(instance == null)
			instance = new Partiehistorie();
		
		return instance;
	}
	public List<IStellung> gebeAlleStellungen() {
		// TODO Auto-generated method stub
		return null;
	}

	public IStellung gebeStellung() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IStellung> gebeStellungen(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean istZugProtokolliert() {
		// TODO Auto-generated method stub
		return false;
	}

	public IStellung simuliereStellung(IFeld start, IFeld ziel)
			throws NegativeConditionException {
		// TODO Auto-generated method stub
		return new Stellung(null,false,null);
	}

}