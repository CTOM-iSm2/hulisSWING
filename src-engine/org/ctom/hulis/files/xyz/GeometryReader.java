package org.ctom.hulis.files.xyz;

import org.ctom.hulis.files.IGeometryProvider;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.util.geometry.Geometry;

public abstract class GeometryReader implements IGeometryProvider {

	protected void setX(Atom a, double value) {
		a.setXAndXGeom3D(value);
	}

	protected void setY(Atom a, double value) {
		a.setYAndYGeom3D(value);
	}

	protected void setZ(Atom a, double value) {
		a.setZAndZGeom3D(value);
	}

	protected void setCoords(Atom a, double x, double y, double z) {
		this.setX(a, x);
		this.setY(a, y);
		this.setZ(a, z);
	}

	
	/***
	 * read all the datas from the provider into the geometry specified in the parameter
	 * @param geometry
	 * @throws ReadxyzException 
	 */
	abstract public void readAll(Geometry geometry) throws ReadxyzException ;
}
