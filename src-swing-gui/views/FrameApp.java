package views;


import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.text.html.parser.ParserDelegator;

import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.methods.HLCIMethod;
import org.ctom.hulis.huckel.methods.HLPMethod;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.methods.NRTMethod;
import org.ctom.hulis.huckel.selectors.MesomeryHLPOverlapStructuresSelector;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import robots.TutorialBuildMolecule;
import sqlbridge.ConnectSQL;
import util.CookieManager;
import util.MyResourceBundle;
import util.PreferencesCookies;
import util.PreferencesStd;
import util.ThreadUtils;
import util.Version;
import util.io.IOClipBoard;
import util.io.IOFichier;
import util.io.SwingIO;
import util.languages.InitResources;
import util.languages.LanguageManager;
import util.polyCalculator.PolyCalculator;
import views.prefs.PreferencesGUI;
import access.Rights;
import controller.globalCommands.ActionAtomsRenum;
import controller.globalCommands.ActionAutoGenStruct;
import controller.globalCommands.ActionChangeMode;
import controller.globalCommands.ActionCommandRegistry;
import controller.globalCommands.ActionDisplayCharges;
import controller.globalCommands.ActionDisplayHxHxy;
import controller.globalCommands.ActionDisplayNum;
import controller.globalCommands.ActionDisplayPolynomes;
import controller.globalCommands.ActionDuplicate;
import controller.globalCommands.ActionEndBloc;
import controller.globalCommands.ActionErase1;
import controller.globalCommands.ActionEraseAll;
import controller.globalCommands.ActionEraseMesomery;
import controller.globalCommands.ActionMenuMethod;
import controller.globalCommands.ActionModeBloc;
import controller.globalCommands.ActionMoleculeAddElecCharge;
import controller.globalCommands.ActionMoleculeRemoveElecCharge;
import controller.globalCommands.ActionOpenAbout;
import controller.globalCommands.ActionOpenLog;
import controller.globalCommands.ActionOptimizeGeometry;
import controller.globalCommands.ActionQuit;
import controller.globalCommands.ActionSavePolynomes;
import controller.globalCommands.ActionShowResultMesomery;
import controller.globalCommands.ActionShowResultStruct;
import controller.globalCommands.ActionStopAutoGenStruct;
import controller.globalCommands.Center;
import controller.globalCommands.ChooseMenuExp;
import controller.globalCommands.EnableActionManager;
import controller.globalCommands.IGlobalCommands;
import controller.globalCommands.OpenPreferences;
import controller.globalCommands.PrintCurrentView;
import controller.globalCommands.Redo;
import controller.globalCommands.ShowListBlocs;
import controller.globalCommands.ShowListExcitations;
import controller.globalCommands.StateManager;
import controller.globalCommands.SuperMario;
import controller.globalCommands.SwitchCanonicalNatural;
import controller.globalCommands.SwitchDensity;
import controller.globalCommands.SwitchSinguletCouchesOuvertes;
import controller.globalCommands.SwitchSinguletTriplet;
import controller.globalCommands.Undo;
import controller.globalCommands.ZoomD;
import controller.globalCommands.ZoomM;
import controller.globalCommands.ZoomP;
import controller.structureViewController.AddState;
import controller.structureViewController.BlocState;
import controller.structureViewController.ChangeElectronsState;
import controller.structureViewController.ChangeState;
import controller.structureViewController.CoupleState;
import controller.structureViewController.IStructureViewControllerState;
import controller.structureViewController.MoveState;
import controller.structureViewController.RemoveState;
import controller.structureViewController.RenumState;
import controller.structureViewController.RotateState;

/**
 * FrameApp : This class is the principal frame view
 * 
 * @version 1.1
 * @since
 * @author Nicolas GOUDARD
 */

@SuppressWarnings("serial")
public class FrameApp extends JApplet implements IGlobalCommands {
	
	private URL codebase = null;

	private static String PREFERENCES_NODE_NAME = "org.ctom.hulis.hulisSwingGui.PreferencesGUI";

	public final static DecimalFormat form = new DecimalFormat("#.##");

	public final static DecimalFormat energyAlphaFormat = new DecimalFormat("#.##");
	public final static DecimalFormat energyBetaFormat = new DecimalFormat("0.00");
	public static final SwingEnergyFormater DEFAULT_ENERGY_FORMATER = new SwingEnergyFormater (energyAlphaFormat, energyBetaFormat);



	public final static DecimalFormat form2 = new DecimalFormat("#.###");
	public static final Color HUCKEL_VIEWS_BACK_COLOR = new Color(102, 204, 255);
	public static final int TIMER_HIT_USE_TIME_DURATION = 30000; // indique tous les combien de temps mettre a jour la duree d'utilisation dans la base
	public static final String kauthors = "<a href=\"http://ism2.univ-amu.fr/permanents/GOUDARD.php\" target=\"new\">Nicolas Goudard</a>, <a href=\"http://ism2.univ-amu.fr/permanents/CARISSAN.php\" target=\"new\">Yannick Carissan</a>, <a href=\"http://ism2.univ-amu.fr/permanents/HAGEBAUM-REIGNIER.php\" target=\"new\">Denis Hagebaum-Reignier</a>, <a href=\"http://ism2.univ-amu.fr/permanents/HUMBEL.php\" target=\"new\">St&eacute;phane Humbel</a>";

	public static final String ksoftname = "HuLiS";

	private static HashMap<String, IMethod> lstImplementedMethods = new HashMap<String, IMethod>();
	public static final float majorJavaVersion = FrameApp
			.getMajorJavaVersion(System.getProperty("java.version"));
	public static final Color MESOMERY_VIEWS_BACK_COLOR = new Color(255, 204,
			102);
	public static float MINIMAL_REQUIRED_JAVA_VERSION = 1.5f;
	protected Rights rights = null;
	private long timeBeginUse ;
	private long timeToReady;
	private long timeToStart;
	public String currentCommand;
	public long id;
	private static boolean isAlreadyRegisterDeconnect ; // pour eviter de mettre un 0 dans la base hulisusers / champ error :
	public final static int splashtime = 6000;
	
