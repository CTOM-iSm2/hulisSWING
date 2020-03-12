package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.Molecule;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

public class ActionOptimizeGeometry extends ActionCommand implements  ICommand,
IGlobalCommands {

	FrameApp app;
	public ActionOptimizeGeometry(FrameApp app) {
		super(IGlobalCommands.OPTIMIZE);
		this.updateLanguage();
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "optimize geometry");
		app.getStateManager().initNewState();
		
		Molecule molecule = this.app.getCurrentMesomery().getDelocalizedStructure();
		/*Geom2DOptimizer opt = new Geom2DOptimizer(molecule);
		opt.setOptimize(true);
		opt.setVerbose(false);
		molecule.notifyChanged();
		opt.setOptimize(false);*/
		molecule.optimizeGeometry();
		app.getStateManager().saveState();

	}

	
	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb2.getString("koptimize"), bundle.getString("koptimize1"),
				bundle.getString("koptimize2"));
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
