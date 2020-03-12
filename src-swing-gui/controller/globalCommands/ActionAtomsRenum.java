package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import util.io.SwingIO;
import views.FrameApp;

/**
 * This command is called when the user enter in "order atom" mode.
 * It resets atoms to zero and disables some options
 * 
 * @author Nicolas
 * 
 */
@SuppressWarnings("serial")
public class ActionAtomsRenum extends ActionChangeMode implements ICommand {
	FrameApp app;

	public ActionAtomsRenum(FrameApp app) {
		super(app, IGlobalCommands.RENUM);
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "renumber");
		Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();
		StructureDelocalized structureDelocalized = mesomery.getDelocalizedStructure();
		if	(structureDelocalized == null)
			return;

		this.app.getStateManager().initNewState();

		if (!structureDelocalized.areCorrectAtomsSeqNum()) {
			/* On donne un numero au atomes qui ne sont pas correctement numerotes */
			for (HuckelAtom huckelAtom : structureDelocalized.getHuckelAtoms())
				if (huckelAtom.getSeqNum() == 0)
					huckelAtom.autoSeqNum();
			
			app.clickButtonCommand(IGlobalCommands.ADD);
			
		}
		else
			structureDelocalized.resetSeqNumListHuckelAtomToZero();
		EnableActionManager.getInstance().updateActionsToEnable(app);
		this.app.getStateManager().saveState();
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
