package controller.globalCommands;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;

import util.MyResourceBundle;
import util.languages.LanguageManager;
import views.FrameApp;
import views.ToolBarHuckel;

/**
 * This command is called when the user change mode. It puts the mode to "no action selected"
 * if the current mode is disabled by another processus
 * 
 * @author Nicolas
 * 
 */

@SuppressWarnings("serial")
public class ActionChangeMode extends ActionCommand implements IGlobalCommands {
	FrameApp app;
	String command;
	public ActionChangeMode(FrameApp app, String command) {
		this.app = app;
		this.command = command;
		init(command);
		updateLanguage();

	}

	public void updateLanguage() {
		MyResourceBundle rb2 =  LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle rb3 =  LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle =  LanguageManager.getInstance().getResource("ActionsApp");
				
		if(this.command.equals(IGlobalCommands.ADD) )
			setLang(rb3.getString("kbuild"), bundle.getString("kbuild1"),
					bundle.getString("kbuild2"));
		else if(this.command.equals(IGlobalCommands.CHANGE) )
			setLang(rb3.getString("kchange"), bundle.getString("kchange1"),
					bundle.getString("kchange2"));
		else if(this.command.equals(IGlobalCommands.REMOVE) )
			setLang(rb3.getString("kdelete"), bundle.getString("kdelete1"),
					bundle.getString("kdelete2"));
		else if(this.command.equals(IGlobalCommands.MOVE) )
			setLang(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(rb3.getString("kimgmove"))).getImage().getScaledInstance(ToolBarHuckel.ICON_WIDTH, ToolBarHuckel.ICON_HEIGHT, Image.SCALE_SMOOTH) ), bundle.getString("kmove1"),
					bundle.getString("kmove2"));
		else if(this.command.equals(IGlobalCommands.ROTATE) )
			setLang(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(rb3.getString("kimgrotate"))).getImage().getScaledInstance(ToolBarHuckel.ICON_WIDTH, ToolBarHuckel.ICON_HEIGHT, Image.SCALE_SMOOTH) ), bundle.getString("krotate1"),
					bundle.getString("krotate2"));
		else if(this.command.equals(IGlobalCommands.RENUM) )
			setLang(rb3.getString("krenum"), bundle.getString("krenum1"),
					bundle.getString("krenum2"));
		else if(this.command.equals(IGlobalCommands.CHANGE_ELECTRON) )
			setLang(rb2.getString("kchangebonds"), bundle.getString("kchangebonds1"),
					bundle.getString("kchangebonds2"));					
		else if(this.command.equals(IGlobalCommands.COUPLE) )
			setLang(rb2.getString("kcouple"), bundle.getString("kcouple1"),
					bundle.getString("kcouple2"));
		else if(this.command.equals(IGlobalCommands.BLOC) )
			setLang(rb2.getString("kbloc"), bundle.getString("kbloc1"),
					bundle.getString("kbloc2"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.app.statusBar.setTxtState(getLongDescription());
		app.setStructureViewControllerState(e.getActionCommand());
	}


}