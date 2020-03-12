/**
 * 
 */
package util.languages;

import java.util.Locale;

import javax.swing.ImageIcon;



public class Language {  
	
	private Locale locale;
	private String name;
	private ImageIcon flag;
	public Language(String name, Locale locale, String flag) {
		this.name = name;
		this.locale = locale;
		this.flag = new ImageIcon(getClass().getClassLoader().getResource(flag));
		
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof Language)
			return ((Language) o).getLocale().equals(getLocale());
		if (o instanceof Locale)
			return ((Locale) o).equals(getLocale());
		return false;
	}

	public ImageIcon getFlag() {
		return this.flag;
	}

	public Locale getLocale() {
		return this.locale;
	}

	
	@Override
	public String toString() {
		return this.name;
	}
}