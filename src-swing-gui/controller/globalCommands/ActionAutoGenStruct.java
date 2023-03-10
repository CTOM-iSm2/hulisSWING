package controller.globalCommands;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.Mesomery;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user invoke Lewis structures autogeneration 
 * It disable some options and execute a thread to autogenerate
 * 
 * @author Nicolas
 * 
 */

@SuppressWarnings("serial")
public class ActionAutoGenStruct extends ActionCommand implements  ICommand,
		IGlobalCommands {
		FrameApp app;
	boolean outOfMemory = false;

	public ActionAutoGenStruct(FrameApp app) {
		super(IGlobalCommands.AUTO_GEN_STRUCT);
		
		this.app = app;
		updateLanguage();
	}

	public void updateLanguage() {
		MyResourceBundle rb2 =  LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb2.getString("kgenerateall"), bundle.getString("kgen1"),
				bundle.getString("kgen2"));
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
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
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "autogenerate structures");
		setEnabled(false);
		boolean outOfMemory = false;
		app.registerAction("AutoGen");
		
		Mesomery mesomery = this.app.getCurrentMesomeryView().getMesomery();

		if (!mesomery.getDelocalizedStructure().areCorrectAtomsSeqNum()) {
			MyJOptionPane.showMessageDialog(this.app, bundle
					.getString("kerrorseqnum"), bundle
					.getString("kerrtitle"), JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (mesomery.getDelocalizedStructure().countAtoms() == 0)
			return;


		new TraitAutoGen(app).start();
	}

}
