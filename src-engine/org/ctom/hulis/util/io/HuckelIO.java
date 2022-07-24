package org.ctom.hulis.util.io;

import org.ctom.hulis.huckel.IDebuggable;

import Jama.Matrix;

public class HuckelIO {
	private static StringBuffer buffer = null;

	private static MyLogger logger = new MyLogger();

	private static final boolean verbose = true;

	public static MyLogger getLooger() {
		return HuckelIO.logger;
	}
	
	
	public static void PrintMatrix(Matrix mat) {
		int nlig = mat.getRowDimension();
		int ncol = mat.getColumnDimension();
		double[][] tab = mat.getArrayCopy();
		HuckelIO.PrintIf(String.format("\n"));
		// System.out.printf("nlig : %d , ncol : %d",nlig,ncol);
		for (int i = 0; i < nlig; i++) {
			for (int j = 0; j < ncol; j++) {
				HuckelIO.PrintIf(String.format("%8.4f", tab[i][j]));
			}
			HuckelIO.PrintIf(String.format("\n"));
		}
		HuckelIO.PrintIf(String.format("\n"));
	}

	public static void PrintMatrix(Matrix mat, String str) {
		HuckelIO.begin(str);
		HuckelIO.PrintMatrix(mat);
		HuckelIO.end(str);
	}

	public static void PrintVector(double[] vector) {
		double[] tab = vector;
		HuckelIO.PrintIf(String.format("\n"));
		for (int i = 0; i < tab.length; i++) {
			HuckelIO.PrintIf(String.format("%8.4f", tab[i]));
			HuckelIO.PrintIf(String.format("\n"));
		}
		HuckelIO.PrintIfln(String.format("\n"));
	}

	public static void PrintVector(double[] real, double[] img) {
		double[] tabR = real;
		double[] tabI = img;
		HuckelIO.PrintIf(String.format("\n"));
		for (int i = 0; i < tabR.length; i++) {
			HuckelIO.PrintIf(String.format("%8.4f + i%8.4f", tabR[i], tabI[i]));
			HuckelIO.PrintIf(String.format("\n"));
		}
		HuckelIO.PrintIfln(String.format("\n"));
	}

	public static void PrintVector(double[] real, double[] img, String str) {
		HuckelIO.begin(str);
		HuckelIO.PrintVector(real, img);
		HuckelIO.end(str);
	}

	public static void PrintVector(double[] vector, String str) {
		HuckelIO.begin(str);
		HuckelIO.PrintVector(vector);
		HuckelIO.end(str);
	}
	
	public static void begin(String str) {
		HuckelIO.PrintIfln("***BEGIN " + str);
	}

	public static void debug(IDebuggable debuggable) {
		debuggable.debug();
	}

	public static void end(String str) {
		HuckelIO.PrintIfln("***  END " + str);
	}

	public static void error(String sourceClass, String sourceMethod, String msg) {
		HuckelIO.logger.logp("SEVERE", sourceClass, sourceMethod, msg);
	}

	public static void error(String sourceClass, String sourceMethod,
			String msg, Throwable thrown) {
		HuckelIO.logger.logp("SEVERE", sourceClass, sourceMethod, msg, thrown);
	}

	public static boolean isverbose() {
		return HuckelIO.verbose;
	}

	
	public static void log(String sourceClass, String sourceMethod, String msg) {
		HuckelIO.logger.logp("INFO", sourceClass, sourceMethod, msg);
		// System.out.println(logger.toString());
	}

	public static void log(String sourceClass, String sourceMethod, String msg,
			Throwable thrown) {
		HuckelIO.logger.logp("INFO", sourceClass, sourceMethod, msg, thrown);
	}

	public static void PrintIf(Object o) {
		if (o instanceof Exception)
			((Exception) o).printStackTrace();
		else if (o instanceof IDebuggable)
			((IDebuggable) o).debug();
		else
			HuckelIO.PrintIf(o.toString());
	}

	public static void PrintIf(String ligne) {
		if (HuckelIO.verbose) {
			System.out.print(ligne);
			if (HuckelIO.buffer != null) {
				HuckelIO.buffer.append(ligne);
			}
		}
	}

	public static void PrintIfln(String ligne) {
		HuckelIO.PrintIf(ligne + "\n");
	}

	
	
	/**
	 * 
	 * @param b
	 */
	public static void setBuffer(StringBuffer b) {
		HuckelIO.buffer = b;
	}

	/**
	 * 
	 */
	public static void unsetBuffer() {
		HuckelIO.buffer = null;
	}

	/**
	 * 
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 */
	public static void warning(String sourceClass, String sourceMethod,
			String msg) {
		HuckelIO.logger.logp("WARNING", sourceClass, sourceMethod, msg);
	}

	/**
	 * 
	 * @param sourceClass
	 * @param sourceMethod
	 * @param msg
	 * @param thrown
	 */
	public static void warning(String sourceClass, String sourceMethod,
			String msg, Throwable thrown) {
		HuckelIO.logger.logp("WARNING", sourceClass, sourceMethod, msg, thrown);
	}

	/**
	 * 
	 */
	public static void PrintIfln() {
		HuckelIO.PrintIfln("");
		
	}

}
