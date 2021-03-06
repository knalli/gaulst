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
	public IStellung simuliereStellung(IFeld start, IFeld ziel, IFigur zuSchlagendeFigur) throws NegativeConditionException;
	
	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur);
	public void protokolliereStellung(boolean schlagzug, IFigur ziehendeFigur, IFigur neueFigur);

	public boolean istEineSimulation();

	public void setzeSimulation(boolean b);
	
	public List<String> gebeBisherigeNotationen();
}
