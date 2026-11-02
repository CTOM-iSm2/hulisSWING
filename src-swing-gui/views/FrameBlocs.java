package views;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;

/**
 * This class describes the frame that displays the results
 * calculated for the moleculeAbstract
 * 
 * @version 1.3
 * @since
 * @author Nicolas GOUDARD
 * 
 */

@SuppressWarnings("serial")
public class FrameBlocs extends JFrame implements IStructureListener, IStructureLocalizedListener, IStructureDelocalizedListener, ActionListener, ListSelectionListener, IStructureReference, FocusListener {

	private static HashMap<StructureView, FrameBlocs> instances = new HashMap<StructureView, FrameBlocs>();
	public static int SPACE_TABS = 10;
	public static int X_MIN_TABS = 20;

	public static FrameBlocs getInstance(StructureView sw) {
		FrameBlocs frame = FrameBlocs.instances.get(sw);

		if (frame == null) {
			try {
				frame = new FrameBlocs(sw);
				FrameBlocs.instances.put(sw, frame);
			} catch (Exception e) {
				SwingIO.warning("FrameBlocs", "getInstance", e
						.getMessage(), e);

			}
		} 

		return frame;

	}

	protected MyResourceBundle bundle2;
	protected JButton btAjouter;
	protected JButton btSupprimer;
	protected JButton btSave;
	protected JButton btOk;
	protected JLabel lblTotal;
	
	protected JTextField txtNbElec;
	JList lstBlocs;
	StructureView structureView;
	StructureLocalized structure;
	enum Mode {ADD, CHANGE};
	Mode mode;
	
