package org.ctom.hulis.util;

import org.ctom.hulis.huckel.exception.ProjectionException;
import org.ctom.hulis.util.io.HuckelIO;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


/**
 * @author yannick
 *
 */
public class Projection {

	private Matrix Overlap;
	private Matrix OverlapWithStot;

	private double[] coeffs = null;
	private double[] weights = null;

	/**
	 * Projection is an object which solves the problem S x c = S_tot
	 * It is quite general but was designed for Lewis structure
	 * based problems
	 * 
	 * The solution is provided via the following methods :
	 * * getCoeffs returns the values of the c coefficients
	 * * getWeights returns the weight of each structure according to the
	 * Coulson-Chirgwin method
	 * * getNormalisedWieghts returns weights whish sum to 100%
	 * * getNorm returns the norm of te overlap of the solution with the real solution
	 */
	public Projection(Matrix overlap, Matrix overlapWithStot) throws ProjectionException {
		super();
		this.Overlap = overlap;
		this.OverlapWithStot = overlapWithStot;
		int size = this.Overlap.getColumnDimension();
		this.coeffs = new double[size];
		this.weights = new double[size];
		//we solve the problem
		solve();
		// we compute the weights
		computeWeights();
	}

	/**
	 * Checks is the problem is not ill defined
	 * @throws ProjectionException
	 */
	private void checkS() throws ProjectionException {
		EigenvalueDecomposition DiagonalisationOverlap = new EigenvalueDecomposition(
				this.Overlap);
		double[] valeursPropresSRe = DiagonalisationOverlap
		.getRealEigenvalues();
		double[] valeursPropresSIm = DiagonalisationOverlap
		.getImagEigenvalues();
		int size=valeursPropresSRe.length;
		for (int i = 0; i < size; i++) {
			HuckelIO.PrintIf(i + "eme valp = " + valeursPropresSRe[i]);
		}
		for (int i = 0; i < size; i++) {
			if (valeursPropresSIm[i] != 0)
				throw new ProjectionException("Une valeur propre de S est imaginaire!");
			if (Math.abs(valeursPropresSRe[i]) <= 0.00001)
				throw new ProjectionException("Une valeur propre de S est presque nulle!");
		}
	}

	/**
	 * computes the norm of the projection
	 * @return norm
	 */
	private double computeNorm() {
		double norm = 0;
		int size = this.weights.length;
		for (int i = 0; i < size; i++) {
			norm += this.weights[i];
		}
		return norm;
	}

	/**
	 * Computes the weights of the structures according to the Coulson Chrigwin formula
	 */
	private void computeWeights() {
		int size = this.getCoeffs().length;
		for (int iphi1 = 0; iphi1 < size; iphi1++) {
			double weight = 0;
			for (int iphi2 = 0; iphi2 < size; iphi2++) {
				weight += getCoeffs()[iphi2] * this.Overlap.get(iphi1, iphi2);
			}
			weight *= getCoeffs()[iphi1];
			this.weights[iphi1] = weight;
		}
		//now that weights are available, computes the norm of the projection
		computeNorm();
	}

	/**
	 * @return the coefficients of the projection
	 */
	public double[] getCoeffs() {
		return this.coeffs;
	}

	/**
	 * @return the normalized coefficients of the projection
	 */
	public double[] getNormalizedCoeffs() {;
		double[] tab = this.getCoeffs();
		int size = tab.length;
		double sqrtNorm = Math.sqrt(getNorm());
		for (int i = 0; i < size; i++) {
			tab[i] /= sqrtNorm;
		}
		return tab;
	}
	
	/**
	 * @return the norm of the projection
	 */
	public double getNorm() {
		return computeNorm();
	}

	/**
	 * @return the normalized weights of the projection
	 */
	public double[] getNormalizedWeights() {
		double[] tab = getWeights();
		int size = tab.length;
		for (int i = 0; i < size; i++) {
			tab[i] /= getNorm();
		}
		return tab;
	}

	/**
	 * @return the weights of the projection
	 */
	public double[] getWeights() {
		return this.weights.clone();
	}

	/**
	 * set an error status in case the error exceptions are ignored
	 */
	public void setError() {
		int size = this.coeffs.length;
		for (int i = 0; i < size; i++) {
			this.coeffs[i] = 0. / 0.;
			this.weights[i] = 0. / 0.;
		}
	}

	/**
	 * This method solves the problem S x c = S_tot
	 * @calls Jama
	 * @throws Exception
	 */
	private void solve() throws ProjectionException {
		try {
			//we check is the problem is ill defined
			checkS();
		} catch (ProjectionException e) {
			HuckelIO.PrintIf("Problem for solving the linear system");
			setError();
			e.printStackTrace();
			throw (e);
		}
		//solve the problem
		Matrix solution = this.Overlap.solve(this.OverlapWithStot);
		int size = this.coeffs.length;
		//stores the c coefficients in the appropriate array
		for (int i = 0; i < size; i++) {
			this.coeffs[i] = solution.get(i, 0);
		}
	}

	public double getNewTau() {
		double tau=0;
		double[] normalizedCoeffs = this.getCoeffs();
		for (int i=0; i<normalizedCoeffs.length; i++) {
			tau+=normalizedCoeffs[i]*this.OverlapWithStot.get(i, 0);
		}
		return tau;
	}
}
