package schach.brett.internal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Brett;
import schach.brett.internal.Feld;
import schach.system.NegativeConditionException;


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
	
	@Test
	public void gueltigeEinheiten() {
		assertEquals(Reihe.R1.naechste(), Reihe.R2);
		assertEquals(Reihe.R7.naechste(), Reihe.R8);
		assertNull(Reihe.R8.naechste());
		
		assertEquals(Linie.A.naechste(), Linie.B);
		assertEquals(Linie.G.naechste(), Linie.H);
		assertNull(Linie.H.naechste());
		
		assertEquals(Reihe.R2.vorherige(), Reihe.R1);
		assertEquals(Reihe.R8.vorherige(), Reihe.R7);
		assertNull(Reihe.R1.vorherige());
		
		assertEquals(Linie.B.vorherige(), Linie.A);
		assertEquals(Linie.H.vorherige(), Linie.G);
		assertNull(Linie.A.vorherige());
	}
	
	@Test
	public void naechsteFelder() {
		IBrett brett = Brett.getInstance();
		IFeld feld1a = brett.gebeFeld(Reihe.R1, Linie.A);
		IFeld feld1b = brett.gebeFeld(Reihe.R2, Linie.A);
		IFeld feld2a = brett.gebeFeld(Reihe.R6, Linie.E);
		IFeld feld2b = brett.gebeFeld(Reihe.R6, Linie.F);
		IFeld feld3 = brett.gebeFeld(Reihe.R8, Linie.A);
		IFeld feld4 = brett.gebeFeld(Reihe.R1, Linie.H);
		
		try {
			assertEquals(feld2a.plusLinie(1), feld2b);
			assertEquals(feld1a.plusReihe(1), feld1b);
			
			assertEquals(feld2b.minusLinie(1), feld2a);
			assertEquals(feld1b.minusReihe(1), feld1a);
			
			assertEquals(feld3.minusReihe(7).plusLinie(7), feld4);
		} catch (NegativeConditionException e) { 
			assertNull(1); // gibt fehler!
		}		

		try {
			feld3.plusReihe(1);
			
			assertNull(1); // gibt fehler!
		} catch (NegativeConditionException e){	}
	}
}
