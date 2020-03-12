package views;

import java.awt.Color;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.OrbitaleMoleculaire;

public class OccupationControl extends OrbitalControl {
	
	public final static float ARROW_STROKE = 1.9f;
	public final static int VMARGIN_ARROW = 2;
	public final static int VDECALAGE_ARROW = 2;
	public final static float DEFAULT_STROKE = 0.0f;
	public final static double HEIGHT = 15;

	public final static float LINE_STROKE = 1.52f;
	public final static double WIDTH = 13;
	public final Color COLOR_ARROW = Color.RED;
	public final Color COLOR_EXCITATION = Color.ORANGE;
	
	public OccupationControl(GraphOM graphOM, OrbitaleMoleculaire om,
			Point2D p) {
		super(graphOM, om, p);
	}

}