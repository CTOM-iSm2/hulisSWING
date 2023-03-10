package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.text.MessageFormat;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import access.Rights;
import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.MesomeryView;
import views.StructureView;

/**
 * This class describes the action when the user terminates a bloc
 * @author goudard
 *
 */
public class ActionEndBloc extends ActionCommand implements  ICommand,
IGlobalCommands  {
	FrameApp app;
	private JComponent parent;

	public ActionEndBloc(FrameApp app) {
		super(IGlobalCommands.END_BLOC);
		updateLanguage();
		this.app = app;
		this.parent=app.getCurrentMesomeryView();

	}
	
	public ActionEndBloc(FrameApp app, JComponent parent) {
		super(IGlobalCommands.END_BLOC);
		updateLanguage();
		this.app = app;
		this.parent=parent;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		this.execute();

	}
	
	/**
	 * Return true if the selected bloc is in the current structure, else show a message dialog and return false
	 * @param app
	 * @return
	 */
	public static boolean checkCurrentBlocInCurrentStructure(FrameApp app) {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		String msgboxTitle = bundle.getString("kTitleMsgboxEndBloc");
		String msgboxMess;
		StructureView sv =  app.getCurrentMesomeryView().getFirstStructureWithSelectedBloc();
		Structure s ;
		// si une autre structure que la structure courante a un bloc selectionne -> message
		if ( (sv != null) && (StructureView.getSelectedBlocDelocalized() != null)  && ((s =sv.getStructure()) != app.getCurrentMesomeryView().getCurrentStructure())) {
			Object args[] = {MesomeryView.PSI + s.getName() };
			msgboxMess = MessageFormat.format(bundle.getString("kMessAnotherBloc"), args);
			JOptionPane.showMessageDialog(app, msgboxMess, msgboxTitle, JOptionPane.INFORMATION_MESSAGE);
			
			return false;
		}
	
		return true;
	}


	
	
	
	//@Override
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "terminate bloc");
		String msgboxTitle = bundle.getString("kTitleMsgboxEndBloc");
		String msgboxMess;

		StructureView currentStructureView = app.getCurrentMesomeryView().getCurrentStructureView();
		StructureLocalized currentStructure = (StructureLocalized) currentStructureView.getStructure();
	
		
		// si la structure courante n'a pas de bloc selectionne, c'est que l'utilisateur a clique sur endbloc juste apres
		// avoir clique sur le bouton bloc : on envoie un message disant qu'aucune modification n'a ete faite
		BlocDelocalized currentSelectedBloc = StructureView.getSelectedBlocDelocalized();
		this.app.getStateManager().initNewState();

		if (currentSelectedBloc == null ) {
			// verifier s'il n'y a pas un bloc courant dans une autre structure : si oui l'utilisateur 
			// doit le terminer
			if (!checkCurrentBlocInCurrentStructure(app))
				return;
			else {
				msgboxMess = bundle.getString("kMessNoModificationBloc");
				JOptionPane.showMessageDialog(parent, msgboxMess, msgboxTitle, JOptionPane.INFORMATION_MESSAGE);
			}
	
		} 
		else if (currentSelectedBloc.countHuckelObjects() ==0 ) {
	
			// sinon si le bloc quon veut modifier ne contient plus d'electrons, supprimer ce bloc vide
			currentSelectedBloc.clear();		
			currentStructure.removeBlocDelocalized(currentSelectedBloc);
			currentSelectedBloc = null;
			
			msgboxMess = bundle.getString("kMessAutoDeleteBloc");
			
			JOptionPane.showMessageDialog(parent, msgboxMess, msgboxTitle, JOptionPane.INFORMATION_MESSAGE);
			
	
		} 
		else {
		// sinon, choisir le nombre d'electrons
			String sNbElec = null;
			int nbElec =0;
			 msgboxMess = bundle.getString("kMessMsgboxEndBloc");
			 
			// saisie du nombre d'electron dans le bloc jusqu'a ce que ce soit effectivement un nombre
			while((sNbElec == null)) {
				try {
					sNbElec = (String) MyJOptionPane.showInputDialog(parent, msgboxMess, msgboxTitle, JOptionPane.QUESTION_MESSAGE, null, null, currentSelectedBloc.getNbElectron());
					nbElec = Integer.valueOf(sNbElec);
				} catch(NumberFormatException e) {
					sNbElec = null;
				}
			} // while
			
	
			// affecter le nombre delectrons au bloc
			currentSelectedBloc.setNbElectron(nbElec);
		}// if

		//deselectionne le bloc (fin de modif)
		// et rafraichir les bouton de menus
		unselectCurrentBloc();

		//TODO : On ne devrait pas calcl
	/*	currentStructure.calculate();
		try {
			currentStructure.getMesomeryParent().calculate();
		} catch (IncorrectNumberPiElectronsException | IMethodException | MesomeryNoStructureLocalizedException
				| CoupleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		this.app.getStateManager().saveState();
	
	} // execute

	public void unselectCurrentBloc(){
		StructureView.setSelectedBlocDelocalized(null);
	
		app.toolBarMesomery.btEndBloc.setVisible(false);
		app.toolBarMesomery.btBloc.setVisible(true && Rights.ALLOW_BLOCKS);

		ActionCommandRegistry.getInstance().enableAll(); // active tout pour pouvoir cliquer sur le bouton dans l'instruction qui suit
		
		String command ="";
		if (app.getCurrentMesomeryView().getCurrentStructure() instanceof StructureLocalized)
			command = IGlobalCommands.CHANGE_ELECTRON;
		else
			command = IGlobalCommands.ADD;
		app.clickButtonCommand(command);// sortir du mode bloc.
		EnableActionManager.getInstance().updateActionsToEnable(app);
		app.statusBar.setTxtState(bundle.getString("kendbloc2"));

		

	}
	
	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(rb2.getString("kendbloc"), bundle.getString("kendbloc1"),
				bundle.getString("kendbloc2"));
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
