package controller.globalCommands;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.util.geometry.CovalentRadii;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.util.maths.VecteurCTOM;

/**
 * We replace in m, A-X by A-B 
 * IF 	X = H, the distance (A,B) = sum of covalent radius, and A-B has same direction as A-H) 
 * ELSE X is just replaced by B, at the same position.
 *
 * A=hBond.getAtomLinkedWith(old)
 * X=old ;
 * B=newAtom;
 * 
 * @param SCALE: display scaling factor
 */
public class ReplaceAtom {
	public ReplaceAtom (Molecule m, Atom old, Atom newAtom) throws HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException  
	{
		synchronized(this) {
			//		SwingIO.PrintIfln("replace: "+old.getSigle()+"--->"+newAtom.getSigle()+".   Nbre de voisins nonH :"+old.countNeighboursExcludingH());
			if (old.countNeighboursExcludingH() <= 1) 
			{
				// gets A, The non H atom
	
				Bond hBond = old.getBonds().get(0);// recupere la seule liaison H
				Atom linkedAtom = hBond.getAtomLinkedWith(old); 
				// compute standard distance
				double covalentNew  = 0;
				double covalentLinked  = 0;
				if (newAtom.getElement() == null )  // cas atome Xx
					covalentNew = CovalentRadii.C.getCovRad();
				else 
					covalentNew = newAtom.getCovalentRadius();
				if (linkedAtom.getElement() == null )  // cas atome Xx
					covalentLinked = CovalentRadii.C.getCovRad();
				else 
					covalentLinked = linkedAtom.getCovalentRadius();
				double distAB =(covalentNew+covalentLinked)*Geometry.SCALE;
				// A-H vector
				VecteurCTOM v= new VecteurCTOM(linkedAtom.getLocation(),old.getLocation());
				// H_A vector to place H on A 
				old.translate(v.times(-1));
				// and displace H to the appropriate
				v.renormEquals(distAB);
				old.translate(v);
			}
			m.replaceAtom(old, newAtom);
		}
	}
}
