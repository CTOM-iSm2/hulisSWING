package org.ctom.hulis.huckel;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ctom.hulis.files.gaussian.ReadGaussian;
import org.ctom.hulis.files.gaussian.ReadGaussianException;
import org.ctom.hulis.huckel.comparators.OverlapComparator;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.AutoGenerateConditionException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.DressStructureLocalizedException;
import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryCopyException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureNotExistsException;
import org.ctom.hulis.huckel.exception.MesomeryLocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryMethodExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.MesomeryRemoveMethodException;
import org.ctom.hulis.huckel.exception.SelectionException;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.methods.IMethodHasOptimizableMatrix;
import org.ctom.hulis.huckel.selectors.IStructuresSelector;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.hulis.util.configS.Conf;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.time.CTOMProfiler;



/**
 * This class describes a mesomery
 * 
 * @author Nicolas Goudard, Yannick Carissan
 * @version 1.6
 * @since JDK 1.5
 * 
 */
public class Mesomery extends Input implements Cloneable, IStructureLocalizedListener, IStructureDelocalizedListener, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9098113981147780386L;



	protected final static int nMaxIter = 1000;   // maximal interations to generate structures

	public enum POLICY_STRUCTURE_CHANGED{
		CHANGE_TRESHOLD_TO_STRUCTURE_VALUE_IF_LOWER, UNSELECT_STRUCTURE_IF_VALUE_IS_LOWER_TAN_TRESHOLD}

	private static POLICY_STRUCTURE_CHANGED policyStructureChanged = POLICY_STRUCTURE_CHANGED.CHANGE_TRESHOLD_TO_STRUCTURE_VALUE_IF_LOWER;



	private boolean isCloneResultsCacheWhenCloneMesomery;


	public boolean isEnabledFlyCalculate; // flag to enable / disable auto-calculation after a change
	private ArrayList<IMesomeryListener> listeners; // object list that listen mesomery changes
	private HashMap<String, IMethod> lstMethods; // methods of weights calculations 
	private ArrayList<Structure> lstSelectedStructs; // selected structures : mesomery structures under the level defined by the structures selector
	private ArrayList<Structure> lstStructs; // this array contains all the mesomery structures
	Conf myconfig = null;
	private int precision = 2;  //  precision to select structures under the structure selector value
	// begin yannick


	private volatile boolean isStopConsumingProcess;  /// booleens : a true permet de sortir des proedures qui consomment du temps comme le calcul et l'autogeneration de a mesomerie


	private volatile boolean isAutoGenerating;


	private volatile boolean isCalculatingWeights;

	private IStructuresSelector structuresSelector;

	/**
	 * Constructor : create a mesomery
	 * 
	 */
	public Mesomery() {
		this.isCloneResultsCacheWhenCloneMesomery=true;
		this.isEnabledFlyCalculate = true;
		this.lstSelectedStructs = new ArrayList<Structure>();
		this.lstStructs = new ArrayList<Structure>();
		this.listeners = new ArrayList<IMesomeryListener>();
		this.lstMethods = new HashMap<String, IMethod>();
		this.structuresSelector = null;
	}

	/*private void readObject(java.io.ObjectInputStream in)
		    throws IOException, ClassNotFoundException {
	    	in.defaultReadObject();
	    	this.listeners = new ArrayList<IMesomeryListener>();
		
	}*/
	
	
	/**
	 * Constructor by copy
	 * @param mesomery
	 */
	public Mesomery(Mesomery mesomery) {
		this.isCloneResultsCacheWhenCloneMesomery = mesomery.isCloneResultsCacheWhenCloneMesomery();
		this.isEnabledFlyCalculate = mesomery.isEnabledFlyCalculate();
		this.lstSelectedStructs = new ArrayList<Structure>();
		this.lstStructs = new ArrayList<Structure>();
		this.listeners = new ArrayList<IMesomeryListener>();
		if (this.myconfig != null)
			this.myconfig.erase();
		this.lstMethods = new HashMap<String, IMethod>();
		for (Map.Entry<String, IMethod> entry : mesomery.lstMethods.entrySet()) {
			IMethod methodClone = null;
			if (mesomery.isCloneResultsCacheWhenCloneMesomery())
				methodClone = (IMethod) entry.getValue().clone();
			else
				try {
					methodClone = (entry.getValue()).getClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			methodClone.setMesomery(this);
			this.lstMethods.put(entry.getKey(), methodClone);
		}
		try {
			deleteAllStructures();
		} catch (CoupleException | IMethodException | MesomeryNoStructureLocalizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mesomery.getDelocalizedStructure() != null)
			try {
				do_addDelocalizedStruct((StructureDelocalized) mesomery.getDelocalizedStructure().clone() );
			} catch (MesomeryDelocalizedStructureExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		for (StructureLocalized s : mesomery.getLocalizedStructures()) {
			do_addLocalizedStruct((StructureLocalized)s.clone());
		}
	
		try {
			this.do_afterAddLocalizedStructures();
		} catch (CoupleException | IMethodException | MesomeryNoStructureLocalizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (mesomery.structuresSelector != null) {
			this.structuresSelector = (IStructuresSelector) mesomery.structuresSelector.clone();
			this.structuresSelector.setMesomery(this);
		}
		else
			HuckelIO.warning(this.getClass().getName(), "copy", "Mesomery copied without structure selector !");
		this.precision = mesomery.precision;

		this.updateSelectedStructs();
		
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {	
		return new Mesomery(this);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Structure> getStructuresHaveDemiElectrons() {
		List<Structure> lstStructureHaveDemiElectrons = new ArrayList<Structure>();
		for (Structure s : this.getAllStructures()) {
			if (s.hasDemiElectrons())
				lstStructureHaveDemiElectrons.add(s);
				
		}
		return lstStructureHaveDemiElectrons;
	}

	/**
	 * 
	 * @param structureSelector
	 */
	public void setStructureSelector(IStructuresSelector structuresSelector) {
		IStructuresSelector old = structuresSelector;
		this.structuresSelector = structuresSelector;
		this.structuresSelector.setMesomery(this);
		this.fireStructuresSelectorChanged(this, old, structuresSelector);
	}


	/**
	 * 
	 * @return
	 */
	public IStructuresSelector getStructureSelector() {
		return this.structuresSelector;
	}

	/**
	 * Constructs a mesomery by reading a gaussian file
	 * @param readGaussian
	 * @throws ReadGaussianException 
	 */
	public Mesomery(ReadGaussian readGaussian) throws ReadGaussianException   {
		this();
		readGaussian.readAll(this);	
	}

	/**
	 * This methods adds a delocalized structure in the mesomery
	 * @param delocalized structure
	 * @throws MesomeryDelocalizedStructureExistsException 
	 */
	public void addDelocalizedStruct(
			StructureDelocalized delocalizedStructure) throws MesomeryDelocalizedStructureExistsException {


		do_addDelocalizedStruct(delocalizedStructure);
		fireStructureAdded(this, null, delocalizedStructure);

		if (this.structuresSelector != null){
			Object old = this.structuresSelector.getSelectionTreshold();
			this.structuresSelector.setSelectionTreshold( this.structuresSelector.getCurrentValue(delocalizedStructure));
			this.updateSelectedStructs();
			if (!this.structuresSelector.getSelectionTreshold().equals(old))
				this.fireStructuresSelectionTresholdChanged(this, old, this.structuresSelector.getSelectionTreshold());

		}
	}

	/**
	 * get a structure by its index. The index of the delocalized structure is 1
	 * @param i
	 * @return
	 */
	public Structure getStructureByIndex(int index) {
		for (Structure s : this.getAllStructures()) {
			if (s.getIndex() == index)
				return s;
		}
		return null;

	}

	private void do_addDelocalizedStruct(
			StructureDelocalized delocalizedStructure) throws MesomeryDelocalizedStructureExistsException {

		if (getDelocalizedStructure() != null)
			throw new MesomeryDelocalizedStructureExistsException("the delocalized structure already exists");

		delocalizedStructure.setCloneResultsCacheWhenCloneStructure(this.isCloneResultsCacheWhenCloneMesomery());
		delocalizedStructure.setMesomeryParent(this);
		delocalizedStructure.addListener(this);
		delocalizedStructure.setIndex(1);
		this.lstStructs.add(delocalizedStructure);
		this.lstSelectedStructs.add(delocalizedStructure);
		if(delocalizedStructure.isEnabledFlyCalculate()) // BIDOUILLE ajoutee car si on ouvre un XYZ l'index de la structure == 0 et le calcul overlap ne se fait pas
			delocalizedStructure.calculate();

	}

	/**
	 * This method adds a localized structure in the mesomery
	 * @param localized structure
	 * @throws CoupleException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 */
	public void addLocalizedStruct(StructureLocalized structure) throws CoupleException, IMethodException, MesomeryNoStructureLocalizedException {	
		String routine="addLocalizedStruct";
		CTOMProfiler.getInstance().debut(routine);
	
		do_addLocalizedStruct(structure);
		do_afterAddLocalizedStructures();
		
		fireStructureAdded(this, null, structure);
	
		
		CTOMProfiler.getInstance().fin(routine);
	}

	/**
	 * This method adds a localized structure in the mesomery
	 * @param localized structure
	 */
	protected void do_addLocalizedStruct(StructureLocalized structure)  {


		// double emin = ((StructureLocalized)
		// Collections.max(this.getLocalizedStructures(), new
		// EnergyComparator())).getTotalEnergy().getBeta();
		structure.setCloneResultsCacheWhenCloneStructure(this.isCloneResultsCacheWhenCloneMesomery());
		structure.addListener(this);
		structure.setMesomeryParent(this);
		structure.setName(Integer.toString(countLocalizedStructs() + 1));
		structure.setIndex(this.getMaxStructureIndex() + 1);
		this.lstStructs.add(structure);

	}
	
	/**
	 * 
	 * @throws CoupleException
	 * @throws IncorrectNumberPiElectronsException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 */
	protected void do_afterAddLocalizedStructures() throws CoupleException, IMethodException, MesomeryNoStructureLocalizedException{
		updateSelectedStructs();
		checkMesomery();
		if (this.isEnabledFlyCalculate
				&& countSelectedLocalizedStructs() > 0) {

			Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
			for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
				IMethod method = entry.getValue();
				
				if (method instanceof IMethodHasOptimizableMatrix)
					((IMethodHasOptimizableMatrix) method).extendOptimizableMatrix();
			}

			try {
				do_calculate();
			} catch (IncorrectNumberPiElectronsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			fireListWeightCalculated(this, null, null);
		}

	}

	/**
	 * This method adds a mesomery listener
	 * @param listener
	 */
	public void addListener(IMesomeryListener listener) {
		this.listeners.add(listener);
	}

	/**
	 * This method adds a calculation methods of weights 
	 * @param calculation method
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws CoupleException 
	 * @throws IncorrectNumberPiElectronsException 
	 */
	public synchronized void addMethod(IMethod method) throws MesomeryMethodExistsException, IncorrectNumberPiElectronsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException  {
		if (this.lstMethods.get(method.getName()) != null)
			throw new MesomeryMethodExistsException ("mesomery already contains this method");
		method.setMesomery(this);
		this.lstMethods.put(method.getName(), method);
		if (this.isEnabledFlyCalculate() && this.countLocalizedStructs()>0) {
			if (method instanceof IMethodHasOptimizableMatrix)
				((IMethodHasOptimizableMatrix) method).computeAllOptimizableMatrix();

			this.calculate(method.getName());
		}

		this.fireMethodAdded(this, null, method);
	}

	public boolean containsMethod(IMethod method) {
		return (this.lstMethods.get(method.getName()) !=null);
	}


	/**
	 * This method refresh the selected structures, calculate weights and invoke listeners
	 * @param struct
	 * @param idxToRemove : index of the removed structure
	 * @param notifyDeleted 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 */
	private void afterRemove(Structure struct, int idxToRemove, boolean notifyDeleted) throws IMethodException, MesomeryNoStructureLocalizedException {
		updateSelectedStructs();
		try {
			if (this.isEnabledFlyCalculate
					&& countSelectedLocalizedStructs() > 0) {

				Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
				for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
					IMethod method = entry.getValue();
					if (method instanceof IMethodHasOptimizableMatrix)
						((IMethodHasOptimizableMatrix) method).removeInOptimizableMatrix(idxToRemove);
				}

				do_calculate();
				fireListWeightCalculated(this, null, null);


			}
		} catch (IncorrectNumberPiElectronsException e1) {
			HuckelIO.warning(this.getClass().getName(), "remove", e1
					.getMessage(), e1);


		} finally {


			// HuckelIO.PrintIf ("structures localisees"); //*******
			for (int i = 0; i < countLocalizedStructs(); i++) {
				getLocalizedStruct(i).setName(Integer.toString(i + 1));
				// HuckelIO.PrintIf ("structures localisees");
			} // rename other structures


			for (Structure s : this.getAllStructures()) {
				if (s.getIndex() > struct.getIndex())
					s.setIndex(s.getIndex() - 1);
			} // for HuckelAtom

			if (notifyDeleted)
				fireStructureRemoved(this, struct, null);
			struct.removeListener(this);
		}
	}


	/**
	 * @return : get the maximal index of the structures in the mesomery
	 */
	protected int getMaxStructureIndex() {
		int maxIndex = 0;
		int currentIndex = 0;

		for (Structure a : this.getAllStructures()) {
			currentIndex = a.getIndex();

			if (currentIndex > maxIndex) {
				maxIndex = currentIndex;
			}
		}

		return maxIndex;
	}

	/**
	 * This function generates Lewis structures from a delocalized structures
	 * @return the count of generated structures
	 * @throws MesomeryException if delocalized structure is null, incorrect atoms sequence number in delocalized structure,
	 *  config error
	 * @throws AutoGenerateConditionException 
	 * @throws DressStructureLocalizedException 
	 * @throws CoupleException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws IncorrectNumberPiElectronsException 
	 */
	public int autoGenLewisStructures() throws AutoGenerateConditionException, DressStructureLocalizedException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, IncorrectNumberPiElectronsException {
		String routine="autoGenLewisStructures";
		CTOMProfiler.getInstance().debut(routine);

		this.isAutoGenerating = true;
		this.setStopConsumingProcess(false);
		eraseConf();

		StructureDelocalized skeleton = getDelocalizedStructure(); // we get
		// the
		// original
		// molecule

		if (skeleton == null) {
			this.isAutoGenerating = false;
			throw new AutoGenerateConditionException("Invalid Skeleton ! value == null ");
		}

		if (skeleton.countAtoms() == 0) {
			this.isAutoGenerating = false;
			return -1;
		}

		if (!(skeleton.areCorrectAtomsSeqNum())) {
			this.isAutoGenerating = false;
			throw new AutoGenerateConditionException("An atom has an incorrect sequence number");
		}

		boolean sav = isEnabledFlyCalculate();

		stopFlyCalculate();

		if (this.myconfig != null)
			this.myconfig.stopFlyCalculate(this);

		ArrayList<Conf> lstConfiguration = null;
		lstConfiguration = new ArrayList<Conf>(); // this array contains list
		// of automatic generated
		// structures

		Conf.setMesomery(this);
		long t0=System.currentTimeMillis();
		this.myconfig = new Conf(skeleton);
		//		int a = this.myconfig.getCharges().length;
		for (int imask = 1; imask <= Conf.nMasque(); imask++) {
			if (this.isStopConsumingProccess()) break;

			if (!this.myconfig.applyMasque(imask))
				continue;
			int nMaxRadical = 2;
			if (!Preferences.isCOUPLE_AUTOGEN()) nMaxRadical=1;
			for (int nRadical = 0; nRadical <= nMaxRadical; nRadical++) {
				if (this.isStopConsumingProccess()) break;
				this.myconfig.generate(nRadical);
			}
		}
		long t1=System.currentTimeMillis();
		System.out.println("time:"+(t1-t0)+" ms");

		for (Conf conf : Conf.getList()) {
			if (this.isStopConsumingProccess()) break;

			//synchronized (this.lstStructs) {
			StructureLocalized struct = skeleton.cloneToLocalized();
			boolean savCalculateFly = struct.isEnabledFlyCalculate();
			struct.stopFlyCalculate();
			try {
				conf.dressStructureLocalized(struct);
			} catch (Exception e) {
				this.isAutoGenerating = false;
				e.printStackTrace();
				throw new DressStructureLocalizedException(e.getMessage());
			}

			struct.setMethodCreation(StructureLocalized.CreationMethod.AUTO_GENERATED);

			if (savCalculateFly) {
				struct.startFlyCalculate();
				struct.calculate();
			}

		
			do_addLocalizedStruct(struct);


			

		} //for
		
		do_afterAddLocalizedStructures();
		
		// reindexe les structures et caclule une fois le overlap
		for (int i = 0; i<this.lstStructs.size(); i++) {
			this.lstStructs.get(i).setIndex(i+1);
			Structure struct = this.getAllStructures().get(i);
			struct.calculateOverlapHLP();
		}
		
		this.sortStructures (new OverlapComparator());
			
		// positionne le seuil de structure au minimum
		if (this.structuresSelector  != null){
			Object oldValue = this.structuresSelector.getSelectionTreshold() ; 
			this.structuresSelector.setTresholdToLocalizedStructureMinimalValue();
			if (!this.structuresSelector.getSelectionTreshold().equals(oldValue)){
				updateSelectedStructs();
				this.fireStructuresSelectionTresholdChanged(this, oldValue, this.structuresSelector.getSelectionTreshold());
			}					
		}
		
		
		long t2=System.currentTimeMillis();
		System.out.println("time dressing:"+(t2-t1)+" ms");


		/*System.out.println(">>>>**** structures generees");
		for (Structure s : this.getAllStructures()) {
			System.out.println("PSI" + s.getName() + " : " + s.getIndex() + " - " + s.getIndexSelection() + " - " + s.getOverlapHLP());

		}
		System.out.println("****<<<");*/

		int size = 0;
		try {
			lstConfiguration.addAll(Conf.getList());
			HuckelIO.PrintIf("\nNumber of generated structures : "
					+ lstConfiguration.size() + "\n");
			size = lstConfiguration.size();
		} catch (Exception e) {
			HuckelIO.warning(this.getClass().getName(),
					"autoGenLewisStructures", e.getMessage(), e);
		} finally {
			//synchronized (this.lstStructs) {
			if (sav)
				startFlyCalculate();
			checkMesomery();

			if (this.isEnabledFlyCalculate
					&& countSelectedLocalizedStructs() > 0 && this.listIncorrectStructPiElecNumber().size()==0) {
				Map<String, IMethod> sortedMap = getSortedMethodsByPriority();

				for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
					IMethod method = entry.getValue();
					if (method instanceof IMethodHasOptimizableMatrix)
						((IMethodHasOptimizableMatrix) method).computeAllOptimizableMatrix();
				}
				try {
					do_calculate();

				} catch (IncorrectNumberPiElectronsException e) {
					this.isAutoGenerating = false;
					throw e;
				}
				fireListWeightCalculated(this, null, null);


			}
			//}
			this.isAutoGenerating = false;
		}

		this.isAutoGenerating = false;

		CTOMProfiler.getInstance().fin(routine);

		return size;
	} // autogenerate
	
	
	/**
	 * @param Comparator
	 */
	public void sortStructures(Comparator comparator) {
		Collections.sort(this.lstStructs, comparator);			
		Collections.sort(this.lstSelectedStructs, comparator);			
	}

	/**
	 * 
	 */
	public void stopConsumingProcess() {
		this.isStopConsumingProcess = true;
	}

	/**
	 * 
	 * @param isStopConsumingProcess
	 */
	public void setStopConsumingProcess(boolean isStopConsumingProcess) {
		this.isStopConsumingProcess = isStopConsumingProcess;
	}

	/**
	 * This methods puts a flag ERROR_INCORRECT_NB_PI_ELEC on localized structures that not have
	 * a correct number of Pi electron or ERROR_TOO_MUCH_SINGLE_ELECTRONS if the structure has got more than 2 single electrons
	 * The number of pi electron (sum of electrons calculated on the orbitals)  is correct on a localized structures if it has got the same
	 * count of Pi electrons as the delocalized molecule (sum of default sum of Pi electrons on each + molecule charge).
	 * 
	 * @return the list of incorrect structures
	 * 
	 */
	private List<StructureLocalized> calcListIncorrectStructPiElecNumber() {
		List<StructureLocalized> l = new ArrayList<StructureLocalized>();
		for (StructureLocalized struct :  this.getLocalizedStructures()) {
			if (!struct.hasCorrectNbPiElec()) {
				l.add(struct);
				struct.setError(StructureLocalized.Error.ERROR_INCORRECT_NB_PI_ELEC);
			}
			else if (struct.hasTooManyElectrons()) {
				l.add(struct);
				struct.setError(StructureLocalized.Error.ERROR_TOO_MUCH_SINGLE_ELECTRONS);
			}


		}
		return l;
	}


	/**
	 * get the list of the selected localized structures that have an incorrect number of pi electrons
	 * @return
	 */
	public List<StructureLocalized> getListIncorrectStructPiElectNumber() {
		List<StructureLocalized> l = new ArrayList<StructureLocalized>();
		int correctNbPiElec = getDelocalizedStructure().getSumOfPiElec();
		for (StructureLocalized struct :  this.getSelectedLocalizedStructures()) {
			if (struct.getSumOfPiElec() != correctNbPiElec) {
				l.add(struct);
			}
			else if (struct.countSingleElectronsOnAtoms()>2) {
				l.add(struct);
			}


		}
		return l;
	}

	/**
	 * Thi method mark structure doublons, structures that have incorrect count of pi electrons, and 
	 * invokes the weights calculation 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws IncorrectNumberPiElectronsException 
	 * @throws CoupleException 
	 */
	public void calculate() throws IncorrectNumberPiElectronsException, IMethodException, MesomeryNoStructureLocalizedException, CoupleException  {
		checkMesomery();
		if (countSelectedLocalizedStructs() > 0) {
			Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
			for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
				IMethod method = entry.getValue();
				if (method instanceof IMethodHasOptimizableMatrix)
					((IMethodHasOptimizableMatrix) method).computeAllOptimizableMatrix();
			}

			do_calculate();
			fireListWeightCalculated(this, null, null);

		}

	}

	/**
	 * This method marks doublons, marks incorrect number pi electrons, invokes the weights calculating method for one specified method
	 * @param method name
	 * @throws CoupleException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws IncorrectNumberPiElectronsException 
	 * @throws HulisException
	 */
	public void calculate(String methodName) throws CoupleException, IncorrectNumberPiElectronsException, IMethodException, MesomeryNoStructureLocalizedException{
		checkMesomery();
		if (countSelectedLocalizedStructs() > 0) {
			checkConditionsBeforeCalculate ();
			this.setStopConsumingProcess(false);

			do_calculateWeights(getMethod(methodName));

			fireListWeightCalculated(this, null, null);
		}
	}

	/**
	 * This method calculates weights with the specified method in parameter
	 * @param method
	 */
	private void do_calculateWeights(IMethod method)  {
		try {
			if (method instanceof IMethodHasOptimizableMatrix)
				((IMethodHasOptimizableMatrix) method).computeAllOptimizableMatrix();
			method.calculate();
		} catch (IMethodException e) {
			this.fireExceptionOccured(this, null, null, e);
		}

	}


	/**
	 * get sorted methods by priority
	 * @return
	 */
	public Map<String, IMethod> getSortedMethodsByPriority() {
		// sort methods by priority
		final Map <String, IMethod> lstMethods = this.lstMethods;
		SortedMap<String, IMethod> sortedMap = new TreeMap (
				new Comparator() {	
					public int compare(Object o1, Object o2) {
						IMethod m1 =(IMethod) lstMethods.get((String) o1);
						IMethod m2 = (IMethod)  lstMethods.get((String) o2);
						return (((Integer) m1.getPriority()).compareTo((m2.getPriority())));
					}
				}
				);
		sortedMap.putAll(this.lstMethods);
		return sortedMap;
	}



	/**
	 * This method calculates weights with all method calculations. 
	 * @throws IncorrectNumberPiElectronsException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 */
	private void do_calculate() throws IncorrectNumberPiElectronsException, IMethodException, MesomeryNoStructureLocalizedException {
		checkConditionsBeforeCalculate();

		this.isCalculatingWeights = true;
		this.setStopConsumingProcess(false);
		this.fireStartComputeWeights(this, null, null);

		Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
		for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
			if (this.isStopConsumingProccess())
				break;
			do_calculateWeights(entry.getValue());
		}
		this.isCalculatingWeights = false;

	}

	/**
	 * 
	 * @return
	 */
	public boolean isCalculatingWeights() {
		return this.isCalculatingWeights;
	}

	/**
	 * force the value of the boolean "isCalculatingWeights" : use only in case of problems such as an exception in a thread
	 */
	public void forceCalculatingWeights(boolean isCalculatingWeights){
		this.isCalculatingWeights=  isCalculatingWeights;
	}

	/**
	 * force the value of the boolean "isAutoGenerating" : use only in case of problems such as an exception in a thread
	 */
	public void forceAutoGenerating(boolean isAutoGenerating){
		this.isAutoGenerating=  isAutoGenerating;
	}

	/**
	 * This method checks if the conditions are correct to start the weights calculation, else return exceptions
	 * @throws IncorrectNumberPiElectronsException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException
	 */
	private void checkConditionsBeforeCalculate() throws IMethodException, IncorrectNumberPiElectronsException, MesomeryNoStructureLocalizedException {

		int nbSelLocalizedStructs = countSelectedLocalizedStructs();

		// test if there are lewis structure. else exception
		if (nbSelLocalizedStructs == 0)
			throw new MesomeryNoStructureLocalizedException("coeff B param matrix must be intialized");

		// tests if all the structures have the same number of Pi electrons
		if (listIncorrectStructPiElecNumber() != null)
			if (listIncorrectStructPiElecNumber().size() > 0)
				throw new IncorrectNumberPiElectronsException(
						"incorrect number of pi electrons in one structure or more");

		boolean ok = true;
		Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
		for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
			IMethod method = entry.getValue();
			if (method instanceof IMethodHasOptimizableMatrix)  {
				ok = ((IMethodHasOptimizableMatrix) method).checkOptimizableMatrix();
				if (!ok)
					throw method.getError();
			}
		}

	}

	/** 
	 * @return the number of structures (deleclalized + localized)
	 */

	public int countAllSructs() {
		if ( (this.lstStructs == null) ||this.lstStructs.isEmpty() )
			return 0;
		return this.lstStructs.size();
	}

	/**
	 * 
	 * @return the number of localized structures
	 */

	public int countLocalizedStructs() {
		if ( (this.lstStructs == null) ||this.lstStructs.isEmpty() )
			return 0;

		return this.lstStructs.size() - 1;
	}

	/**
	 * 
	 * @return the number of localized structures under the level defined by the structure selector
	 */
	public int countSelectedLocalizedStructs() {
		if ( (this.lstSelectedStructs == null) ||this.lstSelectedStructs.isEmpty() )
			return 0;

		return this.lstSelectedStructs.size() - 1;
	}

	/**
	 * 
	 * @return the number of localized structures under the level defined by the structure selector + delocalized structures
	 */
	public int countSelectedStructs() {
		if ( (this.lstSelectedStructs == null) ||this.lstSelectedStructs.isEmpty() )
			return 0;

		return this.lstSelectedStructs.size();
	}

	/**
	 * This function creates  a localized structure
	 * @return
	 * @throws MesomeryCreateLocalizedStructureException
	 * @throws CoupleException 
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 */
	public synchronized StructureLocalized createLocalizedStructure()
			throws MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException {
		if (getDelocalizedStructure() == null) {
			throw new MesomeryDelocalizedStructureNotExistsException(
					"cannot create localized structure because delocalized structure does not exist in mesomery");
		}
		StructureLocalized s;
		s = getDelocalizedStructure().cloneToLocalized();

		addLocalizedStruct(s);

		return s;
	}

	/**
	 * This method deletes all structures in the mesomery
	 * @param notifyEachStructureDeleted
	 * @throws CoupleException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 */
	public void deleteAllStructures(boolean notifyEachStructureDeleted) throws CoupleException, IMethodException, MesomeryNoStructureLocalizedException {
		Structure structureDelocalized = getDelocalizedStructure();
		if (structureDelocalized != null) {
			boolean sav = this.isEnabledFlyCalculate();
			this.stopFlyCalculate();
			this.deleteLocalizedStructures(notifyEachStructureDeleted);
			do_deleteStructure(structureDelocalized, notifyEachStructureDeleted);
			if (sav)
				startFlyCalculate();

		}

	}

	/**
	 * This method deletes all structures in the mesomery
	 * @throws CoupleException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 */
	public void deleteAllStructures() throws CoupleException, IMethodException, MesomeryNoStructureLocalizedException {
		deleteAllStructures(true);

	}

	protected void eraseConf() {
		if (this.myconfig != null)
			this.myconfig.erase();
	}

	public boolean exists(Structure structure) {
		if (structure == null)
			return false;

		for (Structure s : this.lstStructs) {
			if (s == structure)
				return true;
		}

		return false;

	}

	protected void fireListWeightCalculated(Mesomery source, Object oldValue, Object newValue) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener : this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryListWeightCalculated(event);

		}

	}



	protected void fireStartComputeWeights(Mesomery source, Object oldValue, Object newValue) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener : this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryStartComputeWeights(event);

		}

	}

	protected void fireStructuresSelectionTresholdChanged(Mesomery source, Object oldValue, Object newValue) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener : this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue,
						newValue);
			listener.mesomeryStructuresSelectionTresholdChanged(event);
		}
	}

	/**
	 * @param mesomery
	 * @param oldValue
	 * @param newValue
	 */
	private void fireStructuresSelectorChanged(Mesomery source,
			Object oldValue, Object newValue)  {
		MesomeryEvent event = null;

		for (IMesomeryListener listener : this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue,
						newValue);
			listener.mesomeryStructuresSelectorChanged(event);
		}
	}

	protected void fireMethodRemoved(Mesomery source, Object oldValue, Object newValue) {

		MesomeryEvent event = null;

		for (IMesomeryListener listener :  this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryMethodRemoved(event);

		}
	}


	protected void fireMethodAdded(Mesomery source, Object oldValue, Object newValue) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener : this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryMethodAdded(event);

		}
	}


	protected void fireStructureAdded(Mesomery source, Object oldValue, Object newValue ) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener :  this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryStructureAdded(event);
		}
	}


	protected void fireStructureRemoved(Mesomery source, Object oldValue, Object newValue) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener :  this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryStructureRemoved(event);
		}

	}
	protected void fireExceptionOccured(Mesomery source, Object oldValue, Object newValue, Exception e) {
		MesomeryEvent event = null;

		for (IMesomeryListener listener :  this.getListeners()) {
			if (event == null)
				event = new MesomeryEvent(source, oldValue, newValue);
			listener.mesomeryExceptionOccured(event, e);
		}


	}



	/**
	 * This function returns all the localized structures contained in the
	 * mesomery
	 * 
	 * @return an iterator on a list of Structure objects
	 */
	public synchronized List<Structure> getAllStructures() {

		return (List<Structure>) this.lstStructs.clone();
	}


	/**
	 * This function returns the delocalized structure of the mesomery
	 * 
	 * @return Structure
	 */
	public StructureDelocalized getDelocalizedStructure() {

		if (this.lstStructs.isEmpty())
			return null;

		return (StructureDelocalized) this.lstStructs.get(0);
	}



	public ArrayList<IMesomeryListener> getListeners() {
		return (ArrayList<IMesomeryListener>) this.listeners.clone();
	}

	/**
	 * This function returns the structure at the specified index
	 * 
	 * @param idx :
	 *            index
	 * @return
	 */
	public StructureLocalized getLocalizedStruct(int idx) {
		return (StructureLocalized) this.lstStructs.get(idx + 1);
	}

	public synchronized List<StructureLocalized> getLocalizedStructures() {
		ArrayList<Structure> lst = (ArrayList<Structure>) this.lstStructs.clone();
		if (lst.size() == 0)
			return new ArrayList<StructureLocalized>();
		List <? extends Structure> lstStructureToCast =lst.subList(1, lst.size());
		return  (List <StructureLocalized>)lstStructureToCast;
	}

	public IMethod getMethod(String methodName) {
		return this.lstMethods.get(methodName);
	}

	public IMethod[] getMethods() {
		return this.lstMethods.values().toArray(
				new IMethod[this.lstMethods.size()]);
	}

	public StructureLocalized getSelectedLocalizedStruct(int idx) {
		if (idx >=  (this.lstSelectedStructs.size() -1) )
			return null;
		return (StructureLocalized) this.lstSelectedStructs.get(idx + 1);
	}

	public synchronized List<StructureLocalized> getSelectedLocalizedStructures() {
		if (this.lstSelectedStructs.size() == 0)
			return new ArrayList<StructureLocalized>();

		List <? extends Structure> lstStructureToCast = this.lstSelectedStructs.subList(1, this.lstSelectedStructs.size());
		return  (List <StructureLocalized>)lstStructureToCast;


	}

	public synchronized List<Structure> getSelectedStructures() {
		if (this.lstSelectedStructs.size() == 0)
			return new ArrayList<Structure>();

		return (List<Structure>) this.lstSelectedStructs.clone();
	}

	public synchronized void addSelectedStructure(Structure s) {
		this.lstSelectedStructs.add(s);
	}

	public Structure getStructureByName(String name) {

		for (Structure s : this.lstStructs) {
			if (s.getName().equals(name))
				return s;
		}

		return null;
	}

	public boolean isEnabledFlyCalculate() {
		return this.isEnabledFlyCalculate;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized List<StructureLocalized> listIncorrectStructPiElecNumber() {
		List<StructureLocalized> lst= new ArrayList<StructureLocalized>();
		for (StructureLocalized s : this.getLocalizedStructures())
			if ( (s.getError() != null) && (s.getError().equals(StructureLocalized.Error.ERROR_TOO_MUCH_SINGLE_ELECTRONS) || s.getError().equals(StructureLocalized.Error.ERROR_INCORRECT_NB_PI_ELEC)))
				lst.add(s);
			
		return lst;
	}

	/**
	 * mark doublons 
	 * @return true if there are doublons
	 */
	private List<StructureLocalized> markDoublons() {
		List<StructureLocalized> lstDoublons=new ArrayList<StructureLocalized>();
		
		for  (StructureLocalized s :getLocalizedStructures()) {
			for  (StructureLocalized s2 : getLocalizedStructures()) {
				if ( (s != s2)  &&  ((s2.getError()  == null) || (s2.getError() == StructureLocalized.Error.NOT_ERROR)) && (s.geometryEquals(s2)) ) {
					s2.setError(StructureLocalized.Error.ERROR_ALREADY_EXIST);
					s2.setIdenticalStruct(s);
					lstDoublons.add(s);
				}

			}
		}
		return lstDoublons;
	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		this.fireMoleculeAtomAdded(e);	
		handleStructureChanged((Structure) e.getSource());	
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		this.fireMoleculeAtomRemoved(e);	
		handleStructureChanged((Structure) e.getSource());	
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		this.fireMoleculeAtomReplaced(e);	
		handleStructureChanged((Structure) e.getSource());	
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		this.fireMoleculeBondAdded(e);
		handleStructureChanged((Structure) e.getSource());	
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		this.fireMoleculeBondRemoved(e);	
		handleStructureChanged((Structure) e.getSource());	
	}


	//@Override
	public void moleculeChargeChanged(MoleculeEvent e) {
		this.fireMoleculeChargeChanged(e);	
		handleStructureChanged((Structure) e.getSource());	

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent e) {
		this.fireMoleculeListHuckelAtomSeqNumAutoSetted(e);	
		handleStructureChanged((Structure) e.getSource());	

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent e) {
		this.fireMoleculeListHuckelAtomSeqNumReset(e);	
		handleStructureChanged((Structure) e.getSource());	

	}


	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		this.fireHuckelAtomHxChanged(e);	
		handleStructureChanged((Structure) e.getSource().getMoleculeContainer());	

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		this.fireHuckelAtomRadRChanged(e);	
		handleStructureChanged((Structure) e.getSource().getMoleculeContainer());	
	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		this.fireHuckelAtomSeqNumChanged(e);	
		handleStructureChanged((Structure) e.getSource().getMoleculeContainer());	

	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		this.fireHuckelBondBondTypeChanged(e);	
		handleStructureChanged((Structure) e.getSource().getMoleculeContainer());	

	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		this.fireHuckelBondHxyChanged(e);	
		handleStructureChanged((Structure) e.getSource().getMoleculeContainer());	
	}




	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent e) {
		this.fireStructureLocalizedCoupleRemoved(e);	
		handleStructureChanged(e.getSource());	

	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent e) {

		this.fireStructureLocalizedWeightChanged(e);	

	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent e) {
		this.fireStructureLocalizedCoupleChanged(e);	
		handleStructureChanged(e.getSource());	

	}


	//@Override
	public void structureNameChanged(StructureEvent e) {
		this.fireStructureNameChanged(e);	
	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		this.fireMonoExcitationTriggered(e);	
		handleStructureChanged(e.getSource().getStructure());	


	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent e) {
		this.fireStructureDelocalizedSpinChanged(e);	

	}



	protected void handleStructureChanged (Structure s)  {

		if (this.structuresSelector != null) {			
			if (Mesomery.getPolicyStructureChanged() == POLICY_STRUCTURE_CHANGED.CHANGE_TRESHOLD_TO_STRUCTURE_VALUE_IF_LOWER ) {
				if (this.structuresSelector.isCurrentValueLowerThanTreshold(s)) {
					Object oldValue = this.structuresSelector.getSelectionTreshold();
					this.structuresSelector.setSelectionTreshold(this.structuresSelector.getCurrentValue(s));
					updateSelectedStructs();
					if (!oldValue.equals(this.structuresSelector.getSelectionTreshold()))
						fireStructuresSelectionTresholdChanged(this, oldValue, this.structuresSelector.getSelectionTreshold());							
				}
			}
			else if (Mesomery.getPolicyStructureChanged() == POLICY_STRUCTURE_CHANGED.UNSELECT_STRUCTURE_IF_VALUE_IS_LOWER_TAN_TRESHOLD ) {
				if (this.structuresSelector.isCurrentValueLowerThanTreshold(s) && this.lstSelectedStructs.contains(s)) {
					this.lstSelectedStructs.remove(s);
					updateSelectedStructs();

				}	
				else if (!this.structuresSelector.isCurrentValueLowerThanTreshold(s) && !this.lstSelectedStructs.contains(s)) {
					this.lstSelectedStructs.add(s);
					updateSelectedStructs();

				}	
		
			}
		}


		try {
			checkMesomery();
		} catch (CoupleException e) {
			fireExceptionOccured(this, null, null, e);
			return;
		}

		if (this.isEnabledFlyCalculate && countSelectedLocalizedStructs() > 0) {
			int i = getNumOrderSelectedLocalizedStructure(s);

			Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
			for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
				IMethod method = entry.getValue();
				if (method instanceof IMethodHasOptimizableMatrix)
					((IMethodHasOptimizableMatrix) method).computeOptimizableMatrix(i);
			}

			try {
				do_calculate();
				fireListWeightCalculated(this, null, null);

			}
			catch(IncorrectNumberPiElectronsException e) {
				HuckelIO.log(this.getClass().getName(), "handleStructureChanged", "catch incorrect number of pi electrons");
			} catch (IMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MesomeryNoStructureLocalizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


	}


	/**
	 * @return
	 */
	public static POLICY_STRUCTURE_CHANGED getPolicyStructureChanged() {
		return policyStructureChanged;
	}

	/**
	 * @return
	 */
	public static void setPolicyStructureChanged(POLICY_STRUCTURE_CHANGED policyStructureChanged) {
		Mesomery.policyStructureChanged = policyStructureChanged;
	}

	public void moleculeDeleted(MoleculeEvent e) {
		this.fireMoleculeDeleted(e);
	}

	/**
	 * This method deletes a structure
	 * @param structure
	 * @throws CoupleException 
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 * @throws MesomeryLocalizedStructureExistsException : delete a delocalized structure if a localized structure exists
	 */

	public void deleteStructure(Structure structure) throws CoupleException, IMethodException, MesomeryNoStructureLocalizedException, MesomeryLocalizedStructureExistsException {

		if (structure == getDelocalizedStructure() && this.countLocalizedStructs()>0) 
			throw new MesomeryLocalizedStructureExistsException("The mesomery is not empty");

		int indexToRemove = -1;
		// a placer AVANT lstSelectedStructs.remove
		if (structure instanceof StructureLocalized)
			indexToRemove = this.getNumOrderSelectedLocalizedStructure((StructureLocalized) structure);

		this.lstStructs.remove(structure);
		this.lstSelectedStructs.remove(structure);
		checkMesomery();
		afterRemove(structure, indexToRemove, true);
		structure.delete();
		structure.setMesomeryParent(null);
		structure = null;



		if ((countLocalizedStructs() == 0))  {
			eraseConf();

			if (this.structuresSelector != null) {
				Object oldValue = this.structuresSelector.getSelectionTreshold();
				this.structuresSelector.setTresholdValueWhenNoStructure();
				updateSelectedStructs();
				if (!oldValue.equals(this.structuresSelector.getSelectionTreshold()))
					fireStructuresSelectionTresholdChanged(this, oldValue, this.structuresSelector.getSelectionTreshold());

			}

		}

	}

	/**
	 * This method removes the specified structures specified in the parameter.
	 * deleted
	 * @param  notifyAfterEachStructureDeleted
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws CoupleException 
	 * 
	 */

	public  void deleteLocalizedStructures (boolean notifyAfterEachStructureDeleted) throws IMethodException, MesomeryNoStructureLocalizedException, CoupleException {
		boolean sav = isEnabledFlyCalculate();
		this.stopFlyCalculate();
		StructureLocalized StructToDelete;

		for (int i = countLocalizedStructs() - 1; i >= 0; i--) {
			StructToDelete = getLocalizedStruct(i);
			do_deleteStructure(StructToDelete, notifyAfterEachStructureDeleted);
		}
		this.isEnabledFlyCalculate = sav;
		eraseConf();
		if (this.structuresSelector != null) {
			Object oldValue = this.structuresSelector.getSelectionTreshold();
			this.structuresSelector.setTresholdValueWhenNoStructure();
			updateSelectedStructs();
			if (!oldValue.equals(this.structuresSelector.getSelectionTreshold()))
				fireStructuresSelectionTresholdChanged(this, oldValue, this.structuresSelector.getSelectionTreshold());

		}


	}

	/**
	 * This method removes the specified structures specified in the parameter.
	 * deleted
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 * @throws CoupleException
	 */
	public  void deleteLocalizedStructures() throws IMethodException, MesomeryNoStructureLocalizedException, CoupleException {
		deleteLocalizedStructures(true);
	}



	/**
	 * @param structToDelete
	 * @param notifyDeleted
	 * @throws MesomeryNoStructureLocalizedException 
	 * @throws IMethodException 
	 * @throws CoupleException 
	 */
	private void do_deleteStructure(Structure structToDelete, boolean notifyDeleted) throws IMethodException, MesomeryNoStructureLocalizedException, CoupleException {
		this.lstStructs.remove(structToDelete);
		int indexToRemove = this.getNumOrderSelectedLocalizedStructure(structToDelete);
		afterRemove(structToDelete, indexToRemove, notifyDeleted);
		checkMesomery();
		structToDelete.delete(notifyDeleted);
		structToDelete.setMesomeryParent(null);
		structToDelete = null;

	}

	public void removeListener(IMesomeryListener listener) {
		this.listeners.remove(listener);
	}

	/**
	 * 
	 * @param methodName
	 * @throws MesomeryRemoveMethodException
	 * @throws CoupleException 
	 */
	public void removeMethod(String methodName) throws MesomeryRemoveMethodException, CoupleException{
		IMethod old = this.lstMethods.get(methodName);
		if (old == null)
			throw new MesomeryRemoveMethodException ("mesomery does not contains this method");
		this.lstMethods.remove(methodName);
		this.checkMesomery();
		this.fireMethodRemoved(this, old, null);

	}

	/**
	 * 
	 * @return
	 */
	public Object getSelectionTreshold() {
		if (this.structuresSelector == null){
			HuckelIO.warning(this.getClass().getName(), "getSelectionTreshold", "selector is not set in the the mesomery !!");
			return null;
		}
		return this.structuresSelector.getSelectionTreshold();
	}

	/**
	 * 
	 * @param o
	 * @throws SelectionException
	 * @throws CoupleException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 */
	public void setSelectionTreshold(Object o) throws SelectionException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException {
		// if (beta < 0.0)
		// throw new HulisException ("beta must be positive");
		if (this.structuresSelector == null) {
			HuckelIO.warning(this.getClass().getName(), "setMaxValueForStructureSelection", "selector is not set in the the mesomery !!");
			throw new SelectionException("mesomery structureSelector is null. First, set a selector to the mesomery");
		}

		Object oldValue = this.structuresSelector.getSelectionTreshold();
		this.structuresSelector.setSelectionTreshold(o);
		int oldNbSelectedStructs = this.countSelectedStructs();
		updateSelectedStructs(); 
		if (!oldValue.equals(this.structuresSelector.getSelectionTreshold()) && (this.countSelectedStructs()!=oldNbSelectedStructs )) {
			fireStructuresSelectionTresholdChanged(this, oldValue, this.structuresSelector.getSelectionTreshold());
			try {
				checkMesomery();
				if (this.isEnabledFlyCalculate
						&& countSelectedLocalizedStructs() > 0) {
					Map<String, IMethod> sortedMap = getSortedMethodsByPriority();
					for (Map.Entry<String, IMethod> entry : sortedMap.entrySet()) {
						IMethod method = entry.getValue();
						if (method instanceof IMethodHasOptimizableMatrix)
							((IMethodHasOptimizableMatrix) method).computeAllOptimizableMatrix();
					}

					do_calculate();
					fireListWeightCalculated(this, null, null);


				}

			}  catch (IncorrectNumberPiElectronsException e1) {
				HuckelIO.warning(this.getClass().getName(), "setMaxValueForStructureSelection", e1
						.getMessage(), e1);

			} 
		}
	}





	/**
	 * return the index corresponding to structure in the selected Lewis structures or -1 if not found 
	 * Be careful, this function does not return the same index as s.getIndex()
	 */
	private int getNumOrderSelectedLocalizedStructure(Structure s) {
		int nbStruct =this.countSelectedLocalizedStructs();
		for (int n = 0; n < nbStruct ; n++) {
			if( s == this.getSelectedLocalizedStructures().get(n) ) {
				return n;
			}
		}
		return -1;
	}



	/**
	 * This method checks if the mesomery is correct
	 * If not, it sets flags accordingly
	 * 
	 * @return false if there are errors or doublons
	 * @throws CoupleException 
	 */
	private boolean checkMesomery() throws CoupleException {
		if (this.countLocalizedStructs()==0) 
			return true;

		/*TODO : Attention, je crois que les doublons devraient etre
		 * calcules apres le couplage auto. Ou bien les doublons doivent
		 * bien faire attention au couplage
		 */

		// met un flag : pas d'erreur
		for (StructureLocalized sl : getLocalizedStructures() ) 
			sl.setError(StructureLocalized.Error.NOT_ERROR);


		List<StructureLocalized> errors = calcListIncorrectStructPiElecNumber();

		correctCouplage();
		// effectue controle d'erreurs
		List<StructureLocalized> doublons = markDoublons();
		
		return ((errors.size()>0) || (doublons.size()>0));
	}

	/**
	 * This method sets the couplage if necessary
	 * @throws CoupleException 
	 */
	private void correctCouplage() throws CoupleException {
		int delocalisedSpin=this.getDelocalizedStructure().getTotalSpin();
		Iterator iterator = this.getSelectedLocalizedStructures().iterator();
		for (StructureLocalized struct = null; iterator.hasNext();) {
			struct = (StructureLocalized) iterator.next();
			//on couple si la structure ne presente pas d'erreur
			if (struct.getError()==StructureLocalized.Error.NOT_ERROR) {
				/* on fait quelque chose si le spin total n'est pas bon
				 * et si le couplage n'est pas deja fait
				 */
				if ((struct.getTotalSpin() != delocalisedSpin)&&(!struct.aUnCouplage())) {
					struct.autoCouplage();
					if (struct.isEnabledFlyCalculate())
						struct.calculateOverlapHLP();
					//on reteste : si c'est toujours pas bon, on ne peut rien faire
					if (struct.getTotalSpin() != delocalisedSpin) {
						struct.setError(StructureLocalized.Error.ERROR_CORRECT_SPIN_IMPOSSIBLE);
					}
				}
			}
			//			if (struct.getSumOfPiElec() != correctNbPiElec) {
			//				l.add(struct);
			//				struct.setError(StructureLocalized.ERROR_INCORRECT_NB_PI_ELEC);
			//			}
			//			else if (struct.countSingleElectronsOnAtoms()>2) {
			//				l.add(struct);
			//				struct.setError(StructureLocalized.ERROR_TOO_MUCH_SINGLE_ELECTRONS);
			//			}
			//			else {
			//				if (struct.getError() == StructureLocalized.ERROR_INCORRECT_NB_PI_ELEC
			//						|| struct.getError() ==  StructureLocalized.ERROR_TOO_MUCH_SINGLE_ELECTRONS )
			//					struct.setError(StructureLocalized.NOT_ERROR);
			//			}
		}
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public void startFlyCalculate() {
		this.isEnabledFlyCalculate = true;
	}

	public void stopFlyCalculate() {
		this.isEnabledFlyCalculate = false;
	}

	/**
	 * This method updates the list of selected structures.
	 * The wheights are calculated only for these selected structures.
	 * A structure may be in the mesomery but not selected.
	 * A structure is selected if is value of selection criteria is lower than the threshold defined by the structure selector
	 */
	public void updateSelectedStructs() {

		if (this.structuresSelector != null) 
			this.structuresSelector.selectStructures();
		else
			HuckelIO.warning(this.getClass().getName(), "updateSelectedStructs", "cannot select structures because structuresSelector is null");

		// met a jour les index de selection
		for (int i = 0; i < this.countSelectedStructs(); i++ ) 
			this.getSelectedStructures().get(i).setIndexSelection(i + 1);




	}

	/**
	 * clear the list of the selected structures
	 */
	public void clearListSelectedStructures() {
		for (Structure s : this.getAllStructures())
			s.setIndexSelection(0);
		this.lstSelectedStructs.clear();
	}

	protected void fireHuckelAtomHxChanged(HuckelAtomEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {
			listener.huckelAtomHxChanged(e);
		}
	} //

	protected void fireHuckelAtomRadRChanged(HuckelAtomEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.huckelAtomRadRChanged(e);
		}	
	} //

	protected void fireHuckelAtomSeqNumChanged(HuckelAtomEvent e) {

		for (IMesomeryListener listener :   this.getListeners()) {	
			listener.huckelAtomSeqNumChanged(e);
		}	
	} //

	protected void fireHuckelBondBondTypeChanged(HuckelBondEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.huckelBondBondTypeChanged(e);
		}	
	} //

	protected void fireHuckelBondHxyChanged(HuckelBondEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.huckelBondHxyChanged(e);
		}	
	} //

	protected void fireMoleculeAtomAdded(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeAtomAdded(e);
		}	
	} //


	protected void firemoleculeBlocDelocalizedAdded(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeBlocDelocalizedAdded(e);
		}	
	} //

	protected void firemoleculeBlocDelocalizedRemoved(MoleculeEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.moleculeBlocDelocalizedRemoved(e);
		}	
	} //

	protected void fireMoleculeAtomRemoved(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeAtomRemoved(e);
		}	
	} //

	protected void fireMoleculeAtomReplaced(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeAtomReplaced(e);
		}	
	} //

	protected void fireMoleculeBondAdded(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeBondAdded(e);
		}	
	} //

	protected void fireMoleculeBondRemoved(MoleculeEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.moleculeBondAdded(e);
		}	
	} //



	protected void fireMoleculeChargeChanged(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeChargeChanged(e);
		}	
	} //

	protected void fireMoleculeDeleted(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeDeleted(e);
		}	
	} //

	protected void fireMoleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.moleculeListHuckelAtomSeqNumAutoSetted(e);
		}	
	} //

	protected void fireMoleculeListHuckelAtomSeqNumReset(MoleculeEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.moleculeListHuckelAtomSeqNumReset(e);
		}	
	} //



	protected void fireMonoExcitationTriggered(MonoExcitationEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.monoExcitationTriggered(e);
		}	
	} //

	protected void fireStructureDelocalizedSpinChanged(StructureDelocalizedEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.structureDelocalizedSpinChanged(e);
		}
	} //

	protected void fireStructureLocalizedCoupleRemoved(StructureLocalizedEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.structureLocalizedCoupleRemoved(e);
		}
	} //

	protected void fireStructureLocalizedWeightChanged(StructureLocalizedEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.structureLocalizedWeightChanged(e);
		}
	} //


	protected void fireStructureLocalizedCoupleChanged(StructureLocalizedEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.structureLocalizedCoupleChanged(e);
		}
	} //

	protected void fireStructureNameChanged(StructureEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.structureNameChanged(e);
		}
	} //

	protected void fireBlocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.blocDelocalizedHuckelObjectAdded(e);
		}
	} //

	protected void fireBlocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {

		for (IMesomeryListener listener : this.getListeners()) {	
			listener.blocDelocalizedHuckelObjectRemoved(e);
		}
	} //

	protected void fireBlocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {

		for (IMesomeryListener listener :  this.getListeners()) {	
			listener.blocDelocalizedNbElectronsChanged(e);
		}
	} //

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent e) {
		this.firemoleculeBlocDelocalizedAdded(e);	
		handleStructureChanged((Structure) e.getSource());	


	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent e) {
		this.firemoleculeBlocDelocalizedRemoved(e);	
		handleStructureChanged((Structure) e.getSource());	


	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// on ne calcule que si le nombre d'electrons a change
		this.fireBlocDelocalizedHuckelObjectAdded(e);
		if (e.getSource().getNbElectron() >0)
			handleStructureChanged((Structure) e.getSource().getMoleculeParent());	

	}

	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// on ne calcule que si le nombre d'electrons a change
		this.fireBlocDelocalizedHuckelObjectRemoved(e);
		if (e.getSource().getNbElectron() >0)
			handleStructureChanged((Structure) e.getSource().getMoleculeParent());	

	}

	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		this.fireBlocDelocalizedNbElectronsChanged(e);
		handleStructureChanged((Structure) e.getSource().getMoleculeParent());	

	}

	/**
	 * @return
	 */
	public boolean isAutoGenerating() {
		return this.isAutoGenerating;
	}

	/**
	 * @return
	 */
	public boolean isStopConsumingProccess() {
		return this.isStopConsumingProcess;
	}

	/**
	 * @return true if clone the results are cloned when the mesomery is cloned
	 */
	public boolean isCloneResultsCacheWhenCloneMesomery() {
		return isCloneResultsCacheWhenCloneMesomery;
	}

	/**
	 * if the parameter is set to true, the results in cache are cloned when the mesomery is cloned
	 * @param isCloneResultsCacheWhenCloneMesomery the parameter to set 
	 */
	public void setCloneResultsCacheWhenCloneMesomery(
			boolean isCloneResultsCacheWhenCloneMesomery) {
		this.isCloneResultsCacheWhenCloneMesomery = isCloneResultsCacheWhenCloneMesomery;
		for (Structure s : this.getAllStructures())
			s.setCloneResultsCacheWhenCloneStructure(isCloneResultsCacheWhenCloneMesomery);
	}

	/**
	 * @return
	 */
	public int getPrecision() {
		return this.precision;
	}




}
