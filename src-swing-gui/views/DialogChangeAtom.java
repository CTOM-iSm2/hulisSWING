package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.util.common.Convert;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import controller.globalCommands.ReplaceAtom;

/**
 * DialogChangeAtom : Frame Atom Type : this class describes the dialog box for
 * changing an atom
 * 
 * @version 1.0
 * @since
 * @author Nicolas GOUDARD
 */
@SuppressWarnings("serial")
public class DialogChangeAtom extends MyJDialog implements ActionListener {

	public static String XX_ATOM_TEXT = "Xx";
	
	FrameApp app;
	Atom atomSelect;
	Atom atomToChange;
	private String BT_OK;
	private String BT_UNDO;
	public JButton btCancel;
	public JButton btOK;
	MyResourceBundle bundle;

	private HashMap<String, JToggleButton> buttons = new HashMap<String, JToggleButton>();
	private String FRAME_TITLE_ATOM_TYPE;

	public JTextField txtHx;
	private JLabel lblXXNeighbours ;
	private JTextField txtXXNbNeighbours;
	private JLabel lblXXbNbElecPi;
	private JTextField txtXXNbElecPi;
	private JLabel lblXXSymbol;
	private JTextField txtXXSymbol;

	/**
	 * Constructor
	 * 
	 * @param frame :
	 *            parent frame
	 * @param p :
	 *            coordinate of the dialog box in the screen
	 * @param atomToChange :
	 *            atom to change int the moleculeAbstract to change
	 * @throws Exception :
	 *             when a parameter is null
	 */

	public DialogChangeAtom(FrameApp frame, Point p, Atom atomToChange)
			throws Exception {
		super(JOptionPane.getFrameForComponent(frame));
		this.app = frame;

		this.bundle = LanguageManager.getInstance().getResource("DialogChangeAtom");
		Font btFont =  (Font) this.bundle.getObject("font1") ;   
		Font btFont2 = new Font("Helvetica", Font.PLAIN, 11); 

		this.FRAME_TITLE_ATOM_TYPE = this.bundle.getString("kTitleAtomType");
		this.BT_OK = this.bundle.getString("kOK");
		this.BT_UNDO = this.bundle.getString("kundo");

		// if (frame == null) throw new Exception ("frame must be intialized");
		if (p == null)
			throw new Exception("point must be intialized");
		if (atomToChange == null)
			throw new Exception("atom must be initialized");
		
		this.txtHx = new JTextField(4);
		lblXXNeighbours = new JLabel ("<html>n<sub>neighb</sub> = </html>");
		lblXXbNbElecPi = new JLabel ("<html>n<sub>e-\u03C0</sub> = </html>");
		lblXXSymbol = new JLabel ("<html>Symbol = </html>");
		this.txtXXNbNeighbours = new JTextField(3);
		this.txtXXNbElecPi = new JTextField(3);
		this.txtXXSymbol = new JTextField(3);

		lblXXNeighbours.setVisible(false);
		lblXXbNbElecPi.setVisible(false);
		lblXXSymbol.setVisible(false);
		this.txtXXNbNeighbours.setVisible(false);
		this.txtXXNbElecPi.setVisible(false);
		this.txtXXSymbol.setVisible(false);
	
		
		String title = this.FRAME_TITLE_ATOM_TYPE + " "
				+ atomToChange.getSigle();

		this.txtXXNbNeighbours.setText(Integer.toString(atomToChange.getNbNeighboursMax()));
		
		if (atomToChange.getElement() == null)
			this.txtXXSymbol.setText(atomToChange.getSigle());
		else
			this.txtXXSymbol.setText(XX_ATOM_TEXT);

		if (atomToChange instanceof HuckelAtom) {
			HuckelAtom huckelAtomToChange = (HuckelAtom) atomToChange;
			title += ", " + huckelAtomToChange.getSeqNum();
			this.txtHx.setText(Double.toString(huckelAtomToChange.getHx()));
			this.txtXXNbElecPi.setText(Integer.toString(huckelAtomToChange.getNbElecPi()));

		}

		setTitle(title);
		// this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JToggleButton boutonTypeAtome;
		JPanel panneauBouton = new JPanel();
		panneauBouton.setLayout(new GridLayout(4, 4));

		this.atomToChange = atomToChange;
		this.atomSelect = null;

		ArrayList <String> tmp = new ArrayList<String>(Arrays.asList(Convert.enumNameToStringArray(PeriodicTable.Entry.values())));
		tmp.add(XX_ATOM_TEXT);
		String[]  listeAtomes = new String [tmp.size()];
		tmp.toArray(listeAtomes);

		ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < listeAtomes.length; i++) {
			if ((!listeAtomes[i].toString().equals("H"))) {
				boutonTypeAtome = new JToggleButton(listeAtomes[i].toString());
				boutonTypeAtome.setActionCommand(listeAtomes[i].toString());
				boutonTypeAtome.setText("<html>"
						+ AtomColor.valueOf(listeAtomes[i].toString())
								.getDevForm() + "</html>");
				if ((!listeAtomes[i].equals(XX_ATOM_TEXT)) && (PeriodicTable.getNewAtom(PeriodicTable.Entry.valueOf(listeAtomes[i]))
						.getNbNeighboursMax() < atomToChange
						.countNeighboursExcludingH()))
					boutonTypeAtome.setEnabled(false);
				buttonGroup.add(boutonTypeAtome);
				boutonTypeAtome.addActionListener(this);
				boutonTypeAtome.setFont(btFont2);
				panneauBouton.add(boutonTypeAtome);
				this.buttons.put(listeAtomes[i], boutonTypeAtome);
				if (atomToChange instanceof HuckelAtom) {
					if ((!listeAtomes[i].equals(XX_ATOM_TEXT)) && ((HuckelAtom) atomToChange).getEntry() == PeriodicTable.Entry.valueOf(listeAtomes[i])) {
						boutonTypeAtome.setSelected(true); // default selection
						this.atomSelect = PeriodicTable
								.getNewAtom(PeriodicTable.Entry.valueOf(listeAtomes[i]));
					}
					else if ((atomToChange.getElement() == null) &&(listeAtomes[i] == XX_ATOM_TEXT)) {
						boutonTypeAtome.setSelected(true);
						this.atomSelect = new HuckelAtom(0, null, null, XX_ATOM_TEXT, XX_ATOM_TEXT, 0, 0,	0, 0.0, 0) ;
						this.lblXXNeighbours.setVisible(true);
						this.txtXXNbNeighbours.setVisible(true);
						this.lblXXbNbElecPi.setVisible(true);
						this.txtXXNbElecPi.setVisible(true);
						this.lblXXSymbol.setVisible(true);
						this.txtXXSymbol.setVisible(true);
					}
				}
				
				
			}

		}



