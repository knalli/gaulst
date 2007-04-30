package schach.brett.internal;

import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.spieler.internal.Spieler;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;
import schach.system.View;

public abstract class AbstrakteFigur extends Observable implements IFigur {
	protected IFeld position;
	protected IFeld grundposition;
	protected Farbe farbe;
	protected Figurart figurart;
	
	public AbstrakteFigur(Farbe farbe, IFeld feld, Figurart figurart) {
		
		try {
			feld.istBesetzt(true);
			this.farbe = farbe;
			this.grundposition = feld;
			this.position = feld;
			this.figurart = figurart;
			
			addObserver(View.getView());
			
			Logger.debug(farbe+" "+figurart+" wurde auf "+position.gebeLinie()+position.gebeReihe()+" positioniert.");
		} catch (NegativeConditionException e){
			Logger.error("Wurde nicht erzeugt und aufgestellt: "+farbe+" "+figurart+" wurde auf "+position.gebeReihe()+","+position.gebeLinie());
		}
		
		
	}
	
	protected AbstrakteFigur(IFigur figur){
		farbe = figur.gebeFarbe();
		grundposition = figur.gebeGrundposition();
		position = figur.gebePosition();
		figurart = figur.gebeArt();
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
		return Spieler.getInstance(farbe);
	}

	public boolean istAufDemSchachbrett() {
		return grundposition != null;
	}

	public boolean istAufGrundposition() {
		return position.equals(grundposition);
	}

	public void positionieren(IFeld feld) throws NegativeConditionException {
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException();
		
		position = feld;
		AlleFiguren.getInstance().fuegeFigurAn(this);
		setChanged();
		notifyObservers(); 
	}

	public Figurart gebeArt() {
		return figurart;
	}

	public String toString(){
		return figurart+" "+farbe;
	}
	
	public IFigur clone(IFeld neuesfeld) {
		IFigur figur = null;
		
		switch(figurart){
		case BAUER: 	figur = new Bauer(this);	break;
		case DAME: 		figur = new Dame(this);		break;
		case KOENIG: 	figur = new Koenig(this);	break;
		case LAEUFER: 	figur = new Laeufer(this);	break;
		case SPRINGER: 	figur = new Springer(this);	break;
		case TURM: 		figur = new Turm(this);		break;
		}
		
		return figur;
	}
}
