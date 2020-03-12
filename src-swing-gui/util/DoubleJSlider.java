package util;

/**
 * Class    : DoubleJSlider
 * Copyright: 2002 Andreas Gohr, Frank Schubert
 *            Mar 2003: Modified by Arne Morken,
 *                      MindMatters, www.mindmatters.no
 * License  : GPL2 or higher
 * 
 * Info     : This JSlider uses doubles for its values
 */

import javax.swing.JSlider;

@SuppressWarnings("serial")
public class DoubleJSlider extends JSlider {
	private String mName = "";
	private double mPrecision = 2;
	
	/**
	 * Constructor - initializes with 0.0,100.0,50.0
	 */
	public DoubleJSlider() {
		super();
		setName("");
		setDoubleMinimum(0.0);
		setDoubleMaximum(100.0);
		setDoubleValue(50.0);
	}

	/**
	 * Constructor
	 */
	public DoubleJSlider(String name, double val, double min, double max) {

		super();

		setName(name);
		setDoubleMinimum(min);
		setDoubleMaximum(max);
		setDoubleValue(val);
	}

	
	/**
	 * Constructor
	 */
	public DoubleJSlider(int  orientation, double min, double max, double val) {

		super(orientation);
		setName("");
		setDoubleMinimum(min);
		setDoubleMaximum(max);
		setDoubleValue(val);
	}

	
	/**
	 * returns Maximum in double precision
	 */
	public double getDoubleMaximum() {
		return (getMaximum() / Math.pow(10.0, this.mPrecision));
	}

	/**
	 * returns Minimum in double precision
	 */
	public double getDoubleMinimum() {
		return (getMinimum() / Math.pow(10.0, this.mPrecision));
	}

	/**
	 * returns Value in double precision
	 */
	public double getDoubleValue() {
		return (getValue() / Math.pow(10.0, this.mPrecision));
	}

	@Override
	public String getName() {
		return this.mName;
	}

	/**
	 * @return
	 */
	public double getPrecision() {
		return this.mPrecision;
	}

	/**
	 * @param val
	 */
	public void setDoubleMajorTickSpacing(double val) {
		setMajorTickSpacing((int) (val * Math.pow(10.0, this.mPrecision)));
	}

	/**
	 * sets Maximum in double precision
	 */
	public void setDoubleMaximum(double max) {
		setMaximum((int) (max * Math.pow(10.0, this.mPrecision)));
	}

	/**
	 * sets Minimum in double precision
	 */
	public void setDoubleMinimum(double min) {
		setMinimum((int) (min * Math.pow(10.0, this.mPrecision)));
	}

	public void setDoubleMinorTickSpacing(double val) {
		setMinorTickSpacing((int) (val * Math.pow(10.0, this.mPrecision)));
	}

	/**
	 * sets Value in double precision
	 */
	public void setDoubleValue(double val) {
		setValue((int) (val * Math.pow(10.0, this.mPrecision)));
		setToolTipText(Double.toString(val));
	}

	@Override
	public void setName(String name) {
		this.mName = name;
	}

	public void setPrecision(double prec) {
		this.mPrecision = prec;
	}

} // class DoubleJSlider
