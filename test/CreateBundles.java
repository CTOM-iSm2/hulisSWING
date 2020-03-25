import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;


public class CreateBundles {
	
	public void out(PrintWriter stdout, String s) {
		stdout.println(s);
	}
	
	public CreateBundles() {
		ResourceBundle stats = ResourceBundle.getBundle("files/About", Locale.getDefault());
		System.out.println(stats.getString("countrydate"));
		
	/*	PrintWriter stdout = new PrintWriter(
			    new OutputStreamWriter(System.out, StandardCharsets.UTF_8),
			    true);
		
		
		InitResources ress = new InitResources();
		ress.initResources();
		HashMap<String, MyResourceBundle> ressources = MyResourceBundle.resourcesBundlebyname;
		for (Entry<String, MyResourceBundle> ressource : ressources.entrySet() ) {
			String name = ressource.getKey();
			out(stdout, name);
			PrintWriter fileout = null;
			try {
				String filepath = System.getProperty("user.dir") + "/src/files/"  + name + ".properties";
				fileout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),"UTF-8")), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		    HashMap<String, Object>  listeMots =  ressource.getValue().resourcesBundle;
		    for (Entry<String, Object> cleEtMot : listeMots.entrySet() ) {
		    	String cle = cleEtMot.getKey();
		    	Object mot = cleEtMot.getValue();
		    	if (mot instanceof String) {
		    		String s = cle+" = " + StringEscapeUtils.escapeJava((String) mot);
		    		//out(stdout, s);
		    		out(fileout, s);
		    	}
		    	else
		    		out(stdout, cle+"=EST UN OBJET  !!!!!!!");
		    		
		    }
		}*/
	}
	/**
	 * @param args
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Thread(new Runnable(){

			@Override
			public void run() {
				new  CreateBundles();
				
			}}));
	}
	
}
