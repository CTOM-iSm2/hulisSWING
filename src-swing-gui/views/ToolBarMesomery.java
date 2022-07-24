package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.TreeMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.comparators.MethodNameComparator;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.SelectionException;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.selectors.MesomeryEnergyStructuresSelector;
import org.ctom.hulis.huckel.selectors.MesomeryHLPOverlapStructuresSelector;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.util.maths.Maths;

import util.DoubleJSlider;
import util.MyResourceBundle;
import util.SwingNumberFormater;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import controller.globalCommands.ActionStopConsumingProcess;
import controller.globalCommands.IGlobalCommands;

@SuppressWarnings("serial")
public class ToolBarMesomery extends JPanel implements IMesomeryListener,
IMesomeryReference, IGlobalCommands, ChangeListener {

	public final static double DEFAULT_VALUE_SLIDER = 0.0;
	public final static double MAX_ENERGY_SLIDER = 0.0;
	public final static double MIN_ENERGY_SLIDER = -100.0;

	public static final DecimalFormat FORM = new DecimalFormat("#.#");
	protected static String lblBtResults;
	public static final int PRECISION = FrameApp.PRECISION;

	protected String txtLblEmax = "";
	protected FrameApp app;

	public JButton btAutoGenStruct = new JButton();
	public JButton btStopAutoGenStruct = new JButton();
	public JToggleButton btChangeElectrons = new JToggleButton();

	public JToggleButton btCouple = new JToggleButton();
	public JToggleButton btBloc = new JToggleButton();
	public JButton btEndBloc = new JButton();
	public JButton btDuplicate = new JButton();
	public JButton btErase1 = new JButton();
	public JButton btEraseMesomery = new JButton();
	public JButton btShowResultsMesomery = new JButton();
	private MyResourceBundle bundle;
	public JLabel lblEnergy = new JLabel(txtLblEmax);
	public JLabel lblMesomery = new JLabel();
	public JLabel lblWeight = new JLabel();
	public JLabel lblOrder = new JLabel();

	protected Mesomery mesomery;

	public DoubleJSlider slider;
	private volatile boolean isWaiting = false;
	private boolean adjustSliderBecauseMaxValueForSelectionChanged = false;
	
	
	// private JTextField txtEnergyMax;

	/**
	 * 
	 * @param app
	 * @param mesomery
	 */
	public ToolBarMesomery(FrameApp app, Mesomery mesomery) {
		super();

		this.app = app;
		this.mesomery = mesomery;

		mesomery.addListener(this);
		this.slider = new DoubleJSlider("", ToolBarMesomery.DEFAULT_VALUE_SLIDER,
				ToolBarMesomery.MIN_ENERGY_SLIDER, ToolBarMesomery.MAX_ENERGY_SLIDER);
		this.slider.setToolTipText(null);
		this.slider.addChangeListener(this);

		distribute();
		updateLanguage();
		this.slider.setPrecision(ToolBarMesomery.PRECISION);
		this.slider.setOrientation(SwingConstants.VERTICAL);
		this.slider.setDoubleValue(ToolBarMesomery.MIN_ENERGY_SLIDER);


		/*
		 * final Mesomery thisMesomery = mesomery;
		 * 
		 * 
		 * btShowResultsMesomery.addActionListener(new ActionListener() { public
		 * void actionPerformed(ActionEvent e) { try { new
		 * FrameResultMesomery(thisMesomery).setVisible(true); } catch
		 * (Exception e1) { e1.printStackTrace(); } }
		 * 
		 * });
		 */
	}

	/**
	 * 
	 * @param e
	 */
	public void calculated(MesomeryEvent e) {

		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				updateInfoStructure(app.getCurrentMesomeryView().getCurrentStructure());


			}

		});
	}

	/**
	 * 
	 * @return
	 */
	public  TreeMap<Double, Integer> countStructsByEnergy() {

		TreeMap<Double, Integer> tblCountStructsByEnergy = new TreeMap<Double, Integer>();

		Object o;
		Double e;

		for (Structure s : this.mesomery.getAllStructures()) {
			e = Maths.round(s.getTotalEnergy().getBeta(), ToolBarMesomery.PRECISION);
			try {
				o = tblCountStructsByEnergy.get(e);
				if (o != null)
					tblCountStructsByEnergy.put(e, (Integer) o + 1);
				else
					tblCountStructsByEnergy.put(e, 1);
			} catch (ClassCastException e1) {
				SwingIO.error(this.getClass().getName(), "TreeMap",
						"error slider !" + e1.getMessage(), e1);
				SwingIO.reportError(app);
			}

		}

		return tblCountStructsByEnergy;
	}
	

	
	
	/**
	 * 
	 * @return
	 */
	public synchronized TreeMap<Double, Integer> countStructsByOverLap() {

		TreeMap<Double, Integer> tblCountStructsByOverlap = new TreeMap<Double, Integer>();
		
		Object o;
		Double e;

		for (StructureLocalized s : this.mesomery.getLocalizedStructures()) {
			e = Math.abs(Maths.round(s.getOverlapHLP(), ToolBarMesomery.PRECISION));
			try {
				o = tblCountStructsByOverlap.get(e);
				if (o != null)
					tblCountStructsByOverlap.put(e, (Integer) o + 1);
				else
					tblCountStructsByOverlap.put(e, 1);
			} catch (ClassCastException e1) {
				SwingIO.error(this.getClass().getName(), "TreeMap",
						"error slider !" + e1.getMessage(), e1);
				SwingIO.reportError(app);
			}

		}

		return tblCountStructsByOverlap;
	}

	/**
	 * 
	 */
	public void debug() {
		TreeMap<Double, Integer> tbl = countStructsByEnergy();

		if (tbl == null)
			return;

		SwingIO
		.PrintIf("*********** Count of structs by Energy **************");
		for (Double e : tbl.keySet()) {
			SwingIO.PrintIf("Energy : " + e + " beta --- count : "
					+ tbl.get(e));

		}
	}

	/**
	 * 
	 */
	public void distribute() {

		GridBagLayout repartiteur = new GridBagLayout();
		setLayout(repartiteur);
		GridBagConstraints contraintes;
		contraintes = new GridBagConstraints();

		contraintes.gridx = 0;
		contraintes.gridy = 0;
		contraintes.insets = new Insets(0, 0, 5, 0); // top padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.anchor = GridBagConstraints.NORTH;
		add(this.lblMesomery, contraintes);

		contraintes.gridx = 0;
		contraintes.gridy = 1;
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btAutoGenStruct, contraintes);
		add(this.btStopAutoGenStruct, contraintes);
		this.btStopAutoGenStruct.setVisible(false);
		this.btAutoGenStruct.setVisible(true);

		contraintes.gridx = 0;
		contraintes.gridy = 2;
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btDuplicate, contraintes);

		contraintes.gridx = 0;
		contraintes.gridy = 3;
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		repartiteur.setConstraints(this.btChangeElectrons, contraintes);
		add(this.btChangeElectrons, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 4;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(btCouple, contraintes);
		//correction premiere partie du bug numero 10
		this.btCouple.setVisible(this.app.isExpertMode());
		//correction premiere partie du bug numero 10

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 5;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btBloc, contraintes);
		//correction premiere partie du bug numero 10
		this.btBloc.setVisible(this.app.isExpertMode());
		//correction premiere partie du bug numero 10

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 6;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btEndBloc, contraintes);
		//correction premiere partie du bug numero 10
		this.btEndBloc.setVisible(false);
		//correction premiere partie du bug numero 10



		contraintes.insets = new Insets(0, 10, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 7;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.lblEnergy, contraintes);


		contraintes.insets = new Insets(0, 10, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 8;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.lblWeight, contraintes);


		contraintes = new GridBagConstraints();
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 9;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btShowResultsMesomery, contraintes);

		
		contraintes = new GridBagConstraints();
		contraintes.insets = new Insets(5, 50, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 10;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.lblOrder, contraintes);

		
		contraintes = new GridBagConstraints();
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 11;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 19;
		contraintes.weightx = 0;
		contraintes.weighty = 1;
		contraintes.fill = GridBagConstraints.VERTICAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		add(this.slider, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 30;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btErase1, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 0;
		contraintes.gridy = 31;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		repartiteur.setConstraints(this.btEraseMesomery, contraintes);
		add(this.btEraseMesomery, contraintes);
	}

	

	/**
	 * 
	 * @param value
	 */
	public void adjustSlider (double value) {
		slider.removeChangeListener(this);
		slider.setDoubleValue(value);

		slider.addChangeListener(this);
	}

	@Override
	public Insets getInsets() {
		return new Insets(10, 0, 10, 0);
	}

	/* (non-Javadoc)
	 * @see views.IMesomeryReference#getMesomery()
	 */
	public Mesomery getMesomery() {
		return this.mesomery;
	}


	/*
	 * public void actionPerformed (ActionEvent e) { if
	 * (e.getSource().equals(txtEnergyMax)) {
	 * slider.setDoubleValue(-Double.valueOf(txtEnergyMax.getText()));
	 * slider.setValueIsAdjusting(true); } }
	 */

	/*
	 * public void updateTxtEnergyMax () {
	 * txtEnergyMax.setText(Double.toString(-slider.getDoubleValue())); }
	 */

	/**
	 * @return
	 */
	public DoubleJSlider getSlider() {
		return this.slider;
	}



	/**
	 * 
	 */
	public void refresh() {
		updateLanguage();
		refreshSlider();
		updateInfoStructure(this.app.getCurrentMesomeryView().getCurrentStructure());
	}

	/**
	 * 
	 */
	public void refreshSlider() {
		this.slider.removeChangeListener(this);// A METTRE PARCE QUE LE SLIDER LANCE UN EVT CHANGE QUAND REFRESH
		this.slider.setValueIsAdjusting(false);
		
		
		if (this.app.getCurrentMesomery().getStructureSelector() instanceof MesomeryHLPOverlapStructuresSelector)
			refreshSliderHLP();
		else
			refreshSliderHLCI();
		



		this.slider.addChangeListener(this); // A METTRE PARCE QUE LE SLIDER LANCE UN EVT CHANGE QUAND REFRESH

	}

	/**
	 * 
	 */
	private void refreshSliderHLP() {
		TreeMap<Double, Integer> tbl = countStructsByOverLap();

		if (tbl == null)
			return;
		
		
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		
		
		
		double min, max;

		if (!(tbl.isEmpty())) {
			min = -((Double) tbl.lastKey());
			max = -((Double) tbl.firstKey());

			for (Double e : tbl.keySet()) {
					labels
					.put(
							(int)(-e
									* Math.pow(10.0,
											ToolBarMesomery.PRECISION))
							,
							new JLabel(
									"<html><head><style type=\"text/css\">.couleur0 {font-family:\"Arial\"; font-size:8px;} .couleur{font-family:\"Arial\"; font-size:8px; color: #FF0000;}</style></head><body><span class=\"couleur0\">"
											+ e
											+ " </span><span class=\"couleur\">("
											+ tbl.get(e)
											+ ")</span></body></head></html>"));

			}
			if (min == max)
				max = 0;
			//if ((min == 0) && (max == 0))
				//max = 1;
			this.slider.setDoubleMinimum(min);
			this.slider.setDoubleMaximum(max);
			this.slider.setLabelTable(labels); // ----- PUT THE LABELS IN THE
			// SLIDER !!!
			// slider.setDoubleMinorTickSpacing(Math.pow(10.0, -1));
			this.slider.setDoubleMajorTickSpacing(5 * Math.pow(10.0, -1));
			this.slider.setPaintLabels(true);
			this.slider.setPaintTicks(false); // true
		} else
			this.slider.setPaintLabels(false);

	}
	
	/**
	 * 
	 */
	private void refreshSliderHLCI() {
				
		TreeMap<Double, Integer> tbl = countStructsByEnergy();

		if (tbl == null)
			return;

		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		
		double min, max;

		if (!(tbl.isEmpty())) {
			min = -((Double) tbl.lastKey());
			max = -((Double) tbl.firstKey());

			for (Double e : tbl.keySet()) {
				if ((e == -min) && (tbl.get(e)<2)) //cas particulier : psiTot ou le nombre de structures
					labels
					.put(
							(int) (-e
									* Math.pow(10.0,
											ToolBarMesomery.PRECISION)),
							new JLabel(
									("<html><head><style type=\"text/css\">.couleur0 {font-family:\"Arial\"; font-size:8px;} .couleur{font-family:\"Arial\"; font-size:8px; color: #FF0000;}</style></head><body><span class=\"couleur0\">"
											+ (new Energy(0, e))
											.betaToString()
											+ " </span><span class=\"couleur\">("
											+ this.bundle
											.getString("phi")
											+ "<sub>"
											+ StructureDelocalized.NAME_DELOCALIZED_STRUCTURE + "</sub>)</span></body></head></html>")));
				else
					labels
					.put(
							(int)(-e
									* Math.pow(10.0,
											ToolBarMesomery.PRECISION))
							,
							new JLabel(
									"<html><head><style type=\"text/css\">.couleur0 {font-family:\"Arial\"; font-size:8px;} .couleur{font-family:\"Arial\"; font-size:8px; color: #FF0000;}</style></head><body><span class=\"couleur0\">"
											+ (new Energy(0, e))
											.betaToString()
											+ " </span><span class=\"couleur\">("
											+ tbl.get(e)
											+ ")</span></body></head></html>"));

			}
			if (min == max)
				max = 0;
			if ((min == 0) && (max == 0))
				max = 1;
			this.slider.setDoubleMinimum(min);
			this.slider.setDoubleMaximum(max);
			this.slider.setLabelTable(labels); // ----- PUT THE LABELS IN THE
			// SLIDER !!!
			// slider.setDoubleMinorTickSpacing(Math.pow(10.0, -1));
			this.slider.setDoubleMajorTickSpacing(5 * Math.pow(10.0, -1));
			this.slider.setPaintLabels(true);
			this.slider.setPaintTicks(true);
		} else
			this.slider.setPaintLabels(false);
	}
	
	
	
	/**
	 * 
	 * @param bg
	 */
	public void setBackgroundColor(Color bg) {
		super.setBackground(bg);
		this.slider.setBackground(bg);
	}

	/**
	 * 
	 */
	public void setFonts() {
		Font btFont = (Font) bundle.getObject("font"); 
		Font btFont3 = new Font("Helvetica", Font.PLAIN, 11);
		Font btFont4 = new Font("Helvetica",Font.BOLD, 11);
		this.lblEnergy.setFont(btFont3);
		this.btAutoGenStruct.setFont(btFont);
		this.btStopAutoGenStruct.setFont(btFont);
		this.btDuplicate.setFont(btFont);
		this.btChangeElectrons.setFont(btFont);
		this.btCouple.setFont(btFont);
		this.btBloc.setFont(btFont);
		this.btEndBloc.setFont(btFont);
		this.btShowResultsMesomery.setFont(btFont);
		this.btErase1.setFont(btFont);
		this.btEraseMesomery.setFont(btFont);
		this.btAutoGenStruct.setOpaque(false);
		this.btStopAutoGenStruct.setOpaque(false);
		this.btDuplicate.setOpaque(false);
		this.btChangeElectrons.setOpaque(false);
		this.btCouple.setOpaque(false);
		this.btBloc.setOpaque(false);
		this.btEndBloc.setOpaque(false);
		this.btShowResultsMesomery.setOpaque(false);
		this.btErase1.setOpaque(false);
		this.btEraseMesomery.setOpaque(false);
		this.lblOrder.setFont(btFont3);
		this.lblWeight.setFont(btFont4);
		this.lblWeight.setForeground(Color.RED);


		this.lblMesomery.setHorizontalAlignment(SwingConstants.CENTER);
		this.lblMesomery.setPreferredSize(new Dimension(
				(int) this.getPreferredSize().getWidth(), 55));
		this.lblMesomery.setOpaque(true);
		this.lblMesomery.setBackground(new Color(255, 102, 0));

	}

	/**
	 * 
	 */
	public void updateLanguage() {
		this.bundle = LanguageManager.getInstance().getResource("ToolBarMesomery");
		txtLblEmax = "<html>"
				+ this.bundle.getString("kenergy") + "<sub>"
				+ StructureDelocalized.NAME_DELOCALIZED_STRUCTURE
				+ "</sub> =</html>";
		ToolBarMesomery.lblBtResults = this.bundle.getString("kresults");

		this.lblMesomery.setText(this.bundle.getString("kmesomery"));
		this.btAutoGenStruct.setText(this.bundle.getString("kgenerateall"));
		this.btStopAutoGenStruct.setText("STOP");
		this.btDuplicate.setText(this.bundle.getString("kcreate1"));
		this.btChangeElectrons.setText(this.bundle.getString("kchangebonds"));
		this.btCouple.setText(this.bundle.getString("kcouple"));
		this.btBloc.setText(this.bundle.getString("kbloc"));
		this.btEndBloc.setText(this.bundle.getString("kendbloc"));
		this.btCouple.setText(this.bundle.getString("kcouple"));
		this.btShowResultsMesomery.setText(this.bundle.getString("kresults"));
		this.btErase1.setText(this.bundle.getString("kerase1"));
		this.btEraseMesomery.setText(this.bundle.getString("kerasem"));
		this.lblEnergy.setText(txtLblEmax);

		setFonts();
		Structure s = null;
		if (this.app.getCurrentMesomeryView() != null)
			s = this.app.getCurrentMesomeryView().getCurrentStructure();
		updateInfoStructure(s);

	}
	
	/* (non-Javadoc)
	 * @see views.IMesomeryReference#setMesomery(org.ctom.hulis.huckel.Mesomery)
	 */
	public void setMesomery(Mesomery mesomery) {
		this.mesomery.removeListener(this);
		this.mesomery = mesomery;
		this.mesomery.addListener(this);
		this.refresh();
	}

	// SLIDER DRAG EVENT
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(this.slider)) {
			
			if (this.slider.getValueIsAdjusting() || adjustSliderBecauseMaxValueForSelectionChanged ) {
				adjustSliderBecauseMaxValueForSelectionChanged =false;
				return;

			}
			if (mesomery ==null) // pour eviter le null au lancement de l'applet
				return;
			if (mesomery.getDelocalizedStructure() == null)// egalement pour eviter le null au lancement de l'applet
				return;
			final double treshold;
		//	if (this.mesomery.getStructureSelector() instanceof MesomeryEnergyStructuresSelector)
				treshold = -this.slider.getDoubleValue();
			//else if (this.mesomery.getStructureSelector() instanceof MesomeryHLPOverlapStructuresSelector)
		//		treshold = -this.slider.getDoubleValue();
			///else
				//treshold = 0.0;
			SwingIO.log(this.getClass().getName(), "stateChanged", "adjust slider to treshold = " + treshold);
			app.registerAction("Slider");

			slider.setEnabled(false);	
			new Thread(new Runnable() {

				public void run() {
					try {

						Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
						stop.join();
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {
								slider.setEnabled(true);			
							}});
						mesomery.setSelectionTreshold(treshold);

					} catch (CoupleException e1) {
						final Exception exception = e1;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {
								SwingIO.error(this.getClass().getName(), "insertTXT", exception
										.getMessage(), exception);
								SwingIO.reportError(app);

							}});

					} catch (IMethodException e1) {
						final Exception exception = e1;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {
								SwingIO.error(this.getClass().getName(), "insertTXT", exception
										.getMessage(), exception);
								SwingIO.reportError(app);
							}});

					} catch (MesomeryNoStructureLocalizedException e1) {
						final Exception exception = e1;
						ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

							public void run() {

								SwingIO.error(this.getClass().getName(), "insertTXT", exception
										.getMessage(), exception);
								SwingIO.reportError(app);
							}});
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SelectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}}).start();

		}

	}


	/**
	 * @param s
	 */
	public void updateInfoStructure(Structure s) {
		if (s instanceof StructureLocalized) {
			this.lblEnergy.setText("<html>E<sub>" + s.getName() + "</sub> = "
					+ s.getTotalEnergy() + "</html>");
			if (((StructureLocalized) s).getError() == StructureLocalized.Error.ERROR_INCORRECT_NB_PI_ELEC)
				this.lblWeight.setText("<html>" + this.bundle.getString("phi")
						+ "<sub>" + s.getName() + "</sub> "
						+ this.bundle.getString("kincomplete") +  "</html>");
			else if (((StructureLocalized) s).getError() == StructureLocalized.Error.ERROR_ALREADY_EXIST) {
				this.lblWeight.setText("<html>w" + "<sub>" + s.getName()
						+ "</sub> " + this.bundle.getString("exist")
						+ "</html>");
			} else if (this.mesomery.listIncorrectStructPiElecNumber()
					.isEmpty()) {

				IMethod[] methods = this.mesomery.getMethods();
				Arrays.sort(methods, new MethodNameComparator());
				if (methods == null)
					this.lblWeight.setText("<html>&nbsp;<br>&nbsp;<br></html>"); // 2 lignes vides
				else {
					String txtW = "";
					
					// si le calcul des structures mesomeres est en cours, on affiche une animation de points de suspensions
					if (this.mesomery.isCalculatingWeights()) {
						Thread waitThread = new Thread(new Runnable(){

							public void run() {
								int nbPoints = 1;
								String strPoints;
								final String txtCompute = bundle.getString("compute");
								isWaiting = true;
								while (mesomery.isCalculatingWeights()) {
									nbPoints = nbPoints % 10 + 1;
									strPoints = "";
									for (int i = 0; i< nbPoints; i++)
										strPoints += ".";

									final String strPointsToDisplay = strPoints;
									ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

										public void run() {
											if (mesomery.isCalculatingWeights())
												lblWeight.setText(txtCompute+strPointsToDisplay);

										}} );

									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
								isWaiting = false;

							}});
						if (!isWaiting)
							waitThread.start();
					}
					else {
						for (IMethod method : methods) {
							// on affiche le poids de la structure que si les poids ont ete calcules cad method.getWeights non vide
							if (method.getWeights().length != 0) {
								if (txtW != "")
									txtW +="<br>";
								String methodName = method.getName();
								txtW += "w"
										+ "<sub>"
										+ s.getName()
										+ "</sub> = "
										+ SwingNumberFormater.doubleToString(((StructureLocalized) s)
												.getWeight(methodName) * 100 , FORM, "%") + "("
												+ methodName + ")";
							}
						}
						this.lblWeight.setText("<html>" + txtW +"</html>");
					}
				}	

			} else
				this.lblWeight.setText("<html>&nbsp;<br>&nbsp;<br></html>"); // 2 lignes vides
		} else { // si sutructure DELOCALISEE
			if ((this.mesomery.countSelectedLocalizedStructs() == 0)
					|| (!this.mesomery.listIncorrectStructPiElecNumber()
							.isEmpty())) {
				this.lblEnergy.setText("");
				this.lblWeight.setText("<html>&nbsp;<br>&nbsp;<br></html");
				return;
			}

			IMethod[] methods = this.mesomery.getMethods();
			Arrays.sort(methods, new MethodNameComparator());
			if (methods == null)
				this.lblEnergy.setText("");
			else {
				String tau = "";
				for (IMethod method : methods) {

					String methodName = method.getName();
					if (this.mesomery.getMethod(methodName).isImplementedTau()) {
						if (tau != "") 
							tau+="<br>";

						tau += MesomeryView.TAU
								+ "<sub>" + method.getName() +"</sub>"
								+ " = "
								+ SwingNumberFormater.doubleToString(this.mesomery
										.getMethod(methodName).getTau() * 100, FORM, "%");
						//else
						//	tau+= Gallery.TAU + " = NA";
					}
				}
				this.lblEnergy.setText("<html>" + tau + "</html>");

			}

			this.lblWeight
			.setText("<html>n<sub>CI</sub> = "
					+ this.mesomery.countSelectedLocalizedStructs()
					+ "</html>");

		} /// fin si structure delocalisee
	}



	//@Override
	public  void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		this.adjustSliderBecauseMaxValueForSelectionChanged  = true;
		this.slider.setDoubleValue(-(Double)e.getNewValue());
	}
	
	/** 
	 * use this method only for tests
	 */
	public synchronized void setValueManually(double value) {
	//	this.adjustSlider(value);

		try {
			mesomery.setSelectionTreshold(-value);
		} catch (SelectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoupleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MesomeryNoStructureLocalizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

				
	
				
	}
	

	//@Override
	public void mesomeryListWeightCalculated(MesomeryEvent e) {
		
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refresh();

			}

		});

	}

	//@Override
	public void mesomeryMethodAdded(MesomeryEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refresh();

			}

		});


	}

	//@Override
	public void mesomeryMethodRemoved(MesomeryEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refresh();

			}

		});


	}

	//@Override
	public void mesomeryStructureAdded(MesomeryEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refreshSlider();
				adjustSlider(-(Double)mesomery.getSelectionTreshold());

			}

		});


	}

	public void handleStructureChanged(Structure s) {
		refreshSlider();
			
		if (mesomery.getStructureSelector().isCurrentValueLowerThanTreshold(s) && 
				s == app.getCurrentMesomeryView().getCurrentStructure()) 
			adjustSlider(-(Double) mesomery.getStructureSelector().getCurrentValue(s));

		updateInfoStructure(s);

	}

	//@Override
	public void mesomeryStructureRemoved(MesomeryEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				if (mesomery.countLocalizedStructs() == 0) {
					slider.setDoubleValue(ToolBarMesomery.DEFAULT_VALUE_SLIDER);
				}
				updateInfoStructure(app.getCurrentMesomeryView().getCurrentStructure());

				refreshSlider();
				if (app.getCurrentMesomeryView().countDisplayedStructures() > 0)
					updateInfoStructure(app.getCurrentMesomeryView().getCurrentStructure());


			}

		});

	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		// TODO Auto-generated method stub
	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});			}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeChargeChanged(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeDeleted(MoleculeEvent e) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});		
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent e) {
		final MoleculeEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource());
				
				
			}
			
		});			}



	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		final HuckelAtomEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource().getMoleculeContainer());
				
				
			}
			
		});



	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		final HuckelAtomEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				handleStructureChanged((Structure) (event.getSource().getMoleculeContainer()) );

			}

		});


	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		final HuckelAtomEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource().getMoleculeContainer());
				
				
			}
			
		});			

	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		final HuckelBondEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				handleStructureChanged((Structure) (event.getSource().getMoleculeContainer()) );

			}

		});


	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		final HuckelBondEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource().getMoleculeContainer());
				
				
			}
			
		});			

	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		final MonoExcitationEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged( (Structure) event.getSource().getStructure());
				
				
			}
			
		});			
		
	}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refresh();


			}

		});

	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {

		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				refresh();


			}

		});


	}

	////@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		final BlocDelocalizedEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				handleStructureChanged((Structure) (event.getSource().getMoleculeParent()) );


			}

		});


	}

	////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		final BlocDelocalizedEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){

			public void run() {
				handleStructureChanged((Structure) (event.getSource().getMoleculeParent()) );


			}

		});

	}

	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		// TODO Auto-generated method stub

	}

	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		this.updateInfoStructure(this.app.getCurrentMesomeryView().getCurrentStructure());

	}

	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		if (event.getNewValue() instanceof MesomeryEnergyStructuresSelector)
			this.lblOrder.setText("<html>E<sub>i</sub></html>");
		else
			if (event.getNewValue() instanceof MesomeryHLPOverlapStructuresSelector)
				this.lblOrder.setText("<html>&#60;"+ MesomeryView.PSI+"<sub>tot</sub>|"+ MesomeryView.PSI+"<sub>i</sub>&#62;</html>");
			
				
		
	}


}
