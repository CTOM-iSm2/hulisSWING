package views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.symmetry.Symmetry;
import org.ctom.hulis.util.io.HuckelIO;

import Jama.Matrix;
import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;

/**
 * This class describes the frame that displays the results
 * calculated for the moleculeAbstract
 * 
 * @version 1.3
 * @since
 * @author Nicolas GOUDARD
 * 
 */

@SuppressWarnings("serial")
public class FrameResultStruct extends FrameResult implements IStructureListener, IStructureLocalizedListener, IStructureDelocalizedListener, IStructureReference {

	private static HashMap<Structure, FrameResultStruct> instances = new HashMap<Structure, FrameResultStruct>();
	public static int SPACE_TABS = 10;
	public static int X_MIN_TABS = 20;

	public static FrameResultStruct getInstance(FrameApp app) {
		Structure s = app.getCurrentMesomeryView().getCurrentStructure();
		FrameResultStruct fres = FrameResultStruct.instances.get(s);

		if (fres == null) {
			try {
				fres = new FrameResultStruct(app);
				FrameResultStruct.instances.put(s, fres);
			} catch (Exception e) {
				SwingIO.warning("FrameResultStruct", "getInstance", e
						.getMessage(), e);

			}
		} 

		return fres;

	}

	protected MyResourceBundle bundle2;

	Structure structure;
	private Symmetry symmetry;

	/**
	 * FrameResultStruct : Constructor
	 */

	protected FrameResultStruct(FrameApp app) throws Exception {
		super(app);
		final Structure structure = app.getCurrentMesomeryView().getCurrentStructure();

		if (structure == null)
			throw new Exception("structure must be initialized");
		this.structure = structure;
		this.symmetry = null;
		structure.addListener(this);


		this.updateLanguage();
	}

	//@Override
	public void dispose() {
		FrameResultStruct.instances.remove(this.structure);
		this.structure.removeListener(this);
		super.dispose();
	}

	//@Override
	protected Color getBgTextColor() {
		return new Color(240, 255, 255);
	}

	public Structure getStructure() {
		return this.structure;
	}



