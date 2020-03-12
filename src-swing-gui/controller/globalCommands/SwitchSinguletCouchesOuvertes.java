package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ctom.hulis.huckel.exception.SpinOrbitaleMoleculaireException;

import util.io.SwingIO;
import views.FrameApp;


/**
 * This command manipulates the spin of the delocalized structure
 * 
 * @author ycarissan
 *
 */
public class SwitchSinguletCouchesOuvertes implements ICommand, ActionListener {
	FrameApp app;

	public SwitchSinguletCouchesOuvertes(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "switch singulet couches ouvertes");
	
		
		// s'il existe des sutructures localisses l'option est indisponible
		if (this.app.getCurrentMesomery().countAllSructs() > 1)
			return;
		
		SwingIO.PrintIfln("Singulet <-> Singulet CO");
		try {
			this.app.getCurrentMesomery().getDelocalizedStructure().switchSinguletCouchesOuvertes();
		} catch (SpinOrbitaleMoleculaireException e) {
			SwingIO.error(this.getClass().getName(), "execute",
					e.getMessage(), e);
			SwingIO.reportError(app);
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
