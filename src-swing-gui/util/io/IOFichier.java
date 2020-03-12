package util.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.AccessControlException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.ctom.hulis.util.io.HuckelIO;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.gaussian.IOReadGaussian;
import util.languages.LanguageManager;
import util.test.IOReadGaussianTest;
import util.xml.IOReadXML;
import util.xyz.IOReadxyz;
import views.FrameApp;
import views.prefs.PreferencesGUI;
import access.Rights;
import controller.globalCommands.IGlobalCommands;
import controller.globalCommands.ReadGaussianCommand;
import controller.globalCommands.ReadXMLCommand;
import controller.globalCommands.ReadXYZCommand;

public class IOFichier implements ActionListener {
	FrameApp app;
	public static File DEFAULT_DIRECTORY = null;;

	public IOFichier(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		try {
			final JFileChooser choix = new JFileChooser();
			XyzFilter xyzFileFilter = new XyzFilter();
			GaussianFilter gaussianFilter = new GaussianFilter();
			if (Rights.ALLOW_SAVE_TEST_FILE_FORMAT ) {
				choix.addChoosableFileFilter(new GaussianTestFilter());

			}
			int option;

			// se repositionne sur le dernier repertoire utilise la fois d'avant
			if (DEFAULT_DIRECTORY != null)
				choix.setCurrentDirectory(DEFAULT_DIRECTORY);

			// sauvegarde le repertoire courant pour eviter a l'utilisateur d'avoir a reparcourir l'arboressence 
			choix.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					DEFAULT_DIRECTORY = choix.getCurrentDirectory();
				}

			});

			if (arg0.getActionCommand() == IGlobalCommands.OPEN) {
				choix.addChoosableFileFilter(xyzFileFilter);
				choix.addChoosableFileFilter(gaussianFilter);
				if (Rights.ALLOW_XML_FILE_FORMAT )
					choix.addChoosableFileFilter(new XmlFilter());

				choix.setFileFilter(gaussianFilter);

				option = choix.showOpenDialog(this.app);
				if (option == JFileChooser.APPROVE_OPTION) { // si clic sur yes
					File file = choix.getSelectedFile();
					HuckelIO.log(this.getClass().getName(), "actionPerformed","try to read file : \n" + FilesUtil.fileToString(file));
					String type = FilesUtil.getExtension(file).toLowerCase();
					// ****** READ XYZ ******************/
					if (type.equals("xyz")) {

						/*		StreamTokenizer tokenizer2 = new StreamTokenizer(new FileReader(file));
						tokenizer2.eolIsSignificant(false);
						try {
							System.out.println("***BEGIN****");
							while (true) {
								int token = tokenizer2.nextToken();
								switch (token) {
								case StreamTokenizer.TT_EOF : System.out.println("<<EOF>>"); break;
								case StreamTokenizer.TT_EOL : System.out.println("<<EOL"); break;
								case StreamTokenizer.TT_NUMBER : System.out.print(tokenizer2.nval + " "); break;
								case StreamTokenizer.TT_WORD : System.out.print(tokenizer2.sval + " "); break;
								default :System.out.println("<< ??? >>");  break;
								}
								if (token ==  StreamTokenizer.TT_EOF)
									break;

							}
							System.out.println("***END****");
						} catch (IOException e1) {
							System.out.println("***ERROR!!****");
							e1.printStackTrace();
						}*/


						ReadXYZCommand ReadXYZCommand = new ReadXYZCommand(this.app, new IOReadxyz(file));
						ReadXYZCommand.execute();
						if (!ReadXYZCommand.isSucceed())
							MyJOptionPane.showMessageDialog(this.app, ReadXYZCommand.getFailReason(), "Read XYZ error", JOptionPane.INFORMATION_MESSAGE);


						// ****** READ COM ******************/
					} else if (type.equals("com")) {
						ReadGaussianCommand readGaussianCommand = new ReadGaussianCommand(this.app, new IOReadGaussian(file));
						readGaussianCommand.execute();
						if (!readGaussianCommand.isSucceed())
							MyJOptionPane.showMessageDialog(this.app, readGaussianCommand.getFailReason(), "Read Gaussian error", JOptionPane.INFORMATION_MESSAGE);

					} else if (type.equals("test")) {
						ReadGaussianCommand readGaussianCommand  = new ReadGaussianCommand(this.app,new IOReadGaussianTest(file)); 
						readGaussianCommand.execute();
						if (!readGaussianCommand.isSucceed())
							MyJOptionPane.showMessageDialog(this.app, readGaussianCommand.getFailReason(), "Read Gaussian error", JOptionPane.INFORMATION_MESSAGE);

						// ****** READ XML ******************/
					} else if (type.equals("xml")) {
						new ReadXMLCommand(this.app,
								new IOReadXML(file)).execute();
						// ****** READ OTHER ******************/
					} else {
						System.out.println("extension not recognized |" + type
								+ "|");
					}


				}
			} else if (arg0.getActionCommand() == IGlobalCommands.SAVE) {
				choix.addChoosableFileFilter(xyzFileFilter);
				choix.addChoosableFileFilter(gaussianFilter);
				choix.addChoosableFileFilter(new ImagePNGFilter());
				choix.addChoosableFileFilter(new ImageSVGFilter());

				choix.setFileFilter(gaussianFilter);

				option = choix.showSaveDialog(this.app);

				if (option == JFileChooser.APPROVE_OPTION) {
					String ext = "." +  ((HasDefaultExtension) choix.getFileFilter()).getDefaultExtension().toLowerCase();
					if (!choix.getSelectedFile().getPath().toLowerCase().endsWith(ext)) {
						JOptionPane.showMessageDialog(this.app, bundle.getString("krenamefile") + " " + choix.getSelectedFile().getName() + ext);
						choix.setSelectedFile(new File(choix.getSelectedFile().getPath() + ext));
					}	
					HuckelIO.log(this.getClass().getName(), "actionPerformed", "Save file " + choix.getSelectedFile());

					new SaveFile(this.app, choix.getSelectedFile());
				}
			} else if (arg0.getActionCommand() == IGlobalCommands.PRINT) {
				choix.addChoosableFileFilter(new ImagePNGFilter());
				choix.addChoosableFileFilter(new ImageSVGFilter());
				option = choix.showSaveDialog(this.app);
				if (option == JFileChooser.APPROVE_OPTION) {
					String ext="";
					if (choix.getSelectedFile().getName().endsWith(".png")) ext="png";
					if (choix.getSelectedFile().getName().endsWith(".svg")) ext="svg";
					PreferencesGUI.setPrinting(choix.getSelectedFile().getAbsolutePath(), choix.getSelectedFile().getName(), ext);
				}
			} else {
				throw new Exception("command not found");
			}
		} catch (FileNotFoundException e) {
			MyJOptionPane.showMessageDialog(this.app, e.getMessage(), bundle.getString("kfilenotfound"), JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
			HuckelIO.error(this.getClass().getName(), "actionPerformed", "Save file FileNotFoundException");

		} catch (AccessControlException e) {
			MyJOptionPane.showMessageDialog(this.app, bundle
					.getString("kinaccess"), bundle.getString("knotavalaible"),
					JOptionPane.INFORMATION_MESSAGE);
			e.printStackTrace();
			HuckelIO.error(this.getClass().getName(), "actionPerformed", "Save file AccessControlException");
		} catch (Exception e) {
			MyJOptionPane.showMessageDialog(this.app, e.getMessage(), "read file error", JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
			HuckelIO.error(this.getClass().getName(), "actionPerformed", "Save file unknown exception");
		} 
	}
}
// yannick 10/09/07 fin
