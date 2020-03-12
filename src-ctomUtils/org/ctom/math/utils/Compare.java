package org.ctom.math.utils;

import org.apache.commons.math.util.FastMath;

public class Compare {
	
	private final static double THRS_ZERO=1e-12;

	public static boolean isLower(double p,double thrs) {
		return (FastMath.abs(p)<=thrs);
	}

	public static boolean isLower(double p,int thrs) {
		return isLower(p, FastMath.pow(10, -thrs));
	}

	public static boolean isZero(double p) {
		return (FastMath.abs(p)<=THRS_ZERO);
	}

}
