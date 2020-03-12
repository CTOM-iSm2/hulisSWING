package views;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import util.ComponentsTools;
import util.CookieManager;
import controller.globalCommands.ActionQuit;

public class AppletToFrame {
	

	public static FrameApp createAndShowGUI(final boolean splashScreen ) {
		final FrameApp app = new FrameApp();

		//	ThreadUtils.invokeAsSoonAsPossible(new Runnable() {
		//        public void run() {

		app.setOwnedToFrame(true);
		app.setParameter("browser", "");
		new AppletToFrame(app, null);
		JOptionPane.getFrameForComponent(app).setVisible(true);
		if (splashScreen)
			app.displaySplashscreen();
		//      }
		// });

		return app;

	}


	public static FrameApp createGUI(URL codebase, String time, String browser, CookieManager cookieManager ) {
		final FrameApp app = new FrameApp();
		app.setOwnedToFrame(true);
		app.setParameter("time", time);
		app.setParameter("browser", browser);
		app.setCookieManager(cookieManager);
		new AppletToFrame(app, codebase);
		app.displayLicenseToAccept();
		return app;
	}


	private  AppletToFrame (final FrameApp app, URL codebase) {

		FrameApp.checkJavaVersion();
		app.setCodeBase(codebase);
		app.init();
		app.start();
	
		JFrame frame = new JFrame(FrameApp.ksoftname);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				try {
					new ActionQuit(app, 0).execute();
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
		});



		frame.add(app);

		frame.pack();
		frame.setSize ((int) app.getPreferredSize().getWidth(),(int) app.getPreferredSize().getHeight());
		frame.setPreferredSize(new Dimension((int) app.getPreferredSize().getWidth(),
				(int) app.getPreferredSize().getHeight() ) );
		frame.setMinimumSize(new Dimension((int) app.getPreferredSize().getWidth(),
				(int) app.getPreferredSize().getHeight() ));


		frame.setLocationRelativeTo(null);


		app.startNewMesomery();



	}


	
}
