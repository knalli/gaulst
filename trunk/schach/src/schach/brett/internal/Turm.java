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

	public Turm(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.TURM);
	}
	
	public void rochiert(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean wurdeBewegt() throws NegativeConditionException {
		// TODO Auto-generated method stub
		return false;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
//		boolean test = true;
//		IKoenig king = null;
//		IBauer knecht = null;
//		IFeld  temp;

//		k_reihe.remove(0);
//		k_linie.remove(0);
		
//		List<IFigur> koenig = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe);
//		for (IFigur x: koenig){
//			if (x instanceof IKoenig){
//				king = (IKoenig) x;
//				if (king.istInEinerRochade())
//					test = false;
//			}
//		}
		List<IFigur> figuren = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe);
		IKoenig koenig = (IKoenig) figuren.get(0);
		if(koenig.istInEinerRochade()){
			throw new NegativePreConditionException();
		}
			
				
//		start = this.gebePosition();
//		if (test) {
//			if (!this.gehoertSpieler().istZugberechtigt() 
//			|| Partiezustand.getInstance().istRemis()
//				|| Partiezustand.getInstance().istPatt()
//				|| Partiezustand.getInstance().istSchachmatt()) {
//				test=false;
//			}
//			temp = this.position;
//			this.positionieren(ziel);
//			if(king.istBedroht())
//				test = false;		
//			this.positionieren(temp);
//		}		
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
		
//		IFeld start=position;
//		if(test) {
//			if(!start.gebeLinie().toString().equals(ziel.gebeLinie().toString()) 
//				|| !start.gebeReihe().toString().equals(ziel.gebeReihe().toString()))
//				test = false;
//		}
//		if(test) {
//			if(start.gebeReihe().toString().equals(ziel.gebeReihe().toString())) {
//				if (!Brett.getInstance().sindAlleFelderFrei(k_reihe)){
//					test=false;
//				}
//				
//			}
//			else if (start.gebeLinie().toString().equals(ziel.gebeReihe().toString()))
//				test=false;
//		}
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
//			for (IFigur x: bauer){
//				if (x instanceof IBauer){
//					knecht = (IBauer) x;
//					//knecht.setDoppelschritt(false);
//					
//					// Partie.istStellungProtokolliert() =  true
//					// fehlt... auch keine Idee... sorry
//					
//				}
//			}
		
//		}
			
	}


	public void geschlagenWerden() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sollEntferntWerden() {
		// TODO Auto-generated method stub
		return false;
	}

}
