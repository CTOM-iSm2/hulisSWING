package views;

import java.awt.Color;
import java.awt.FontMetrics;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.files.gaussian.WriteGaussianException;
import org.ctom.hulis.files.xyz.WritexyzException;
import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.adapters.MoleculeAdapter;
import org.ctom.hulis.huckel.comparators.MethodNameComparator;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.GeometryEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.HLPOverlapException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NullOvlMatrixException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.RedundanceException;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.listeners.IMoleculeListener;
import org.ctom.hulis.huckel.methods.HLPMethod;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.huckel.warning.IMethodWarning;
import org.ctom.hulis.huckel.warning.IMethodWarning.HulisWarningType;
import util.MyResourceBundle;
import util.SwingNumberFormater;
import util.ThreadUtils;
import util.gaussian.IOWriteGaussian;
import util.io.SwingIO;
import util.languages.LanguageManager;
import util.xyz.PrintWriterAdapter;

/**
 * This class describes the frame that displays the mesomery results
 * 
 * @since
 * @author Nicolas GOUDARD
 * 
 */
@SuppressWarnings("serial")
public class FrameResultMesomery extends FrameResult implements
IMesomeryReference, IMesomeryListener {
	public static String FORMAT_DECIMAL2 = "#.#";
	private static HashMap<Mesomery, FrameResultMesomery> instances = new HashMap<Mesomery, FrameResultMesomery>();
	public static String NOTHING;
	public static int SPACE_TABS = 10;

	public static int X_MIN_TABS = 20;


	public static FrameResultMesomery getInstance(FrameApp app) {
		Mesomery m = app.getCurrentMesomery();
		FrameResultMesomery fres = FrameResultMesomery.instances.get(m);

		if (fres == null) {
			try {
				fres = new FrameResultMesomery(app);
			} catch (Exception e) {
				SwingIO.warning("FrameResultMesomery'", "getInstance",
						e.getMessage(), e);
			}
			FrameResultMesomery.instances.put(m, fres);
			return fres;
		} else
			return fres;

	}


	protected MyResourceBundle bundle2;

	private Mesomery mesomery;
	int size;
	int size2;
	int align1;
	int align2;
	private JCheckBox initialOrientationCheckBox;
	public JCheckBox nboCheckBox;
	private MoleculeAdapter moleculeAdapter;


	/**
	 * 
	 * @param mesomery
	 * @throws Exception
	 */
	protected FrameResultMesomery(FrameApp app) throws Exception {
		super(app);
		this.mesomery = app.getCurrentMesomery();

		if (mesomery == null)
			throw new Exception("mesomery must be intitalized");
		this.mesomery = app.getCurrentMesomery();

		mesomery.addListener(this);
		moleculeAdapter = new FrameMoleculeAdapter();
		StructureDelocalized structureDelocalized = this.mesomery.getDelocalizedStructure();
		if (structureDelocalized != null)
			structureDelocalized.addListener((IMoleculeListener) moleculeAdapter);

		this.updateLanguage();
		super.refresh();

	}

	private class FrameMoleculeAdapter extends MoleculeAdapter {
		@Override
		public void geometryHasChanged(GeometryEvent event) {
			if (nboCheckBox.isSelected())
				refresh();
		}
	}

	@Override
	protected void constructPanelButton(JPanel pannelButton){
		pannelButton.add(this.jbFermer);
		this.nboCheckBox = new JCheckBox("NBO");
		this.nboCheckBox.addChangeListener(new ChangeListener()  {

			public void stateChanged(ChangeEvent e) {
				initialOrientationCheckBox.setSelected(nboCheckBox.isSelected());
				refresh();

			}});
		this.initialOrientationCheckBox = new JCheckBox("initial orientation");
		this.initialOrientationCheckBox.addChangeListener(new ChangeListener()  {

			public void stateChanged(ChangeEvent e) {
				refresh();

			}});
		pannelButton.add(this.nboCheckBox);
		pannelButton.add(this.initialOrientationCheckBox);
		pannelButton.add(this.jbRefresh);

	}

	//@Override
	public void dispose() {
		FrameResultMesomery.instances.remove(this.mesomery);
		this.mesomery.removeListener(this);
		StructureDelocalized structureDelocalized  = this.mesomery.getDelocalizedStructure();
		if (structureDelocalized != null)
			structureDelocalized.removeListener((IMoleculeListener) moleculeAdapter);
		super.dispose();

	}


	//@Override
	protected Color getBgTextColor() {
		return new Color(255, 240, 216);
	}

	/**
	 * 
	 */
	public Mesomery getMesomery() {
		return this.mesomery;
	}

	/**
	 * 
	 */
	@Override
	public void updateLanguage(){
		this.bundle2 = LanguageManager.getInstance().getResource("FrameResultMesomery");
		setTitle(this.bundle2.getString("ktitleresultsmesomery"));
		super.updateLanguage();

	}


	//@Override
	public void putResultTXT() {

		this.size = Integer.valueOf(this.bundle.getString("fontsize2"));
		this.size2 = 12;
		this.align1 = (Integer) this.bundle.getObject("align1");
		this.align2 = (Integer) this.bundle.getObject("align2");

		FrameResultMesomery.NOTHING = this.bundle2.getString("nothingmesomery");

		String RESULT_PHI_TOT = this.bundle2.getString("resmes1");

		int i;
		int j;
		// StringBuffer resultat = new StringBuffer();
		//	
		DateFormat f = DateFormat.getDateTimeInstance(
				DateFormat.FULL,
				DateFormat.FULL, LanguageManager.getInstance().getLocale());
		insertTXT("------------------------------------------------------------------\n", size, align1);
		insertTXT(FrameApp.ksoftname + " " + app.getVersion()  + " - " + f.format(new Date()) +"\n", size, align1);
		insertTXT("------------------------------------------------------------------\n", size, align1);
		insertTXT("\n", size, align1);

		// to announce results
		insertTXT(RESULT_PHI_TOT + "\n\n", size, align1);

		// If there is not mesomery
		if (mesomery.isAutoGenerating()) {
			insertTXT(this.bundle2.getString("waitweights")+"\n", size, align1);
			

			return;
		}
		
		if (mesomery.isCalculatingWeights()) {
			insertTXT(this.bundle2.getString("waitweights2")+"\n", size, align1);
			

			return;
		}
		
		if (this.mesomery.getDelocalizedStructure() == null) {
			insertTXT(FrameResultMesomery.NOTHING+"\n", size, align1);
			return;
		}

		// avertissement anions
		if (this.mesomery.getDelocalizedStructure().getNbElecCharge() > 0)
			insertTXT(this.bundle.getString("kWarNegCharge")+ "\n\n", size, align1);

		if (this.mesomery.countSelectedLocalizedStructs() == 0) {
			insertTXT(FrameResultMesomery.NOTHING+"\n", size, align1);
			this.writeNBOString();
			return;
		}


		// structure with incorrect number of electrons

		List<StructureLocalized> errStruct = this.mesomery
				.listIncorrectStructPiElecNumber();
		if (errStruct != null) {

			if (errStruct.size() > 0) {
				for (StructureLocalized s : errStruct) {
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
					switch (s.getError()) {
					case  ERROR_TOO_MUCH_SINGLE_ELECTRONS :
						insertTXT( MesomeryView.PSI
								+ s.getName() + " : " +
								this.bundle2.getString("tooMuchSingleElec") + "\n" ,
								size2, align2);
						break;
					case ERROR_INCORRECT_NB_PI_ELEC :
						Object args[] = {
								MesomeryView.PSI + s.getName(),
								new Integer(s.getSumOfPiElec()),
								new Integer(s.getMesomeryParent()
										.getDelocalizedStructure().getSumOfPiElec()) };
						insertTXT(MessageFormat.format(bundle2.getString("kelecerror"), args) + "\n", size,
								align1);
						break;
					}
				}
				this.writeNBOString();
				return;
			}
		}

		// if mesomery is not calculated
		if (mesomery.countSelectedLocalizedStructs() <= 0) {
			insertTXT(FrameResultMesomery.NOTHING, size, align1);
			return;
		}


		// display count of structures in the mesomery
		insertTXT(this.bundle2.getString("resmesnbgen") + " "
				+ this.mesomery.countLocalizedStructs() + "\n", size, align1);
		insertTXT(this.bundle2.getString("resmesnbdisp") + " "
				+ this.mesomery.countSelectedLocalizedStructs() + "\n", size,
				align1);

		double[][] matC2 = new double[0][0];
		// Display second matrix (Hamiltonian of the mesomery)
		insertTXT("\n", size, align1);
		insertTXT(this.bundle2.getString("resmes7") + "\n", size, align1);

		IMethod[] mmethods = this.mesomery.getMethods();
		Arrays.sort(mmethods, new MethodNameComparator());
		for (IMethod mmethod : mmethods) {
			
			if (mmethod.isImplementedHamiltonian()) {
				insertTXT("\n", size, align1);
				insertTXT(mmethod.getName() + " : \n", size2, align2);

				insertTXT("\t", size, align2);
				matC2 = this.mesomery.getMethod(mmethod.getName()).getHamiltonian();
				if (matC2 != null) {
					// ecrire les noms des structures en ligne	
					for (i = 0; i < matC2.length; i++)  {
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
						insertTXT(MesomeryView.PSI + (i + 1) + " \t", size2, align2);
					}
					if (matC2.length > 0)
						insertTXT("\n", size, align2);
					for (i = 0; i < matC2.length; i++) { // ecrire le contenu de
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
						// l'hamiltonien
						insertTXT(MesomeryView.PSI + (i + 1) + " \t ", size2,
								align2);
						for (j = 0; j < matC2[i].length; j++) {
							if (stopCurrentRefresh) {
								isRefreshing = false;
								return;
							}

							
							if (i == j)
								insertTXT(" "
										+ new Energy(this.mesomery.getDelocalizedStructure().getTotalEnergy().getAlpha(),
												matC2[i][j], this.getEnergyformater()) + " \t", size2, align2);
							else
								insertTXT(" " + new Energy(0, matC2[i][j], this.getEnergyformater()) + " \t",
										size2, align2);
						}
						insertTXT("\n", size, align2);
					} // if longueur matrice > 0
				} // if hamiltonien  not null
			}
		}



		insertTXT("\n", size, align1);


		/*
		 * Display overlap matrix if hlp is implemented
		 */

		HLPMethod hlpMethod  = (HLPMethod) this.mesomery.getMethod("HLP");
		double [][] overlaptab = new double[0][0];
		if (hlpMethod != null) {
			List<Structure> selectedStructures = mesomery.getSelectedStructures();
			insertTXT(bundle2.getString("hlpOverlap")+ "\n", size, align1);

			try {
				try {
					overlaptab = hlpMethod.getOverlapTabAll();
				} catch (ArithmeticException e) {
					insertTXT("Cannot compute weight : Exceed valence\n", size, align1);
					return;

				} catch (NullOvlMatrixException e) {
					insertTXT("Cannot compute weight : unexpected error " + e.getClass().getName() + " - " + e.getMessage(), size, align1);
					SwingIO.error(this.getClass().getName(), "putResultTXT", e.getClass().getName(), e);
				} catch (OrbitaleMoleculaireException e) {
					insertTXT("Cannot compute weight : unexpected error " + e.getClass().getName() + " - " + e.getMessage(), size, align1);
					SwingIO.error(this.getClass().getName(), "putResultTXT", e.getClass().getName(), e);
				} catch (HLPOverlapException e) {
					insertTXT("Cannot compute weight : unexpected error " + e.getClass().getName() + " - " + e.getMessage(), size, align1);
					SwingIO.error(this.getClass().getName(), "putResultTXT", e.getClass().getName(), e);
				}
				
				insertTXT("\t", size, align2);

				for (i = 0; i < overlaptab.length; i++)  
					insertTXT(MesomeryView.PSI + selectedStructures.get(i).getName()  + " \t", size2, align2);

				if (overlaptab.length > 0)
					insertTXT("\n", size, align2);

				for (i = 0; i < overlaptab.length; i++) { // ecrire le contenu de l'hamiltonien
					if (stopCurrentRefresh) {
						isRefreshing = false;
						return;
					}
					insertTXT(MesomeryView.PSI + selectedStructures.get(i).getName()  + " \t ", size2,
							align2);
					for (j = 0; j < overlaptab[i].length; j++) {
						if (stopCurrentRefresh) {
							isRefreshing = false;
							return;
						}
						insertTXT(" " + this.getMyDecimalFormatMO().format(overlaptab[i][j]) + " \t",
								size2, align2);
					}
					insertTXT("\n", size, align2);
				} // if longueur matrice > 0
			} catch (NoElectronException e) {
				insertTXT("Cannot compute weight : unexpected error " + e.getClass().getName() + " - " + e.getMessage(), size, align1);
				SwingIO.error(this.getClass().getName(), "putResultTXT", e.getClass().getName(), e);
			}
			catch (Exception e) {
				insertTXT("Cannot compute weight : unexpected error " + e.getClass().getName() + " - " + e.getMessage(), size, align1);
				SwingIO.error(this.getClass().getName(), "putResultTXT", e.getClass().getName(), e);
			}

		}

		insertTXT("\n", size, align1);

		/* 
		 *  Display tau, Coefficients and weights
		 */

		insertTXT(this.bundle2.getString("resmes10") + "\n", size, align1);
		insertTXT(this.bundle2.getString("resmes11") + "\n", size, align1);

		// Cas de demi electrons
		List<Structure> lstStructuresHaveDemiElectrons=this.mesomery.getStructuresHaveDemiElectrons();
		if ((getMesomery().getMethod("HLP") != null) && (lstStructuresHaveDemiElectrons.size()>0)) {		
			String comma="";
			String structuresNames="";
			for (Structure s: lstStructuresHaveDemiElectrons) {
				structuresNames+=(comma + MesomeryView.PSI + s.getName()) ;
				comma=", ";
			}
			Object args[] = {structuresNames};
			insertTXT(MessageFormat.format(bundle2.getString("kdemielectrons"), args) + "\n", size,align1) ;
		}

		
		// titles : name of methods
		insertTXT(this.bundle2.getString("methods")+"\t", size2, align2);
		for (IMethod method : mmethods)
			insertTXT( "\t\t" + method.getName(), size2, align2);
		insertTXT("\n", size2, align2);

		// tau
		String stau = "";
		boolean thereIsTau = false;
		for (IMethod method : mmethods) {
			if (method.isImplementedTau() && method.getError() == null) {
				String tau = SwingNumberFormater.doubleToString(this.mesomery.getMethod(method.getName())
						.getTau() * 100, this.getMyDecimalFormatMO(), "%");
				stau += "\t\t"+ tau;
				thereIsTau = true;
			}
			else 
				stau += "\t";
		}
		/*for (IMethod method : mmethods) {
			if (method.getWarnings().size()!=0) {
				for (IMethodWarning warning : method.getWarnings()) {
					if (warning.type==HulisWarningType.UNAPPROPRIATE_METHOD) {
						String str = warning.getMessage();
						insertTXT(str, size2, align1);
					}
				}
			}
		}		*/	

		if (thereIsTau) // insere une ligne avec les tau si au moins un tau existe
			insertTXT(this.bundle2.getString("tau") + stau + "\n", size2, align2);

		// columns
		insertTXT(MesomeryView.PSI + "i\tEi ", size2, align2);
		for (IMethod method : mmethods)
			insertTXT("\tCi\tWi" + method.getName(), size2, align2);
		insertTXT("\n", size2, align2);

		i = 0;
		for (StructureLocalized s : this.mesomery.getSelectedLocalizedStructures()) {
			if (stopCurrentRefresh) {
				isRefreshing = false;
				return;
			}

			double w = 0.0;
			double ci = 0.0;
			insertTXT(MesomeryView.PSI + s.getName() + " \t "
					+ Energy.format(s.getTotalEnergy(), this.getEnergyformater()), size2, align2);
			for (IMethod method : mmethods) {
				if (method.getCI() != null && method.getCI().length>0 && method.getError() == null) {
					ci = method.getCI()[i];
					insertTXT("\t" + SwingNumberFormater.doubleToString(ci, this.getMyDecimalFormatMO()), size2, align2);
				}
				else {
					insertTXT("\t", size2, align2);
				}

				if (this.mesomery.getMethod(method.getName()) != null
						&& i < method.getWeights().length) {
					w =  s.getWeight(method.getName());
					insertTXT( "\t" + SwingNumberFormater.doubleToString(w*100, this.getMyDecimalFormatMO(), "%")  , size2, align2);
				}
				else
					insertTXT("\t", size2, align2);
			}
			if (s.getError() == StructureLocalized.Error.ERROR_ALREADY_EXIST)
				insertTXT(" (" + MesomeryView.PSI
						+ s.getIdenticalStruct().getName() + " "
						+ this.bundle2.getString("kelecerror4") + ")",
						size2, align2);
			insertTXT("\n", size, align2);


			i++;
		}


		insertTXT("\n", size, align2);
		insertTXT("\n", size, align2);
		for (IMethod method : mmethods) {
			if (method.getError() != null) {
				if (method.getError() instanceof RedundanceException)
					insertTXT(bundle2.getString("methoderrorRedundance")+"\n", size, align1);
				else {
					Object args[] = { method.getName(), method.getError().getMessage()  };
					insertTXT(MessageFormat.format(bundle2.getString("methoderror"), args) +"\n", size, align1);
				}
			}
		}

		this.writeNBOString();

		// cite this work
		insertTXT("\n", size, align2);
		insertTXT(bundle2.getString("publications"), size, align2);


		/*
		tabulations
		 */
		int nb = 6;
		if (matC2.length > nb)
			nb = matC2.length ;
		if (overlaptab.length > nb)
			nb = overlaptab.length ;


		// Create a left-aligned tab stop at 100 pixels from the left margin
		FontMetrics metrics = getFontMetrics(this.txtResult.getFont());
		int width = FrameResultMesomery.SPACE_TABS
				+ metrics.stringWidth(Energy.format( this.mesomery.getDelocalizedStructure()
						.getTotalEnergy(), this.getEnergyformater()));
		tabule(0, this.txtResult.getStyledDocument().getLength() - 1,
				FrameResultMesomery.X_MIN_TABS, width, nb);

	}

	/**
	 * 
	 */
	public void writeNBOString() {


		if (this.nboCheckBox.isSelected()) {
			//NBO String
			insertTXT("\n",size, align1);
			insertTXT(this.bundle2.getString("knbostring")+"\n",size, align1);
			insertTXT("\n",size, align1);
			StringWriter stringWriter= new StringWriter();
			PrintWriterAdapter printWriter = new PrintWriterAdapter (stringWriter);
			DateFormat f = DateFormat.getDateTimeInstance(
					DateFormat.FULL,
					DateFormat.FULL, LanguageManager.getInstance().getLocale());	
			String messageHeader = "File generated by the " +FrameApp.ksoftname + " " + app.getVersion() + " code - " +  f.format(new Date()) ;

			try {
				IOWriteGaussian ioWriteGaussian = new IOWriteGaussian(this.mesomery);
				ioWriteGaussian.setMessageHeader(messageHeader);
				ioWriteGaussian.setWriteInitialOrientation(this.initialOrientationCheckBox.isSelected());
				ioWriteGaussian.setNumberFormater(new SwingNumberFormater(this.getMyDecimalFormatMO()));
				ioWriteGaussian.writeAll(printWriter);
				printWriter.flush();
				printWriter.close();
				insertTXT(stringWriter.toString(),size2, align2);
			} catch (WriteGaussianException e) {
				printWriter.close();
				insertTXT("Problem to write NBO string : " + e.getMessage(),size2, align2);
				e.printStackTrace();
			} catch (WritexyzException e) {
				printWriter.close();
				insertTXT("Problem to write NBO string : " + e.getMessage() ,size2, align2);
				e.printStackTrace();
			}
		}	
	}


	/* (non-Javadoc)
	 * @see views.IMesomeryReference#setMesomery(org.ctom.hulis.huckel.Mesomery)
	 */
	public void setMesomery(Mesomery mesomery) {
		FrameResultMesomery.instances.remove(this.mesomery);
		this.mesomery.removeListener(this);
		this.mesomery = mesomery;
		FrameResultMesomery.instances.put(mesomery, this);
		this.mesomery.addListener(this);
		this.refresh();
	}




	//@Override
	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		refresh();

	}

	//@Override
	public void mesomeryListWeightCalculated(MesomeryEvent e) {
		refresh();

	}

	//@Override
	public void mesomeryMethodAdded(MesomeryEvent e) {
		refresh();

	}

	//@Override
	public void mesomeryMethodRemoved(MesomeryEvent e) {
		refresh();

	}

	//@Override
	public void mesomeryStructureAdded(MesomeryEvent e) {
		refresh();



	}




	//@Override
	public void mesomeryStructureRemoved(MesomeryEvent e) {

		if (e.getOldValue() instanceof StructureDelocalized) 
			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {
					dispose();
					
				}
				
			});
		else
			refresh();



	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		if (nboCheckBox.isSelected())
			refresh();


	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		if (nboCheckBox.isSelected())
			refresh();


	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		if (nboCheckBox.isSelected())
			refresh();


	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		if (nboCheckBox.isSelected())
			refresh();


	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		if (nboCheckBox.isSelected())
			refresh();


	}



	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		refresh();



	}

	//@Override
	public void moleculeDeleted(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}



	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		refresh();


	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		refresh();
	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}


	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		// TODO Auto-generated method stub

	}


	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		refresh();


	}


	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {

	}


	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {

	}


	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		refresh();
	}


	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		//JOptionPane.showMessageDialog(null, "Votre structure est rejet�e");


	}

	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		refresh();
		
	}

	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		// TODO Auto-generated method stub
		
	}



}
