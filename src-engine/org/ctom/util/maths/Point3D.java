package org.ctom.util.maths;

import Jama.Matrix;

public class Point3D extends Matrix implements Cloneable {
	
	/**
	 * Create a point with 3D coordinates
	 * @param x
	 * @param y
	 * @param z
	 */
	public Point3D(double x, double y, double z) {
		this();
		this.set(0, 0, x);
		this.set(1, 0, y);
		this.set(2, 0, z);
	}

	public Point3D() {
		super(3,1);
	}
	/**
	 * constructor by copy
	 * @param p
	 */
	public Point3D(Point3D p) {
		this();
		this.setX(p.getX());
		this.setY(p.getY());
		this.setZ(p.getZ());
	}
	
	
	/**
	 * 
	 */
	public boolean isNull() {
		return Double.isNaN(this.getX()) || Double.isNaN(this.getY())  || Double.isNaN(this.getZ()) ;
	}
	

	
	@Override
	public boolean equals(Object o){
		
		if (!(o instanceof Point3D))
			return false;
			
		Point3D p = (Point3D) o;
	
		if ((p.getX() == this.getX()) && (p.getY() == this.getY()) && (p.getZ() == this.getZ()))
			return true;
	
		return false;
	}

	
	@Override
	public Object clone() {
		return new Point3D(this);
	}

