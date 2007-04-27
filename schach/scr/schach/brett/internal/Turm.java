package schach.brett.internal;

import java.util.List;
import schach.brett.*;
import schach.partie.internal.*;
import schach.system.NegativeConditionException;

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
		return schonBewegt;
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub
		boolean test = true;
		IKoenig king = null;
		IBauer knecht = null;
		IFeld start, temp;
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(this.position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(this.position, ziel);
		k_reihe.remove(0);
		k_linie.remove(0);
		
		List<IFigur> koenig = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, this.farbe);
		for (IFigur x: koenig){
			if (x instanceof IKoenig){
				king = (IKoenig) x;
				if (king.istInEinerRochade())
					test = false;
			}
		}
		start = this.gebePosition();
		if (test) {
			if (!this.gehoertSpieler().istZugberechtigt() 
			|| Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt()
				|| Partiezustand.getInstance().istSchachmatt()) {
				test=false;
			}
			temp = this.position;
			this.positionieren(ziel);
			if(king.istBedroht())
				test = false;		
			this.positionieren(temp);
		}
		if(test) {
			if(!start.gebeLinie().toString().equals(ziel.gebeLinie().toString()) 
				|| !start.gebeReihe().toString().equals(ziel.gebeReihe().toString()))
				test = false;
		}
		if(test) {
			if(start.gebeReihe().toString().equals(ziel.gebeReihe().toString())) {
				if (!Brett.getInstance().sindAlleFelderFrei(k_reihe)){
					test=false;
				}
				
			}
			else if (start.gebeLinie().toString().equals(ziel.gebeReihe().toString()))
				test=false;
		}
		if (test){
			Partie.getInstance().setzeAktuellenSpieler();
			this.positionieren(ziel);
			schonBewegt = true;
			List<IFigur> bauer = AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, this.farbe);
			for (IFigur x: bauer){
				if (x instanceof IBauer){
					knecht = (IBauer) x;
					knecht.setDoppelschritt(false);
					
					// Partie.istStellungProtokolliert() =  true
					// fehlt... auch keine Idee... sorry
					
				}
			}
			
			
			
		}
			
	}

	public void geschlagenWerden() throws NegativeConditionException {
		// TODO Auto-generated method stub

	}

	public boolean sollEntferntWerden() {
		// TODO Auto-generated method stub
		return false;
	}

}
