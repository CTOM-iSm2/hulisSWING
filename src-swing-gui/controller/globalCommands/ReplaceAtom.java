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
 * IF X a un seul voisin A, on remplace X par B la distance (A,B) = sum of covalent radius, and A-B has same direction as A-X) 
 * ELSE X a plus d'un voisin, il est juste remplace by B,  aux memes coordonnes.
 *
 * X=old ;
 * A=hBond.getAtomLinkedWith(old)
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
	
				Bond bondNonH = old.getBonds().get(0);// recupere la seule liaison qui ne contient pas d'atome H
				Atom linkedAtom = bondNonH.getAtomLinkedWith(old); // assertion, l'atom linkedAtom n'est pas un hydrogene
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
				// A-X vector
				VecteurCTOM v= new VecteurCTOM(linkedAtom.getLocation(),old.getLocation());
				// on inverse le vecteur (devenu X_A) et on le translate pour placer X sur A 
				old.translate(v.times(-1));
				// on renorme le vecteur de la "longueur" AB 
				v.renormEquals(distAB);
				// et on deplace X a la bonne distance de A
				old.translate(v);
				// il ne reste plus qu'A remplacer X par B
			}
			m.replaceAtom(old, newAtom);
		}
	}
}
