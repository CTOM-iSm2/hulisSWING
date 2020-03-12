package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.Structure;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
* This command is called when the user adds an eletron one the entire molecule (charge -)
* 
* @author Nicolas
* 
*/

@SuppressWarnings("serial")
public class ActionMoleculeRemoveElecCharge extends ActionCommand implements
		IGlobalCommands {
	FrameApp app;

	public ActionMoleculeRemoveElecCharge(FrameApp app) {
		super(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE);
		this.app = app;
		updateLanguage();

	}
	
	public void updateLanguage() {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang("+",
				bundle.getString("kdeletecharge1"),
				bundle.getString("kdeletecharge2"));
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.execute();

	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "remove an electron on the structure (charge +)");

		Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		this.app.statusBar.setTxtState(getLongDescription());

		Structure structure = mesomery.getDelocalizedStructure();

		if ((structure.getSumOfPiElec() - 1) >= 0) {
			try {
				this.app.getStateManager().initNewState();
				structure.addNbElecCharge(-1);
				this.app.getStateManager().saveState();
			} catch (Exception e1) {
				SwingIO.error(this.getClass().getName(), "actionPerformed", e1
						.getMessage(), e1);
				e1.printStackTrace();
				SwingIO.reportError(app);

			}
		}
		
	}

}
