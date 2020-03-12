package views;

import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Container;
import java.net.URL;

import javax.jnlp.DownloadServiceListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import netscape.javascript.JSObject;




// ...

public class CustomProgress implements DownloadServiceListener {   
	Container surfaceContainer = null;
	AppletStub appletStub = null;
	JProgressBar progressBar = null;
	JLabel statusLabel = null;
	boolean uiCreated = false;

	public CustomProgress(Object surface) {
		init(surface, null);
	}

	public CustomProgress(Object surface, Object stub) {
		init(surface, stub);
	}

	public void init(Object surface, Object stub) {
		try {
			surfaceContainer = (Container) surface;
			appletStub = (AppletStub) stub;
		} catch (ClassCastException cce) {
			// ...
		}
	}

	public void downloadFailed(java.net.URL url, java.lang.String version) {        
		// TODO Auto-generated method stub

	}

	public void progress(URL url, String version,
			long readSoFar, long total,
			int overallPercent) {        
		// check progress of download and update display
		updateProgressUI(overallPercent);
	}


	public void upgradingArchive(java.net.URL url,
			java.lang.String version,
			int patchPercent,
			int overallPercent) {
		updateProgressUI(overallPercent);

	}

	public void validating(java.net.URL url,
			java.lang.String version,
			long entry,
			long total,
			int overallPercent) {
		updateProgressUI(overallPercent);		
	}

	private void updateProgressUI(int overallPercent) {
		if (!uiCreated && overallPercent > 0
				&& overallPercent < 100) {
			// create custom progress indicator's
			// UI only if there is more work to do,
			// meaning overallPercent > 0 and
			// < 100 this prevents flashing when
			// RIA is loaded from cache
			create(); 
			uiCreated = true;            
		}
		if (uiCreated) {
			progressBar.setValue(overallPercent);
		}

	}
	private void create() {
		JPanel top = createComponents();
		if (surfaceContainer != null) {
			// lay out loading progress UI in the given
			// Container
			surfaceContainer.add(top, BorderLayout.NORTH);
			surfaceContainer.invalidate();
			surfaceContainer.validate();
		}     
	}
	private JPanel createComponents() {
		JPanel top = new JPanel();
		// ...
		// get applet parameter using an instance of the
		// AppletStub class "tagLine" parameter specified
		// in applet's JNLP file
		String tagLine = "";
		if (appletStub != null) {
			tagLine = appletStub.getParameter("tagLine");
		}
		String lblText = "<html><font color=red size=+2>JDK Documentation</font><br/>" +
				tagLine + " <br/></html>";
		JLabel lbl = new JLabel(lblText);
		top.add(lbl, BorderLayout.NORTH);

		// use JSObject.getWindow(null) method to retrieve
		// a reference to the web page and make JavaScript
		// calls. Duke logo displayed if displayLogo variable
		// set to "true" in the web page
		String displayLogo = "false";    
		JSObject window = JSObject.getWindow(null);        
		if (window != null) {
			displayLogo = (String)window.getMember("displayLogo");
		}
		if (displayLogo.equals("true")) {
			lbl = new JLabel();
			ImageIcon logo = createImageIcon("images/DukeWave.gif", "logo");
			lbl.setIcon(logo);
			top.add(lbl, BorderLayout.EAST);
		}

		statusLabel = new JLabel(
				"html><font color=green size=-2>" +
				"Loading applet...</font></html>");
		top.add(statusLabel, BorderLayout.CENTER);

		// progress bar displays progress
		progressBar = new JProgressBar(0, 100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		top.add(progressBar, BorderLayout.SOUTH);

		return top;
	}
	 /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path, String description) {
        java.net.URL imgURL = CustomProgress.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
	

}

