package schach.brett.internal.test;


import static org.junit.Assert.*;
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
	public void wegInLinie(){
		/*
		 * 1) A2 -> A8 gültig: {A3,A4,A5,A6,A7}
		 * 2) A4 -> A5 gültig: {}
		 * 3) A4 -> H5 ungültig
		 */
		IFeld a2 = brett.gebeFeld(Reihe.R2, Linie.A);
		IFeld a8 = brett.gebeFeld(Reihe.R8, Linie.A);
		IFeld a4 = brett.gebeFeld(Reihe.R4, Linie.A);
		IFeld a5 = brett.gebeFeld(Reihe.R5, Linie.A);
		IFeld h5 = brett.gebeFeld(Reihe.R5, Linie.H);
		
		List<IFeld> weg_a2_a8 = new ArrayList<IFeld>();
		List<IFeld> weg_a4_a5 = new ArrayList<IFeld>();
		weg_a2_a8.add(brett.gebeFeld(Reihe.R3, Linie.A));
		weg_a2_a8.add(brett.gebeFeld(Reihe.R4, Linie.A));
		weg_a2_a8.add(brett.gebeFeld(Reihe.R5, Linie.A));
		weg_a2_a8.add(brett.gebeFeld(Reihe.R6, Linie.A));
		weg_a2_a8.add(brett.gebeFeld(Reihe.R7, Linie.A));
		
		// Teste 1
		try {
			assertEquals(weg_a2_a8, brett.gebeFelderInLinie(a2, a8));
		} catch (NegativeConditionException e) { 
			assertNull(1);
		}
		
		// Teste 2
		try {
			assertEquals(weg_a4_a5, brett.gebeFelderInLinie(a4, a5));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}		
		
		// Teste ungültig
		try {
			brett.gebeFelderInLinie(a4, h5);
			assertNull(1);
		} catch (NegativeConditionException e) { }		
		
	}
	
	@Test
	public void wegInReihe(){
		/*
		 * 1) A2 -> H2 gültig: {B2,B3,B4,B5,B6,B7}
		 * 2) A4 -> H4 gültig: {}
		 * 3) A4 -> H5 ungültig
		 */
		IFeld a2 = brett.gebeFeld(Reihe.R2, Linie.A);
		IFeld a4 = brett.gebeFeld(Reihe.R4, Linie.A);
		IFeld h2 = brett.gebeFeld(Reihe.R2, Linie.H);
		IFeld h8 = brett.gebeFeld(Reihe.R8, Linie.H);
		IFeld b4 = brett.gebeFeld(Reihe.R4, Linie.B);
		
		List<IFeld> weg_a2_h2 = new ArrayList<IFeld>();
		List<IFeld> weg_a4_b4 = new ArrayList<IFeld>();
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.B));
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.C));
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.D));
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.E));
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.F));
		weg_a2_h2.add(brett.gebeFeld(Reihe.R2, Linie.G));
		
		// Teste 1
		try {
			assertEquals(weg_a2_h2, brett.gebeFelderInReihe(a2, h2));
		} catch (NegativeConditionException e) { 
			assertNull(1);
		}
		
		// Teste 2
		try {
			assertEquals(weg_a4_b4, brett.gebeFelderInReihe(a4, b4));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}		
		
		// Teste ungültig
		try {
			brett.gebeFelderInReihe(a4, h8);
			assertNull(1);
		} catch (NegativeConditionException e) { }		
	}
	
	@Test
	public void wegInDiagonale() {
		/*
		 * 1) B2 -> G4 ungültig
		 * 2) C2 -> F5 gültig: {D3,E4}
		 * 3) A1 -> H8 gültig: {B2,C3,D4,E5,F6,G7}
		 * 4) G8 -> E5 ungültig
		 * 5) G8 -> D5 gültig: {F7,E6}
		 * 6) D5 -> G8 gültig: {E6,F7}
		 */
		
		IFeld b2 = brett.gebeFeld(Reihe.R2, Linie.B);
		IFeld g4 = brett.gebeFeld(Reihe.R4, Linie.G);
		IFeld c2 = brett.gebeFeld(Reihe.R2, Linie.C);
		IFeld d3 = brett.gebeFeld(Reihe.R3, Linie.D);
		IFeld e4 = brett.gebeFeld(Reihe.R4, Linie.E);
		IFeld a1 = brett.gebeFeld(Reihe.R1, Linie.A);
		IFeld c3 = brett.gebeFeld(Reihe.R3, Linie.C);
		IFeld d4 = brett.gebeFeld(Reihe.R4, Linie.D);
		IFeld e5 = brett.gebeFeld(Reihe.R5, Linie.E);
		IFeld f5 = brett.gebeFeld(Reihe.R5, Linie.F);
		IFeld f6 = brett.gebeFeld(Reihe.R6, Linie.F);
		IFeld g7 = brett.gebeFeld(Reihe.R7, Linie.G);
		IFeld h8 = brett.gebeFeld(Reihe.R8, Linie.H);
		IFeld g8 = brett.gebeFeld(Reihe.R8, Linie.G);
		IFeld f7 = brett.gebeFeld(Reihe.R7, Linie.F);
		IFeld e6 = brett.gebeFeld(Reihe.R6, Linie.E);
		IFeld d5 = brett.gebeFeld(Reihe.R5, Linie.D);
		
		List<IFeld> weg_c2_f5 = new ArrayList<IFeld>();
		weg_c2_f5.add(d3);
		weg_c2_f5.add(e4);
		
		List<IFeld> weg_a1_h8 = new ArrayList<IFeld>();
		weg_a1_h8.add(b2);
		weg_a1_h8.add(c3);
		weg_a1_h8.add(d4);
		weg_a1_h8.add(e5);
		weg_a1_h8.add(f6);
		weg_a1_h8.add(g7);
		
		List<IFeld> weg_g8_d5 = new ArrayList<IFeld>();
		weg_g8_d5.add(f7);
		weg_g8_d5.add(e6);
		
		List<IFeld> weg_d5_g8 = new ArrayList<IFeld>();
		weg_d5_g8.add(e6);
		weg_d5_g8.add(f7);
		
		// Teste 1
		try {
			brett.gebeFelderInDiagonalen(b2, g4);
			assertNull(1);
		} catch (NegativeConditionException e) { }
		
		// Teste 2
		try {
			assertEquals(weg_c2_f5, brett.gebeFelderInDiagonalen(c2, f5));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}
		
		// Teste 3
		try {
			assertEquals(weg_a1_h8, brett.gebeFelderInDiagonalen(a1,h8));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}
		
		// Teste 4
		try {
			brett.gebeFelderInDiagonalen(g8, e5);
			assertNull(1);
		} catch (NegativeConditionException e) { }
		
		// Teste 5
		try {
			assertEquals(weg_g8_d5, brett.gebeFelderInDiagonalen(g8,d5));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}
		
		// Teste 6
		try {
			assertEquals(weg_d5_g8, brett.gebeFelderInDiagonalen(d5,g8));
		} catch (NegativeConditionException e) {
			assertNull(1);
		}
	}
}
