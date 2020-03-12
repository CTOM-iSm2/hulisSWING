package org.ctom.hulis.polynomes;

import java.text.DecimalFormat;

import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.util.math.Complex;

/**
 * @author yannick
 *
 */
public class Polynomial {

	private double[] coefficients;

	/**
	 * Empty constructor
	 */
	public Polynomial() {
	}

	/**
	 * Creates an empty polynomial of degree degree
	 * @param degree : degree of the polynomial to create
	 */
	public Polynomial(int degree) {
		this();
		this.setDegree(degree);
	}

	/**
	 * Creates a polynomial of degree coefficients.length-1 with coefficients
	 * @param coefficients : coefficients of the polynomial ordered by increasing degree
	 */
	public Polynomial(double[] coefficients) {
		this(coefficients.length-1);
		this.setCoefficients(coefficients);
	}

	/**
	 * Sets the degree of a polynomial : coefficients are erased
	 * @param degree
	 */
	private void setDegree(int degree) {
		this.coefficients = new double[degree+1];
	}

	/**
	 * Sets the coefficients of the polnomial
	 * @param coeff
	 */
	private void setCoefficients(double[] coeff) {
		System.arraycopy(coeff, 0, this.coefficients, 0, coeff.length);
	}

	/**
	 * @return the degree of the polynomial
	 */
	public int getDegree() {
		return this.coefficients.length-1;
	}

	/**
	 * @return a copy of the array of the coefficients of the polynomial
	 */
	public double[] getCoefficients() {
		int l=coefficients.length;
		double[] res = new double[l];
		System.arraycopy(coefficients, 0, res, 0, l);
		return res;
	}

	/**
	 * @param degree
	 * @return the coefficient of the given degree
	 */
	public double getCoeffOfDegree(int degree) {
		if (degree>this.getDegree()) {
			return 0;
		} else {
			return this.coefficients[degree];
		}
	}

	/**
	 * @param degree
	 * @return the coefficient of the given degree
	 */
	public void setCoeffOfDegree(int degree, double a) {
		if (degree>this.getDegree()) {
			try {
				throw new HulisException("Cannot set coef of degree higher than Maxdegree="+this.getDegree());
			} catch (HulisException e) {
				e.printStackTrace();
				//System.exit(99);
			}
		} else {
			this.coefficients[degree]=a;
		}
	}

	/**
	 * Evaluates the polynomial at value x
	 * @param x
	 * @return
	 */
	public double eval(double x) {
		double res=0.0;
		for (int i=this.coefficients.length-1; i>=0; i--) {
			res=res*x+this.getCoeffOfDegree(i);
		}
		return res;
	}

	/**
	 * @param x
	 * @param nd
	 * @return an array with elements [0] : value of the polynomial at x and [1..nd]�value of the nd^th derivatives at x
	 */
	public double[] fastEval(double x, int nd) {
		double[] res= new double[nd+1];
		int n=this.coefficients.length-1; //degree

		switch (n) {
		case 0:
			res[0]=this.coefficients[0];
			break;
		case 1:
			res[0]=this.coefficients[1]*x+this.coefficients[0];
			if (nd>0) res[1]=this.coefficients[1];
			break;
		default: //from numerical recipes
			res[0]=this.coefficients[n];
			for (int i=n-1; i>=0; i--) {
				int ndd=( nd<n-i ? nd : n-i );
				for (int j=ndd; j>=1; j--) res[j]=res[j]*x+res[j-1];
				res[0]=res[0]*x+this.coefficients[i];
			}
			int c=1;
			for (int i=2; i<=nd; i++) {
				c*=i;
				res[i]*=i;
			}
			break;
		}


		return res;
	}

