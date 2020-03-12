package org.ctom.hulis.files.xyz;

public class XYZErreurLigne3 extends Exception {

	static String msg = "After the 2nd line, lines must conform to the format:\n"
			+ "AtomSymbol CoordX CoordY CoordZ LinkedTo1 LinkedTo2\n"
			+ "..."
			+ "He         0.3    2      0.004  1         4" + "...";

	XYZErreurLigne3() {
		super(XYZErreurLigne3.msg);
	}
}
