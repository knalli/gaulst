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

public class AbstrakteFigur extends Observable {
	protected IFeld position = null;
	protected IFeld vorposition = null;
	protected IFeld grundposition = null;
	protected Farbe farbe = null;
	protected Figurart figurart = null;
	
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
			throw new NegativePreConditionException("Partie läuft nicht.");
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

	public void positionieren(IFigur figur, IFeld feld) throws NegativeConditionException {
		if(!Brett.getInstance().istBauernUmwandlung())
			throw new NegativePreConditionException("Es besteht derzeit keine Bauernumwandlung.");
		
		if(figur.gebeArt().equals(Figurart.BAUER) || figur.gebeArt().equals(Figurart.KOENIG))
			throw new NegativePreConditionException("Auszutauschende Figur darf kein Bauer oder König sein.");
		
		position = feld;
		grundposition = position;
		AlleFiguren.getInstance().fuegeFigurAn(figur);
	}

	public Figurart gebeArt() {
		return figurart;
	}

	public String toString(){
		return figurart+" "+farbe+" ["+(position==null?"weg":position)+"]";
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
	
	public void erzwingeUpdate() {
//		informiere die Beobachter, dass sich etwas geändert hat
		setChanged();
		notifyObservers();
	}

	public void setzeUmPosition(IFeld feld){
		position = feld;
	}
	
	public void setzeUmGrundposition(IFeld feld){
		grundposition = feld;
	}
}
