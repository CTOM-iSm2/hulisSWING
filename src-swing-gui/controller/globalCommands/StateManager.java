package controller.globalCommands;

import java.io.StringWriter;
import java.util.Hashtable;

import javax.swing.JOptionPane;
import javax.swing.undo.StateEdit;
import javax.swing.undo.StateEditable;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;

import org.ctom.hulis.files.gaussian.WriteGaussianException;
import org.ctom.hulis.files.xyz.WritexyzException;
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
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import util.MyJOptionPane;
import util.gaussian.IOReadGaussian;
import util.gaussian.IOWriteGaussian;
import util.io.SwingIO;
import util.xyz.PrintWriterAdapter;
import views.FrameApp;

/***
 * This class describes the state to save in the UndoRedoManager
 * 
 * @author ngoudard
 *
 */
public class StateManager implements StateEditable, IMesomeryListener {
	public static final int OBJECT_STRATEGY = 0; //  on sauvegarde la mesomerie sous forme d'objet 
	public static final int GAUSSIAN_STRATEGY = 1; // ou on sauvegarde la mesomerie sous forme de fichiers gaussian   attention on perd le numero de sequence des atomes
	public static final int SAVE_STRATEGY = OBJECT_STRATEGY;

	public static final boolean DISABLE_UNDO_REDO = false;


	public static final int OUT_OF_MEMORY_NB_STRUCTURES = 400; // si  OBJECT_STRATEGY   on considere qu'a partir de ce nombre de structure la memoire JVM est saturee
	public static final int OUT_OF_MEMORY_MAX_STORE_STATE = 2; // si OBJECT_STRATEGY  nombre d'etat maxi a sauvegarder quand on arrive au nombre de structure max

	public static final int MAX_STORE_STATE = 40; // nombre d'etats sauvegardes maximum quand 1 structure,  ce nombre decroit proportionnellement au nombre de structure stockees
	UndoableEditSupport undoableEditSupport;
	UndoManager undoManager;
	FrameApp app;
	StateEdit stateEdit;

	/**
	 * 
	 * @param app
	 */
	public StateManager(FrameApp app) {
		this.app = app;
		this.undoableEditSupport = new UndoableEditSupport();
		this.undoManager = new UndoManager();
		this.undoManager.setLimit(MAX_STORE_STATE);
		this.undoableEditSupport.addUndoableEditListener(this.undoManager);
	}

	/**
	 * 
	 */
	public void initNewState() {
		if (DISABLE_UNDO_REDO)
			return;
		this.stateEdit = new StateEdit(this);
	}

	/**
	 * 
	 */
	public void saveState() {
		if (DISABLE_UNDO_REDO)
			return;
		this.stateEdit.end();
		this.undoableEditSupport.postEdit(this.stateEdit);
	}

	/**
	 * 
	 */
	public void undo() {
		this.undoManager.undo();
	}

	/**
	 * 
	 */
	public void redo() {
		this.undoManager.redo();
	}

	/**
	 * 
	 */
	public void storeState(Hashtable<Object, Object> state) {
		if (SAVE_STRATEGY == OBJECT_STRATEGY) {
			Mesomery mesomeryClone = (Mesomery) this.app.getCurrentMesomery().clone();
			if (mesomeryClone != null) {
				state.put("mesomery", mesomeryClone);
			}
			
		}
		else if (SAVE_STRATEGY == GAUSSIAN_STRATEGY) {
			IOWriteGaussian writer;
			try {
				writer = new IOWriteGaussian(this.app.getCurrentMesomery());
				writer.setWriteInitialOrientation(true);
				writer.setWriteConnectivity(true); // necessaire car si les molecules ne sont pas optimisees certaines laisons ne se feront pas au undo (leture gaussian)
				StringWriter stringWriter = new StringWriter();
				PrintWriterAdapter printWriter = new PrintWriterAdapter (stringWriter);
				writer.writeAll(printWriter);
				printWriter.flush();
				printWriter.close();
				state.put("mesomeryString", stringWriter.toString());
			} catch (WriteGaussianException e) {
				SwingIO.error(this.getClass().getName(), "storeState", e.getMessage());
				e.printStackTrace();
			} catch (WritexyzException e) {
				SwingIO.error(this.getClass().getName(), "storeState", e.getMessage());
				e.printStackTrace();
			}
		}

		if (this.app.getCurrentMesomeryView().getCurrentStructure() == null) 
			state.put("currentStructureName", StructureDelocalized.NAME_DELOCALIZED_STRUCTURE);
		else
			state.put("currentStructureName", this.app.getCurrentMesomeryView().getCurrentStructure().getName());
		state.put("selectedCommand", this.app.getCurrentEnabledCommand());

	}

