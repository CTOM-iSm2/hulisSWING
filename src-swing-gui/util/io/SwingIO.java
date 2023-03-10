package util.io;

import org.ctom.hulis.util.io.HuckelIO;

import access.Rights;
import controller.globalCommands.ActionQuit;
import views.ErrorReport;
import views.FrameApp;

public class SwingIO extends 	HuckelIO {
	

	public static void reportError (FrameApp app) {
		System.out.println(SwingIO.getLooger().toString());
		new ErrorReport(SwingIO.getLooger().toString()).setVisible(true);
		if (Rights.CHECK_USER_ACCESS_RIGHTS)
			app.registerDisconnect();
		new ActionQuit(app, 99).execute();
	}
	
	

}
