package org.ctom.hulis.util;

import Jama.Matrix;



public enum Modele {
	
	ETHYLENE(new Matrix(new double[] {
			 1.0, 0.0,
			 0.0, 1.0
	},2)),
	BUTADIENE(new Matrix(new double[] {
			 0.0, 1.0, 0.0, 0.0,
			 1.0, 0.0, 1.0, 0.0,
			 0.0, 1.0, 0.0, 1.0,
			 0.0, 0.0, 1.0, 0.0
	},4)),
	HEXATRIENE(new Matrix(new double[] {
			 0.0, 1.0, 0.0, 0.0, 0.0, 0.0,
			 1.0, 0.0, 1.0, 0.0, 0.0, 0.0,
			 0.0, 1.0, 0.0, 1.0, 0.0, 0.0,
			 0.0, 0.0, 1.0, 0.0, 1.0, 0.0,
			 0.0, 0.0, 0.0, 1.0, 0.0, 1.0,
			 0.0, 0.0, 0.0, 0.0, 1.0, 0.0
	},6)),
	BENZENE(new Matrix(new double[] {
			 0.0, 1.0, 0.0, 0.0, 0.0, 1.0,
			 1.0, 0.0, 1.0, 0.0, 0.0, 0.0,
			 0.0, 1.0, 0.0, 1.0, 0.0, 0.0,
			 0.0, 0.0, 1.0, 0.0, 1.0, 0.0,
			 0.0, 0.0, 0.0, 1.0, 0.0, 1.0,
			 1.0, 0.0, 0.0, 0.0, 1.0, 0.0
	},6)),
	CYCLOPROPENE(new Matrix(new double[] {
			 0.0, 1.0, 1.0,
			 1.0, 0.0, 1.0,
			 1.0, 1.0, 0.0
	},3)),
	METHYLENE_CYCLOPROPENE(new Matrix(new double[] {
			 0.0, 1.0, 1.0, 1.0,
			 1.0, 0.0, 1.0, 0.0,
			 1.0, 1.0, 0.0, 0.0,
			 1.0, 0.0, 0.0, 0.0
	},4)),
	ALLYL(new Matrix(new double[] {
			 0.0, 1.0, 0.0,
			 1.0, 0.0, 1.0,
			 0.0, 1.0, 0.0
	},3)),
	MOEBIUS_BENZENE(new Matrix(new double[] {
			 0.0,-1.0, 0.0, 0.0, 0.0, 1.0,
			-1.0, 0.0, 1.0, 0.0, 0.0, 0.0,
			 0.0, 1.0, 0.0, 1.0, 0.0, 0.0,
			 0.0, 0.0, 1.0, 0.0, 1.0, 0.0,
			 0.0, 0.0, 0.0, 1.0, 0.0, 1.0,
			 1.0, 0.0, 0.0, 0.0, 1.0, 0.0
	},6)),
	MOEBIUS_CYCLOPROPENE(new Matrix(new double[] {
			 0.0,-1.0, 1.0,
			-1.0, 0.0, 1.0,
			 1.0, 1.0, 0.0
	},3)),
	MOEBIUS_METHYLENE_CYCLOPROPENE(new Matrix(new double[] {
			 0.0,-1.0, 1.0, 1.0,
			-1.0, 0.0, 1.0, 0.0,
			 1.0, 1.0, 0.0, 0.0,
			 1.0, 0.0, 0.0, 0.0,
	},4));
	
	private Matrix matrix;

	private Modele(Matrix mat) {
		matrix=mat;
	}

	/**
	 * @return the matrix
	 */
	public Matrix getMatrix() {
		return this.matrix;
	}

}
