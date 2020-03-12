package util.polyCalculator.view.elements;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import org.ctom.hulis.polynomes.Polynomial;

public class PolyTextField extends JTextField {
	
	private Polynomial pf;
	private ArrayList<PolyTextFieldListener> listeners = new ArrayList<PolyTextFieldListener>();
	
	public PolyTextField(Polynomial pf) {
		this.setPf(pf);
		this.setEditable(false);
		PolyTextFieldMouseListener ml = new PolyTextFieldMouseListener();
		this.addMouseListener(ml);
	}

	/**
	 * @return the pf
	 */
	public Polynomial getPf() {
		return pf;
	}

	/**
	 * @param pf the pf to set
	 */
	protected void setPf(Polynomial pf) {
		this.pf = pf;
		this.setText(pf.toString());
	}

	private class PolyTextFieldMouseListener implements MouseListener {

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
				Object o = e.getSource();
				JPopupMenu popup = new JPopupMenu();
				JMenuItem menu = new JMenuItem();
				menu.setAction(new AbstractAction() {
					public void actionPerformed(ActionEvent arg0) {
						removePoly();
					}
				});
				menu.setText("Remove Poly");
				popup.add(menu);
	            popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }

		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	public void addlistener(PolyTextFieldListener l) {
		this.listeners.add(l);
	}
	
	public void removePoly() {
		for (PolyTextFieldListener l : this.listeners) {
			l.removePoly(getPf());
		}
	}

}
