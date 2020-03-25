package tools;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.util.maths.Maths;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import util.xml.XMLConstants;

/**
 * Compare mesomery structures getting in a file .comtest to a content xml
 * @author ngoudard
 *
 */
public class XMLCompareToMesomery implements XMLConstants {
	public final static int DEFAULT_ENERGY_PRECISION = 2;
	public final static int DEFAULT_WEIGHT_PRECISION = 2;
	public final static double  DEFAULT_WEIGHT_TOLERANCE = 0.0;
	Mesomery mesomery;
	String xmlFileName;
	int energyPrecision;
	int weightPrecision;
	double weightTolerance;

	
	private void displayResume (Structure failed, Element racineXML ){
		TestOut.println("\n*** Test resume");
		TestOut.println("Energy precision : " +  energyPrecision);
		TestOut.println("Weight precision : " +  weightPrecision);
		TestOut.println("Weight tolerance : +/-" +  weightTolerance);
		if (failed != null)
			displayStructureMessage (failed, "Structure not found :");
		TestOut.println ("\nResultat attendu : XML Contents : ");
		String mess="";
		List<Element> lststructuresXML = racineXML.getChildren(BALISE_STRUCTURE);
		for (Element element : lststructuresXML) {
		    mess = element.getChild(BALISE_TYPE).getText();
			mess += "\t" + new Energy (Maths.round(Double.valueOf(element.getChild(BALISE_ENERGY).getChild(BALISE_ALPHA).getText() ), energyPrecision),
					Maths.round(Double.valueOf(element.getChild(BALISE_ENERGY).getChild(BALISE_BETA).getText()), energyPrecision)).toString();
					
			List<Element> weightsElement = element.getChildren(BALISE_WEIGHT);
			for (Element weight : weightsElement) {
				mess += "\t" + Maths.round(Double.valueOf(weight.getText()),weightPrecision) + "("+weight.getAttribute(CLASS_METHOD).getValue() + ")";
			//		mess += "\t" + Maths.round(Double.valueOf(weight.getText()),weightPrecision) + "("+weight.getAttribute(CLASS_METHOD).getValue() + ")";
			}
			TestOut.println(mess);
		}
		TestOut.println ("\nResultat obtenu : Mesomery structures : ");

		for (Structure s : mesomery.getSelectedStructures()) {
			mess = s.getName();
			mess+= "\t"+ s.getClass().getName();
			mess+= "\t"+ new Energy(Maths.round(s.getTotalEnergy().getAlpha(), energyPrecision), Maths.round(s.getTotalEnergy().getBeta(), energyPrecision)).toString();
			if (s instanceof StructureLocalized)
				for (IMethod method : mesomery.getMethods()) {
					mess+= "\t" + Maths.round(((StructureLocalized) s).getWeight(method.getName()), weightPrecision)+ "(" + method.getName()+")";
				}
			TestOut.println(mess);
		}
	}

	
	private void displayStructureMessage (Structure s, String  mess){
		String name = "Phi" + s.getName();
		
		double alpha = Maths.round(s.getTotalEnergy().getAlpha(), energyPrecision);
		double beta = Maths.round(s.getTotalEnergy().getBeta(), energyPrecision);
		double weightMesomery ;
		String weights = "";
		if (s instanceof StructureLocalized) 
			for (IMethod method : mesomery.getMethods()) {
				weightMesomery = Maths.round(((StructureLocalized) s).getWeight(method.getName()), weightPrecision);
				if (weights != "")
					weights += ", ";
				weights += method.getName() + " = " + weightMesomery;
			}
		
		TestOut.println (mess + " " + name + ", E=" + new Energy(alpha, beta) +", weights : " + weights);
	}
	
	/**
	 * Compare a structure element xml to a structure
	 * @param s : structure to compare
	 * @param element xml structure
	 * @return true if the both structures are the same
	 */
	public boolean isSame(Structure s, Element structureXMLelement){
		// verifie que le type de structure est le meme
		// si c'est le cas, compare les energies
		// si les energies sont egales compare le poids
		// sinon renvoie false
				
		if ( (s.getClass().getName()).endsWith(structureXMLelement.getChild(BALISE_TYPE).getText())
				&& (Maths.round(s.getTotalEnergy().getAlpha(), energyPrecision) == Maths.round(Double.valueOf(structureXMLelement.getChild(BALISE_ENERGY).getChild(BALISE_ALPHA).getText()), energyPrecision))
				&& (Maths.round( s.getTotalEnergy().getBeta(), energyPrecision) == Maths.round(Double.valueOf(structureXMLelement.getChild(BALISE_ENERGY).getChild(BALISE_BETA).getText()) , energyPrecision)) )
		{
			List<Element> weights = structureXMLelement.getChildren(BALISE_WEIGHT);
			if (s instanceof StructureLocalized) 
				for (IMethod method : mesomery.getMethods()) {
					boolean found = false;
					for (Element weight : weights) {
						if (weight.getAttribute(CLASS_METHOD).getValue().equals(method.getName())) {
							found = true;
							double weightXML = Maths.round(Double.valueOf(weight.getText()),weightPrecision);
							double weightMesomery = Maths.round(((StructureLocalized) s).getWeight(method.getName()), weightPrecision);
							if  (! ( (weightMesomery - weightTolerance <= weightXML) && (weightMesomery + weightTolerance >= weightXML))) {
								return false;
							}
							break;
						}
					}
					if (!found) {
						String mess="TEST FAILED !!! weights for method " + method.getName() + " not found in " + xmlFileName;
						TestOut.println(mess);
					}
				}
		}
		else
			return false;
		return true;
	}
	
