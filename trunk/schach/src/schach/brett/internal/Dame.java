package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IBrett;
import schach.brett.IDame;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.ISchlagbareFigur;
import schach.partie.IStellung;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Dame extends AbstrakteFigur implements IDame {
	
	IBrett brett = Brett.getInstance();

	public Dame(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.DAME);
	}
	
	protected Dame(IFigur figur){
		super(figur);
	}
	
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		/**
		 * es fehlt noch !position.gebeDiagonale().equals(ziel.gebeDiagonale()) 
		 * da ja auch überprüft werden muss ob das zielfeld ggf. in einer der beiden
		 * diagonalen liegt
		 * das gleiche beim läufer auch bzw. da muss ja nur geprüft werden ob ziel in
		 * den diagonalen liegt 
		 */
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
		
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(position, ziel);
		List <IFeld> k_diagonale = Brett.getInstance().gebeFelderInDiagonalen(position, ziel);	
		
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_linie))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_reihe))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_diagonale))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
		}
				
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)){
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
			|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
		
		List <IFeld> k_reihe = Brett.getInstance().gebeFelderInReihe(position, ziel);
		List <IFeld> k_linie = Brett.getInstance().gebeFelderInLinie(position, ziel);
		List <IFeld> k_diagonale = Brett.getInstance().gebeFelderInDiagonalen(position, ziel);	
		
		if (ziel.istBesetzt())
			throw new NegativePreConditionException();
		else {
			if (!brett.sindAlleFelderFrei(k_linie))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_reihe))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
			}
			if (!brett.sindAlleFelderFrei(k_diagonale))
				throw new NegativePreConditionException();
			else{
				position.istBesetzt(false);
				position = ziel;
				position.istBesetzt(true);
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
