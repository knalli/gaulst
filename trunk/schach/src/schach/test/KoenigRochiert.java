/**
 * 
 */
package schach.test;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IKoenig;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.brett.internal.Koenig;
import schach.partie.internal.Partie;
import schach.partie.internal.Partiehistorie;
import schach.partie.internal.Partiezustand;
import schach.system.IController;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.View;
import schach.system.internal.Controller;


/**
 * @author Jan Philipp
 * 
 * Dies testet den Operator Koenig.rochiert() mittels MINIMALEN MEHRFACHBEDINGUNGSTEST!
 * 
 * Junit-Funktionen
 * <ul><li>setUpBeforeClass wird benˆtigt, um einmalig etwas zu initialisieren</li>
 * <li>tearDownAfterClass wird benötigt, um (optional) etwas ganz Ende wieder zu entfernen</li>
 * <li>setUp wird benötigt, um vor jedem Testfall die Situation zu resetten</li>
 * <li>tearDown wird benötigt, um optional nach jedem Testfall etwas zu berechnen</li>
 * </ul>
 * 
 * Der Operator muss mit 17 Bedingungen getestet werden, dabei sind 2 Bedingungen Mehrfachbedigungen (2 OR).
 *
 */
public class KoenigRochiert {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		View.setView(View.DEVNULL);
		Logger.appendLogger("console");
		Logger.disableAllExceptTest();
		Logger.test("Test WEISSER KOENIG ROCHIERT gestartet..");
		
		Brett.getInstance();
		Partie.getInstance().start();
		
		controller = Controller.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		((Brett) Brett.getInstance()).restart();
		((AlleFiguren) AlleFiguren.getInstance()).restart();
		((Partie) Partie.getInstance()).restart();
		((Partiehistorie) Partiehistorie.getInstance()).restart();
		((Partiezustand) Partiezustand.getInstance()).restart();
		
