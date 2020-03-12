package controller.globalCommands;

import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.FrameResult;

/**
 * This command removes all the structures.
 * THen creates a new delocalized structures
 * 
 * @author Nicolas
 * 
 */
@SuppressWarnings("serial")
public class ActionEraseAll extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionEraseAll(FrameApp app) {
		super(IGlobalCommands.ERASE_ALL);
	
		updateLanguage();
		this.app = app;
	}

	public void updateLanguage () {
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(rb3.getString("keraseall"), bundle.getString("keraseall1"),"");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();

	}

	public void execute()  {
		SwingIO.log(this.getClass().getName(), "execute", "erase all structures");
		final Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		/*
		 * if (JOptionPane.showConfirmDialog(app,
		 * bundle.getString("kconfirmeraseall"),
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
		
		t = new Thread(new Runnable() {

			public void run() {
				app.statusBar.setTxtState("");
				try {
					app.getStateManager().initNewState();
					mesomery.deleteAllStructures();

					mesomery.addDelocalizedStruct(new StructureDelocalized());
					
					ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

						public void run() {		
							// fermer toutes les fenetres de resultat
							for (Window w : Frame.getWindows()) 
								if (w instanceof FrameResult) 
									w.dispose();
							
							EnableActionManager.getInstance().updateActionsToEnable(app);
							app.menuBar.menuOptGeom.setSelected(false);
							app.toolBarHuckel.btAdd.doClick();
						}
					});

					app.getStateManager().saveState();
					
				} catch (MesomeryDelocalizedStructureExistsException e) {
					final Exception ex = e;
					ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

						public void run() {			
							SwingIO.error(this.getClass().getName(), "execute", ex
									.getMessage(), ex);					
							SwingIO.reportError(app);
						}
					});

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

				}

				
			}});
			t.start();
			try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
