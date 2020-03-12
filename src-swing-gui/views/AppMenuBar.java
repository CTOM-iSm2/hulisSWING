package views;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.ctom.hulis.huckel.comparators.MethodNameComparator;
import org.ctom.hulis.huckel.methods.IMethod;

import util.MyResourceBundle;
import util.languages.InitResources;
import util.languages.Language;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;
import access.Rights;
import controller.globalCommands.ChangeLanguage;
import controller.globalCommands.IGlobalCommands;

@SuppressWarnings("serial")
public class AppMenuBar extends JMenuBar  {
	public FrameApp app;
	public JMenu editMenu;
	public JMenu fileMenu;
	public JMenu helpMenu;
	public JMenu languageMenu;
	public JMenu displayMenu;

	private HashMap<String, JMenuItem> itemsMethods;
	public JMenuItem menuAbout;
	public JMenuItem menuLog;
	public JCheckBoxMenuItem menuExp;
	public JMenuItem menuGuide;
	public JMenuItem menuNew;
	public JMenuItem menuOpen;
	public JMenuItem menuPaste;
	public JMenuItem menuPreferences;
	public JMenuItem menuPrintCurrentView;
	public JMenuItem menuQuit;
	public JMenuItem menuRedo;
	public JMenuItem menuSave;
	public JMenuItem menuTutorial0;
	public JMenuItem menuUndo;
	public JMenuItem menuZoomD;
	public JMenuItem menuSinguletTriplet;
	public JMenuItem superMario;
	public JCheckBoxMenuItem menuSwitchNaturalCanonical;
	public JCheckBoxMenuItem menuSwitchDensity;
	public JCheckBoxMenuItem menuOptGeom;
	public JMenuItem menuSinguletCO;
	public JMenuItem menuExcitations;
	public JMenuItem menuBlocs;
	public JMenuItem menuDisplayPolynomes;
	public JMenuItem menuAddPolynome;
	public JMenuItem menuZoomM;
	public JMenuItem menuZoomP;
	public JMenu computeMenu;
	public JMenu tutorialMenu;
	MyResourceBundle bundle;

	public void updateLanguage() {
		bundle = LanguageManager.getInstance().getResource("Menu");

		fileMenu.setText(bundle.getString("mnfile"));
		menuOpen.setText(bundle.getString("mnopen"));
		menuSave.setText(bundle.getString("mnsave"));
		menuPreferences.setText(bundle.getString("mnPreferences"));
		menuPrintCurrentView.setText("Print...");
		menuQuit.setText(bundle.getString("mnquit"));

		editMenu.setText(bundle.getString("mnedit"));
		menuUndo.setText(bundle.getString("mnundo"));
		menuRedo.setText(bundle.getString("mnredo"));
		menuPaste.setText(bundle.getString("mnpaste"));


		displayMenu.setText(bundle.getString("mndisplay"));
		this.menuExp.setText(bundle.getString("mnexp"));
		this.menuSinguletTriplet.setText(bundle.getString("mnmanipspin"));
		this.menuSwitchNaturalCanonical.setText("Nat");
		this.menuSwitchDensity.setText("Density");
		this.menuSinguletCO.setText(bundle.getString("mnmanipspinCO"));
		this.menuOptGeom.setText(bundle.getString("mnoptgeom"));
		this.menuDisplayPolynomes.setText (bundle.getString("mnpolynomes"));
		menuExcitations.setText(bundle.getString("mnExcitations"));
		menuBlocs.setText(bundle.getString("mnBlocs"));
		menuZoomD.setText(bundle.getString("mnzoomd"));	
		menuZoomP.setText(bundle.getString("mnzoomp"));
		menuZoomM.setText (bundle.getString("mnzoomm"));

		this.computeMenu.setText(bundle.getString("mncompute"));
		this.menuAddPolynome.setText(bundle.getString("mnaddpolynome"));

		this.tutorialMenu.setText(bundle.getString("mntutorials"));
		this.menuTutorial0.setText(bundle.getString("mntuto0"));

		this.helpMenu.setText(bundle.getString("mnhelp"));
		this.menuGuide.setText(bundle.getString("mnguide"));
		this.menuAbout.setText (bundle.getString("mnabout"));
		this.menuLog.setText (bundle.getString("mnlog"));

		Font btFont = (Font) bundle.getObject("mnfont");
		fileMenu.setFont(btFont);
		menuOpen.setFont(btFont);
		menuOpen.setFont(btFont);
		menuSave.setFont(btFont);
		menuPreferences.setFont(btFont);
		menuPrintCurrentView.setFont(btFont);
		menuQuit.setFont(btFont);
		editMenu.setFont(btFont);
		menuUndo.setFont(btFont);
		menuRedo.setFont(btFont);
		menuPaste.setFont(btFont);
		displayMenu.setFont(btFont);
		menuExp.setFont(btFont);
		menuSinguletTriplet.setFont(btFont);	
		menuSwitchNaturalCanonical.setFont(btFont);	
		menuSwitchDensity.setFont(btFont);	
		menuSinguletCO.setFont(btFont);
		menuOptGeom.setFont(btFont);	
		menuDisplayPolynomes.setFont(btFont);
		menuExcitations.setFont(btFont);
		menuBlocs.setFont(btFont);
		menuZoomD.setFont(btFont);
		menuZoomP.setFont(btFont);
		menuZoomM.setFont(btFont);
		computeMenu.setFont(btFont);
		menuAddPolynome.setFont(btFont);
		tutorialMenu.setFont(btFont);
		menuTutorial0.setFont(btFont);
		helpMenu.setFont(btFont);
		menuGuide.setFont(btFont);
		menuAbout.setFont(btFont);
		menuLog.setFont(btFont);
		for (JMenuItem item : this.getItemsdMethods()) {
			item.setFont(btFont);
		}

	}

