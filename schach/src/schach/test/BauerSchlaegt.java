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
	
	/*vor matt muss noch patt kommen*/
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("B04: ist die Partie Matt");
		CreateTestCase04();
		
	}


	private void CreateTestCase01() throws NegativeConditionException{
	
		for(String cmd : new String[]{
				"E7E5","D1C1","D4E5"})
			controller.parseInputString(cmd, true);
		
		
	}
	private void CreateTestCase02() throws NegativeConditionException{
		// TODO Auto-generated method stub
		for(String cmd : new String[]{
				"E7E5","D1C1","D4E5","REMIS","B6B5","D4E5"})
			controller.parseInputString(cmd, true);
		
	}
	
	private void CreateTestCase04() throws NegativeConditionException{
		// TODO Auto-generated method stub
		/*.	a2-a4	Lc8-b7
		7.	d4-d5	Dd8-c8
		8.	Ld2-e3	Dc8-c7
		9.	d5-d6	Dc7-c6
		10.	h2-h4	Dc6-c5
		11.	b2-b4	a7-a6
		12.	b4xc5	Sg8-h6
		13.	Sg1-f3	Ke8-d8
		14.	c5-c6	g7-g6
		15.	c6-c7+	*/
		for(String cmd : new String[]{
				"A2-A4","C8-B7","D4-D5","D8-C8","D2-E3","C8-C7",
				"D5-D6","C7-C6","H2-H4","C6-C5","B2-B4","G8-H6","G1-F3",
				"E8-D8","C5-C6","G7-G6","C6-C7"})
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
