package schach.partie;


import java.util.List;

import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.system.NegativeConditionException;

public interface IPartiehistorie {
	public boolean istZugProtokolliert();
	
	public List<IStellung> gebeStellungen(int n);
	
	public List<IStellung> gebeAlleStellungen();
	
	public IStellung gebeStellung();
	
	public IStellung simuliereStellung(IFeld start, IFeld ziel) throws NegativeConditionException;
	
	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur);
}
