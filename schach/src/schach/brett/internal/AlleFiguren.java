package schach.brett.internal;

import java.util.Arrays;
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
import schach.system.ChessException;

public class AlleFiguren implements IAlleFiguren {
	private static AlleFiguren instance = null;
	private AlleFiguren(){};
	private List<IFigur> figuren = new LinkedList<IFigur>();
	
	public static AlleFiguren getInstance() {
		if(instance == null)
			instance = new AlleFiguren();
		
		return instance;
	}
	
	public void aufstellen() {
		// TODO Auto-generated method stub

	}

	public List<IFigur> gebeFiguren(List<Figurart> figuren, List<Farbe> farben) {
		// TODO Auto-generated method stub
		return null;
	}

	public void stelleAlleFigurenAuf() throws ChessException {
		IBrett brett = Brett.getInstance();
		
		// @ TODO VErvollständigen
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.A));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.B));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.C));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.D));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.E));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.F));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.G));
		erzeugeFigur(Farbe.SCHWARZ, Figurart.BAUER, brett.gebeFeld(Reihe.R2, Linie.H));
	}

	private IFigur erzeugeFigur(Farbe farbe, Figurart figurart, IFeld feld) {
		IFigur figur = null;
		switch(figurart){
		case BAUER:
			figur = new Bauer(farbe,feld);
		case DAME: 
			figur = null;
		case KOENIG: 
			figur = null;
		case LAEUFER: 
			figur =  null;
		case SPRINGER: 
			figur =  null;
		case TURM: 
			figur =  null;
		}
		return figur;
	}
}
