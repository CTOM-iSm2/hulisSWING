package org.ctom.hulis.huckel.exception;

public class UnavailableSOMException extends HulisException {

	public UnavailableSOMException(int iSOM, int size) {
		super("Molecular Spin Orbital " + iSOM + " unavailable : SOM size = "
				+ size + "\n");
	}

}
