package org.ctom.hulis.files.xyz;

public class XYZErreurLigne1 extends Exception {

	static String msg = "The first line of the xyz file must be the number of atoms";

	public XYZErreurLigne1() {
		super(XYZErreurLigne1.msg);
	}
}