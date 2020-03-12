package views;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.MonoExcitationException;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;

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
public class FrameExcitations extends JFrame implements IStructureListener, IStructureLocalizedListener, IStructureDelocalizedListener, ActionListener, ListSelectionListener, IStructureReference {

	private static HashMap<Structure, FrameExcitations> instances = new HashMap<Structure, FrameExcitations>();
	public static int SPACE_TABS = 10;
	public static int X_MIN_TABS = 20;

	public static FrameExcitations getInstance(Structure s) {
		FrameExcitations frame = FrameExcitations.instances.get(s);

		if (frame == null) {
			try {
				frame = new FrameExcitations(s);
				FrameExcitations.instances.put(s, frame);
			} catch (Exception e) {
				SwingIO.warning("FrameExcitations", "getInstance", e
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
	protected JTextField txtFrom;
	protected JTextField txtTo;
	JList lstExcitations;
	Structure structure;
	enum Mode {ADD, CHANGE};
	Mode mode;
	
	public void actionPerformed (ActionEvent e) {
		if ( (e.getSource() == this.btAjouter) && (structure instanceof StructureDelocalized) && (this.structure.getMesomeryParent().countAllSructs() == 1)) {
			if (this.mode == Mode.ADD)
				return;
			this.txtFrom.setText( "");			
			this.txtTo.setText( "");
			DefaultListModel model = (DefaultListModel) this.lstExcitations.getModel();
			model.addElement ("new");
			this.lstExcitations.setSelectedIndex(model.getSize()-1);
			this.mode = Mode.ADD;

		}
		else if ((e.getSource() == this.btSupprimer) && (structure instanceof StructureDelocalized) && (this.structure.getMesomeryParent().countAllSructs() == 1) ) {
			if (this.mode == Mode.ADD) {
				// on supprime la nouvelle monoexcitation : derniere de la liste
				DefaultListModel model = (DefaultListModel) this.lstExcitations.getModel();
				model.remove(model.getSize()-1);
				this.mode = Mode.CHANGE;
				return;
			}
			MonoExcitation monoExcitation = this.getSelectedMonoExcitation();
					
			if (monoExcitation == null) {
				JOptionPane.showMessageDialog(this, "Please select an excitation");
				return;
			}
			this.structure.removeMonoExcitation(monoExcitation);
			this.mode = Mode.CHANGE;
		}
		else if ((e.getSource() == this.btSave) && (structure instanceof StructureDelocalized) && (this.structure.getMesomeryParent().countAllSructs() == 1)){
			MonoExcitation monoExcitation;
			// on sauvegarde la monoexcitation
			if (this.mode == Mode.ADD)
				monoExcitation = new MonoExcitation(structure);
			else
				monoExcitation = this.getSelectedMonoExcitation();
			
			if (monoExcitation == null) {
				JOptionPane.showMessageDialog(this, "Please select an excitation");
				return;
			}
			try {
				monoExcitation.setFrom(this.structure.getSpinOM(Integer.valueOf(this.txtFrom.getText())));
				monoExcitation.setTo(this.structure.getSpinOM(Integer.valueOf(this.txtTo.getText())));
				monoExcitation.check();
				this.mode = Mode.CHANGE;

			} catch (NumberFormatException e1) {
					e1.printStackTrace();
				JOptionPane.showMessageDialog(this, "The value must be a number");
			} catch (MonoExcitationException e1) {
					e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMessage());
			} catch (NoSOMAvailableException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMessage());
			} catch (UnavailableSOMException e1) {
				e1.printStackTrace();
				JOptionPane.showMessageDialog(this, e1.getMessage());
			}
		}
		else if (e.getSource() == this.btOk){
			this.dispose();
			
		}	

	}
	


	/**
	 * FrameResultStruct : Constructor
	 */

	protected FrameExcitations(final Structure structure) throws Exception {
		super();
	
		if (structure == null)
			throw new Exception("structure must be initialized");
		this.structure = structure;
		structure.addListener(this);
		
		this.txtFrom = new JTextField(5);
		this.txtTo = new JTextField(5);
		
		this.lblTotal = new JLabel();
		
		this.btAjouter = new JButton ("+");
		this.btSupprimer = new JButton ("-");
		this.btSave = new JButton ("Save");
		this.btOk = new JButton ("Ok");;
		
		this.btOk.addActionListener(this);
		if (structure instanceof StructureDelocalized){
			this.btAjouter.addActionListener(this);
			this.btSupprimer.addActionListener(this);
			this.btSave.addActionListener(this);
		
		}
		else {
			this.btAjouter.setEnabled(false);
			this.btSupprimer.setEnabled(false);
			this.btSave.setEnabled(false);
			
		}
	
		this.setTitle(MesomeryView.PSI + this.structure.getName() + " excitations");
		
		this.lstExcitations = new JList();
		this.lstExcitations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.lstExcitations.addListSelectionListener(this);
		Container pane = this.getContentPane();
		JScrollPane scrollPane = new JScrollPane(this.lstExcitations);
		scrollPane.getViewport().setView(this.lstExcitations);
		 
		
		
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
		pane.add(new JLabel("Excitations list :"), c);
		
		
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
		pane.add(new JLabel ("Selected excitation :"), c);
		
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(5, 5, 0, 0); //
		c.gridwidth = 2;
		c.gridheight = 3;
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
		pane.add(new JLabel ("From :"), c);
				
		
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
		pane.add(this.txtFrom, c);
		
		
		c.gridx = 2;
		c.gridy = 2;
		c.insets = new Insets(5, 10, 0, 0); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		pane.add(new JLabel ("To :"), c);
				
		
		c.gridx = 3;
		c.gridy = 2;
		c.insets = new Insets(5, 5, 0, 5); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		pane.add(this.txtTo, c);	
		
		
		c.gridx = 3;
		c.gridy = 3;
		c.insets = new Insets(5, 5, 0, 5); //
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 0;
		c.weighty = 1;
		c.ipadx = 0;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.FIRST_LINE_END;
		pane.add(this.btSave, c);	

		c.gridx = 0;
		c.gridy = 4;
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
		c.gridy = 5;
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

		c.gridx =1;
		c.gridy = 5;
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
		c.gridy = 6;
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
		
		final FrameExcitations frameExcitations = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frameExcitations.dispose();
			}
		});
		
