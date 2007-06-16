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
		//execSchwarzerBauerSchlaegtWeisserBauer();
	}
	
	@Test
	public void b00bAllesOkay() throws NegativeConditionException{
		Logger.test("Gültiger Schlagzug: Weisser Bauer schlaegt Schwarzer Bauer");
		createTestCaseOkayb();
		//execweisserBauerSchlaegtSchwarzerBauer();
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
		//execSchwarzerBauerSchlaegtWeisserBauer();
	}
	
	/*vor matt muss noch patt kommen*/
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("B04: ist die Partie Matt");
		CreateTestCase04();
		//execweisserBauerSchlaegtSchwarzerBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b05KoenigBereitsInEinerRochadeFalse() throws NegativeConditionException{
		Logger.test("B05: Befindet sich der Weisse König in einer Rochade?");
		createTestCase05();
		Koenig k = (Koenig) AlleFiguren.getInstance().gebeFiguren(Figurart.KOENIG, Farbe.WEISS).get(0);
		k.setzeIstInEinerRochade(true);
		//execweisserBauerSchlaegtSchwarzerBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b06KeineBauernUmwandlungFalse() throws NegativeConditionException{
		Logger.test("B06: Besteht derzeit eine Bauernumwandlung bevor ?");
		createTestCase06();
		//execSchwarzerBauerSchlaegtWeisserBauer();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b07KoenigNichtBedrohtImNaechstenZugFalse() throws NegativeConditionException{
		Logger.test("B07: Wäre der Weisse König im nächsten Zug bedroht ?");
		createTestCase07();
		//execweisserBauerSchlaegtSchwarzerBauer();
		
	}

	@Test (expected=NegativeConditionException.class)
	public void b08GangartGueltigFueSchlagzug() throws NegativeConditionException{
		Logger.test("b08: Gangart für Schlagzug gültig ?");
		createTestCase08();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b09ZielFeldBesetztFalse() throws NegativeConditionException{
		Logger.test("Zielfeld beim Schlage besetzt ?");
		createTest09();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b10ZuSchlagendeFigurSchlagbarFalse() throws NegativeConditionException{
		Logger.test("B10: Ist zu schlagende Figur schlagbar?");
		createTestCase10();
		
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b11FarbedesSchlagendenEqualsFarbedesZuschlagendeFigurFalse() throws NegativeConditionException{
		Logger.test("B11: Schlägt der Weisse Bauer eine Schwarze Figur ?");
		createTestCase11();
		
	}


	private void CreateTestCase01() throws NegativeConditionException{
		/*WEiss zugberechtugt?
		1.	e2-e4	d7-d5
		2.	Sg1-h3	
		E4D5*/
		for(String cmd : new String[]{
				"E2E4","D7D5","G1H3",
				"E4D5"})
			controller.parseInputString(cmd, true);
	}
	private void CreateTestCase02() throws NegativeConditionException{
		/*1.	e2-e4	d7-d5
			2.	Sg1-h3 (=)	*/
		for(String cmd : new String[]{
				"E2E4","D7D5","REMIS","D5E4"})
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
				"B2B4","A7A5",
				"F2F3", "E7E6",
				"G2G4", "D8H4",
				"B4A5"})
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
				"G2G4", "C7C5",
				"G1H3", "B7B6",
				"D2D4", "F7F6",
				"F1G2", "A7A6",
				"E1G1","C5D4","H1F1"})
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
				"C7C8", "G5H5"})
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
				"G2G4", "A5B4"})
			controller.parseInputString(cmd, true);
		
	}
	
	private void createTestCase08() throws NegativeConditionException{
	
		for(String cmd : new String[]{
				"E2E4", "E7E5", 
				"E4E5"})
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
				"E2E4","D7D5",
				"E4D5"})
			controller.parseInputString(cmd, true);
		
		
	}
	
	private void createTestCaseOkaya() throws NegativeConditionException {
		/*1.	c2-c4	b7-b5
		2.	d2-d3	b5xc4*/
		for(String cmd : new String[]{
				"C2C4", "B7B5",
				"D2D3","B5C4"})
		
		controller.parseInputString(cmd, true);
		
	}


	private static IController controller;
}