	public static final int PRECISION = 2;


	public static final boolean CLONE_RESULTS_WHEN_CLONE_MESOMERY = false;
	private Thread registerDurationThread;
	IStructureViewControllerState structureViewControllerState;
	StateManager stateManager;

	/*
	 * On ne peut lancer qu'une seule instance de l'applet car 
	 * ce sont les memes variables static qui sont utilisee quand on lance 
	 * plusieurs fois le navigateur
	 */


	final Version version = new Version(3, 4, 0, "c dev"); // VERSION OF HULIS


	static {
		FrameApp.lstImplementedMethods.put("HLCI", new HLCIMethod());
		FrameApp.lstImplementedMethods.put("HLP", new HLPMethod());
		//YC
		if (Rights.ALLOW_NRT)
			FrameApp.lstImplementedMethods.put("NRT", new NRTMethod());
		//YC
	}



	/**
	 * cannot run applet for old java version
	 */
	public static void checkJavaVersion() {
		if (FrameApp.majorJavaVersion < FrameApp.MINIMAL_REQUIRED_JAVA_VERSION) { 

			System.out
			.println("Java version too low. Please downlaod and install the last version of Java on http://www.java.com");
			System.exit(1);

		} // end if javaversion
	}

	
	/**
	 * 
	 * @return true if the applet is executed on internet
	 */
	public boolean isExecutedOnInternet() {
		URL url =  this.getCodeBase();

		if (url == null)
			return false;
		String sUrl = url.toString();
		return  (sUrl.startsWith("http://")  || sUrl.startsWith("https://") );
	}
	
	
	/* (non-Javadoc)
	 * @see java.applet.Applet#getCodeBase()
	 */
	@Override
	public URL getCodeBase() {
		try{
			if (this.codebase == null)
				this.codebase=super.getCodeBase();
		} catch (Exception e) {
			//SwingIO.log(this.getClass().getName(), "getCodeBase()", "codebase is null");
		}

		return this.codebase;
	}
	
	
	/**
	 * type of use
	 * @return 1=JAVA WEB or 4=JAVA STANDALONE
	 */
	public int getTypeOfUse() {
		int typeOfUse;
		
		if (this.isExecutedOnInternet())
			typeOfUse = 1;
		else
			typeOfUse = 4;
		
		return typeOfUse;

	}
	
	
	
	/**
	 * set to force the code base from the parent applet
	 */
	public void setCodeBase(URL codebase) {
		this.codebase=codebase;
	}
	
	


	/**
	 * 
	 * @return
	 */
	public StateManager getStateManager(){
		return this.stateManager;
	}


	public String getCurrentEnabledCommand() {
		Enumeration e = this.buttonGroup.getElements();
		AbstractButton b;
		while (e.hasMoreElements()) {
			b = (AbstractButton) e.nextElement();
			if (b.isEnabled() && b.getActionCommand().equals(this.currentCommand)) 
				return this.currentCommand;
		} // while
		return "";
	}

	public boolean isEnabledCurrentCommand() {
		Enumeration e = this.buttonGroup.getElements();
		AbstractButton b;
		while (e.hasMoreElements()) {
			b = (AbstractButton) e.nextElement();
			if (b.isEnabled() && b.getActionCommand().equals(this.currentCommand)) 
				return true;
		} // while
		return false;
	}


	public IStructureViewControllerState getCurrentStructureViewControllerState() {
		if (isEnabledCurrentCommand())
			return structureViewControllerState;
		return null;
	}

	/**
	 * 
	 * @param currentCommand
	 */
	public void setStructureViewControllerState(String currentCommand) {
		this.currentCommand = currentCommand;
		if (currentCommand.equals(IGlobalCommands.ADD))
			structureViewControllerState = new AddState();
		else if (currentCommand.equals(IGlobalCommands.CHANGE))
			structureViewControllerState = new ChangeState();
		else if (currentCommand.equals(IGlobalCommands.REMOVE))
			structureViewControllerState = new RemoveState();
		else if (currentCommand.equals(IGlobalCommands.MOVE))
			structureViewControllerState = new MoveState();
		else if (currentCommand.equals(IGlobalCommands.ROTATE))
			structureViewControllerState = new RotateState();
		else if (currentCommand.equals(IGlobalCommands.RENUM))
			structureViewControllerState = new RenumState();
		else if (currentCommand.equals(IGlobalCommands.CHANGE_ELECTRON))
			structureViewControllerState = new ChangeElectronsState();
		else if (currentCommand.equals(IGlobalCommands.COUPLE))
			structureViewControllerState = new CoupleState();
		else if (currentCommand.equals(IGlobalCommands.BLOC))
			structureViewControllerState = new BlocState();
		
		this.getStatusBar().setTxtState(ActionCommandRegistry.getInstance().get(currentCommand).getLongDescription());

	}

	/**
	 * 
	 * @param currentCommand
	 */
	public void clickButtonCommand(String currentCommand) {
		// selectionner le bouton correspondant a la commande passee en parametre
		Enumeration e = this.buttonGroup.getElements();
		AbstractButton b;
		while (e.hasMoreElements()) {
			b = (AbstractButton) e.nextElement();
			if (b.getActionCommand().equals(currentCommand)) {
				if (b.isEnabled()){
					b.doClick();
					setStructureViewControllerState(currentCommand);
				}
				else {
					SwingIO.warning(this.getClass().getName(), "clickButtonCommand", "cannot click on the disabled command  '" + currentCommand + "'");
				}
				break;

			}
		} // while
	}