	/**
	 * @param x
	 * @param nd
	 * @return an array with elements [0] : value of the polynomial at x and [1..nd]�value of the nd^th derivatives at x
	 */
	public Complex[] fastEval(Complex z, int nd) {
		Complex[] res= new Complex[nd+1];
		int n=this.coefficients.length-1; //degree

		switch (n) {
		case 0:
			res[0]=new Complex(coefficients[0],0);
			break;
		case 1:
			res[0] = new Complex(z);
			res[0].prod(this.coefficients[1]);
			res[0].add(this.coefficients[0]);
			if (nd>0) res[1]=new Complex(coefficients[1],0);
			break;
		default: //from numerical recipes
			res[0]=new Complex(coefficients[n],0);
			for (int i=1; i<=nd;i++) res[i] = new Complex(0,0);
			for (int i=n-1; i>=0; i--) {
				int ndd=( nd<n-i ? nd : n-i );
				for (int j=ndd; j>=1; j--) {
					res[j].prod(z);
					res[j].add(res[j-1]);
				}
				res[0].prod(z);
				res[0].add(this.coefficients[i]);
			}
			int c=1;
			for (int i=2; i<=nd; i++) {
				c*=i;
				res[i].prod(i);
			}
			break;
		}

		return res;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String res="";
		for (int i=this.coefficients.length-1; i>=0 ; i--) {
			double c=this.getCoeffOfDegree(i);
			if (c>0.0) {
				if (i<this.coefficients.length-1) res+=" +";
			} else if (c==-1) {
				res+=" -";
			}
			if (c!= 0.0) {
				if ( (Math.abs(c)!=1)||(i==0) ) {
					DecimalFormat df = new DecimalFormat("0.0000");
					boolean runUnderGWT=false;
					if (runUnderGWT) {
						res+=Double.toString(c);
					} else {
						res+=df.format(c);
					}
				}
				if (i>1) {
					res+="x^{"+i+"}";
				} else if (i==1) {
					res+="x";
				}
			}
		}
		return res;
	}

	/**
	 * @param that
	 * @return the sum of the two polynomials
	 */
	public Polynomial add(Polynomial that) {
		int degreeMax = Math.max(this.getDegree(), that.getDegree());
		double[] coeff = new double[degreeMax+1];
		for (int j=0; j<=degreeMax; j++) {
			coeff[j]=this.getCoeffOfDegree(j) + that.getCoeffOfDegree(j);
		}
		return new Polynomial(coeff);
	}

	/**
	 * @param that
	 * @return the product of the two polynomials
	 */
	public Polynomial multiply(Polynomial that) {
		/*TODO
		 * to optimize
		 */
		int degreeProd = this.getDegree() + that.getDegree();
		double[] coeff = new double[degreeProd+1];
		for (int j=0; j<=degreeProd; j++) {
			double val=0;
			for (int k=0; k<=j; k++) {
				val+=this.getCoeffOfDegree(k)*that.getCoeffOfDegree(j-k);
			}
			coeff[j] = val;
		}
		return new Polynomial(coeff);
	}

	public Polynomial[] divide(Polynomial that) {
		Polynomial[] res = new Polynomial[2];
		int n = this.getDegree();
		int m = that.getDegree();
		Polynomial q=new Polynomial(n);
		Polynomial r=new Polynomial(n);

		r.setCoefficients(this.coefficients);

		for (int k=n-m; k>=0; k--) {
			double a=r.coefficients[m+k]/that.coefficients[m];
			q.coefficients[k]=a;
			for (int j=m+k-1; j>=k; j--) {
				r.coefficients[j] -= q.coefficients[k]*that.coefficients[j-k];
			}
		}
		for (int j=m; j<=n; j++) r.coefficients[j]=0.0;
		res[0]=q;
		res[1]=r;
		return res;
	}

	public boolean isSimilar(Polynomial that, double thrs) {
		int n=Math.max(this.getDegree(),that.getDegree());
		for (int i=0; i<n; i++) {
			if (Math.abs(this.getCoeffOfDegree(i)-that.getCoeffOfDegree(i))>thrs) return false;
		}
		return true;
	}

	public Polynomial getCopy() {
		Polynomial res = new Polynomial(this.getDegree());
		res.setCoefficients(coefficients);
		return res;
	}

}
