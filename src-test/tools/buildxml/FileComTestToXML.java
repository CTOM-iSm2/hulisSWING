/**
 * 
 */
package tools.buildxml;

import java.io.File;

import util.test.IOReadGaussianTest;
import views.FrameApp;
import controller.globalCommands.ReadGaussianCommand;
import controller.globalCommands.SaveMesomeryToXML;

/**
 * Copy a file .comtest to a file .txml
 * @author ngoudard
 *
 */
public class FileComTestToXML {
	/**
	 * 
	 */
	private final FrameApp app;

	/**
	 * <
	 * @param app
	 * @param in : file in
	 * @param out : file out
	 */
	public FileComTestToXML(FrameApp app, File in, File out) {
		this.app = app;
		app.selectAllMethods();
		try {
		   // read the gaussian .comtest file
			new ReadGaussianCommand(this.app, new IOReadGaussianTest(in) ).execute();
			
			new SaveMesomeryToXML (app.getCurrentMesomery(), out).execute();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}