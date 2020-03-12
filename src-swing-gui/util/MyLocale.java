package util;

import java.util.Locale;

public class MyLocale {
	public static Locale getDefault() {
		return new MyLocale(Locale.getDefault().getLanguage(), Locale
				.getDefault().getCountry()).getLocale();
	}

	public static String getOSToVariant() {
		String os = System.getProperty("os.name");
		
		if (os.endsWith("NT") || os.endsWith("2000") || os.endsWith("XP"))
			return "WIN";
		else if (os.startsWith("Windows"))
			return "WIN";
		else if (os.startsWith("Mac OS"))
			return "MAC";
		else
			return "LINUX";
	}
	
	public static String getOSName() {
		return  System.getProperty("os.name");
	}


	
	public static String getJavaVersion () {
		return System.getProperty("java.version");
	}

	Locale locale;
	
	public  MyLocale (String language, String country) {
		this.locale = new Locale (language, country, getOSToVariant() );
	}

	public String getLanguage() {
		return  locale.getLanguage();
	}
	
	public String getCountry() {
		return  locale.getCountry();
	}
	
	public String getVariant (){
		return  locale.getVariant();
	}

	public Locale getLocale (){
		return  locale;
	}
	
}
