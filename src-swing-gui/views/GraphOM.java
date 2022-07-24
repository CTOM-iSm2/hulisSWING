package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JPanel;

import org.apache.batik.apps.svgbrowser.TransformHistory;
import org.ctom.hulis.huckel.NatOrbitaleMoleculaire;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.prefs.Preferences;

import controller.graphOM.GraphOMController;
import util.ThreadUtils;
import util.io.SwingIO;

@SuppressWarnings("serial")
public class GraphOM extends JPanel implements IStructureListener, IStructureDelocalizedListener, IStructureLocalizedListener {



	public final static int DEFAULT_BETA_MAX = 5;
	final static boolean DRAW_ARROW_HEAD_PART1 = true;
	final static boolean DRAW_ARROW_HEAD_PART2 = false;
	public final static int MAX_ITER_ORGANIZE_GRAPH = 1000;
	public final static int X_MARGIN = 15;
	public final static int Y_MARGIN = 10;

	public static Dimension optimal_size = null;

	private double betaMax;

	private Point currentPointDragged;

	private boolean drawingExcitation = false;

	private ArrayList<OrbitalControl> orbitalControls;
	private OrbitaleMoleculaire sOMSelected;

	private Point2D pointFrom = null; // Deplacement NG

	private Structure structure;
	private StructureView structureView;

	private GraphOMController graphOMController;

	public GraphOM(Structure structure, StructureView structureView) {
		super();

		this.structure = structure;
		this.structureView = structureView;
		this.betaMax = GraphOM.DEFAULT_BETA_MAX;
		this.orbitalControls = new ArrayList<OrbitalControl>();
		graphOMController = new GraphOMController(this);
		addMouseListener(graphOMController);
		addMouseMotionListener(graphOMController);
		this.structure.addListener(this);
		this.currentPointDragged = new Point();
	}


	/**
	 * 
	 * @param ec1
	 * @return
	 */
	private EnergyControl getPartner (OrbitalControl ec1) {
		SpinOrbitaleMoleculaire partner =  ((EnergyControl) ec1).getSOM().getPartner() ;
		for (OrbitalControl oc : this.getOrbitalControls()) {
			EnergyControl current = (EnergyControl) oc;
			if ( partner ==  current.getSOM())
				return current;
		}
		return null;
	}

	public void erase() {
		this.removeMouseListener(graphOMController);
		removeMouseMotionListener(graphOMController);
		graphOMController = null;
	}

	public StructureView getStructureView() {
		return this.structureView;
	}

	public double getBetaMAx() {
		return this.betaMax;
	}

	public ArrayList<OrbitalControl> getOrbitalControls() {
		return this.orbitalControls;
	}

	@Override
	public Insets getInsets() {
		return new Insets(4, 4, 4, 4);
	}


	public void moleculeDeleted(MoleculeEvent e) {
		this.structureView.setShownOrbital(null);
		repaint();
	}

	public ArrayList<EnergyControl> getVirtualEnergyControlAt(Point2D p) {
		ArrayList<EnergyControl> list = new ArrayList<EnergyControl>(2);
		for (OrbitalControl ec : this.getOrbitalControlAt(p)) {
			if ((((EnergyControl) ec).getSpin()==Spin.NOSPIN_ALPHA)||(((EnergyControl) ec).getSpin()==Spin.NOSPIN_BETA)) {
				list.add((EnergyControl) ec);
			}
		}
		return list;
	}

	public ArrayList<EnergyControl> getOccupiedEnergyControlAt(Point2D p) {
		ArrayList<EnergyControl> list = new ArrayList<EnergyControl>(2);
		for (OrbitalControl ec : this.getOrbitalControlAt(p)) {
			SpinOrbitaleMoleculaire som = ((EnergyControl) ec).getSOM();
			Spin spin = som.getSpin();
			if ((spin==Spin.ALPHA)||(spin==Spin.BETA)) {
				list.add((EnergyControl) ec);
			}
		}
		return list;
	}

	private ArrayList<OrbitalControl> getOrbitalControlAt(Point2D p) {
		ArrayList<OrbitalControl> list = new ArrayList<OrbitalControl>(2);
		for (OrbitalControl ec : getOrbitalControls()) {
			if (ec.contains(p)) {
				list.add(ec);
			}
		}
		return list;
	}

