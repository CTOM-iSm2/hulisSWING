package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Window;
import java.text.SimpleDateFormat;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;

import sqlbridge.ConnectSQL;
import util.FollowLink;
import util.MyResourceBundle;
import util.Version;
import util.languages.LanguageManager;
import access.Rights;

public class AboutPanel extends JPanel {
	protected JTextPane aboutLabel;
	protected JLabel imgLabel;
	MyResourceBundle bundle;
	FrameApp app;

	public AboutPanel(FrameApp app, final Window win) {
		setLayout(new BorderLayout());
		this.app = app;
		this.bundle = LanguageManager.getInstance().getResource("About");
		String IMG_PATH = this.bundle.getString("kimgpath");

		this.aboutLabel = new JTextPane();
		this.aboutLabel.setContentType("text/html; charset=UTF-8");
		this.aboutLabel.setText(getDefaultLabel());
		this.aboutLabel.setEditable(false);
		this.aboutLabel.setAlignmentX(Label.LEFT);
		this.aboutLabel.addHyperlinkListener(new FollowLink(this.aboutLabel));

		ImageIcon im = new ImageIcon(getClass().getClassLoader().getResource(
				IMG_PATH));

		imgLabel = new JLabel(im);
		imgLabel.setOpaque(false);
		JPanel imgPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		imgPanel.setBackground(Color.WHITE);
		imgPanel.add(imgLabel);

		JPanel textPanel = new JPanel(new GridLayout(1, 1, 0, 0));
		textPanel.add(this.aboutLabel);

		setBorder(new LineBorder(Color.black));
		this.add(imgPanel, BorderLayout.NORTH);
		this.add(textPanel, BorderLayout.CENTER);



	}

	public String getDefaultLabel() {

		String HEAD_HTML = this.bundle.getString("kheadhtml");
		String SOFT_NAME = FrameApp.ksoftname;
		String JDK = this.bundle.getString("kjdk") + " "
		+ System.getProperty("java.version");
		String AUTHORS = this.bundle.getString("kauthors") + " "
		+ FrameApp.kauthors;
		String COPYRIGHT = this.bundle.getString("kcopyright");
		String VERSION = this.app.getVersion().toString();

		String VERSION_WARMIGS = "";
		
		String txtCheckVersion = app.getCheckVersionTxt();
		if (! txtCheckVersion.equals(""))
			VERSION_WARMIGS += ". " +  txtCheckVersion;
		if (!app.getRights().isValidUser()) {
			VERSION_WARMIGS += "<br>"
				+ this.bundle.getString("kstarterror2")
				+ " "
				+ app.getRights().getRejectReason();
			;
			VERSION_WARMIGS += "</span>";
		}


		return "<html>" + HEAD_HTML + "<body><br><span class='couleur'>"
		+ SOFT_NAME + " " + VERSION + "</span>" +  VERSION_WARMIGS
		+ "<span class='couleur'><br><br>"
		+ this.bundle.getString("kwarmings") + "<br><br>" + AUTHORS
		+ "<br><br>" + JDK + "<br><br></span>" + COPYRIGHT
		+ "</body></html>";

	}
}
