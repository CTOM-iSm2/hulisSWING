package org.ctom.hulis.files.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.PeriodicTable.Entry;
import org.ctom.hulis.util.geometry.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Readxml extends GeometryReader {
	
	Document dom;
	private Geometry geom;
	File file;

	public Readxml(File file) {
		this.file = file;
		this.geom = new Geometry();
		parseXmlFile();
		parseDocument();
		this.geom.generateConnectivity();
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.files.xyz.GeometryReader#readAll(org.ctom.hulis.util.geometry.Geometry)
	 */
	@Override
	public void readAll(Geometry geometry) throws ReadxyzException {
		geometry = this.geom;
		
	}

	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private void parseXmlFile(){
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			//parse using builder to get DOM representation of the XML file
			dom = db.parse(this.file);


		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private void parseDocument(){
		//get the root element
		Element docEle = dom.getDocumentElement();

		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName(XMLElements.Atom.toString());
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; i < nl.getLength();i++) {

				//get the employee element
				Element el = (Element)nl.item(i);

				//get the Employee object
				Atom at = getAtom(el);

				//add it to list
				geom.add(at);
			}
		}
	}
	
	/*
	 * 	YC adapted from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private Atom getAtom(Element atEl) {

		//for each <atom> element get text or int values of
		//name ,id, age and name
		String name = getTextValue(atEl,XMLElements.Atom.toString());
		double x = getDoubleValue(atEl,XMLElements.X.toString());
		double y = getDoubleValue(atEl,XMLElements.Y.toString());
		double z = getDoubleValue(atEl,XMLElements.Z.toString());

		/*
		 * ca peut toujours etre utile
		 *		String type = atEl.getAttribute("type");
		 */

		//Create a new Atom with the value read from the xml nodes
		Atom at = new Atom(0, Entry.valueOf(name), org.ctom.hulis.huckel.PeriodicTable.Element.valueOf(name), name, name, 1, 0);
		this.setX(at, x);
		this.setY(at, y);
		this.setZ(at, z);
		return at;
	}
	
	/**
	 * Calls getTextValue and returns a int value
	 */
	private double getDoubleValue(Element ele, String tagName) {
		//in production application you would catch the exception
		return Double.parseDouble(getTextValue(ele,tagName));
	}
	
	/**
	 * I take a xml element and the tag name, look for the tag and get
	 * the text content
	 * i.e for <employee><name>John</name></employee> xml snippet if
	 * the Element points to employee node and tagName is 'name' I will return John
	 * 
	 * from http://www.java-samples.com/showtutorial.php?tutorialid=152
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}

	public Geometry getGeometry() {
		return geom;
	}
	
}
