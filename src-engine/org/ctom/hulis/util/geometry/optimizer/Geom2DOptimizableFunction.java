package org.ctom.hulis.util.geometry.optimizer;

import java.util.ArrayList;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.util.maths.minimizer.MultiVariableFunction;

import Jama.Matrix;

/**
 * @author yannick
 * 
 */
public class Geom2DOptimizableFunction implements MultiVariableFunction {

	private int nAt;
	private int[][] topo;
	private boolean verbose = false;
	private Molecule molecule;

	/**
	 * @return true if the object was set up as verbose
	 */
	public boolean isVerbose() {
		return verbose;
	}

	/**
	 * @param verbose
	 * set the verbosity of the object
	 */
	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Constructor : create an optimizable function foe the geometry of the molecule
	 * @param molecule
	 */
	public Geom2DOptimizableFunction(Molecule molecule) {
		this.nAt = molecule.getHuckelAtoms().size();
		this.topo = molecule.getTopology();
		this.molecule = molecule;
	}

	public int getDimension() {
		return nAt * 2;
	}

	public double getValue(Matrix v) {
		double E = 0;
		double ALPHA0 = 2 * Math.PI / 3;

		ArrayList<Double> bondValues = new ArrayList<Double>();
		ArrayList<Double> angleValues = new ArrayList<Double>();

		E += getBondEnergy(v, E, bondValues);
		E += getAngleEnergy(v, E, ALPHA0, angleValues);

		if (this.verbose) {
			for (double d : bondValues) {
				System.out.print("  " + d);
			}
			for (double a : angleValues) {
				System.out.print("  " + a);
			}
			System.out.println();
		}
		return E;
	}

	private double getAngleEnergy(Matrix v, double E, double ALPHA0,
			ArrayList<Double> angles) {
		for (int iat = 0; iat < nAt; iat++) {
			// i is the apex
			double xi = v.get(iat * 2, 0);
			double yi = v.get(iat * 2 + 1, 0);
			for (int jat = 0; jat < nAt; jat++) {
				if (iat != jat) {
					// jat is bound to iat
					if (topo[iat][jat] != 0) {
						double xj = v.get(jat * 2, 0);
						double yj = v.get(jat * 2 + 1, 0);
						double dij = Math.sqrt(Math.pow(xj - xi, 2)
								+ Math.pow(yj - yi, 2));
						for (int kat = jat + 1; kat < nAt; kat++) {
							if (kat != jat) {
								if (topo[iat][kat] != 0) {
									// kat is bound to iat and is not jat
									double xk = v.get(kat * 2, 0);
									double yk = v.get(kat * 2 + 1, 0);
									double dik = Math.sqrt(Math.pow(xk - xi, 2)
											+ Math.pow(yk - yi, 2));
									double djk = Math.sqrt(Math.pow(xk - xj, 2)
											+ Math.pow(yk - yj, 2));
									double tmp = dij * dij + dik * dik - djk
											* djk;
									tmp /= 2 * dij * dik;
									double alpha = Math.acos(tmp);

									angles.add(alpha);

									E += Math.pow(alpha / ALPHA0 - 1, 2);
								}
							}
						}
					}
				}
			}
		}
		return E;
	}

	private double getBondEnergy(Matrix v, double E,ArrayList<Double> bonds) {
		for (int iat = 0; iat < nAt; iat++) {
			double xi = v.get(iat * 2, 0);
			double yi = v.get(iat * 2 + 1, 0);
			for (int jat = iat + 1; jat < nAt; jat++) {
				if (topo[iat][jat] != 0) {
					double xj = v.get(jat * 2, 0);
					double yj = v.get(jat * 2 + 1, 0);
					double dij = Math.sqrt(Math.pow(xj - xi, 2)
							+ Math.pow(yj - yi, 2));

					bonds.add(dij);

					Atom a1 = molecule.getAtomByIndex(iat+1);
					Atom a2 = molecule.getAtomByIndex(jat+1);
					double DOPT;
					// AJOUT NG : if ( this.molecule.getBond(a1, a2)!= null)
					if (( this.molecule.getBond(a1, a2)!= null) && (a1 instanceof HuckelAtom)&&(a2 instanceof HuckelAtom)) {						
						DOPT = this.molecule.getBond(a1, a2).getOptimalValue();
					} else {
						DOPT = a1.getCovalentRadius() + a2.getCovalentRadius();
					}
					DOPT*=org.ctom.hulis.util.geometry.Geometry.SCALE;
					E += Math.pow(dij / DOPT - 1, 2);
				}
			}
		}
		return E;
	}

}
