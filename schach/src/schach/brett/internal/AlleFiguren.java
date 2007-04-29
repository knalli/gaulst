package schach.brett.internal;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IAlleFiguren;
import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.IFigur;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiezustand;
import schach.system.ChessException;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.NegativePreConditionException;

public class AlleFiguren implements IAlleFiguren {
	private static IAlleFiguren instance = null;
	private AlleFiguren(){
		Logger.debug("AlleFiguren Konstruktor");
	};
	private List<IFigur> figuren = new LinkedList<IFigur>();
	
	public static IAlleFiguren getInstance() {
		if(instance == null)
			instance = new AlleFiguren();
		
		return instance;
	}
	
	public void aufstellen() {
		// TODO Auto-generated method stub

	}

	public List<IFigur> gebeFiguren(List<Figurart> figurarten, List<Farbe> farben) {
		List<IFigur> figuren2 = new ArrayList<IFigur>();
		
		for(IFigur figur : this.figuren){
			if(farben.contains(figur.gebeFarbe()) && figurarten.contains(figur.gebeArt()) && figur.istAufDemSchachbrett()){
				figuren2.add(figur);
			}
		}
		
		return figuren2;
	}

	public void stelleAlleFigurenAuf() throws ChessException {
		IBrett brett = Brett.getInstance();
		
		
//		wei§e Bauern auf Reihe 2 A-H
		for(Linie linie : Linie.values())
			figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.BAUER, brett.gebeFeld(Reihe.R2, linie)));
		
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.TURM, brett.gebeFeld(Reihe.R1, Linie.A)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.SPRINGER, brett.gebeFeld(Reihe.R1, Linie.B)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.LAEUFER, brett.gebeFeld(Reihe.R1, Linie.C)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.DAME, brett.gebeFeld(Reihe.R1, Linie.D)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.KOENIG, brett.gebeFeld(Reihe.R1, Linie.E)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.LAEUFER, brett.gebeFeld(Reihe.R1, Linie.F)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.SPRINGER, brett.gebeFeld(Reihe.R1, Linie.G)));
		figuren.add(erzeugeFigur(Farbe.WEISS, Figurart.TURM, brett.gebeFeld(Reihe.R1, Linie.H)));
		
		
//		schwarze  Bauern auf Reihe 7 A-H
		for(Linie linie : Linie.values())
			figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R7, linie)));
		
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.TURM, brett.gebeFeld(Reihe.R8, Linie.A)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.SPRINGER, brett.gebeFeld(Reihe.R8, Linie.B)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.LAEUFER, brett.gebeFeld(Reihe.R8, Linie.C)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.DAME, brett.gebeFeld(Reihe.R8, Linie.D)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.KOENIG, brett.gebeFeld(Reihe.R8, Linie.E)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.LAEUFER, brett.gebeFeld(Reihe.R8, Linie.F)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.SPRINGER, brett.gebeFeld(Reihe.R8, Linie.G)));
		figuren.add(erzeugeFigur(Farbe.SCHWARZ, Figurart.TURM, brett.gebeFeld(Reihe.R8, Linie.H)));
	}

	private IFigur erzeugeFigur(Farbe farbe, Figurart figurart, IFeld feld) {
		IFigur figur = null;
		switch(figurart){
		case BAUER:
			figur = new Bauer(farbe,feld);
			break;
		case DAME: 
			figur = new Dame(farbe,feld);
			break;
		case KOENIG: 
			figur = new Koenig(farbe,feld);
			break;
		case LAEUFER: 
			figur =  new Laeufer(farbe,feld);
			break;
		case SPRINGER: 
			figur =  new Springer(farbe,feld);
			break;
		case TURM: 
			figur =  new Turm(farbe,feld);
		}
		return figur;
	}

	public List<IFigur> gebeFiguren(Figurart figurart, Farbe farbe) {
		List<Figurart> figurarten = new ArrayList<Figurart>(1);
		List<Farbe> farben = new ArrayList<Farbe>(1);
		figurarten.add(figurart);
		farben.add(farbe);
		return gebeFiguren(figurarten,farben);
	}

	public void entferneFiguren() throws NegativeConditionException {
		if(Partiezustand.getInstance().inPartie())
			throw new NegativePreConditionException();
		
		figuren.clear();
	}


	public void fuegeFigurAn(IFigur figur) throws NegativeConditionException {
		// wie soll man das sonst testen?
		if(Partie.getInstance().aktuelleFarbe().equals(Farbe.WEISS)){
			if(!figur.gebePosition().gebeReihe().equals(Reihe.R8)){
				throw new NegativePreConditionException();
			}
		}
		else {
			if(!figur.gebePosition().gebeReihe().equals(Reihe.R1)){
				throw new NegativePreConditionException();
			}
		}
		
		if(!figur.istAufGrundposition()){
			throw new NegativePreConditionException();
		}
		
		figuren.add(figur);
		figur.gebePosition().istBesetzt(true);
	}
}
