package controller.globalCommands;

import java.util.HashMap;


/**
 * This class registries ActionCommand objects
 * 
 * @author ngoudard
 *
 */
public class ActionCommandRegistry {
	
	private HashMap<String, ActionCommand> lstActions;
	private static ActionCommandRegistry instance = null;
	
	public static ActionCommandRegistry getInstance() {
		if (instance ==null)
			instance = new ActionCommandRegistry();
		return instance;
	}
	
	private ActionCommandRegistry() {
		this.lstActions = new HashMap<String, ActionCommand>();
	}
	
	/**
	 * Add an action command in the registry
	 * @param action
	 */
	public void add(ActionCommand action) {
		this.lstActions.put(action.getActionCommand(), action);
	}

	/**
	 * Get a registered action by its id
	 * 
	 * @param actionCommand
	 * @return
	 */
	public ActionCommand get(String actionCommand) {
		return this.lstActions.get(actionCommand);
	}
	
	/**
	 * 
	 */
	public void disableAll() {
		if (lstActions == null)
			return;
		
		for (ActionCommand action : lstActions.values())
			action.setEnabled(false);
	}


	/**
	 * 
	 */
	public void enableAll() {
		if (lstActions == null)
			return;
		
		for (ActionCommand action : lstActions.values())
			action.setEnabled(true);
	}

	/**
	 * 
	 */
	public void updateLanguage() {
		if (lstActions == null)
			return;
		
		for (ActionCommand action : lstActions.values())
			action.updateLanguage();
	}
	
	
	/**
	 * 
	 */
	public static void clear() {
		instance.lstActions.clear();
		instance = null ;

	}
	
}
