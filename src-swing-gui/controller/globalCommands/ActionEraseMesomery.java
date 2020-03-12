package controller.globalCommands;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.FrameResultStruct;

/***
 * This command removes all Lewis structures
 * 
 * @author ngoudard
 *
 */

@SuppressWarnings("serial")
public class ActionEraseMesomery extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionEraseMesomery(FrameApp app) {
		super(IGlobalCommands.ERASE_LEWIS);
		updateLanguage();
		this.app = app;
	}
	
	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb2.getString("kerasem"), bundle.getString("kerasem1"),
				bundle.getString("kerasem2"));
	}
	
	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		execute();

	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "erase the mesomery");

		final Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		/*
		 * if (JOptionPane.showConfirmDialog(app,
		 * bundle.getString("kconfirmerasemes") ,
		 * bundle.getString("kquitconfirmtitle"), JOptionPane.YES_NO_OPTION) ==
		 * JOptionPane.NO_OPTION) return;
		 */
		this.setEnabled(false);
		
		Thread t = ActionStopConsumingProcess.stopAllConsumingProces(app);
		try {
			t.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		t = new Thread(new Runnable(){

			public void run() {
				app.statusBar.setTxtState(bundle
						.getString("kerasem2"));
				try {

					app.getStateManager().initNewState();
					
					mesomery.deleteLocalizedStructures(false);
					
					ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

						public void run() {			
							// fermer toutes les fenetres des structures localisees
							for (Window w : Frame.getWindows()) 
								if (w instanceof FrameResultStruct && (((FrameResultStruct) w).getStructure() instanceof StructureLocalized)) 
									w.dispose();
							
							EnableActionManager.getInstance().updateActionsToEnable(app);
							app.toolBarHuckel.btAdd.doClick();
							app.toolBarMesomery.refresh();
							app.getCurrentMesomeryView().rebuild();

						}});
					app.getStateManager().saveState();					
				} catch (Exception e1) {
					final Exception ex = e1;
					ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

						public void run() {			
							SwingIO.error(this.getClass().getName(), "execute", ex
									.getMessage(), ex);					
							SwingIO.reportError(app);
						}
					});


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