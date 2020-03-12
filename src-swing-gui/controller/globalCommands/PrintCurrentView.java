package controller.globalCommands;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import util.io.IOFichier;
import util.io.SwingIO;
import views.FrameApp;
import views.MesomeryView;
import views.prefs.PreferencesGUI;


/**
 * This command prints the current vuiew
 * 
 * @author ycarissan
 *
 */
public class PrintCurrentView implements ICommand, ActionListener {
	FrameApp app;
	String radical;

	public PrintCurrentView(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "print current view");

		SwingIO.PrintIfln("Print current view");

		MesomeryView view = this.app.getCurrentMesomeryView();
		BufferedImage img = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		this.app.getCurrentMesomeryView().paint(g);
		g.dispose();
		if (!PreferencesGUI.isPrintingSet()) {
			new IOFichier(app);
		}
		try {
			boolean found=true;
			int ifile=0;
			File file = null;
			while (found) {
				String strFile=PreferencesGUI.getDirPrint();
				int l=strFile.length();
				ifile=ifile+1;
				strFile = strFile.substring(0, l-4)+ifile+"."+PreferencesGUI.getExtPrint();
				file = new File(strFile);
				found=file.exists();
			}
			String extPrint = PreferencesGUI.getExtPrint();
			ImageIO.write(img,extPrint,file);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
