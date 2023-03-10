package controller.globalCommands;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

import util.MyResourceBundle;
import util.languages.LanguageManager;

/**
 * This class is a popup edition menu 
 * @author ngoudard
 *
 */
public class PopUp {

	public static void pop(final JTextPane t1) {

		MyResourceBundle menus = LanguageManager.getInstance().getResource("MenuPop");
		final JPopupMenu x = new JPopupMenu();

		JMenuItem i1 = new JMenuItem(menus.getString("mnpopcut"));
		JMenuItem i2 = new JMenuItem(menus.getString("mnpopcopy"));
		JMenuItem i3 = new JMenuItem(menus.getString("mnpoppaste"));
		JMenuItem i4 = new JMenuItem(menus.getString("mnpopselall"));
	
		int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(); // raccourci menu selon l'OS (alt, pomme, etc...)

		i1.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_CUT,shortcutKeyMask));
		i2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COPY,shortcutKeyMask));
		i3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PASTE,shortcutKeyMask));
		i4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,shortcutKeyMask));

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
				// Toolkit toolKit = Toolkit.getDefaultToolkit();
				// Clipboard cb = toolKit.getSystemClipboard();
				// cb.setContents(new StringSelection(t1.getSelectedText()),
				// null);
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
