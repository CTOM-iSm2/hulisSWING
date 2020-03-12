package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.io.SwingIO;
import controller.globalCommands.EnableActionManager;

@SuppressWarnings("serial")
public class MesomeryView extends JTabbedPane implements IMesomeryReference,
ChangeListener, IMesomeryListener {

	public class Child extends JPanel {
		StructureView structureView = null;;

		public Child(StructureView structureView) {
			super();
			this.structureView = structureView;

		}

		public void erase() {
			this.structureView.erase();
			this.structureView = null;
		}

		public StructureView getStructureView() {
			return this.structureView;
		}

	}

	public final static double DEFAULT_ZOOM = 0.8;
	public final static double INCR_ZOOM = 0.1;
	public final static double MAX_ZOOM = 10.0;
	public final static double MIN_ZOOM = 0.1;

	public static final String PSI = "\u03C8";
	public static final String TAU = "\u03C4";
	public boolean isDisplayPolynomes = false;
	public boolean isDisplayHxHxy = false;
	public boolean isDisplayCharges = false;
	public boolean isDisplayNum = false;
	private FrameApp frameApp;
	// private final Lock lock = new ReentrantLock();
	private HashMap<Structure, Child> indexedTabsMap;

	protected Mesomery mesomery;

	protected double zoom ;

	protected double angle;

	protected StructureLocalized structureLocalizedOwnerCurrentBloc;

	private double xMargin;

	private double yMargin;

	
	public MesomeryView(Mesomery mesomery, FrameApp frameApp) {
		super(SwingConstants.TOP);

		this.setFont(new Font("Helvetica", Font.PLAIN, 11));
		this.mesomery = mesomery;
		this.frameApp = frameApp;

		mesomery.addListener(this);
		this.indexedTabsMap = new HashMap<Structure, Child>();
		addChangeListener(this);
		setDefaultsPrefs();
	}

	public void updateLanguage() {
		if (this.getCurrentStructureView() != null)
			this.getCurrentStructureView() .repaint();
	}

	/**
	 *  return the first structure view that have a selected bloc
	 */
	public StructureView getFirstStructureWithSelectedBloc() {

		for (StructureView sv : this.getListStructureView()) 

			if (sv.getSelectedBlocDelocalized() != null) 
				return sv;

		return null;

	}

	/**
	 * This methods adds a tab
	 * 
	 * @param structureDrawer
	 *            to be added
	 */

	public synchronized void addStructureView(final Structure structure) throws Exception {
		if (!this.mesomery.exists(structure))
			throw new Exception("structure does not belong to the mesomery");


		// lock.lock();
		// try {
		final StructureView structureView = new StructureView(structure, this);

		//		ThreadUtils.invokeAsSoonAsPossible(new Runnable () {

		//		public void run() {



		final Child child = new Child(structureView);
		child.setPreferredSize(getPreferredSize());
		// JScrollPane ascPanneauMolecule = ne;w
		// JScrollPane(panneauMolecule,
		// JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		// JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		indexedTabsMap.put(structure, child);

		addTab(MesomeryView.PSI + structure.getName(), child);




		structureView.setPreferredSize(
				new Dimension( (int) getPreferredSize().getWidth()  * 5 / 7,
						(int) getPreferredSize().getHeight() ));




		structureView.getGraphOM().setPreferredSize(
				new Dimension((int) (getPreferredSize().getWidth() * 2 / 7),					
						(int) getPreferredSize().getHeight() ));


		structureView.setBackground(getBackground());
		structureView.getGraphOM().setBackground(getBackground());



		child.setLayout(new BoxLayout (child, BoxLayout.X_AXIS));
		child.add(structureView);
		GraphOM graphOM = structureView
				.getGraphOM();
		child.add(graphOM);

		//	}});

	}


	public int countDisplayedStructures() {
		return super.getTabCount();
	}

	public boolean isDisplayPolynomes() {
		return this.isDisplayPolynomes;
	}

	public boolean isDisplayCharges() {
		return this.isDisplayCharges;
	}

	public boolean isDisplayHxHxy() {
		return this.isDisplayHxHxy;
	}

	public boolean isDisplayNum() {
		return this.isDisplayNum;
	}


	/**
	 * This function returns the current sructure displayed
	 * 
	 * @return structure
	 */

	public Structure getCurrentStructure() {

		StructureView sv = this.getCurrentStructureView();
		if (sv == null) {
			SwingIO.warning(this.getClass().getName(),
					"getCurrentStructure", "cannot select structure view");
			return null;
		}
		return  sv.getStructure();

	}

	public StructureView getCurrentStructureView() {
		try {
			Child child = (Child) getSelectedComponent();
			if (child == null)
				return null;
			return child.getStructureView();
		} catch (Exception e) {
			SwingIO.warning(this.getClass().getName(),
					"getCurrentStructureView", "cannot select tab", e);
			return null;
		}
	}

	public FrameApp getFrameApp() {
		return this.frameApp;
	}

	//@Override
	public Insets getInsets() {
		return new Insets(0, 0, 0, 0);
	}

	// end yannick
	public Mesomery getMesomery() {
		return this.mesomery;
	}

	/**
	 * This function returns the current structure displayed
	 * 
	 * @return structure
	 */

	public StructureView getStructureView(Structure structure) {
		Child child = this.indexedTabsMap.get(structure);
		if (child == null) {
			SwingIO.warning(this.getClass().getName(), "getStructureView",
					"cannot select tab");
			return null;
		}
		return child.getStructureView();

	}


	/**
	 * This function returns the list of displayed structureviews
	 * 
	 * @return list of displayed structure views
	 */

	public List<StructureView> getListStructureView() {
		List <StructureView> lst = new ArrayList<StructureView>();
		for (Structure s : this.indexedTabsMap.keySet())
			lst.add(this.indexedTabsMap.get(s)
					.getStructureView());
		return lst;

	}



	public double getZoom() {
		return this.zoom;
	}




	public void rebuild() {
		// System.out.println("rebuild : " + mesomery.getEnergyMax());

		Structure sav = getCurrentStructure();


		List<Structure> lstSelect = this.mesomery.getAllStructures();

		for (int i = getTabCount() - 1; i >= 0; i--) {
			Child child = (Child) super.getComponentAt(i);
			child.erase();
			this.remove(i);
			child = null;
		}

		this.indexedTabsMap.clear();

		lstSelect = this.mesomery.getSelectedStructures();

		for (int i = 0; i < this.mesomery.countSelectedStructs(); i++) {
			// System.out.println(((Structure) lstSelect.get(i) ).getName());
			Structure structure = lstSelect.get(i);
			if (this.mesomery.getSelectedStructures().contains(structure)) {
				try {
					addStructureView(structure);

				} catch (Exception e) {
					SwingIO.error(getName(), "rebuild", e.getMessage(), e);
					SwingIO.reportError(frameApp);
				}
			}
		}

		for (int i = 0; i < getTabCount(); i++) {
			// System.out.println(((Structure) lstSelect.get(i) ).getName());
			Child child = (Child) getComponent(i);
			if (child == null)
				break;
			StructureView sv = child.getStructureView();
			if (sv == null)
				break;
			Structure s = sv.getStructure();
			if (s == sav) {
				setCurrentStructure(s);
				break;
			}
		}

	}

	/**
	 * This method refreshes each structure org.ctom.view
	 */
	public void refresh() {
		StructureView structureView;
		List<Structure> lstSelect = this.mesomery.getSelectedStructures();

		for (int i = 0; i < this.mesomery.countSelectedStructs(); i++) {
			structureView = getStructureView((Structure) lstSelect.get(i));
			if (structureView != null)
				structureView.repaint();
			else
				SwingIO.warning(this.getClass().getName(), "refresh", "cannot select structure view in the mersomery view to refresh it");
		}

	}

	/**
	 * This methods removes a tab containing the structure to remove specified in
	 * parameter
	 * 
	 * @param structure to remove
	 */
	public synchronized void removeStructureView(final Structure structure) throws Exception {
		final Child child = (this.indexedTabsMap.get(structure));
		if (child == null)
			throw new Exception("structure not found in viewer");
		//	ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

		//		public void run() {

		remove(child);
		indexedTabsMap.remove(structure);

		Structure tmp;

		for (int i = 1; i < getTabCount(); i++) {
			tmp = ((Child) getComponentAt(i)).getStructureView()
					.getStructure();
			setTitleAt(i, MesomeryView.PSI + tmp.getName());
		}	



		//	}});



	}

	/**
	 * This function returns the current structure displayed
	 * 
	 * @return structure
	 */

	public synchronized void setCurrentStructure(final Structure structure) {
		//ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

		//public void run() {
		Child component  = indexedTabsMap.get(structure);			
		setSelectedComponent(component);

		//	}} );


	}

	public void setDefaultsPrefs() {
		this.isDisplayPolynomes = false;
		this.isDisplayCharges = false;
		this.isDisplayHxHxy = false;
		this.isDisplayNum = false;
		this.zoom = DEFAULT_ZOOM;
		this.xMargin = 0.0;
		this.yMargin = 0.0;
		this.angle = 0.0;
	}

	public void setMesomery(Mesomery mesomery) {
		this.mesomery.removeListener(this);
		this.mesomery = mesomery;
		this.mesomery.addListener(this);
		this.rebuild();
	}

	public void setZoom(double zoom) {
		this.zoom = zoom;
	}

	public void stateChanged(ChangeEvent e) {
		MesomeryView mesomeryView = (MesomeryView) e.getSource();

		Structure currentStructure;
		try {
			currentStructure = mesomeryView.getCurrentStructure();
			EnableActionManager.getInstance().updateActionsToEnable(this.frameApp);

			this.frameApp.toolBarMesomery.updateInfoStructure(currentStructure);

		} catch (Exception e1) {
			SwingIO.warning(this.getClass().getName(), "stateChanged", e1
					.getMessage(), e1);
			return;
			/*
			 * there is an Exception when the user delete all structures
			 * (delocalized structure) because JTabbedPane class calls
			 * stateChanged before the new delocalized struct exists that's why
			 * mesomeryView.getCurrentStructure() points nowhere
			 * 
			 * and that's why the instruction if
			 * (mesomeryView.countDisplayedStructure > 0) returns false in that
			 * case
			 * 
			 */
		}
	}

	public void structureAdded(MesomeryEvent e) {


	}

	public void switchPolynomes() {
		this.isDisplayPolynomes = !this.isDisplayPolynomes;
	}

	public void switchCharges() {
		this.isDisplayCharges = !this.isDisplayCharges;
	}

	public void switchHxHxy() {
		this.isDisplayHxHxy = !this.isDisplayHxHxy;
	}

	public void switchNum() {
		this.isDisplayNum = !this.isDisplayNum;
	}



	//@Override
	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		rebuild();

	}

	//@Override
	public void mesomeryListWeightCalculated(MesomeryEvent e) {

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

		synchronized(this){

			Structure structure = (Structure) e.getNewValue();

			if (e.getSource().getSelectedStructures().contains(structure)) {
				try {
					addStructureView(structure);
				} catch (Exception e1) {
					SwingIO
					.error(getName(), "structureAdded", e1.getMessage(), e1);
					SwingIO.reportError(frameApp);
				}

				if (structure instanceof StructureLocalized) {
					if (((StructureLocalized) structure).getMethodCreation() == StructureLocalized.CreationMethod.CREATED_MANUALLY)
						setCurrentStructure(structure);
				} else
					setCurrentStructure(structure);
			}

		}




	}



	//@Override
	public void mesomeryStructureRemoved(MesomeryEvent e) {

		try {
			removeStructureView((Structure) e.getOldValue());
		} catch (Exception e1) {
			SwingIO.log(this.getClass().getName(), "structureRemoved",
					"Just info : Structure not found in mesomery viewer");
		}


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
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {

	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		// TODO Auto-generated method stub

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
		StructureView structureView = this.getCurrentStructureView();//getStructureView((Structure) e.getSource().getMoleculeContainer());
		structureView.repaint();

	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		structureView.repaint();

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
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		System.out.println("mesomeryview   structureLocalizedCoupleRemoved -> repaint");
		structureView.repaint();

	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		System.out.println("mesomeryview   structureLocalizedCoupleChanged -> repaint");
		structureView.repaint();

	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		structureView.repaint();

	}

	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		structureView.repaint();

	}

	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		StructureView structureView = this.getCurrentStructureView(); //getStructureView((Structure) e.getSource().getMoleculeContainer());
		structureView.repaint();

	}

	//@Override
	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @return
	 */
	public double getXMargin() {
		return this.xMargin;
	}

	/**
	 * 
	 * @return
	 */
	public double getYMargin() {
		return this.yMargin;
	}
	/**
	 * 
	 * @param xMargin
	 */
	public void setXMargin (double xMargin) {
		this.xMargin = xMargin;
	}


	/**
	 * 
	 * @param yMargin
	 */
	public void setYMargin (double yMargin) {
		this.yMargin = yMargin;
	}

	/**
	 * 
	 * @return
	 */
	public double getAngle() {
		return angle;
	}

	/**
	 * 
	 * @param angle
	 */
	public void setAngle(double angle) {
		this.angle = angle;
	}

	

	
	
	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		// TODO Auto-generated method stub

	}

	public void setMargins(double xMargin, double yMargin) {
		this.xMargin= xMargin;
		this.yMargin = yMargin;

	}

	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		// TODO Auto-generated method stub

	}


} // Gallery
