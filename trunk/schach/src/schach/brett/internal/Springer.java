package schach.brett.internal;

import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
import schach.brett.ISpringer;
import schach.partie.IStellung;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class Springer extends AbstrakteFigur implements ISpringer {

	private boolean sollentferntwerden;

	public Springer(Farbe farbe, IFeld feld) {
		super(farbe, feld, Figurart.SPRINGER);
	}

	protected Springer(IFigur figur){
		super(figur);
	}
	
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner)
			throws NegativeConditionException {
		
		if (!this.farbe.equals(gegner.gebeFarbe()))
			throw new NegativePreConditionException();
		

		
		
		
		List<IFigur> figuren = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe);
		IKoenig koenig = (IKoenig) figuren.get(0);
		if(koenig.istInEinerRochade()){
			throw new NegativePreConditionException();
		}
			
			
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		if(!position.plusReihe(1).plusLinie(2).equals(ziel)||
				!position.plusReihe(1).minusLinie(2).equals(ziel) ||
				!position.minusReihe(1).plusLinie(2).equals(ziel) ||
				!position.minusReihe(1).minusLinie(2).equals(ziel) ||
				!position.plusReihe(2).plusLinie(2).equals(ziel) ||
				!position.plusReihe(2).minusLinie(1).equals(ziel) ||
				!position.minusReihe(2).plusLinie(1).equals(ziel) ||
				!position.minusReihe(2).minusLinie(1).equals(ziel)){
			throw new NegativePreConditionException();
		}
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
			
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
				
		if(!ziel.istBesetzt()){
			throw new NegativePreConditionException();
		} 
		else {
			
			// im OCL hatten wir gegner.aufDemSchachbrett() stehen
			// stimmt das so?
			gegner.sollEntferntWerden();
			
			
			position.istBesetzt(false);
			position = ziel;
			position.istBesetzt(true);
		}
		
		for(IFigur fig : AlleFiguren.getInstance().gebeFiguren(Figurart.BAUER, farbe)) {
			((IBauer) fig).letzteRundeDoppelschritt(false);
		}
		

	}

	public void zieht(IFeld ziel) throws NegativeConditionException {
		// TODO Auto-generated method stub

		List<IFigur> figuren = AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, farbe);
		IKoenig koenig = (IKoenig) figuren.get(0);
		if(koenig.istInEinerRochade()){
			throw new NegativePreConditionException();
		}
			
			
		if(position.equals(ziel))
			throw new NegativePreConditionException();
		
		if(!position.plusReihe(1).plusLinie(2).equals(ziel)||
				!position.plusReihe(1).minusLinie(2).equals(ziel) ||
				!position.minusReihe(1).plusLinie(2).equals(ziel) ||
				!position.minusReihe(1).minusLinie(2).equals(ziel) ||
				!position.plusReihe(2).plusLinie(2).equals(ziel) ||
				!position.plusReihe(2).minusLinie(1).equals(ziel) ||
				!position.minusReihe(2).plusLinie(1).equals(ziel) ||
				!position.minusReihe(2).minusLinie(1).equals(ziel)){
			throw new NegativePreConditionException();
		}
		
		if(!this.gehoertSpieler().istZugberechtigt() || Partiezustand.getInstance().istRemis()
				|| Partiezustand.getInstance().istPatt() || Partiezustand.getInstance().istSchachmatt()){
				throw new NegativePreConditionException();
		}
			
		IStellung stellung = Partiehistorie.getInstance().simuliereStellung(position, ziel);
		if(stellung.istKoenigBedroht(farbe))
			throw new NegativePreConditionException();
				
		if(ziel.istBesetzt()){
			throw new NegativePreConditionException();
		}
		
		position.istBesetzt(false);
		position = ziel;
		position.istBesetzt(true);
		
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
