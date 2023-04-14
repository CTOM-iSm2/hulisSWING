package util.io;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import javax.swing.JOptionPane;

import org.ctom.hulis.files.gamess.GamessReadXYZ;
import org.ctom.hulis.files.gaussian.Gaussian03ReadXYZ;
import org.ctom.hulis.files.gaussian.NRTSTRreader;
import org.ctom.hulis.files.gaussian.ReadGaussian;
import org.ctom.hulis.files.xyz.ReadCoordinatesxyz;
import org.ctom.hulis.files.xyz.Readxyz;

import controller.globalCommands.ICommand;
import controller.globalCommands.ReadGaussianCommand;
import controller.globalCommands.ReadNRTSTRCommand;
import controller.globalCommands.ReadXYZCommand;
import views.FrameApp;

public class IOClipBoard implements ActionListener, ClipboardOwner {
	FrameApp app;
	String failMessage;

	public IOClipBoard(FrameApp app) {
		this.app = app;
		this.failMessage = "";
	}

	// try command and if it fails returns the error message;
	public boolean tryCommand(String format, ICommand command) {
		try {
			SwingIO.log(this.getClass().getName(),"tryCommand", "try to interpret format : " + format);
			command.execute();
			if (!command.isSucceed()) {
				this.failMessage += "\n Not " + format + " format : " + command.getFailReason();
				return false;
			}
		} catch (NullPointerException e1) {
			SwingIO.warning(this.getClass().getName(), "actionPerformed",
					"paste an empty clipboard !", e1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	public void actionPerformed(ActionEvent arg0) {
		SwingIO.log(this.getClass().getName(),"actionPerformed", "try to read clipboard content : \n" + getClipboardContents());
		
		if (!tryCommand("NRT string for NBO", new ReadNRTSTRCommand(this.app, new NRTSTRreader(getClipboardContents()))))						
			if (!tryCommand("xyz", new ReadXYZCommand(this.app, new Readxyz(new StreamTokenizer(new StringReader(getClipboardContents()))))))
				if (!tryCommand("Gaussian03 xyz", new ReadXYZCommand(this.app, new Gaussian03ReadXYZ(getClipboardContents()))))
					if (!tryCommand("Gamess xyz", new ReadXYZCommand(this.app, new GamessReadXYZ(getClipboardContents()))))
						if (!tryCommand("xyz coordinates", new ReadXYZCommand(this.app, new ReadCoordinatesxyz(getClipboardContents()))))						
							if (!tryCommand("Gaussian", new ReadGaussianCommand(this.app, new ReadGaussian(getClipboardContents()))))
								JOptionPane.showMessageDialog(this.app, "Not recognized format." + failMessage, "Clipboard paste", JOptionPane.INFORMATION_MESSAGE);

		this.failMessage = "";

	}

	public String getClipboardContents() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		// odd: the Object param of getContents is not currently used
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null)
				&& contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents
						.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException ex) {
				// highly unlikely since we are using a standard DataFlavor
				SwingIO.PrintIf(ex);
				ex.printStackTrace();
			} catch (IOException ex) {
				SwingIO.PrintIf(ex);
				ex.printStackTrace();
			}
		}
		return result;
	}

	public void lostOwnership(Clipboard arg0, Transferable arg1) {
		// TODO Auto-generated method stub
	}
}
// yannick 10/09/07 fin
