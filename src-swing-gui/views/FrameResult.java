package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.TabSet;
import javax.swing.text.TabStop;

import org.ctom.hulis.huckel.Energy.EnergyFormater;

import controller.globalCommands.PopUp;
import util.MyDecimalFormat;
import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;

public abstract class FrameResult extends JFrame implements 
ActionListener {
	public static String BT_CLOSE;
	public static String BT_REFRESH;

	protected MyDecimalFormat decimalFormatMO= new MyDecimalFormat("0.00");
	MyDecimalFormat decimalEnergyAlpha = new MyDecimalFormat("#.##");
	MyDecimalFormat decimalEnergyBeta = new MyDecimalFormat("0.00");
	protected EnergyFormater energyFormater = new SwingEnergyFormater (decimalEnergyAlpha, decimalEnergyBeta );


	protected MyResourceBundle bundle;

	DefaultStyledDocument docTXT;

	JButton jbFermer;
	JButton jbRefresh;


	/**
	 * 
	 * @return
	 */
	public MyDecimalFormat getMyDecimalFormatMO() {
		return decimalFormatMO;		
	}

	/**
	 * 
	 * @return
	 */
	public EnergyFormater getEnergyformater () {
		return energyFormater;		
	}


	/**
	 * 
	 * @param decimalFormat for MO
	 * @param decimal format for Energies
	 */
	public void setMyDecimalFormat (MyDecimalFormat decimalFormatMO, MyDecimalFormat decimalFormatEnergies) {
		this.decimalFormatMO =decimalFormatMO;
		this.energyFormater =new SwingEnergyFormater (decimalEnergyAlpha, decimalFormatEnergies);
	}


	protected int offset;

	private StringBuffer stringResult;;

	protected JTextPane txtResult;
	protected FrameApp app;
	protected volatile boolean isRefreshing;
	protected volatile boolean stopCurrentRefresh;

	/**
	 */
	protected FrameResult(FrameApp app) {
		super();
		this.app = app;
		final FrameResult frameResult = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frameResult.dispose();
			}
		});

		setPreferredSize(new Dimension(500, 600));
		this.setSize(new Dimension(100, 100));
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout(5, 5));

		this.txtResult = new JTextPane(); 
		this.txtResult.setContentType("text/html; charset=UTF-8");
		this.txtResult.setPreferredSize(getPreferredSize());
		this.txtResult.setEditable(false);

		PopUp.pop(this.txtResult);

		JScrollPane ascTxtResult = new JScrollPane(this.txtResult,
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


		JPanel panneauBoutons = new JPanel();
		JPanel panneauBoutons2 = new JPanel();

		this.jbFermer = new JButton(FrameResult.BT_CLOSE);
		this.jbFermer.addActionListener(this);
		this.jbRefresh = new JButton(FrameResult.BT_REFRESH);
		this.jbRefresh.setVisible(false);
		this.jbRefresh.addActionListener(this);

		constructPanelButton(panneauBoutons2);

		panneauBoutons.add(panneauBoutons2, BorderLayout.EAST);

		contenu.add(ascTxtResult, BorderLayout.CENTER);
		contenu.add(panneauBoutons, BorderLayout.SOUTH);

		this.setLang();

		pack();
	}


	protected void constructPanelButton(JPanel pannelButton){
		pannelButton.add(this.jbFermer);
		pannelButton.add(this.jbRefresh);

	}


	/**
	 * actionPerformed : this method is called when the button close is clicked 
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == FrameResult.BT_CLOSE) {
			dispose();
		} else
			refresh();

	}

	@Override
	public void dispose() {
		super.dispose();
	}

	protected Color getBgTextColor() {
		return Color.white;
	}

	protected void insertHTML(String s) {
		// try {
		// kitHTML.insertHTML(docHTML,docHTML.getLength(),s,0,0,HTML.Tag.HTML);
		// kitHTML.read(new StringReader(s), docHTML, docHTML.getLength());
		this.stringResult.append(s);
		// } catch (BadLocationException e) {
		// SwingIO.PrintIf(e);
		// }
		// catch (IOException e) {
		// SwingIO.PrintIf(e);
		// }
	}

	protected void insertTXT(String s, int size, int align) {
		
		try {
			this.docTXT.insertString(this.offset, s, null);
		} catch (BadLocationException e) {
			SwingIO.error(this.getClass().getName(), "insertTXT", e
					.getMessage(), e);
		}
		int current = this.offset;
		this.offset += s.length();

		SimpleAttributeSet aSet = new SimpleAttributeSet();
		StyleConstants.setForeground(aSet, Color.black);
		StyleConstants.setBackground(aSet, getBgTextColor());
		StyleConstants.setFontFamily(aSet, "San Serif");
		StyleConstants.setFontSize(aSet, size);

		StyledDocument doc = this.txtResult.getStyledDocument();
		doc.setCharacterAttributes(current, this.offset, aSet, false);

		SimpleAttributeSet bSet = new SimpleAttributeSet();
		StyleConstants.setForeground(bSet, Color.black);
		StyleConstants.setBackground(bSet, getBgTextColor());
		StyleConstants.setFontFamily(bSet,  "San Serif");
		StyleConstants.setAlignment(bSet, align);
		StyleConstants.setFontSize(bSet, size);

		doc.setParagraphAttributes(current, this.offset, bSet, false);
		
		if (stopCurrentRefresh) 
			isRefreshing = false;

	}

	public abstract void putResultTXT();

	/**
	 * 
	 */
	public void refresh() {

		new Thread (new Runnable() {
			public void run() {
				stopCurrentRefresh= true;
				while (isRefreshing)
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				stopCurrentRefresh = false;
				showResults();

			}

		}).start();



	}


	/* (non-Javadoc)
	 * @see java.awt.Window#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		this.refresh();
	}


	private void showResults() {
	
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				offset = 0;
				stringResult = new StringBuffer();

				docTXT = new DefaultStyledDocument();
				txtResult.setEditorKit(new StyledEditorKit());
				txtResult.setContentType("text/plain; charset=UTF-8");
				txtResult.setDocument(docTXT);
				txtResult.setBackground(getBgTextColor());

				txtResult
				.setComponentOrientation((ComponentOrientation) bundle
						.getObject("orientation"));

				/*
				 * SimpleAttributeSet aSet = new SimpleAttributeSet();
				 * StyleConstants.setForeground(aSet, Color.blue);
				 * StyleConstants.setBackground(aSet, Color.orange);
				 * StyleConstants.setFontFamily(aSet, "lucida bright italic");
				 * StyleConstants.setFontSize(aSet, 18);
				 * 
				 * SimpleAttributeSet bSet = new SimpleAttributeSet();
				 * StyleConstants.setAlignment(bSet, StyleConstants.ALIGN_CENTER);
				 * StyleConstants.setUnderline(bSet, true);
				 * StyleConstants.setFontFamily(bSet, "lucida typewriter bold");
				 * StyleConstants.setFontSize(bSet, 24);
				 * 
				 * JTextPane textPane = new JTextPane(); textPane.setText(text);
				 * StyledDocument doc = textPane.getStyledDocument();
				 * doc.setCharacterAttributes(105, doc.getLength()-105, aSet, false);
				 * doc.setParagraphAttributes(0, 104, bSet, false);
				 */

				putResultTXT();
				txtResult.setCaretPosition(0); // se repositionner en haut de la fenetre


			}

		});

	}

	public void tabule(int deb, int fin, int xmintabs, int width, int ntabs) {

		List list = new ArrayList();

		// Create a left-aligned tab stop at 100 pixels from the left margin
		float pos;

		int align;
		int leader;
		TabStop tstop;
		if (this.txtResult.getComponentOrientation().equals(
				ComponentOrientation.LEFT_TO_RIGHT)) {
			pos = 0; // metrics.stringWidth(Gallery.PSI +
			// mesomery.countAllSructs() + " ");
			align = TabStop.ALIGN_LEFT;
		} else {
			pos = this.txtResult.getWidth();
			align = TabStop.ALIGN_RIGHT;
		}
		leader = TabStop.LEAD_NONE;
		tstop = new TabStop(pos, align, leader);
		list.add(tstop);

		for (int j = 0; j < ntabs; j++) {
			if (this.txtResult.getComponentOrientation().equals(
					ComponentOrientation.LEFT_TO_RIGHT))
				pos = xmintabs + width + j * width;
			else
				pos = this.txtResult.getWidth() - xmintabs - width - j * width;
			align = TabStop.ALIGN_DECIMAL;
			leader = TabStop.LEAD_NONE;
			tstop = new TabStop(pos, align, leader);
			list.add(tstop);
		}
		// Create a tab set from the tab stops
		TabStop[] tstops = (TabStop[]) list.toArray(new TabStop[0]);
		TabSet tabs = new TabSet(tstops);

		// Add the tab set to the logical style;
		// the logical style is inherited by all paragraphs
		// Add the tab set to the logical style;
		// the logical style is inherited by all paragraphs

		SimpleAttributeSet attributes = new SimpleAttributeSet();
		StyleConstants.setTabSet(attributes, tabs);
		this.txtResult.getStyledDocument().setParagraphAttributes(deb, fin,
				attributes, false);

		// autre possibilite sur le document entier
		// Style style = this.txtResult.getLogicalStyle();
		// StyleConstants.setTabSet(style, tabs);
		// txtResult.setLogicalStyle(style);

	}


	public void setLang() {
		this.bundle = LanguageManager.getInstance().getResource("FrameResult");
		Font btFont = (Font) bundle.getObject("font");

		this.jbFermer.setFont(btFont);
		this.jbRefresh.setFont(btFont);
		FrameResult.BT_CLOSE = this.bundle.getString("kclose");
		FrameResult.BT_REFRESH = this.bundle.getString("krefresh");
		this.jbFermer.setText(FrameResult.BT_CLOSE);
		this.jbRefresh.setText(FrameResult.BT_REFRESH);	
	}

	public void updateLanguage() {
		setLang();
		refresh();
	}

}
