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
	 * Enth�lt die Grundposition der Schachfigur auf dem Schachbrett.
	 * @return Feld
	 */
	public IFeld gebeGrundposition();
	
	/**
	 * Enth�lt die Position der Schachfigur auf dem Schachbrett.
	 * @return Feld
	 */
	public IFeld gebePosition();
	
	/**
	 * Enth�lt den zugeh�rigen Spieler einer Schachfigur.
	 * @return Spieler
	 */
	public ISpieler gehoertSpieler();
	
	/**
	 * Enth�lt die zugeh�rige Farbe einer Schachfigur.
	 * @return Farbe
	 */
	public Farbe gebeFarbe();
	
	/**
	 * Gibt zur�ck, ob die Schachfigur sich auf dem Schachbrett befindet.
	 * @return
	 */
	public boolean istAufDemSchachbrett();
	
	/**
	 * Gibt zur�ck, ob die Schachfigur auf ihrer Grundposition steht.
	 * @return
	 */
	public boolean istAufGrundposition();
	
	/**
	 * Gibt zur�ck, welchen Typ die Figur hat.
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
	 * Die Figur zieht auf das <code>ziel</code>-Feld und schl�gt dabei die gegnerische <code>gegner</code>-Figur.
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
