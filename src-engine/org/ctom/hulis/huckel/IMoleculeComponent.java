package org.ctom.hulis.huckel;

import org.ctom.util.maths.Point3D;



/**
 * This interface describe common method to molecule components : atom or bond
 * @author ngoudard
 *
 */
public interface IMoleculeComponent {

	/**
	 * This method deletes a molecule component
	 */
	public void remove() throws Exception;
	
	/**
	 * @return the index of the component in the molecule
	 */
	public int getIndex();

	/**
	 * 
	 * @return the molecule that contains the component
	 */
	public Molecule getMoleculeContainer();

	/**
	 * This function returns the component abscissa 
	 * 
	 */

	public double getX();

	/**
	 * This function returns the component ordinate 
	 * 
	 */
	public double getY();

	/***
	 * 
	 * @return the 3D location of the molecule component
	 */
	public Point3D getLocation();

}
