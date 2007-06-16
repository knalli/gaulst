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


public class BauerSchlaegt {
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		View.setView(View.DEVNULL);
		Logger.appendLogger("console");
		Logger.disableAllExceptTest();
		Logger.test("Test Weisser Bauer Schlägt startet..");
		
		Brett.getInstance();
		Partie.getInstance().start();
		
		controller = Controller.getInstance();
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
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
	private void execSchwarzerBauerSchlaegtWeisserBauer() throws  NegativeConditionException{
		controller.parseInputString("C5D4");
	
	}
	
	private void execweisserBauerSchlaegtSchwarzerBauer() throws  NegativeConditionException{
		controller.parseInputString("E3D4");
	
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void b00aAllesOkay()throws NegativeConditionException{
		Logger.test("Gültiger Schlagzug:Schwarzer Bauer schlaegt Weisser Bauer");
		createTestCaseOkaya();
		execSchwarzerBauerSchlaegtWeisserBauer();
	}
	
	@Test
	public void b00bAllesOkay() throws NegativeConditionException{
		Logger.test("Gültiger Schlagzug: Weisser Bauer schlaegt Schwarzer Bauer");
		createTestCaseOkayb();
		execweisserBauerSchlaegtSchwarzerBauer();
	}
	@Test (expected=NegativeConditionException.class)
	public void b01FarbeAmZugFalse() throws NegativeConditionException{
		Logger.test("B01: ist der Spieler weiss zugberechtigt?");
		CreateTestCase01();
	}
	@Test (expected=NegativeConditionException.class)
	public void b02NichtRemisFalse() throws NegativeConditionException{
		Logger.test("B02: ist die Partie Remis?");
		CreateTestCase02();
		
	}
	@Test(expected=NegativeConditionException.class)
	public void b03NichtPattFalse() throws NegativeConditionException{
		Logger.test("B03: ist die Partie Patt ?");
		CreateTestCase03();
		execSchwarzerBauerSchlaegtWeisserBauer();
	}
	
	/*vor matt muss noch patt kommen*/
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("B04: ist die Partie Matt");
		CreateTestCase04();
		execweisserBauerSchlaegtSchwarzerBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b05KoenigBereitsInEinerRochadeFalse() throws NegativeConditionException{
		Logger.test("B05: Befindet sich der Weisse König in einer Rochade?");
		createTestCase05();
		Koenig k = (Koenig) AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0);
		k.setzeIstInEinerRochade(true);
		execweisserBauerSchlaegtSchwarzerBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b06KeineBauernUmwandlungFalse() throws NegativeConditionException{
		Logger.test("B06: Besteht derzeit eine Bauernumwandlung bevor ?");
		createTestCase06();
		execSchwarzerBauerSchlaegtWeisserBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b07KoenigNichtBedrohtImNaechstenZugFalse() throws NegativeConditionException{
		Logger.test("B07: Wäre der Weisse König im nächsten Zug bedroht ?");
		createTestCase07();
		execweisserBauerSchlaegtSchwarzerBauer();
		
	}


	private void CreateTestCase01() throws NegativeConditionException{
		for(String cmd : new String[]{
				"E7E5","D1C1","D4E5"})
			controller.parseInputString(cmd, true);
	}
	private void CreateTestCase02() throws NegativeConditionException{
		for(String cmd : new String[]{
				"E7E5","D1C1","D4E5","REMIS","B6B5","D4E5"})
			controller.parseInputString(cmd, true);
	}
	
	private void CreateTestCase03() throws NegativeConditionException{


		for(String cmd : new String[]{
				"A4C5", "D4D6",
				"D2E5", "F4E4",
				"H3E7", "H2H4",
				"A3E6", "G3B3",
				"D2A5", "D5E3 ",
				"C4F5", "F3F4"})
			controller.parseInputString(cmd, true);
		
	}
	private void CreateTestCase04() throws NegativeConditionException{

		for(String cmd : new String[]{
				"F2F3", "E7E6",
				"G2G4", "D8H8"})
			controller.parseInputString(cmd, true);
	}
	
	private void createTestCase05() throws NegativeConditionException{
		// TODO Auto-generated method stub
		for(String cmd : new String[]{
				"B1A3", "B8A6",
				"D2D4", "D7D5",
				"D1D3", "D8D6",
				"C1D2", "C8D7"})
			controller.parseInputString(cmd, true);	
	}
	
	private void createTestCase06() throws NegativeConditionException{

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
	
	private void createTestCase07() throws NegativeConditionException{
		// TODO Auto-generated method stub
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
	private void createTestCaseOkayb() throws NegativeConditionException{
	
		for(String cmd : new String[]{
				"E2E3","B7B6"})
			controller.parseInputString(cmd, true);
		
		
	}

	private void createTestCaseOkaya() throws NegativeConditionException {
	
		for(String cmd : new String[]{
				"D2D4", "C7C5",
				"C1D2","C5D4"})
		
		controller.parseInputString(cmd, true);
		
	}
	private static IController controller;
}