	public void actionPerformed (ActionEvent e) {
		if (e.getSource() == this.btAjouter){
			if (structure.getMesomeryParent().countAllSructs()>1)
				return;
			
			if (this.mode == Mode.ADD)
				return;
			this.txtNbElec.setText( "");			
			DefaultListModel model = (DefaultListModel) this.lstBlocs.getModel();
			model.addElement ("new");
			this.lstBlocs.setSelectedIndex(model.getSize()-1);
			this.mode = Mode.ADD;

		}
		else if (e.getSource() == this.btSupprimer){
			
			// impossible de modfier un bloc s'il y a des structures localisees dans la mesomerie
			if (structure.getMesomeryParent().countAllSructs()>1)
				return;
			
			if (this.mode == Mode.ADD) {
				// on supprime le dernier bloc : derniere de la liste
				DefaultListModel model = (DefaultListModel) this.lstBlocs.getModel();
				model.remove(model.getSize()-1);
				this.mode = Mode.CHANGE;
				return;
			}
			BlocDelocalized bloc = this.getSelectedBloc();
					
			if (bloc == null) {
				JOptionPane.showMessageDialog(this, "Please select a bloc");
				return;
			}
			this.structure.removeBlocDelocalized(bloc);
			bloc.clear();
			bloc = null;
			StructureView.setSelectedBlocDelocalized(null);
			this.mode = Mode.CHANGE;
		}
		else if (e.getSource() == this.btSave){
			// impossible de modfier un bloc s'il ya des structures loclaisees dans la mesomerie
			if (structure.getMesomeryParent().countAllSructs()>1 )
				return;
			BlocDelocalized bloc;
			if (this.mode == Mode.ADD){
				bloc = new BlocDelocalized();
				structure.addBlocDelocalized(bloc);
			}
			else
				bloc = this.getSelectedBloc();
			
			if (bloc == null) {
				JOptionPane.showMessageDialog(this, "Please select a bloc");
				return;
			}
			try {
				bloc.setNbElectron(Integer.valueOf(this.txtNbElec.getText()));
				this.mode = Mode.CHANGE;

			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "The value must be a number");
			} 
		}
		else if (e.getSource() == this.btOk){
			StructureView.setSelectedBlocDelocalized(null);
			this.structureView.repaint();
			this.dispose();
			
		}	

	}

	/**
	 * FrameResultStruct : Constructor
	 */

	protected FrameBlocs(final StructureView structureView) throws Exception {
		super();
	
		if (structureView == null)
			throw new Exception("structureView must be initialized");
		
		this.structureView = structureView;
		this.structure = (StructureLocalized) structureView.getStructure();
		structure.addListener(this);
		
		this.txtNbElec = new JTextField(5);
		
		this.lblTotal = new JLabel();
			
		this.btAjouter = new JButton ("+");
		this.btSupprimer = new JButton ("-");
		this.btSave = new JButton ("Save");
		this.btOk = new JButton ("Ok");;
		
		this.btAjouter.addActionListener(this);
		this.btSupprimer.addActionListener(this);
		this.btSave.addActionListener(this);
		this.btOk.addActionListener(this);
	
		this.setTitle(MesomeryView.PSI + this.structure.getName() + " blocs");
		
		this.lstBlocs = new JList();
		this.lstBlocs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstBlocs.addListSelectionListener(this);
		Container pane = this.getContentPane();
		JScrollPane scrollPane = new JScrollPane(this.lstBlocs);
		scrollPane.getViewport().setView(this.lstBlocs);
		 	
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();	

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 5, 0, 0); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		pane.add(new JLabel("Blocs list :"), c);
		
		
		c.gridx = 2;
		c.gridy = 0;
		c.insets = new Insets(5, 0, 5, 0); //
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		pane.add(new JLabel ("Selected bloc :"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0); //
		c.gridwidth = 2;
		c.gridheight = 2;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		pane.add(scrollPane, c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.insets = new Insets(5, 10, 0, 0); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		pane.add(new JLabel ("Nb e- :"), c);
				
		
		c.gridx = 3;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 5); // 
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.txtNbElec, c);
		
		
		
		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 0, 5); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.btSave, c);	

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 0); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.lblTotal, c);	
		
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(5, 5, 0, 0); // 
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.btAjouter, c);
		this.btAjouter.setEnabled(false);
		
		c.gridx =1;
		c.gridy =4;
		c.insets = new Insets(5, 0, 0, 0); // 
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 1;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.btSupprimer, c);

		
		c.gridx =3;
		c.gridy = 5;
		c.insets = new Insets(5, 0, 5, 5); // 
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		pane.add(this.btOk, c);

		this.setSize(320,250);
		
		final FrameBlocs frameExcitations = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frameExcitations.dispose();
			}
		});
		
		showBlocs();
		
		this.addFocusListener(this);

	}
	
	
	public void showBlocs() {
		
		DefaultListModel model = new DefaultListModel();
		String s;
		int i = 0;
		for (BlocDelocalized bloc : structure.getListBlocDelocalized()) {
			s = "bloc " + i;
			i++;
			model.addElement(s);
			
		}
		
		this.lstBlocs.setModel(model);
		this.lblTotal.setText("Total : "+ this.structure.getListBlocDelocalized().size());
		this.mode = Mode.CHANGE;

	}

	//@Override
	public void dispose() {
		FrameBlocs.instances.remove(this.structure);
		this.structure.removeListener(this);
		super.dispose();
	}

	
	
	public Structure getStructure() {
		return this.structure;
	}

	

	public void moleculeDeleted(MoleculeEvent e) {
		final FrameBlocs frame = this;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				structure.removeListener(frame);
				dispose();
			}
			
		});			


	}

	public void setStructure(Structure s) {
		this.structure.removeListener(this);
		this.structure = (StructureLocalized) s;
		this.structure.addListener(this);
		this.showBlocs();
	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
		
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
		
	}



	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {

		
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {

	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		
	}



	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {

	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		
	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {

	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
	
		
	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {

		
	}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {

		
	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		//TODO
	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {

	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			

		
	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
		
	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
		
	}

	//////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showBlocs();
			}
			
		});			
	}


	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		if (!listSelectionEvent.getValueIsAdjusting() ) // cette ligne est necessaire car valueChanged est appelee deux fois a cause de click de souris
			return;
			
		if (this.mode == Mode.ADD) {
			// on supprime le nouveau bloc : dernier de la liste
			DefaultListModel model = (DefaultListModel) this.lstBlocs.getModel();
			model.remove(model.getSize()-1);
		}
	
		BlocDelocalized bloc =  this.getSelectedBloc();
		if (bloc != null) {
			this.txtNbElec.setText(Integer.toString(bloc.getNbElectron()));
			
			StructureView.setSelectedBlocDelocalized(bloc);
			this.structureView.repaint();
		}
		this.mode = Mode.CHANGE;

	}

	
	private BlocDelocalized getSelectedBloc() {
		if (this.lstBlocs.getSelectedIndex() <0)
			 return null;
		return  this.structure.getListBlocDelocalized().get(this.lstBlocs.getSelectedIndex());
	}

	public void focusGained(FocusEvent arg0) {
		BlocDelocalized bloc =  this.getSelectedBloc();
		System.out.println("focus gained")
		;		if (bloc != null) {
			
			StructureView.setSelectedBlocDelocalized(bloc);
			this.structureView.repaint();
		}		
	}

	public void focusLost(FocusEvent arg0) {
		StructureView.setSelectedBlocDelocalized(null);
		System.out.println("focus lost")
;		this.structureView.repaint();
	
	}
}
