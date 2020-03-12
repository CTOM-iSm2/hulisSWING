/**
 * 
 */
package views;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JApplet;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import netscape.javascript.JSObject;
import util.CookieManager;
import util.ThreadUtils;

/**
 * @author macbook
 *
 */
public class HulisLauncher extends JApplet {

	@Override
	public void init(){
		final CookieManager cookieManager= new CookieManager(this);
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {
			public void run() {
				FrameApp app = AppletToFrame.createGUI(getCodeBase(), getParameter("time"), getParameter("browser"), cookieManager);
				Frame frame =  JOptionPane.getFrameForComponent(app);
				if (frame != null) {
					frame.setVisible(true);
					frame.addWindowListener(new WindowAdapter(){
						public void windowOpened(WindowEvent e){
							JOptionPane pane = new JOptionPane();
							final JDialog dialog = pane.createDialog("Please Wait");
							Timer timer = new Timer(50, new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									dialog.dispose();
								}
							});
							timer.setRepeats(false);
							timer.start();
							dialog.setVisible(true);
						}
					});

				}

			}
		});


	}

	@Override
	public void start() {
		this.restoreImageStartButton();
	}	

	/*@Override
	public void stop() {
	    this.restoreImageStartButton();
	}*/

	public void restoreImageStartButton() {
		try {
			JSObject window = JSObject.getWindow(this);
			window.eval("RestoreImage()");

		}
		catch (Exception e){
			e.printStackTrace();
		}

	}


}
