package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryLocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command deletes the current structure
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionErase1 extends ActionCommand implements  ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionErase1(FrameApp app) {
		super(IGlobalCommands.ERASE_1);
		this.app = app;
	
		updateLanguage();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();

	}

	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb2.getString("kerase1"), bundle.getString("kerase11"),
				bundle.getString("kerase12"));
	}
	
	public void execute() {
		final Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();
		this.setEnabled(false);
		
		Thread t = ActionStopConsumingProcess.stopAllConsumingProces(app);
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		t = new Thread(new Runnable() {

			public void run() {
				app.statusBar.setTxtState(bundle
						.getString("kerase12"));

				Structure currentStructure = app.getCurrentMesomeryView().getCurrentStructure();

				if (currentStructure instanceof StructureLocalized){
					try {
						app.getStateManager().initNewState();
						final String structureName =  currentStructure.getName();
						SwingIO.log(this.getClass().getName(), "execute", "Try to erase structure Psi" + structureName );
						mesomery.deleteStructure(currentStructure);

						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {			
								SwingIO.log(this.getClass().getName(), "execute", "structure Psi" + structureName   + " has been erased");
								EnableActionManager.getInstance().updateActionsToEnable(app);
								if (mesomery.countSelectedLocalizedStructs() == 0) 
									app.clickButtonCommand(IGlobalCommands.ADD);
								
							}
						});
						app.getStateManager().saveState();
					} catch (CoupleException e) {
						final Exception ex = e;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {			
								SwingIO.error(this.getClass().getName(), "execute", ex
										.getMessage(), ex);					
								SwingIO.reportError(app);
							}
						});
					

					} catch (IMethodException e) {
						final Exception ex = e;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {			
								SwingIO.error(this.getClass().getName(), "execute", ex
										.getMessage(), ex);					
								SwingIO.reportError(app);
							}
						});
					} catch (MesomeryNoStructureLocalizedException e) {
						final Exception ex = e;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {			
								SwingIO.error(this.getClass().getName(), "execute", ex
										.getMessage(), ex);					
								SwingIO.reportError(app);
							}
						});
					} catch (MesomeryLocalizedStructureExistsException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			}});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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