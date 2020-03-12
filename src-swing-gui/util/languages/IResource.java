package util.languages;

import java.util.Locale;

import util.MyResourceBundle;

public interface IResource {
	public void initResources();
	
	public  MyResourceBundle get (String name, Locale locale);

	public void removeResources();
	
}
