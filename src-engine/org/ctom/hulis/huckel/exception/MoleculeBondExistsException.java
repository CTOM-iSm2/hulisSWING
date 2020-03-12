package org.ctom.hulis.huckel.exception;

public class MoleculeBondExistsException extends Exception {
	public MoleculeBondExistsException(String mess) {
		super(mess); 
	}

	public MoleculeBondExistsException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
}