	/**
	 * 
	 */
	public FrameApp() {
		isAlreadyRegisterDeconnect = false;
		timeBeginUse = 0;
		timeToReady= 0;
		timeToStart= 0;
		registerDurationThread = null;
		this.lastAction ="";

	}

	

	/**
	 * 
	 * @return
	 */
	public String getBrowser() {
		try {
			return getParameter("browser");
	
		} catch (Exception e1) {
			return "?";
		}
	}

	/**
	 * @return
	 */
	public long getTimeToStart() {
		return this.timeToStart;
	}

	
	
	/**
	 * @return
	 */
	public long getTimeToReady() {
		return this.timeToReady;
	}

	/**
	 * @param methodName
	 * @return
	 */
	public static IMethod getImplementedMethod(String methodName) {
		return FrameApp.lstImplementedMethods.get(methodName);
	}

	/**
	 * @return
	 */
	public static IMethod[] getImplementedMethods() {
		return FrameApp.lstImplementedMethods.values().toArray(
				new IMethod[FrameApp.lstImplementedMethods.size()]);
	}



	/**
	 * Parses the java version system property to determine the major java
	 * version, ie 1.x
	 * 
	 * @param javaVersion
	 *            the system property 'java.version'
	 * @return A float of the form 1.x
	 * 
	 * 
	 */

	static final float getMajorJavaVersion(String javaVersion) {
		try {
			return Float.valueOf(javaVersion.substring(0, 3)); // Float.parseFloat(javaVersion.substring(0,
			// 3));
		} catch (NumberFormatException e) {
			// Some JVMs may not conform to the x.y.z java.version format
			return FrameApp.MINIMAL_REQUIRED_JAVA_VERSION;
		}
	}

