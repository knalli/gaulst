package schach.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import schach.brett.Farbe;
import schach.brett.Figurart;
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


public class BauerZieht {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		View.setView(View.DEVNULL);
		Logger.appendLogger("console");
		Logger.disableAllExceptTest();
		Logger.test("Test WEISSER BAUER ZIEHT gestartet..");
		
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
	
	private void execWeisserBauerZieht() throws NegativeConditionException {
		controller.parseInputString("H2H3", true);
	}
	
	
	private static IController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	// t01
	@Test
	public void b00aAllesOkay() throws NegativeConditionException{
		Logger.test("B00a Gueltiger Zug");
		Logger.noTest();
		createTestCaseOK00a();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}
	
	// t02
	@Test
	public void b00bAllesOkay() throws NegativeConditionException{
		Logger.test("B00b Gueltiger Zug");
		Logger.noTest();
		createTestCaseOK00b();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}
	
	// t03
	@Test (expected=NegativeConditionException.class)
	public void b01FarbeAmZugFalse() throws NegativeConditionException{
		Logger.test("T01 ist der Spieler weiss zugberechtigt?");
		Logger.noTest();
		CreateTestCase01();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}
	
	// t04
	@Test (expected=NegativeConditionException.class)
	public void b02NichtRemisFalse() throws NegativeConditionException{
		Logger.test("t02: ist die Partie Remis ?");
		Logger.noTest();
		createTestCase02();
		Logger.disableAllExceptTest();
		controller.parseInputString("A2A3", true);
	}
	
	// t05
	@Test(expected=NegativeConditionException.class)
	public void b03NichtPattFalse() throws NegativeConditionException{
		Logger.test("t03: ist die Partie Patt ?");
		Logger.noTest();
		createTestCase03();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}
	
	// t06
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("t04: ist die Partie Matt ?");
		Logger.noTest();
		createTestCase04();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}	
	
	// t07
	@Test (expected=NegativeConditionException.class)
	public void b05KoenigBereitsInEinerRochadeFalse() throws NegativeConditionException {
		Logger.test("t05: Befindet sich der weisse Koenig bereits in einer Rochade?");
		Logger.noTest();
		createTestCase05();
		Koenig k = (Koenig) AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0);
		Logger.disableAllExceptTest();
		k.setzeIstInEinerRochade(true);
		execWeisserBauerZieht();
	}
	
	// t08
	@Test (expected=NegativeConditionException.class)
	public void b06KeineBauernUmwandlungFalse() throws NegativeConditionException {
		Logger.test("t06: Besteht derzeit eine Bauernumwandlung bevor?");
		Logger.noTest();
		createTestCase06();
		Logger.disableAllExceptTest();
		execWeisserBauerZieht();
	}
	
	// t09
	@Test (expected=NegativeConditionException.class)
	public void b07KoenigNichtBedrohtImNaechstenZugFalse() throws NegativeConditionException {
		Logger.test("t07: Waere der weisse Koenig im naechsten Zug direkt bedroht?");
		Logger.noTest();
		createTestCase07();
		Logger.disableAllExceptTest();
		controller.parseInputString("F2F4", true);
	}
	
	// t10
	@Test (expected=NegativeConditionException.class)
	public void b08ZielfeldNichtBesetztFalse() throws NegativeConditionException {
		Logger.test("t08: Zugfeld besetzt?");
		Logger.noTest();
		createTestCase08();
		Logger.disableAllExceptTest();
		Brett.getInstance().gebeFigurVonFeld(Reihe.R2, Linie.C).zieht(Brett.getInstance().gebeFeld(Reihe.R3, Linie.C));
	}
	
	// t11
	@Test (expected=NegativeConditionException.class)
	public void b09DoppelzugFalse() throws NegativeConditionException {
		Logger.test("t09: Doppelzug erlaubt?");
		Logger.noTest();
		createTestCase09();
		Logger.disableAllExceptTest();
		controller.parseInputString("D3D5", true);
	}
	
	// t12
	@Test (expected=NegativeConditionException.class)
	public void b10GangartErlaubtFalse() throws NegativeConditionException {
		Logger.test("t10: Gangart gueltig?");
		Logger.noTest();
		createTestCase09();
		Logger.disableAllExceptTest();
		controller.parseInputString("D3E4", true);
	}
	
	
	private void CreateTestCase01() throws NegativeConditionException{
		for(String cmd : new String[]{
				"B2B3","B8C6",
				"C1B2"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase02() throws NegativeConditionException {
		for(String cmd : new String[] {"D2D4", "C7C5", "C1D2", "REMIS", "H7H5", "REMIS"})
			controller.parseInputString(cmd, true);
	}
	public void createTestCase02a() throws NegativeConditionException {
		for(String cmd : new String[] {"REMIS", "REMIS"})
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase03() throws NegativeConditionException {
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
				"D2D3", "E7E6",
				"E2E3", "D8H4" })
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase08() throws NegativeConditionException {
		for(String cmd : new String[]{"B1C3","H7H6"})
		controller.parseInputString(cmd, true);
	}
	
	public void createTestCase09() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D3", "B8A6"})
		controller.parseInputString(cmd, true);
	}
	
	public void createTestCaseOK00a() throws NegativeConditionException {
		for(String cmd : new String[]{
				"A2A3", "B8C6"})
		controller.parseInputString(cmd, true);
	}
	
	public void createTestCaseOK00b() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B2B4", "B8C6",
				"C1A3", "A8B8"})
		controller.parseInputString(cmd, true);
	}

}