		JPanel panneauChamps = new JPanel();
		JLabel lblHx = new JLabel("Hx =");
		lblHx.setFont(btFont2);
		this.txtHx.setFont(btFont2);
		panneauChamps.setLayout(new FlowLayout(FlowLayout.LEFT));
		panneauChamps.add(lblHx);
		panneauChamps.add(this.txtHx);

		
		lblXXNeighbours.setFont(btFont2);
		panneauChamps.add(lblXXNeighbours);
		this.txtXXNbNeighbours.setFont(btFont2);
		panneauChamps.add(this.txtXXNbNeighbours);
		
		lblXXbNbElecPi.setFont(btFont2);
		panneauChamps.add(lblXXbNbElecPi);
		this.txtXXNbElecPi.setFont(btFont2);
		panneauChamps.add(this.txtXXNbElecPi);
		
		lblXXSymbol.setFont(btFont2);
		panneauChamps.add(lblXXSymbol);
		this.txtXXSymbol.setFont(btFont2);
		panneauChamps.add(this.txtXXSymbol);
		
		JPanel panneauOKAnnuler = new JPanel();
		panneauOKAnnuler.setLayout(new FlowLayout());

		this.btOK = new JButton(this.BT_OK);
		this.btOK.setFont(btFont);
		this.btOK.addActionListener(this);
		panneauOKAnnuler.add(this.btOK);

		this.btCancel = new JButton(this.BT_UNDO);
		this.btCancel.addActionListener(this);
		this.btCancel.setFont(btFont);
		panneauOKAnnuler.add(this.btCancel);

