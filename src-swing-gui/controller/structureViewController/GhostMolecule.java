package controller.structureViewController;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.IHuckelObject;

import views.StructureView;

public class GhostMolecule {

	private ArrayList<Atom> lstAtoms; // atoms which compose a molecule
	private ArrayList<Bond> lstBonds; // Bonds that compose a molecule
	private Point2D mousePosition;
	private Point2D offset;

	public GhostMolecule(StructureView structureView, Point2D mousePosition) {
		super();
		structureView.setGhost(this);
		this.mousePosition=mousePosition;
		lstAtoms = new ArrayList<Atom>();
		lstBonds = new ArrayList<Bond>();
	}

	public boolean add(IHuckelObject o){
		if (o instanceof Bond) {
			return lstBonds.add((Bond) o);
		}
		if (o instanceof Atom) {
			return lstAtoms.add((Atom) o);
		}
		return false;
	}

	public ArrayList<Bond> getBonds() {
		return lstBonds;
	}

	public ArrayList<Atom> getAtoms() {
		return lstAtoms;
	}

	public Point2D getOffset() {
		this.setOffset();
		return this.offset;
	}

	private void setOffset() {
		this.offset = new Point2D.Double(mousePosition.getX()-lstAtoms.get(0).getX(),mousePosition.getY()-lstAtoms.get(0).getY());
	}

}
