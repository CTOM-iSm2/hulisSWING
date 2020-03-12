package org.ctom.hulis.util.math;


public class Complex {

	/**
	 * 
	 * @author macbook
	 *
	 */
	public interface ComplexFormater {
		public String formatNumber(double number);
	}
	
	
	/**
	 * 
	 * @author macbook
	 *
	 */
	public static class DefaultComplexFormater implements ComplexFormater {
		public String formatNumber(double number) {
			return Double.toString(number);
			
		}
	}
	
	public static DefaultComplexFormater DEFAULT_COMPLEX_FORMATER= new DefaultComplexFormater();

	/**
	 * 
	 * @param defaultComplexFormater
	 */
	public static void setDefaultComplexFormater(DefaultComplexFormater defaultComplexFormater) {
		DEFAULT_COMPLEX_FORMATER = defaultComplexFormater;
	}
	

	
	public static final Complex NaN = new Complex(Double.NaN,Double.NaN);
	private double real;
	private double imag;
	private ComplexFormater complexFormater;
	
	
	
	/**
	 * 
	 * @param complex
	 * @return
	 */
	public static String format(Complex complex, ComplexFormater complexFormater) {
	    ComplexFormater  tmpComplexFormater;
		
		if (complexFormater == null)
			tmpComplexFormater  = DEFAULT_COMPLEX_FORMATER;
		else 
			tmpComplexFormater = complexFormater;
		
		String formatedComplex=  tmpComplexFormater.formatNumber(complex.real)+"+"+tmpComplexFormater.formatNumber(complex.imag)+" i";	
		tmpComplexFormater = null;
		return formatedComplex;
	}
	
	
	
	/**
	 * 
	 * @param real
	 * @param img
	 */
	public Complex(double real, double img) {
		this.real=real;
		this.imag=img;
		this.complexFormater = null;
	}
		
	/**
	 * 
	 * @param real
	 */
	public Complex(double real) {
		this(real,0);
	}

	/**
	 * 
	 * @param z
	 */
	public Complex(Complex z) {
		this.real=z.real;
		this.imag=z.imag;
		this.complexFormater = z.complexFormater;

	}
	
	/**
	 * 
	 * @param complexFormater
	 */
	public void setComplexFormater(ComplexFormater complexFormater) {
		this.complexFormater = complexFormater;
	}

	/**
	 * 
	 */
	public void conjugate() {
		this.imag=-this.imag;
	}

	/**
	 * 
	 * @return
	 */
	public double getNorm() {
		return Math.sqrt(getNorm2());
	}

	/**
	 * 
	 * @return
	 */
	public double getNorm2() {
		return real*real+imag*imag;
	}

	/**
	 * 
	 * @return
	 */
	public double getReal() {
		return real;
	}

	/**
	 * 
	 * @param real
	 */
	public void setReal(double real) {
		this.real = real;
	}

	/**
	 * 
	 * @return
	 */
	public double getImag() {
		return imag;
	}

	/**
	 * 
	 * @param img
	 */
	public void setImg(double img) {
		this.imag = img;
	}

	/**
	 * 
	 * @param z
	 */
	public void add(Complex z) {
		this.real+=z.real;
		this.imag+=z.imag;
	}

	/**
	 * 
	 * @param a
	 */
	public void add(double a) {
		this.real+=a;
	}

	/**
	 * 
	 * @param z
	 */
	public void sub(Complex z) {
		this.real-=z.real;
		this.imag-=z.imag;
	}

	/**
	 * 
	 * @param a
	 */
	public void sub(double a) {
		this.real-=a;
	}

	/**
	 * 
	 * @param a
	 */
	public void prod(double a) {
		this.real*=a;
		this.imag*=a;
	}

	/**
	 * 
	 * @param z
	 */
	public void prod(Complex z) {
		double r = this.real*z.real-this.imag*z.imag;
		double i = this.real*z.imag + this.imag*z.real;
		this.real = r;
		this.imag  = i;
	}

	/**
	 * 
	 * @return
	 */
	public Complex inverse() {
		Complex z=new Complex(this.real, -this.imag);
		z.prod(1/this.getNorm2());
		return z;
	}

	/**
	 * 
	 * @param z
	 */
	public void div(Complex z) {
		this.prod(z.inverse());
	}
	

	/**
	 * 
	 */
	public void sqrt() {
		if (this.imag==0) {
			if (this.real>=0) {
				this.real=Math.sqrt(this.real);
				return;
			} else {
				this.imag=Math.sqrt(-this.real);
				this.real=0;
				return;
			}
		}
		int sign=(this.imag < 0 ? -1 : 1);
		double a=this.real;
		double r=this.getNorm();
		this.real = Math.sqrt(0.5*(r+a));
		this.imag = sign * Math.sqrt(0.5*(r-a));
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public void aXpb(Complex a, Complex b) {
		this.prod(a);
		this.add(b);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public void aXmb(Complex a, Complex b) {
		this.prod(a);
		this.sub(b);
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double dist(Complex a, Complex b) {
		Complex z=new Complex(a);
		z.prod(-1);
		z.add(b);
		return z.getNorm();
	}

	/**
	 * 
	 * 
	 */
	public String toString() {
		return format(this, this.complexFormater);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isReal() {
		if (this.imag==0) return true;
		return false;
	}

}