		JPanel panneauBas = new JPanel();
		panneauBas.setLayout(new GridLayout(2, 1));
		panneauBas.add(panneauChamps);
		panneauBas.add(panneauOKAnnuler);

		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout());
		contenu.add(panneauBouton, BorderLayout.CENTER);
		contenu.add(panneauBas, BorderLayout.SOUTH);

		pack();
		setPreferredSize(new Dimension(380, 200));
		this.setSize(new Dimension(380,200));
		this.setLocation(p);
		
	}

	/**
	 * actionPerformed : this method is called when the user clic on a button
	 * 
	 * @param e :
	 *            Action Event ok : quit and change the moleculeAbstract undo :
	 *            quit without saving
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		Structure structure = (Structure) this.atomToChange.getMoleculeContainer();

		try {
			if (e.getActionCommand() == this.BT_OK) {
				if (atomSelect == null) {
					MyJOptionPane.showMessageDialog(this, this.bundle
							.getString("katomnull"), this.bundle
							.getString("kTitleAtomType"),
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				double valHx = Double.valueOf(this.txtHx.getText())
						.doubleValue();
				
				app.getStateManager().initNewState();
				
				// si on change un atome XX ou qu'on choisi un atome different de celui existant
				if ((atomToChange.getEntry() == null) || (atomSelect.getEntry() != atomToChange.getEntry()) ) {
					boolean savCalcFly = structure.isEnabledFlyCalculate();
					structure.stopFlyCalculate();

					structure.setEnabledNotify(false);
					if (this.atomSelect instanceof HuckelAtom) {
						HuckelAtom huckelAtom = ((HuckelAtom) this.atomSelect);
						huckelAtom.setHx(valHx);
						
						if (this.atomSelect.getSigle().equals(XX_ATOM_TEXT)) {
							huckelAtom.setNbNeighboursMax(Integer.valueOf(this.txtXXNbNeighbours.getText()));
							huckelAtom.setNbElecPi(Integer.valueOf(this.txtXXNbElecPi.getText()));
							huckelAtom.setSigle(this.txtXXSymbol.getText());												
						}
						
					
					}
					if (savCalcFly) {
						structure.startFlyCalculate();
						structure.calculate();
					}
					structure.setEnabledNotify(true);
					SwingIO.log(this.getClass().getName(), "actionPerformed", "replace " + atomToChange + " by " + atomSelect );
					StructureView sv = this.app.getCurrentMesomeryView().getCurrentStructureView();
					new ReplaceAtom(structure, atomToChange, this.atomSelect);
	
				}
				else // si on garde le meme atome (non xx)
					if (this.atomToChange instanceof HuckelAtom) {
						SwingIO.log(this.getClass().getName(), "actionPerformed", "set HX " + valHx + " to " + atomToChange );

						((HuckelAtom) this.atomToChange).setHx(valHx);
					}
				// TODO : IL MANQUE LE CAS : si on modifie un atome XX
				app.getStateManager().saveState();
				dispose();
			} else if (e.getActionCommand() == this.BT_UNDO) {
				dispose();
			} else {
		
				// clic sur un type d'atomes pour le selectionner
				String sigle = e.getActionCommand();
				if (sigle.equals(XX_ATOM_TEXT)){
					SwingIO.log(this.getClass().getName(), "actionPerformed", "XX atom selected");
					this.atomSelect = new HuckelAtom(0, null, null, XX_ATOM_TEXT, XX_ATOM_TEXT, 0, 0,	0, 0.0, 0) ;
					this.lblXXNeighbours.setVisible(true);
					this.txtXXNbNeighbours.setVisible(true);
					this.lblXXbNbElecPi.setVisible(true);
					this.txtXXNbElecPi.setVisible(true);
					this.lblXXSymbol.setVisible(true);
					this.txtXXSymbol.setVisible(true);
				}
				else {
					SwingIO.log(this.getClass().getName(), "actionPerformed", sigle + " atom selected");
					this.atomSelect = PeriodicTable.getNewAtom(PeriodicTable.Entry
							.valueOf(sigle));
					this.lblXXNeighbours.setVisible(false);
					this.txtXXNbNeighbours.setVisible(false);
					this.lblXXbNbElecPi.setVisible(false);
					this.txtXXNbElecPi.setVisible(false);
					this.lblXXSymbol.setVisible(false);
					this.txtXXSymbol.setVisible(false);
				}
				if (this.atomSelect instanceof HuckelAtom)
					this.txtHx.setText(Double
							.toString(((HuckelAtom) this.atomSelect).getHx()));
			}
		} catch (NumberFormatException e1) {
			structure.setEnabledNotify(true);
			MyJOptionPane.showMessageDialog(this, this.bundle
					.getString("knumeric"), this.bundle
					.getString("kTitleAtomType"),
					JOptionPane.INFORMATION_MESSAGE);
			this.txtHx.selectAll();
			SwingIO.warning(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);	

		} catch (HuckelBondException e1) {
			structure.setEnabledNotify(true);
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);

		} catch (BondException e1) {
			structure.setEnabledNotify(true);
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);
		} catch (MoleculeAtomNullException e1) {
			structure.setEnabledNotify(true);
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);
		} catch (MoleculeTooManyNeighboursException e1) {
			structure.setEnabledNotify(true);
			JOptionPane.showMessageDialog(this.app, bundle.getString("cannotReduceValence"), bundle.getString("kTitleAtomType"), JOptionPane.INFORMATION_MESSAGE);
			//SwingIO.reportError(app);
		} catch (MoleculeCoherenceException e1) {
			structure.setEnabledNotify(true);
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);
		} catch (MoleculeBondExistsException e1) {
			structure.setEnabledNotify(true);
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);
		} 
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public JToggleButton getButton(String entry) {
		return this.buttons.get(entry);
	}

}
