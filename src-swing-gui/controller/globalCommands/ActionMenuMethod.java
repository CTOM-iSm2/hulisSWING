package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryMethodExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.MesomeryRemoveMethodException;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.selectors.MesomeryEnergyStructuresSelector;
import org.ctom.hulis.huckel.selectors.MesomeryHLPOverlapStructuresSelector;

import util.io.SwingIO;
import views.FrameApp;

/***
 * This command is called when the user select an item method calculation in the menu bar.
 * If the item is disabled, the method is removed in the mesomery.
 * If he enable the method, the method is added in the mesomery and then
 * the weights calculation is only invoked for this method 
 * 
 * @author ngoudard
 *
 */
public class ActionMenuMethod implements ICommand, ActionListener {
	public static boolean RUNNING_TEST = false;

	FrameApp app;
	JMenuItem item;

	public ActionMenuMethod(JMenuItem item, FrameApp app) {
		this.app = app;
		this.item = item;
	}

	public void actionPerformed(ActionEvent e) {
		final Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();
		String methodName = app.menuBar.getMethodNameByItem(item);
		final IMethod method = FrameApp.getImplementedMethod(methodName);
		if (!(mesomery.containsMethod(method)))
			app.registerAction(method.getName());
		execute();
	}

	public void execute() {

		final Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();
		String methodName = app.menuBar.getMethodNameByItem(item);
		final IMethod method = FrameApp.getImplementedMethod(methodName);

		SwingIO.log(this.getClass().getName(), "execute", "enable / disable method : " + methodName + " -> " + this.item.isSelected() );

		if (this.item.isSelected() ) {
			if (!(mesomery.containsMethod(method))){
			
				Thread t = ActionStopConsumingProcess.stopAllConsumingProces(app);
				try {
					t.join();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (method.getName().equals("HLP"))
					mesomery.setStructureSelector(new MesomeryHLPOverlapStructuresSelector());
				if (mesomery.getStructureSelector() == null)
					mesomery.setStructureSelector(new MesomeryEnergyStructuresSelector());				
				Thread t2 = new Thread(new Runnable() {

					public void run() {
						try {
							mesomery.addMethod(method);
							//app.getCurrentMesomeryView().rebuild();

						} catch (IncorrectNumberPiElectronsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MesomeryMethodExistsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CoupleException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (MesomeryNoStructureLocalizedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}});
				t2.start();
				if (RUNNING_TEST)
					try {
						t2.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

			}
		} 
		else // deselect
			if (mesomery.containsMethod(method))
				try {
					if (method.getName().equals("HLP"))
						mesomery.setStructureSelector(new MesomeryEnergyStructuresSelector());
					if (mesomery.getStructureSelector() == null)
						mesomery.setStructureSelector(new MesomeryEnergyStructuresSelector());
		
					mesomery.removeMethod(method.getName());
					//app.getCurrentMesomeryView().rebuild();
				} catch (CoupleException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				} catch (MesomeryRemoveMethodException e) {
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
