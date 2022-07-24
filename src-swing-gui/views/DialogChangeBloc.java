package views;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import access.Rights;
import controller.globalCommands.ActionEndBloc;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;

public class DialogChangeBloc extends MyJDialog implements ActionListener {
	StructureView structureView;
	BlocDelocalized blocDelocalized;
	ButtonGroup buttonGroup;
	JButton btOk;
	JButton btCancel;
	
	MyResourceBundle bundle;

	public DialogChangeBloc (StructureView structureView, Point p, BlocDelocalized blocDelocalized) {
		super(JOptionPane.getFrameForComponent(structureView.getMesomeryView().getFrameApp()));
		this.structureView = structureView;
		this.blocDelocalized = blocDelocalized;
		bundle = LanguageManager.getInstance().getResource("DialogChangeBloc");
		Font font1 =  (Font) bundle.getObject("font1") ;   
		JPanel radioPanel = new JPanel();
		radioPanel.setLayout(new GridLayout(3,1));
		buttonGroup= new ButtonGroup();
		String[] options ={"kchoix1", "kchoix2", "kchoix3"};
		for (String option: options){
			JRadioButton radioButton = new JRadioButton(bundle.getString(option));
			radioButton.setActionCommand(option);
			buttonGroup.add(radioButton);
			radioPanel.add(radioButton);
			radioPanel.setFont(font1);
		
		}
		
		
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new FlowLayout());

		this.btOk = new JButton(bundle.getString("kOK"));
		this.btOk.setFont(font1);
		this.btOk.addActionListener(this);
		confirmPanel.add(this.btOk);

		this.btCancel = new JButton(bundle.getString("kundo"));
		this.btCancel.addActionListener(this);
		this.btCancel.setFont(font1);
		confirmPanel.add(this.btCancel);

		
		Container contenu = getContentPane();
		contenu.setLayout(new GridLayout(2,1));
		contenu.add(radioPanel);
		contenu.add(confirmPanel);
		this.pack();
		this.setLocation(p);

	}

//	@Override
	public void actionPerformed(ActionEvent e) {
		this.structureView.setOverflightBlocDelocalized(null);

		if (e.getSource() == this.btOk) {
			FrameApp app = this.structureView.getMesomeryView().getFrameApp();

			// evalue bouton radio selectionne
			String command = this.buttonGroup.getSelection().getActionCommand();
	
			// choix1 : modifier un bloc  
			if (command.equals("kchoix1")){
				SwingIO.log(this.getClass().getName(), "actionPerformed", "select a block to change it");
				StructureView.setSelectedBlocDelocalized(this.blocDelocalized);
			}
			//choix2 : supprimer un bloc
			else if (command.equals("kchoix2")) {
				SwingIO.log(this.getClass().getName(), "actionPerformed", "remove a block");
				app.getStateManager().initNewState();
				((StructureLocalized) this.structureView.getStructure()).removeBlocDelocalized(this.blocDelocalized);
				this.blocDelocalized.clear();
				this.blocDelocalized = null;
				StructureView.setSelectedBlocDelocalized(null);
				app.toolBarMesomery.btEndBloc.setSelected(false);
				app.toolBarMesomery.btEndBloc.setVisible(false);
				app.toolBarMesomery.btBloc.setVisible(true && Rights.ALLOW_BLOCKS);
				app.toolBarMesomery.btChangeElectrons.doClick();
				new ActionEndBloc(app).unselectCurrentBloc();
				app.getStateManager().saveState();

			}
			// choix 3 : modifier le nombre d'electrons
			else if (command.equals("kchoix3")) {
				SwingIO.log(this.getClass().getName(), "actionPerformed", "change electron number");
				StructureView.setSelectedBlocDelocalized(this.blocDelocalized);
				new ActionEndBloc(app, this.getRootPane()).execute();
			}

			this.dispose();
		}
		else if (e.getSource() == this.btCancel) {
			this.dispose();
		}
		
	}
	
	
	
}
