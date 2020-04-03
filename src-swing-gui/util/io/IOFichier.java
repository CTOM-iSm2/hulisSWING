package util.io;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.security.AccessControlException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.util.io.HuckelIO;

import access.Rights;
import controller.globalCommands.ActionStopConsumingProcess;
import controller.globalCommands.Center;
import controller.globalCommands.EnableActionManager;
import controller.globalCommands.IGlobalCommands;
import controller.globalCommands.ReadGaussianCommand;
import controller.globalCommands.ReadXMLCommand;
import controller.globalCommands.ReadXYZCommand;
import util.MyJOptionPane;
import util.MyResourceBundle;
import util.gaussian.IOReadGaussian;
import util.languages.LanguageManager;
import util.test.IOReadGaussianTest;
import util.xml.IOReadXML;
import util.xyz.IOReadxyz;
import views.FrameApp;
import views.IMesomeryReference;
import views.IStructureReference;
import views.prefs.PreferencesGUI;

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
			HulisFilter hulisFileFilter = new HulisFilter();
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
				choix.addChoosableFileFilter(hulisFileFilter);
				choix.addChoosableFileFilter(xyzFileFilter);
				choix.addChoosableFileFilter(gaussianFilter);
				if (Rights.ALLOW_XML_FILE_FORMAT )
					choix.addChoosableFileFilter(new XmlFilter());

				choix.setFileFilter(hulisFileFilter);

				option = choix.showOpenDialog(this.app);
				if (option == JFileChooser.APPROVE_OPTION) { // si clic sur yes
					File file = choix.getSelectedFile();
					HuckelIO.log(this.getClass().getName(), "actionPerformed","try to read file : \n" + FilesUtil.fileToString(file));
					String type = FilesUtil.getExtension(file).toLowerCase();
					HuckelIO.log(this.getClass().getName(), "actionPerformed","file extension=|" + type + "|");
					
					// ****** READ XYZ ******************/
					if (type.equals("xyz")) {

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
					}
					else if (type.equals("hulis")) {
					     System.out.println("DESERIALISATION");

					     FileInputStream fis = new FileInputStream(file);
					     ObjectInputStream ois = new ObjectInputStream(fis);
					     Mesomery mesomery = (Mesomery) ois.readObject(); 
					     System.out.println("NB ATOMES= " + mesomery.getDelocalizedStructure().countAtoms());
					     
					     List<IMethod> lstCurrentSelectedMethods = this.app.getListSelectedMethods();
					     this.app.getStateManager().initNewState();
					
					     this.app.setMesomery(mesomery, "", "");
						
					     // update views and center molecules
					     this.app.getCurrentMesomeryView().rebuild();

					     // on restaure les methodes qu'on avait sauvegardees au debut del a commande
					     for (IMethod method : lstCurrentSelectedMethods) 
						 this.app.selectMethod(method.getName(), true);
				
				
					  // select the structures with the greatest value		
					mesomery.setSelectionTreshold(mesomery.getStructureSelector().getMaximalValue());
				
					// on le fait une deuxieme fois car la premiere fois aucune structure n'est selectionne donc le treshold etait a zero
					mesomery.setSelectionTreshold(mesomery.getStructureSelector().getMaximalValue());
	
					for (Structure s : mesomery.getAllStructures()) {
						if (!s.isCloneResultsCacheWhenCloneStructure())
							s.calculate();
					}
					Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
					stop.join();

					new Thread (new Runnable() {

						public void run() {
							try {
							    mesomery.calculate();
							} catch (IncorrectNumberPiElectronsException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (MesomeryNoStructureLocalizedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CoupleException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							catch (Exception e) {
								e.printStackTrace();
								mesomery.stopConsumingProcess();
								mesomery.forceAutoGenerating(false);
								mesomery.forceCalculatingWeights(false);
								SwingIO.error(this.getClass().getName(), "restoreState", "mesomery compute error when restore state", e);
								MyJOptionPane.showMessageDialog(app, "Unexpected compute error during undo/redo operation", "Error", JOptionPane.ERROR_MESSAGE);

							}		
						}}).start();


					     new Center (this.app.getCurrentMesomeryView()).execute();
					     this.app.getStateManager().saveState();

					     ois.close();				
						// ****** READ XML ******************/
					} else if (type.equals("xml")) {
						new ReadXMLCommand(this.app,
								new IOReadXML(file)).execute();
						// ****** READ OTHER ******************/
					} else {
						System.out.println("Extension not recognized |" + type + "|");
					}


				}
			} else if (arg0.getActionCommand() == IGlobalCommands.SAVE) {
				choix.addChoosableFileFilter(hulisFileFilter);
				choix.addChoosableFileFilter(xyzFileFilter);
				choix.addChoosableFileFilter(gaussianFilter);
				choix.addChoosableFileFilter(new ImagePNGFilter());
				choix.addChoosableFileFilter(new ImageSVGFilter());

				choix.setFileFilter(hulisFileFilter);

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
