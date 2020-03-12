package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.ThreadUtils;
import util.io.SwingIO;
import views.AboutBox;
import views.FrameApp;

/**
 * This commands opens the about box
 * 
 * @author ngoudard
 *
 */
public class ActionOpenAbout implements ActionListener, ICommand {
	FrameApp app;

	public ActionOpenAbout(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "open about box");
		final AboutBox dialog= new AboutBox(this.app);
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