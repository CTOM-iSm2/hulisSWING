package views;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
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


public class SimpleDialogScrollText extends MyJDialog {
	JTextPane pane;
	/**
	 * 
	 * @param frame
	 * @param title
	 * @param url
	 */
	public SimpleDialogScrollText(JFrame frame, String title, URL url) {
		super (frame);
		init(frame, title);
		try {
			pane.setPage(url);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.setVisible(true);
	}
	
	
	/**
	 * 
	 * @param frame
	 * @param title
	 * @param text
	 */
	public SimpleDialogScrollText(JFrame frame, String title, String text) {
		super (frame);
		init(frame, title);
		System.out.println(text);
		pane.setText(text);
		this.setVisible(true);
	}
	
	
	/**
	 * @param frame
	 * @param title
	 */
	protected void  init(JFrame frame, String title) {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("About");
		
		this.setTitle(title);
		pane =  new JTextPane();
		pane.setPreferredSize(this.getPreferredSize());
		pane.setEditable(false);
		pane.setContentType("text/html; charset=UTF-8");
		pane.setCaretColor(Color.black);
		pane.setFont(new Font("Monaco", Font.ROMAN_BASELINE, 12));
		
		pane.setCaretPosition(0);
		pane.setMargin( new Insets(10,10,10,10) );

		pane.addHyperlinkListener(new FollowLink(pane));
	
		PopUp.pop(pane);
		
		JScrollPane scrollPane = new JScrollPane(pane,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// Pb scrollbar qui ne s'affiche pas parfois
		scrollPane	.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		// yannick
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(800, 600));
		// yannick
		Container contenu = this.getContentPane();
		contenu.setLayout(new BorderLayout(5, 5));	
		JButton btClose = new JButton(bundle.getString("close"));
		btClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
			
		});
		
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(new FlowLayout());
		panelButtons.add(btClose);
		
		contenu.add(scrollPane, BorderLayout.CENTER);
		contenu.add(panelButtons,  BorderLayout.SOUTH);
		
		this.pack();	
		this.setPreferredSize(new Dimension(500,600));
		this.setSize(new Dimension(500,600));

		
		this.setLocationRelativeTo(frame);
	}
}
