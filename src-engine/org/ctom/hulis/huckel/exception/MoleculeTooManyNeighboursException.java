package org.ctom.hulis.huckel.exception;

public class MoleculeTooManyNeighboursException extends Exception {
	public MoleculeTooManyNeighboursException(String mess) {
		super(mess); 
	}

	public MoleculeTooManyNeighboursException(Exception e) {
		// TODO Auto-generated constructor stub
		super(e);
	}
}
