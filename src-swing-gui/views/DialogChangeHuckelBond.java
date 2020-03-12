package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.HuckelBondException;

import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;

/**
 * DialogChangeBond : Frame Bond Type : this class describes the dialog box for
 * changing an atom
 * 
 * @version 1.0
 * @since
 * @author Nicolas GOUDARD
 * 
 */
@SuppressWarnings("serial")
public class DialogChangeHuckelBond extends MyJDialog implements ActionListener {
	

	FrameApp app;
	HuckelBond bondToChange;
	public JButton btOK;
	private String BT_DEFAULT;
	private String BT_OK;

	private String BT_UNDO;
	MyResourceBundle bundle;
	private String FRAME_TITLE_BOND_TYPE;
	public JTextField txtHxy;

	/**
	 * Constructor
	 * 
	 * @param frame :
	 *            parent frame
	 * @param p :
	 *            coordinate of the dialog box in the screen
	 * @param moleculeToChange :
	 *            moleculeAbstract to change
	 * @param bondToChange :
	 *            atom to change int the moleculeAbstract to change
	 * @throws Exception :
	 *             when a parameter is null
	 */

	public DialogChangeHuckelBond(FrameApp frame, Point p,
			HuckelBond bondToChange) throws Exception {
		super(JOptionPane.getFrameForComponent(frame));
		this.app = frame;
		this.bundle = LanguageManager.getInstance().getResource("DialogChangeHuckelBond");
		Font btFont = (Font) this.bundle
				.getObject("font1");
		Font btFont2 = new Font("Helvetica", Font.PLAIN, 11);

		this.FRAME_TITLE_BOND_TYPE = this.bundle.getString("kTitleChangeBonds");
		this.BT_DEFAULT = this.bundle.getString("kdefault");
		this.BT_OK = this.bundle.getString("kOK");
		this.BT_UNDO = this.bundle.getString("kundo");

		// if (frame == null) throw new Exception ("frame must etre
		// intialized");
		if (p == null)
			throw new Exception("point must be intialized");
		if (bondToChange == null)
			throw new Exception("bond must be initialized");

		JLabel lblLiaison = new JLabel();

		setTitle(this.FRAME_TITLE_BOND_TYPE);

		lblLiaison.setText(bondToChange.toString());
		lblLiaison.setFont(btFont2);
		this.bondToChange = bondToChange;

		JPanel panneauLabel = new JPanel();
		panneauLabel.add(lblLiaison);

		JPanel panneauChamps = new JPanel();
		JLabel lblHxy = new JLabel("Hxy =");
		lblHxy.setFont(btFont2);

		this.txtHxy = new JTextField(5);
		this.txtHxy.setFont(btFont2);
		panneauChamps.setLayout(new FlowLayout());
		panneauChamps.add(lblHxy);
		panneauChamps.add(this.txtHxy);
		JButton boutonDefaut = new JButton(this.BT_DEFAULT);
		boutonDefaut.setFont(btFont);
		boutonDefaut.addActionListener(this);
		panneauChamps.add(boutonDefaut);

		JPanel panneauOKAnnuler = new JPanel();
		panneauOKAnnuler.setLayout(new FlowLayout());

		JPanel panneauHaut = new JPanel();
		panneauHaut.setLayout(new GridLayout(2, 1));
		panneauHaut.add(panneauLabel);
		panneauHaut.add(panneauChamps);

	    btOK = new JButton(this.BT_OK);
		btOK.setFont(btFont);
		btOK.addActionListener(this);
		panneauOKAnnuler.add(btOK);

		JButton boutonAnnuler = new JButton(this.BT_UNDO);
		boutonAnnuler.addActionListener(this);
		boutonAnnuler.setFont(btFont);
		panneauOKAnnuler.add(boutonAnnuler);

		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout());
		contenu.add(panneauHaut, BorderLayout.CENTER);
		contenu.add(panneauOKAnnuler, BorderLayout.SOUTH);

		this.txtHxy.setText(Double.toString(bondToChange.getHxy()));

		// this.setSize(new Dimension(200,100));
		pack();
		this.setLocation(p);
	}

	/**
	 * actionPerformed : this method is called when the user clic on a button
	 * 
	 * @param e :
	 *            Action Event ok : quit and change the moleculeAbstract default :
	 *            set the values by default in the text zone undo : quit without
	 *            saving
	 * 
	 */

	public void actionPerformed(ActionEvent e) {
		try {
			double valHxy = Double.valueOf(this.txtHxy.getText()).doubleValue();

			if (e.getActionCommand() == this.BT_OK) {
				SwingIO.log(this.getClass().getName(), "actionPerformed", "set hxy=" + valHxy + " on bond " + this.bondToChange);
				// if (valHxy >= 0) {
				app.getStateManager().initNewState();
				this.bondToChange.setHxy(valHxy);
				app.getStateManager().saveState();
				dispose();
				// }
			} else if (e.getActionCommand() == this.BT_DEFAULT) {
				double defaultValue = 0.0;
				PeriodicTable.Entry entry1 = ((HuckelAtom) this.bondToChange.getAtom1()).getEntry();
				PeriodicTable.Entry entry2 = ((HuckelAtom) this.bondToChange.getAtom2()).getEntry();
				
				if  ((entry1 != null) && (entry2 != null))
					defaultValue = HuckelBond.getHxy(entry1, entry2);
				SwingIO.log(this.getClass().getName(), "actionPerformed", "set default value hxy=" + defaultValue);
				this.txtHxy.setText(Double.toString(defaultValue));
			}
			if (e.getActionCommand() == this.BT_UNDO) {
				SwingIO.log(this.getClass().getName(), "actionPerformed", "cancel dialog box");
				dispose();
			}
		} catch (NumberFormatException e1) {
			MyJOptionPane.showMessageDialog(this, this.bundle
					.getString("knumeric"), this.bundle
					.getString("kTitleChangeBonds"),
					JOptionPane.INFORMATION_MESSAGE);
			this.txtHxy.selectAll();
			SwingIO.warning(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
		} catch (HuckelBondException e1) {
			SwingIO.error(this.getClass().getName(), "actionPerformed", e1
					.getMessage(), e1);
			SwingIO.reportError(app);
		}

	}

	public void dispose() {
		SwingIO.log(this.getClass().getName(), "dispose", "dispose dialog box");
		super.dispose();
	}
}
