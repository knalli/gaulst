package schach.brett.internal;

import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;
import schach.system.View;

public abstract class AbstrakteFigur extends Observable implements IFigur {
	private IFeld position;
	private IFeld grundposition;
	private Farbe farbe;
	private Figurart figurart;
	
	public AbstrakteFigur(Farbe farbe, IFeld feld, Figurart figurart) {
		
		try {
			feld.istBesetzt(true);
			this.farbe = farbe;
			this.grundposition = feld;
			this.position = feld;
			this.figurart = figurart;
			
			addObserver(View.getView());
			
			Logger.debug(farbe+" "+figurart+" wurde auf "+position.gebeReihe()+","+position.gebeLinie());
		} catch (NegativeConditionException e){
			Logger.error("Wurde nicht erzeugt und aufgestellt: "+farbe+" "+figurart+" wurde auf "+position.gebeReihe()+","+position.gebeLinie());
		}
		
		
	}

	public void aufstellen(IFeld feld) throws NegativeConditionException {
		if(!Partiezustand.getInstance().inPartie())
			throw new NegativePreConditionException();
		
		// @TODO brauchen wir das Ÿberhaupt noch (siehe Knstruktor)
	}

	public Farbe gebeFarbe() {
		return farbe;
	}

	public IFeld gebeGrundposition() {
		return grundposition;
	}

	public IFeld gebePosition() {
		return position;
	}

	public ISpieler gehoertSpieler() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean istAufDemSchachbrett() {
		return position == null;
	}

	public boolean istAufGrundposition() {
		return position.equals(grundposition);
	}

	public void positionieren(IFeld feld) throws NegativeConditionException {
		if(Brett.getInstance().istBauernUmwandlung() != true)
			throw new NegativePreConditionException();
		if(feld.istBesetzt())
			throw new NegativePreConditionException();
		
		position = feld;
		setChanged();
		notifyObservers(); 
	}

	public Figurart gebeArt() {
		return figurart;
	}

	public String toString(){
		return figurart+" "+farbe;
	}
}
