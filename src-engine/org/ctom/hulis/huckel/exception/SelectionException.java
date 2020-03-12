/**
 * 
 */
package org.ctom.hulis.huckel.exception;

/**
 * @author macbook
 *
 */
public class SelectionException extends Exception {
	public SelectionException(String mess) {
		super(mess); 
	}

	public SelectionException(Exception e) {
		super(e);
	}
}