package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class MyResourceBundle {
	protected static HashMap<String, MyResourceBundle> resourcesBundlebyname = new HashMap<String, MyResourceBundle>();

	public static void addBundle(String basename, MyResourceBundle bundle) /*
																			 * throws
																			 * Exception
																			 */{
		// if (resourcesBundlebyname.containsKey(basename))
		// throw new Exception ("this basename already exists : " + basename);

		MyResourceBundle.resourcesBundlebyname.put(basename, bundle);
	}

	public static MyResourceBundle getBundle(String basename, Locale locale) {

		if (MyResourceBundle.resourcesBundlebyname.isEmpty())
			return null;
		String langue = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();
		String key = basename + "_" + langue + "_" + country + "_" + variant;

		if (MyResourceBundle.resourcesBundlebyname.containsKey(key))
			return MyResourceBundle.resourcesBundlebyname.get(key);
		else {
			key = basename + "_" + langue + "_" + country;
			if (MyResourceBundle.resourcesBundlebyname.containsKey(key))
				return MyResourceBundle.resourcesBundlebyname.get(key);
			else {
				key = basename + "_" + langue;
				if (MyResourceBundle.resourcesBundlebyname.containsKey(key))
					return MyResourceBundle.resourcesBundlebyname.get(key);
				else {
					key = basename ;
					if (MyResourceBundle.resourcesBundlebyname.containsKey(key))
						return MyResourceBundle.resourcesBundlebyname.get(key);
					else
						return null;
				}

			}
		}
	}

	public static void removeBundle(String basename) {
		if (MyResourceBundle.resourcesBundlebyname.containsKey(basename)) {
			MyResourceBundle bundle = MyResourceBundle.resourcesBundlebyname
					.get(basename);
			if (bundle.resourcesBundle != null) {
				Iterator<String> it = bundle.resourcesBundle.keySet()
						.iterator();
				// for (Entry <String, Object> entry :
				// bundle.resourcesBundle.entrySet())
				while (it.hasNext()) {
					it.next();
					it.remove();
				}
				bundle.resourcesBundle = null;
			}
			MyResourceBundle.resourcesBundlebyname.remove(basename);
		}
	}

	protected HashMap<String, Object> resourcesBundle;

	public MyResourceBundle() {
		this.resourcesBundle = new HashMap<String, Object>();
	}

	public void add(String key, Object value) /* throws Exception */{
		// if (resourcesBundle.containsKey(key))
		// throw new Exception ("this key already exists : " + key );
		this.resourcesBundle.put(key, value);
	}

	
	
	public Object getObject(String key) {
		if (!this.resourcesBundle.containsKey(key))
			return null;
		Object obj = this.resourcesBundle.get(key);
		;
		return obj;
	}

	public String getString(String key) {
		if (!this.resourcesBundle.containsKey(key))
			return null;
		Object obj = this.resourcesBundle.get(key);
		if (!(obj instanceof String))
			return null;
		return (String) obj;
	}


	
	public void remove(String key) {
		if (this.resourcesBundle.containsKey(key))
			this.resourcesBundle.remove(key);
	}
}
