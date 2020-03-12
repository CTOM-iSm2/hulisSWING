package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.ThreadUtils;
import util.io.SwingIO;
import views.FrameApp;
import views.PreferencesFrame;

/**
 * This commands opens the about box
 * 
 * @author ngoudard
 *
 */
public class OpenPreferences implements ActionListener, ICommand {
	FrameApp app;

	public OpenPreferences(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "open preferences");

		final PreferencesFrame dialog=  new PreferencesFrame(app);
		 // this solves the problem where the dialog was not getting
		 // focus the second time it was displayed
		 //http://www.devdaily.com/blog/post/jfc-swing/input-focus-dialog-jdialog-java-swing-problem-solution
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() 
		  {  
		    public void run() {  
		    	dialog.requestFocusInWindow();
		    }
		  });
		dialog.setVisible(true);
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