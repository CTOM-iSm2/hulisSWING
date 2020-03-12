package controller.globalCommands;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import util.MyResourceBundle;
import util.languages.LanguageManager;

/***
 * This class describe a genric action command
 * @author ngoudard
 *
 */

@SuppressWarnings("serial")
public abstract class ActionCommand extends AbstractAction {

	MyResourceBundle bundle ;

	
	public ActionCommand() {
		init("", "", "", "");
	}

	public ActionCommand(String actionCommand) {
		init(actionCommand, "", "", "");
	}
	
	public ActionCommand(String actionCommand, String name) {
		init(actionCommand, name, "", "");
	}
	
	public ActionCommand(String actionCommand, Icon icon) {
		init(actionCommand, icon, "", "");
	}

	public ActionCommand(String actionCommand, String name,
			String shortDescription, String longDescription) {
		init(actionCommand, name, shortDescription, longDescription);
	}
	
	public ActionCommand(String actionCommand, Icon icon,
			String shortDescription, String longDescription) {
		init(actionCommand, icon, shortDescription, longDescription);
	}

	public void actionPerformed(ActionEvent e) {

	}

	public String formatToolTip(String s) {
		String s2;
		s2 = "<html><span style = \"font-size : 11pt\">" + s + "</span></html>";

		return s2;
	}

	public String getActionCommand() {
		return (String) getValue(Action.ACTION_COMMAND_KEY);
	}

	public String getLongDescription() {
		return (String) getValue(Action.LONG_DESCRIPTION);
	}

	public String getName() {
		return (String) getValue(Action.NAME);
	}

	public String getShortDescription() {
		return (String) getValue(Action.SHORT_DESCRIPTION);
	}

	public void init(String actionCommand, String name,
			String shortDescription, String longDescription) {
		putValue(Action.ACTION_COMMAND_KEY, actionCommand);
		putValue(Action.NAME, name);
		putValue(Action.LONG_DESCRIPTION, longDescription);
		putValue(Action.SHORT_DESCRIPTION, formatToolTip(shortDescription));
	}
	
	public void init(String actionCommand, Icon icon,
			String shortDescription, String longDescription) {
		putValue(Action.ACTION_COMMAND_KEY, actionCommand);
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.LONG_DESCRIPTION, longDescription);
		putValue(Action.SHORT_DESCRIPTION, formatToolTip(shortDescription));

	}
	
	public void init(String actionCommand) {
		putValue(Action.ACTION_COMMAND_KEY, actionCommand);
		putValue(Action.LONG_DESCRIPTION, "");
		putValue(Action.SHORT_DESCRIPTION, "");

	}

	public void updateLanguage() {
		
	}

	public void setLang(String name, String shortDescription,
			String longDescription) {
		bundle = LanguageManager.getInstance().getResource("ActionsApp");
		putValue(Action.NAME, name);
		putValue(Action.LONG_DESCRIPTION, longDescription);
		putValue(Action.SHORT_DESCRIPTION, formatToolTip(shortDescription));

	};
	
	public void setLang(Icon icon, String shortDescription,
			String longDescription) {
		bundle = LanguageManager.getInstance().getResource("ActionsApp");
		putValue(Action.LARGE_ICON_KEY, icon);
		putValue(Action.LONG_DESCRIPTION, longDescription);
		putValue(Action.SHORT_DESCRIPTION, formatToolTip(shortDescription));

	};
}