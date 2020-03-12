package multimedia;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class SousTitres extends JWindow implements Runnable {

	Container contenu;
	int delay;
	JTextField jtf;
	String string;
	Thread thread;

	public SousTitres(String s, int delay) {
		super((JFrame) null);
		this.thread = new Thread(this);
		this.string = s;
		this.delay = delay;
		this.jtf = new JTextField(this.string);
		setAlwaysOnTop(true);
		this.contenu = getContentPane();
		this.contenu.setLayout(new BorderLayout());
		this.contenu.add(this.jtf);
		pack();
		this.contenu.setVisible(true);
		this.thread.start();
	}

	public void run() {
		setVisible(true);
		try {
			Thread.sleep(this.delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setVisible(false);
	}

}
