package schach.brett.internal.test;


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import schach.brett.IBrett;
import schach.brett.IFeld;
import schach.brett.Linie;
import schach.brett.Reihe;
import schach.brett.internal.Brett;
import schach.system.NegativeConditionException;

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
	
	@Test
	public void wegTest(){
		IFeld start1 = brett.gebeFeld(Reihe.R2, Linie.A);
		IFeld ende1 = brett.gebeFeld(Reihe.R8, Linie.A);
		IFeld start2 = brett.gebeFeld(Reihe.R4, Linie.A);
		IFeld ende2 = brett.gebeFeld(Reihe.R5, Linie.A);
		
		List<IFeld> weg1 = new ArrayList<IFeld>();
		List<IFeld> weg2 = new ArrayList<IFeld>();
		weg1.add(brett.gebeFeld(Reihe.R3, Linie.A));
		weg1.add(brett.gebeFeld(Reihe.R4, Linie.A));
		weg1.add(brett.gebeFeld(Reihe.R5, Linie.A));
		weg1.add(brett.gebeFeld(Reihe.R6, Linie.A));
		weg1.add(brett.gebeFeld(Reihe.R7, Linie.A));
		
		try {
			assertEquals(weg1, brett.gebeFelderInLinie(start1, ende1));
		} catch (NegativeConditionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			assertEquals(weg2, brett.gebeFelderInLinie(start2, ende2));
		} catch (NegativeConditionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
