package org.ctom.hulis.huckel;

/*import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;*/

public class ObjectLaTeX {

	/*public static final String BEGIN_UML_CLASS="\\umlClass{";
	public static final String END_UML_CLASS="}";
	public static final String SEP_NAME_DEF_CLASS="}{%\n";
	public static final String CHAMPS="Champs :";
	public static final String CONSTRUCTEURS="Constructeurs :";
	public static final String METHODES="Methodes :";
	public static final String HLINE_N="\\hline\n";
	public static final String EOL_N="\\\\\n";

	public static final File TeXDir=new File("/Users/yannick/latex/hulis/");

	private String getLaTeXName() {
		return "";
//		return "[name="+this.getClass().getName()+"]";
	}*/

	public String toLaTeX() {
/*		String str = getLaTeXName();
		Class<?> c = this.getClass();
		str+=BEGIN_UML_CLASS+c.getName()+SEP_NAME_DEF_CLASS;
		Method[] methodes = c.getDeclaredMethods();
		Constructor<?>[] constructeurs = c.getConstructors();
		Field[] champs = c.getDeclaredFields();
		str+=CHAMPS;
		str+=EOL_N;
		for (Field champ : champs) {
			if ( ! Modifier.isPrivate(champ.getModifiers())) {
				str+=champ.getType().getSimpleName()+" "+champ.getName()+EOL_N;
			}
//			str+=champ+EOL_N;
		}
		str+=HLINE_N;
		str+=CONSTRUCTEURS;
		str+=EOL_N;
		for (Constructor<?> constructor : constructeurs) {
			str+=constructor+EOL_N;
		}
		str+=HLINE_N;
		str+=METHODES;
		str+=EOL_N;
		for (Method m : methodes) {
			if ( ! Modifier.isPrivate(m.getModifiers())) {
				str+=m.getName()+EOL_N;
			}
		}
		str+="}";
		if (TeXDir.canWrite()) {
			File file=new File(TeXDir+"/"+this.getClass().getName()+".tex");
			FileWriter fw;
			try {
				fw = new FileWriter(file);
				fw.write(str);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return str;*/
		return "";
	}

}