		showMonoExcitations();

	}
	
	
	public void showMonoExcitations() {
		
		DefaultListModel model = new DefaultListModel();
		String s;
		String iFrom;
		String iTo;
		for (MonoExcitation monoExcitation : structure.getMonoExcitations()) {
			if (monoExcitation == null)
				s = "null excitation";
			else {
				if (monoExcitation.getFrom() == null)
					iFrom = "null";
				else
					iFrom =  String.valueOf(monoExcitation.getFrom().getIndex());
				
				if (monoExcitation.getTo() == null)
					iTo = "null";
				else
					iTo =  String.valueOf(monoExcitation.getTo().getIndex());

				s = iFrom + "->" +  iTo;
		
			}
			model.addElement(s);
			
		}
		
		this.lstExcitations.setModel(model);
		this.lblTotal.setText("Total : "+ this.structure.getMonoExcitations().size());
		this.mode = Mode.CHANGE;

	}

	//@Override
	public void dispose() {
		FrameExcitations.instances.remove(this.structure);
		this.structure.removeListener(this);
		super.dispose();
	}

	
	
	public Structure getStructure() {
		return this.structure;
	}

	

	public void moleculeDeleted(MoleculeEvent e) {
		final FrameExcitations frame = this;
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				structure.removeListener(frame);
				dispose();
			}
			
		});				

	}

	public void setStructure(Structure s) {
		this.structure.removeListener(this);
		this.structure = s;
		this.structure.addListener(this);
		this.showMonoExcitations();
	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
		
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
		
	}



	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
		
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});			}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});				
	}


	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		//TODO
	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	
	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		

		
	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
	}

	//////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				showMonoExcitations();
			}
			
		});		
		
	}


	public void valueChanged(ListSelectionEvent listSelectionEvent) {
		if (!listSelectionEvent.getValueIsAdjusting() ) // cette ligne est necessaire car valueChanged est appelee deux fois a cause de click de souris
			return;
			
		if (this.mode == Mode.ADD) {
			// on supprime la nouvelle monoexcitation : derniere de la liste
			DefaultListModel model = (DefaultListModel) this.lstExcitations.getModel();
			model.remove(model.getSize()-1);
		}
	
		MonoExcitation monoExcitation =  this.getSelectedMonoExcitation();
		if (monoExcitation != null) {
			this.txtFrom.setText(Integer.toString(monoExcitation.getFrom().getIndex()));
			this.txtTo.setText(Integer.toString(monoExcitation.getTo().getIndex()));
		}
		this.mode = Mode.CHANGE;

	}


	
	private MonoExcitation getSelectedMonoExcitation() {
		if (this.lstExcitations.getSelectedIndex() <0)
			 return null;
		return  this.structure.getMonoExcitations().get(this.lstExcitations.getSelectedIndex());
	}
}
