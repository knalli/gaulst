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

	/**
	 * @throws java.lang.Exception
	 */
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
	private static IController controller;
	
/*		
	B1	Spieler ist zugberechtigt
	B2	Partie ist nicht Remis
	B3	Partie ist nicht Schach
	B4	Partie ist nicht Schachmatt
	B5	Der König befindet sich nicht in einer Rochade
	B6	Es besteht keine Bauernumwandlung
	B7	König wäre nach dem Zug nicht bedroht
	B8	Das Zielfeld ist nicht besetzt
	B9	Bauer macht einen Doppelschritt und wurde zuvor noch nicht bewegt
	B10	Die Gangart ist gültig
	B11	Der Zugweg frei

*/
	
	/* B1 Spieler ist zugberechtigt
	 * beim aufrufen des tests versucht weißer spieler erneut zu ziehen 
	 */
	public void createTestCase01() throws NegativeConditionException {
		for(String cmd : new String[] {"D2D4", "D7D5","E2E3"})
			controller.parseInputString(cmd, true);
	}
	/*
	 * B2: Partie ist nicht Remis
	 */
	
	/*
	 * B3: Partie ist nicht Schach
	 * Weißer Bauer versucht B2B3. König steht noch im schach
	 */
	public void createTestCase03() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D4", "B8A6",
				"D1D3", "A6C5",
				"D3H7", "C5D3"}) //könig wird bedroht

			controller.parseInputString(cmd, true);
	}
	/*
	 * B4: Partie ist nicht Schachmatt
	 */
	
	/*
	 * B5: Der König befindet sich nicht in einer Rochade 
	 */

	/*
	 * B6: Es besteht keine Bauernumwandlung
	 * Weißer Bauer zieht auf B8 schwarzer Bauer versucht C4C3
	 */
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
	
	/*
	 * B7: König wäre nach dem Zug nicht bedroht
	 * Weißer Bauer versucht F2F4 obwohl dann schwarze Dame König bedroht (D8H4)
	 */
	
	public void createTestCase07() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D3", "E7E6",
				"E2E3", "D8H4",
				"F2F4" })
			controller.parseInputString(cmd, true);
	}
	
	/*
	 * B8: Das Zielfeld ist nicht besetzt
	 */

	/*
	 * B9: Bauer macht einen Doppelschritt und wurde zuvor noch nicht bewegt
	 * Bauer wurde schon bewegt und versucht doppelschritt 
	 */

	public void createTestCase09() throws NegativeConditionException {
		for(String cmd : new String[]{
				"D2D3", "B8A6", "D3D5"})
		controller.parseInputString(cmd, true);
	}
	/*
	 * B10: Die Gangart ist gültig
	 * Bauer versucht zum Auftakt des Spiels diagonal zu ziehen
	 */
	public void createTestCase10() throws NegativeConditionException {
		for(String cmd : new String[]{"H2G3"})
		controller.parseInputString(cmd, true);
	}

	/*
	 * B11: Der Zugweg frei
	 * Bauer versucht über eigene Figur mit Doppelschritt zu springen 
	 */
	public void createTestCase11() throws NegativeConditionException {
		for(String cmd : new String[]{
				"G1F3", "E6E4", "G2G4"})
		controller.parseInputString(cmd, true);
	}
	
	





}

