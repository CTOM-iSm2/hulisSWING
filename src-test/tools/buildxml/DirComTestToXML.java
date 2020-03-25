package tools.buildxml;

import java.io.File;
import java.io.FilenameFilter;

import views.AppletToFrame;
import views.FrameApp;

/****
 * Gets files .comtest in a directory (special gaussian files) and copy/transform
 * them in xml files
 * @author ngoudard
 *
 */
public class DirComTestToXML   {
	private final static String DATA_DIR = "./src/test/datas/test";
	FrameApp app;
	
	/**
	 * filter .comtest files
	 * @author ngoudard
	 *
	 */
	class ComTestFilter implements FilenameFilter {
	    public boolean accept(File dir, String name) {
	    	return (name.endsWith(".comtest"));
	    }
	}
	
	/**
	 * constructor 
	 */
	public DirComTestToXML() {
		// open applet
		
		this.app =  AppletToFrame.createAndShowGUI(false);
		// select all methods checkboxes in the menu bar
		app.selectAllMethods();
		
		// select .comtest files in the datas directory invoke a class to 
		// trasnform them in xml
		File f = new File(DATA_DIR);
		FilenameFilter filter = new ComTestFilter();
		String[] noms = f.list(filter);

		for (int i = 0; noms != null && i < noms.length; i++) {
			new FileComTestToXML (app,  new File(DATA_DIR+noms[i])
			, new File(DATA_DIR+ noms[i]+ ".txml") );	
		}
		
	}
	
	/**
	 * main to test
	 * @param args
	 */
	public static void main (String[] args) {
		new DirComTestToXML();
	}
	
}
