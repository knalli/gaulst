package schach.brett.internal;

import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.spieler.internal.Spieler;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;
import schach.system.View;

public abstract class AbstrakteFigur extends Observable implements IFigur {
	protected IFeld position;
	protected IFeld vorposition;
	protected IFeld grundposition;
	protected Farbe farbe;
	protected Figurart figurart;
	
	public AbstrakteFigur(Farbe farbe, IFeld feld, Figurart figurart) {
		
		if(feld != null)
			feld.istBesetzt(true);
		
		this.farbe = farbe;
		this.grundposition = feld;
		this.position = feld;
		this.vorposition = null;
		this.figurart = figurart;
		
		addObserver(View.getView());
	}
	
	protected AbstrakteFigur(IFigur figur){
		farbe = figur.gebeFarbe();
		grundposition = figur.gebeGrundposition();
		position = figur.gebePosition();
		figurart = figur.gebeArt();
	}

	public void aufstellen(IFeld feld) throws NegativeConditionException {
		if(!Partiezustand.getInstance().inPartie())
			throw new NegativePreConditionException("Partie läuft nicht");
		
		// @TODO brauchen wir das überhaupt noch (siehe Knstruktor)
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
		return position != null;
	}

	public boolean istAufGrundposition() {
		return istAufDemSchachbrett() && position.equals(grundposition);
	}

	public void positionieren(IFeld feld) throws NegativeConditionException {
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Es besteht derzeit keine Bauernumwandlung.");
		
		position = feld;
		grundposition = position;
		AlleFiguren.getInstance().fuegeFigurAn(this);
		
//		informiere die Beobachter, dass sich etwas geändert hat
		setChanged();
		notifyObservers(); 
	}

	public Figurart gebeArt() {
		return figurart;
	}

	public String toString(){
		return figurart+" "+farbe+" ["+(position==null?"weg":position)+"]";
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
	
	public void simuliereBrettzug(IFeld ziel) {
		// @TODO Für Rochade muss verfeinert werden
		if(Partiehistorie.getInstance().istEineSimulation()){
			position = ziel;
		}
	}
	
	public IFeld gebeVorPosition() {
		return vorposition;
	}
	
	protected void speichereVorPosition() {
		vorposition = position;
	}
}
