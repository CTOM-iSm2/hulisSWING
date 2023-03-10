package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import controller.globalCommands.ActionQuit;
import controller.globalCommands.PopUp;
import util.FollowLink;
import util.MyResourceBundle;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;


/**
 * 
 * @author macbook
 *
 */
public class LicenseFrame extends MyJDialog  {
	
	protected MyResourceBundle bundle;
	
	
	
	/**
	 * 
	 * @param app
	 */
	public LicenseFrame(final FrameApp app) {
		super(JOptionPane.getFrameForComponent(app));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		bundle = LanguageManager.getInstance().getResource("LicenseFrame");

		
		this.setTitle(bundle.getString("title"));

		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout(5, 5));

		final JTextPane txtLicence = new JTextPane(); // "text/html l; charset=UTF-8",
		txtLicence.setContentType("text/html; charset=UTF-8");
		txtLicence.setPreferredSize(getPreferredSize());
		txtLicence.setEditable(false);

		try {
			txtLicence.setPage((URL)bundle.getObject("license"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		txtLicence.setCaretPosition(0);
		PopUp.pop(txtLicence);

		txtLicence.setFont(new Font("Monaco", Font.ROMAN_BASELINE, 12));
		// txtResult.setFont(new Font("Symbol",Font.TRUETYPE_FONT,14));

		JScrollPane txtLicenceScroll = new JScrollPane(txtLicence,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// Pb scrollbar qui ne s'affiche pas parfois
		txtLicenceScroll
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// yannick
		txtLicenceScroll
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		txtLicenceScroll.setPreferredSize(new Dimension(800, 600));
		// yannick
		txtLicence.addHyperlinkListener(new FollowLink(txtLicence));
		txtLicence.setMargin( new Insets(5,10,5,10) );
		
		Font btFont = (Font) bundle.getObject("font2");
		JPanel panneauBoutons = new JPanel();
		JButton acceptBt = new JButton(bundle.getString("accept"));
		JButton closeBt = new JButton(bundle.getString("quit"));
	

		panneauBoutons.add(acceptBt);
		panneauBoutons.add(closeBt);

		acceptBt.setFont(btFont);
		closeBt.setFont(btFont);
		
		acceptBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PreferencesGUI.getInstance().setAcceptLicense(true);
				dispose();
			}
		});

		closeBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ActionQuit(app).execute();
				dispose();
			}
		});

		Informations.setFrameApp(app);
		
		JTextPane label = new JTextPane();
		label.setMargin( new Insets(10,10,5,10) );
		label.setContentType("text/html; charset=UTF-8");
		label.setEditable(false);
		label.setSize(100, 20);
		label.setOpaque(false);
		label.setText("<body align='justify'>"+bundle.getString("texthead")+"</body>");
		label.addHyperlinkListener(new FollowLink(label));
	
		contenu.add(label, BorderLayout.NORTH);
		contenu.add(txtLicenceScroll, BorderLayout.CENTER);
		contenu.add(panneauBoutons, BorderLayout.SOUTH);

		
		
		pack();
		
		
		setPreferredSize(new Dimension(500,600));
		this.setSize(new Dimension(500,600));

		setLocationRelativeTo(app);
		this.setModal(true);
	}
}
