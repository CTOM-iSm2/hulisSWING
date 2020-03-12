package org.ctom.hulis.util;

import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;


public class Arrays {

	
	public static SpinOrbitaleMoleculaire[] arrayCopy(
			SpinOrbitaleMoleculaire[] array) {
		if (array == null) return null;
		SpinOrbitaleMoleculaire[] arraycopy = new SpinOrbitaleMoleculaire[array.length];
		for (int i = 0; i < array.length; i++)
			arraycopy[i] = new SpinOrbitaleMoleculaire(array[i]);
		return arraycopy;
	}

}
