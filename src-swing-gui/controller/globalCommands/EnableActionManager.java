package controller.globalCommands;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import access.Rights;

/**
 * This class enables/disables options menu according to user selections 
 * 
 * @author ngoudard
 *
 */
public class EnableActionManager  {
	
	static EnableActionManager instance = null;
	
	
	public static EnableActionManager getInstance() {
		if (instance == null)
			instance = new EnableActionManager();
		return instance;
	}
	
	private EnableActionManager() {
	}
	public synchronized void updateActionsToEnable(FrameApp app) {
		Structure currentStructure = app.getCurrentMesomeryView().getCurrentStructure();
		Mesomery mesomery = app.getCurrentMesomeryView().getMesomery();

		if (mesomery == null)
			return;
		else if (mesomery.countAllSructs() == 0)
			return;
		
		 if (currentStructure ==null)
			 return;

		 
		// cas general : on active / desactive suivant le type de structure selectionne
		if ((mesomery.countLocalizedStructs() == 0)	&& (mesomery.getDelocalizedStructure().countAtoms() == 0))
			this.delocalizedStructureEmptySelected(app);
		else if ((mesomery.countLocalizedStructs() == 0) && (mesomery.getDelocalizedStructure().countAtoms() != 0))
			this.delocalizedStructureAloneSelected(app);
				
		else if (currentStructure instanceof StructureDelocalized) 
			this.delocalizedStructureSelected(app);
			
		 else if ((currentStructure instanceof StructureLocalized)) 
			this.localizedStructureSelected(app);
	
		
		app.toolBarMesomery.btBloc.setVisible(app.isExpertMode() && Rights.ALLOW_BLOCKS);
		app.toolBarMesomery.btEndBloc.setVisible(false);

		
		String command = app.getCurrentEnabledCommand(); 
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		// mettre un message dans la statusbar si aucune action n'est selectionnee ou bouton disable
	   // sinon mettre le message correspondant au bouton selectionne
		if (command.equals("")) 
			app.statusBar.setTxtState(bundle.getString("kstatusnoaction"));
		else 
			app.statusBar.setTxtState(ActionCommandRegistry.getInstance().get(command).getLongDescription());

		// si une autogeneration est en cours ou non , desactiver/activer certaines options
		handleAutogenerateContext(app);

		// contexte renumerotation
		// si on est en mode renumerotation, desactiver certaines options, jusqua ce que
		// la numerotation sois terminee, ensuite reactiver les options et passer en mode ajout
			
		if (command.equals(IGlobalCommands.RENUM)  && currentStructure instanceof StructureLocalized){
			// cette situation est normalement impossible a moins que le programme buggue
			SwingIO.error(this.getClass().getName(), "updateContext", "Cannot renumber a localized structure");
			SwingIO.reportError(app);
	
		}
		else if (command.equals(IGlobalCommands.RENUM) && currentStructure.areCorrectAtomsSeqNum()) 
			this.endRenumCase(app);
	
		else if (command.equals(IGlobalCommands.RENUM) && !currentStructure.areCorrectAtomsSeqNum()) 
			this.renumCase(app);
		
		else if (command.equals(IGlobalCommands.BLOC))
			this.blocCase(app);
		
	
		// coche les case des menu concernant les methodes HLP, HLCI selon les methodes implementees par la mesomerie 
		// : cette option est utile avec les undo / redo
		updateMenuMethodFromMesomery(app, app.getCurrentMesomery());
		

	}
	

	protected void handleAutogenerateContext(FrameApp app){
		enableActionsAfterAutogen(app, !app.getCurrentMesomery().isAutoGenerating());

		
	}
	

