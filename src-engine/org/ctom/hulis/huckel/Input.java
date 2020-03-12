package org.ctom.hulis.huckel;

import javax.swing.JOptionPane;


/**
 * 
 * @author Y Carissan
 *
 */
public class Input {

	public static final boolean bidouille_allyl_cation_VB = false;
	// un seul doit etre true!!
	public static final boolean bidouille_allyl_radical_VB = false;
	public static final boolean coulsonWeights = false;
	public static final boolean extendedHLCI = false;
	// term added on the extra diagonal of S between p orbitals
	private static double extraDiagS_Huckel = 0.0;
	public static final boolean forceBvalue = false;
	public static final double forcedBvalue = -0.7;
	public static final boolean forceUnitOverlap = false;
	public static final boolean HLCIS = false;
	private static boolean interactive = false;

	public static final boolean inverseOverlapWeights = false;
	//
	public static final boolean lowdin = true;
	public static final boolean modifySHuckel = false;

	public static boolean oldfashion = false;
	public static final boolean overrideS_UNIT = false;

	public static final double RESET = 0.22011977;
	public static final boolean scanOverlap = false;
	public static final boolean scanValB = false;
	public static final boolean setBvalue = false;
	// NG : les JOpionPane devraient etre appelees de l'interface Swing : HulisSwingGui
	public double askBValue() {
		String ans;
		ans = JOptionPane.showInputDialog(null, "Valeur de B ?");
		double val = Double.parseDouble(ans);
		return val;
	}

	private void askExtraDiagS_Huckel() {
		String ans;
		double val;
		if (Input.interactive) {
			ans = JOptionPane.showInputDialog(null, "Valeur de Si+1 ?");
			val = Double.parseDouble(ans);
		} else
			val = Input.extraDiagS_Huckel;
		setExtraDiagS_Huckel(val);
	}

	public double getExtraDiagS_Huckel() {
		if (Input.modifySHuckel && Input.extraDiagS_Huckel == Input.RESET) {
			askExtraDiagS_Huckel();
		}
		return Input.extraDiagS_Huckel;
	}

	public void setExtraDiagS_Huckel(double val) {
		Input.extraDiagS_Huckel = val;
	}

}