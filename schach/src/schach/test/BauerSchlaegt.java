package schach.test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IKoenig;
import schach.brett.ISchlagbareFigur;
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
		Logger.test("GŸltiger Schlagzug:Schwarzer Bauer schlaegt Weisser Bauer");
		Logger.noTest();
		createTestCaseOkaya();
		Logger.disableAllExceptTest();
		controller.parseInputString("B5C4", true);
	}
	
	@Test
	public void b00bAllesOkay() throws NegativeConditionException{
		Logger.test("Gültiger Schlagzug: Weisser Bauer schlaegt Schwarzer Bauer");
		Logger.noTest();
		createTestCaseOkayb();
		Logger.disableAllExceptTest();
		controller.parseInputString("E4D5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b01FarbeAmZugFalse() throws NegativeConditionException{
		Logger.test("B01: ist der Spieler weiss zugberechtigt?");
		Logger.noTest();
		createTestCase01();
		Logger.disableAllExceptTest();
		controller.parseInputString("D4E5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b02NichtRemisFalse() throws NegativeConditionException{
		Logger.test("B02: ist die Partie Remis?");
		Logger.noTest();
		CreateTestCase02();
		Logger.disableAllExceptTest();
		controller.parseInputString("D5E4", true);
	}
	
	@Test(expected=NegativeConditionException.class)
	public void b03NichtPattFalse() throws NegativeConditionException{
		Logger.test("B03: ist die Partie Patt ?");
		Logger.noTest();
		CreateTestCase03();
		Logger.disableAllExceptTest();
		controller.parseInputString("H5G4", true);
	}
	
	/*vor matt muss noch patt kommen*/
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("B04: ist die Partie Matt");
		Logger.noTest();
		CreateTestCase04();
		Logger.disableAllExceptTest();
		controller.parseInputString("B4A5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b05KoenigBereitsInEinerRochadeFalse() throws NegativeConditionException{
		Logger.test("B05: Befindet sich der Weisse König in einer Rochade?");
		Logger.noTest();
		createTestCase05();
		Logger.disableAllExceptTest();
		Koenig k = (Koenig) AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0);
		k.setzeIstInEinerRochade(true);
		controller.parseInputString("A4B5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b06KeineBauernUmwandlungFalse() throws NegativeConditionException{
		Logger.test("B06: Besteht derzeit eine Bauernumwandlung bevor?");
		Logger.noTest();
		createTestCase06();
		Logger.disableAllExceptTest();
		controller.parseInputString("C4B5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b07KoenigNichtBedrohtImNaechstenZugFalse() throws NegativeConditionException{
		Logger.test("B07: WŠre der Weisse Kšnig im nŠchsten Zug bedroht?");
		Logger.noTest();
		createTestCase07();
		Logger.disableAllExceptTest();
		controller.parseInputString("F7E6", true);
	}

	@Test (expected=NegativeConditionException.class)
	public void b08GangartGueltigFueSchlagzug() throws NegativeConditionException{
		Logger.test("b08: Gangart fŸr Schlagzug gŸltig?");
		Logger.noTest();
		createTestCase08();
		Logger.disableAllExceptTest();
		Brett.getInstance().gebeFigurVonFeld(Reihe.R4, Linie.E).schlaegt(Brett.getInstance().gebeFeld(Reihe.R5, Linie.E), (ISchlagbareFigur) Brett.getInstance().gebeFigurVonFeld(Reihe.R5, Linie.E));
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b09ZielFeldBesetztFalse() throws NegativeConditionException{
		Logger.test("Zielfeld beim Schlage besetzt ?");
		Logger.noTest();
		createTest09();
		Logger.disableAllExceptTest();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b10ZuSchlagendeFigurSchlagbarFalse() throws NegativeConditionException{
		Logger.test("B10: Ist zu schlagende Figur schlagbar?");
		Logger.noTest();
		createTestCase10();
		Logger.disableAllExceptTest();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b11FarbedesSchlagendenEqualsFarbedesZuschlagendeFigurFalse() throws NegativeConditionException{
		Logger.test("B11: Schlägt der Weisse Bauer eine Schwarze Figur ?");
		Logger.noTest();
		createTestCase11();
		Logger.disableAllExceptTest();
	}


	private void createTestCase01() throws NegativeConditionException{
		/*WEiss zugberechtugt?
		1.	e2-e4	d7-d5
		2.	Sg1-h3	
		E4D5*/
		for(String cmd : new String[]{
				"E2E4","D7D5"})
			controller.parseInputString(cmd, true);
	}
	private void CreateTestCase02() throws NegativeConditionException{
		/*1.	e2-e4	d7-d5
			2.	Sg1-h3 (=)	*/
		for(String cmd : new String[]{
				"E2E4","REMIS", "D7D5","REMIS"})
			controller.parseInputString(cmd, true);
	}
	
	private void CreateTestCase03() throws NegativeConditionException{

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
				"G2G4", "E7E6",
				"C8E6", "F8E7",
				"E6D7", "F6F5", 
				"D7E6", "E7F6", 
				"E6C8", "F6C3",
				"D2C3", "F5F4",
				"C8E6", "G8F6",
				"E6D7", "F6D5", 
				"D7D5", "F4F3",
				"D5E6"})
			controller.parseInputString(cmd, true);
		
	}
	private void CreateTestCase04() throws NegativeConditionException{

		for(String cmd : new String[]{
				"B2B4","A7A5",
				"F2F3", "E7E6",
				"G2G4", "D8H4"})
			controller.parseInputString(cmd, true);
	}
	
	private void createTestCase05() throws NegativeConditionException{
		// TODO Auto-generated method stub
		/*kurze rochade
		1.	g2-g4	c7-c5
		2.	Sg1-h3	b7-b6
		3.	d2-d4	f7-f6
		4.	Lf1-g2	a7-a6
		5.	0-0	
		C5D4	
		E1G1
		H1F1*/
		for(String cmd : new String[]{
				"A2A4", "B7B5"})
			controller.parseInputString(cmd, true);	
	}
	
	private void createTestCase06() throws NegativeConditionException{
		/*Bauernumwandlung
		1.	d2-d4	c7-c5
		2.	h2-h4	g7-g5
		3.	d4xc5	d7-d6
		4.	c5-c6	Lc8-d7
		5.	c6-c7	Ld7-e6
		C7C8,G5H4*/
		for(String cmd : new String[]{
				"D2D4", "C7C5",
				"H2H4", "G7G5",
				"D4C5", "D7D6",
				"C5C6", "C8D7",
				"C2C4", "B7B5",
				"C6C7", "D7E6", 
				"C7C8"})
			controller.parseInputString(cmd, true);	
	}
	private void createTestCase07() throws NegativeConditionException{
		// TODO Auto-generated method stub
		/*König Bedroht
		1.	d2-d4	e7-e5
		2.	d4xe5	Ke8-e7
		3.	e5-e6	d7-d6
		4.	Sg1-h3	Ke7-f6
		5.	Sh3-g5	Kf6-f5
		6.	g2-g4+	
		A5B4*/
		for(String cmd : new String[]{
				"D2D4", "E7E5", 
				"D4E5", "E8E7", 
				"E5E6", "D7D6", 
				"G1H3", "E7F6", 
				"H3G5", "F6F5",
				"G2G4"})
			controller.parseInputString(cmd, true);
		
	}
	
	private void createTestCase08() throws NegativeConditionException{
	
		for(String cmd : new String[]{
				"E2E4", "E7E5"})
			controller.parseInputString(cmd, true);
		
	}
	
	private void createTest09() throws NegativeConditionException{

		//1.	d2-d4	c7-c6
		for(String cmd : new String[]{
				"D2D4","C7C6","D4C5"})
			controller.parseInputString(cmd, true);
		
		
	}
	

	private void createTestCase10() throws NegativeConditionException{
		// TODO Auto-generated method stub
		/*1.	e2-e4	d7-d5
		2.	Ke1-e2	d5-d4
		3.	e4-e5	d4-d3+
		4.	Ke2-e3	*/
		for(String cmd : new String[]{
				"E1E4","D7D5",
				"E1E2","D5D4",
				"E4E5","D4D3",
				"E2E3","D3E3"})
			controller.parseInputString(cmd, true);
		
	}
	

	private void createTestCase11() throws NegativeConditionException{
		// TODO Auto-generated method stub
		/**Farbe==Farbe
		1.	d2-d4	Sb8-a6
		2.	e2-e3	b7-b6
		E3E4
		 */
		for(String cmd : new String[]{
				"D2D4","B8A6",
				"E2E3","B7B6",
				"E3D4"})
			controller.parseInputString(cmd, true);
		
	}

	private void createTestCaseOkayb() throws NegativeConditionException{
			
		/*1.	e2-e4	d7-d5
		2.	e4xd5	*/
		for(String cmd : new String[]{
				"E2E4","D7D5"})
			controller.parseInputString(cmd, true);
		
		
	}
	
	private void createTestCaseOkaya() throws NegativeConditionException {
		/*1.	c2-c4	b7-b5
		2.	d2-d3	b5xc4*/
		for(String cmd : new String[]{
				"C2C4", "B7B5",
				"D2D3"})
		
		controller.parseInputString(cmd, true);
		
	}


	private static IController controller;
}
