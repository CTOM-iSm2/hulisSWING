package org.ctom.hulis.files.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.PeriodicTable.Entry;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class Writexml {
	
	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	
	Document dom;
	private Geometry geom;
	File file;
	
	Molecule molecule;
	
	public Writexml(Molecule molecule, File file) {
		this.file = file;
		this.molecule = molecule;
		createDocument();
		createDOMTree();
		printToFile();
	}
	
	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private void createDocument() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			dom = db.newDocument();
		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "createDocument", "Error creating the dom");
		}
	}
	
	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private void createDOMTree() {
		
		//On cree la racine du document
		Element rootEle = dom.createElement(XMLElements.Geometry.toString());
		dom.appendChild(rootEle);
		
		Iterator it = molecule.getAtoms().iterator();
		while (it.hasNext()) {
			Atom at = (Atom)it.next();
			
			Element atomEle = createAtomElement(at);
			rootEle.appendChild(atomEle);
		}
	}

	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private Element createAtomElement(Atom at) {
		//on cree l'element atome
		Element AtomEle = dom.createElement(XMLElements.Atom.toString());
		if (at instanceof HuckelAtom) {
			AtomEle.setAttribute(XMLElements.Type.toString(), XMLTypes.HuckelAtom.toString());
		} else {
			AtomEle.setAttribute(XMLElements.Type.toString(), XMLTypes.NonHuckelAtom.toString());
		}
		//on cree le nom de l'atome
		Element nameEle = dom.createElement(XMLElements.Name.toString());
		nameEle.appendChild(dom.createTextNode(at.getSigle()));
		//on cree les coordonnees de l'atome
		Element xEle = dom.createElement(XMLElements.X.toString());
		xEle.appendChild(dom.createTextNode(""+at.getX()));
		Element yEle = dom.createElement(XMLElements.Y.toString());
		yEle.appendChild(dom.createTextNode(""+at.getY()));
		Element zEle = dom.createElement(XMLElements.Z.toString());
		zEle.appendChild(dom.createTextNode(""+at.getZ()));
		
		AtomEle.appendChild(nameEle);
		AtomEle.appendChild(xEle);
		AtomEle.appendChild(yEle);
		AtomEle.appendChild(zEle);
		
		return AtomEle;
	}
	
	private void printToFile() {
		try {
			OutputFormat format = new OutputFormat(dom);
			format.setIndenting(true);
			FileOutputStream fos = new FileOutputStream(file);
			XMLSerializer serializer = new XMLSerializer(fos,format);
			serializer.serialize(dom);
		} catch (Exception e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "printToFile", "Error while writing xml file");
		}
	}
	
	public static void main(String[] args) {
		Molecule mol = new Molecule();
		try {
			mol.createNewAtom(Entry.C, new Point3D(1,0,0));
			mol.createNewAtom(Entry.C, new Point3D(0,1,0));
			mol.createNewAtom(Entry.C, new Point3D(1,1,0));
		} catch (HuckelBondException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BondException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MoleculeTooManyNeighboursException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MoleculeCoherenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MoleculeBondExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Writexml wxml = new Writexml(mol, new File("newXML.xml"));
	}
	
}
