package org.ctom.hulis.files.xyz;

public class XYZErreurLigne2 extends Exception {

	static String msg = "The second line must contain a title";

	public XYZErreurLigne2() {
		super(XYZErreurLigne2.msg);
	}
}
