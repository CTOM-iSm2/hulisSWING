package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import util.ComponentsTools;
import util.FollowLink;
import util.MyResourceBundle;
import util.languages.LanguageManager;
import controller.globalCommands.PopUp;

@SuppressWarnings("serial")
public class ErrorReport extends JDialog {

	String error;
	protected static boolean isErrorReported = false;
	
	public static boolean isErrorReported () {
		return isErrorReported;
	}

	public static void clear () {
		isErrorReported = false;
	}

	public ErrorReport(String error) {
		this (error, true);
	}

	
	public ErrorReport(String error, boolean fatal) {
		super((JFrame) null, true);

		if (fatal)
			isErrorReported = true;
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ErrorReport");

		this.error = error;

		this.setTitle(bundle.getString("title"));
		setPreferredSize(new Dimension(500, 600));
		this.setSize(new Dimension(100, 100));
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout(5, 5));

		JTextPane txtResult = new JTextPane();		
		txtResult.setContentType("charset=UTF-8");
		txtResult.setPreferredSize(getPreferredSize());
		txtResult.setEditable(false);

		txtResult.setText(error);

		PopUp.pop(txtResult);

		txtResult.setFont(new Font("Monaco", Font.ROMAN_BASELINE, 12));
		// txtResult.setFont(new Font("Symbol",Font.TRUETYPE_FONT,14));

		JScrollPane ascTxtResult = new JScrollPane(txtResult,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// Pb scrollbar qui ne s'affiche pas parfois
		ascTxtResult
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// yannick
		ascTxtResult
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ascTxtResult.setPreferredSize(new Dimension(800, 600));
		// yannick

		Font btFont = (Font) bundle.getObject("font2");
		JPanel panneauBoutons = new JPanel();
		final JTextPane txtResult2 = txtResult;
		JButton jbCopy = new JButton(bundle.getString("kcopy"));
		jbCopy.setFont(btFont);
		jbCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtResult2.selectAll();
				txtResult2.copy();
			}
		});

		panneauBoutons.add(jbCopy);

		JButton jbFermer;
		if (fatal)
			jbFermer = new JButton(bundle.getString("kquit"));
		else
			jbFermer = new JButton(bundle.getString("kclose"));
		jbFermer.setFont(btFont);
		jbFermer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// sendFTP();
				dispose();
			}
		});

		panneauBoutons.add(jbFermer);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// sendFTP();
				dispose();
			}
		});

		JTextPane label = new JTextPane();
		label.setContentType("text/html; charset=UTF-8");
		label.setEditable(false);
		label.setSize(100, 20);
		if (fatal){
			label.setText("<html>" + bundle.getString("kerrtxt") + "</html>");
			label.addHyperlinkListener(new FollowLink(label));
		}
		
		contenu.add(label, BorderLayout.NORTH);
		contenu.add(ascTxtResult, BorderLayout.CENTER);
		contenu.add(panneauBoutons, BorderLayout.SOUTH);

		pack();
		this.setLocationRelativeTo(null);
	}
}
