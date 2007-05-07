package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.brett.ITurm;
import schach.partie.IStellung;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Turm extends AbstrakteFigur implements ITurm {

	private boolean schonBewegt = false;
	private boolean sollentferntwerden;

	public Turm(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.TURM);
	}
	
	protected Turm(IFigur figur) {
		super(figur);
	}
	
	public void rochiert(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean wurdeBewegt() throws NegativeConditionException {
		return schonBewegt;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {

		List<IFigur> figuren = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe);
		IKoenig koenig = (IKoenig) figuren.get(0);
		if(koenig.istInEinerRochade()){
			throw new NegativePreConditionException();
		}
			
			
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		if(!position.gebeLinie().equals(ziel.gebeLinie()) && !position.gebeReihe().equals(ziel.gebeReihe())){
			throw new NegativePreConditionException();
		}
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
			
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
		
		IBrett brett = Brett.getInstance();
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(position, ziel);
		
		if(position.gebeLinie().equals(ziel.gebeLinie())){
			if(ziel.istBesetzt() || !brett.sindAlleFelderFrei(k_linie)){
				throw new NegativePreConditionException();
			} 
			else {
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
				schonBewegt = true;
			}
		} 
		else {
			if(ziel.istBesetzt() || !brett.sindAlleFelderFrei(k_reihe)){
				throw new NegativePreConditionException();
			} 
			else {
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
				schonBewegt = true;
			}
		}
		
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
					
	}

	public void geschlagenWerden() throws NegativeConditionException {
		if(!sollentferntwerden || !istAufDemSchachbrett()){
			throw new NegativePreConditionException();
		}
		
		position.istBesetzt(false);
		position = null;
		grundposition = null;
	}

	public boolean sollEntferntWerden() {
		return sollentferntwerden;
	}
}
