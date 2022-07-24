package org.ctom.hulis.huckel;

import org.ctom.hulis.huckel.exception.BlocDelocalizedException;
import org.ctom.hulis.huckel.exception.HulisException;

/**
 * 
 * @author nicolasgoudard
 *
 */
public class MonoBlock extends HuckelBlock  {

	/**
	 * 
	 * @param huckelObject
	 * @throws HulisException 
	 */
	public MonoBlock(IHuckelObject huckelObject) throws  HulisException{
		super();
		if (huckelObject.getBlocDelocalizedParent() != null) throw new HulisException(huckelObject + " is already in a block");
		if (huckelObject instanceof HuckelBond) {
			HuckelBond huckelBond = (HuckelBond) huckelObject;
			HuckelAtom a1= huckelBond.getAtom1();
			if (a1.getBlocDelocalizedParent() != null) throw new HulisException(a1 + " is already in a block");			
			lstHuckelObject.add(a1);
			HuckelAtom a2= huckelBond.getAtom2();
			if (a2.getBlocDelocalizedParent() != null) throw new HulisException(a2 + " is already in a block");
			lstHuckelObject.add(a2);
		}
		this.lstHuckelObject.add(huckelObject);
		this.nbElectrons =huckelObject.getNbElectrons();
	}
	
	
	
}
