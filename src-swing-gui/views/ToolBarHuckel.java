package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.text.MessageFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.languages.LanguageManager;
import controller.globalCommands.IGlobalCommands;

/**
 * ToolBarHuckel : This class describes the .view for the toolbar of the applet
 * 
 * @author Nicolas GOUDARD
 * @version 2.0
 * @since
 * 
 */

@SuppressWarnings("serial")
public class ToolBarHuckel extends JPanel implements  IGlobalCommands,
		IMesomeryListener {
	public static final int ICON_WIDTH = 28;
	public static final int ICON_HEIGHT = 15;
	
	
	private FrameApp app;

	public JToggleButton btAdd = new JToggleButton();

	public JToggleButton btChange = new JToggleButton();

	public JCheckBox btDisplayHxHxy = new JCheckBox();
	public JCheckBox btDisplayCharges = new JCheckBox();
	public JCheckBox btDisplayNum = new JCheckBox();
	public JButton btErase = new JButton();
	public JButton btMoleculeAddElecCharge = new JButton();
	public JButton btMoleculeRemoveElecCharge = new JButton();

	public JToggleButton btMove = new JToggleButton();

	public JButton btQuit = new JButton();

	/*
	 * Buttons and controls of the toolbar
	 */

	public JToggleButton btRemove = new JToggleButton();

	public JToggleButton btRenum = new JToggleButton();
	public JToggleButton btRotate = new JToggleButton();
	public JButton btOptimize = new JButton();
	public JButton btCenter = new JButton();
	public JButton btShowResults = new JButton();
	MyResourceBundle bundle;
	public JLabel lblEHuckel = new JLabel();
	public JLabel lblHuckel = new JLabel();
	public JLabel lblTotalCharge = new JLabel();
	private String sCharge="";
	private String sEnergy="";

	public ToolBarHuckel(FrameApp app) {
		super();
		this.app = app;
		updateLanguage();
		distribute();
	}

	public void distribute() {

		GridBagLayout repartiteur = new GridBagLayout();
		setLayout(repartiteur);
		GridBagConstraints contraintes = new GridBagConstraints();

		this.lblHuckel.setHorizontalAlignment(SwingConstants.CENTER);
		// lblHuckel.setFont(new Font("Dialog", Font.BOLD, 25));
		this.lblHuckel.setPreferredSize(new Dimension((int) this.getPreferredSize().getWidth(),
				55));
		this.lblHuckel.setOpaque(true);
		this.lblHuckel.setBackground(new Color(0, 153, 255));
		contraintes.gridx = 0;
		contraintes.gridy = 0;
		contraintes.insets = new Insets(0, 0, 5, 0); // top padding
		contraintes.gridwidth = 4;
		contraintes.gridheight = 2;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.BOTH;
		contraintes.anchor = GridBagConstraints.NORTH;
		add(this.lblHuckel, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 2;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btAdd, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 3;
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btChange, contraintes);

		contraintes.gridx = 1;
		contraintes.gridy = 4;
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btRemove, contraintes);

		contraintes.insets = new Insets(0, 10, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 5;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.anchor = GridBagConstraints.LINE_START;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		add(this.lblTotalCharge, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 6;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.fill = GridBagConstraints.NONE;
		add(this.btMoleculeAddElecCharge, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 2;
		contraintes.gridy = 6;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.fill = GridBagConstraints.NONE;
		add(this.btMoleculeRemoveElecCharge, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 7;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.fill = GridBagConstraints.NONE;
		add(this.btMove, contraintes);

		
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 2;
		contraintes.gridy = 7;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.anchor = GridBagConstraints.CENTER;
		contraintes.fill = GridBagConstraints.NONE;
		add(this.btRotate, contraintes);
		
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 8;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btOptimize, contraintes);
		
		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 2;
		contraintes.gridy = 8;
		contraintes.gridwidth = 1;
		contraintes.gridheight = 1;
		contraintes.weightx = 0.5;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btCenter, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 9;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btRenum, contraintes);

		contraintes.insets = new Insets(0, 5, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 10;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.btDisplayNum, contraintes);

		contraintes.insets = new Insets(0, 5, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 11;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.btDisplayHxHxy, contraintes);
		
		contraintes.insets = new Insets(0, 5, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 12;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.btDisplayCharges, contraintes);
		
		contraintes.insets = new Insets(0, 10, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 13;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.LINE_START;
		add(this.lblEHuckel, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 14;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.CENTER;
		add(this.btShowResults, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 15;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 1;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.SOUTH;
		add(this.btErase, contraintes);

		contraintes.insets = new Insets(0, 0, 0, 0); // top padding
		contraintes.gridx = 1;
		contraintes.gridy = 16;
		contraintes.gridwidth = 2;
		contraintes.gridheight = 1;
		contraintes.weightx = 0;
		contraintes.weighty = 0;
		contraintes.ipadx = 0;
		contraintes.ipady = 0;
		contraintes.fill = GridBagConstraints.HORIZONTAL;
		contraintes.anchor = GridBagConstraints.SOUTH;
		add(this.btQuit, contraintes);
		
	}

	

	@Override
	public Insets getInsets() {
		return new Insets(10, 0, 10, 0);
	}

	/**
	 * this function tests if the checbox that displays Huckel parameters is checked
	 * 
	 * @param
	 */

	public boolean isCheckedHxHxy() {
		return this.btDisplayHxHxy.isSelected();
	}


	public boolean isCheckedCharges() {
		return this.btDisplayCharges.isSelected();
	}

	
	

	public void refresh() {
		updateLanguage();
	
	}

	/**
	 * this method sets the background color of the toolbar
	 * 
	 * @param background
	 *            color
	 */
	public void setBackgroundColor(Color bg) {
		super.setBackground(bg);
		this.btDisplayHxHxy.setBackground(bg);
		this.btDisplayCharges.setBackground(bg);
		this.btDisplayNum.setBackground(bg);

	}

	public void setFonts() {
		Font btFont =  (Font) this.bundle
				.getObject("font");
		Font btFont2 = new Font("Helvetica", Font.PLAIN, 11);
		this.lblTotalCharge.setFont(btFont);
		this.lblEHuckel.setFont(btFont2);
		this.btAdd.setFont(btFont);
		this.btDisplayHxHxy.setFont(btFont);
		this.btDisplayCharges.setFont(btFont);
		this.btDisplayNum.setFont(btFont);
		this.btChange.setFont(btFont);
		this.btRemove.setFont(btFont);
		this.btMove.setFont(btFont);
		this.btRotate.setFont(btFont);
		this.btOptimize.setFont(btFont);
		this.btCenter.setFont(btFont);
		this.btRenum.setFont(btFont);
		this.btShowResults.setFont(btFont);
		this.btErase.setFont(btFont);
		this.btQuit.setFont(btFont);
		this.btAdd.setOpaque(false);
		this.btMoleculeAddElecCharge.setOpaque(false);
		this.btMoleculeRemoveElecCharge.setOpaque(false);
		this.btDisplayNum.setOpaque(false);
		this.btDisplayHxHxy.setOpaque(false);
		this.btDisplayCharges.setOpaque(false);
		this.btChange.setOpaque(false);
		this.btRemove.setOpaque(false);
		this.btMove.setOpaque(false);
		this.btRotate.setOpaque(false);
		this.btOptimize.setOpaque(false);
		this.btCenter.setOpaque(false);
		this.btRenum.setOpaque(false);
		this.btShowResults.setOpaque(false);
		this.btErase.setOpaque(false);
		this.btQuit.setOpaque(false);

	}

	/**
	 * Construtor
	 * 
	 * 
	 */

	public void updateLanguage() {
		this.bundle = LanguageManager.getInstance().getResource("ToolBarHuckel");

		this.sCharge = this.bundle.getString("ktotalcharge");
		this.sEnergy = this.bundle.getString("kenergy") + "<sub>"
				+ StructureDelocalized.NAME_DELOCALIZED_STRUCTURE + "</sub> = ";

		this.lblHuckel.setText(this.bundle.getString("khuckel"));
		this.btAdd.setText(this.bundle.getString("kbuild"));
		this.btChange.setText(this.bundle.getString("kchange"));
		this.btRemove.setText(this.bundle.getString("kdelete"));
		this.btMove.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(this.bundle.getString("kimgmove"))).getImage().getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH) ) );
		this.btRotate.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(this.bundle.getString("kimgrotate"))).getImage().getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH) ) );
		
		this.btOptimize.setText(this.bundle.getString("koptimize"));
		this.btCenter.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(this.bundle.getString("kimgcenter"))).getImage().getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH) ) );
		this.btRenum.setText(this.bundle.getString("krenum"));
		this.btShowResults.setText(this.bundle.getString("kresults"));
		this.btErase.setText(this.bundle.getString("keraseall"));
		this.btMoleculeAddElecCharge.setText("-");
		this.btMoleculeRemoveElecCharge.setText("+");

		this.btDisplayHxHxy.setText(this.bundle.getString("kinfos"));
		this.btDisplayCharges.setText(this.bundle.getString("kcharges"));
		this.btDisplayNum.setText(this.bundle.getString("knum"));
		this.btQuit.setText(this.bundle.getString("kquit"));
		setFonts();
		
		refreshDelocalizedStructureInfos();

	}

	protected void refreshDelocalizedStructureInfos(){
		if (app.getCurrentMesomeryView() != null ) {
			StructureDelocalized s = this.app.getCurrentMesomeryView().getMesomery().getDelocalizedStructure();
			 if (s != null) {
				int charge = -s.getNbElecCharge();
				Energy totalEnergy = s.getTotalEnergy();
			 	Object args[] = { charge };
				this.lblTotalCharge.setText(MessageFormat.format(this.sCharge, args));
				this.lblEHuckel.setText("<html>" + this.sEnergy +totalEnergy
						+ "</html>");

			 }
			 else{
					this.lblTotalCharge.setText(MessageFormat.format(this.sCharge, new Object[]{0}));
					this.lblEHuckel.setText("<html>" + this.sEnergy +"0"+ "</html>");
			 }
		}
	}

	//@Override
	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mesomeryListWeightCalculated(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mesomeryMethodAdded(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mesomeryMethodRemoved(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	
	public void handleStructureChanged(Structure s) {
		
		if (s instanceof StructureDelocalized) 		
			refreshDelocalizedStructureInfos();
		
	}

	//@Override
	public void mesomeryStructureRemoved(MesomeryEvent e) {
		final MesomeryEvent event = e;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable(){
	
			public void run() {
				handleStructureChanged((Structure)event.getOldValue());
				
				
			}
			
		});				

	
	}

	//@Override
	public void mesomeryStructureAdded(MesomeryEvent e) {
		
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
			
		});			
	
	}

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
			
		});					
	}

	
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	////@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		// TODO Auto-generated method stub
		
	}

}
