package org.ctom.hulis.huckel;

import java.io.Serializable;

import org.ctom.hulis.huckel.exception.HulisException;




/**
 * This class describe an energy 
 * An energy E can be written as below : 
 * E = n Alpha + m Beta 
 * where n and m are double variables
 * 
 * @see Atom
 * @see Molecule
 * @see Bond
 * @version 2.0
 * @since
 * @author Nicolas GOUDARD
 */

/**
 * @author goudard, carissan
 *
 */
public class Energy implements Comparable, Cloneable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7474770522893765317L;

	/**
	 * interface to format number in alpha and beta parameters
	 * @author macbook
	 *
	 */
	
	
	public enum LetterFormat {UTF8, ASCII};
	
	/**
	 * it is possible to implements this interface to format energy representation string
	 * @author macbook
	 *
	 */
	public interface EnergyFormater  {
		public LetterFormat getFormatLetters();
		public String formatAlpha(double number);
		public String formatBeta(double number);
	
	}
	
	/**
	 * This class represents the dafault format to a represent an energy in string format
	 * @author macbook
	 *
	 */
	public static class DefaultEnergyFormater  implements EnergyFormater{
		public  LetterFormat getFormatLetters() {
			return LetterFormat.UTF8;
			
		}
		
		public String formatAlpha(double number) {
			return Double.toString(number);
			
		}
		
		public String formatBeta(double number) {
			return Double.toString(number);
			
		}

	}
	
	public static  EnergyFormater DEFAULT_ENERGY_FORMATER= new DefaultEnergyFormater();
	public static final String ALPHA = "\u03B1"; // alpha letter constant used in the string representation of the energy
	public static final String BETA = "\u03B2";  // letter letter constant used in the string representation of the energy
	
	private double alpha; //  alpha value of the energy
	private double beta; //   beta value of the energy
	EnergyFormater energyFormater; // decimal format of alpha and beta in the string representation
	
	/**
	 * set a default energy formater for energy string representation 
	 * @param energyFormater
	 */
	public static void setDefaultEnergyFormater(EnergyFormater energyFormater) {
		DEFAULT_ENERGY_FORMATER = energyFormater;
	}

	/**
	 * This function return a string representation of an energy in the specified energy format
	 * @param e
	 * @param energyFormater
	 * @return
	 */
	public static String format(Energy e, EnergyFormater energyFormater) {
		double alpha = e.getAlpha();
		double beta = e.getBeta();
	    EnergyFormater  tmpEnergyFormater;
		
		if (energyFormater == null)
			tmpEnergyFormater = DEFAULT_ENERGY_FORMATER;
		else 
			tmpEnergyFormater = energyFormater;
		
		String sAlpha = "";
		String sBeta = "";
		switch (tmpEnergyFormater.getFormatLetters()) {
		case UTF8 :
			sAlpha = ALPHA;
			sBeta = BETA;
			break;
		case ASCII :
			sAlpha = "alpha";
			sBeta = "beta";	
			break;
		default :
			sAlpha = "alpha";
			sBeta = "beta";	
			break;
		}
	
		
		String resultat = "";

		if ((alpha == 0.0) && (beta == 0.0))
			return tmpEnergyFormater.formatBeta(beta) + sBeta;

		if (alpha == 1.0)
			resultat = sAlpha;
		else if (alpha == -1.0)
			resultat = "-" + sAlpha;
		else if (alpha != 0.0) 
			resultat = (tmpEnergyFormater.formatAlpha(alpha) + sAlpha);

		if ((beta) < 0.0) {
			if (alpha == 0.0)
				resultat += (tmpEnergyFormater.formatBeta(beta) + sBeta);
			else
				resultat += (" - " + tmpEnergyFormater.formatBeta(-beta) + sBeta);
			
		}
	    else if (beta >= 0.0) {
			if (alpha != 0)
				resultat += (" + " + tmpEnergyFormater.formatBeta(beta) + sBeta);
			else
				resultat += tmpEnergyFormater.formatBeta(beta) + sBeta;
		
		}
		
		tmpEnergyFormater = null;

		return resultat;


	}


	/**
	 * Constructor : construct an energy with alpha and beta coefficients specified in parameter.
	 * Example : new Energy (2.0,-5.0) -> E = 2.0 alpha -5.0 beta
	 * @param alpha parameter
	 * @param beta parameter
	 */
	public Energy(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
		this.energyFormater = null;
	}

	/**
	 * Constructor : construct an energy with alpha and beta coefficients, and the decimal format specified in parameter.
	 * @param alpha parameter
	 * @param beta parameter
	 * @param energyFormater
	 */
	public Energy(double alpha, double beta, EnergyFormater energyFormater) {
		this.alpha = alpha;
		this.beta = beta;
		this.energyFormater = energyFormater;
	}
	
	/** Constructor by copy.
	 * 
	 * @param e : energy to copy
	 */
	public Energy(Energy e) {
		this.alpha = e.getAlpha();
		this.beta = e.getBeta();
		this.energyFormater = e.energyFormater;

	}

	/**
	 * This method adds alpha and beta specified in parameter to the energy
	 * For example : add (1.0, 3.0) to E = alpha - beta -> E = 2.0 alpha + 2.0 beta
	 * @param alpha to add
	 * @param beta to add
	 */
	public void add(double alpha, double beta) {
		this.alpha += alpha;
		this.beta += beta;
	}

	/**
	 * This method adds alpha and beta of the energy specified in parameter to the energy
	 * For example : add (1.0, 3.0) to E = alpha - beta -> E = 2.0 alpha + 2.0 beta
	 * @param energy to add
	 */
	public void add(Energy e) {
		this.alpha += e.getAlpha();
		this.beta += e.getBeta();
	}

	/**
	 * This function returns a copy of this energy
	 * @return energy
	 **/
    @Override
	public Object clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return new Energy(this);
	}

	/**
	 * This function compares the energy to another
	 * @param : other energy
	 * @return 0 if energies are equals, 1 if this.beta is greater than o.beta, else -1
	 */
	public int compareTo(Object o) {

		if (! (o instanceof Energy))
			throw new ClassCastException(
					"object to compare is not an instance of Energy class");
		
		Energy e = (Energy) o;
		if (this.alpha != e.alpha)
			throw new ClassCastException(
					"alpha must be identic for the both compared energies");
//		return new Double(-this.beta).compareTo(new Double(-e.beta));
		return new Double(this.beta).compareTo(new Double(e.beta));

	}

	/**
	 * This function return the conjugation energy
	 * For example : E = alpha + 2.0 beta -> return alpha - 2.0 beta
	 * @return a new energy
	 */
	public Energy conjugation() {
		return new Energy(this.alpha, -this.beta);
	}

	/**
	 * This function tests if this energy is equal to another
	 * @return true if alpha and beta coefficients are equals to energy specified in the parameter
	 * @param an instance of Energy
	 * @exception ClassCastException if the parameter is not an instance of Energy
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Energy))
			throw new ClassCastException();
		Energy e = (Energy) o;
		// return (alpha == e.getAlpha()) && (Maths.round(beta, 2) ==
		// e.getBeta());
		return (this.alpha == e.getAlpha()) && (this.beta == e.getBeta());
	}

	private volatile int hashCode = 0;

	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + new Double(this.alpha).hashCode();;
            code = multiplier * code +  new Double(this.beta).hashCode();;
            hashCode = code;
        }
        return hashCode;
	
	}
	/**
	 * This function returns the energy alpha coefficient 
	 * @return double
	 */
	public double getAlpha() {
		return this.alpha;
	}


	/**
	 * This function returns the energy beta coefficient 
	 * @return double
	 */

	public double getBeta() {
		return this.beta;
	}


	/**
	 * This function tests if the energy is greater than the energy specified in parameter
	 * @return true or false
	 * @throws HulisException if alpha parameters are not equals
	 */
	public boolean isGreaterThan(Energy e) throws HulisException {
		if (e.getAlpha() != getAlpha())
			throw new HulisException("alpha must be identical to compare energies");

		return -this.beta > -e.getBeta();
	}

	/**
	 * This method multiply the energy by the number specified in parameter
	 *  For example : mult (3.0) to E = alpha - beta -> E = 3.0 alpha - 3.0 beta
	 * @param multiplicator
	 */
	
	public void mult(double n) {
		this.alpha *= n;
		this.beta *= n;
	}

	/**
	 * This method sets the alpha and beta part to the energy
	 * 
	 */
	
	public void set(double alpha, double beta) {
		this.alpha = alpha;
		this.beta = beta;
	}

	/**
	 * This method sets the decimal format for alpha and beta part used in string representations
	 * @param string number format
	 */
	public void setEnergyFormater(EnergyFormater sformat) {
		this.energyFormater = sformat;
	}

	
	/**
	 * This method subs alpha and beta specified in parameter to the energy
	 * For example : sub (1.0, 3.0) to E = 2.0 alpha - beta -> E = alpha - 2.0 beta
	 * @param alpha to sub
	 * @param beta to sub
	 */

	public void sub(double alpha, double beta) {
		this.alpha -= alpha;
		this.beta -= beta;
	}

	
	/**
	 * This method subs alpha and beta of the energy specified in parameter to the energy
	 * @param energy to sub
	 */

	public void sub(Energy e) {
		this.alpha -= e.getAlpha();
		this.beta -= e.getBeta();
	}

	/**
	 * This function returns a string representation of the energy
	 */
	@Override
	public String toString() {

		return format(this, this.energyFormater);

	}
	
	/**
	 * 
	 * @param energyFormater
	 * @return
	 */
	public String toString(EnergyFormater energyFormater) {

		return format(this, energyFormater);

	}
	


	/**
	 * This function returns a string parameter for the part alpha 
	 * @return string
	 */
	public String alphaToString() {
		if (this.energyFormater == null)
			return this.alpha +  Energy.ALPHA;
		else
			return this.energyFormater.formatAlpha(this.alpha) + Energy.ALPHA;
	
	}


	/**
	 * This function returns a string parameter for the part beta 
	 * @return string
	 */
	public String betaToString() {
		if (this.energyFormater == null)
			return this.beta +  Energy.BETA;
		else
			return this.energyFormater.formatBeta(this.beta) + Energy.BETA;
	}


	/**
	 * @param orb : reference orbital
	 * @return the lowest energy comparable with the energy of the reference orbital 
	 */
	public static Energy MIN(Orbitale orb) {
		Energy e=new Energy(orb.getEnergy().getAlpha(), Integer.MAX_VALUE);
		return e;
	}

	/**
	 * @param orb : reference orbital
	 * @return the highest energy comparable with the energy of the reference orbital 
	 */
	public static Energy MAX(Orbitale orb) {
		Energy e=new Energy(orb.getEnergy().getAlpha(),-Integer.MAX_VALUE);
		return e;
	}

}