		Partie.getInstance().start();
		Logger.test("======================================================");
	}
	
	private void execGrosseRoachadeNachLinksMitWeiss() throws NegativeConditionException {
		controller.parseInputString("E1C1", true);
	}
	
	private void execKleineRoachadeNachRechtsMitWeiss() throws NegativeConditionException {
		controller.parseInputString("E1G1", true);
	}
	
	private void execGrosseRochadeNachLinksMitSchwarz() throws NegativeConditionException {
		controller.parseInputString("E8C8", true);
	}
	
	private static IController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void b00aAllesOkay() throws NegativeConditionException {
		Logger.test("B00: Gültige Rochade ausführen");
		Logger.noTest();
		createTestCase09a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test
	public void b00bAllesOkay() throws NegativeConditionException {
		Logger.test("B00: Gültige Rochade ausführen");
		Logger.noTest();
		createTestCase09b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test
	public void b00cAllesOkay() throws NegativeConditionException {
		Logger.test("B00: Gültige Rochade ausführen");
		Logger.noTest();
		createTestCase09b();
		Logger.disableAllExceptTest();
		controller.parseInputString("REMIS", true);
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b01FarbeAmZugFalse() throws NegativeConditionException {
		Logger.test("B01: Ist der Spieler WEISS nicht zugberechtigt?");
		Logger.noTest();
		createTestCase01();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b02NichtRemisFalse() throws NegativeConditionException {
		Logger.test("B02: Ist die Partie remis?");
		Logger.noTest();
		createTestCase02();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b03NichtPattFalse() throws NegativeConditionException {
		Logger.test("B03: Ist die Partie patt? (Sonderfall, Schwarze Rochade)");
		Logger.noTest();
		createTestCase03();
		Logger.disableAllExceptTest();
		execGrosseRochadeNachLinksMitSchwarz();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException {
		Logger.test("B04: Ist die Partie matt?");
		Logger.noTest();
		createTestCase04();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b05KoenigBereitsInEinerRochadeFalse() throws NegativeConditionException {
		Logger.test("B05: Befindet sich der weiße König bereits in einer Rochade?");
		Logger.noTest();
		createTestCase05();
		Logger.disableAllExceptTest();
		Koenig k = (Koenig) AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0);
		k.setzeIstInEinerRochade(true);
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b06KeineBauernUmwandlungFalse() throws NegativeConditionException {
		Logger.test("B06: Besteht derzeit eine Bauernumwandlung bevor?");
		Logger.noTest();
		createTestCase06();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b07KoenigNichtBedrohtImNaechstenZugFalse() throws NegativeConditionException {
		Logger.test("B07: Wäre der weiße König im nächsten Zug direkt bedroht?");
		Logger.noTest();
		createTestCase07();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b08KoenigWurdeNichtBewegtFalse() throws NegativeConditionException {
		Logger.test("B08: Wurde der weiße König bereits bewegt?");
		Logger.noTest();
		createTestCase08();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b09ARichtigesZielfeldFalse() throws NegativeConditionException {
		Logger.test("B09a: Zielfeld ist links nicht richtig?");
		Logger.noTest();
		createTestCase09a();
		Logger.disableAllExceptTest();
		//controller.parseInputString("E1B1", true);
		((IKoenig) (AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0))).rochiert(Brett.getInstance().gebeFeld(Reihe.R1, Linie.B));
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b09BRichtigesZielfeldFalse() throws NegativeConditionException {
		Logger.test("B09b: Zielfeld ist rechts nicht richtig?");
		Logger.noTest();
		createTestCase09b();
		Logger.disableAllExceptTest();
		((IKoenig) (AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0))).rochiert(Brett.getInstance().gebeFeld(Reihe.R1, Linie.F));
		//controller.parseInputString("E1F1", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b11ATurmfeldBesetztFalse() throws NegativeConditionException {
		Logger.test("B11a: Turmfeld nicht besetzt?");
		Logger.noTest();
		createTestCase11a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b11BTurmfeldBesetztFalse() throws NegativeConditionException {
		Logger.test("B11b: Turmfeld nicht besetzt?");
		Logger.noTest();
		createTestCase11b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b12aTurmfeldMitTurmBesetztFalse() throws NegativeConditionException {
		Logger.test("B12a: Turmfeld ist mit einem Turm besetzt?");
		Logger.noTest();
		createTestCase12a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b12bTurmfeldMitTurmBesetztFalse() throws NegativeConditionException {
		Logger.test("B12b: Turmfeld ist mit einem Turm besetzt?");
		Logger.noTest();
		createTestCase12b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b14aTurmWurdeNichtBewegtFalse() throws NegativeConditionException {
		Logger.test("B14a: Der Turm wurde bereits bewegt?");
		Logger.noTest();
		createTestCase14a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b14bTurmWurdeNichtBewegtFalse() throws NegativeConditionException {
		Logger.test("B14b: Der Turm wurde bereits bewegt?");
		Logger.noTest();
		createTestCase14b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b15aFelderZwischenKoenigTurmNichtBesetztFalse() throws NegativeConditionException {
		Logger.test("B15a: Die Felder zwischen weißen König und Turm sind besetzt?");
		Logger.noTest();
		createTestCase15a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b15bFelderZwischenKoenigTurmNichtBesetztFalse() throws NegativeConditionException {
		Logger.test("B15b: Die Felder zwischen weißen König und Turm sind besetzt?");
		Logger.noTest();
		createTestCase15b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b16aZugwegDesKoenigNichtBedrohtFalse() throws NegativeConditionException {
		Logger.test("B16a: Die Zugfelder des weißen Königs sind nicht bedroht?");
		Logger.noTest();
		createTestCase16a();
		Logger.disableAllExceptTest();
		execGrosseRoachadeNachLinksMitWeiss();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b16bZugwegDesKoenigNichtBedrohtFalse() throws NegativeConditionException {
		Logger.test("B16b: Die Zugfelder des weissen Königs sind nicht bedroht?");
		Logger.noTest();
		createTestCase16b();
		Logger.disableAllExceptTest();
		execKleineRoachadeNachRechtsMitWeiss();
	}
	
	
	public void createTestCase01() throws NegativeConditionException {
		for(String cmd : new String[] {"D2D4", "C7C5", "C1D2"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase02() throws NegativeConditionException {
		for(String cmd : new String[] {"D2D4", "C7C5", "C1D2", "REMIS", "H7H5", "REMIS"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase03() throws NegativeConditionException {
		// 10.De6
		for(String cmd : new String[]{
				"E2E3", "A7A5",
				"D1H5", "A8A6",
				"H5A5", "H7H5",
				"A5C7", "A6H6",
				"H2H4", "F7F6",
				"C7D7", "E8F7",
				"D7B7", "D8D3",
				"B7B8", "D3H7",
				"B8C8", "F7G6",
				"C8E6"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase04() throws NegativeConditionException {
		for(String cmd : new String[]{
				"F2F3", "E7E6",
				"G2G4", "D8H4"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase05() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6",
				"D2D4", "D7D5",
				"D1D3", "D8D6",
				"C1D2", "C8D7"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase06() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6",
				"D2D4", "D7D5",
				"D1D3", "D8D6",
				"C1D2", "C8D7",
				"C2C4", "B7B5",
				"C4B5", "C7C5", 
				"B5C6", "D7C8",
				"C6C7", "C8D7",
				"C7C8"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase07() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6", 
				"D2D3", "D7D6", 
				"C1E3", "C8D7", 
				"F2F3", "E7E6", 
				"D1D2", "D8G5",
				"D2C3", "G5E3",
				"A3B5", "E3D3"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase08() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6", 
				"D2D3", "D7D6", 
				"C1D2", "C8D7", 
				"F2F3", "E7E6", 
				"E1F2", "H7H6", 
				"F2E1", "H6H5",
				"D2C3", "C7C6",
				"D1D2", "C6C5"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase09a() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6", 
				"A3C4", "H7H6", 
				"D2D3", "D7D6", 
				"C1D2", "C8D7", 
				"F2F3", "E7E6", 
				"D2C3", "C7C6",
				"D1D2", "C6C5"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase09b() throws NegativeConditionException {
		for(String cmd : new String[]{
				"G1F3", "G8H6", 
				"E2E3", "E7E6", 
				"F1E2", "F8E7"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase10a() throws NegativeConditionException {
		createTestCase09a();
	}
	
	public void createTestCase10b() throws NegativeConditionException {
		createTestCase09b();
	}
	
	public void createTestCase11a() throws NegativeConditionException {
		createTestCase09a();
		for(String cmd : new String[]{
				"A2A3", "H6H5", 
				"A1A2", "H5H4"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase11b() throws NegativeConditionException {
		createTestCase09b();
		for(String cmd : new String[]{
				"H2H3", "G7G6", 
				"H1H2", "G6G5"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase12a() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B1A3", "B8A6", 
				"A3C4", "H7H6", 
				"D2D3", "D7D6", 
				"C1D2", "C8D7", 
				"F2F3", "E7E6", 
				"D2C3", "C7C6",
				"A2A3", "C6C5",
				"A1A2", "G7G6",
				"D1A1", "F7F6"})
			controller.parseInputString(cmd, true);	
	}
	
	public void createTestCase12b() throws NegativeConditionException {
		for(String cmd : new String[]{
				"G1H3", "A7A6", 
				"F2F3", "B7B6", 
				"E2E3", "C7C6", 
				"F1E2", "D7D6", 
				"H3F2", "E7E6",
				"H2H3", "F7F6",
				"H1H2", "G7G6",
				"F2H1", "H7H6"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase14a() throws NegativeConditionException {
		createTestCase11a();
		controller.parseInputString("A2A1", true);
		controller.parseInputString("G7G6", true);
	}
	
	public void createTestCase14b() throws NegativeConditionException {
		createTestCase11b();
		controller.parseInputString("H2H1", true);
		controller.parseInputString("H6G8", true);
	}
	
	public void createTestCase15a() throws NegativeConditionException {
		createTestCase09a();
		for(String cmd : new String[]{
				"D2D1", "D8G5"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase15b() throws NegativeConditionException {
		createTestCase09b();
		for(String cmd : new String[]{
				"E2F1", "A7A6"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase16a() throws NegativeConditionException {
		createTestCase09a();
		for(String cmd : new String[]{
				"D2G5", "D8G5"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase16b() throws NegativeConditionException {
		for(String cmd : new String[]{
				"G1F3", "G8H6", 
				"E2E3", "D7D6", 
				"F1D3", "D8D7",
				"B2B3", "D7B5",
				"A2A3", "B5D3"})
			controller.parseInputString(cmd, true);
	}
}
