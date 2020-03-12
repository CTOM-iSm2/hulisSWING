package views;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ctom.hulis.huckel.events.MesomeryEvent;

import util.MyResourceBundle;
import util.languages.LanguageManager;

/**
 * StatusBar : This class describes a view for the status bar
 * 
 * @version 3.0
 * @since
 * @author Nicolas GOUDARD
 */

@SuppressWarnings("serial")
public class StatusBar extends JPanel {

	protected MyResourceBundle bundle;

	private JLabel txtState;

	public StatusBar(){
		super();
	
		this.bundle = LanguageManager.getInstance().getResource("StatusBar");
		this.txtState = new JLabel();
		this.txtState.setFont((Font) bundle.getObject("font"));

		this.setBorder(new EmptyBorder(0,10,0,0)); // marge de 10px a gauche

		setLayout(new GridLayout(1,1));
		add(this.txtState);

		setTxtState("");
	}
	
	public void updateLanguage() {
		bundle = LanguageManager.getInstance().getResource("FrameApp");

		this.txtState.setFont((Font) bundle.getObject("font"));
	
	}

	
	public String getTxtState() {
		return this.txtState.getText();
	}

	

	/**
	 * refresh : this method repaints the status bar
	 * 
	 * 
	 */

	public void setTxtState(String txtState) {
		if (txtState == null)
			return;
		this.txtState.setText(txtState);
		this.txtState.setPreferredSize(getPreferredSize());
		this.setToolTipText(txtState);

	}

	public void structureAdded(MesomeryEvent e) {
	}

	public void structureRemoved(MesomeryEvent e) {
	}
}
