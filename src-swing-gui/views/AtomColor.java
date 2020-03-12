/**
 * 
 */
package views;

import java.awt.Color;

public enum AtomColor {

	B(AtomDrawer.DEFAULT_ATOM_RADIUS, "-B R<sub>2</sub>", new Color(255, 181,
			181), new Color(255, 181, 181)), 
	C(AtomDrawer.DEFAULT_ATOM_RADIUS,
			"=C R<sub>2</sub>", new Color(144, 144, 144), new Color(144, 144,
					144)), 
	Cl(AtomDrawer.DEFAULT_ATOM_RADIUS, "-Cl", new Color(
			31, 240, 31), new Color(31, 240, 31)), 
			
	F(AtomDrawer.DEFAULT_ATOM_RADIUS, "-F", new Color(144, 224, 80),
			new Color(144, 224, 80)),
	// ATOM_COLORS.put(PeriodicTable.Element.X, new
	// AtomColor(DEFAULT_ATOM_RADIUS, new Color (255,89,172), new Color
	// (255,89,172)));
	H(AtomDrawer.H_ATOM_RADIUS, "-H", new Color(0, 0, 0), Color.WHITE), 
	
	Me(AtomDrawer.DEFAULT_ATOM_RADIUS, "-CH3", new Color(230, 230, 0),
			new Color(230, 230, 0)), 
	N1(AtomDrawer.DEFAULT_ATOM_RADIUS, "=N R",
			new Color(48, 80, 248), new Color(48, 80, 248)),
	
	N2(AtomDrawer.DEFAULT_ATOM_RADIUS, "-N R<sub>2</sub>", new Color(48,
					80, 248), new Color(48, 80, 248)), 
	O1(AtomDrawer.DEFAULT_ATOM_RADIUS, "=O", new Color(255, 13, 13),
			new Color(255, 13, 13)), 
	O2(AtomDrawer.DEFAULT_ATOM_RADIUS, "-O R",
			new Color(255, 13, 13), new Color(255, 13, 13)), P1(
			AtomDrawer.DEFAULT_ATOM_RADIUS, "=P R", new Color(255, 128, 0),
			new Color(255, 128, 0)), 
	P2(AtomDrawer.DEFAULT_ATOM_RADIUS,
			"-P R<sub>2</sub>", new Color(255, 128, 0), new Color(255, 128, 0)), 
	S1(AtomDrawer.DEFAULT_ATOM_RADIUS, "=S", new Color(255, 255, 48),
			new Color(255, 255, 48)), 
	S2(AtomDrawer.DEFAULT_ATOM_RADIUS,
			"-S R", new Color(255, 255, 48), new Color(255, 255, 48)), 
	Si(AtomDrawer.DEFAULT_ATOM_RADIUS, "=Si R<sub>2</sub>", new Color(240,
					200, 160), new Color(240, 200, 160)),
	Xx(AtomDrawer.DEFAULT_ATOM_RADIUS,
			"-Xx", new Color(144, 144, 144), new Color(144, 144,
					144));
	protected Color border;
	protected String devForm;
	protected Color fill;
	protected int radius;

	private AtomColor(int radius, String devForm, Color border, Color fill) {
		this.devForm = devForm;
		this.radius = radius;
		this.border = border;
		this.fill = fill;
	}

	public Color getBorderColor() {
		return this.border;
	}

	public String getDevForm() {
		return this.devForm;
	}

	public Color getFillcolor() {
		return this.fill;
	}

	public int getRadius() {
		return this.radius;
	}
}