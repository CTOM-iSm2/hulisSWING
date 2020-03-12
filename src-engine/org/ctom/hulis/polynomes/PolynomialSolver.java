package org.ctom.hulis.polynomes;

import java.util.ArrayList;

import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.hulis.util.math.Complex;

public class PolynomialSolver {

	private Polynomial polynomial;
	private Polynomial polynomial_init;
	private ArrayList<Polynomial> divisors = new ArrayList<Polynomial>();
	private double THRS=1e-10;

	public double getThrs() {
		return THRS;
	}

	public void setThrs(double thrs) {
		this.THRS = thrs;
	}

	public PolynomialSolver(Polynomial p) {
		this.polynomial_init=p.getCopy();
		this.polynomial=p.getCopy();
	}

	@Deprecated
	public double solve_One_Root(double xg) throws Exception {
		double a=1000;
		double g;
		double g2;
		double h;
		double tmp;
		int n=polynomial.getDegree();
		double[] values = polynomial.fastEval(xg, 2);
		while (Math.abs(values[0])>THRS) {
			g=values[1]/values[0];
			g2=g*g;
			h=g2-values[2]/values[0];
			tmp=(n-1)*(n*h-g2);
			if (tmp<0) {
				throw new Exception("Complex root");
			}
			if (g>0) {
				a=n/(g+Math.sqrt(tmp));
			} else {
				a=n/(g-Math.sqrt(tmp));
			}
			xg-=a;
			values = polynomial.fastEval(xg, 2);
		}
		return xg;
	}

	public Complex solve_One_Root(Complex xg) throws Exception {
		final int MR=8;
		final int MT=10;

		double[] frac = new double[]{0.0, 0.5, 0.25, 0.75, 0.13, 0.38, 0.62, 0.88, 1.0};

		int n = this.polynomial.getDegree();
		switch (n) {
		case 0:
			xg=new Complex(Double.NaN, Double.NaN);
			break;
		case 1:
			xg=new Complex(-polynomial.getCoeffOfDegree(0)/polynomial.getCoeffOfDegree(1),0);
			break;
		default:
			Complex[] values = polynomial.fastEval(xg, 2);
			int iter=0;
			while (values[0].getNorm()>THRS) {
				if (iter>MR*MT) {
					throw new Exception("TOO MANY ITERATIONS");
				}
				iter++;
				HuckelIO.PrintIf(iter+":"+xg.toString());
				Complex g=new Complex(values[1]);
				g.div(values[0]);
				Complex g2=new Complex(g.getNorm2());
				Complex h=new Complex(values[2]);
				h.div(values[0]);
				h.sub(g2); //h=p''/p-g2
				h.prod(-1);
				h.prod(n);
				h.sub(g2);
				h.prod(n-1);
				h.sqrt();
				Complex denomPlus=new Complex(g);
				Complex denomMoins=new Complex(g);
				denomPlus.add(h);
				denomMoins.sub(h);
				h.setReal(n);
				h.setImg(0);
				double dp = denomPlus.getNorm();
				double dm = denomMoins.getNorm();
				if (dp>dm) {
					h.div(denomPlus);
				} else {
					h.div(denomMoins);
				}
				
				Complex dx=null;
				
				if (Math.max(dp, dm)>0.0) {
					dx=h;
				} else {
					dx=new Complex(1+xg.getNorm());
					dx.prod(new Complex(Math.cos((double)iter), Math.sin((double)iter)));
				}
				
				if (iter%MT==0) {
					dx.prod(frac[iter/MT]);
				}
				
				xg.sub(h);
				values = polynomial.fastEval(xg, 2);
			}
		}
		return xg;
	}

	public ArrayList<Complex> getRoots() throws Exception {
		ArrayList<Complex> res = new ArrayList<Complex>();

		while (res.size()<this.polynomial_init.getDegree()) {
			Complex root = this.solve_One_Root(new Complex(0,0));
			res.add(root);

			Polynomial poly;

			if (root.isReal()) {
				double r=root.getReal();
				poly = new Polynomial(new double[]{-r,1});
			} else {
				Complex rootcc = new Complex(root);
				rootcc.conjugate();
				res.add(rootcc);
				double a = root.getReal();
				double b = root.getImag();
				poly = new Polynomial(new double[]{a*a+b*b,-2*a,1});			
			}
			divisors.add(poly);
			polynomial = polynomial.divide(poly)[0];
		}

		return res;
	}

}
