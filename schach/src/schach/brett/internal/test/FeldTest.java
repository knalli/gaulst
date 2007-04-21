package schach.brett.internal.test;

import org.junit.Test;
import static org.junit.Assert.*;

import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Feld;


public class FeldTest {
	@Test
	public void gueltigeFelder(){
		IFeld feld1 = new Feld(Reihe.R1, Linie.A);
		IFeld feld2 = new Feld(Reihe.R7, Linie.D);
		
		IFeld feld3 = null;
		try {
			feld3 = new Feld(Reihe.R1, null);
		} catch (RuntimeException e) { }
		assertNull(feld3);
		
		try {
			feld3 = new Feld(null, Linie.D);
		} catch (RuntimeException e) { }

		assertNull(feld3);
		
		try {
			feld3 = new Feld(null, null);
		} catch (RuntimeException e) { }
		assertNull(feld3);
		
		assertEquals(feld1.gebeLinie(), Linie.A);
		assertEquals(feld2.gebeLinie(), Linie.D);
		assertEquals(feld1.gebeReihe(), Reihe.R1);
		assertEquals(feld2.gebeReihe(), Reihe.R7);
	}
}
