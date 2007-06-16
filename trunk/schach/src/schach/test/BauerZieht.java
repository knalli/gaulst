package schach.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import schach.brett.Farbe;
import schach.brett.Figurart;
import schach.brett.IBauer;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.AlleFiguren;
import schach.brett.internal.Brett;
import schach.brett.internal.Bauer;
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
	
	private void execWeisserBauerZieht() throws NegativeConditionException {
		controller.parseInputString("E2E3", true);
	}
	
	private static IController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	
	@Test
	public void b00aAllesOkay() throws NegativeConditionException{
		Logger.test("B00a Gueltiger Zug");
		createTestCaseOK00a();
	}

	@Test
	public void b00bAllesOkay() throws NegativeConditionException{
		Logger.test("B00b Gueltiger Zug");
		createTestCaseOK00b();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b01FarbeAmZugFalse() throws NegativeConditionException{
		Logger.test("B01: ist der Spieler weiss zugberechtigt?");
		CreateTestCase01();
		execWeisserBauerZieht();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b02NichtRemisFalse() throws NegativeConditionException{
		Logger.test("B02: ist die Partie Remis ?");
		//CreateTestCase02();
	}
	@Test(expected=NegativeConditionException.class)
	public void b03NichtPattFalse() throws NegativeConditionException{
		Logger.test("B03: ist die Partie Patt ?");
		//CreateTestCase03();
	}
	
	@Test (expected=NegativeConditionException.class)
	public void b04NichtMattFalse() throws NegativeConditionException{
		Logger.test("B04: ist die Partie Matt ?");
		//CreateTestCase04();
	}	
	
	private void CreateTestCase01() throws NegativeConditionException{
		for(String cmd : new String[]{
				"A2A3","B8C6",
				"C1A3"})
			controller.parseInputString(cmd, true);
	}
	
	private void CreateTestCase02() throws NegativeConditionException{
		for(String cmd : new String[]{
				"E7E5","D1C1",
				"D4E5","REMIS",
				"B6B5","D4E5"})
			controller.parseInputString(cmd, true);
	}
	
	private void CreateTestCase03() throws NegativeConditionException{
		for(String cmd : new String[]{
				"A4C5", "D4D6",
				"D2E5", "F4E4",
				"H3E7", "H2H4",
				"A3E6", "G3B3",
				"D2A5", "D5E3",
				"C4F5", "F3F4"})
			controller.parseInputString(cmd, true);
	}
	private void CreateTestCase04() throws NegativeConditionException{

		for(String cmd : new String[]{
				"F2F3", "E7E6",
				"G2G4", "D8H8"})
			controller.parseInputString(cmd, true);
	}

	public void createTestCase06() throws NegativeConditionException {
		for(String cmd : new String[]{
				"B2B4", "B7B5",
				"C2C4", "B5C4",
				"B4B5", "B8C6",
				"B5B6", "C8A6",
				"B6B7", "A8A6",
				"B7B8", "C4C3" })
			controller.parseInputString(cmd, true);
	}
			
	public void createTestCase07() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D3", "E7E6",
				"E2E3", "D8H4",
				"F2F4" })
			controller.parseInputString(cmd, true);
	}
	
	public void createTestCase09() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D3", "B8A6", "D3D5"})
		controller.parseInputString(cmd, true);
	}
	
	public void createTestCase10() throws NegativeConditionException {
		for(String cmd : new String[]{"H2G3"})
		controller.parseInputString(cmd, true);
	}

	public void createTestCase11() throws NegativeConditionException {
		for(String cmd : new String[]{
				"G1F3", "E6E4", "G2G4"})
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

