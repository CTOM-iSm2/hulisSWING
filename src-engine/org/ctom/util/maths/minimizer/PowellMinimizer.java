package org.ctom.util.maths.minimizer;

import java.util.ArrayList;

import Jama.Matrix;



public class PowellMinimizer {

	private final MultiVariableFunction function;
	private MinimizerStop stopReason=MinimizerStop.NOREASON;
	private double stopValue = Double.NaN;
	private int nITER_1D;
	private int nITER_nD;
	private boolean verbose = false;
	private boolean microiteration_verbose = false;
	private Matrix PointDepart;
	private double ThrsD;
	private double ThrsV;
	private int NITERMAX;
	
	private ArrayList<PowellMinimizerListener> listener;

	public MinimizerStop getStopReason() {
		return stopReason;
	}

	public double getStopValue() {
		return stopValue ;
	}
	
	public boolean addListener(PowellMinimizerListener list) {
		if (this.listener==null) this.listener=new ArrayList<PowellMinimizerListener>();
		return this.listener.add(list);
	}
	
	private void fireLap(Matrix matrix) {
		for (PowellMinimizerListener l : this.listener) {
			l.intermediateValues(matrix);
		}
	}

	/*
	 * Creates an instance of PowellMinimizer for the function func
	 */
	public PowellMinimizer(MultiVariableFunction func) {
		this.function = func;
	}

	
	public void setOptimizationParameters(Matrix PointDepart, int NITERMAX, double ThrsD, double ThrsV) {
		this.PointDepart = PointDepart;
		this.NITERMAX = NITERMAX;
		this.ThrsD = ThrsD;
		this.ThrsV = ThrsV;
	}
	
	/*
	 * Search the minimum along all directions
	 */
	public Matrix minimize() {
		nITER_nD = 0;
		int ndim = function.getDimension();
		Matrix matriceDeplacement = Matrix.identity(ndim, ndim);
		Matrix PointActuel = new Matrix(ndim,1);
		if (PointDepart == null) {
			PointActuel = new Matrix(ndim, 1);
		} else {
			PointActuel = PointDepart.copy();
		}
		Matrix PointPrec = PointActuel.copy();
		double potPrec = function.getValue(PointPrec);

		double DistanceParcourue = ThrsD+1;
		double dPot=0;
		boolean converged=false;

		//infinite loop
		if (verbose) System.out.printf("%3s\t%3s\t%10s\t%10s\n", "itn", "it1", "dist", "dpot");
		while (! converged) {
			int idim=0;
			nITER_nD++;
			Matrix vecTemp;
			if (Math.abs(matriceDeplacement.det())<1e-1) { // avoid linear dependance
				matriceDeplacement = Matrix.identity(ndim, ndim);
			}
			//on minimise selon toutes les dimensions
			while (idim<ndim) {
				//on recupere le vecteur de delacement
				vecTemp = matriceDeplacement.getMatrix(0, ndim-1, 0, 0);
				//on minimise selon ce vecteur
				PointActuel=minimize1D(PointActuel, vecTemp, 1, ThrsD, ThrsV);
				rotationMatrice(matriceDeplacement, vecTemp);
				idim++;
			}
			//on calcule le deplacement
			vecTemp = PointActuel.copy().minus(PointPrec);
			double norm = vecTemp.normF();
			//on met a jour la matrice de deplacements
			vecTemp.timesEquals(1./norm);
			rotationMatrice(matriceDeplacement, vecTemp);
//			System.out.println("Matrice deplacement pour niter = "+nITER_nD+"  "+matriceDeplacement.det());
//			matriceDeplacement.print(10, 5);
			if (norm<Double.MIN_VALUE) {
				DistanceParcourue = 0;
			} else {
				DistanceParcourue = norm;
			}
			//on calcule la variation de potentiel
			double potActuel = function.getValue(PointActuel);
			dPot = Math.abs(potActuel-potPrec);
			potPrec = potActuel;
			if (nITER_nD>=NITERMAX) {
				stopReason = MinimizerStop.NITERMAX_REACHED;
				stopValue = nITER_nD;
				break;
			}
			PointPrec = PointActuel.copy();

			if (verbose) System.out.printf("%3d\t%3d\t%10.6f\t%10.6f\n", nITER_nD,nITER_1D,DistanceParcourue, dPot);
			converged = (DistanceParcourue<ThrsD);
			converged = converged && (dPot<ThrsV);
			if (converged) {
				stopReason = MinimizerStop.CONVERGED;
				stopValue = nITER_nD;
			}
			if (nITER_nD%1==0) {
				this.fireLap(PointActuel.copy());
			}
		}
		return PointActuel;
	}

	/*
	 * shift the matriceDeplacement matrix by one column from right to left.
	 * add the vector vecTemp in the last column of matriceDeplacement
	 */
	private void rotationMatrice(Matrix matriceDeplacement, Matrix vecTemp) {
		int ndim = matriceDeplacement.getColumnDimension();
		matriceDeplacement.setMatrix(0, ndim-1, 0, ndim-2, matriceDeplacement.getMatrix(0, ndim-1, 1, ndim-1));
		matriceDeplacement.setMatrix(0, ndim-1, ndim-1, ndim-1, vecTemp.getMatrix(0, ndim-1, 0, 0));
	}

	/*
	 * Minimizes the function along Direction from the PointActuel point.
	 * The search will be done in the range [PointActuel-Delta:PointActuel+Delta] with the Thrs threshold
	 */
	private Matrix minimize1D(Matrix PointActuel, Matrix Direction, double Delta, double thrsD, double thrsV){

		final double MAGIC = 0.5*(3-Math.sqrt(5));

		/*
		 * Definition des points de depart
		 */
		Matrix b = PointActuel.copy();
		Matrix x = PointActuel.copy();
		Matrix a = b.copy();
		Matrix c = b.copy();

		a.plusEquals(Direction.times(-Delta));
		c.plusEquals(Direction.times(+Delta));

		double potA = function.getValue(a);
		double potB = function.getValue(b);
		double potC = function.getValue(c);

		double potX = Math.max(Math.abs(potA), Math.abs(potC));

		double TailleAC=thrsD+1;
		nITER_1D=0;
		double deltaV = Math.abs(potX-Math.abs(potB));
		boolean converged=false;

		if (microiteration_verbose) System.out.printf("\t\t%3s\t%10s\t%10s\n", "it1", "delta", "deltaV");
		while (!converged) {
			nITER_1D++;
			double TailleAB = a.minus(b).normF();
			double TailleBC = b.minus(c).normF();

			boolean XdansAB;
			if (TailleAB>=TailleBC) {
				x = b.minus(b.minus(a).times(MAGIC));
				XdansAB=true;
			} else {
				x = b.plus(c.minus(b).times(MAGIC));
				XdansAB=false;
			}

			//On calcule f(X)

			potX = function.getValue(x);

			if (XdansAB) {
				if (potX>potB) {
					a = x;
					potA = potX;
				} else {
					c = b;
					potC = potB;
					b = x;
					potB = potX;
				}
			} else {
				if (potX > potB) {
					c = x;
					potC = potX;
				} else {
					a = b;
					potA = potB;
					b = x;
					potB = potX;
				}
			}

			PointActuel = x;
			TailleAC = a.minus(c).normF();
			deltaV = Math.abs(potC-potA);
			converged = (TailleAC<thrsD)&&(deltaV<thrsV);

			if (microiteration_verbose) System.out.printf("\t\t%3d\t%10.6f\t%10.6f\n", nITER_1D, TailleAC, deltaV);
		}
		return PointActuel;
	}

}
