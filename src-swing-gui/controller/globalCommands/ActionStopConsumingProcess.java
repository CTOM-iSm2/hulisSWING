package controller.globalCommands;

import javax.swing.Action;

import org.ctom.hulis.huckel.Mesomery;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user invoke the action to stop the weights compute and the 
 * autogeneration
 * 
 * @author Nicolas
 * 
 */

@SuppressWarnings("serial")
public class ActionStopConsumingProcess {
	
	private static Thread stopAutoGenStructures;
	private static Thread stopCalculateWeights;
	private static Thread stopAllConsumingProcess;
	
	
	
	public static boolean isStopAutoGenStructuresRunning() {
		if (stopAutoGenStructures == null)
			return false;
		return stopAutoGenStructures.isAlive();
	}
	
	public static boolean isStopCalculateWeightsRunning() {
		if (stopCalculateWeights == null)
			return false;
		return stopCalculateWeights.isAlive();
	}
	
	public static boolean isStopConsumingProcessRunning() {
		if ((stopAutoGenStructures == null) && (stopCalculateWeights == null))
			return false;
		return  ((stopAutoGenStructures != null) && stopAutoGenStructures.isAlive()) || ((stopCalculateWeights != null) && stopCalculateWeights.isAlive());
	}
	
	/**
	 * 
	 */
	public static Thread  stopAllConsumingProces(final FrameApp app) {
		stopAllConsumingProcess = new Thread(new Runnable() {

			public void run() {
				Thread t1 = stopAutoGenStructures(app);
				try {
					t1.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stopCalculateWeights(app);
					
				
			}});
		
		stopAllConsumingProcess.start();
		return stopAllConsumingProcess;
	}
	/**
	 * 
	 * @param app
	 * @return
	 */
	public static Thread stopAutoGenStructures(final FrameApp app) {	
		final Action action = ActionCommandRegistry.getInstance().get(IGlobalCommands.STOP_AUTO_GEN_STRUCT);
		action.setEnabled(false);
		final String txtToRestore = app.getStatusBar().getTxtState();
		stopCalculateWeights = new Thread(new Runnable() {

			public void run() {
				Mesomery mesomery= app.getCurrentMesomery();
				mesomery.stopConsumingProcess();
				
				while (mesomery.isAutoGenerating()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
				ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

					public void run() {
						ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

							public void run() {
								app.getStatusBar().setTxtState(txtToRestore);
								app.toolBarMesomery.btStopAutoGenStruct.setVisible(false);									
							}});
				
						action.setEnabled(true);
						
					}});

			}});
		stopCalculateWeights.start();
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				MyResourceBundle bdl = LanguageManager.getInstance().getResource("Threads");
				app.getStatusBar().setTxtState(bdl.getString("stopComputeWeights"));
				
			}
			
		});
		return stopCalculateWeights;


	}
	
	/**
	 * 
	 * @param app
	 * @return
	 */
	public static Thread stopCalculateWeights(final FrameApp app) {
		final String txtToRestore = app.getStatusBar().getTxtState();
		stopCalculateWeights = new Thread(new Runnable() {

			public void run() {
				Mesomery mesomery= app.getCurrentMesomery();
				mesomery.stopConsumingProcess();
				
				while (mesomery.isCalculatingWeights()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
		
				ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

					public void run() {
						app.getStatusBar().setTxtState(txtToRestore);
						
					}});

			}});
		stopCalculateWeights.start();
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				MyResourceBundle bdl = LanguageManager.getInstance().getResource("Threads");
				app.getStatusBar().setTxtState(bdl.getString("stopAutogen"));
	
				
			}
			
		});
		return stopCalculateWeights;

	}
	

	
	
}
