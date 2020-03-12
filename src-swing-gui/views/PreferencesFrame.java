package views;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.prefs.Preferences;

import util.ComponentsTools;
import util.FollowLink;
import util.IntegerSpinner;
import util.MyDecimalFormat;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;

public class PreferencesFrame extends MyJDialog implements ActionListener {
	protected JButton btOK;
	protected JButton btUndo;
	FrameApp app;
	JSpinner spinnerMO;
	JSpinner spinnerEnergy;
	JSpinner spinnerMOratio;
	JSlider sliderMOratio;
	JCheckBox chkCircle;
	JCheckBox chkSymbol;
	//debyc
	JSpinner spinnerNSepCharge;
	JCheckBox checkBoxCouple;
	//endyc
	JCheckBox chkCheckVersion;
	JTextPane lblCheckVersion;
	JCheckBox chkSendInformation;
	
	protected MyResourceBundle bundle;
	private boolean oldCheckBoxCouple;
	private int oldSpinnerNSepCharge;
	
	int oldMOprecision;
	int oldEnergyPrecision;
	double oldZoomMO;
	boolean oldDisplayAtomsCircles;
	boolean oldDisplayAtomsSymbols;
	boolean  oldCheckVersion;
	boolean  oldSendInformation;

	PreferencesGUI preferencesGUI = PreferencesGUI.getInstance();
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource().equals(this.btOK) ) {
			SwingIO.log(this.getClass().getName(), "actionPerformed", "validate changes");

			this.dispose();
		}
		else if (e.getSource().equals(this.btUndo)) {
			SwingIO.log(this.getClass().getName(), "actionPerformed", "cancel changes");
			this.restoreOldValues();
			this.dispose();
		}
		

	}
	
	/**
	 *  restore old values on cancel
	 */
	public void restoreOldValues() {
		SwingIO.log(this.getClass().getName(), "restoreOldValues", "restore old values");
		preferencesGUI.setResultsPrecisionMO(oldMOprecision);
		preferencesGUI.setResultsPrecisionEnergies(oldEnergyPrecision);
		preferencesGUI.setSCALE_ORB(oldZoomMO);
		preferencesGUI.setDisplayAtomsCircles(oldDisplayAtomsCircles);
		preferencesGUI.setDisplayAtomsSymbols(oldDisplayAtomsSymbols);
		preferencesGUI.setCheckVersion(oldCheckVersion);
		preferencesGUI.setSendInformation(oldSendInformation);
		Preferences.setCOUPLE_AUTOGEN(oldCheckBoxCouple);
		Preferences.setNUM_CHARGE_SEPARATION(oldSpinnerNSepCharge);
		
	}
	
	/**
	 *  save the current values. The user can restore them by clicking cancel or close the frame with the standard close button
	 */
	public void saveCurrentValues() {
		oldMOprecision = preferencesGUI.getResultsPrecisionMO();
		oldEnergyPrecision= preferencesGUI.getResultsPrecisionEnergies();
		oldZoomMO= preferencesGUI.getSCALE_ORB();
		oldDisplayAtomsCircles= preferencesGUI.isDisplayAtomsCircles();
		oldDisplayAtomsSymbols= preferencesGUI.isDisplayAtomsSymbols();
		oldCheckVersion = preferencesGUI.isCheckVersion();
		oldSendInformation = preferencesGUI.isSendInformation();
		oldCheckBoxCouple= Preferences.isCOUPLE_AUTOGEN();
		oldSpinnerNSepCharge= Preferences.getNUM_CHARGE_SEPARATION();
	}
	
	/**
	 * Get the current values to Preferences Frame
	 */
	public void applyValuesToUI() {
		spinnerMO.setValue(preferencesGUI.getResultsPrecisionMO());
		spinnerEnergy.setValue(preferencesGUI.getResultsPrecisionEnergies());
		sliderMOratio.setValue(convertRealMoRatioToSpinnerMoRatioValue(preferencesGUI.getSCALE_ORB()));
		this.chkCircle.setSelected(preferencesGUI.isDisplayAtomsCircles());
		this.chkSymbol.setSelected(preferencesGUI.isDisplayAtomsSymbols());
		//debyc
		checkBoxCouple.setSelected(Preferences.isCOUPLE_AUTOGEN());
		spinnerNSepCharge.setValue(Preferences.getNUM_CHARGE_SEPARATION());
		chkCheckVersion.setSelected(preferencesGUI.isCheckVersion());
		chkSendInformation.setSelected(preferencesGUI.isSendInformation());
		this.lblCheckVersion.setText(this.getTextCheckVersion(chkCheckVersion.isSelected()));
		//endyc
	}
	
	/**
	 * apply preferences
	 * 
	 */
	public void applyPreferences() {
		SwingIO.log(this.getClass().getName(), "applyPreferences", "apply preferences");
		int precisionMO = (Integer) spinnerMO.getValue();
		int precisionEnergies = (Integer) spinnerEnergy.getValue();
		
		//debyc
		boolean selected = checkBoxCouple.isSelected();
		Preferences.setCOUPLE_AUTOGEN(selected);
		Preferences.setNUM_CHARGE_SEPARATION((Integer) spinnerNSepCharge.getValue());
		//endyc

		for (Window  r : JFrame.getWindows()) {
			if (r instanceof FrameResult)  {
				FrameResult frameResult =(FrameResult) r;
				frameResult.setMyDecimalFormat(new MyDecimalFormat(precisionMO), new MyDecimalFormat(precisionEnergies));
				frameResult.refresh();
			}
		}			
		
		preferencesGUI.setResultsPrecisionMO(precisionMO);
		preferencesGUI.setResultsPrecisionEnergies(precisionEnergies);
		preferencesGUI.setSCALE_ORB(convertSpinnerMoRatioValueToRealMoRatio(sliderMOratio.getValue()));
		preferencesGUI.setDisplayAtomsCircles(this.chkCircle.isSelected());
		preferencesGUI.setDisplayAtomsSymbols(this.chkSymbol.isSelected());
		app.getCurrentMesomeryView().getCurrentStructureView().repaint();

		preferencesGUI.setCheckVersion(this.chkCheckVersion.isSelected());
		preferencesGUI.setSendInformation(this.chkSendInformation.isSelected());

	}
	
	
	/**
	 * @author macbook
	 *
	 */
	protected class ApplyPreferencesListener implements ChangeListener {

		public void stateChanged(ChangeEvent e) {
			applyPreferences();
			
		}
		
	}
	
	/**
	 * 
	 * @param spinnerMoRatioValue
	 * convert [-100, 100] to [-10, 10]
	 * @return
	 */
	private static double convertSpinnerMoRatioValueToRealMoRatio(int spinnerMoRatioValue ) {
		return spinnerMoRatioValue/10;
	}
	
	/**
	 * 
	 * @param moRatioValue
	 * convert [-10, 10] to [-100, 100]
	 * @return
	 */
	private static int convertRealMoRatioToSpinnerMoRatioValue(double moRatioValue) {
		return  (int)(10 * moRatioValue);
	}
	
	
	/**
	 * 
	 * @param app
	 */
	public PreferencesFrame(FrameApp app) {
		super(JOptionPane.getFrameForComponent(app));
		this.app = app;
		ApplyPreferencesListener applyPreferencesListener = new ApplyPreferencesListener();
		
		
		final PreferencesFrame preferencesFrame = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				SwingIO.log(this.getClass().getName(), "windowClosing", "cancel changes");
				restoreOldValues();
				preferencesFrame.dispose();
			}
		});
		Container contenu = getContentPane();


		bundle = LanguageManager.getInstance().getResource("Preferences");
		this.setTitle(bundle.getString("title"));
		
		// spinner MO
		spinnerMO = IntegerSpinner.makeIntegerSpinner(preferencesGUI.getResultsPrecisionMO(),0,	99,	1);
		spinnerMO.addChangeListener(applyPreferencesListener);
			

		// spinner energy
		spinnerEnergy =  IntegerSpinner.makeIntegerSpinner(preferencesGUI.getResultsPrecisionEnergies(), 0, 99, 1);
		spinnerEnergy.addChangeListener(applyPreferencesListener);
		
		// spinner MO ratio
		sliderMOratio = new JSlider(JSlider.HORIZONTAL, -100 ,100, convertRealMoRatioToSpinnerMoRatioValue(preferencesGUI.getSCALE_ORB()));
		sliderMOratio.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent e) {
				spinnerMOratio.setValue(sliderMOratio.getValue());
				sliderMOratio.setToolTipText(String.valueOf(sliderMOratio.getValue()));
				
			}});
		sliderMOratio.addChangeListener(applyPreferencesListener);
		
		
		// spinner MO ratio
		SpinnerModel spinnerModel =
	         new SpinnerNumberModel(convertRealMoRatioToSpinnerMoRatioValue(preferencesGUI.getSCALE_ORB()), //initial value
	            -100, //min
	            100, //max
	            1);//step
		spinnerMOratio= new JSpinner(spinnerModel);
		spinnerMOratio.addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent e) {
				sliderMOratio.setValue((Integer)spinnerMOratio.getValue());
				
			}});
		
		// atom presentation cercle ou symbole
		
		this.chkCircle = new JCheckBox(bundle.getString("kcircle"));
		this.chkCircle.setSelected(preferencesGUI.isDisplayAtomsCircles());
		this.chkCircle.addChangeListener(applyPreferencesListener);
		
		this.chkSymbol = new JCheckBox(bundle.getString("ksymbols"));
		this.chkSymbol.setSelected(preferencesGUI.isDisplayAtomsSymbols());
		this.chkSymbol.addChangeListener(applyPreferencesListener);
		
		// boutons ok cancel
		this.btOK = new JButton(bundle.getString("kOK"));
		this.btOK.addActionListener(this);
		this.btUndo = new JButton(bundle.getString("kundo"));
		this.btUndo.addActionListener(this);

		//spinner nsep
		spinnerNSepCharge = IntegerSpinner.makeIntegerSpinner(Preferences.getNUM_CHARGE_SEPARATION(), 0, 1, 1);
		spinnerNSepCharge.addChangeListener(applyPreferencesListener);

		checkBoxCouple = new JCheckBox(bundle.getString("kautocouple"));
		checkBoxCouple.setSelected(Preferences.isCOUPLE_AUTOGEN());
		checkBoxCouple.addChangeListener(applyPreferencesListener);
		
		// panneau a onglets gestion de vie privee
		
		JTabbedPane tabbedPane = new JTabbedPane();
		JPanel panel1 = new JPanel();
		panel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		chkCheckVersion = new JCheckBox(bundle.getString("checkVersion"));
		chkCheckVersion.setSelected(preferencesGUI.isCheckVersion());
		chkCheckVersion.addChangeListener(applyPreferencesListener);

		lblCheckVersion = new JTextPane();
		lblCheckVersion.setContentType("text/html");
		lblCheckVersion.setEditable(false);
		lblCheckVersion.setOpaque(false);
		lblCheckVersion.setText(getTextCheckVersion(chkCheckVersion.isSelected()));
		lblCheckVersion.setPreferredSize(new Dimension(420, 80));
		lblCheckVersion.addHyperlinkListener(new FollowLink(lblCheckVersion));
				
		panel1.add(chkCheckVersion);
		panel1.add(lblCheckVersion);
		tabbedPane.addTab(bundle.getString("updates"), panel1);
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.LEFT));
		JTextPane infos = new JTextPane();
		infos.setContentType("text/html");
		infos.setEditable(false);
		infos.setOpaque(false);
		infos.setText(bundle.getString("sendInformation"));
		infos.setPreferredSize(new Dimension(420, 80));
		infos.addHyperlinkListener(new FollowLink(infos));
		panel2.add(infos);
		chkSendInformation = new JCheckBox(bundle.getString("chkSendInformation"));
		chkSendInformation.setSelected(preferencesGUI.isSendInformation());
		Informations.setFrameApp(app);
		chkSendInformation.addChangeListener(applyPreferencesListener);

		panel2.add(chkSendInformation);
		tabbedPane.addTab(bundle.getString("privacy"), panel2);
		tabbedPane.setPreferredSize(new Dimension(450, 160));
		
		// repartiteur
		
		GridBagLayout repartiteur = new GridBagLayout();
		GridBagConstraints contraintes = new GridBagConstraints();
		contenu.setLayout(repartiteur);

		
		contraintes.gridx = 1;
		contraintes.gridy = 1;
		contraintes.insets = new Insets(10, 0, 0, 0); // top padding
		contraintes.gridwidth = 3;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("kdecimal")), contraintes);
		
		contraintes.gridx = 2;
		contraintes.gridy = 2;
		contraintes.insets = new Insets(0, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("kprecisionMO")), contraintes);

		contraintes.gridx = 3;
		contraintes.gridy = 2;
		contraintes.insets = new Insets(0, 0, 0, 0); //padding
		contraintes.gridwidth =1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(spinnerMO, contraintes);

		contraintes.gridx = 2;
		contraintes.gridy = 3;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("kprecisionEnergy")), contraintes);

		contraintes.gridx = 3;
		contraintes.gridy = 3;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(spinnerEnergy, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 4;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("kMOratio")), contraintes);

		
		contraintes.gridx = 2;
		contraintes.gridy = 4;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(sliderMOratio, contraintes);
		
		
		contraintes.gridx = 3;
		contraintes.gridy = 4;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(spinnerMOratio, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 5;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 3;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("kAtomDisplay")), contraintes);
	
		contraintes.gridx = 2;
		contraintes.gridy = 6;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.chkCircle, contraintes);

		contraintes.gridx = 3;
		contraintes.gridy = 6;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.chkSymbol, contraintes);
		
		contraintes.gridx = 1;
		contraintes.gridy = 7;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 3;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(new JLabel(bundle.getString("prefAutoGen")), contraintes);
		
		JPanel panel = new JPanel();
		panel.add(new JLabel(bundle.getString("Nsep")));
		panel.add(this.spinnerNSepCharge);
						
		contraintes.gridx = 2;
		contraintes.gridy = 8;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(panel, contraintes);


		contraintes.gridx = 3;
		contraintes.gridy = 8;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.checkBoxCouple, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 9;
		contraintes.insets = new Insets(5, 0, 0, 0); //padding
		contraintes.gridwidth = 3;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(tabbedPane, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 10;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.btOK, contraintes);

		contraintes.gridx = 3;
		contraintes.gridy = 10;
		contraintes.insets = new Insets(10, 0, 0, 0); //padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.NONE;
		contraintes.anchor = GridBagConstraints.LINE_END;
		add(this.btUndo, contraintes);

		pack();
		
		setPreferredSize(new Dimension(500, 480));
		this.setSize(new Dimension(500,480));

		setLocationRelativeTo(app);
		this.setModal(true);
		this.applyValuesToUI();
		this.saveCurrentValues();
		
	}

	/**
	 * @param selected
	 * @return
	 */
	private String getTextCheckVersion(boolean selected) {
		if (!selected) 
			return "";
		else {
			
			String txt = app.getCheckVersionTxt();
			if (!app.isExecutedOnInternet() && txt.equals("")) // hulis est a jour si non execute depuis internet et pas de texte de nouvelle version
				return bundle.getString("uptodate");
			else
				return "<html>" + txt + "</html>";
		}
		
		
	}
	
}
