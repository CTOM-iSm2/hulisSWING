package robots;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;

import multimedia.Multimedia;
import multimedia.SousTitres;
import sun.awt.SunToolkit;
import views.DialogChangeAtom;
import views.DialogChangeHuckelBond;
import views.FrameApp;
import access.Rights;

public abstract class Tutorial extends Robot implements ActionListener, Runnable {
	
	public static final boolean FORCE_MOVE_FAST_INSTEAD_SLOW = Rights.MOVE_MOUSE_QUICKLY_DURING_TESTS;

	protected FrameApp applet;

	private static SunToolkit toolkit;
	protected static Point CurrentMousePosition;
	protected static final int INFINITE_DELAY = 6000;
	protected static final int LONG_DELAY = 50;
	protected static final int NORMAL_DELAY = 5;
	protected static final int RAPID_DELAY = 1;
	protected static final int VERYLONG_DELAY = 500;


	public void actionPerformed (ActionEvent e) {
		Thread t = new Thread(this);
		t.start();
	}
	
	public void run() {
		setAutoDelay(Tutorial.NORMAL_DELAY);
		go();
	}
	
	public DialogChangeAtom getDialogChangeAtom () {
		for (Window w : JDialog.getWindows()) {
    		if ( w instanceof DialogChangeAtom && w.isVisible()) {
    			return (DialogChangeAtom) w;
    		}
    	}
		return null;
	}
	
	public DialogChangeHuckelBond  getDialogChangeHuckelBond () {
		for (Window w : JDialog.getWindows()) {
    		if ( w instanceof DialogChangeHuckelBond && w.isVisible()) {
    			return (DialogChangeHuckelBond) w;
    		}
    	}
		return null;
	}
	
	
	public void sleep (int delay) {
		super.delay(delay);
	}
	
	public Tutorial(FrameApp applet) throws AWTException {
		super();
		//this.waitForIdle(); // on attend que l'applet soit affichee entierement : je sais pas si ca marcche 
		this.applet = applet;
		Tutorial.CurrentMousePosition = new Point();
		this.mouseMove(0,0);
		toolkit = (SunToolkit) Toolkit.getDefaultToolkit();
	}

	

	public abstract void go();

	

	
	synchronized public void mouseLeftSimpleClick() {
		SousTitres st = new SousTitres("Click", Tutorial.VERYLONG_DELAY);
		mousePress(InputEvent.BUTTON1_MASK);
		if (!SwingUtilities.isEventDispatchThread())
			this.waitForIdle();
		else
			sleep(2000);
			
		mouseRelease(InputEvent.BUTTON1_MASK);
		if (!SwingUtilities.isEventDispatchThread())
			this.waitForIdle();
		else
			sleep(2000);

		syncAndDelay();
	}

	private void mouseMove(double x, double y) {
		this.mouseMove((int) x, (int) y);
	}

	@Override
	synchronized public void mouseMove(int x, int y) {
		super.mouseMove(x, y);
		Tutorial.CurrentMousePosition.setLocation(x, y);
	}

	public void mouseMove(Point point) {
		// System.out.print("Move to : "+point.toString());
		this.mouseMove(point.getX(), point.getY());
	}

	synchronized public void mouseMoveSlowly(int x, int y) {
		
		if (FORCE_MOVE_FAST_INSTEAD_SLOW){
			mouseMove (new Point(x, y));
			return;
		}
		
		Point target = new Point(x, y);
		double dx = 1;
		double dy = 1;
		while (((int) dx != 0) || ((int) dy != 0)) {
			dx = Math.signum((int) target.getX()
					- Tutorial.CurrentMousePosition.getX());
			dy = Math.signum((int) target.getY()
					- Tutorial.CurrentMousePosition.getY());
			Point nextPoint = new Point();
			nextPoint.setLocation(Tutorial.CurrentMousePosition.getX() + dx,
					Tutorial.CurrentMousePosition.getY() + dy);
			this.mouseMove(nextPoint);
	//		System.out.print(Tutorial.CurrentMousePosition.getX() + " "
	//				+ Tutorial.CurrentMousePosition.getY() + "\n");
		}
	}

	public void mouseMoveSlowly(Point p) {
		this.mouseMoveSlowly((int) p.getX(), (int) p.getY());
	}

	@Override
	synchronized public void mousePress(int buttons) {
		Multimedia m;
		try {
			m = new Multimedia("src-swing-gui/audio/simpleclick.wav");
			m.playwav();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setAutoDelay(Tutorial.VERYLONG_DELAY);
		super.mousePress(buttons);
		setAutoDelay(Tutorial.NORMAL_DELAY);
	}

	

	public static void syncAndDelay() {
		 try {
			Thread.sleep(RAPID_DELAY);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!SwingUtilities.isEventDispatchThread())
			 toolkit.realSync();
		else
			try {
				Thread.sleep(LONG_DELAY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
