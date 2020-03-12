package views;

import java.awt.Frame;

import javax.swing.JDialog;

public class MyJDialog extends JDialog {
	protected static boolean sModal = true;
	

	public static void setDefaultModal(boolean modal) {
		sModal = modal;
	}

	public MyJDialog(Frame frame) {
		super (frame, sModal);
		
		
	}
	
	public void setModal() {
		this.setModal (sModal);
	
	}
}