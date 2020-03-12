package org.ctom.math.utils;

public class Binomial {

	public static int nAk(int n, int k) throws Exception {
		if (k<0) return 0;
		if (k==0) return 1;
		if (n<=0) throw new Exception("n <=0 in  binomial expression");
		double a=1;

		for (int i = n-k+1; i<=n; i++) {
			a=a*i;
		}
		return (int)a;
	}

	public static int nCk(int n, int k) throws Exception {
		if (k<0) return 0;
		if (k==0) return 1;
		if (n<=0) throw new Exception("n <=0 in  binomial expression");
		double a=1;
		for (double i=1; i<=k; i++) {
			a=a*(n-k+i)/i;
		}

		return (int)a;
	}
}