	// synchronized pour le tutorial
	@Override
	public synchronized void paint(Graphics g) {
		super.paint(g);


		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Helvetica", Font.PLAIN, 12));

		// ****** draw graph
		// dessine la grille

		double yScale;
		double height=0.0;
		int max = DEFAULT_BETA_MAX;
		if (optimal_size==null)
			optimal_size = this.getSize();


		if (this.getHeight()<=optimal_size.getHeight()){
			height = getHeight()  - 2 * GraphOM.Y_MARGIN;  // on considere la hauteur relle  pour lehechelle si on r�duit la fenetre ne dessous de la taille de depart
		}
		else {
			height =optimal_size.getHeight() - 2 * GraphOM.Y_MARGIN; // si on etire la fenetre on considere tj la hauteur de la fenetre de depart pour l'echelle
			max= (int) ( (this.getHeight()- 2 * GraphOM.Y_MARGIN) * (DEFAULT_BETA_MAX / height))	;

		}

		yScale= height / (2 * DEFAULT_BETA_MAX);		

		double y0 = getHeight() / 2;
		double x0 = getWidth() / 2;

		g2d.drawLine((int) x0, GraphOM.Y_MARGIN, (int) x0, getHeight()
				- GraphOM.Y_MARGIN);


		for (int y = -max; y <= max; y++) {
			g2d.drawString(Integer.toString(-y), 0, (int) (y0 - y * yScale)); // ecrit
			// un
			// chiffre
			// entier
			// :
			// energie
			// beta
			g2d.drawLine(GraphOM.X_MARGIN, (int) (y0 - y * yScale),
					getWidth() - GraphOM.X_MARGIN, (int) (y0 - y
							* yScale));
		}

		// **** draw energies

		int nSOM;
		try {
			nSOM = this.structure.countSpinOM();
		} catch (NullPointerException e1) {
			return;
		}
		if ( nSOM == 0)
			return;

		// Arrays.sort(eOrbs); // **** SORT THE ARRAY OF ENERGY IN ORDER OF BETA
		// VALUE

	

		// Par defaut toutes les cases d'energies sont placees au centre du
		// graphiques
		double x;
		double y = 0;

		//
		//Generation des cases quantiques

		this.orbitalControls=new ArrayList<OrbitalControl>(nSOM);

		switch (Preferences.DISPLAY_ORBITAL) {
		case CANONICAL:

			try {
				for (int i = 0; i < nSOM; i++) {
					SpinOrbitaleMoleculaire som = this.structure.getSpinOM(i);
					y = y0 + som.getEnergy().getBeta() * yScale;
					x = x0;
					this.orbitalControls.add(new EnergyControl(this, som, new Point2D.Double(
							x, y)));
				}
			} catch (NoSOMAvailableException e1) {
				e1.printStackTrace();
				SwingIO.error(this.getClass().getName(), "EnergiesOrbView.paint", "No SOM available");
			} catch (UnavailableSOMException e1) {
				e1.printStackTrace();
				SwingIO.error(this.getClass().getName(), "EnergiesOrbView.paint", "Specified SOM available");
			}
		
			Collections.sort(orbitalControls, new Comparator() {
				
				@Override
				public int compare(Object o1, Object o2) {
					try {
						SpinOrbitaleMoleculaire som1 = ( (EnergyControl) o1).getSOM();
						SpinOrbitaleMoleculaire som2 = ( (EnergyControl) o2).getSOM();
						// les soms partenaires sont egales
						
						if (som1.getPartner() == som2)
							return 0;
						else if (som1.getEnergy().isGreaterThan(som2.getEnergy(), StructureDelocalized.PRECISION)){
							return 1;
						}
						else if (som2.getEnergy().isGreaterThan(som1.getEnergy(), StructureDelocalized.PRECISION)){
							return -1;
						}
						else  {
							// cas ou les soms ne sont pas partenaires mais egales en energie, alors le spin alpha est a gauche
							if ((som1.getSpin() == Spin.ALPHA) && ( (som2.getSpin() == Spin.BETA) || (som2.getSpin() == Spin.NOSPIN_BETA) || (som2.getSpin() == Spin.NOSPIN_ALPHA)  ))
								return -1;
							else if ((som2.getSpin() == Spin.ALPHA) && ((som1.getSpin() == Spin.BETA ) || (som1.getSpin() == Spin.NOSPIN_BETA) || (som1.getSpin() == Spin.NOSPIN_ALPHA) )  )
								return 1;
							else return 0;
										
						}
					}
					catch (Exception e1) {
						e1.printStackTrace();
						return 0;
					}
				
				}
				
			});

			break;
		case NATURAL:
			int nNO = this.structure.getNaturalOrbitals().size();
			for (int i = 0; i<nNO; i++) {
				NatOrbitaleMoleculaire om = this.structure.getNatOM(i);
				y=y0+om.getOcc()*yScale;
				x=x0;
				this.orbitalControls.add(new OccupationControl(this, om, new Point2D.Double(
						x, y)));
			}
			break;

		default:
			break;
		}
		//


		boolean ok = false;
		int iter = 0;

		// On boucle jusqu'a une valeur seuil de tolerance pour placer les
		// 'cases d'energies' de sorte qu'elles ne se
		// touchent en utilisant un test de collision (intersect).
		// si elle se touchent on les decale jusqu'a un emplacement libre et cela
		// jusqu'au seuil de tolerance d'iterations
		while (!ok) {

			ok = true;

			if (iter == GraphOM.MAX_ITER_ORGANIZE_GRAPH)
				break;

			iter++;

			switch (Preferences.DISPLAY_ORBITAL) {
			case CANONICAL:
				for (int i = 0; i < this.orbitalControls.size(); i++) {

					for (int j = 0; j < this.orbitalControls.size(); j++) {
						OrbitalControl ec1 = this.orbitalControls.get(i);
						OrbitalControl ec2 = this.orbitalControls.get(j);
						//si les orbitales sont partenaires, on les dessine au meme niveau sinon on les d�cale du centre
						if (((EnergyControl) ec1).getSOM().getPartner() != ((EnergyControl)ec2).getSOM()) {

							if (ec1.intersects(ec2)
									&& (i != j)) {
								x = ec1.getPoint().getX();
								y = ec1.getPoint().getY();
								x = x - EnergyControl.WIDTH * 1.5;
								ec1.setLocation(new Point2D.Double(
										x, y));
								this.getPartner(ec1).setLocation(new Point2D.Double(
										x, y));

								x = ec2.getPoint().getX();
								y = ec2.getPoint().getY();
								x = x + EnergyControl.WIDTH * 1.5;
								ec2.setLocation(new Point2D.Double(
										x, y));
								this.getPartner(ec2).setLocation(new Point2D.Double(
										x, y));
								ok = false;
							}
						}
					}// for j

				}// for i
				break;
			case NATURAL:
				for (int i = 0; i < this.orbitalControls.size(); i++) {

					for (int j = 0; j < this.orbitalControls.size(); j++) {
						OrbitalControl ec1 = this.orbitalControls.get(i);
						OrbitalControl ec2 = this.orbitalControls.get(j);
						if (ec1.intersects(ec2)
								&& (i != j)) {
							x = ec1.getPoint().getX();
							y = ec1.getPoint().getY();
							x = x + EnergyControl.WIDTH * 1.5;
							ec1.setLocation(new Point2D.Double(
									x, y));
							this.getPartner(ec1).setLocation(new Point2D.Double(
									x, y));
							
							x = ec2.getPoint().getX();
							y = ec2.getPoint().getY();
							x = x - EnergyControl.WIDTH * 1.5;
							ec2.setLocation(new Point2D.Double(
									x, y));
							this.getPartner(ec2).setLocation(new Point2D.Double(
									x, y));
							ok = false;
						}

					}// for j

				}// for i
			default:
				break;
			}

		} // while ok

		// on place les composants apres qu'ils aient ete localises precedement
		// on est oblige de faire une boucle a part pour les dessiner en une
		// seules fois sinon ils
		// se dessinent plusieurs fois

		for (int i = 0; i < this.orbitalControls.size(); i++) {
			this.orbitalControls.get(i).draw(g2d);
		}

	} // paint