	public AppMenuBar(FrameApp app) {
		super();
		this.app = app;
		this.itemsMethods = new HashMap<String, JMenuItem>();

		bundle = LanguageManager.getInstance().getResource("Menu");

		int defaultShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // raccourci menu selon l'OS (alt, pomme, etc...)

		// File menu
		this.fileMenu = new JMenu(bundle.getString("mnfile"));

		this.menuOpen = new JMenuItem( bundle.getString("mnopen"));
		menuOpen.setMnemonic(KeyEvent.VK_O);
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, defaultShortcutKeyMask));
		menuOpen.setActionCommand(IGlobalCommands.OPEN);

		this.menuSave = new JMenuItem (bundle.getString("mnsave"));
		menuSave.setMnemonic( KeyEvent.VK_S);
		menuSave.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_S,defaultShortcutKeyMask));
		menuSave.setActionCommand(IGlobalCommands.SAVE);

		this.menuPreferences = new JMenuItem( bundle.getString("mnPreferences"));

		this.menuPrintCurrentView = new JMenuItem("print");

		menuPrintCurrentView.setMnemonic(KeyEvent.VK_C);
		menuPrintCurrentView.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_C,defaultShortcutKeyMask));
		menuPrintCurrentView.setActionCommand(IGlobalCommands.PRINT);


		this.menuQuit = new JMenuItem( bundle.getString("mnquit"));
		menuQuit.setMnemonic(KeyEvent.VK_Q);
		menuQuit.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q,defaultShortcutKeyMask));

		// Edit Menu
		this.editMenu = new JMenu (bundle.getString("mnedit"));

		this.menuUndo = new JMenuItem (bundle.getString("mnundo"));
		menuUndo.setMnemonic(KeyEvent.VK_Z);
		menuUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultShortcutKeyMask));

		this.menuRedo = new JMenuItem (bundle.getString("mnredo"));
		menuRedo.setMnemonic(KeyEvent.VK_Y);
		menuRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultShortcutKeyMask));

		this.menuPaste = new JMenuItem (bundle.getString("mnpaste"));
		menuPaste.setMnemonic(KeyEvent.VK_V);
		menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultShortcutKeyMask));

		// display menu
		this.displayMenu = new JMenu(bundle.getString("mndisplay"));


		this.menuSinguletTriplet = new JMenuItem (bundle.getString("mnmanipspin"));
		menuSinguletTriplet.setMnemonic(KeyEvent.VK_T);
		menuSinguletTriplet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T,defaultShortcutKeyMask));

		this.superMario = new JMenuItem("Super Mario");
		superMario.setMnemonic(KeyEvent.VK_COMMA);
		superMario.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA,defaultShortcutKeyMask));

		this.menuSwitchNaturalCanonical = new JCheckBoxMenuItem ("Nat");
		menuSwitchNaturalCanonical.setMnemonic(KeyEvent.VK_N);
		menuSwitchNaturalCanonical.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,defaultShortcutKeyMask));

		this.menuSwitchDensity = new JCheckBoxMenuItem ("Density");
		menuSwitchDensity.setMnemonic(KeyEvent.VK_B);
		menuSwitchDensity.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,defaultShortcutKeyMask));

		this.menuOptGeom = new JCheckBoxMenuItem (bundle.getString("mnoptgeom"));
		menuOptGeom.setMnemonic(KeyEvent.VK_W);
		menuOptGeom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,defaultShortcutKeyMask));
		menuOptGeom.setVisible(false); // demande de Stephane : pas de meu optimiseur dans la barre de menu

		this.menuSinguletCO = new JMenuItem (bundle.getString("mnmanipspinCO"));
		menuSinguletCO.setMnemonic(KeyEvent.VK_R);
		menuSinguletCO.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, defaultShortcutKeyMask));

		this.menuDisplayPolynomes = new JCheckBoxMenuItem (bundle.getString("mnpolynomes"));

		this.menuExcitations = new JMenuItem (bundle.getString("mnExcitations"));

		this.menuBlocs = new JMenuItem (bundle.getString("mnBlocs"));

		this.menuZoomD = new JMenuItem (bundle.getString("mnzoomd"));
		menuZoomD.setMnemonic(KeyEvent.VK_D);
		menuZoomD.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				defaultShortcutKeyMask));

		this.menuZoomP= new JMenuItem (bundle.getString("mnzoomp"));
		menuZoomP.setMnemonic(KeyEvent.VK_P);
		menuZoomP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,defaultShortcutKeyMask));

		this.menuZoomM= new JMenuItem (bundle.getString("mnzoomm"));
		menuZoomM.setMnemonic(KeyEvent.VK_M);
		menuZoomM.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,defaultShortcutKeyMask));

		// compute

		this.computeMenu = new JMenu (bundle.getString("mncompute"));

		this.menuExp = new JCheckBoxMenuItem (bundle.getString("mnexp"));

		this.menuAddPolynome = new JMenuItem (bundle.getString("mnaddpolynome"));
		menuAddPolynome.setMnemonic(KeyEvent.VK_R);
		menuAddPolynome.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_R,
				ActionEvent.ALT_MASK));

		IMethod[] methods = FrameApp.getImplementedMethods();
		Arrays.sort(methods, new MethodNameComparator());
		for (IMethod method : methods) {
			JMenuItem item = new JCheckBoxMenuItem(method.getName());
			this.itemsMethods.put(method.getName(), item);
		}


		// Menu Tutorials
		this.tutorialMenu = new JMenu (bundle.getString("mntutorials"));
		this.menuTutorial0 = new JMenuItem (bundle.getString("mntuto0"));

		// Help Menu
		this.helpMenu = new JMenu(bundle.getString("mnhelp"));
		this.menuGuide = new JMenuItem (bundle.getString("mnguide"));

		this.menuAbout = new JMenuItem (bundle.getString("mnabout"));
		this.menuLog = new JMenuItem (bundle.getString("mnlog"));


		// langage menu 

		Language language = (Language)(bundle.getObject("mdefaultlocale"));
		this.languageMenu = new JMenu ();
		this.languageMenu.setIcon(language.getFlag());

		this.fileMenu.add(this.menuOpen);
		if (app.isExecutedOnInternet())
			this.menuOpen.setVisible(false);

		this.fileMenu.add(this.menuSave);

		if (app.isExecutedOnInternet())
			this.menuOpen.setVisible(false);

		this.fileMenu.addSeparator();
		this.fileMenu.add(this.menuPreferences);
		this.fileMenu.add(this.menuPrintCurrentView);
		this.fileMenu.add(this.menuQuit);
		this.add(this.fileMenu);

		this.editMenu.add(this.menuUndo);
		this.editMenu.add(this.menuRedo);
		if (!(app.isExecutedOnInternet()))
			this.editMenu.addSeparator();
		this.editMenu.add(this.menuPaste);


		if (app.isExecutedOnInternet())
			this.menuPaste.setVisible(false);
		this.add(this.editMenu);

		this.displayMenu.add(this.menuSinguletTriplet);
		this.displayMenu.add(this.menuSwitchNaturalCanonical);
		this.displayMenu.add(this.menuSwitchDensity);
		this.displayMenu.add(this.menuOptGeom);
		this.displayMenu.add(this.menuSinguletCO);
		this.displayMenu.add(this.superMario);
		try {
			PreferencesGUI.setSuperMario(System.getProperty("user.name").equals("yannick"));
			PreferencesGUI.setSuperMario(System.getenv("SUPERMARIO").equals("bump"));
		} catch (Exception e) {
			//nothing to do: probablement une exception de securite.
		}
		this.superMario.setVisible(PreferencesGUI.getSuperMario());
		if (Rights.ALLOW_POLYNOMES)
			this.displayMenu.addSeparator();
		this.displayMenu.add(this.menuDisplayPolynomes);

		if(!Rights.ALLOW_POLYNOMES) 
			this.menuDisplayPolynomes.setVisible(false);

		if(!Rights.ALLOW_EXCITATIONS) {
			this.menuSinguletTriplet.setVisible(false);
			this.menuSinguletCO.setVisible(false);
		}

		this.displayMenu.add(this.menuExcitations);
		if(!Rights.ALLOW_EXCITATIONS)
			this.menuExcitations.setVisible(false);

		this.displayMenu.add(this.menuBlocs);
		if(!Rights.ALLOW_BLOCKS)
			this.menuBlocs.setVisible(false);

		if (Rights.ALLOW_BLOCKS || Rights.ALLOW_EXCITATIONS || Rights.ALLOW_POLYNOMES)
			this.displayMenu.addSeparator();
		this.displayMenu.add(this.menuZoomD);
		this.displayMenu.add(this.menuZoomP);
		this.displayMenu.add(this.menuZoomM);
		this.add(this.displayMenu);

		this.computeMenu.add(this.menuExp);
		this.computeMenu.addSeparator();
		this.computeMenu.add(this.menuAddPolynome);

		if (!Rights.ALLOW_POLYNOMES)
			this.menuAddPolynome.setVisible(false);

		if (Rights.ALLOW_POLYNOMES)
			this.computeMenu.addSeparator();
		for (IMethod method : methods)
			this.computeMenu.add(this.itemsMethods.get(method.getName()));
		this.add(this.computeMenu);

		this.tutorialMenu.add(this.menuTutorial0);
		this.add(this.tutorialMenu);

		this.helpMenu.add(this.menuGuide);
		this.helpMenu.addSeparator();
		this.helpMenu.add(this.menuLog);
		this.helpMenu.add(this.menuAbout);
		this.add(this.helpMenu);


		for (final Language l : InitResources.languages) {
			JMenuItem languageItem = new JMenuItem (l.toString(), l.getFlag());
			if (l.getLocale().getLanguage().equals ("ar")) 
				languageItem.setFont((Font) bundle.getObject("mnfontAr"));
			else
				languageItem.setFont((Font) bundle.getObject("mnfont"));
			languageItem.addActionListener(new ChangeLanguage(this.app, l));
			this.languageMenu.add(languageItem);

		}

		this.add(this.languageMenu);

		this.menuExcitations.setVisible(this.app.isExpertMode());
		this.menuBlocs.setVisible(this.app.isExpertMode());

		this.updateLanguage();

	}

	/**
	 * 
	 * @param methodName
	 * @return
	 */
	public JMenuItem getItemMethod(String methodName) {
		return this.itemsMethods.get(methodName);
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	public String getMethodNameByItem(JMenuItem item) {
		for(String o:this.itemsMethods.keySet()){
			if(itemsMethods.get(o).equals(item)) {
				return o;
			}
		}
		return null;

	}

	/**
	 * 
	 * @return
	 */
	public JMenuItem[] getItemsdMethods() {
		return this.itemsMethods.values().toArray(
				new JMenuItem[this.itemsMethods.size()]);
	}



}
