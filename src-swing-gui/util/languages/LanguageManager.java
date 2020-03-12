package util.languages;

import java.util.Locale;

import util.MyLocale;
import util.MyResourceBundle;



public class LanguageManager {
	
	private Locale locale;
    IResource resource;
	
	private static LanguageManager instance;

	public static LanguageManager getInstance() {
		if (LanguageManager.instance == null) {

			LanguageManager.instance = new LanguageManager();
		}

		return LanguageManager.instance;
	}
	
	public Locale getLocale() {
		return locale;
	}

	
	public void setResource(IResource resource) {
		this.resource = resource;
		this.resource.initResources();
	}
	
	public MyResourceBundle getResource(String name) {
		return this.resource.get(name, this.locale);
	}
	
	
	private  LanguageManager() {
		this.locale =  MyLocale.getDefault();  // Locale.getDefault();
	}


	public void setLanguage(Locale locale)  {
		this.locale = locale;
		
	}


	
}