	private volatile int hashCode = 0;
	

	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code +  new Double (this.getX()).hashCode();
            code = multiplier * code +  new Double (this.getY()).hashCode();
            code = multiplier * code +  new Double (this.getZ()).hashCode();
                       hashCode = code;
        }
        return hashCode;
	
	}
	
	/**
	 * 
	 * @return
	 */
	public double getX(){
		return this.get(0, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	public double getY() {
		return this.get(1, 0);
	}
	
	/**
	 * 
	 * @return
	 */
	public double getZ() {
		return this.get(2, 0);
	}
	
	/**
	 * 
	 * @param x
	 */
	public void setX(double x) {
		this.set(0,0,x);
	}
	
	/**
	 * 
	 * @param y
	 */
	public void setY(double y){
		this.set(1,0,y);
	}
	
	/**
	 * 
	 * @param z
	 */
	public void setZ(double z){
		this.set(2,0,z);
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public double getDistance(Point3D other) {
		return Math.sqrt( (this.getX() - other.getX()) * (this.getX() - other.getX())  +  (this.getY() - other.getY()) * (this.getY() - other.getY()) + (this.getZ() - other.getZ()) * (this.getZ() - other.getZ()) );

	}
	
	
	/**
	 * returns a new point3D such that
	 * 
	 * C = A + B
	 * 
	 * @param other point
	 * @return	
	  */
	public Point3D plus(Point3D that){
		Point3D r = new Point3D(this);
		r.plusEquals(that);
		return r;
	}
	
	/**
	 * adds B to A and returns A:
	 * 
	 * A = A + B
	 * 
	 * @param other point
	 * @return
	 */
	public Point3D plusEquals(Point3D that){
		this.setX(this.getX() + that.getX());
		this.setY(this.getY() + that.getY());
		this.setZ(this.getZ() + that.getZ());
		return this;
	}
	
	/**
	 * return a new point after a translation of this point by a vector v
	 * @param v
	 * @return
	 */
	public Point3D translate (VecteurCTOM v) {
		Point3D p = new Point3D(this);
		return p.translateEquals(v);
	}
	
	

	/**
	 * this method translates this point by a vector v
	 * @param v
	 * @return
	 */
	public Point3D translateEquals (VecteurCTOM v) {
		this.plusEquals(v);
		return this;
	}
	
	/**
	 * return a new point after a rotation of this point by a rotation mRot
	 * @param mRotate
	 * @return
	 */
	public Point3D rotate (Rotation mRot) {				
		Point3D p = new Point3D(this);
		return p.rotateEquals(mRot);
	}
	
	
	/**
	 * This method rotates this point by a rotation mRot
	 * @param mRotate
	 * @return
	 */
	public Point3D rotateEquals (Rotation mRot) {
		Matrix mImage = mRot.times(this);
		this.setX(mImage.get(0, 0));
		this.setY(mImage.get(1, 0));									
		this.setZ(mImage.get(2, 0));
		return this;

	}
	
	/**
	 * This function return a new point whose coordinates are those of this point rotated around another point by the rotation matrix
	 * @param around : the point to rotate around
	 * @param mRot : rotation matrix
	 * @return
	 */
	public Point3D rotate(Point3D around, Rotation mRot) {
		/*
	 	Source : http://discussions.tomsguide.fr/forum/id-660682/rotation-point-rapport-point-qqonque.html 
		Je peux te donner la formule :
		soit un rep�re (O,x,y) 
		centre de rotation C(a,b) 
		point M(x,y) � faire tourner
		alpha = angle de rotation
		M'(x',y') image de M par rotation(C,alpha) dans le rep�re (O,x,y)
		
		alors CM' = R*CM (o� CM et CM' sont des vecteurs)
		o� R est la matrice de rotation d'angle alpha, autrement dit : 
		CM=(x-a;y-b)
		CM'=(x'-a,y'-b)
		
		R= 
		cos(alpha) sin(alpha)
		-sin(alpha) cos(alpha)
		
		ce qui donne : 
		(x';y') = R*(x-a,y-b) + (a,b) o� les couples sont des vecteurs donc � representer en tant que matrice 2,1
		*/
		Point3D p = new Point3D(this);
		return p.rotateEquals(around, mRot);
	}
	
	
	
	/**
	 * This method rotates around another point by the rotation matrix
	 * @param around : the point to rotate around
	 * @param mRot : rotation matrix
	 * @return
	 */
	public Point3D rotateEquals(Point3D around, Rotation mRot) {
		/*
	 	Source : http://discussions.tomsguide.fr/forum/id-660682/rotation-point-rapport-point-qqonque.html 
		Je peux te donner la formule :
		soit un rep�re (O,x,y) 
		centre de rotation C(a,b) 
		point M(x,y) � faire tourner
		alpha = angle de rotation
		M'(x',y') image de M par rotation(C,alpha) dans le rep�re (O,x,y)
		
		alors CM' = R*CM (o� CM et CM' sont des vecteurs)
		o� R est la matrice de rotation d'angle alpha, autrement dit : 
		CM=(x-a;y-b)
		CM'=(x'-a,y'-b)
		
		R= 
		cos(alpha) sin(alpha)
		-sin(alpha) cos(alpha)
		
		ce qui donne : 
		(x';y') = R*(x-a,y-b) + (a,b) o� les couples sont des vecteurs donc � representer en tant que matrice 2,1
		*/
		VecteurCTOM vTranslation = new VecteurCTOM (around, this);
		Matrix mImage = mRot.times(vTranslation).plus(around);
		this.setX(mImage.get(0, 0));
		this.setY(mImage.get(1, 0));									
		this.setZ(mImage.get(2, 0));
		return this;
		
	}
	
	
	/**
	 * This function return a new point P' whose coordinates are those of this point rotated around another 
	 * point C by the rotation matrix.
	 * The new point P' is put on the axis CP' to the desired distance of C after the rotation
	 * @param around : the point C to rotate around
	 * @param mRot : rotation matrix
	 * @param distance : the distance of the new point P' to C on the axis CP' after the rotation
	 * @return
	 */
	public Point3D rotateAndRenorm(Point3D around, Rotation mRot, double distance) {
		Point3D p = new Point3D (this);
		return p.rotateAndRenormEquals(around, mRot, distance);
	}
	
	
	
	/**
	 * This function moves this points whose the new coordinates P'(x',y',z') are those of this point rotated around another 
	 * point C by the rotation matrix.
	 * The new point P'(x',y',z') is put on the axis CP' to the desired distance of C after the rotation
	 * @param around : the point C to rotate around
	 * @param mRot : rotation matrix
	 * @param distance : the distance of the new point P' to C on the axis CP' after the rotation
	 * @return
	 */
	public Point3D rotateAndRenormEquals(Point3D around, Rotation mRot, double distance) {
		Point3D pImage = rotate (around, mRot);
		VecteurCTOM v = new VecteurCTOM (around, pImage);
		v.renormEquals(distance);
		pImage = around.translate(v);
		this.setX(pImage.getX());
		this.setY(pImage.getY());
		this.setZ(pImage.getZ());
		return this;
	}
	
	
	@Override
	public String toString(){
		return "(" + this.getX() +  ", " +  this.getY() + ", " + this.getZ() + ")";
	}

}