	public void enableActionsAfterAutogen(FrameApp app, boolean b) {
		//System.out.println("handleAutogenerateContext");
		boolean condition = (app.getCurrentMesomery().countLocalizedStructs()>0);
		app.toolBarHuckel.btErase.setEnabled(b && (app.getCurrentMesomeryView().getCurrentStructure().countAtoms()>0));
		app.toolBarMesomery.btAutoGenStruct.setVisible(b );
		app.toolBarMesomery.btStopAutoGenStruct.setVisible(!b);
		app.toolBarMesomery.btChangeElectrons.setEnabled(b && condition);
		app.toolBarMesomery.btDuplicate.setEnabled(b && (app.getCurrentMesomeryView().getCurrentStructure().countAtoms()>0));
		app.toolBarMesomery.btCouple.setEnabled(b && condition);
		app.toolBarMesomery.btErase1.setEnabled(b && condition  && (app.getCurrentMesomeryView().getCurrentStructure() instanceof StructureLocalized));
		app.toolBarMesomery.btEraseMesomery.setEnabled(b && condition);
			app.toolBarMesomery.slider.setEnabled(b);
		app.menuBar.menuOpen.setEnabled(b
				&& !app.isExecutedOnInternet() );
		app.menuBar.menuQuit.setEnabled(true /*!app.getRights().isExecutedFromNetwork()*/);
		app.menuBar.menuSave.setEnabled(b
				&& !app.isExecutedOnInternet() );// !(rights.connectFrom()=="network")
		app.menuBar.menuPaste.setEnabled(b
				&& !app.isExecutedOnInternet() ) ;
		app.menuBar.menuTutorial0
		.setEnabled(b && !app.isExecutedOnInternet());
		app.menuBar.menuGuide.setEnabled(false);
		app.toolBarHuckel.btQuit.setEnabled(true/*!app.getRights().isExecutedFromNetwork()*/);

		ActionCommandRegistry.getInstance().get(
				IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(b && (app.getCurrentMesomery() != null) && (app.getCurrentMesomery().getDelocalizedStructure() != null)
						&& (app.getCurrentMesomery().getDelocalizedStructure().countAtoms()>0) );	
	}
	
	protected void updateMenuMethodFromMesomery(FrameApp app, Mesomery mesomery){
		//SwingIO.PrintIfln("updateMenuMethodFromMesomery");

		// cocher les methodes (HLP, HLCI) dans le menu si elles sont implementees dans la mesomerie
		for (IMethod method : FrameApp.getImplementedMethods())
			app.menuBar.getItemMethod(method.getName()).setSelected(
					mesomery.getMethod(method.getName()) != null);

	} // 
	
	protected void delocalizedStructureEmptySelected(FrameApp app){
		//SwingIO.PrintIfln("delocalizedStructureEmptySelected");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();

		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS).setEnabled(true);	
		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.QUIT).setEnabled(true);

	
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE_ELECTRON).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.COUPLE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.BLOC).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.END_BLOC).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS_MESOMERY).setEnabled(true);
		app.toolBarMesomery.slider.setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.ERASE_1).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ERASE_LEWIS).setEnabled(false);
	} // 
	

	protected void delocalizedStructureAloneSelected(FrameApp app){
		//SwingIO.PrintIfln("delocalizedStructureAloneSelected");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();

		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS).setEnabled(true);	
		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.QUIT).setEnabled(true);

	
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CHANGE_ELECTRON).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.COUPLE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.BLOC).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.END_BLOC).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS_MESOMERY).setEnabled(true);
		app.toolBarMesomery.slider.setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.ERASE_1).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ERASE_LEWIS).setEnabled(false);
	}

	
	protected void delocalizedStructureSelected(FrameApp app){
		//SwingIO.PrintIfln("delocalizedStructureSelected");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();

		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS).setEnabled(true);	
		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.QUIT).setEnabled(true);

	
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CHANGE_ELECTRON).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.COUPLE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.BLOC).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.END_BLOC).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS_MESOMERY).setEnabled(true);
		app.toolBarMesomery.slider.setEnabled(true);

		actionCommandRegistry.get(IGlobalCommands.ERASE_1).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ERASE_LEWIS).setEnabled(true);
	}

	
	protected void localizedStructureSelected(FrameApp app){
		//SwingIO.PrintIfln("localizedStructureSelected");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();

		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS).setEnabled(true);	
		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.QUIT).setEnabled(true);

	
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.CHANGE_ELECTRON).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.COUPLE).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.BLOC).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.END_BLOC).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.SHOW_RESULTS_MESOMERY).setEnabled(true);
		app.toolBarMesomery.slider.setEnabled(true);

		actionCommandRegistry.get(IGlobalCommands.ERASE_1).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.ERASE_LEWIS).setEnabled(true);
	}

	
	
	

	protected void renumCase(FrameApp app){
		//SwingIO.PrintIfln("renumContext");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();
		
		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE)	.setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(false);
		
		
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(false);

		
		
		
	} // 

	
	protected void endRenumCase (FrameApp app) {
		//SwingIO.PrintIfln("renumContext");
		 delocalizedStructureAloneSelected(app);
		app.toolBarHuckel.btAdd.doClick();
	}
	

	protected void blocCase(FrameApp app){
		//SwingIO.PrintIfln("blocContext");
		ActionCommandRegistry actionCommandRegistry = ActionCommandRegistry.getInstance();
	
		actionCommandRegistry.get(IGlobalCommands.ADD).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.REMOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.MOVE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ROTATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.OPTIMIZE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CENTER).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.RENUM).setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(false);
		
	
		actionCommandRegistry.get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.DUPLICATE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.CHANGE_ELECTRON).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.COUPLE).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.BLOC).setEnabled(true);
		actionCommandRegistry.get(IGlobalCommands.END_BLOC).setEnabled(true);
		app.toolBarMesomery.btBloc.setVisible(false);
		app.toolBarMesomery.btEndBloc.setVisible(true);
		app.toolBarMesomery.slider.setEnabled(false);

		actionCommandRegistry.get(IGlobalCommands.ERASE_1).setEnabled(false);
		actionCommandRegistry.get(IGlobalCommands.ERASE_LEWIS).setEnabled(false);
		
		
		
	} // 
	

}
