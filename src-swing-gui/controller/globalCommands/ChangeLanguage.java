package controller.globalCommands;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.Language;
import util.languages.LanguageManager;
import views.FrameApp;
import views.FrameResult;
import views.prefs.PreferencesGUI;

public class ChangeLanguage implements ActionListener, ICommand {
	FrameApp app;
	Language language;
	
	public ChangeLanguage(FrameApp app, Language language) {
		this.app = app;
		this.language = language;
	}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "change language");
		
		Locale locale = language.getLocale();
		LanguageManager.getInstance().setLanguage(language.getLocale());
		PreferencesGUI.getInstance().setLanguage(language.getLocale());
		
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("JOptionPane");

		UIManager.put("OptionPane.yesButtonText", rb2.getString("Yes"));
		UIManager.put("OptionPane.noButtonText", rb2.getString("No"));
		UIManager.put("OptionPane.cancelButtonText", rb2.getString("Cancel"));
		
		
		// On change la locale par defaut du LookAndFeel :	
		UIManager.getDefaults().setDefaultLocale(locale);
		// On change la locale par defaut des nouveaux composants :
		JComponent.setDefaultLocale(locale);
		/*
		 * UIManager.put("FileChooser.saveButtonText","Enregistrer");
		 * UIManager.put("FileChooser.openButtonText","Ouvrir");
		 * UIManager.put("FileChooser.cancelButtonText","Annuler");
		 * UIManager.put("FileChooser.updateButtonText","Actualiser");
		 * UIManager.put("FileChooser.helpButtonText","Aide");
		 * UIManager.put("FileChooser.saveButtonToolTipText","Enregistre le fichier"); Format type de fichier
		 */
		
		for (Window  r : JFrame.getWindows()) {
			if (r instanceof FrameResult) 
				((FrameResult) r).updateLanguage();
		}
		// met a jour le language dnas les composantes de l'UI
		this.app.menuBar.languageMenu.setIcon(language.getFlag());
		this.app.menuBar.updateLanguage();
		this.app.toolBarHuckel.updateLanguage();
		this.app.toolBarMesomery.updateLanguage();
		this.app.getCurrentMesomeryView().updateLanguage();
		this.app.statusBar.updateLanguage();
		this.app.updateLanguage();
	
		// notifie dans barre detat qu'on a change la langue
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("FrameApp");
		this.app.statusBar.setTxtState(bundle.getString("kchangelang"));

		
		ActionCommandRegistry.getInstance().updateLanguage();

		

	}

	public String getActionCommand() {
		return IGlobalCommands.ABOUT;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return "";
	}
}
