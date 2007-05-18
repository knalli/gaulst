/**
 * 
 */
package schach.test;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import schach.brett.internal.Brett;
import schach.partie.internal.Partie;
import schach.system.IController;
import schach.system.Logger;
import schach.system.NegativeConditionException;
import schach.system.View;
import schach.system.internal.Controller;


/**
 * @author knalli
 *
 */
public class WeisserBauerZiehtTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		View.setView(View.TEXT);
		Logger.appendLogger("console");
		Logger.debug("Test gestartet..");
		
		Brett.getInstance();
		Partie.getInstance().start();
		
		controller = Controller.getInstance();
		controller.parseInputString("H2H3");
		controller.parseInputString("G7G6");
		controller.parseInputString("B2B4");
		controller.parseInputString("A7A5");
		controller.parseInputString("B4B5");
		controller.parseInputString("C7C5");
	}
	
	private static IController controller;

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	@Test (expected=NegativeConditionException.class)
	public void testeDreifachZug() throws NegativeConditionException{
		controller.parseInputString("E2E5", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void testeEnpassentSchlazugOhneGegner() throws NegativeConditionException {
		controller.parseInputString("E2F3", true);
	}
	
	@Test (expected=NegativeConditionException.class)
	public void testeDoppelzugBeiBereitsBewegt() throws NegativeConditionException {
		controller.parseInputString("H3H5", true);
	}
}
