package util;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

public class ThreadUtils {
	public static boolean NEVER_INVOKE_LATER = false;

	public static void  invokeAsSoonAsPossible(Runnable action) {
	    if (NEVER_INVOKE_LATER || EventQueue.isDispatchThread() || SwingUtilities.isEventDispatchThread()) {
	    	action.run();
	    	    	
	    }
	    else SwingUtilities.invokeLater(action);
	}

}
