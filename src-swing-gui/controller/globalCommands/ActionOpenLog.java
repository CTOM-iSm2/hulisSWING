package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.ThreadUtils;
import util.io.SwingIO;
import views.ErrorReport;
import views.FrameApp;

/**
 * This commands opens the log event
 * 
 * @author ngoudard
 *
 */
public class ActionOpenLog implements ActionListener, ICommand {
	FrameApp app;

	public ActionOpenLog(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "open log event");
		final ErrorReport dialog= new ErrorReport(SwingIO.getLooger().toString(), false);
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