package util;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JOptionPane;


public class MyJOptionPane {
	static boolean defaultEnabledModal = true;
	
	public static void setDefaultEnabledModal(boolean modal) {
		defaultEnabledModal = modal;
	}

	
	public static void showMessageDialog(Component parentComponent, 
			Object message, String title, int messageType) {
		if(defaultEnabledModal)
			JOptionPane.showMessageDialog(parentComponent, message,
					title, messageType);
		else
			System.out.println ("disabled dialog box message : " + message);
	}

	public static Object showInputDialog(Component parentComponent, Object message, String title, int messageType, Icon img, Object[] lst, Object init) {
		if(defaultEnabledModal)
			return JOptionPane.showInputDialog(parentComponent, message, title, messageType, img, lst, init);
		else {
			System.out.println ("disabled input box message : " + message );
			System.out.println ("returned value : " + init );
			if (init instanceof Integer)
				return String.valueOf(init);
			return init;
		}
	}
	
	
}
