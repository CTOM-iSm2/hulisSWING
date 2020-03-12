package org.ctom.hulis.polynomes.test;

import static org.junit.Assert.assertTrue;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.hulis.util.math.Complex;
import org.junit.Before;
import org.junit.Test;

public class PolynomialTest {
	
	private Polynomial p;
	private Polynomial q;
	private Polynomial r;
	private Polynomial s;
	
	private static final double THRS=1e-10;

	@Before
	public void setUp() throws Exception {
		p = new Polynomial(new double[]{2.0,-1.0,1.0});
		q = new Polynomial(new double[]{1.0,1.0,1.0});
		r = new Polynomial(new double[]{-3,-2,-2,1});
		s = new Polynomial(new double[]{-3,1});
	}

	@Test
	public void testGetDegree() {
		assertTrue(p.getDegree()==2);
	}

	@Test
	public void testGetCoefficients() {
		assertTrue(p.getCoefficients().length==3);
		assertTrue(p.getCoefficients()[0]==2);
		assertTrue(p.getCoefficients()[1]==-1);
		assertTrue(p.getCoefficients()[2]==1);
	}

	@Test
	public void testGetCoeffOfDegree() {
		assertTrue(p.getCoeffOfDegree(0)==2);
		assertTrue(p.getCoeffOfDegree(1)==-1);
		assertTrue(p.getCoeffOfDegree(2)==1);
		assertTrue(p.getCoeffOfDegree(3)==0);
	}

	@Test
	public void testToString() {
		String str="x^2 -x +2.0";
		assertTrue("|"+p.toString()+"|==|"+str+"| ?"+(p.toString().equals(str)),p.toString().equals(str));
	}
	
	@Test
	public void testEval() {
		double x=-1;
		assertTrue(p.eval(x)==4);
	}
	
	@Test
	public void testAdd() {
		Polynomial sum = p.add(q);
		assertTrue(sum.getCoeffOfDegree(0)==3);
		assertTrue(sum.getCoeffOfDegree(1)==0);
		assertTrue(sum.getCoeffOfDegree(2)==2);
	}

	@Test
	public void testMultiply() {
		Polynomial prod = p.multiply(q);
		assertTrue(prod.getDegree()==4);
		assertTrue(prod.getCoeffOfDegree(0)==2);
		assertTrue(prod.getCoeffOfDegree(1)==1);
		assertTrue(prod.getCoeffOfDegree(2)==2);
		assertTrue(prod.getCoeffOfDegree(3)==0);
		assertTrue(prod.getCoeffOfDegree(4)+"",prod.getCoeffOfDegree(4)==1);
	}
	
	@Test
	public void testFastEvalReal() {
		assertTrue(p.fastEval(2,2)[0]==4);
		assertTrue(p.fastEval(2,2)[1]==3);
		assertTrue(p.fastEval(2,2)[2]==2);
		assertTrue(q.fastEval(2,2)[0]==7);
		assertTrue(q.fastEval(2,2)[1]==5);
		assertTrue(q.fastEval(2,2)[2]==2);
	}

	@Test
	public void testFastEvalComplex() {
		HuckelIO.PrintIf(p.toString()+" : "+p.fastEval(new Complex(1,1), 2)[0].toString());
		assertTrue(p.fastEval(new Complex(1,1),2)[0].getReal()==1);
		assertTrue(p.fastEval(new Complex(1,1),2)[0].getImag()==1);
	}
	
	@Test
	public void testDividePbyP() {
		Polynomial[] div = p.divide(p);
		Polynomial pq = div[0];
		Polynomial pr = div[1];
		assertTrue(pq.isSimilar(new Polynomial(new double[]{1.0}),THRS));
		assertTrue(pr.isSimilar(new Polynomial(new double[]{0.0}),THRS));
	}

	@Test
	public void testDivideQbyQ() {
		Polynomial[] div = q.divide(q);
		Polynomial qq = div[0];
		Polynomial qr = div[1];
		assertTrue(qq.isSimilar(new Polynomial(new double[]{1.0}),THRS));
		assertTrue(qr.isSimilar(new Polynomial(new double[]{0.0}),THRS));
	}

	@Test
	public void testDivideRbyR() {
		Polynomial[] div = r.divide(r);
		Polynomial rq = div[0];
		Polynomial rr = div[1];
		assertTrue(rq.isSimilar(new Polynomial(new double[]{1.0}),THRS));
		assertTrue(rr.isSimilar(new Polynomial(new double[]{0.0}),THRS));
	}

	@Test
	public void testDivideRbyQ() {
		Polynomial[] div = r.divide(q);
		Polynomial rq = div[0];
		Polynomial rr = div[1];
		assertTrue(rq.isSimilar(new Polynomial(new double[]{-3,1}),THRS));
		assertTrue(rr.isSimilar(new Polynomial(new double[]{0.0}),THRS));
	}

	@Test
	public void testDivideRbyS() {
		Polynomial[] div = r.divide(s);
		Polynomial rq = div[0];
		Polynomial rr = div[1];
		assertTrue(rq.isSimilar(q,THRS));
		assertTrue(rr.isSimilar(new Polynomial(new double[]{0.0}),THRS));
	}

}