	 /**
	  * 
	  * @param mesomery
	  * @param xmlFileName
	  * @param energyPrecision
	  * @param weightPrecision
	  * @param weightTolerance
	  */
	public XMLCompareToMesomery(Mesomery mesomery, String xmlFileName, int energyPrecision, int weightPrecision, double weightTolerance) {
		// open the both files
		this.mesomery = mesomery;
		this.xmlFileName = xmlFileName;
		this.energyPrecision = energyPrecision;
		this.weightPrecision = weightPrecision;
		this.weightTolerance = weightTolerance;
	}
	

	/**
	 * Compare all mesomery structures to a content xml
	 * @param mesomery
	 * @param xmlFileName
	 */
	public XMLCompareToMesomery(Mesomery mesomery, String xmlFileName) {
		// open the both files
		this.mesomery = mesomery;
		this.xmlFileName = xmlFileName;
		this.energyPrecision = DEFAULT_ENERGY_PRECISION;
		this.weightPrecision = DEFAULT_WEIGHT_PRECISION;
		this.weightTolerance = DEFAULT_WEIGHT_TOLERANCE;
	}
	
	
	public void execute() {
		TestOut.println ("Test Mesomery : " + xmlFileName);
		String mess;
		Document document = null;

		// cree une liste de structures a partir des structures de la mesomerie.
		// Chaque fois qu'une structure identique a ete trouvee dans le xml, on la supprime 
		// de cette liste.



		//On cree un nouveau document JDOM avec en argument le fichier XML
		//
		try {
			File fileXML = new File(  xmlFileName);
			SAXBuilder sxb = new SAXBuilder();
			document = sxb.build(fileXML);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mess="TEST FAILED !!!!Cannot open file "+ xmlFileName;
			TestOut.println(mess);
			fail(mess);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			mess="TEST FAILED !!!!Cannot open file "+ xmlFileName;
			TestOut.println(mess);
			fail(mess);
			e.printStackTrace();
		}
		 catch (Exception e) {		 
			mess=e.getMessage();
			TestOut.println(mess);
			fail(mess);
			e.printStackTrace();
		 }

		Element racine = document.getRootElement();
		Element racineClone = (Element) document.getRootElement().clone();
		List<Element> lststructuresXML = racine.getChildren(BALISE_STRUCTURE);

		// si la taille du xml n'est pas identique a la taille de la mesomerie etudie , le test a echoue
		if (lststructuresXML.size()  != mesomery.countSelectedStructs()) {
			mess = "TEST FAILED !!! " + xmlFileName + " xml results and mesomery count of structure are differents !";
			mess += "\nXML size : " + lststructuresXML.size();
			mess += "\nMesomery size : " + mesomery.countSelectedStructs();
			TestOut.println(mess);
			displayResume(null, racineClone);
		}
		
		// parcours toutes les tructures dans le xml et verifie 
		// si elle est dans la mesomerie. Si oui, on la supprime de la liste
		// si elle n'y est pas le test a echoue
		for (Structure s : mesomery.getSelectedStructures()) {
			Element found = null;
			displayStructureMessage (s, "Searching into " + mesomery.countSelectedStructs() + " selected structures");
			for (Element element : lststructuresXML) {
				if (isSame(s, element)) {
					found = element;
					break;
				}
			}	
			// on supprime la structure de la liste si elle est trouvee
			if (found != null) {
				lststructuresXML.remove(found);
			}
			else {
				mess = "TEST FAILED !!! Energy or weights of structure  Phi" + s.getName() + " not found in xml file : " + xmlFileName;
				TestOut.println (mess);				
				displayResume(s, racineClone);
				fail (mess + ". Check " + TestOut.OUT_NAME);
			}
		}

		// si la liste est vide, toutes les sturctures on ete trouvees dans le xml
		// donc la comparaison a reussi
		if (! lststructuresXML.isEmpty()) {
			mess= "TEST FAILED !!!! " + xmlFileName ;
			TestOut.println (mess);
			displayResume(null, racineClone);
			fail (mess + ". Check " + TestOut.OUT_NAME);
		}
		
		TestOut.println ("TEST OK : Energies and weights of all structures are found in the xml file : "  + xmlFileName);
	
		
	}

	
	
}
