package util;

/**
 * <p>
 * Titre : SuiviDeLien
 * </p>
 * <p>
 * Description : Classe permettant de r�cup�rer les liens hypertextes des
 * fichiers HTML
 * </p>
 * <p>
 * Copyright : Copyright (c) 2004
 * </p>
 * <p>
 * Soci�t� : BakaOne
 * </p>
 * 
 * @author Indiana_jules
 * @version 1.0
 */

// Importation des packages n�cessaires
import java.awt.Cursor;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import util.io.SwingIO;
import views.Explorer;

public class FollowLink implements HyperlinkListener {
	private static final  Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	public static void OpenExplorer(URL url) throws IOException {

		try {
			
			String os = System.getProperty("os.name");
			Runtime r = Runtime.getRuntime();
			try {
				// ouvre le lien dans le navigateur suivant l'OS
				if (os.endsWith("NT") || os.endsWith("2000")
						|| os.endsWith("XP")) {
					r.exec("cmd /c start " + url);
				} else if (os.startsWith("Mac OS")) {
					System.out.println(os);
					r.exec("open " + url);
				} else {
					System.out.println(os);
					r.exec("start " + url);
				}
			} catch (Exception e1) {
				try {
					SwingIO.warning("FollowLink", "OpenExplorer", e1
							.getMessage(), e1);
					new Explorer(url);

				} catch (IOException ex) {
					SwingIO.warning("FollowLink", "OpenExplorer", ex
							.getMessage(), ex);
					throw ex;
				}

			}
			// panneau.setPage(evt.getURL());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Variables globales
	private JEditorPane panneau;

	/** Constructeur par d�faut */
	public FollowLink() {
	}

	/**
	 * Constructeur
	 * 
	 * @param panneau
	 *            Indiquation du JEditorPane de destination
	 */
	public FollowLink(JEditorPane panneau) {
		this.panneau = panneau;
	}

	/**
	 * M�thode retournant le JEditorPane
	 * 
	 * @return Retour du JEditorPane destination
	 */
	public JEditorPane getPanneau() {
		return this.panneau;
	}

	/**
	 * m�thode permettant d'agir lors d'un clique sur un lien
	 * 
	 * @param evt
	 *            Gestionnaire d'�v�nement
	 */
	public void hyperlinkUpdate(HyperlinkEvent evt) {

		/*
		 * permet de changer le pointeur de la souris lors du survol d'un lien
		 * hypertext
		 */
		if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
			this.panneau.setCursor(HAND_CURSOR);
		}
		/*
		 * permet de changer le pointeur de la souris en pointeur normal apr�s
		 * le survol d'un lien hypertext
		 */
		else {

			this.panneau.setCursor(DEFAULT_CURSOR);
		}

		// si on clique sur un lien hypertexte, on l'affiche soit dans le
		// JEditorPane
		if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			try {
				if (evt.getDescription().startsWith("class:")) {
					String className = evt.getDescription().substring(6);
					Class.forName(className).newInstance();
				}
				else
					FollowLink.OpenExplorer(evt.getURL());
			} catch (IOException e) {
				SwingIO.warning(this.getClass().getName(), "hyperlinkUpdate", e
						.getMessage(), e);

			} catch (InstantiationException e) {
				SwingIO.warning(this.getClass().getName(), "hyperlinkUpdate", e
						.getMessage(), e);
			} catch (IllegalAccessException e) {
				SwingIO.warning(this.getClass().getName(), "hyperlinkUpdate", e
						.getMessage(), e);
			} catch (ClassNotFoundException e) {
				SwingIO.warning(this.getClass().getName(), "hyperlinkUpdate", e
						.getMessage(), e);
			}
		}
	}

	/**
	 * M�thode permettant de d�finir le JEditorPane destination
	 * 
	 * @param panneau
	 *            JEditorPane destination
	 */
	public void setPanneau(JEditorPane panneau) {
		this.panneau = panneau;
	}
}
