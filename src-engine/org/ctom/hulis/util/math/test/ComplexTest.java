package org.ctom.hulis.util.math.test;

import static org.junit.Assert.assertTrue;

import org.ctom.hulis.util.math.Complex;
import org.junit.Test;

public class ComplexTest {
	
	private static final double THRS=1e-10;
	private static final double sqrt2 = Math.sqrt(2);
	private static final double sqrt2p2i_real = Math.sqrt(sqrt2+1);
	private static final double sqrt2p2i_imag = Math.sqrt(sqrt2-1);
	private static final double sqrt_2_29=Math.sqrt(2./29.);

	@Test
	public void testSqrt() {
		Complex a=new Complex(2);
		a.sqrt();
		double err=Math.abs(a.getReal()-sqrt2);
		assertTrue(err<THRS);
	}

	@Test
	public void testSqrt_Complex_2_2() {
		Complex a=new Complex(2,2);
		a.sqrt();
		double err_real=Math.abs(a.getReal()-sqrt2p2i_real);
		assertTrue(err_real<THRS);
		double err_imag=Math.abs(a.getImag()-sqrt2p2i_imag);
		assertTrue(err_imag<THRS);
	}

	@Test
	public void testSqrt_Complex_m2_2() {
		Complex a=new Complex(-2,2);
		a.sqrt();
		double err_real=Math.abs(a.getReal()-sqrt2p2i_imag);
		assertTrue(err_real<THRS);
		double err_imag=Math.abs(a.getImag()-sqrt2p2i_real);
		assertTrue(err_imag<THRS);
	}

	@Test
	public void testSqrt_Complex_2_m2() {
		Complex a=new Complex(2,-2);
		a.sqrt();
		double err_real=Math.abs(a.getReal()-sqrt2p2i_real);
		assertTrue(err_real<THRS);
		double err_imag=Math.abs(a.getImag()+sqrt2p2i_imag);
		assertTrue(err_imag<THRS);
	}

	@Test
	public void testSqrt_Complex_m2_m2() {
		Complex a=new Complex(-2,-2);
		a.sqrt();
		double err_real=Math.abs(a.getReal()-sqrt2p2i_imag);
		assertTrue(err_real<THRS);
		double err_imag=Math.abs(a.getImag()+sqrt2p2i_real);
		assertTrue(err_imag<THRS);
	}

	@Test
	public void testSqrt_Complex_03() {
		Complex a=new Complex(-2./29.,0);
		a.sqrt();
		double err_real=Math.abs(a.getReal()-0);
		assertTrue(err_real<THRS);
		double err_imag=Math.abs(a.getImag()-sqrt_2_29);
		assertTrue(err_imag<THRS);
	}

}
