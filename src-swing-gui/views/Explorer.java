package views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import util.io.SwingIO;

public class Explorer extends JFrame implements HyperlinkListener {

	protected JEditorPane txtResult;

	public Explorer(URL url) throws IOException {
		super();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

		setPreferredSize(new Dimension(800, 600));
		Container contenu = getContentPane();
		contenu.setLayout(new BorderLayout(5, 5));

		this.txtResult = new JEditorPane(url);
		this.txtResult.addHyperlinkListener(this);
		this.txtResult.setPreferredSize(getPreferredSize());
		this.txtResult.setEditable(false);

		pop(this.txtResult);

		JScrollPane ascTxtResult = new JScrollPane(this.txtResult,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// yannick
		ascTxtResult
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		ascTxtResult.setPreferredSize(new Dimension(800, 600));
		// yannick

		contenu.add(ascTxtResult, BorderLayout.CENTER);

		pack();
		setVisible(true);
	}

	public void hyperlinkUpdate(HyperlinkEvent event) {
		if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				this.txtResult.setPage(event.getURL());
			} catch (IOException ioe) {
				SwingIO.warning(this.getClass().getName(), "hyperlinkUpdate",
						"Can't follow link to "
								+ event.getURL().toExternalForm() + ": " + ioe,
						ioe);

			}
		}
	}

	public void pop(final JEditorPane t1) {

		final JPopupMenu x = new JPopupMenu();

		JMenuItem i1 =  new JMenuItem("cut");
		JMenuItem i2 = new JMenuItem("copy");
		JMenuItem i3 =  new JMenuItem("paste");
		JMenuItem i4 =  new JMenuItem("select all");

		i1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		i2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				ActionEvent.CTRL_MASK));
		i3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				ActionEvent.CTRL_MASK));
		i4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.CTRL_MASK));

		/* x.add(i1); */x.add(i2);/* x.add(i3); */
		x.addSeparator();
		x.add(i4);

		i1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.cut();
			}
		});

		i2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.copy();
			}
		});

		i3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.paste();
			}
		});

		i4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.selectAll();
			}
		});

		t1.addMouseListener(new MouseAdapter() {
			private void checkForTriggerEvent(MouseEvent e) {
				if (e.isPopupTrigger())
					x.show(e.getComponent(), e.getX(), e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				checkForTriggerEvent(e);
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				checkForTriggerEvent(e);
			}
		});
	}

}
