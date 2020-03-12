package org.ctom.hulis.huckel;


/**
 * This interface defines common methods and functions used in org.ctom.hulis.huckel atoms and org.ctom.hulis.huckel bonds 
 * @author ngoudard
 *
 */
public interface IHuckelObject extends IMoleculeComponent {

	/**
	 * @return electronic density
	 */
	public double getDensity();

	/**
	 * @return Huckel paramater name
	 */
	public String getNameParamHuckel();

	/**
	 * @return Huckel parameter name in HTML format used in tooltip text
	 */
	public String getNameParamHuckelHTML();

	/**
	 * @return Huckel parameter value
	 */
	public double getParamHuckel();


	/**
	 * @return parent delocalized bloc
	 */
	public BlocDelocalized getBlocDelocalizedParent();

	/**
	 * 
	 * @param parnet delicalized bloc 
	 */
	public void setBlocDelocalizedParent(BlocDelocalized blocDelocalized);

	/**
	 * 
	 * @return use getNbElectrons instead
	 */
	@Deprecated
	public int getNbRadR() ;
	
	
	/**
	 * This function returns the number of  electrons set by the user on an atom (radical) or on the bond
	 * 
	 * @return int
	 */
	public int getNbElectrons() ;

}
