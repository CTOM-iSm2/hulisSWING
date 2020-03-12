package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import util.ComponentsTools;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;

@SuppressWarnings("serial")
class SplashWindow extends JWindow {

	FrameApp app;
	MyResourceBundle bundle;
	Thread splashThread ;
	
	public SplashWindow(final FrameApp app, int waitTime) {
		super((JFrame) null);

		this.app = app;

		try {
			setAlwaysOnTop(true);
		} catch (SecurityException e3) {
			SwingIO.warning(this.getClass().getName(), "SplashWindow",
					"AlwaysOnTop does not work in the online version", e3);

		} catch (Exception e4) {
			SwingIO.warning(this.getClass().getName(), "SplashWindow",
					"AlwaysOnTop does not work in the online version", e4);
		}

		this.bundle = LanguageManager.getInstance().getResource("About");
		int aboutWidth = (Integer) (this.bundle.getObject("ksplashWidth"));
		int aboutHeight = (Integer) (this.bundle.getObject("ksplashHeight"));

		AboutPanel bigPanel = new AboutPanel(app, this);
		bigPanel.imgLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				close();
			}
		});

		bigPanel.aboutLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				close();
			}
		});
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout());
		contenu.add(bigPanel, BorderLayout.CENTER);
		pack();
		this.setSize(new Dimension(aboutWidth, aboutHeight));

		this.setLocationRelativeTo(app);

		// rend le splash screen invisible lorsque l'on clique dessus
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				close();

			}
		});

		// afin d'acceder � la valeur WaitTime
		final int pause = waitTime;

		// thread pour fermer le splash screen
		final Runnable closerRunner = new Runnable() {
			public void run() {
				close();
			}
		};

		Runnable waitRunner = new Runnable() {
			public void run() {
				try {
					Thread.sleep(pause);
					// lance le thread qui ferme le splash screen
					SwingUtilities.invokeAndWait(closerRunner);
				} catch(InterruptedException e) {
					SwingIO.log("SplasScreen", "close timer", "timer interruption ok");
				} catch (Exception e) {
					SwingIO.error(this.getClass().getName(), "run", e
							.getMessage(), e);

				}
			}
		};

		// affiche le splash screen
		setVisible(true);

		// lance le thread qui ferme le splash screen apres un certain temps
		splashThread = new Thread(waitRunner, "SplashThread");
		if (app.getRights().isValidUser())
			splashThread.start();
	}
	
	protected void close() {
		splashThread.interrupt();
		setVisible(false);
		dispose();
		app.displayLicenseToAccept();
	}
}