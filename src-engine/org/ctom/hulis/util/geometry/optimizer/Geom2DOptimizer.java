package org.ctom.hulis.util.geometry.optimizer;

import java.util.ArrayList;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.minimizer.PowellMinimizer;
import org.ctom.util.maths.minimizer.PowellMinimizerListener;

import Jama.Matrix;

public class Geom2DOptimizer implements IMoleculeValueListener, PowellMinimizerListener {
	
	private Molecule molecule;
	private boolean opt=true;
	private boolean verbose=false;
	private ArrayList<HuckelAtom> listAtoms;
	
	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/**
	 * Create an optimizer for the 2D geometry of the molcule in argument
	 * @param molecule
	 */
	public Geom2DOptimizer(Molecule molecule) {
		this.molecule = molecule;
		molecule.addListener(this);
	}
	
	private void updateAndOptimize() {
		if (! this.opt) return;
		listAtoms=null;
		listAtoms = (ArrayList<HuckelAtom>) molecule.getHuckelAtoms();
		
		Point3D G = computeCenterofMass(listAtoms);
		if (G.getZ() != 0) {
			HuckelIO.error(this.getClass().toString(), "updateAndOptimize", "Non planar molecule : cannot optimize geometry");
		}
		
		Matrix geom = new Matrix(2*listAtoms.size(),1);
		for (int iat = 0; iat<listAtoms.size(); iat++) {
			Atom a = listAtoms.get(iat);
			geom.set(iat*2  , 0, a.getX());
			geom.set(iat*2+1, 0, a.getY());
		}
		
		Geom2DOptimizableFunction func = new Geom2DOptimizableFunction(molecule);
		PowellMinimizer opt = new PowellMinimizer(func);
		opt.setOptimizationParameters(geom,100, 1e-4, 1e-4);// commentaire goudard
		//opt.setOptimizationParameters(geom,100, 0.1, 0.1);
		opt.addListener(this);
		Matrix optgeom = opt.minimize();
		if (verbose) HuckelIO.PrintIf(opt.getStopReason().toString()+" : "+opt.getStopValue());
		updateCoords(optgeom);
	}

	private void updateCoords(Matrix optgeom) {
		for (int iat = 0; iat<listAtoms.size(); iat++) {
			Atom a = listAtoms.get(iat);
			a.setXNotNotify(optgeom.get(iat*2  , 0));
			a.setYNotNotify(optgeom.get(iat*2+1, 0));
			molecule.correctHBonds();
		}
	}

	private Point3D computeCenterofMass(ArrayList<HuckelAtom> listAtoms) {
		Point3D G = new Point3D(0, 0, 0);
		for (Atom a : listAtoms) {
			G.plusEquals(a.getLocation());
		}
		return G;
	}

	public void moleculeAtomAdded(MoleculeEvent e) {updateAndOptimize();}
	public void moleculeAtomRemoved(MoleculeEvent e) {updateAndOptimize();}
	public void moleculeBondAdded(MoleculeEvent e) {updateAndOptimize();}
	public void moleculeBondRemoved(MoleculeEvent e) {updateAndOptimize();}
	public void moleculeAtomReplaced(MoleculeEvent e) {updateAndOptimize();}

	public void moleculeDeleted(MoleculeEvent e) {}
	public void huckelAtomHxChanged(HuckelAtomEvent e) {}
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {}
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {}
	public void huckelBondHxyChanged(HuckelBondEvent e) {}
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {}
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {}
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {}
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {}
	public void moleculeChargeChanged(MoleculeEvent event) {}
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {}
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {}
	public void moleculeBlocDelocalizedAdded(MoleculeEvent event) {}
	public void moleculeBlocDelocalizedRemoved(MoleculeEvent event) {}

	/**
	 * if the argument is true, the geometry is optimized
	 * @param b
	 */
	public void setOptimize(boolean b) {
		this.opt=b;
	}

	public void intermediateValues(Matrix matrix) {
		updateCoords(matrix);
	}

}
