/**
 * 
 */
package org.ctom.util.maths.minimizer;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import Jama.Matrix;

/**
 * @author yannick
 *
 */
public class PowellMinimizerTest {

	private PowellMinimizer opt;

	private MultiVariableFunctionTEST mExpm10xm22 = new MultiVariableFunctionTEST() {

		//@Override
		public double getValue(Matrix v) {
			double x=v.get(0, 0);
			return -Math.exp(-10*(x-2)*(x-2));
		}

		//@Override
		public int getDimension() {
			return 1;
		}

		//@Override
		public Matrix getOpt() {
			return new Matrix(new double[][]{{2}});
		}
	};

	private MultiVariableFunctionTEST MMH2O = new MultiVariableFunctionTEST() {

		//@Override
		public double getValue(Matrix v) {
			double oh1=v.get(0, 0);
			double oh2=v.get(1, 0);
			double hoh=v.get(2, 0);
			double OH0 = 0.95;
			double HOH0 = 109.45;
			double kOH = 1e4;
			double kHOH = 1;
			return (kOH*Math.pow((oh1-OH0),2)+kOH*Math.pow((oh2-OH0),2)+kHOH*Math.pow((hoh-HOH0),2));
		}

		//@Override
		public int getDimension() {
			return 3;
		}

		//@Override
		public Matrix getOpt() {
			return new Matrix(new double[][]{{0.95},{0.95},{109.45}});
		}
	};

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link org.ctom.util.maths.minimizer.PowellMinimizer#minimize()}.
	 */
	@Test
	public void testMinimize() {
		opt = new PowellMinimizer(mExpm10xm22);
		double thrs=1e-6;
		opt.setOptimizationParameters(null, 100, thrs, thrs);
		Matrix pointOpt = opt.minimize();
		System.out.println("TARGET :");
		mExpm10xm22.getOpt().print(10, 5);
		System.out.println("OPTIMUM FOUND :");
		pointOpt.print(10, 5);
		double norm = pointOpt.minus(mExpm10xm22.getOpt()).normF();
		System.out.println("Norm: "+ norm+" "+(norm<thrs));
		assertTrue(norm<thrs);
	}
	
	/**
	 * Test method for {@link org.ctom.util.maths.minimizer.PowellMinimizer#minimize()}.
	 */
	@Test
	public void testMinimize2() {
		opt = new PowellMinimizer(MMH2O);
		double thrs=1e-6;
		opt.setOptimizationParameters(new Matrix(new double[][]{{1.1},{1.1},{100}}), 100, thrs, thrs);
		Matrix pointOpt = opt.minimize();
		System.out.println("TARGET :");
		MMH2O.getOpt().print(10, 5);
		System.out.println("OPTIMUM FOUND :");
		pointOpt.print(10, 5);
		double norm = pointOpt.minus(MMH2O.getOpt()).normF();
		System.out.println("Norm: "+ norm+" "+(norm<thrs));
		assertTrue(norm<thrs);
	}

}
