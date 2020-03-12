package controller.globalCommands;

import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.AutoGenerateConditionException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.DressStructureLocalizedException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This thread autogenerate the Lewis structures 
 * @author ngoudard
 *
 */
public class TraitAutoGen extends Thread {

	MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
	FrameApp app;

	public TraitAutoGen(FrameApp app) {
		this.app = app;
	}

	@Override
	public void run() {

		final Mesomery mesomery = app.getCurrentMesomeryView().getMesomery();
		try {
			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {

					app.getStateManager().initNewState();
					EnableActionManager.getInstance().updateActionsToEnable(app);
					ActionCommandRegistry.getInstance().get(IGlobalCommands.AUTO_GEN_STRUCT).setEnabled(false);
				}
			});	
			//generate
			mesomery.autoGenLewisStructures();
			
			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {

					EnableActionManager.getInstance().updateActionsToEnable(app);
					EnableActionManager.getInstance().enableActionsAfterAutogen(app, true);
					app.toolBarMesomery.btChangeElectrons.doClick();		
					app.toolBarMesomery.refreshSlider();
					app.toolBarMesomery.adjustSlider(-(Double)mesomery.getSelectionTreshold());
					app.statusBar.setTxtState(bundle
							.getString("kgen3"));
					app.getStateManager().saveState();
				}
			});	

		}catch(DressStructureLocalizedException e) {
			final Exception exception = e;
			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					JOptionPane.showMessageDialog(app, "cannot generate mesomery",  "error", JOptionPane.ERROR_MESSAGE);
				}
			});	


		} catch (AutoGenerateConditionException e) {
			final Exception exception = e;
			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					MyJOptionPane.showMessageDialog(app, exception.getMessage(),
							bundle.getString("kerrtitle"),
							JOptionPane.ERROR_MESSAGE);
					app.statusBar.setTxtState(exception.getMessage());
					return;
				}
			});	



		} catch (IncorrectNumberPiElectronsException e) {
			final Exception exception = e;

			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {			
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					MyJOptionPane.showMessageDialog(app, exception.getMessage(),
							bundle.getString("kerrtitle"),
							JOptionPane.ERROR_MESSAGE);
					app.statusBar.setTxtState(exception.getMessage());			
					return;
				}
			});	

		} catch (CoupleException e) {
			final Exception exception = e;

			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {	
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					MyJOptionPane.showMessageDialog(app, exception.getMessage(),
							bundle.getString("kerrtitle"),
							JOptionPane.ERROR_MESSAGE);
					app.statusBar.setTxtState(exception.getMessage());

					return;
				}
			});	
		} catch (IMethodException e) {
			final Exception exception = e;

			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {	
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					MyJOptionPane.showMessageDialog(app, exception.getMessage(),
							bundle.getString("kerrtitle"),
							JOptionPane.ERROR_MESSAGE);
					app.statusBar.setTxtState(exception.getMessage());

					return;
				}
			});	
		} catch (MesomeryNoStructureLocalizedException e) {
			final Exception exception = e;

			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {	
					SwingIO.error(this.getClass().getName(), "run", exception.getMessage(), exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					MyJOptionPane.showMessageDialog(app, exception.getMessage(),
							bundle.getString("kerrtitle"),
							JOptionPane.ERROR_MESSAGE);
					app.statusBar.setTxtState(exception.getMessage());

					return;
				}
			});	
		} catch (OutOfMemoryError e) {
			final OutOfMemoryError exception = e;

			ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

				public void run() {			
					SwingIO.error(this.getClass().getName(), "run", "out of memory", exception);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					mesomery.startFlyCalculate();
					app.statusBar.setTxtState(exception.getMessage());
					// interrupt();
					SwingIO.reportError(app);
					return;
				}
			});
		} // Try Catch
		catch (Exception e ) {
			SwingIO.error(this.getClass().getName(), "run", "unexpected error has occured in autogenerating lewis structures", e);
			EnableActionManager.getInstance().updateActionsToEnable(app);
			mesomery.startFlyCalculate();
			JOptionPane.showMessageDialog(app, "Unexpected error has occured in autogenerating lewis structures", "error",  JOptionPane.ERROR_MESSAGE);
		}



	} // Run

}
