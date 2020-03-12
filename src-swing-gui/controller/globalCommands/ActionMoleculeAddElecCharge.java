package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.Structure;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user remove an eletron one the entire molecule (charge +)
 * @author Nicolas
 * 
 */

@SuppressWarnings("serial")
public class ActionMoleculeAddElecCharge extends ActionCommand implements
		ICommand, IGlobalCommands {
	FrameApp app;

	public ActionMoleculeAddElecCharge(FrameApp app) {
		super(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE);
		updateLanguage();
		this.app = app;
	}
	
	public void updateLanguage() {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang("-",
				bundle.getString("kaddcharge1"),
				bundle.getString("kaddcharge2"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "add an electron on the structure (charge -)");

		Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		this.app.statusBar.setTxtState(bundle
				.getString("kaddcharge2"));

		Structure structure = mesomery.getDelocalizedStructure();

		int nbMaxElecPossible = structure.countHuckelAtoms() * 2;
		if ((structure.getSumOfPiElec() + 1) <= nbMaxElecPossible) {
			try {
				this.app.getStateManager().initNewState();
				structure.addNbElecCharge(1);
				this.app.getStateManager().saveState();

			} catch (Exception e1) {
				SwingIO.error(this.getClass().getName(), "actionPerformed", e1
						.getMessage(), e1);
				e1.printStackTrace();
				SwingIO.reportError(app);


			}
		}
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
