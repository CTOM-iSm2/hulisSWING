/**
 * 
 */
package org.ctom.hulis.util.geometry;

import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.VecteurCTOM;

/**
 * @author macbook
 *
 */
public class InitialOrientation {
	
	VecteurCTOM axis;
	double angle;
	Point3D origin;
	/**
	 * 
	 * @param axis : rotate axis
	 * @param angle : rotate angle
	 * @param origin : centre de la molecule apres translation
	 */
	public InitialOrientation (VecteurCTOM axis, double angle, Point3D origin) {
		this.axis = axis;
		this.angle = angle;
		this.origin = origin;
	}
	
	public InitialOrientation (InitialOrientation initialOrientation) {
		this.axis = (VecteurCTOM) initialOrientation.axis.clone();
		this.angle = initialOrientation.angle;
		this.origin = (Point3D) initialOrientation.origin.clone();
	}
	
	
	public Object clone() {
		return new InitialOrientation (this);
	}
	
	
	/**
	 * 
	 */
	public InitialOrientation() {
		this.axis = VecteurCTOM.V_Z_AXIS;
		this.angle = 0.0;
		this.origin = new Point3D(0.0, 0.0 ,0.0);
	}


	/**
	 * 
	 * @return
	 */
	public VecteurCTOM getAxis(){
		return this.axis;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getAngle() {
		return this.angle;
	}
	
	/**
	 * 
	 * @return
	 */
	public Point3D getOrigin() {
		return this.origin;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "axis = " + this.axis + " ; angle = " + angle + " ; origin = " + this.origin;
	}
}
