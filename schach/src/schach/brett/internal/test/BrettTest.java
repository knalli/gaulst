package schach.brett.internal.test;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import schach.brett.IBrett;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Brett;

public class BrettTest {
	private  IBrett brett;
	
	@Before
	public void setUp() throws Exception {
		brett = Brett.getInstance();
	}

	@Test
	public void gebeFeld() {
		assertEquals(brett.gebeFeld(Reihe.R6, Linie.A).gebeLinie(),Linie.A);
		assertEquals(brett.gebeFeld(Reihe.R8, Linie.H).gebeLinie(),Linie.H);
		assertEquals(brett.gebeFeld(Reihe.R1, Linie.D).gebeReihe(),Reihe.R1);
		assertEquals(brett.gebeFeld(Reihe.R8, Linie.D).gebeReihe(),Reihe.R8);
	}
}
