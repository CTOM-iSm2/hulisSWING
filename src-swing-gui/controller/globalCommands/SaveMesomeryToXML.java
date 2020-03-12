package controller.globalCommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.comparators.MethodNameComparator;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.util.maths.Maths;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import util.io.SwingIO;
import util.xml.XMLConstants;

public class SaveMesomeryToXML implements XMLConstants, ICommand{
	
	public final static int precision=2;
	Mesomery mesomery;
	File out;

	public SaveMesomeryToXML(Mesomery mesomery, File out) throws FileNotFoundException, IOException {
		this.mesomery = mesomery;
		this.out = out;
	}

	public void execute() throws IOException {
		SwingIO.log(this.getClass().getName(), "execute", "save mesomery to xml");

		// racine of the xml
		Element racine = new Element(BALISE_RACINE);

		// get all structures created by gaussian .comtest
		//and put infos into xml file

		for (Structure s : mesomery.getAllStructures())
		{
			Element elemStruct = new Element(BALISE_STRUCTURE);
			Element elemType = new Element(BALISE_TYPE);
			elemType.setText(s.getClass().getName());
			elemStruct.addContent(elemType);
			Element elemEnergy = new Element(BALISE_ENERGY);
			Element elemAlpha = new Element(BALISE_ALPHA);
			elemAlpha.setText(String.valueOf(Maths.round (s.getTotalEnergy().getAlpha(),precision)));
			Element elemBeta= new Element(BALISE_BETA);
			elemBeta.setText(String.valueOf(Maths.round(s.getTotalEnergy().getBeta(),precision)));
			elemEnergy.addContent(elemAlpha);
			elemEnergy.addContent(elemBeta);
			elemStruct.addContent(elemEnergy);

			if (s instanceof StructureLocalized) {
				IMethod[] mmethods = this.mesomery.getMethods();
				Arrays.sort(mmethods, new MethodNameComparator());
				for (IMethod method : mmethods) {
					Element elemWeight = new Element (BALISE_WEIGHT);
					elemWeight.setText(String.valueOf( Maths.round( ((StructureLocalized) s).getWeight(method.getName()), precision) ));
					Attribute classe = new Attribute (CLASS_METHOD, method.getName());
					elemWeight.setAttribute(classe);
					elemStruct.addContent(elemWeight);
				}
			}

			racine.addContent(elemStruct);

		}

		//On cree un nouveau Document JDOM base sur la racine que l'on vient de creer
		Document document = new Document(racine);

		//On utilise ici un affichage classique avec getPrettyFormat()
		XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		//Remarquez qu'il suffit simplement de creer une instance de FileOutputStream
		//avec en argument le nom du fichier pour effectuer la serialisation.
		try {
			sortie.output(document, new FileOutputStream(out));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw e;
		}

		//   XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
		// sortie.output(document, System.out);

	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return "";
	}
}