	/**
	 * 
	 */
	public void restoreState(Hashtable<?, ?> state) {
		try {
			String currentStructureName = "";
			if (state.get("currentStructureName") != null)
				currentStructureName = (String) state.get("currentStructureName");
			else if (this.app.getCurrentMesomeryView().getCurrentStructure() != null)
				currentStructureName = this.app.getCurrentMesomeryView().getCurrentStructure().getName();
			else
				currentStructureName = StructureDelocalized.NAME_DELOCALIZED_STRUCTURE;

			String selectedCommand = "";
			if (state.get("selectedCommand") != null)
				selectedCommand = (String) state.get("selectedCommand");
			else
				selectedCommand = this.app.getCurrentEnabledCommand();

			// maj reference mesomery dans l'applet
			final Mesomery m ;
			if (SAVE_STRATEGY == OBJECT_STRATEGY) {				
				if (state.get("mesomery") != null)
					m = (Mesomery) ((Mesomery) state.get("mesomery")).clone();
				else 
					m = this.app.getCurrentMesomery();		
			}
			else if (SAVE_STRATEGY == GAUSSIAN_STRATEGY) {
				m = this.app.getCurrentMesomery();
				if (state.get("mesomeryString") != null){
					m.deleteAllStructures();
					IOReadGaussian readGaussian = new IOReadGaussian((String)state.get("mesomeryString"));
					readGaussian.readAll(m);
				}


			}




			// si on restaure les fichier gaussian ou les resultats ne sont pas clones, quand on annule ou refait, on doit relancer les calculs
			if ((SAVE_STRATEGY == GAUSSIAN_STRATEGY) || (!m.isCloneResultsCacheWhenCloneMesomery() && m.isEnabledFlyCalculate())) {
				for (Structure s : m.getAllStructures()) {
					if (!s.isCloneResultsCacheWhenCloneStructure())
						s.calculate();
				}
				Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
				stop.join();

				new Thread (new Runnable() {

					public void run() {
						try {
							m.calculate();
						} catch (IncorrectNumberPiElectronsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MesomeryNoStructureLocalizedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CoupleException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						catch (Exception e) {
							e.printStackTrace();
							m.stopConsumingProcess();
							m.forceAutoGenerating(false);
							m.forceCalculatingWeights(false);
							SwingIO.error(this.getClass().getName(), "restoreState", "mesomery compute error when restore state", e);
							MyJOptionPane.showMessageDialog(app, "Unexpected compute error during undo/redo operation", "Error", JOptionPane.ERROR_MESSAGE);

						}		
					}}).start();


			}
			
			this.app.setMesomery(m, currentStructureName, selectedCommand);
			
		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "restoreState", e
					.getMessage(), e);
			//SwingIO.reportError(app);
		}






	}

	/**
	 * Limite le stockage d'operations annulables en fonction du nombre de structures pour eviter un out of memory
	 * @param mesomery
	 */
	public void updateStorageLimit(Mesomery mesomery) {
		// on ne change pas la limite de stockage si on stocke des chaines gaussian
		if (SAVE_STRATEGY == GAUSSIAN_STRATEGY)
			return;

		// on considere le nombre de d'etas sauvegardes comme une fonction lineraire : car le nombre detas sauv est proportionnel au nb de structures
		// source http://fr.wikipedia.org/wiki/Fonction_affine
		// coeff directeur a = f(x2)-f(x1) / (x2 -x1)
		// origine b  = (x2 f(x1) - x1 f(x2)) / (x2-x1)
		double coeffdirecteur = (double) ((OUT_OF_MEMORY_MAX_STORE_STATE - MAX_STORE_STATE) /  (OUT_OF_MEMORY_NB_STRUCTURES - 0.0));
		double origine =  ( ( (OUT_OF_MEMORY_NB_STRUCTURES* MAX_STORE_STATE )   -( 0 * OUT_OF_MEMORY_MAX_STORE_STATE) )/  (OUT_OF_MEMORY_NB_STRUCTURES - 0) );

		int limit = (int) ((mesomery.countAllSructs()  * coeffdirecteur) + origine); // y = a x + b

		this.undoManager.setLimit(limit);

	}

	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub

	}

	public void moleculeDeleted(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeAtomAdded(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeAtomRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeAtomReplaced(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeBondAdded(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeBondRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	public void moleculeChargeChanged(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	public void moleculeBlocDelocalizedAdded(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	public void moleculeBlocDelocalizedRemoved(MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

	}

	public void huckelBondHxyChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub

	}

	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub

	}

	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	public void monoExcitationTriggered(MonoExcitationEvent e) {
		// TODO Auto-generated method stub

	}

	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	public void mesomeryListWeightCalculated(MesomeryEvent e) {
		// TODO Auto-generated method stub

	}

	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		// TODO Auto-generated method stub

	}

	public void mesomeryStructureAdded(MesomeryEvent e) {
		updateStorageLimit(e.getSource());

	}

	public void mesomeryStructureRemoved(MesomeryEvent e) {
		updateStorageLimit(e.getSource());

	}

	public void mesomeryMethodAdded(MesomeryEvent e) {
		// TODO Auto-generated method stub

	}

	public void mesomeryMethodRemoved(MesomeryEvent e) {
		// TODO Auto-generated method stub

	}

	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		// TODO Auto-generated method stub

	}

	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		// TODO Auto-generated method stub

	}

	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		// TODO Auto-generated method stub

	}



}