	public void setBetaMax(double betaMax) {
		this.betaMax = betaMax;
	}

	public void setEOrbSelected(OrbitaleMoleculaire spinOrbitaleMoleculaire) {
		this.setOMSelected(spinOrbitaleMoleculaire);
	}

	protected void handleMoleculeChanged(){
		this.structureView.setShownOrbital(null);
		repaint();

	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});	

	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}



	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		
	}



	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			
	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});		}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			

	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		ThreadUtils.invokeAsSoonAsPossible(new Runnable() {

			public void run() {
				handleMoleculeChanged()	;
			}

		});			

	}

	////@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	////@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	////@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub

	}

	public Structure getStructure() {
		return this.structure;
	}

	public Point getCurrentPointDragged() {
		return currentPointDragged;
	}

	public void setCurrentPointDragged(Point currentPointDragged) {
		this.currentPointDragged = currentPointDragged;
	}

	public boolean isDrawingExcitation() {
		return drawingExcitation;
	}

	public void setDrawingExcitation(boolean drawingExcitation) {
		this.drawingExcitation = drawingExcitation;
	}

	public void setPointFrom(Point2D pointFrom) {
		this.pointFrom = pointFrom;
	}

	public Point2D getPointFrom() {
		return pointFrom;
	}

	public OrbitaleMoleculaire getsOMSelected() {
		return sOMSelected;
	}

	public void setOMSelected(OrbitaleMoleculaire sOMSelected) {
		this.sOMSelected = sOMSelected;
	}







}
