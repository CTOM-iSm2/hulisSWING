package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user invoke the action to stop the 
 * autogeneration of lewis structures
 * 
 * @author Nicolas
 * 
 */

@SuppressWarnings("serial")
public class ActionStopAutoGenStruct extends ActionCommand implements  ICommand,
	IGlobalCommands {
	
	FrameApp app;
	
	public ActionStopAutoGenStruct(FrameApp app) {
		super(IGlobalCommands.STOP_AUTO_GEN_STRUCT);
		
		this.app = app;
		updateLanguage();
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
	

	/**
	 * 
	 */
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "stop consuming process");

		ActionStopConsumingProcess.stopAutoGenStructures(app);
	}


	public void updateLanguage() {
		MyResourceBundle rb2 =  LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		/*setLang(
				rb2.getString("kgenerateall"), bundle.getString("kgen1"),
				bundle.getString("kgen2"));*/
		setLang(
		rb2.getString("stop"), "stop generation of Lewis structures stop to compute weights",
		"compute is stopping...");

	}
}
