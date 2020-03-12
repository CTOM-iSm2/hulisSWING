/**
 * 
 */
package org.ctom.util.maths;

import Jama.Matrix;



/**
 * @author macbook
 *
 */
public class Rotation extends Matrix{
	
	/**
	 * 
	 * @param vect : axis of rotation (normalized or not)
	 * @param angle : angle of rotation in radian
	 * @return 3D rotation matrix
	 */
	
	public static Rotation getRotation3D(VecteurCTOM vect, double angle) {
		VecteurCTOM v = vect.normalize();
		
		double cos = Math.cos(angle);
		double sin = Math.sin(angle);
		Rotation mRotate = new Rotation(3,3, v, angle);
		
		mRotate.set(0, 0, v.getX() * v.getX() + (1- (v.getX() *v.getX()) ) * cos );
		mRotate.set(0, 1, v.getX() *v.getY() * (1- cos) - v.getZ() * sin );
		mRotate.set(0, 2, v.getX() * v.getZ() * (1-cos)  + v.getY() * sin);
	
		mRotate.set(1, 0, v.getX() * v.getY() * (1-cos) + v.getZ() *sin);
		mRotate.set(1, 1, v.getY() * v.getY() +  (1 - (v.getY() * v.getY()) ) * cos);
		mRotate.set(1, 2, v.getY() * v.getZ() * (1-cos)  - v.getX() * sin);
		
		mRotate.set(2, 0, v.getX() * v.getZ() * (1 -cos) - v.getY() *sin) ;
		mRotate.set(2, 1, v.getY() * v.getZ() * (1-cos) + v.getX() * sin);
		mRotate.set(2, 2, v.getZ() * v.getZ() + (1- (v.getZ() * v.getZ()))*cos);
		
		return mRotate;

	}
	
	/**
	 * Rotate 3D around X axis
	 * @param angle
	 * @return 3D rotation matrix
	 */
	public static Rotation getRotationX(double angle) {
		 /* 1  0    0
		  * 0 cos -sin
		  * 0 sin cos
		  * */
	
		return getRotation3D(VecteurCTOM.V_X_AXIS, angle);

	}
	
	
	/**
	 * Rotate 3D around Y axis
	 * @param angle
	 * @return 3D rotation matrix
	 */
	public static Rotation getRotationY(double angle) {
		/*  cos  0  sin
		 *    0  1  0
		 *  -sin 0  cos  
		 */
		
		return getRotation3D(VecteurCTOM.V_Y_AXIS, angle);


	}

	
	/**
	 * Rotate 3D around Z axis
	 * @param angle
	 * @return 3D rotation matrix
	 */
	public static Rotation getRotationZ(double angle) {
		/* cos  -sin  0
		 * sin   cos  0
		 *   0    0   1
 		 */
		return getRotation3D(VecteurCTOM.V_Z_AXIS, angle);



	}


	private VecteurCTOM axis;
	private double angle;
	
	
	/**
	 * @param nbLine
	 * @param nbColumn
	 * @param v : axis of rotation (normalized vector)
	 * @param angle : angle of rotation (in radians)
	 */
	protected Rotation(int nbLine, int nbColumn, VecteurCTOM axis, double angle){
		super (nbLine, nbColumn);
		this.axis = axis;
		this.angle = angle;
	}

	/**
	 * @return the axis of rotation (normalized vector)
	 */
	public VecteurCTOM getAxis() {
		return axis;
	}

	

	/**
	 * @return the angle of rotation in radians
	 */
	public double getAngle() {
		return angle;
	}

	
	
	
}
