package schach.brett;
import schach.spieler.ISpieler;
import schach.system.NegativeConditionException;

/**
 * Eine Schachfigur in einer Schachpartie.
 * 
 * @author Jan Philipp
 *
 */
public interface IFigur {
	/**
	 * Die Schachfigur wird auf das Feld ihrer Grundposition aufgestellt.
	 * 
	 * @param feld Position auf dem Schachbrett
	 * @throws NegativeConditionException
	 */
	public void aufstellen(IFeld feld) throws NegativeConditionException;
	
	/**
	 * Die Schachfigur wird auf eine bestimmte Position (Feld) gesetzt.
	 * @param feld
	 * @throws NegativeConditionException
	 */
	public void positionieren(IFeld feld) throws NegativeConditionException;
	
	/**
	 * Enthält die Grundposition der Schachfigur auf dem Schachbrett.
	 * @return Feld
	 */
	public IFeld gebeGrundposition();
	
	/**
	 * Enthält die Position der Schachfigur auf dem Schachbrett.
	 * @return Feld
	 */
	public IFeld gebePosition();
	
	/**
	 * Enthält den zugehörigen Spieler einer Schachfigur.
	 * @return Spieler
	 */
	public ISpieler gehoertSpieler();
	
	/**
	 * Enthält die zugehörige Farbe einer Schachfigur.
	 * @return Farbe
	 */
	public Farbe gebeFarbe();
	
	/**
	 * Gibt zurück, ob die Schachfigur sich auf dem Schachbrett befindet.
	 * @return
	 */
	public boolean istAufDemSchachbrett();
	
	/**
	 * Gibt zurück, ob die Schachfigur auf ihrer Grundposition steht.
	 * @return
	 */
	public boolean istAufGrundposition();
	
	/**
	 * Gibt zurück, welchen Typ die Figur hat.
	 * @return
	 */
	public Figurart gebeArt();
	
	/**
	 * Die Figur zieht auf das <code>ziel</code>-Feld.
	 * @param ziel
	 * @throws NegativeConditionException
	 */
	public void zieht(IFeld ziel) throws NegativeConditionException;
	
	/**
	 * Die Figur zieht auf das <code>ziel</code>-Feld und schlägt dabei die gegnerische <code>gegner</code>-Figur.
	 * @param ziel Zielfeld
	 * @param gegner gegnerische Figur
	 * @throws NegativeConditionException
	 */
	public void schlaegt(IFeld ziel, ISchlagbareFigur gegner) throws NegativeConditionException;
	
	/**
	 * Erstellt eine neue Kopie der Figur und positioniert sie auf ein Feld.
	 * Dieses Feld sollte nicht auf dem Schachbrett vorhanden sein.
	 * 
	 * @param neuesfeld
	 * @return
	 */
	public IFigur clone(IFeld neuesfeld);

	public void testeZug(IFeld ziel) throws NegativeConditionException;

	public void simuliereBrettzug(IFeld start);
	
	public IFeld gebeVorPosition();
}