	public void moleculeDeleted(MoleculeEvent e) {
		final FrameResultStruct frame = this;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				structure.removeListener(frame);
				dispose();

			}

		});

	}

	@Override
	public void updateLanguage(){
		this.bundle2 = LanguageManager.getInstance().getResource("FrameResultStruct");
		setTitle(MesomeryView.PSI + this.structure.getName() + " - " + this.bundle2.getString("ktitleresultstruct"));
		super.updateLanguage();

	}
	//@Override
	public void putResultTXT() {
		int size = Integer.valueOf(this.bundle.getString("fontsize2"));
		int size2 = 12;
		int align1 = (Integer) this.bundle.getObject("align1");
		int align2 = (Integer) this.bundle.getObject("align2");


		String RESULT_NO_ATOM = this.bundle2.getString("resh0");
		String RESULT_METHOD = this.bundle2.getString("resh1");
		String RESULT_ATOM_NB = this.bundle2.getString("resh2");
		String RESULT_HUCKEL_ATOM_NB = this.bundle2.getString("resh2a");
		String RESULT_PI_ELEC_NB = this.bundle2.getString("resh3");
		String RESULT_NET_CHARGE = this.bundle2.getString("resh4");
		String RESULT_HAMILT = this.bundle2.getString("resh5");
		String RESULT_ORB = this.bundle2.getString("resh6");
		String RESULT_ENERGY = this.bundle2.getString("resh7");
		String RESULT_TOTAL_ENERGY = this.bundle2.getString("resh8");
		String RESULT_BOND_ORDER = this.bundle2.getString("resh9");
		String RESULT_CHARGES = this.bundle2.getString("resh10");
		String RESULT_TOTAL_CHARGE = this.bundle2.getString("resh11");
		String RESULT_ELEC_DISTRIB = this.bundle2.getString("resh12");
		String RESULT_ATOM_CHARGE = this.bundle2.getString("resh13");

		//	
		DateFormat f = DateFormat.getDateTimeInstance(
				DateFormat.FULL,
				DateFormat.FULL, LanguageManager.getInstance().getLocale());
		insertTXT("------------------------------------------------------------------\n", size, align1);
		insertTXT(FrameApp.ksoftname + " " + app.getVersion()  + " - " + f.format(new Date()) +"\n", size, align1);
		insertTXT("------------------------------------------------------------------\n", size, align1);
		insertTXT("\n", size, align1);

		if (this.structure.countAtoms() == 0) {
			insertTXT(MesomeryView.PSI + this.structure.getName() + " : "
					+ RESULT_NO_ATOM, size, align1);
			return;
		}

		int i;
		int j;

		double[][] hamiltonien = this.structure.getHamiltonianArray();

		try {

			insertTXT(MesomeryView.PSI + this.structure.getName() + "\n", size,
					align1);

			if (hamiltonien == null)
				return;

			insertTXT(RESULT_METHOD, size, align1);

			if (this.structure instanceof StructureDelocalized)
				insertTXT(this.bundle2.getString("kdeloc"), size, align1);
			else
				insertTXT(this.bundle2.getString("kloc"), size, align1);
			insertTXT("\n", size, align1);

			insertTXT(RESULT_ATOM_NB + this.structure.countAtoms() + "\n",
					size, align1);

			insertTXT(RESULT_HUCKEL_ATOM_NB + this.structure.countHuckelAtoms()
					+ "\n", size, align1);

			insertTXT(RESULT_PI_ELEC_NB + this.structure.getSumOfPiElec()
					+ "\n", size, align1);

			insertTXT(RESULT_NET_CHARGE + (-this.structure.getNbElecCharge())
					+ "\n", size, align1);

			insertTXT("\n", size, align1);

			// 1- hamiltonien
			insertTXT(RESULT_HAMILT + "\n", size, align1);
			insertTXT("\t", size, align1);

			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < hamiltonien.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1) + "  ", size2, align2);
					insertTXT(this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle()
							+ "\t", size2, align2);
				}

			insertTXT("\n", size, align2);

			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < hamiltonien.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1) + "  ", size2, align2);
					insertTXT(this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle()
							+ "\t", size2, align2);
					for (j = 0; j < hamiltonien[i].length; j++) {
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
	
						if (i == j)
							insertTXT(new Energy(1, hamiltonien[i][j], this.getEnergyformater()) + "\t",
									size2, align2);
						else
							insertTXT(new Energy(0, hamiltonien[i][j], this.getEnergyformater()) + "\t",
									size2, align2);
					}
					insertTXT("\n", size, align2);
				}

			insertTXT("\n", size, align2);

			// energies des orbitales
			insertTXT(RESULT_ORB + "\n", size, align1);

			insertTXT(RESULT_ENERGY + "\t", size, align1);

			// energies en ligne
			double[] energies = this.structure.getEnergiesArray();

			for (i = 0; i < energies.length; i++) {
				if (stopCurrentRefresh) {
					isRefreshing = false;
					return;
				}

				insertTXT((new Energy(1, energies[i], this.getEnergyformater())) + "\t", size2, align2);
			}

			insertTXT("\n", size, align2);

			double[] repartElec = this.structure.getDistributionElecPiArray();
			insertTXT(RESULT_ELEC_DISTRIB + "\t", size, align2);

			for (i = 0; i < repartElec.length; i++) {
				if (stopCurrentRefresh) {
					isRefreshing = false;
					return;
				}

				insertTXT(this.getMyDecimalFormatMO().format(repartElec[i]) + "\t", size2, align2);

			}

			// energies des orbitales
			double[][] orbitales = this.structure.getOrbitalsCoeffArray();
			
			HuckelIO.PrintMatrix(new Matrix(orbitales), "orbitals results window");

			insertTXT("\n", size, align2);

			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < orbitales.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1) + "  ", size2, align2);
					insertTXT(this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle()
							+ "\t", size2, align2);
					for (j = 0; j < orbitales[i].length; j++) {
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
	
						insertTXT(this.getMyDecimalFormatMO().format(orbitales[i][j]) + "\t", size2,
								align2);
					}
					insertTXT("\n", size2, align2);
				}

			insertTXT("\n", size, align2);

			// energie totale
			Energy totalEnergy = new Energy(this.structure.getTotalEnergy().getAlpha(), this.structure.getTotalEnergy().getBeta(), this.getEnergyformater()) ;
			insertTXT(RESULT_TOTAL_ENERGY + totalEnergy
					+ "\n\n", size, align1);

			// bond orders
			insertTXT(RESULT_BOND_ORDER, size, align1);

			double[][] bondOrders = this.structure.getBondOrdersArray();

			insertTXT("\n", size, align1);
			insertTXT("\t", size, align1);
			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < bondOrders.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1) + "  ", size2, align2);
					insertTXT(this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle()
							+ "\t", size2, align2);
				}
			
			insertTXT("\n", size, align2);
			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < bondOrders.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1) + "  ", size2, align2);
					insertTXT(this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle()
							+ "\t", size2, align2);
					for (j = 0; j < bondOrders[i].length; j++) {
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
	
						insertTXT("" + this.getMyDecimalFormatMO().format(bondOrders[i][j]) + "\t", size2,
								align2);
					}
					insertTXT("\n", size, align2);
				}

			// charges des atomes
			insertTXT("\n", size, align1);
			insertTXT(RESULT_CHARGES + "\n", size, align1);
			insertTXT(RESULT_ATOM_CHARGE + "\n", size, align2);

			double[] tblCharges = this.structure.getChargeDensitiesArray();
			if (this.structure.areCorrectAtomsSeqNum())
				for (i = 0; i < tblCharges.length; i++) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
	
					insertTXT((i + 1)
							+ "  "
							+ this.structure.getHuckelAtomBySeqNum(i + 1)
							.getSigle() + "\t", size2, align2);
					insertTXT(this.getMyDecimalFormatMO().format(tblCharges[i]) + "\n", size2, align2);
				}

			insertTXT("\n", size, align2);

			insertTXT(RESULT_TOTAL_CHARGE
					+ this.getMyDecimalFormatMO().format(this.structure.getSumOFChargeDensities())
					+ "\n\n", size, align1);

			FontMetrics metrics = getFontMetrics(this.txtResult.getFont());
			int width = FrameResultMesomery.SPACE_TABS
					+ metrics.stringWidth(Energy.format(structure.getTotalEnergy(), this.getEnergyformater()));
			
			if (hamiltonien.length>0)
				tabule(0, this.txtResult.getStyledDocument().getLength() - 1,
						FrameResultStruct.X_MIN_TABS, width,
						hamiltonien[0].length);

		} catch (Exception e) {
			SwingIO.warning(this.getClass().getName(), "putResultTXT", e
					.getMessage(), e);

		}
		if (PreferencesGUI.getSuperMario()) {
			Structure struct = this.structure;
			String str = struct.getCharacteristicPolynomial().toString();
			insertTXT(str+"\n", size, align1);

			Symmetry sym = this.getSymmetry();
			HashMap<String, ArrayList<Integer>> uniqueAtoms = sym.getUniqueAtoms();
			for (ArrayList<Integer> list : uniqueAtoms.values()) {
				insertTXT(list.toString(), size, align1);
			};
			insertTXT("\n", size, align1);
			HashMap<String, ArrayList<Bond>> uniqueBonds = sym.getUniqueBonds();
			for (ArrayList<Bond> list : uniqueBonds.values()) {
				insertTXT(list.toString(), size, align1);
			};
			insertTXT("\n", size, align1);
		}
	}

	/*
	 * get structure symmetry
	 * @return symmetry
	 */
	private Symmetry getSymmetry() {
		if (symmetry == null)
			createSymmetry();
		return symmetry;
	}


	/*
	 * create structure symmetry
	 */
	private void createSymmetry() {
		this.symmetry = new Symmetry(this.getStructure());

	}

	public void setStructure(Structure s) {
		FrameResultStruct.instances.remove(this.structure);
		this.structure.removeListener(this);
		this.structure = s;
		FrameResultStruct.instances.put(this.structure , this);
		this.structure.addListener(this);
		this.refresh();
	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		createSymmetry();
		refresh();



	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		createSymmetry();
		refresh();


	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		createSymmetry();
		refresh();


	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		createSymmetry();
		refresh();

	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		createSymmetry();
		refresh();


	}



	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		refresh();
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		refresh();

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		refresh();


	}



	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		createSymmetry();
		refresh();



	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		refresh();



	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		refresh();


	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		refresh();


	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		createSymmetry();
		refresh();



	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		refresh();

	}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		refresh();


	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		//TODO
	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		//TODO
	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		refresh();
	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		refresh();




	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		refresh();



	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	//////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}
}
