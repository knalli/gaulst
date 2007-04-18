package schach.brett.internal;

import java.util.Observable;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.main.View;
import schach.partie.internal.Partiezustand;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;
import schach.system.NegativePostConditionException;
import schach.system.NegativePreConditionException;

public abstract class AbstrakteFigur extends Observable implements IFigur {
	private IFeld position;
	private IFeld grundposition;
	private Farbe farbe;
	private Figurart figurart;
	
	public AbstrakteFigur(Farbe farbe, IFeld feld, Figurart figurart) {
		
		addObserver(View.getView());
		
		this.farbe = farbe;
		this.grundposition = feld;
		this.position = feld;
		this.figurart = figurart;


	}

	public void aufstellen(IFeld feld) throws NegativeConditionException {
		if(!Partiezustand.getInstance().inPartie())
			throw new NegativePreConditionException();
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

	public Figurart gebeTyp() {
		return figurart;
	}

	public ISpieler gehoertSpieler() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean istAufDemSchachbrett() {
		return grundposition != null;
	}

	public boolean istAufGrundposition() {
		return position.equals(grundposition);
	}

	public void positionieren(IFeld feld) throws NegativeConditionException {
		if(Brett.getInstance().istBauernUmwandlung() != true)
			throw new NegativePreConditionException();
		
		position = feld;
		
		if(position != feld)
			throw new NegativePostConditionException();
		
		setChanged();
		notifyObservers(); 
	}

}