	/**
	 * @param argv
	 */
	static public void main(String argv[]) { 

		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {
			public void run() {

				AppletToFrame.createAndShowGUI(true);
			}
		});
	}

	/**
	 * get the selected methods in tne menubar
	 * @return
	 */
	public List<IMethod> getListSelectedMethods() {
		ArrayList <IMethod> lstMethods = new ArrayList<IMethod>();

		for (JMenuItem item : menuBar.getItemsdMethods()) 
			if (item.isSelected())
				lstMethods.add(FrameApp.getImplementedMethod(menuBar.getMethodNameByItem(item)));
		return lstMethods;

	}
	
	/**
	 * @param : method name
	 * @return true if the method is selected
	 */
	public boolean isMethodSelected(String name){
	
		for (IMethod method : this.getListSelectedMethods()) 
			if (method.getName().equals(name))
				return true;
		return false;

	}


	/**
	 * select all compute weight methods menu items in the menubar
	 */
	public void selectAllMethods() {
		for (JMenuItem item : menuBar.getItemsdMethods()) {
			item.setSelected(true);
			new ActionMenuMethod(item, this).execute();
		}
	}

	/**
	 * deselect all the mesomery compute weights methods menu items in the menubar
	 */
	public void deselectAllMethods() {
		// select all menu methods
		for (JMenuItem item : menuBar.getItemsdMethods()) {
			item.setSelected(false);
			new ActionMenuMethod(item, this).execute();
		}
	}

	/**
	 * select / deselect the specified mesomery compute weight menu item method in the menubar
	 * @param method
	 * @param select
	 */
	public void selectMethod (String method, boolean select) {
		JMenuItem item = menuBar.getItemMethod(method);
		item.setSelected(select);
		new ActionMenuMethod(item, this).execute();
	}

	/**
	 * start the applet with a new mesomery an an empty delocalized structure
	 */
	public  void startNewMesomery() {

		try {
			Mesomery mesomery = this.mesomeryView.getMesomery();
			mesomery.deleteAllStructures();

			if (this.rights.isValidUser()) {
				mesomery.addDelocalizedStruct(new StructureDelocalized());
			
				this.selectMethod("HLP", true);
				EnableActionManager.getInstance().updateActionsToEnable(this);
				this.getStateManager().initNewState();
				this.getStateManager().saveState();
				this.clickButtonCommand(IGlobalCommands.ADD); // retablit le bon message dans la barre d'etat
		}


		} catch (MesomeryDelocalizedStructureExistsException e) {
			SwingIO.error(this.getClass().getName(), "startNewMesomery",
					e.getMessage(), e);
			SwingIO.reportError(this);
		} catch (CoupleException e) {
			SwingIO.error(this.getClass().getName(), "startNewMesomery",
					e.getMessage(), e);
			SwingIO.reportError(this);
		} catch (IMethodException e) {
			SwingIO.error(this.getClass().getName(), "startNewMesomery",
					e.getMessage(), e);
			SwingIO.reportError(this);
		} catch (MesomeryNoStructureLocalizedException e) {
			SwingIO.error(this.getClass().getName(), "startNewMesomery",
					e.getMessage(), e);
			SwingIO.reportError(this);
		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "startNewMesomery",
					e.getMessage(), e);
			SwingIO.reportError(this);
		} finally {
			if (Rights.CHECK_USER_ACCESS_RIGHTS) {
				final FrameApp app = this;
				Runnable registerDuration = new Runnable() {
					public void run() {
						try {
							while(!isAlreadyRegisterDeconnect) {
								Thread.sleep(TIMER_HIT_USE_TIME_DURATION);
								registerDuration();
							}
						} catch (InterruptedException e) {


						} catch (Exception e) {
							SwingIO.error(this.getClass().getName(), "run", e
									.getMessage(), e);

						}
					}
				};

				registerDurationThread = new Thread(registerDuration, "registerDuration");
				if (PreferencesGUI.getInstance().isSendInformation())
					registerDurationThread.start();

				SwingIO.log(this.getClass().getName(), "FrameApp",
						"register user");
				timeToReady = new Date().getTime() - timeBeginUse;
				System.out.println ("ready after : " + timeToReady + " ms");
				registerNewUser();

			}


		}
		
	} // startNewMesomery

	public ButtonGroup buttonGroup = new ButtonGroup();
	// private Mesomery mesomery;

	private boolean isExpertMode = false;

	public AppMenuBar menuBar;

	private MesomeryView mesomeryView;


	MyResourceBundle rb;

	public StatusBar statusBar;

	public ToolBarHuckel toolBarHuckel;

	public ToolBarMesomery toolBarMesomery;

	private PolyCalculator polyCalc;


	private boolean ownedToFrame=false;

	
	/** 
	 * register the new user in the database
	 */
	public void registerNewUser() {
		if (PreferencesGUI.getInstance().isSendInformation())
			ConnectSQL.getInstance().registerNewHulisUser(this);

	}
	
	/**
	 * register action in the database 
	 * @param action
	 * @return
	 */
	public void registerAction(String action) {
		if (PreferencesGUI.getInstance().isSendInformation())
			ConnectSQL.getInstance().registerDuration(this, action);

	}
	
	/**
	 * send a hit to the database to register duration of use
	 */
	public void registerDuration() {
		if (PreferencesGUI.getInstance().isSendInformation())
			ConnectSQL.getInstance().registerDuration(this);
	}
	
	/**
	 * 
	 */
	public void registerDisconnect() {
		if (PreferencesGUI.getInstance().isSendInformation())
			ConnectSQL.getInstance().registerDisconnectApplet(this);

	}
	
	
	/**
	 *  constructs the frame org.ctom.view
	 * 
	 */

	public void construct() {
		this.isExpertMode = false;
		this.currentCommand = "";
		try {

			Energy.setDefaultEnergyFormater(DEFAULT_ENERGY_FORMATER);

			Mesomery mesomery = new Mesomery();
			mesomery.setStructureSelector(new MesomeryHLPOverlapStructuresSelector());
			//mesomery.setStructureSelector(new MesomeryEnergyStructuresSelector(mesomery));
			mesomery.setCloneResultsCacheWhenCloneMesomery(CLONE_RESULTS_WHEN_CLONE_MESOMERY);

			this.stateManager = new StateManager(this);

			this.statusBar = new StatusBar();
			this.toolBarHuckel = new ToolBarHuckel(this);
			this.toolBarMesomery = new ToolBarMesomery(this, mesomery);
			this.mesomeryView = new MesomeryView(mesomery, this);

			this.menuBar = new AppMenuBar(this);
			this.menuBar.menuExp.setSelected(this.isExpertMode);


			setJMenuBar(this.menuBar);

			this.updateLanguage();
			setSizes();
			// this.pack();

			// create frame

			UIManager.getDefaults().put("ToolTip.background", Color.YELLOW);

			/*
			 * UIDefaults defaults = UIManager.getDefaults();
			 * System.out.println(defaults.size()+ " properties deffined !");
			 * String[ ] colName = {"Key", "Value"}; String[ ][ ] rowData = new
			 * String[ defaults.size() ][ 2 ]; int i = 0; for(Enumeration e =
			 * defaults.keys(); e.hasMoreElements(); i++){ Object key =
			 * e.nextElement(); rowData[ i ] [ 0 ] = key.toString(); rowData[ i ] [
			 * 1 ] = ""+defaults.get(key); System.out.println(rowData[i][0]+" ,,
			 * "+rowData[i][1]); }
			 */
			ToolTipManager.sharedInstance().setDismissDelay(60000);
			ToolTipManager.sharedInstance().setInitialDelay(500);

			// 1 - Panneau contenant la barre d'outils
			setBackground(FrameApp.HUCKEL_VIEWS_BACK_COLOR);
			this.toolBarHuckel
			.setBackgroundColor(FrameApp.HUCKEL_VIEWS_BACK_COLOR);
			mesomery.addListener(this.toolBarHuckel);
			mesomery.addListener(this.getStateManager());

			
			// 2- panneau graphe d'energies
			this.toolBarMesomery
			.setBackgroundColor(FrameApp.MESOMERY_VIEWS_BACK_COLOR);

			// 3- panneau contenant les onglets
			this.mesomeryView.setBackground(Color.WHITE);

			// 4- panneau pour la barre d'etat
			this.statusBar.setBackground(new Color(254, 254, 254)) ; //   //new Color(236, 233, 216));

			this.menuBar.setBackground(new Color(254, 254, 254));

			// mesomery.addMesomeryListener(statusBar);

			this.buttonGroup.add(this.toolBarHuckel.btAdd);
			this.buttonGroup.add(this.toolBarHuckel.btChange);
			this.buttonGroup.add(this.toolBarHuckel.btRemove);
			this.buttonGroup.add(this.toolBarHuckel.btMove);
			this.buttonGroup.add(this.toolBarHuckel.btRotate);
			this.buttonGroup.add(this.toolBarHuckel.btRenum);
			this.buttonGroup.add(this.toolBarMesomery.btCouple);
			this.buttonGroup.add(this.toolBarMesomery.btBloc);
			this.buttonGroup.add(this.toolBarMesomery.btChangeElectrons);

			// 5 - Mise en page de l'application

			// this.setTitle(ksoftname + " " + version);
			Container contenu = getContentPane();
			contenu.setLayout(new BorderLayout(0, 0));
			contenu.add(this.toolBarHuckel, BorderLayout.WEST);
			contenu.add(this.mesomeryView, BorderLayout.CENTER);
			contenu.add(this.toolBarMesomery, BorderLayout.EAST);
			contenu.add(this.statusBar, BorderLayout.SOUTH);

			// Give Controls

			// instance.setVisible(true);

			/* give controls to the user : mouse, etc... */
			/* initialize the mesomery and create the structure of huckel */


			this.toolBarHuckel.btDisplayHxHxy.setSelected(this.mesomeryView
					.isDisplayHxHxy());
			this.toolBarHuckel.btDisplayCharges.setSelected(this.mesomeryView
					.isDisplayCharges());

			this.toolBarHuckel.btDisplayNum.setSelected(this.mesomeryView
					.isDisplayNum());

			// actionStartApp = new ActionStartApp();
			ActionCommandRegistry actionCommandRegistry =  ActionCommandRegistry.getInstance();
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.ADD));
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.CHANGE));
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.REMOVE));
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.MOVE));
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.ROTATE));
			actionCommandRegistry.add(new ActionAtomsRenum(this));
			actionCommandRegistry.add(new ActionShowResultStruct(this));
			actionCommandRegistry.add(new ActionDuplicate(this));
			actionCommandRegistry.add(new ActionEraseAll(this));
			actionCommandRegistry.add(new ActionOptimizeGeometry(this));
			actionCommandRegistry.add(new Center(this.getCurrentMesomeryView()));
			actionCommandRegistry.add(new ActionErase1(this));
			actionCommandRegistry.add(new ActionEraseMesomery(this));
			actionCommandRegistry.add(new ActionShowResultMesomery(this));
			actionCommandRegistry.add(new ActionDisplayHxHxy(this));
			actionCommandRegistry.add(new ActionDisplayCharges(this));
			actionCommandRegistry.add(new ActionDisplayNum(this));
			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.COUPLE));
			actionCommandRegistry.add(new ActionModeBloc(this));
			actionCommandRegistry.add(new ActionEndBloc(this));

			actionCommandRegistry.add(new ActionChangeMode(this,
					IGlobalCommands.CHANGE_ELECTRON));
			actionCommandRegistry.add(new ActionMoleculeAddElecCharge(this));
			actionCommandRegistry.add(new ActionMoleculeRemoveElecCharge(this));
			actionCommandRegistry.add(new ActionAutoGenStruct(this));
			actionCommandRegistry.add(new ActionStopAutoGenStruct(this));
			actionCommandRegistry.add(new ActionQuit(this));

			this.toolBarHuckel.btAdd.setAction(actionCommandRegistry
					.get(IGlobalCommands.ADD));
			this.toolBarHuckel.btChange.setAction(actionCommandRegistry
					.get(IGlobalCommands.CHANGE));
			this.toolBarHuckel.btRemove.setAction(actionCommandRegistry
					.get(IGlobalCommands.REMOVE));
			this.toolBarHuckel.btMove.setAction(actionCommandRegistry
					.get(IGlobalCommands.MOVE));
			this.toolBarHuckel.btRotate.setAction(actionCommandRegistry
					.get(IGlobalCommands.ROTATE));
			this.toolBarHuckel.btOptimize.setAction(actionCommandRegistry
					.get(IGlobalCommands.OPTIMIZE));
			this.toolBarHuckel.btCenter.setAction(actionCommandRegistry
					.get(IGlobalCommands.CENTER));
			this.toolBarHuckel.btRenum.setAction(actionCommandRegistry
					.get(IGlobalCommands.RENUM));
			this.toolBarHuckel.btErase.setAction(actionCommandRegistry
					.get(IGlobalCommands.ERASE_ALL));
			this.toolBarHuckel.btShowResults.setAction(actionCommandRegistry
					.get(IGlobalCommands.SHOW_RESULTS));
			this.toolBarMesomery.btShowResultsMesomery
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.SHOW_RESULTS_MESOMERY));
			this.toolBarMesomery.btDuplicate.setAction(actionCommandRegistry
					.get(IGlobalCommands.DUPLICATE));
			this.toolBarMesomery.btAutoGenStruct
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.AUTO_GEN_STRUCT));
			this.toolBarMesomery.btStopAutoGenStruct
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.STOP_AUTO_GEN_STRUCT));
			this.toolBarMesomery.btErase1.setAction(actionCommandRegistry
					.get(IGlobalCommands.ERASE_1));
			this.toolBarMesomery.btEraseMesomery
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.ERASE_LEWIS));
			this.toolBarMesomery.btCouple.setAction(actionCommandRegistry
					.get(IGlobalCommands.COUPLE));
			this.toolBarMesomery.btBloc.setAction(actionCommandRegistry
					.get(IGlobalCommands.BLOC));
			this.toolBarMesomery.btEndBloc.setAction(actionCommandRegistry
					.get(IGlobalCommands.END_BLOC));
			this.toolBarMesomery.btChangeElectrons
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.CHANGE_ELECTRON));
			this.toolBarHuckel.btDisplayHxHxy.setAction(actionCommandRegistry
					.get(IGlobalCommands.DISPLAY_INFOS));
			this.toolBarHuckel.btDisplayCharges.setAction(actionCommandRegistry
					.get(IGlobalCommands.DISPLAY_CHARGES));			
			this.toolBarHuckel.btDisplayNum.setAction(actionCommandRegistry
					.get(IGlobalCommands.DISPLAY_NUM));
			this.toolBarHuckel.btMoleculeAddElecCharge
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.MOLECULE_ADD_ELEC_CHARGE));
			this.toolBarHuckel.btMoleculeRemoveElecCharge
			.setAction(actionCommandRegistry
					.get(IGlobalCommands.MOLECULE_REMOVE_ELEC_CHARGE));
			this.toolBarHuckel.btQuit.setAction(actionCommandRegistry
					.get(IGlobalCommands.QUIT));


			menuBar.menuPreferences.addActionListener(new OpenPreferences(this));
			menuBar.menuPrintCurrentView.addActionListener(new PrintCurrentView(this));
			menuBar.menuQuit.setAction(actionCommandRegistry.get(IGlobalCommands.QUIT));

			menuBar.menuUndo.addActionListener(new Undo(this));
			menuBar.menuRedo.addActionListener(new Redo(this));
			menuBar.menuPaste.addActionListener(new IOClipBoard(this));
			menuBar.superMario.addActionListener(new SuperMario(this));

			menuBar.menuExp.addActionListener(new ChooseMenuExp(this));
			menuBar.menuDisplayPolynomes.addActionListener(new ActionDisplayPolynomes(this));
			menuBar.menuExcitations.addActionListener(new ShowListExcitations(this));
			menuBar.menuBlocs.addActionListener(new ShowListBlocs(this));
			menuBar.menuSinguletTriplet.addActionListener(new SwitchSinguletTriplet(this));
			menuBar.menuSwitchNaturalCanonical.addActionListener(new SwitchCanonicalNatural(this));
			menuBar.menuSwitchDensity.addActionListener(new SwitchDensity(this));
			menuBar.menuOptGeom.addActionListener(new ActionOptimizeGeometry(this));
			menuBar.menuSinguletCO.addActionListener(new SwitchSinguletCouchesOuvertes(this));			
			menuBar.menuZoomD.addActionListener(new ZoomD(this));
			menuBar.menuZoomP.addActionListener(new ZoomP(this));
			menuBar.menuZoomM.addActionListener(new ZoomM(this));

			//yc polynomes
			menuBar.menuAddPolynome.addActionListener(new ActionSavePolynomes(this));
			//fin yc polynomes
			for (JMenuItem item : menuBar.getItemsdMethods())
				item.addActionListener(new ActionMenuMethod(item, this));
			
			menuBar.menuLog.addActionListener(new ActionOpenLog(
					this));
			menuBar.menuAbout.addActionListener(new ActionOpenAbout(
					this));


			this.toolBarHuckel.btAdd.doClick();

			/** * intialize authorizations ** */

			rights = new Rights(this);

			
			if (!(isExecutedOnInternet())){ // Application standalone 
				menuBar.menuSave.addActionListener(new IOFichier(this));
				menuBar.menuOpen.addActionListener(new IOFichier(this));
				menuBar.menuPrintCurrentView.addActionListener(new IOFichier(this));
				try {
					menuBar.menuTutorial0
					.addActionListener(new TutorialBuildMolecule(this));
				} catch (AWTException e) {
					e.printStackTrace();
				}
				catch (SecurityException e){
					e.printStackTrace();

				}
			}
			
			PreferencesGUI.getInstance().setTotalUse(PreferencesGUI.getInstance().getTotalUse() + 1);

			
			this.menuBar.menuOpen.setEnabled(rights.isValidUser());
			this.menuBar.menuPaste.setEnabled(rights.isValidUser());
			actionCommandRegistry.get(IGlobalCommands.ERASE_ALL).setEnabled(
					rights.isValidUser());
			//actionCommandRegistry.get(IGlobalCommands.QUIT).setEnabled(!this.getRights().isExecutedFromNetwork());
			EnableActionManager.getInstance().updateActionsToEnable(this);


		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "FrameApp", e
					.getMessage(), e);
			SwingIO.reportError(this);
		}

	}

	public Mesomery getCurrentMesomery() {
		return this.mesomeryView.getMesomery();
	}

	public MesomeryView getCurrentMesomeryView() {
		return this.mesomeryView;
	}


	public ToolBarMesomery getEnergiesSlider() {
		return this.toolBarMesomery;
	}



	public Rights getRights() {
		return rights;
	}

	public StatusBar getStatusBar() {
		return this.statusBar;
	}

	public MesomeryView getTabs() {
		return this.mesomeryView;
	}

	public ToolBarHuckel getToolBarHuckel() {

		return this.toolBarHuckel;
	}

	public ToolBarMesomery getToolBarMesomery() {
		return this.toolBarMesomery;
	}
	public Version getVersion() {
		return version;
	}


	@Override
	public void start() {
		System.out.println("start");
		super.start();
		new ParserDelegator(); // corrige un bug du parserdelegator qui fait planter l'applet quand on  raffraichit la page web
		SwingIO.log(this.getClass().getName(), "start","applet is started");
	}


	@Override
	public void stop() {
		System.out.println("stop");
		registerDisconnect();
		super.stop();
	}

	private HashMap <String, String> parameters = new HashMap<String, String>();

	private String lastAction;

	private CookieManager cookieManager;

	
	@Override
	public String getParameter(String name) {
		if (parameters.containsKey(name))
			return parameters.get(name);
		else
			try{
				return super.getParameter(name);
			} catch (Exception e) {
				SwingIO.log(this.getClass().getName(), "getParameter()", "stub is null, return null for parameter " + name);
				return null;
			}

	}

	public void setParameter(String name, String value) {
		this.parameters.put(name, value);
	}


	@Override
	public void init() {
		
		SwingIO.setVerbose(Rights.CONSOLE_VERBOSE);
		SwingIO.log(this.getClass().getName(), "init", "start applet "  + FrameApp.ksoftname + " "+this.getVersion());
		
		super.init();
		
		
		this.setVisible(true);

		try {
			Runtime.getRuntime().runFinalization();
			Runtime.getRuntime().gc();
			//LanguageManager.getInstance().setLanguage(MyLocale.getDefault());
			if (!(isExecutedOnInternet()))// Application standalone 
				PreferencesGUI.getInstance().setPreferencesManager(new PreferencesStd(PREFERENCES_NODE_NAME)); // preferences java standard dans le cas d'une appli standalone
			else 
				PreferencesGUI.getInstance().setPreferencesManager(new PreferencesCookies(cookieManager)) ; // gestion des preferences avec les cookies pour une application inernet

			if (PreferencesGUI.CLEAR_PREFERENCES_WHEN_START) {
				SwingIO.log(this.getClass().getName(), "init", "clear all preferences");
				PreferencesGUI.getInstance().clearPreferences();
			}
						
			LanguageManager langageManager = LanguageManager.getInstance();
			langageManager.setLanguage(PreferencesGUI.getInstance().getLanguage());
			langageManager.setResource(new InitResources());


			String mess = "";
			String sTime ="";
			timeBeginUse = 0;
			Long  internalTime = 0L;
			try {
				sTime = getParameter("time");
				mess = "read external time : " + sTime + " ms";
				SwingIO.log(this.getClass().getName(), "init", mess);

			} catch (Exception e1) {
				sTime ="";
				mess = "No time in applet parameter";
				SwingIO.warning(this.getClass().getName(), "init", mess);
			}
			try {
				if (sTime != "" && sTime != null)
					timeBeginUse = Long.valueOf(sTime);
				else
					timeBeginUse=new Date().getTime();
				internalTime = new Date().getTime();
				timeToStart = internalTime  - timeBeginUse;
			} catch (NumberFormatException e1) {
				mess= "time error number format : " +sTime;
				SwingIO.warning(this.getClass().getName(), "init", mess);
			}
			mess= "time to start : " + timeToStart + " ms";
			SwingIO.log(this.getClass().getName(), "init", mess);
			System.out.println ("internal time : " + internalTime + " ms");

		

			FrameApp.checkJavaVersion();

			SwingIO.log(this.getClass().getName(), "init", "Browser "
					+ this.getParameter("browser"));
			SwingIO.log(this.getClass().getName(), "init", "OS Name : "
					+ System.getProperty("os.name"));
			SwingIO.log(this.getClass().getName(), "init", "OS Version : "
					+ System.getProperty("os.version"));
			SwingIO.log(this.getClass().getName(), "init", "JDK version : "
					+ System.getProperty("java.version"));
			SwingIO.log(this.getClass().getName(), "init", "JDK from : "
					+ System.getProperty("java.vendor"));
			SwingIO.log(this.getClass().getName(), "init", "JVM version : "
					+ System.getProperty("java.vm.version"));
			SwingIO.log(this.getClass().getName(), "init", "JVM from : "
					+ System.getProperty("java.vm.vendor"));


			SwingIO.log(this.getClass().getName(), "init", "construct views " + FrameApp.ksoftname + " "+this.getVersion());
			construct();
			SwingIO.log(this.getClass().getName(), "init", "end construct views ");
			if (! this.isOwnedToFrame()) {
				startNewMesomery();
				/* display a splash screen */

				this.displaySplashscreen();
			}

			
		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "Applet", e.getMessage(),
					e);
			SwingIO.reportError(this);
		}

	} // init

	/**
	 * 
	 */
	public void displayLicenseToAccept() {
		if (!PreferencesGUI.MANAGE_LICENCE)
			return; 
		if (PreferencesGUI.getInstance().isLicenseAlreadyAccepted())
			SwingIO.warning(this.getClass().getName(), "displayLicenseToAccept", "License is already accepted !");
		else
			new LicenseFrame(this).setVisible(true);
	}
	
	/**
	 * 
	 */
	public void displaySplashscreen() {
		new SplashWindow(this, FrameApp.splashtime).setVisible(true);
	}


	/**
	 * 
	 * @return
	 */
	public boolean isExpertMode() {
		return this.isExpertMode;
	}

	/**
	 * 
	 * @param isExpertMode
	 */
	public void setExpertMode(boolean isExpertMode) {
		this.isExpertMode = isExpertMode;
	}

	/**
	 * 
	 */
	public void updateLanguage() {
		this.rb = LanguageManager.getInstance().getResource("FrameApp");


	}

	/**
	 * 
	 * @return
	 */
	public AppMenuBar getMenuBar() {
		return this.menuBar;
	}

	/**
	 * 
	 */
	public void setSizes() {
		int FRAME_WIDTH = (Integer) (this.rb.getObject("kframew"));
		int FRAME_HEIGTH = (Integer) (this.rb.getObject("kframeh"));
		int PANEL_MOLECULE_WIDTH = (Integer) (this.rb
				.getObject("kpanelmolw"));
		int TOOLBAR_WIDTH = (Integer) (this.rb.getObject("ktoolbarw"));
		int STATUSBAR_HEIGTH = (Integer) (this.rb.getObject("kstatusbarh"));
		int TOOLBAR_MES_WIDTH = (Integer) (this.rb.getObject("ktoolbarmw"));
		this.toolBarHuckel.setPreferredSize(new Dimension(
				TOOLBAR_WIDTH, FRAME_HEIGTH));
		this.toolBarHuckel.setMinimumSize(new Dimension(TOOLBAR_WIDTH,
				FRAME_HEIGTH));
		this.toolBarMesomery.setPreferredSize(new Dimension(
				TOOLBAR_MES_WIDTH, FRAME_HEIGTH));
		this.toolBarMesomery.setMinimumSize(new Dimension(
				TOOLBAR_MES_WIDTH, FRAME_HEIGTH));

		this.mesomeryView.setPreferredSize(new Dimension(PANEL_MOLECULE_WIDTH,
				FRAME_HEIGTH ));
		this.mesomeryView.setMinimumSize(new Dimension(
				PANEL_MOLECULE_WIDTH, FRAME_HEIGTH ));

		this.statusBar.setPreferredSize(new Dimension(FRAME_WIDTH,
				STATUSBAR_HEIGTH));
		this.statusBar.setMinimumSize(new Dimension(FRAME_WIDTH,
				STATUSBAR_HEIGTH));
		this
		.setSize(new Dimension(FRAME_WIDTH,
				FRAME_HEIGTH));
		setPreferredSize(new Dimension(FRAME_WIDTH,
				FRAME_HEIGTH));
		setMinimumSize(new Dimension(FRAME_WIDTH,
				FRAME_HEIGTH));
	}



	@Override
	public void destroy() {
		System.out.println("destroy");
		try {
			// fermer toutes les fenetre de resultat
			for (Window w : Frame.getWindows()) 
				if (w instanceof FrameResult) 
					w.dispose();

			ActionCommandRegistry.clear();

			if (registerDurationThread != null && registerDurationThread.isAlive())
				registerDurationThread.interrupt(); 

			//	InitResources.removeResources(); // PROBLEME SI ON LIBERE LES RESSOURCES : 
			// SI ON LANCE LAPPLET 2 FOIS DE SUITE EN LIGNE, CA PLANTE : 
			// util.MYRESSOURCEBUNDLE public String getString(String key) {
			//	if (!this.resourcesBundle.containsKey(key))  renvoie pointeur null

		}
		catch (Exception e) {

			//System.out.println(e);
		}
		finally {
			if (Rights.CHECK_USER_ACCESS_RIGHTS  && !isAlreadyRegisterDeconnect) {
				isAlreadyRegisterDeconnect = true;
			}
			ErrorReport.clear();
			ConnectSQL.clear();
			super.destroy();

		}
	}

	public synchronized void terminate(int errcode) {

		try {
			ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

				@Override
				public void run() {
					setVisible(false);
					
				}});
			
			registerDisconnect();
			stop();
			destroy();
			Frame frame =  JOptionPane.getFrameForComponent(this);
			if (frame != null)
				frame.dispose();
			if (!this.isExecutedOnInternet())
				System.exit(errcode); // exit provoqu une exception pour une applet en ligne non signee
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}




	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;

	}
	public PolyCalculator getPolyCalc() {
		int nelec = this.getCurrentMesomery().getDelocalizedStructure().getSumOfPiElec();
		if (this.polyCalc==null) {
			this.polyCalc = new PolyCalculator(nelec);
		}
		return this.polyCalc;
	}

	/**
	 * @param ownedToFrame
	 */
	public void setOwnedToFrame(boolean ownedToFrame) {
		this.ownedToFrame  = ownedToFrame;

	}

	/**
	 * @return true if the applet owned to a frame 
	 */
	public boolean isOwnedToFrame() {
		return this.ownedToFrame;

	}


	/**
	 * check the available version and returns the text
	 * @return  get text for the checked version
	 */
	public String getCheckVersionTxt() {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("About");
		
		if (this.isExecutedOnInternet() ) 
			return "";
		
		String txt="";
		
		if (! PreferencesGUI.getInstance().isCheckVersion())
			return "";
		
		Version avalaibleVersion = ConnectSQL.getCurrentVersion();

		// date limite de la version d'evaluation
		if ((Rights.DATE_LIMIT != null)
				&& (!Rights.BYPASS_LIMIT_DATE)) {
			SimpleDateFormat fmt = new SimpleDateFormat(bundle
					.getString("countrydate"));
			String limit = fmt.format(Rights.DATE_LIMIT);
			txt += bundle.getString("avalaibleuntil") + " " + limit;
		}

		// nouvelle version dispo
		if ((avalaibleVersion != null) &&
				(avalaibleVersion.compareTo(this.getVersion()) > 0)) {
			if (!txt.equals(""))
				txt += ". ";
			txt += bundle.getString("avalaibleversion") + " "
				+ avalaibleVersion;
		}
		
		return txt;
		
	}


	/**
	 * 
	 * @return
	 */
	public long getTimeBeginUse() {
		return this.timeBeginUse;
	}

	
	
	/**
	 * 
	 * @return get last action
	 */

	public String getLastAction () {
		return this.lastAction;
	}


	/**
	 * 
	 * @param lastAction
	 */
	public void setLastAction (String lastAction) {
		this.lastAction = lastAction;
	}

	/**
	 * 
	 * @param cookieManager
	 */
	public void setCookieManager(CookieManager cookieManager) {
		this.cookieManager=cookieManager;
		
	}


	/**
	 * @param m
	 */
	public void setMesomery(Mesomery m, String currentStructureName, String selectedCommand) {
	    	if (currentStructureName=="")
	    	    currentStructureName = "tot";
	    	if (selectedCommand =="") {
	    	    selectedCommand=this.getCurrentEnabledCommand();
	    	}
		this.getCurrentMesomeryView().setMesomery(m);
		m.addListener(this.getToolBarHuckel());
		m.addListener(this.getStateManager());

		this.getToolBarMesomery().setMesomery(m);

		// Maj refernce dans fenetres ouvertes
		ArrayList <JFrame> toClose = new ArrayList <JFrame>();
		for (Window  r : JFrame.getWindows()) {
			if (r instanceof IStructureReference) {
				IStructureReference sr = (IStructureReference) r;
				Structure s = sr.getStructure();
				String name = s.getName();
				Structure searched =m.getStructureByName(name);
				if (searched != null) 
					sr.setStructure(searched);

				else 
					toClose.add((JFrame) sr);
			} else if (r instanceof IMesomeryReference) {
				FrameResultMesomery mr = (FrameResultMesomery) r;
				mr.setMesomery(m);

			}
		}

		for (JFrame fr : toClose)
			fr.dispose();

		Structure current = m.getStructureByName(currentStructureName);
		if (current==null) {
			SwingIO.warning(this.getClass().getName(), "restoreState", "structure Psi" + currentStructureName +  " was not found in the restored mesomery, try to get the delocalized structure");
			current = m.getDelocalizedStructure();
			if (current == null)
				SwingIO.warning(this.getClass().getName(), "restoreState", "structure Psitot was not found anymore in the restored mesomery");
		}
		this.getCurrentMesomeryView().setCurrentStructure(current);
		ActionCommandRegistry.getInstance().enableAll(); // active tous les boutons de facon a ce que l'instruction suivante puisse cliquer sur un bouton. Le contextmanager se chargera de rsesactiver le boutons necessaires apres
		this.clickButtonCommand(selectedCommand);
		EnableActionManager.getInstance().updateActionsToEnable(this);
		this.getToolBarHuckel().refresh();
		this.getToolBarMesomery().refresh();

	    
	}

	
}
