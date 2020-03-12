package controller.globalCommands;

import java.awt.event.ActionEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.MenuElement;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command quits the applet
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionQuit extends ActionCommand implements  ICommand,
		IGlobalCommands {

	FrameApp app;
	int errcode;
	
	public ActionQuit(FrameApp app) {
		super(IGlobalCommands.QUIT);
	
		updateLanguage();
		this.app = app;
		this.errcode=0;
	}

	public ActionQuit(FrameApp app, int errcode) {
		
		super(IGlobalCommands.QUIT);
		updateLanguage();
		this.app = app;
		this.errcode= errcode;
	}
	
	public void updateLanguage() {
	
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb3.getString("kquit"), bundle.getString("kquit1"), "");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		try {
			SwingIO.log(this.getClass().getName(), "execute", "quit the software");
			this.setEnabled(false);
				Thread t = ActionStopConsumingProcess.stopAllConsumingProces(app);
			try {
				t.join();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			app.getCurrentMesomery().deleteAllStructures();
			System.out.println("disableOptionsContext");
			ActionCommandRegistry.getInstance().disableAll(); // apres app.getCurrentMesomeryView().removeAll() sinon les boutons se reactivent
			
			// desactive toutes les options de la barre de menu
			for (MenuElement menuElement : app.menuBar.getSubElements())
				if (menuElement instanceof JMenuItem)
					((JMenuItem) menuElement).setEnabled(false);
				else if (menuElement instanceof JMenu)
					((JMenu) menuElement).setEnabled(false);
				

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
	   
			this.app.terminate(errcode);
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