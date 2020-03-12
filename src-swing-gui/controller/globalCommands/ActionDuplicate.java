package controller.globalCommands;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureNotExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.SelectionException;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user add manually a structure. 
 * This new structure is added to mesomery seleted structures with updating mesomery energymax parameter
 * This new structure becomes the current structure
 * @author Nicolas
 * 
 */
@SuppressWarnings("serial")
public class ActionDuplicate extends ActionCommand implements  ICommand,
		IGlobalCommands {

	FrameApp app;

	public ActionDuplicate(FrameApp app) {
		super(IGlobalCommands.DUPLICATE);
	
		updateLanguage();
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}
	
	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(rb2.getString("kcreate1"), bundle.getString("kcreate11"),
				bundle.getString("kcreate12"));
	}

	public  void execute() {
		Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		if (!mesomery.getDelocalizedStructure().areCorrectAtomsSeqNum()) {
			MyJOptionPane.showMessageDialog(this.app, bundle
					.getString("krerrorseqnum"), bundle.getString("kerrtitle"),
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (mesomery.getDelocalizedStructure().countAtoms() != 0) {
			try {
				this.app.getStateManager().initNewState();
				StructureLocalized s = mesomery.createLocalizedStructure();
				SwingIO.log(this.getClass().getName(), "execute", "create a localized structure : structure" + s.getName());
				if (mesomery.getStructureSelector().isCurrentValueLowerThanTreshold(s)) {
					mesomery.setSelectionTreshold(mesomery.getStructureSelector().getCurrentValue(s));
					this.app.getCurrentMesomeryView().setCurrentStructure(s);	
					this.app.toolBarMesomery.adjustSlider(-(Double)mesomery.getSelectionTreshold()); 
					
				}
				EnableActionManager.getInstance().updateActionsToEnable(app);
				this.app.statusBar.setTxtState(bundle.getString("kcreate12"));
				this.app.toolBarMesomery.btChangeElectrons.doClick();

				this.app.getStateManager().saveState();
			} catch (MesomeryDelocalizedStructureNotExistsException e) {
				SwingIO.error(this.getClass().getName(), "execute", e
						.getMessage(), e);
				SwingIO.reportError(app);
			} catch (CoupleException e) {
				SwingIO.error(this.getClass().getName(), "execute", e
						.getMessage(), e);
				SwingIO.reportError(app);
			} catch (IMethodException e) {
				SwingIO.error(this.getClass().getName(), "execute", e
						.getMessage(), e);
				SwingIO.reportError(app);
			} catch (MesomeryNoStructureLocalizedException e) {
				SwingIO.error(this.getClass().getName(), "execute", e
						.getMessage(), e);
				SwingIO.reportError(app);
			} catch (SelectionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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