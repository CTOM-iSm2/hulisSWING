package org.ctom.hulis.polynomes;

import java.text.DecimalFormat;
import java.util.Vector;

import org.ctom.hulis.util.Modele;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.hulis.util.math.Complex;

import Jama.Matrix;

public class CharacteristicPolynomial {

	private static final String MATRIX_DECIMALFORMAT = " 0.0;-0.0";
	private Matrix matrix;
	private int order;
	private double coeff[];
	private Polynomial pf;
	private boolean debug=false;

	public CharacteristicPolynomial(Matrix m) throws Exception {
		order = m.getColumnDimension();
		if (order!=m.getRowDimension()) {
			throw new Exception("Matrix must be squared to get a polynomial");
		}
		matrix=m;
		coeff = new double[order+1];
		setPolynomialFunction();
		solvePolynomialFunction();
	}
	
	public Polynomial getPolynomialFunction() {
		return pf;
	}

	private void solvePolynomialFunction() {
		PolynomialSolver solver = new PolynomialSolver(pf);
		Object[] roots = new Complex[pf.getDegree()];
		try {
			roots=solver.getRoots().toArray();
			for (int i=0; i<roots.length; i++) {
				Complex root = (Complex)roots[i];
				HuckelIO.PrintIf("\nroot "+i+" "+root.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setPolynomialFunction() {
		computeCoeff();
		double[] c = new double[order+1];
		for (int i=0; i<=order; i++) {
			c[i]=coeff[order-i];
		}
		pf = new Polynomial(c);
		if (pf.getCoefficients()[order]<0.0) {
			pf=pf.multiply(new Polynomial(new double[] {-1.0}));
		}
	}

	private void computeCoeff() {
		Matrix A=matrix;
		Vector<Matrix> B = new Vector<Matrix>();
/*
		c_0 = -1
		B_0 = A
 */
		coeff[0]=-1;
		B.add(new Matrix(A.getArray()));
/*
	c_k = Tr( B_(k-1) ) / k
	B_k = A * ( B_(k-1) - c_k * I )
 */
		for (int k=1; k <= order; k++) {
			coeff[k]=B.get(k-1).trace()/k;
			B.add(A.times(B.get(k-1).minus(Matrix.identity(order,order).times(coeff[k]))));
			
			if (debug) B.get(k).print(new DecimalFormat(MATRIX_DECIMALFORMAT), 2);
		}
	}

	@Override
	public String toString() {
		return pf.toString();
	}

	public static void main(String[] args) {
		Matrix m=Modele.MOEBIUS_METHYLENE_CYCLOPROPENE.getMatrix();
		m.print(new DecimalFormat(MATRIX_DECIMALFORMAT), 2);
		CharacteristicPolynomial poly;
		Polynomial p;
		try {
			poly = new CharacteristicPolynomial(m);
			HuckelIO.PrintIf(poly.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}