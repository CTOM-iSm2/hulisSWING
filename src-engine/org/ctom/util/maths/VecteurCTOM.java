/**
 * 
 */
package org.ctom.util.maths;

import Jama.Matrix;



/**
 * @author macbook
 *
 */
public class VecteurCTOM extends Point3D{
	
	public static final VecteurCTOM V_X_AXIS = new VecteurCTOM (1,0,0); //unit vector of X axis
	public static final VecteurCTOM V_Y_AXIS = new VecteurCTOM (0,1,0);// unit vector of Y axis
	public static final VecteurCTOM V_Z_AXIS = new VecteurCTOM (0,0,1);// unit vector of Z axis
	
	
	
	/**
	 * define a vector v(x, y, z)
	 * @param x
	 * @param y
	 * @param z
	 */
	public VecteurCTOM (double x, double y, double z) {
		super(x,y,z);
	}
	
	
	
	/**
	 * 
	 * @param v
	 */
	public VecteurCTOM (VecteurCTOM v) {
		super (v);
	}
	

	/**
	 * Define a vector AB
	 * @param pA
	 * @param pB
	 */
	public VecteurCTOM (Point3D pA, Point3D pB) {
		super ( pB.getX() - pA.getX(),	pB.getY() - pA.getY(), pB.getZ() - pA.getZ());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new VecteurCTOM(this);
	}
	
	
	/* (non-Javadoc)
	 * @see org.ctom.util.JamaCTOM.Matrix#times(double)
	 */
	@Override
	public VecteurCTOM times(double d){ 
		VecteurCTOM v = new VecteurCTOM(this);
		return v.timesEquals(d);
	}
	
	
	/* (non-Javadoc)
	 * @see org.ctom.util.JamaCTOM.Matrix#timesEquals(double)
	 */
	@Override
	public VecteurCTOM timesEquals(double d){ 
		Matrix m = super.times(d);	
		this.setX(m.get(0, 0));
		this.setY(m.get(1, 0));
		this.setZ(m.get(2, 0));
		return this;

	}
	
		

	/**
	 * 
	 * @return
	 */
	public boolean isInfinite(){
		return super.isNull();
	}
	
	
	/**
	 * 
	 */
	public String toString() {
		return "vector(" + this.getX() +", " + this.getY() + ", "+ this.getZ() + ")";
	}
	
	/**
	 * 
	 * @return
	 */
	public double getNorm() {
		return Math.sqrt(this.getDotProduct(this) );
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public double getDotProduct(VecteurCTOM other) {
		return this.getX() * other.getX() + this.getY() * other.getY() + this.getZ() * other.getZ(); // produit scalaire
	}
	
	

	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public double getAngle (VecteurCTOM other) {
		return Math.acos(this.getDotProduct(other) / ( this.getNorm() * other.getNorm()));
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public VecteurCTOM getCrossProduct(VecteurCTOM other){
		double x = this.getY() * other.getZ() - this.getZ() * other.getY();
		double y = this.getZ() * other.getX() - this.getX() * other.getZ();
		double z = this.getX() * other.getY() - this.getY() * other.getX();
		return new VecteurCTOM (x, y, z);
	}
	
	/**
	 * This function return a new vector that is this vector normalized to a unit vector 
	 * @return
	 */
	public VecteurCTOM normalize() {
		VecteurCTOM v = (VecteurCTOM) this.clone();
		return v.normalizeEquals();
	}
	
	
	
	/**
	 * This method normalizes this vector to a unit vector 
	 * @return
	 */
	public VecteurCTOM normalizeEquals() {
		double norm = this.getNorm();
		this.setX(this.getX() / norm);
		this.setY(this.getY() / norm);
		this.setZ(this.getZ() / norm);
		return this;
		
	}
	
	
	/**
	 * This function returns a normalized vector that is this vector to the desired norm
	 * it is equivalent to this.normalize().times(d)
	 * @param d : desired norm
	 * @return
	 */
	public VecteurCTOM renorm(double d) {
		return this.normalize().times(d);
	}
	
	
	/**
	 * This normalizes this vector to the desired norm
	 * @param d : desired norm
	 * @return
	 */
	public VecteurCTOM renormEquals(double d) {
		this.normalizeEquals();
		this.timesEquals(d);
		return this;
	}
	
	/**
	 * This function returns a new vector that is this vector rotated by the rotation matrix given in paraemter
	 * @param mRot : rotation matrix
	 * @return 
	 */
	public VecteurCTOM rotate (Rotation mRot) {
		Matrix afterRotate = super.rotate(mRot);
		double x = afterRotate.get(0, 0);
		double y = afterRotate.get(1, 0);									
		double z = afterRotate.get(2, 0);
		return new VecteurCTOM(x,y,z);

	}
	
}