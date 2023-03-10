package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;

@SuppressWarnings("serial")
public class AboutBox extends MyJDialog implements ActionListener {

	private static ArrayList<AboutBox> opened = new ArrayList<AboutBox>();

	public static List<AboutBox> getOpened() {
		return AboutBox.opened;
	}
	
	MyResourceBundle bundle;
	private FrameApp app;


	
	
	/**
	 * 
	 * @param app
	 */
	public AboutBox(final FrameApp app) {
		super(JOptionPane.getFrameForComponent(app));
		this.app = app;
		this.bundle = LanguageManager.getInstance().getResource("About");
		int aboutWidth = (Integer) (this.bundle.getObject("kaboutWidth"));
		int aboutHeight = (Integer) (this.bundle.getObject("kaboutHeight"));

		setLayout(new BorderLayout(15, 15));

	
		setTitle(this.bundle.getString("kabouttitle"));
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent event) {
				setVisible(false);
				dispose();
			}
		});

		setLayout(new BorderLayout(15, 15));

		JPanel bigPanel = new AboutPanel(app, this);

		Container contenu = getContentPane();
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout());
		JButton btLicence = new JButton(this.bundle.getString("licence"));
		JButton btCredits = new JButton(this.bundle.getString("credits"));
		JButton btClose = new JButton(this.bundle.getString("close"));
			
		panelButtons.add(btLicence);
		panelButtons.add(btCredits);
		panelButtons.add(btClose);
		
		btLicence.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MyResourceBundle bdl = LanguageManager.getInstance().getResource("LicenseFrame");
				new SimpleDialogScrollText((JFrame)(JOptionPane.getFrameForComponent(app)), bundle.getString("licencetitle"), (URL)bdl.getObject("license"));
				
			}});
		

		btCredits.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new SimpleDialogScrollText((JFrame)(JOptionPane.getFrameForComponent(app)), bundle.getString("creditstitle"), (URL)bundle.getObject("creditsfile"));
			}});
		
		btClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
			
		});

		contenu.setLayout(new BorderLayout());
		contenu.add(bigPanel, BorderLayout.CENTER);
	
		if (PreferencesGUI.MANAGE_LICENCE)
			contenu.add(panelButtons, BorderLayout.SOUTH);

		pack();
		this.setSize(new Dimension(aboutWidth, aboutHeight));
		setLocationRelativeTo(app);
		AboutBox.opened.add(this);
	}

	public void actionPerformed(ActionEvent newEvent) {
		dispose();
	}

	@Override
	public void dispose () {
		SwingIO.log(this.getClass().getName(), "dispose", "close about box");
		AboutBox.opened.remove(this);
		super.dispose();
	}
}