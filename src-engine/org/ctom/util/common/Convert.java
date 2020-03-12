/**
 * 
 */
package org.ctom.util.common;

/**
 * @author macbook
 *
 */
public class Convert {

	/**
	 * convert enums values to string array
	 * source : http://www.coderanch.com/t/446722/java/java/Enum-String-array
	 * @param values
	 * @return
	 */
	public static <T extends Enum<T>> String[] enumNameToStringArray(T[] values) {  
	    int i = 0;  
	    String[] result = new String[values.length];  
	    for (T value: values) {  
	        result[i++] = value.name();  
	    }  
	    return result;  
	}  
	  
	
}
