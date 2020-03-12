package org.ctom.view.Dessin.Interactif.Composite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.JMenu;

import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.DessinMode;
import org.ctom.view.Dessin.Interactif.DessinInteractif;


public abstract class DessinComposite implements Dessin, DessinInteractif {

	private Color color = Color.BLACK;

	private ArrayList<Dessin> dessins = new ArrayList<Dessin>();

	private DessinMode mode = DessinMode.NORMAL;

	protected String titre;

	public void add(Dessin d) {
			d.setMode(this.getMode());
		this.dessins.add(d);
	}

	//@Override
	public boolean contains(double x, double y) {
		return this.getDessinInteractifAt(x, y).size() != 0;
	}

	//@Override
	public void draw(Graphics2D g2d) {
		HashSet<Dessin> toDraw = this.getToDraw();
		for (Dessin d : toDraw) {
			Color oldColor = g2d.getColor();
			g2d.setColor(this.getColor());
			d.draw(g2d);
			g2d.setColor(oldColor);
		}
	}

	public HashSet<DessinInteractif> getAllDessinInteractif() {
		HashSet<DessinInteractif> set = new HashSet<DessinInteractif>();
		for (Dessin d : this.getToDraw()) {
			if (d instanceof DessinInteractif) {
				set.add((DessinInteractif) d);
			}
		}
		return set;
	}

	/**
	 * @return the color
	 */
	//@Override
	public Color getColor() {
		return this.color;
	}

	public HashSet<DessinInteractif> getDessinInteractifAt(double x, double y) {
		HashSet<DessinInteractif> set = new HashSet<DessinInteractif>();
		for (DessinInteractif di : this.getAllDessinInteractif()) {
			if (di.contains(x, y)) {
				set.add(di);
			}
		}
		return set;
	}

	/**
	 * @return the dessins
	 */
	public ArrayList<Dessin> getDessins() {
		return this.dessins;
	}

	/**
	 * @return the mode
	 */
	//@Override
	public DessinMode getMode() {
		return this.mode;
	}

	/*
	 * (non-Javadoc)
	 * @see org.ctom.view.Dessin.Interactif.DessinInteractif#getPopupMenu(int, int)
	 */
	//@Override
	public TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y) {
		TreeSet<JMenu> list = null;
		for (DessinInteractif di : this.getDessinInteractifAt(x, y)) {
			if (list == null) {
				list = new TreeSet<JMenu>();
			}
			TreeSet<JMenu> popupMenu = di.getPopupMenu(al, x, y);
			if (popupMenu != null) {
				list.addAll(popupMenu);
			}
		}
		return list;
	}

	//@Override
	public Shape getShape() {
		return null;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	//@Override
	public void setColor(Color color) {
		this.color = color;
	}

	//@Override
	public void setLineWidth(int lw) {
		for (Dessin d : this.dessins) {
			d.setLineWidth(lw);
		}
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	//@Override
	public void setMode(DessinMode mode) {
		this.mode = mode;
		for (Dessin d : this.dessins) {
			d.setMode(this.getMode());
		}
	}

	//@Override
	public void setSelected() {
		this.setMode(DessinMode.SELECTED);
	}

	//@Override
	public void unsetSelected() {
		this.setMode(DessinMode.NORMAL);
	}

	protected String getTitre() {
		return this.titre;
	}

	protected HashSet<Dessin> getToDraw() {
		HashSet<Dessin> todraw = new HashSet<Dessin>();
		for (Dessin d : this.dessins) {
			if (d instanceof DessinComposite) {
				HashSet<Dessin> list = ((DessinComposite) d).getToDraw();
				todraw.addAll(list);
			} else {
				todraw.add(d);
			}
		}
		return todraw;
	}

	protected abstract void setTitre(String string);

}
