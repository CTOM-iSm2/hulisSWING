package org.ctom.hulis.polynomes.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.polynomes.PolynomialSolver;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.hulis.util.math.Complex;
import org.junit.Before;
import org.junit.Test;

public class PolynomialSolverTest {

	private Polynomial p = new Polynomial(new double[]{-2.0,1.0});
	private Polynomial q = new Polynomial(new double[]{1.0,3.0,1.0});
	private Polynomial r = new Polynomial(new double[]{1.0,1.0,1.0});
	private Polynomial s = new Polynomial(new double[]{-3,-2,-2,1});
	private Polynomial t = new Polynomial(new double[]{-2,4,-3,1});
	
	private static final double THRS=1e-10;
	
	private final Complex root_p=new Complex(2,0); 
	private final Complex root_q1=new Complex(0.5*(-3-Math.sqrt(5))); 
	private final Complex root_q2=new Complex(0.5*(-3+Math.sqrt(5)));
	private final Complex root_r1=new Complex(-0.5,-0.5*Math.sqrt(3)); 
	private final Complex root_r2=new Complex(-0.5,+0.5*Math.sqrt(3)); 
	private final Complex root_s1=new Complex(-0.5,-0.5*Math.sqrt(3)); 
	private final Complex root_s2=new Complex(-0.5,+0.5*Math.sqrt(3)); 
	private final Complex root_s3=new Complex(3); 
	private final Complex root_t1=new Complex(1,1); 
	private final Complex root_t2=new Complex(1,-1); 
	private final Complex root_t3=new Complex(1); 


	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testSolve_linear_function_guess01() {
		PolynomialSolver solver = new PolynomialSolver(p);
		double xg=4;
		try {
			xg=solver.solve_One_Root(xg);
		} catch (Exception e) {
			e.printStackTrace();
			xg=Double.NaN;
		}
		double err=Math.abs(xg-root_p.getReal());
		assertTrue("err="+err+"root :"+xg,err<THRS);
	}

	@Test
	public void testSolve_linear_function_guess02() {	
		PolynomialSolver solver = new PolynomialSolver(p);
		double xg=0;
		try {
			xg=solver.solve_One_Root(xg);
		} catch (Exception e) {
			e.printStackTrace();
			xg=Double.NaN;
		}
		double err=Math.abs(xg-root_p.getReal());
		assertTrue("err="+err+"root :"+xg,err<THRS);
	}

	@Test
	public void testSolve_quadratic_function_RealRoots_RealSolver() {	

		PolynomialSolver solver = new PolynomialSolver(q);
		double xg=0;
		try {
			xg=solver.solve_One_Root(xg);
		} catch (Exception e) {
			e.printStackTrace();
			xg=Double.NaN;
		}
		double err=Math.abs(xg-root_q2.getReal());
		assertTrue("err="+err+"root :"+xg,err<THRS);
	}

	@Test
	public void testSolve_quadratic_function_RealRoots_ComplexSolver() {	

		PolynomialSolver solver = new PolynomialSolver(q);
		Complex xg = new Complex(4);
		try {
			xg = solver.solve_One_Root(xg);
		} catch (Exception e) {
			e.printStackTrace();
			xg=Complex.NaN;
		}
		double err=Complex.dist(xg, root_q2);
		assertTrue("err="+err+"  root :"+xg.toString()+"=="+root_q2,err<THRS);
	}

//	@Test
//	public void testSolve_quadratic_function_ComplexRoots_ComplexSolver() {	
//
//		PolynomialSolver solver = new PolynomialSolver(r);
//		Complex xg = new Complex(0);
//		xg = solver.solve_One_Root(xg);
//		double err=Complex.dist(xg, root_r1);
//		assertTrue("err="+err+"root :"+xg.toString()+"=="+root_r1,err<THRS);
//	}
	
	@Test
	public void testGetRoots_R() {
		
		PolynomialSolver solver = new PolynomialSolver(r);
		ArrayList<Complex> roots;
		try {
			roots = solver.getRoots();
		} catch (Exception e) {
			roots=new ArrayList<Complex>();
		}

		boolean r1found=false;
		boolean r2found=false;
		HuckelIO.PrintIf(r.toString()+" Roots");
		for (Complex r : roots) {
			HuckelIO.PrintIf(r.toString());
			r1found=r1found||Complex.dist(r, root_r1)<THRS;
			r2found=r2found||Complex.dist(r, root_r2)<THRS;
		}
		assertTrue(r1found&&r2found);
	}

	@Test
	public void testGetRoots_S() {
		
		PolynomialSolver solver = new PolynomialSolver(s);
		ArrayList<Complex> roots;
		try {
			roots = solver.getRoots();
		} catch (Exception e) {
			roots=new ArrayList<Complex>();
		}

		boolean s1found=false;
		boolean s2found=false;
		boolean s3found=false;
		HuckelIO.PrintIf(s.toString()+" Roots");
		for (Complex r : roots) {
			HuckelIO.PrintIf(r.toString());
			s1found=s1found||Complex.dist(r, root_s1)<THRS;
			s2found=s2found||Complex.dist(r, root_s2)<THRS;
			s3found=s3found||Complex.dist(r, root_s3)<THRS;
		}
		assertTrue(s1found&&s2found&&s3found);		
	}

	@Test
	public void testGetRoots_T() {
		
		PolynomialSolver solver = new PolynomialSolver(t);
		ArrayList<Complex> roots;
		try {
			roots = solver.getRoots();
		} catch (Exception e) {
			roots=new ArrayList<Complex>();
		}

		boolean t1found=false;
		boolean t2found=false;
		boolean t3found=false;
		HuckelIO.PrintIf(t.toString()+" Roots");
		for (Complex r : roots) {
			HuckelIO.PrintIf(r.toString());
			t1found=t1found||Complex.dist(r, root_t1)<THRS;
			t2found=t2found||Complex.dist(r, root_t2)<THRS;
			t3found=t3found||Complex.dist(r, root_t3)<THRS;
		}
		assertTrue(t1found&&t2found&&t3found);		
	}

}
