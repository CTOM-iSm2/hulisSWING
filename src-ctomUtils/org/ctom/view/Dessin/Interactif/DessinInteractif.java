package org.ctom.view.Dessin.Interactif;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JMenu;

public interface DessinInteractif {
	
	/* 
	 * Mode actif ou non
	 * popup txt
	 * contient ou pas
	 */

	public abstract boolean contains(double x, double y);
	public abstract ArrayList<String> getToolTipText(int x, int y);
	public abstract TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y);

}