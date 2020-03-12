package util.languages;


import java.awt.ComponentOrientation;
import java.awt.Font;
import java.util.Locale;

import javax.swing.text.StyleConstants;

import util.MyResourceBundle;

public class InitResources implements IResource {

	/***************************************************************************
	 * 
	 * Implemented languages
	 * 
	 **************************************************************************/
	static Language francais = new Language("Fran\u00E7ais", Locale.FRENCH, "img/francais.png");
	static Language english = new Language("English", Locale.ENGLISH,"img/uk.png");
	static Language arabian = new Language("\u0639\u0631\u0628\u064A\u0629", new Locale("ar", ""), "img/arabia.png");
	static Language italian = new Language("Italiano", Locale.ITALIAN, "img/italy.png");


	
		
	public static Language[] languages = new Language[] {
		 arabian,  english, francais, italian
	 };
	

	
	/***************************************************************************
	 * 
	 * Arabian language
	 * 
	 **************************************************************************/

	public void initArabian() {
		
		//Threads
		MyResourceBundle bundle = new MyResourceBundle();
		bundle.add("stopComputeWeights", "The computation of the weights of Lewis structures is being stopped...");
		bundle.add("stopAutogen", "The generation of the Lewis structures is being stopped...");
		MyResourceBundle.addBundle("Threads_ar", bundle);

		// ToolBarMesomery
	    bundle = new MyResourceBundle();
		bundle.add("compute", "compute");
		bundle
		.add(
				"kmesomery",
				"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">\u0644\u0648\u064A\u0633<br><span class=\"Style2\">\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629</span></p></body></html>");
		bundle.add("kcreate1", "\u0625\u062F\u0631\u0627\u062C");
		bundle.add("kgenerateall",
				"\u0625\u062F\u0631\u0627\u062C\u0020\u0627\u0644\u0643\u0644");
		bundle.add("kcouple", "\u0632\u0648\u062C\u0020");
		bundle.add ("kbloc", "Block");
		bundle.add ("kendbloc", "End block");
		bundle
				.add("kchangebonds",
						"\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A\u0020 \u25cf");
		bundle.add("kresults", "\u0646\u062A\u0627\u0626\u062C");
		bundle.add("kerase1",
				"\u0625\u0632\u0627\u0644\u0629\u0020\u0020\u0031");
		bundle
				.add(
						"kerasem",
						"\u0625\u0632\u0627\u0644\u0629\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629");
		bundle.add("kenergy", "E");
		bundle.add("phi", "\u03C8");
		bundle
				.add(
						"kCI",
						"\u062A\u0641\u0627\u0639\u0644\u0020\u0645\u0627\u0020\u0628\u064A\u0646\u0020\u0627\u0644\u0628\u0646\u064A\u0627\u062A");
		bundle.add("kincomplete", "\u0646\u0627\u0642\u0635");
		bundle.add("exist", "\u0645\u0632\u062F\u0648\u062C\u0629");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 13)); // taille des caracteres sur les boutons
		MyResourceBundle.addBundle("ToolBarMesomery_ar", bundle);

		// ToolBarHuckel

		bundle = new MyResourceBundle();
		bundle
		.add(
				"khuckel",
				"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">\u0647\u0648\u0643\u0644<br><span class=\"Style2\">&nbsp;</style></p></body></html>");
		bundle.add("kbuild", "\u0625\u062F\u0631\u0627\u062C");
		bundle.add("kchange", "\uFEAE\uFEF4\uFEF4\uFED0\uFE97");
		bundle.add("kdelete", "\u0625\u0632\u0627\u0644\u0629");
		bundle.add("kmove", "\u0646\u0642\u0644");
		bundle.add("krotate", "\u062A\u062F\u0648\u064A\u0631");
		bundle.add("kimgmove", "img/move.png");
		bundle.add("kimgrotate", "img/rotate.png");
		bundle.add("kimgcenter", "img/center.png");
		bundle.add("koptimize", "Opt.");
		bundle.add("krenum", "\u062A\u0631\u062A\u064A\u0628");
		bundle
				.add(
						"kinfos",
						"\uFEE3\uFE98\uFED0\uFEF4\uFEAE\u0627\u062A");
		bundle
		.add(
				"kcharges",
				"\uFEE6\uFEA4\uFEB7");//\u0627\u0644\u0634\u062D\u0646\u0629
		bundle.add("knum", "\u062A\u0631\u0642\u064A\u0645");
		bundle.add("kresults", "\u0646\u062A\u0627\u0626\u062C");
		bundle.add("keraseall",
				"\u0625\u0632\u0627\u0644\u0629\u0020\u0627\u0644\u0643\u0644");
		bundle
				.add(
						"kquit",
						"\u0645\u063A\u0627\u062F\u0631\u0629\u0020\u0627\u0644\u0628\u0631\u0646\u0627\u0645\u062C");
		bundle
				.add(
						"ktotalcharge",
						"<html>{0} = \u0627\uFEE0\uFEB8\uFEA4\uFEE8\uFE94\u0020\u0627\uFEE0\uFEDC\uFEE0\uFEF4\uFE94</html>"); // \u0627\uFEE0\uFEB8\uFEA4\uFEE8\uFE94\u0020\u0627\uFEE0\uFEDC\uFEE0\uFEF4\uFE94
		bundle.add("kenergy", "E");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 13) ); // taille des caracteres des boutons
		MyResourceBundle.addBundle("ToolBarHuckel_ar", bundle);

		// StructureView
		bundle = new MyResourceBundle();
		
		bundle.add("kwarning", "\u062A\u062D\u062F\u064A\u0631");
		bundle
		.add(
				"kWarNegCharge",
				"\u0628\u0627\u0631\u0627\u0645\u062A\u0631\u0627\u062A\u0020\u0637\u0631\u064A\u0642\u0629\u0020\u0647\u0648\u0643\u0644\u0020\u0627\u0644\u062E\u0627\u0635\u0629\u0020\u0628\u0627\u0644\u0623\u064A\u0648\u0646\u0627\u062A\u0020\u0627\u0644\u0633\u0627\u0644\u0628\u0629\u0020\u0644\u064A\u0633\u062A\u0020\u0647\u064A\u0020\u0627\u0644\u0623\u062D\u0633\u0646");

		bundle.add("kverticalspacebetweenlines", 18);
		bundle.add("kendbloc", "Please, terminate the block in {0}");
		bundle.add("kselectbloc", "Select an existing bloc or create a new one");
		
		bundle.add("tooMuchSingleElec", "No more than two single electrons");

		bundle
				.add(
						"knetcharge",
						"\u0627\u0644\u0634\u062D\u0646\u0629\u0020\u0627\u0644\u0643\u0644\u064A\u0629");
		bundle.add("kenergy", "E");
		bundle
				.add(
						"kelecerror",
						"\u062D\u062F\u062F\u0020\u0628\u0646\u064A\u0629\u0020\u0644\u0648\u064A\u0633\u0020\u0628\u062A\u0639\u064A\u064A\u0646\u0020\u0623\u0645\u0627\u0643\u0646");
		bundle
		.add(
				"kelecerrorb",
		"\u062A\u0648\u0627\u062C\u062F\u0020\u0627\u0644\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A\u0020\u0627\u0644\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A\u0020");
			
		
		bundle
				.add(
						"kelecerror1",
						"\u0627\u0644\u0628\u0627\u0642\u064A\u0629\u0020\u0648\u0020\u0627\u0644\u0645\u0645\u0643\u0646\u0020\u0625\u0636\u0627\u0641\u062A\u0647\u0627 {0}");
		bundle
				.add(
						"kelecerror2",
						"\u0627\u0644\u062A\u064A\u0020\u064A\u062C\u0628\u0020\u0625\u0632\u0627\u0644\u062A\u0647\u0627 {0}");
		bundle
				.add(
						"kelecerror4",
						"\u0644\u0642\u062F\u0020\u062A\u0645\u0020\u0625\u062F\u0631\u0627\u062C\u0020\u0647\u0630\u0627");
		bundle
		.add(
				"kelecerror4b",
				        "\u0627\u0644\u0634\u0643\u0644\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A");

		bundle.add("kErrorRedundance",  "Redundance has been detected in the structures");
		bundle.add("methoderror","Cannot compute {0} : {1}");
		bundle.add("font",  new Font("Helvetica", Font.BOLD, 13)); // taille texte erreurs
		MyResourceBundle.addBundle("StructureView_ar", bundle);

		// SkelMouseController
		bundle = new MyResourceBundle();
		bundle
				.add("ktoomanyneighbours2",
						"\uFEFB\u0020\uFEF3\uFEE4\uFEDC\uFEE6\u0020\uFEDF\uFEEC\uFEAC\u0647\u0020\u0627\uFEF9\u0631\uFE97\uFE92\uFE8E\u0637\u0020\uFE91\uFEAC\u0631\u0629\u0020\uFEE3\uFEA0\uFE8E\u0648\u0631\u0629\u0020\u0623\uFE9F\uFEAE\u064D\u0649\u0020");
		bundle
				.add(
						"removeHAtom",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u0625\u0632\u0627\u0644\u0629\u0020\u0630\u0631\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646\u002E");
		bundle
				.add(
						"removeHBond",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u0625\u0632\u0627\u0644\u0629\u0020\u0631\u0627\u0628\u0637\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646\u064A\u0629\u002E");
		bundle
				.add(
						"changeHBond",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062A\u063A\u064A\u064A\u0631\u0020\u0631\u0627\u0628\u0637\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646\u064A\u0629\u002E");
		bundle
				.add(
						"HRadR",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062A\u063A\u064A\u064A\u0631\u0020\u0639\u062F\u062F\u0020\u0627\u0644\u062C\u062F\u0648\u0631\u0020\u0627\u0644\u0645\u062A\u0635\u0644\u0629\u0020\u0628\u0630\u0631\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646\u002E");
		MyResourceBundle.addBundle("SkelMouseController_ar", bundle);

	
		// Preferences
		bundle = new MyResourceBundle();
		bundle.add("title", "Preferences");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Cancel");
		bundle.add("kdecimal", "Digits : ");
		bundle.add("kprecisionMO", "<html>MO, charge, density,<br>overlap, weigths, coefficients :</html>");
		bundle.add("kprecisionEnergy", "Energies \u03B2, hamiltionians : ");
		bundle.add("kMOratio", "Zoom MO : ");
		bundle.add("prefAutoGen", "Automatic generation of Lewis structures :");
		bundle.add("Nsep", "Max charge : ");
		bundle.add("kAtomDisplay", "Atoms representation :");
		bundle.add("kcircle", "Circles");
		bundle.add("ksymbols", "Symbols");
		bundle.add("kautocouple", "Allow bi-radicals");
		bundle.add("kautolongdistancecouple", "AutoGen couple ld");
		bundle.add("kautosep", "AutoGen sep");
		bundle.add("kautolongdistancesep", "AutoGen sep ld");
		bundle.add("ktitle", "Preferences");
		bundle.add("uptodate", "HuLiS is up to date");
		bundle.add("checkVersion", "Automatically check for updates");
		bundle.add("updates", "Updates");
		bundle.add("sendInformation", "<html>HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs.</html>");
		bundle.add("chkSendInformation", "Allow sending of informations to HuLiS");
		bundle.add("privacy", "Privacy");
		MyResourceBundle.addBundle("Preferences_ar", bundle);
		

		// JOptionPane
		bundle = new MyResourceBundle();
		bundle.add("Yes", "\u0646\u0639\u0645");
		bundle.add("No", "\u0644\u0627");
		bundle.add("Cancel", "\u0625\u0644\u063A\u0627\u0621");
		MyResourceBundle.addBundle("JOptionPane_ar", bundle);

		// FrameResultStruct
		bundle = new MyResourceBundle();
		bundle.add("pisymbol", "\u03c0");
		bundle
				.add(
						"resh0",
						"\u0644\u0627\u0020\u062A\u0648\u062C\u062F\u0020\u0623\u064A\u0629\u0020\u0628\u0646\u064A\u0629\u002E\u0020\u0627\u0633\u062A\u0639\u0645\u0644\u0020\u0627\u0644\u0642\u0641\u0644\u0020\u0625\u062D\u062F\u0627\u062B\u0020\u0645\u0646\u0020\u0644\u0648\u062D\u0629\u0020\u0627\u0644\u0645\u0641\u0627\u062A\u064A\u062D\u0020\u0644\u062A\u062D\u062F\u064A\u062F\u0020\u0627\u0644\u0647\u064A\u0643\u0644\u0020\u0627\u0644\u0641\u062D\u0645\u064A\u0020\u0644\u0628\u0646\u064A\u0629\u0020\u0627\u0644\u062C\u0632\u064A\u0621\u0020\u0028\u0636\u063A\u0637\u0020\u0648\u0020\u0627\u0633\u062D\u0628\u0029\u0020\u062B\u0645\u0020\u0627\u0644\u0642\u0641\u0644\u0020\u062A\u063A\u064A\u064A\u0631\u0020\u0644\u062A\u063A\u064A\u064A\u0631\u0020\u0646\u0648\u0639\u0020\u0627\u0644\u0630\u0631\u0629\u0020\u0028\u062E\u0635\u0627\u0626\u0635\u0020\u0627\u0644\u0630\u0631\u0629\u0020\u0648\u0020\u0627\u0644\u0631\u0648\u0627\u0628\u0637");
		bundle
				.add("resh1",
						"\u0646\u0648\u0639\u064A\u0629\u0020\u0627\u0644\u0630\u0631\u0629");
		bundle
				.add("resh2",
						"\u0639\u062F\u062F\u0020\u0627\u0644\u0630\u0631\u0627\u062A : ");
		bundle
				.add(
						"resh2a",
						"\u062F\u0644\u064A\u0644\u0020\u0627\u0644\u0645\u0633\u062A\u062E\u062F\u0645\u064A\u0646 : ");
		bundle
				.add(
						"resh3",
						"\u0639\u062F\u062F\u0020\u0627\u0644\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A : ");
		bundle
				.add(
						"resh4",
						"\u0627\u0644\u0634\u062D\u0646\u0629\u0020\u0627\u0644\u0643\u0644\u064A\u0629 ");
		// paragraphe 1
		bundle
				.add(
						"resh5",
						"\u003A\u0020\uFE8E\uFEE0\uFEE4\u0624\uFE9C\uFEAE\u0020\u0627\uFEE0\uFEEC\uFE8E\uFEE4\uFEE0\uFE98\uFEEE\uFEE8\uFEF2\u0020\u002D\u0049");
		// paragraphe 2
		bundle
				.add(
						"resh6",
						"\u003A\u0020\u0627\uFEE0\uFEE4\uFEAA\uFE8E\uFEAE\uFE8E\u062A\u0020\u002D\u0049\u0049");
		bundle.add("resh7", "\u025Bi :  ");

		// Energie totale
		bundle
				.add(
						"resh8",
						"\u003A\u0020\u0627\uFEE0\uFEC4\uFE8E\uFED8\uFE94\u0020\u0627\uFEE0\uFEDC\uFEE0\uFEF4\uFE94\n");
		// paragraphe 3
		bundle
				.add(
						"resh9",
						"\u003A\u0020\uFEE3\uFEBC\uFED4\uFEEE\uFED4\uFE94\u0020\u0627\uFEE0\uFEDC\uFE9C\uFE8E\uFED4\uFE94\u0020\u0627\uFEE0\u0625\uFEE0\uFEDC\uFE98\uFEAE\uFEEE\uFEE8\uFEF4\uFE94\u0020\u003A\u0020\uFEDB\uFEE4\uFEF4\uFE94\u0020\u0627\uFEE0\u0625\uFEE0\uFEDC\uFE98\uFEAE\uFEEE\uFEE8\uFE8E\u062A\u0020\uFECB\uFEE0\uFEF0\u0020\u0627\uFEE0\uFEAC\uFEAE\uFE8E\u062A\u0020\u0648\u0020\uFEE3\uFE8E\u0020\uFE91\uFEF4\uFEE6\u0020\u0627\uFEE0\uFEAC\uFEAE\uFE8E\uFE98\u002E\u0020\uFEDF\uFE8E\u0020\uFEF3\uFE98\uFEE2\u0020\u0627\uFEB4\uFE98\uFECC\uFEE4\uFE8E\u0644\u0020\u0627\uFEE0\uFEDC\uFE9C\uFE8E\uFED4\uFE94\u0020\u0627\uFEE0\u0625\uFEE0\uFEDC\uFE98\uFEAE\uFEEE\uFEE8\uFEF4\uFE94\u0020\uFEE3\uFE8E\u0020\uFE91\uFEF4\uFEE6\u0020\u0627\uFEE0\uFEAC\uFEAE\uFE8E\u062A\u0020\u0627\uFEE0\uFED0\uFEF4\uFEAE\u0020\uFEE3\uFE98\uFEAE\uFE8E\uFE92\uFEC4\uFE94\u0020\u002D\u0049\u0049\u0049");
		// paragraphe 4
		bundle
				.add(
						"resh10",
						"\u003A\u0020\u0627\uFEE0\uFEB8\uFEA4\uFEE8\uFE94\u0020\u0627\uFEE0\uFEAC\uFEAE\uFEF4\uFE94\u0020\u002D\u0049\u0056");
		bundle
				.add(
						"resh11",
						"\u0627\u0644\u0634\u062D\u0646\u0629\u0020\u0627\u0644\u0643\u0644\u064A\u0629");
		bundle.add("resh12", "ni :");
		bundle
				.add("resh13",
						"\u0627\u0644\u0630\u0631\u0629\u002F\u0627\u0644\u0634\u062D\u0646\u0629");
		bundle
				.add(
						"resh131",
						"<b>\u0627\u0644\u0630\u0631\u0629</b></td><td><b>\u0627\u0644\u0634\u062D\u0646\u0629</b>");
		bundle
				.add(
						"ktitleresultstruct",
						"\u0647\u0644\u064A\u0633\u0020\u003A\u0020\u0646\u062A\u0627\u0626\u062C\u0020\u0637\u0631\u064A\u0642\u0629\u0020\u0647\u0648\u0643\u0644");
		bundle
				.add(
						"kdeloc",
						"\u0634\u0643\u0644\u0020\u063A\u064A\u0631\u0020\u0645\u0645\u0631\u0643\u0632");
		bundle.add("kloc",
				"\u0634\u0643\u0644\u0020\u0645\u0645\u0631\u0643\u0632");
		MyResourceBundle.addBundle("FrameResultStruct_ar", bundle);

		// FrameResultMesomery
		bundle = new MyResourceBundle();
		bundle.add("phitot", "\u03C8tot");
		// nb structures

		
		bundle
				.add(
						"resmesnbgen",
						"\u0639\u062F\u062F\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633:");
		// nb structures active
		bundle
				.add(
						"resmesnbdisp",
						"\u0639\u062F\u062F\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633\u0020\u0627\u0644\u0641\u0627\u0639\u0644\u0629:");
		bundle
				.add(
						"resmes1",
						"\u062D\u0633\u0627\u0628\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629\u0020\u0639\u0646\u0020\u0637\u0631\u064A\u0642\u0020\u062A\u0641\u0627\u0639\u0644\u0020\u0645\u0627\u0020\u0628\u064A\u0646\u0020\u0627\u0644\u0628\u0646\u064A\u0627\u062A");
		bundle
				.add(
						"resmes2",
						" \u0645\u0635\u0641\u0648\u0641\u0629\u0020\u0627\u0644\u0645\u0639\u0627\u0645\u0644\u0627\u062AB\u0627\u0644\u0645\u0633\u062A\u0639\u0645\u0644\u0629");
		bundle
				.add(
						"resmes3",
						"\u0627\u0644\u0645\u0635\u0641\u0648\u0641\u0629\u0020\u0627\u0644\u0623\u0648\u0644\u0649C");
		bundle
				.add("resmes4",
						"\u0627\u0644\u0642\u064A\u0645\u0020\u0627\u0644\u062E\u0627\u0635\u0629An");
		bundle
				.add(
						"resmes5",
						"\u0627\u0644\u0642\u064A\u0645\u0020\u0627\u0644\u0645\u0648\u062C\u0628\u0629\u0020\u0627\u0644\u0648\u062D\u064A\u062F\u0629Bn = -(1 / (An - 1)");
		bundle.add("resmes6",
				"\u0623\u062D\u0633\u0646\u0020\u0642\u064A\u0645Bn");
		// paragraphe 1
		bundle
				.add(
						"resmes7",
						"\u0627\u0644\u0645\u0624\u062B\u0631\u0020\u0627\u0644\u0647\u0627\u0645\u0644\u062A\u0648\u0646\u064A\u0020\u0644\u062A\u0641\u0627\u0639\u0644\u0020\u0645\u0627\u0020\u0628\u064A\u0646\u0020\u0627\u0644\u0628\u0646\u064A\u0627\u062A");
		
		
		bundle.add("hlpOverlap", "    Overlap HLP matrix :" );
		// paragraphe 2
		bundle
				.add(
						"resmes10",
						"\u0627\u0644\u0623\u0634\u0639\u0629\u0020\u0627\u0644\u062E\u0627\u0635\u0629(Ci)\u0020\u0020\u0020\u0020\u0648\u0020\u0648\u0632\u0646\u0647\u0627(Wi)\u0020\u0020\u0020\u0020\u0641\u064A\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629\u0020\u0639\u0646\u0020\u0637\u0631\u064A\u0642\u0020\u0645\u0642\u0627\u0631\u0628\u0629");
		bundle.add("resmes11", "\u03C8tot = C1.\u03C81 + C2.\u03C82 ...");
		bundle.add("knbostring", "3- NBO String for gaussian :");
		bundle
				.add(
						"resmes111",
						"\u03C8<sub>tot</sub> = C<sub>1</sub>.\u03C8<sub>1</sub> + C<sub>2</sub>.\u03C8<sub>2</sub> ...");
		bundle
				.add(
						"nothingmesomery",
						"\u0625\u062D\u062F\u0627\u062B\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633\u0020\u0628\u0627\u0633\u062A\u0639\u0645\u0627\u0644\u0020\u0627\u0644\u0642\u0641\u0644\u064A\u0646\u0020\u0625\u062D\u062F\u0627\u062B\u0020\u0648\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A");
		bundle
				.add(
						"kelecerror",
						":{0}\u0020\u0020\u0020\u0020\u0639\u062F\u062F\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A\u0020\u03c0\u0020\u0020\u0020\u0020\u063A\u064A\u0631\u0020\u0645\u0646\u0627\u0633\u0628\u002C\u0020\u0644\u0642\u062F\u0020\u062A\u0645\u0020\u0648\u0636\u0639\u0020{1}\u0020\u0020\u0020\u0020\u0639\u0648\u0636\u0020{2}");
		bundle.add("tooMuchSingleElec", "No more than two single electrons");
		bundle
				.add(
						"kelecerror3",
						"\u062E\u0637\u0623\u0020\u0641\u064A\u0020\u062D\u0633\u0627\u0628\u0020\u0627\u0644\u0645\u062F\u0627\u0631\u0627\u062A");
		bundle.add("kelecerror4", "\u0645\u0632\u062F\u0648\u062C\u0629");
		bundle
				.add(
						"ktitleresultsmesomery",
						"\u0647\u0644\u064A\u0633\u0020\u003A\u0020\u0646\u062A\u0627\u0626\u062C\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629");
		bundle
				.add(
						"waitweights",
						"\u0639\u0645\u0644\u064A\u0629\u0020\u0627\u0644\u0625\u062F\u0631\u0627\u062C\u0020\u0627\u0644\u0622\u0644\u064A\u0020\u0644\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633\u0020\u0645\u0633\u062A\u0645\u0631\u0629");
		bundle
		.add(
				"waitweights2",
				"Calculating weights of structures... please wait...");
		bundle.add("methoderror",	"Cannot compute {0} : {1}");
		bundle.add("methods",	"Method");
		bundle.add("tau",	"Trust factor\t");
		bundle.add("methoderrorRedundance",	"Cannot compute HLP : Redundance detected in the structures");
		bundle.add("publications",	"Please, cite this work :\nH\u00FCckel-Lewis-Projection Method: A \"Weights Watcher\" for Mesomeric Structures. Y. Carissan, D. Hagebaum-Reignier, N. Goudard, S. Humbel J. Phys. Chem. A. 2008, 118 (50), 13256–13262.\nH\u00FCckel Theory for Lewis Structures: H\u00FCckel-Lewis Configuration Interaction (HL-CI). D. Hagebaum-Reignier, R. Girardi, Y. Carissan, S. Humbel J. Mol. Struct. (THEOCHEM) 2007, 817, 99-109.");

		MyResourceBundle.addBundle("FrameResultMesomery_ar", bundle);

		// FrameResult
		bundle
		.add(
				"kWarNegCharge",
				"\u062A\u062D\u062F\u064A\u0631 \u0628\u0627\u0631\u0627\u0645\u062A\u0631\u0627\u062A\u0020\u0637\u0631\u064A\u0642\u0629\u0020\u0647\u0648\u0643\u0644\u0020\u0627\u0644\u062E\u0627\u0635\u0629\u0020\u0628\u0627\u0644\u0623\u064A\u0648\u0646\u0627\u062A\u0020\u0627\u0644\u0633\u0627\u0644\u0628\u0629\u0020\u0644\u064A\u0633\u062A\u0020\u0647\u064A\u0020\u0627\u0644\u0623\u062D\u0633\u0646");

		bundle.add("kclose", "\u0625\u063A\u0644\u0627\u0642");
		bundle.add("krefresh", "\u062A\u062D\u0630\u064A\u062B");
		bundle.add("kbttxt", "\u0646\u0635");
		bundle.add("kbthtml", "html");
		bundle
				.add("ktitleresults",
						"\u0647\u0644\u064A\u0633\u0020\u003A\u0020\u0646\u062A\u0627\u0626\u062C");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 13)); // taille des boutons
		bundle.add("fontsize2", "12"); // taille du texte
		// matrices (rester en tailel
		// occidntale)
		bundle.add("align1", StyleConstants.ALIGN_RIGHT); // titres des
		// paragraphes
		bundle.add("align2", StyleConstants.ALIGN_LEFT); // matrices
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);
		MyResourceBundle.addBundle("FrameResult_ar", bundle);

		// FrameApp
		bundle = new MyResourceBundle();
		bundle.add("kframew", 660);
		bundle.add("kframeh", 520);
		bundle.add("kpanelmolw", 440);
		bundle.add("ktoolbarw", 110);
		bundle.add("kstatusbarh", 25);
		bundle.add("ktoolbarmw", 110);
		bundle
				.add(
						"kchangelang",
						"\u062A\u0645\u0020\u0627\u062E\u062A\u064A\u0627\u0631\u0020\u0627\u0644\u0644\u063A\u0629\u0020\u0627\u0644\u0639\u0631\u0628\u064A\u0629");
		MyResourceBundle.addBundle("FrameApp_ar", bundle);

		// DialogChangeAtom
		bundle = new MyResourceBundle();
		bundle.add("cannotReduceValence", "Cannot reduce valence");
		bundle
				.add("kTitleAtomType",
						"\u062A\u063A\u064A\u064A\u0631\u0020\u0627\u0644\u0630\u0631\u0629");
		bundle.add("kOK", "\u0645\u0648\u0627\u0641\u0642");
		bundle.add("kundo", "\u0625\u0644\u063A\u0627\u0621");
		bundle
				.add(
						"kdefault",
						"\u0627\u0644\u062D\u0627\u0644\u0629\u0020\u0627\u0644\u0627\u0641\u062A\u0631\u0627\u0636\u064A\u0629");
		bundle
				.add(
						"ktoomanyneighbours",
						"I\u0020\u0020\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062A\u063A\u064A\u064A\u0631\u0020\u0646\u0648\u0639\u0020\u0627\u0644\u0630\u0631\u0629\u003A\u0020\u0625\u0628\u062F\u0623\u0020\u0628\u0625\u0632\u0627\u0644\u0629\u0020\u0630\u0631\u0627\u062A\u0020\u0645\u0631\u062A\u0628\u0637\u0629\u0020\u0628\u0647\u0630\u0647\u0020\u0627\u0644");

		bundle
		.add(
				"katomnull",
				"You must choose an atom type");

		bundle
				.add(
						"knumeric",
						"\u064A\u062C\u0628\u0020\u0623\u0646\u0020\u062A\u0643\u0648\u0646hXY\u0020\u0642\u064A\u0645\u0629\u0020\u0631\u0642\u0645\u064A\u0629");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 13)); // taille ok annuler

		MyResourceBundle.addBundle("DialogChangeAtom_ar", bundle);

		// DialogChangeHuckelBond
		bundle = new MyResourceBundle();
		bundle
				.add(
						"kTitleChangeBonds",
						"\u062A\u063A\u064A\u064A\u0631\u0020\u0631\u0627\u0628\u0637\u0629\u0020\u0643\u064A\u0645\u064A\u0627\u0626\u064A\u0629");
		bundle.add("kOK", "\u0645\u0648\u0627\u0641\u0642");
		bundle.add("kundo", "\u0625\u0644\u063A\u0627\u0621");
		bundle
				.add(
						"kdefault",
						"\u0627\u0644\u062D\u0627\u0644\u0629\u0020\u0627\u0644\u0627\u0641\u062A\u0631\u0627\u0636\u064A\u0629");
		bundle
				.add(
						"knumeric",
						"\u064A\u062C\u0628\u0020\u0623\u0646\u0020\u062A\u0643\u0648\u0646hXY\u0020\u0642\u064A\u0645\u0629\u0020\u0631\u0642\u0645\u064A\u0629");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 13)); // taille ok annuler
		MyResourceBundle.addBundle("DialogChangeHuckelBond_ar", bundle);

		// DialogChangeBloc
		bundle = new MyResourceBundle();
		bundle.add("kchoix1", "Modify the selected bloc");
		bundle.add("kchoix2", "Delete the selected bloc");
		bundle.add("kchoix3", "Modify the number of electrons");
		bundle.add("kOK", "\u0645\u0648\u0627\u0641\u0642");
		bundle.add("kundo", "\u0625\u0644\u063A\u0627\u0621");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 13)); 
		MyResourceBundle.addBundle("DialogChangeBloc_ar", bundle);

		
		// ActionsApp
		bundle = new MyResourceBundle();
		bundle
				.add(
						"kbuild1",
						"\u0625\u062F\u0631\u0627\u062C\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0648\u0020\u0627\u0644\u0631\u0648\u0627\u0628\u0637");
		bundle
				.add(
						"kbuild2",
						"\u0625\u0636\u0627\u0641\u0629\u0020\u0630\u0631\u0627\u062A\u0020\u062C\u062F\u064A\u062F\u0629\u0020\u0639\u0646\u0020\u0637\u0631\u064A\u0642\u0020\u0636\u063A\u0637\u0020\u0648\u0020\u0636\u063A\u0637\u002D\u0633\u062D\u0628\u0020\u0639\u0644\u0649\u0020\u0645\u0633\u062A\u0648\u0649\u0020\u0627\u0644\u0644\u0648\u062D\u0020\u0627\u0644\u0645\u0631\u0643\u0632\u064A");
		bundle
				.add(
						"kchange1",
						"\u062A\u063A\u064A\u064A\u0631\u0020\u062E\u0635\u0627\u0626\u0635\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0648\u0020\u0627\u0644\u0631\u0648\u0627\u0628\u0637");
		bundle
				.add(
						"kchange2",
						"\u0625\u0636\u063A\u0637\u0020\u0639\u0644\u0649\u0020\u0627\u0644\u0630\u0631\u0629\u0020\u0623\u0648\u0020\u0627\u0644\u0631\u0627\u0628\u0637\u0629\u0020\u0627\u0644\u0645\u0631\u0627\u062F\u0020\u062A\u063A\u064A\u064A\u0631\u0020\u062E\u0635\u0627\u0626\u0635\u0647\u0627");
		bundle
				.add(
						"kdelete2",
						"\u0625\u0636\u063A\u0637\u0020\u0639\u0644\u0649\u0020\u0627\u0644\u0630\u0631\u0629\u0020\u0623\u0648\u0020\u0627\u0644\u0631\u0627\u0628\u0637\u0629\u0020\u0627\u0644\u0645\u0631\u0627\u062F\u0020\u062D\u062F\u0641\u0647\u0627");
		bundle
				.add(
						"kdelete1",
						"\u062D\u062F\u0641\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0648\u0020\u0627\u0644\u0631\u0627\u0628\u0637\u0629");
		bundle
				.add(
						"kaddcharge1",
						"\u0625\u0636\u0627\u0641\u0629\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0020\u064A\u062E\u0641\u0636\u0020\u0627\u0644\u0634\u062D\u0646\u0629\u0020\u0627\u0644\u0645\u0648\u062C\u0628\u0629");
		bundle
				.add(
						"kaddcharge2",
						"\u0644\u0642\u062F\u0020\u062A\u0645\u0020\u062E\u0641\u0636\u0020\u0627\u0644\u0634\u062D\u0646\u0629");
		bundle
				.add(
						"kdeletecharge1",
						"\u062D\u062F\u0641\u0020\u0648\u0627\u062D\u062F\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0020\u064A\u0631\u0641\u0639\u0020\u0627\u0644\u0634\u062D\u0646\u0629");
		bundle
				.add(
						"kdeletecharge2",
						"\u0644\u0642\u062F\u0020\u062A\u0645\u0020\u0631\u0641\u0639\u0020\u0627\u0644\u0634\u062D\u0646\u0629");
		bundle
				.add(
						"krenum1",
						"\u0625\u0639\u0627\u062F\u0629\u0020\u062A\u0631\u0642\u064A\u0645\u0020\u0627\u0644\u0630\u0631\u0627\u062A");
		bundle
				.add(
						"krenum2",
						"\u0627\u0636\u063A\u0637\u0020\u0639\u0644\u0649\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0644\u062A\u063A\u064A\u064A\u0631\u0020\u0645\u0631\u062A\u0628\u062A\u0647\u0646");
		bundle
				.add(
						"kmove1",
						"\u0646\u0642\u0644\u0020\u0630\u0631\u0629\u0020\u0623\u0648\u0020\u062C\u0632\u064A\u0621");
		bundle
				.add(
						"kmove2",
						"\u0627\u0636\u063A\u0637\u0020\u0648\u0020\u0627\u0633\u062D\u0628\u0020\u0627\u0644\u0641\u0623\u0631\u0629\u0020\u0645\u0646\u0020\u0623\u062C\u0644\u0020\u0646\u0642\u0644\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0623\u0648\u0020\u0627\u0644\u062C\u0632\u064A\u0621");
		bundle
				.add("krotate1",
						"\u062A\u062F\u0648\u064A\u0631\u0020\u0627\u0644\u062C\u0632\u064A\u0621");
		bundle
				.add(
						"krotate2",
						"\u0636\u063A\u0637\u0020\u0648\u0020\u0633\u062D\u0628\u0020\u0627\u0644\u0641\u0623\u0631\u0629\u0020\u0645\u0646\u0020\u0623\u062C\u0644\u0020\u062A\u062F\u0648\u064A\u0631\u0020\u0627\u0644\u062C\u0632\u064A\u0621");

		bundle.add("kcenter1","Center the molecule in the panel");
		bundle.add("kcenter2","The molecule has been centered");
		bundle.add("koptimize1","Optimize Geometry");
		bundle.add("koptimize2","The geometry has been optimized");

		bundle
				.add(
						"knum1",
						" \u0625\u0638\u0647\u0627\u0631\u0020\u0623\u0631\u0642\u0627\u0645\u0020\u0627\u0644\u0630\u0631\u0627\u062A");
		bundle
				.add(
						"kinfos1",
						"\u0625\u0638\u0647\u0627\u0631\u0020\u0645\u0639\u0644\u0648\u0645\u0627\u062A\u0020\u0627\u0644\u0628\u0646\u064A\u0629");
		bundle
		.add(
				"kcharges1",
				"\u0625\uFEC7\uFEEC\uFE8E\u0631\u0020\u0627\uFEDF\uFEB8\uFEA4\uFEE6\u0020\uFECB\uFEE0\uFEF0\u0020\u0627\uFEDF\uFEAC\u0631\u0627\u062A\u0020");
		bundle
				.add(
						"kresults1",
						"\u0641\u062A\u062D\u0020\u0646\u0627\u0641\u062F\u0629\u0020\u0627\u0644\u0646\u062A\u0627\u0626\u062C");
		bundle
				.add(
						"keraseall1",
						"\u062D\u062F\u0641\u0020\u0643\u0644\u0020\u0627\u0644\u0628\u0646\u064A\u0627\u062A\u0020\u0627\u0644\u0645\u0632\u0627\u062D\u0629\u0020\u0648\u0020\u0627\u0644\u0645\u064A\u0632\u0648\u0645\u064A\u0631\u064A\u0629");
		bundle.add("kquit1", "\u0625\u063A\u0644\u0627\u0642");
		bundle
				.add(
						"kcreate11",
						"\u0625\u0636\u0627\u0641\u0629\u0020\u0628\u0646\u064A\u0629\u0020\u0644\u0648\u064A\u0633\u0020\u062C\u062F\u064A\u062F\u0629");
		bundle
				.add(
						"kcreate12",
						"\u062A\u0645\u0020\u0625\u0636\u0627\u0641\u0629\u0020\u0628\u0646\u064A\u0629\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kgen1",
						" \u0625\u0636\u063A\u0637\u0020\u0647\u0646\u0627\u0020\u0645\u0646\u0020\u0623\u062C\u0644\u0020\u0627\u0644\u0625\u062D\u0630\u0627\u062B\u0020\u0627\u0644\u0622\u0644\u064A\u0020\u0644\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kgen2",
						"\u0625\u062D\u0630\u0627\u062B\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kgen3",
						"\u062A\u0645\u0020\u0625\u062D\u0630\u0627\u062B\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kcouple1",
						"\u062A\u0632\u0627\u0648\u062C\u0020\u0628\u064A\u0646\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u064A\u0646\u0020\u0623\u0639\u0632\u0628\u064A\u0646");
		bundle
				.add(
						"kcouple2",
						"\u0625\u0636\u063A\u0637\u0020\u0648\u0020\u0627\u0633\u062D\u0628\u0020\u0628\u064A\u0646\u0020\u0630\u0631\u062A\u064A\u0646\u0020\u0645\u0646\u0020\u0623\u062C\u0644\u0020\u062A\u0632\u0627\u0648\u062C\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u064A\u0646\u0020\u0623\u0639\u0632\u0628\u064A\u0646");
	
		bundle.add ("kbloc1", "Create a block of atoms and bonds");
		bundle.add ("kbloc2", "Click on atoms and bonds to create a new block or on [n] to modify or delete an existing block. Click on 'End block' button to finish.");
		bundle.add ("kendbloc1", "Terminate the block");
		bundle.add ("kendbloc2", "Block terminated.");
		bundle.add ("kTitleMsgboxEndBloc","Block");
		bundle.add ("kMessMsgboxEndBloc", "Number of electrons in the block :");
		bundle.add ("kMessNoModificationBloc", "No modification has been done.");
		bundle.add ("kMessAutoDeleteBloc", "The empty bloc has been deleted.");
		bundle.add ("kMessAnotherBloc", "Please, first terminate the bloc in {0}.");
		
		bundle
				.add(
						"kresults2",
						"\u0625\u0638\u0647\u0627\u0631\u0020\u0646\u0635\u0020\u0627\u0644\u0646\u062A\u0627\u0626\u062C");
		bundle
				.add(
						"kerase11",
						"\u062D\u062F\u0641\u0020\u0628\u0646\u064A\u0629\u0020\u0644\u0648\u064A\u0633\u0020\u0627\u0644\u062D\u0627\u0644\u064A\u0629");
		bundle
				.add(
						"kerase12",
						"\u062A\u0645\u0020\u062D\u062F\u0641\u0020\u0628\u0646\u064A\u0629\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kerasem1",
						"\u062D\u062F\u0641\u0020\u062C\u0645\u064A\u0639\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kerasem2",
						"\u062A\u0645\u0020\u062D\u062F\u0641\u0020\u062C\u0645\u064A\u0639\u0020\u0628\u0646\u064A\u0627\u062A\u0020\u0644\u0648\u064A\u0633");
		bundle
				.add(
						"kchangebonds1",
						"\u0625\u0636\u0627\u0641\u0629\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A");
		bundle
				.add(
						"kchangebonds2",
						"\u0625\u0636\u063A\u0637\u0020\u0639\u0644\u0649\u0020\u0627\u0644\u0630\u0631\u0629\u0020\u0623\u0648\u0020\u0627\u0644\u0631\u0627\u0628\u0637\u0629\u0020\u0627\u0644\u062A\u064A\u0020\u062A\u0631\u064A\u062F\u0020\u0625\u0636\u0627\u0641\u0629\u0020\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u0627\u062A\u0020\u0625\u0644\u064A\u0647\u0627");
		bundle
				.add(
						"kerrgen2",
						"\u062A\u062C\u0627\u0648\u0632\u0020\u0627\u0644\u0625\u0645\u0643\u0627\u0646\u064A\u0627\u062A\u002E\u0020\u0628\u0646\u064A\u0629\u0020\u0645\u0639\u0642\u062F\u0629\u002E");
		bundle
				.add(
						"kerrgen",
						"\u0648\u0642\u0639\u0020\u062E\u0637\u0623\u0020\u0623\u062B\u0646\u0627\u0621\u0020\u0627\u0644\u0625\u062D\u0630\u0627\u062B\u0020\u0627\u0644\u0622\u0644\u064A\u002E");
		bundle.add("kerrtitle", "\u062E\u0637\u0623");
		bundle
				.add(
						"removeHAtom",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062D\u062F\u0641\u0020\u0630\u0631\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646");
		bundle
				.add(
						"removeHBond",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062D\u062F\u0641\u0020\u0631\u0627\u0628\u0637\u0629\u0020\u0647\u064A\u062F\u0631\u0648\u062C\u064A\u0646\u064A\u0629");
		bundle
				.add(
						"kstatusnoaction",
						"\u0644\u0645\u0020\u064A\u062A\u0645\u0020\u0627\u062E\u062A\u064A\u0627\u0631\u0020\u0623\u064A\u0629\u0020\u0639\u0645\u0644\u064A\u0629");
		bundle
				.add(
						"kerrorseqnum",
						"\u062A\u0641\u0636\u0644\u0020\u0628\u0625\u0639\u062F\u0629\u0020\u062A\u0631\u0642\u064A\u0645\u0020\u0627\u0644\u0630\u0631\u0627\u062A\u0020\u0628\u0637\u0631\u064A\u0642\u0629\u0020\u0633\u0644\u064A\u0645\u0629");
		bundle
				.add(
						"kinaccess",
						"\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u0627\u0633\u062A\u0639\u0645\u0627\u0644\u0020\u0647\u0630\u0627\u0020\u0627\u0644\u062E\u064A\u0627\u0631\u0020\u0644\u0623\u0646\u0647\u0020\u063A\u064A\u0631\u0020\u0645\u062A\u0628\u062A\u0020\u0641\u064A\u0020\u0647\u0630\u0647\u0020\u0627\u0644\u0646\u0633\u062E\u0629\u0020\u0644\u0647\u0644\u064A\u0633");
		bundle.add("krenamefile", "The file will be named");
		bundle.add("kfilenotfound", "File not found");
		bundle.add("kinitialorientation", "Do you want to save the file with the initial structure orientation ?");
		bundle
				.add(
						"knotavalaible",
						"\u063A\u064A\u0631\u0020\u0642\u0627\u0628\u0644\u0629\u0020\u0644\u0644\u0625\u0633\u062A\u0639\u0645\u0627\u0644");
		MyResourceBundle.addBundle("ActionsApp_ar", bundle);

		// Menu
		bundle = new MyResourceBundle();
		bundle
		.add("mdefaultlocale",arabian );

		
		bundle
		.add("mnfile","\u0645\u0644\u0641");
		
		bundle
		.add("mnopen","\u0641\u062A\u062D");
		
		bundle.add("mnsave", "\u062A\u0633\u062C\u064A\u0644");
		bundle.add("mnPreferences", "\u0627\u062E\u062A\u064A\u0627\u0631\u0627\u062A");

		
		bundle.add("mnquit",
				"\u0645\u063A\u0627\u062F\u0631\u0629");
		
		bundle.add("mnedit",	"\u0627\u0644\u062A\u062D\u0631\u064A\u0631");

		
		bundle.add("mnundo", "\u0625\uFEDF\uFED0\uFE8E\u0621\u0020");
	
		bundle.add("mnredo", "\u0625\uFECB\uFE8E\u062F\u0629\u0020");
		bundle.add("mnpaste","\u0644\u0635\u0642");

		bundle.add("mndisplay", "\uFECB\uFEAE\u0636\u0020");


		bundle.add("mnexp",
				"\u0646\u0645\u0637\u0020\u0627\u0644\u062E\u0628\u064A\u0631");
		
		bundle.add("mnoptgeom","Optimize Geometry");
		
		bundle.add("mnmanipspin","Manip Spin");
		
		bundle.add("mnmanipspinCO","Switch singulet CO <-> CF");

		bundle.add("mnpolynomes","Polynomes");

		bundle.add("mnExcitations","Show excitations list...");
		bundle.add("mnBlocs","Show blocs list...");

		bundle.add("mnzoomd",
				"\u0627\u0644\u0632\u0648\u0645\u0020\u0627\u0644\u0642\u064A\u0627\u0633\u064A");			

		bundle.add("mnzoomp",
				"\u062A\u0643\u0628\u064A\u0631");
	
		bundle.add("mnzoomm",
				"\u062A\u0635\u063A\u064A\u0631");

		bundle.add("mncompute","Compute");

		bundle.add("mnaddpolynome","Add to polynome calculation");

		bundle.add("mntutorials", "\u062F\u0631\u0648\u0633\u0020\u062A\u062F\u0631\u064A\u0628\u064A\u0629\u0020");

		bundle.add("mntuto0",
				"\u062F\u0631\u0648\u0633\u0020\u062A\u062F\u0631\u064A\u0628\u064A\u0629\u0020 : 0 \uFE97\uFEDC\uFEEE\uFEF3\uFEE6\u0020\uFE9F\uFEB0\uFEF3\uFE8A\u0020");

		bundle.add ("mnhelp", "\u0625\u0639\u0627\u0646\u0629");

		bundle.add("mnguide",
			"\u062F\u0644\u064A\u0644\u0020\u0627\u0644\u0645\u0633\u062A\u062E\u062F\u0645\u064A\u0646");

		bundle.add("mnlog","Event log...");
		bundle.add("mnabout",
				"\u0645\u0639\u0644\u0648\u0645\u0627\u062A\u0020\u062D\u0648\u0644\u0020\u0647\u0644\u064A\u0633");
	

		
		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 16));
		bundle.add("mnfontAr", new Font("Helvetica", Font.PLAIN, 18));
		
		MyResourceBundle.addBundle("Menu_ar", bundle);

		// MenuPop
		bundle = new MyResourceBundle();

		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 16));

		bundle.add("mnpopcut", "\u0625\u0642\u0637\u0639");
		bundle.add("mnpopcopy", "\u0646\u0633\u062E");
		bundle.add("mnpoppaste", "\u0644\u0635\u0642");
		bundle.add("mnpopselall", "\u0627\u062E\u062A\u064A\u0627\u0631\u0020\u0627\u0644\u0643\u0644");
		

		MyResourceBundle.addBundle("MenuPop_ar", bundle);

		// About
		bundle = new MyResourceBundle();
		bundle
				.add(
						"kheadhtml",
						"<head><style type=\"text/css\">.couleur {font-family:Helvetica;font-size:13px; color: #000000;} .starterror {font-family:Helvetica;font-size:13px; color: #FF0000;}</style></head>");
		bundle.add("kimgpath", "img/hulis.gif");
		bundle.add("kjdk", "JDK");
		bundle
				.add(
						"kauthors",
						"");
		
		bundle.add("orientation", ComponentOrientation.RIGHT_TO_LEFT);

		bundle
				.add(
						"kcopyright",
						"\u062C\u0645\u064A\u0639\u0020\u062D\u0642\u0648\u0642\u0020\u0627\u0644\u0637\u0628\u0639\u0020\u0645\u062D\u0641\u0648\u0638\u0629\u0020\u0644\u0644\u0645\u0633\u0627\u0647\u0645\u064A\u0646\u0020\u0641\u064A\u0020\u0625\u0646\u062C\u0627\u0632\u0020\u0647\u0644\u064A\u0633\u0020\u0032\u0030\u0030\u0037\u002E\u0020\u062A\u0641\u0636\u0644\u0020\u0628\u0632\u064A\u0627\u0631\u0629\u0020\u0627\u0644\u0645\u0648\u0642\u0639<a href=\"http://ism2.univ-amu.fr/hulis/\">http://ism2.univ-amu.fr/hulis/</a>.");
		bundle
				.add(
						"kwarmings",
						"\u064A\u062D\u062A\u0645\u0644\u0020\u0623\u0646\u0020\u062A\u062D\u062A\u0648\u064A\u0020\u0647\u0630\u0647\u0020\u0627\u0644\u0646\u0633\u062E\u0629\u0020\u0645\u0646\u0020\u0647\u0644\u064A\u0633\u0020\u0639\u0644\u0649\u0020\u0623\u062E\u0637\u0627\u0621\u002E\u0020\u0627\u0644\u0631\u062C\u0627\u0621\u0020\u0625\u0639\u0644\u0627\u0645\u0627\u0644\u0645\u0624\u0644\u0641\u0648\u0646\u0020\u0020\u0028\u0627\u0644\u0645\u0633\u0627\u0647\u0645\u0648\u0646\u0020\u0641\u064A\u0020\u0635\u0646\u0639\u0020\u0627\u0644\u0628\u0631\u0646\u0627\u0645\u062C\u0029");
		bundle
				.add(
						"kabouttitle",
						"\u0645\u0639\u0644\u0648\u0645\u0627\u062A\u0020\u062D\u0648\u0644\u0020\u0647\u0644\u064A\u0633");
		bundle.add("kaboutWidth", 450);
		bundle.add("kaboutHeight", 420);
		bundle.add("ksplashWidth", 350);
		bundle.add("ksplashHeight", 410);
		bundle.add("dateformat", "dd/MM/yyyy");
		bundle
				.add(
						"limit",
						"\u0647\u0630\u0647\u0020\u0627\u0644\u0646\u0633\u062E\u0629\u0020\u0635\u0627\u0644\u062D\u0629\u0020\u0644\u0644\u0625\u0633\u062A\u0639\u0645\u0627\u0644\u0020\u0625\u0644\u0649\u0020\u063A\u0627\u064A\u0629");
		bundle.add("countrydate", "dd/MM/yyyy");
		bundle
				.add(
						"avalaibleversion",
						"\u0646\u0633\u062E\u0629\u0020\u062C\u062F\u064A\u062F\u0629\u0020\u0641\u064A\u0020\u0645\u062A\u0646\u0627\u0648\u0644\u0643\u0020\u0639\u0644\u0649\u0020\u0627\u0644\u0645\u0648\u0642\u0639");
		bundle
				.add(
						"avalaibleuntil",
						"\u0635\u0627\u0644\u062D\u0629\u0020\u0625\u0644\u0649\u0020\u063A\u0627\u064A\u0629");
		bundle.add("kwarning", "\u062A\u062D\u062F\u064A\u0631");

		bundle
				.add(
						"kstarterror2",
						"<\u0644\u0627\u0020\u064A\u0645\u0643\u0646\u0020\u062A\u0634\u063A\u064A\u0644\u0020\u0647\u0644\u064A\u0633");
			
		bundle.add("licencetitle", "Software agreement");
		bundle.add("creditstitle", "Credits");
		bundle.add("licence", "Software agreement...");
		bundle.add("credits", "Credits...");
		bundle.add("close", "Close");
		bundle.add("creditsfile", this.getClass().getClassLoader().getResource("files/credits_en.html"));
		MyResourceBundle.addBundle("About_ar", bundle);

		
		// StatusBar
		bundle = new MyResourceBundle();
		bundle.add("font", new Font("Helvetica",Font.PLAIN, 16));
		MyResourceBundle.addBundle("StatusBar_ar", bundle);

		// LicenseFrame
		bundle = new MyResourceBundle();
		bundle.add("texthead", "HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs. This can be desactivated in the menu File / Preferences / Privacy. By clicking on \"Accept and continue\",  you accept the HuLiS end user licence agreement below :");		
		bundle.add("title", "Software Agreement");
		bundle.add("accept", "Accept and continue");
		bundle.add("quit", "Quit HuLiS");	
		bundle.add("license", this.getClass().getClassLoader().getResource("files/license_en.html"));
		MyResourceBundle.addBundle("LicenseFrame_ar", bundle);
		
		// ErrorReport
		bundle = new MyResourceBundle();
		bundle.add("font2", new Font("Helvetica", Font.PLAIN, 13));
		bundle.add("title", "Event log");
		bundle.add("kquit", "Quit");
		bundle.add("kclose", "Close");
		bundle.add("kcopy", "Copy");
		bundle.add("kerrtxt","\u062D\u062F\u062B\u0020\u062E\u0637\u0623\u0020\u062D\u0627\u0633\u0645\u002E\u0020\u0623\u0646\u062A\u0645\u0020\u0645\u0634\u0643\u0648\u0631\u064A\u0646\u0020\u0639\u0644\u0649\u0020\u0646\u0633\u062E\u0020\u0648\u0020\u0625\u0631\u0633\u0627\u0644\u0020\u062A\u0642\u0631\u064A\u0631\u0020\u0627\u0644\u062E\u0637\u0623\u0020\u0627\u0644\u062A\u0627\u0644\u064A\u0020\u0625\u0644\u0649\u0020\u0627\u0644\u0639\u0646\u0648\u0627\u0646\u0020\u0627\u0644\u0625\u0644\u0643\u062A\u0631\u0648\u0646\u064A<a href=\"mailto:www.hulis@free.fr\" target=\"new\">www.hulis@free.fr</a>");
		MyResourceBundle.addBundle("ErrorReport_ar", bundle);

		// Informations
		bundle = new MyResourceBundle() ;
		bundle.add("informations", "Information");		
		bundle.add("message", "The information shown below will be sent to HuLiS.");		
		bundle.add("nb_use", "Number of worldwild use : ");		
		bundle.add("country", "Country  : ");		
		bundle.add("type_of_use", "Type of use : ");		
		bundle.add("total_use", "Number of use : ");		
		bundle.add("java_lang", "Java language : ");		
		bundle.add("java_version", "Java version : ");		
		bundle.add("os", "Operating system : ");		
		bundle.add("browser", "Browser : ");		
		bundle.add("hulis_version", "HuLis version : ");		
		bundle.add("start_time", "Start time (ms) : ");		
		bundle.add("date_applet", "Date and time of use (timestamp) : ");		
		bundle.add("duration", "Duration of use (ms) : ");		
		bundle.add("error", "Error  : ");		
		bundle.add("last_command", "Last command : ");		
		bundle.add("preferences_path", "Path  preferences file : ");		
		MyResourceBundle.addBundle("informations_ar", bundle);

		
	} // End initArabian

	


	
	/***************************************************************************
	 * 
	 * English language and default
	 * 
	 **************************************************************************/

	public void initEnglish() {
		//Threads
		MyResourceBundle bundle = new MyResourceBundle();
		bundle.add("stopComputeWeights", "The computation of the weights of Lewis structures is being stopped...");
		bundle.add("stopAutogen", "The generation of the Lewis structures is being stopped...");
		MyResourceBundle.addBundle("Threads_en", bundle);
		
		// ToolBarMesomery
		bundle = new MyResourceBundle();
		bundle.add("compute", "compute");
		bundle
		.add(
				"kmesomery",
				"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Lewis<br>  <span class=\"Style2\">Mesomery</span></p></body></html>");
		bundle.add("kcreate1", "Create");
		bundle.add("kgenerateall", "Generate all");
		bundle.add("kcouple", "Spin coupling");
		bundle.add ("kbloc", "Block");
		bundle.add ("kendbloc", "End block");
		bundle.add("kchangebonds", "Bond / Elect.");//\u25cf
		bundle.add("kresults", "Results");
		bundle.add("kerase1", "Erase 1");
		bundle.add("kerasem", "Erase mes.");
		bundle.add("kenergy", "E");
		bundle.add("phi", "\u03C8");
		bundle.add("kCI", "CI");
		bundle.add("kincomplete", "incomplete");
		bundle.add("exist", "duplicated");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11)); // taille des caracteres sur les boutons
		MyResourceBundle.addBundle("ToolBarMesomery_en", bundle);
		MyResourceBundle.addBundle("ToolBarMesomery", bundle);

		// ToolBarHuckel
		bundle = new MyResourceBundle();
		bundle
		.add(
				"khuckel",
				"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Huckel<br><span class=\"Style2\">&nbsp;</style></p></body></html>");
		bundle.add("kbuild", "Build");
		bundle.add("kchange", "Change");
		bundle.add("kdelete", "Delete");
		bundle.add("kmove", "Move");
		bundle.add("krotate", "Rotate");
		bundle.add("kimgmove", "img/move.png");
		bundle.add("kimgrotate", "img/rotate.png");
		bundle.add("kimgcenter", "img/center.png");
		bundle.add("koptimize", "Opt.");
		bundle.add("krenum", "Sort");
		bundle.add("kinfos", "Parameters");
		bundle.add("kcharges", "Charges");
		bundle.add("knum", "Numbering");
		bundle.add("kresults", "Results");
		bundle.add("keraseall", "Erase all");
		bundle.add("kquit", "Quit");
		bundle.add("ktotalcharge", "Total charge = {0}");
		bundle.add("kenergy", "E");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11)); 
		

		MyResourceBundle.addBundle("ToolBarHuckel_en", bundle);
		MyResourceBundle.addBundle("ToolBarHuckel", bundle);

		// StructureView
		bundle = new MyResourceBundle();

		bundle.add("kwarning", "warning");
		bundle.add("kWarNegCharge",
		"H\u00fcckel's parameters are not optimum for anions.");

		bundle.add("kverticalspacebetweenlines", 12);
		bundle.add("kendbloc", "Please, terminate the block in {0}");
		bundle.add("kselectbloc", "Select an existing bloc or create a new one");

		bundle.add("tooMuchSingleElec", "No more than two single electrons");
		bundle.add("knetcharge", "Total charge");
		bundle.add("kenergy", "E");
		bundle.add("kelecerror",
		"Please define this Lewis structure by clicking");
		bundle.add("kelecerrorb","where electrons are.");
		bundle.add("kelecerror1", "Electron(s) remaining to place : {0}");
		bundle.add("kelecerror2", "Electron(s) to remove : {0}");
		bundle.add("kelecerror4",
				"This structure already exists");
		bundle.add("kelecerror4b",
		"in mesomery : ");
		bundle.add("kErrorRedundance",  "Redundance has been detected in the structures");
		bundle.add("methoderror","Cannot compute {0} : {1}");
		bundle.add("font",  new Font("Helvetica", Font.BOLD, 11));
		MyResourceBundle.addBundle("StructureView_en", bundle);
		MyResourceBundle.addBundle("StructureView", bundle);

		// SkelMouseController
		bundle = new MyResourceBundle();
		bundle.add("ktoomanyneighbours2", "Too many bonds already.");
		bundle.add("removeHAtom", "You may not remove a hydrogen atom");
		bundle.add("removeHBond", "You may not remove a hydrogen bond");
		bundle.add("changeHBond", "You may not change a hydrogen bond");
		bundle.add("HRadR",
				"You may not change the number of radicals on a hydrogen atom");
		MyResourceBundle.addBundle("SkelMouseController_en", bundle);
		MyResourceBundle.addBundle("SkelMouseController", bundle);

		// Preferences
		bundle = new MyResourceBundle();
		bundle.add("title", "Preferences");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Cancel");
		bundle.add("kdecimal", "Digits : ");
		bundle.add("kprecisionMO", "<html>MO, charge, density,<br>overlap, weigths, coefficients :</html>");
		bundle.add("kprecisionEnergy", "Energies \u03B2, hamiltionians : ");
		bundle.add("kMOratio", "Zoom MO : ");
		bundle.add("prefAutoGen", "Automatic generation of Lewis structures :");
		bundle.add("Nsep", "Max charge : ");
		bundle.add("kAtomDisplay", "Atoms representation :");
		bundle.add("kcircle", "Circles");
		bundle.add("ksymbols", "Symbols");
		bundle.add("kautocouple", "Allow bi-radicals");
		bundle.add("kautolongdistancecouple", "AutoGen couple ld");
		bundle.add("kautosep", "AutoGen sep");
		bundle.add("kautolongdistancesep", "AutoGen sep ld");
		bundle.add("ktitle", "Preferences");
		bundle.add("uptodate", "HuLiS is up to date");
		bundle.add("checkVersion", "Automatically check for updates");
		bundle.add("updates", "Updates");
		bundle.add("sendInformation", "<html>HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs.</html>");
		bundle.add("chkSendInformation", "Allow sending of informations to HuLiS");
		bundle.add("privacy", "Privacy");
		MyResourceBundle.addBundle("Preferences_en", bundle);	
		MyResourceBundle.addBundle("Preferences", bundle);	
		
		// JOptionPane
		bundle = new MyResourceBundle();
		bundle.add("Yes", "Yes");
		bundle.add("No", "No");
		bundle.add("Cancel", "Cancel");
		MyResourceBundle.addBundle("JOptionPane_en", bundle);
		MyResourceBundle.addBundle("JOptionPane", bundle);

		// FrameResultStruct
		bundle = new MyResourceBundle();
		bundle.add("pisymbol", "\u03c0");
		bundle
				.add(
						"resh0",
						"No structures, use the \"build\" button on the left to define a carbon based molecular skeleton (drag and drop) and \"change\" to set non-carbon atoms.");
		bundle.add("resh1", "Structure type : ");
		bundle.add("resh2", "Number of atoms : ");
		bundle.add("resh2a", "Number of H\u00fcckel atoms : ");
		bundle.add("resh3", "Number of \u03c0 electrons : ");
		bundle.add("resh4", "Total charge : ");
		bundle.add("resh5", "1- Hamiltonian : ");
		bundle.add("resh6", "2- Orbitals : ");
		bundle.add("resh7", "\u025Bi :  ");
		bundle.add("resh8", "Total energy : ");
		bundle
				.add(
						"resh9",
						"3- Electronic density matrix : electron densities on and between the atoms (the density non bonded atoms is ill-defined) : ");
		bundle.add("resh10", "4- Atomic charges : ");
		bundle.add("resh11", "Total charge : ");
		bundle.add("resh12", "ni :");
		bundle.add("resh13", "Atom\tCharge :");
		bundle.add("resh131", "<b>Atom</b></td><td><b>Charge</b>");
		bundle.add("ktitleresultstruct", "Results for Huckel");
		bundle.add("kdeloc", "delocalized structure");
		bundle.add("kloc", "localized structure");
		MyResourceBundle.addBundle("FrameResultStruct_en", bundle);
		MyResourceBundle.addBundle("FrameResultStruct", bundle);

		// FrameResultMesomery
		bundle = new MyResourceBundle();
		bundle.add("phitot", "\u03C8tot");
		bundle.add("resmesnbgen", "Number of Lewis structures :");
		bundle.add("resmesnbdisp", "Number of active Lewis structures :");
		bundle.add("resmes1", "Mesomery by configurations interaction");
		bundle.add("resmes2", "B matrix coeff :");
		bundle.add("resmes3", "C matrix :");
		bundle.add("resmes4", "Eigenvalues An :");
		bundle.add("resmes5", "Best Bn = -(1 / (An - 1) )");
		bundle.add("resmes6", "Best Bn :");
		bundle.add("resmes7",
				"1- Configurations interaction :");
		bundle.add("hlpOverlap", "    Overlap HLP matrix :" );

		bundle
				.add("resmes10",
						"2- Eigenvectors (Ci) and weights (Wi) of the mesomery :");
		bundle.add("resmes11", "\u03C8tot = C1.\u03C81 + C2.\u03C82 ...");
		bundle.add("knbostring", "3- NBO String for gaussian :");
		bundle
				.add(
						"resmes111",
						"\u03C8<sub>tot</sub> = C<sub>1</sub>.\u03C8<sub>1</sub> + C<sub>2</sub>.\u03C8<sub>2</sub> ...");
		bundle
				.add("nothingmesomery",
						"Create Lewis structures with the \"Create\" and \"Electrons\" buttons.");
		bundle
				.add("kelecerror",
						"{0} : Incorrect number of \u03c0 electrons : you put {1} instead of {2}");
		bundle.add("kelecerror3", "Error in orbitals calculation.");
		bundle.add("kelecerror4", "Duplicated");
		bundle.add("ktitleresultsmesomery", "HuLiS - Results for mesomery");
		bundle.add("waitweights", "Lewis structures automatic generation is running... please wait...");
		bundle.add(	"waitweights2",	"The compute of weights of Lewis structures is running... please wait...");
		bundle.add("methoderror","Cannot compute {0} : {1}");
		bundle.add("methods",	"Method");
		bundle.add("tau",	"Trust factor\t");
		bundle.add("methoderrorRedundance",	"Cannot compute : Redundance detected in the structures");
		bundle.add("publications",	"Please, cite this work :\nH\u00FCckel-Lewis-Projection Method: A \"Weights Watcher\" for Mesomeric Structures. Y. Carissan, D. Hagebaum-Reignier, N. Goudard, S. Humbel J. Phys. Chem. A. 2008, 118 (50), 13256–13262.\nH\u00FCckel Theory for Lewis Structures: H\u00FCckel-Lewis Configuration Interaction (HL-CI). D. Hagebaum-Reignier, R. Girardi, Y. Carissan, S. Humbel J. Mol. Struct. (THEOCHEM) 2007, 817, 99-109.");
		MyResourceBundle.addBundle("FrameResultMesomery_en", bundle);
		MyResourceBundle.addBundle("FrameResultMesomery", bundle);

		// FrameResult
		bundle = new MyResourceBundle();
		bundle.add("kWarNegCharge",
		"Warning : H\u00fcckel's parameters are not optimum for anions.");
		bundle.add("kclose", "Close");
		bundle.add("krefresh", "Refresh");
		bundle.add("kbttxt", "text");
		bundle.add("kbthtml", "html");
		bundle.add("ktitleresults", "HuLiS - Results");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("fontsize2", "12");
		bundle.add("align1", StyleConstants.ALIGN_LEFT);
		bundle.add("align2", StyleConstants.ALIGN_LEFT);
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);
		MyResourceBundle.addBundle("FrameResult_en", bundle);
		MyResourceBundle.addBundle("FrameResult", bundle);

		// FrameApp
		bundle = new MyResourceBundle();
		bundle.add("kframew", 660);
		bundle.add("kframeh", 520);
		bundle.add("kpanelmolw", 440);
		bundle.add("ktoolbarw", 110);
		bundle.add("kstatusbarh", 25);
		bundle.add("ktoolbarmw", 110);
		bundle.add("kchangelang", "English language selected");
		MyResourceBundle.addBundle("FrameApp_en", bundle);
		MyResourceBundle.addBundle("FrameApp", bundle);

		// DialogChangeAtom
		bundle = new MyResourceBundle();
		bundle.add("cannotReduceValence", "Cannot reduce valence");
		bundle.add("kTitleAtomType", "Change atom");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Cancel");
		bundle.add("kdefault", "Default");
		bundle.add("ktoomanyneighbours",
				"You may not change atom type. Too many bonds already.");
		bundle
		.add(
				"katomnull",
				"You must choose an atom type");
		bundle.add("knumeric", "hX must be a numeric value");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeAtom_en", bundle);
		MyResourceBundle.addBundle("DialogChangeAtom", bundle);

		// DialogChangeHuckelBond
		bundle = new MyResourceBundle();
		bundle.add("kTitleChangeBonds", "Change bond");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Cancel");
		bundle.add("kdefault", "Default");
		bundle.add("knumeric", "hXY must be a numeric value");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeHuckelBond_en", bundle);
		MyResourceBundle.addBundle("DialogChangeHuckelBond", bundle);

		// DialogChangeBloc
		bundle = new MyResourceBundle();
		bundle.add("kchoix1", "Modify the selected bloc");
		bundle.add("kchoix2", "Delete the selected bloc");
		bundle.add("kchoix3", "Modify the number of electrons");
		bundle.add("kOK", "Ok");
		bundle.add("kundo", "Cancel");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11)); 
		MyResourceBundle.addBundle("DialogChangeBloc_en", bundle);
		MyResourceBundle.addBundle("DialogChangeBloc", bundle);

		
		// ActionsApp
		bundle = new MyResourceBundle();
		bundle.add("kbuild1", "Build atoms and bonds");
		bundle
				.add("kbuild2",
						"Add new atoms by clicks and drag and drop in the central panel");
		bundle.add("kchange1", "Change atoms' and bonds' parameters");
		bundle.add("kchange2",
				"Select atoms or bonds to change their parameters");
		bundle.add("kdelete1", "Delete atoms and bonds");
		bundle.add("kdelete2", "Select atoms or bonds to delete");
		bundle.add("kaddcharge1",
				"Add one electron<br>(decrease the positive charge)");
		bundle.add("kaddcharge2", "The charge has been decreased");
		bundle.add("kdeletecharge1",
				"Delete one electron<br>(increase the positive charge)");
		bundle.add("kdeletecharge2", "The charge has been increased");
		bundle.add("krenum1", "Sort the atoms");
		bundle.add("krenum2", "Select the atoms to change their order");
		bundle.add("kmove1", "Move atoms<br>or the molecule");
		bundle.add("kmove2", "Use the mouse to move atoms or the molecule");
		bundle.add("krotate1", "rotate the molecule");
		bundle.add("krotate2", "Use the mouse to rotate");
		bundle.add("kcenter1","Center the molecule in the panel");
		bundle.add("kcenter2","The molecule has been centered");
		bundle.add("koptimize1","Optimize Geometry");
		bundle.add("koptimize2","The geometry has been optimized");
		bundle.add("knum1", " Display atoms' numbering");
		bundle.add("kinfos1", "Display atoms' and bonds' parameters");
		bundle.add("kcharges1", "Display the atomic charges");
			bundle.add("kresults1", "Open the results window (text)");
		bundle.add("keraseall1",
				"Erase all<br>(Delocalized structure and mesomery)");
		bundle.add("kquit1", "Quit the applet");
		bundle.add("kcreate11", "Add a new Lewis structure");
		bundle.add("kcreate12", "A Lewis structure has been added");
		bundle.add("kgen1", " Automatic Lewis structures guess");
		bundle.add("kgen2", "Generating guessed Lewis structures...");
		bundle.add("kgen3", "Lewis structures have been guessed");
		bundle.add("kcouple1", "Spin coupling");
		bundle.add("kcouple2",
				"Drag and drop between two radicals to couple them as singlet");
		bundle.add ("kbloc1", "Create a block of atoms and bonds");
		bundle.add ("kbloc2", "Click on atoms and bonds to create a new block or on [n] to modify or delete an existing block. Click on 'End block' button to finish.");
		bundle.add ("kendbloc1", "Terminate the block");
		bundle.add ("kendbloc2", "Block terminated.");
		bundle.add ("kTitleMsgboxEndBloc","Block");
		bundle.add ("kMessMsgboxEndBloc", "Number of electrons in the block :");
		bundle.add ("kMessNoModificationBloc", "No modification has been done.");
		bundle.add ("kMessAutoDeleteBloc", "The empty bloc has been deleted.");
		bundle.add ("kMessAnotherBloc", "Please, first terminate the bloc in {0}.");

		bundle.add("kresults2", "Open the results window (text)");
		bundle.add("kerase11", "Delete this Lewis structure");
		bundle.add("kerase12", "The Lewis structure has been deleted");
		bundle.add("kerasem1", "Delete all Lewis structures");
		bundle.add("kerasem2", "Lewis structures have been deleted");
		bundle.add("kchangebonds1", "Define electrons and bonds ");
		bundle.add("kchangebonds2", "Select where to add electrons and bonds.");
		bundle.add("kerrgen2", "Overflow ");
		bundle.add("kerrgen", "Error in autogeneration");
		bundle.add("kerrtitle", "Error");
		bundle.add("removeHAtom", "You may not remove a hydrogen atom");
		bundle.add("removeHBond", "You may not remove a hydrogen bond");
		bundle.add("kstatusnoaction", "Please select a command option.");
		bundle.add("kerrorseqnum", "Please reorder the atoms");
		bundle.add("kinaccess", "This option is not implemented");
		bundle.add("krenamefile", "The file will be named");
		bundle.add("kfilenotfound", "File not found");
		bundle.add("kinitialorientation", "Do you want to save the file with the initial structure orientation ?");
		bundle.add("knotavalaible", "Not avalaible");
		MyResourceBundle.addBundle("ActionsApp_en", bundle);
		MyResourceBundle.addBundle("ActionsApp", bundle);

		
		
		// Menu
		bundle = new MyResourceBundle();
		bundle
		.add("mdefaultlocale",english );

		bundle
		.add("mnfile","File");
		
		bundle
		.add("mnopen","Open...");

		bundle.add("mnsave", "Save...");

		bundle.add("mnPreferences", "Preferences...");

		
		bundle.add("mnquit",
				"Quit");
		
		bundle.add("mnedit",	"Edit");

		bundle.add("mnundo", "Undo");

		bundle.add("mnredo", "Redo");
		
		bundle.add("mnpaste","Paste");

		bundle.add("mndisplay", "Display");

		bundle.add("mnexp",
				"Expert mode");

		bundle.add("mnoptgeom","Optimize Geometry");
		
		bundle.add("mnmanipspin","Manip Spin");

		bundle.add("mnmanipspinCO","Switch singulet CO <-> CF");

		bundle.add("mnpolynomes","Polynomes");

		bundle.add("mnExcitations","Show excitations list...");
		bundle.add("mnBlocs","Show blocs list...");


		bundle.add("mnzoomd",
				"Default Zoom");
		
		bundle.add("mnzoomp",
				"Zoom in");

		bundle.add("mnzoomm",
				"Zoom out");

		bundle.add("mncompute","Compute");

		bundle.add("mnaddpolynome","Add to polynome calculation");
		
		bundle.add("mntutorials", "Tutorials");

		bundle.add("mntuto0",
				"Tutorial 0 : Build");

		bundle.add ("mnhelp", "Help");

		bundle.add("mnguide",
			"User's guide...");
		bundle.add("mnlog","Event log...");
		bundle.add("mnabout",
				"About HuLiS...");
		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 12));
		bundle.add("mnfontAr", new Font("Helvetica", Font.PLAIN, 16));
	
		MyResourceBundle.addBundle("Menu_en", bundle);
		MyResourceBundle.addBundle("Menu", bundle);

		// MenuPop
		bundle = new MyResourceBundle();

		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 12));
		bundle.add("mnpopcut", "Cut");
		bundle.add("mnpopcopy", "Copy");
		bundle.add("mnpoppaste", "Paste");
		bundle.add("mnpopselall", "Select all");	

		MyResourceBundle.addBundle("MenuPop_en", bundle);
		MyResourceBundle.addBundle("MenuPop", bundle);
			
	
		// About
		bundle = new MyResourceBundle();
		bundle
				.add(
						"kheadhtml",
						"<head><style type=\"text/css\">.couleur {font-family:Helvetica;font-size:10px; color: #000000;}.starterror {font-family:Helvetica;font-size:10px; c color: #FF0000;}</style></head>");
		bundle.add("kimgpath", "img/hulis.gif");
		bundle.add("kjdk", "JDK");
		bundle.add("kauthors", "Authors :");
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);

		bundle
				.add(
						"kcopyright",
						"(c) Copyright HuLiS contributors 2007. All rights reserved.<br>Visit the web site <a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a>");
		bundle
				.add(
						"kwarmings",
						"<span class=\"couleur\">For any remarks, please report to </span><a href=\"mailto:stephane.humbel@univ-amu.fr\" target=\"new\">stephane.humbel@univ-amu.fr</a><span class=\"couleur\">.</span>");
		bundle.add("kabouttitle", "About HuLiS");
		bundle.add("kaboutWidth", 400);
		bundle.add("kaboutHeight", 420);
		bundle.add("ksplashWidth", 350);
		bundle.add("ksplashHeight", 420);
		bundle.add("dateformat", "MM/dd/yyyy");
		bundle.add("limit", "This version is valid until");
		bundle
				.add(
						"avalaibleversion",
						"A new version is avalaible on <a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a> : v");
		bundle.add("avalaibleuntil", "Avalaible until : ");
		bundle.add("countrydate", "MM/dd/yyyy");
	
		bundle.add("kwarning", "warning");
		bundle
				.add("kstarterror2",
						"<span class=\"starterror\">Impossible de d\u00E9marrer HuLiS.");
		bundle.add("licencetitle", "Software agreement");
		bundle.add("creditstitle", "Credits");
		bundle.add("licence", "Software agreement...");
		bundle.add("credits", "Credits...");
		bundle.add("close", "Close");
		bundle.add("creditsfile", this.getClass().getClassLoader().getResource("files/credits_en.html"));
		MyResourceBundle.addBundle("About_en", bundle);
		MyResourceBundle.addBundle("About", bundle);

		// StatusBar
		bundle = new MyResourceBundle();
		bundle.add("fontsize",  new Font("Helvetica", Font.PLAIN, 12));
		MyResourceBundle.addBundle("StatusBar_en", bundle);
		MyResourceBundle.addBundle("StatusBar", bundle);

		// LicenseFrame
		bundle = new MyResourceBundle();
		bundle.add("texthead", "HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs. This can be desactivated in the menu File / Preferences / Privacy. By clicking on \"Accept and continue\",  you accept the HuLiS end user licence agreement below :");		
		bundle.add("title", "Software Agreement");
		bundle.add("accept", "Accept and continue");
		bundle.add("quit", "Quit HuLiS");
		bundle.add("license", this.getClass().getClassLoader().getResource("files/license_en.html"));
		MyResourceBundle.addBundle("LicenseFrame_en", bundle);
		MyResourceBundle.addBundle("LicenseFrame", bundle);

		// ErrorReport
		bundle = new MyResourceBundle();
		bundle.add("font2",  new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("title", "Event log");
		bundle.add("kquit", "Quit");
		bundle.add("kclose", "Close");
		bundle.add("kcopy", "Copy");
		bundle
				.add(
						"kerrtxt",
						"A critical error was happened. Please, copy this below error report and paste it in an email to <a href=\"mailto:www.hulis@free.fr\" target=\"new\">www.hulis@free.fr</a>");
		MyResourceBundle.addBundle("ErrorReport_en", bundle);
		MyResourceBundle.addBundle("ErrorReport", bundle);
		
		// Informations
		bundle = new MyResourceBundle() ;
		bundle.add("informations", "Information");		
		bundle.add("message", "The information shown below will be sent to HuLiS.");		
		bundle.add("nb_use", "Number of worldwild use : ");		
		bundle.add("country", "Country  : ");		
		bundle.add("type_of_use", "Type of use : ");		
		bundle.add("total_use", "Number of use : ");		
		bundle.add("java_lang", "Java language : ");		
		bundle.add("java_version", "Java version : ");		
		bundle.add("os", "Operating system : ");		
		bundle.add("browser", "Browser : ");		
		bundle.add("hulis_version", "HuLis version : ");		
		bundle.add("start_time", "Start time (ms) : ");		
		bundle.add("date_applet", "Date and time of use (timestamp) : ");		
		bundle.add("duration", "Duration of use (ms) : ");		
		bundle.add("error", "Error  : ");		
		bundle.add("last_command", "Last command : ");		
		bundle.add("preferences_path", "Path  preferences file : ");		
		MyResourceBundle.addBundle("informations_en", bundle);
		MyResourceBundle.addBundle("informations", bundle);

		
	} // end InitEnglish

	/***************************************************************************
	 * 
	 * French language
	 * 
	 **************************************************************************/

	public void initFrench() {
		//Threads
		MyResourceBundle bundle = new MyResourceBundle();
		bundle.add("stopComputeWeights", "Arrêt du calcul des poids...");
		bundle.add("stopAutogen", "Arrêt de la génération automatique des structures de Lewis...");
		MyResourceBundle.addBundle("Threads_fr", bundle);
		
		
		// ToolBarMesomery
		bundle = new MyResourceBundle();
		bundle.add("compute", "calcul");
		bundle
				.add(
						"kmesomery",
						"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Lewis<br>  <span class=\"Style2\">M\u00E9somerie</span></p></body></html>");
		bundle.add("kcreate1", "Cr\u00E9er");
		bundle.add("kgenerateall", "G\u00E9n\u00E9rer tout");
		bundle.add("kcouple", "Couple");
		bundle.add ("kbloc", "Bloc");
		bundle.add ("kendbloc", "Fin bloc");
		bundle.add("kchangebonds", "Liaison / Elect."); // \u25cf
		bundle.add("kresults", "R\u00E9sultats");
		bundle.add("kerase1", "Suppr. 1");
		bundle.add("kerasem", "Suppr. mes.");
		bundle.add("kenergy", "E");
		bundle.add("phi", "\u03C8");
		bundle.add("kCI", "CI");
		bundle.add("kincomplete", "incomplet");
		bundle.add("exist", "dupliqu\u00E9e");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11)); // taille des caracteres sur les boutons
		MyResourceBundle.addBundle("ToolBarMesomery_fr", bundle);

		// ToolBarHuckel

		bundle = new MyResourceBundle();
		bundle
				.add(
						"khuckel",
						"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Huckel<br><span class=\"Style2\">&nbsp;</style></p></body></html>");
		bundle.add("kbuild", "Cr\u00E9er");
		bundle.add("kchange", "Modifier");
		bundle.add("kdelete", "Supprimer");
		bundle.add("kmove", "D\u00E9placer");
		bundle.add("krotate", "Tourner");
		bundle.add("kimgmove", "img/move.png");
		bundle.add("kimgrotate", "img/rotate.png");
		bundle.add("kimgcenter", "img/center.png");
		bundle.add("koptimize", "Opt.");
		bundle.add("krenum", "Ordonner");
		bundle.add("kinfos", "Param\u00E8tres");
		bundle.add("kcharges", "Charges");
		bundle.add("knum", "Num\u00E9rotation");
		bundle.add("kresults", "R\u00E9sultats");
		bundle.add("keraseall", "Suppr. tout");
		bundle.add("kquit", "Quitter");
		bundle.add("ktotalcharge", "Charge totale = {0}");
		bundle.add("kenergy", "E");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11) );
		MyResourceBundle.addBundle("ToolBarHuckel_fr", bundle);

		
		// StructureView
		bundle = new MyResourceBundle();
		
		bundle.add("kwarning", "Avertissement");
		bundle.add("kWarNegCharge",
		"Le param\u00E9trage des anions n'est pas optimum");

		bundle.add("kverticalspacebetweenlines", 12);
		bundle.add("kendbloc", "Terminez le bloc dans {0}");
		bundle.add("kselectbloc", "Selectionnez un bloc ou cr\u00E9ez en un nouveau");

		bundle.add("tooMuchSingleElec", "Pas plus de deux \u00E9lectrons c\u00E9libataires");
		bundle.add("knetcharge", "Charge nette");
		bundle.add("kenergy", "E");
		bundle
				.add("kelecerror",
						"D\u00E9crivez votre structure de Lewis en pla\u00E7ant");
		bundle
		.add("kelecerrorb",
				"les \u00E9lectrons sur (ou entre) les atomes.");
		bundle.add("kelecerror1",
				"Electron(s) restant a placer : {0}");
		bundle.add("kelecerror2",
				"Electron(s) \u00E0 supprimer : {0}");
		bundle
				.add("kelecerror4",
						"Cette structure existe d\u00E9j\u00E0");
		bundle
		.add("kelecerror4b",
				"dans la m\u00E9somerie : ");
		bundle.add("kErrorRedundance",  "Redondance d\u00E9tect\u00E9e dans les structures pr\u00E9sentes");
		bundle.add("methoderror","Impossible de calculer {0} : {1}");

		bundle.add("font",  new Font("Helvetica", Font.BOLD, 11));
		MyResourceBundle.addBundle("StructureView_fr", bundle);

		// SkelMouseController
		bundle = new MyResourceBundle();
		bundle
				.add("ktoomanyneighbours2",
						"Impossible de cr\u00E9er une liaison. L'atome a trop d'atomes voisins.");
		bundle.add("removeHAtom",
				"Impossible supprimer un atome d'hydrog\u00E8ne");
		bundle.add("removeHBond",
				"Impossible de supprimer une liaison hydrog\u00E8ne");
		bundle.add("changeHBond",
				"Impossible de modifier une liaison hydrog\u00E8ne");
		bundle
				.add("HRadR",
						"Impossible de changer le nombre de radicaux sur un atome d'hydrog\u00E8ne");
		MyResourceBundle.addBundle("SkelMouseController_fr", bundle);

		// Preferences

		bundle = new MyResourceBundle();
		bundle.add("title", "Pr\u00E9f\u00E9rences");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Annuler");
		bundle.add("kdecimal", "D\u00E9cimaux : ");
		bundle.add("kprecisionMO", "<html>OM, charge, recouvrement,<br>densit\u00E9, poids, coefficients :</html>");
		bundle.add("kprecisionEnergy", "\u00C9nergies \u03B2, hamiltioniens : ");
		bundle.add("kMOratio", "Zoom OM : ");
		bundle.add("prefAutoGen", "G\u00E9n\u00E9rateur automatiques de structures de Lewis :");
		bundle.add("Nsep", "Max charge : ");
		bundle.add("kAtomDisplay", "Repr\u00E9sentation des atomes :");
		bundle.add("kcircle", "Dessin");
		bundle.add("ksymbols", "Symboles");
		bundle.add("kautocouple", "Autoriser bi-radicaux");
		bundle.add("kautolongdistancecouple", "AutoGen couple ld");
		bundle.add("kautosep", "AutoGen sep");
		bundle.add("kautolongdistancesep", "AutoGen sep ld");
		bundle.add("ktitle", "Pr\u00E9f\u00E9rences");
		bundle.add("uptodate", "HuLiS est à jour");
		bundle.add("checkVersion", "V\u00E9rifier les mises \u00E0 jour automatiquement");
		bundle.add("updates", "Mise \u00E0 jour");
		bundle.add("sendInformation", "<html>HuLiS est rendu librement disponible par des subventions qui nous am\u00E8nent \u00E0 collecter une quantit\u00E9 minimale d'<a href=\"class:views.Informations\">informations</a> anonymes pour pouvoir renseigner le taux d’utilisation et adapter au mieux le service aux besoins de nos utilisateurs.</html>");
		bundle.add("chkSendInformation", "Autoriser l’envoi d’informations \u00E0 HuLiS");
		bundle.add("privacy", "Vie priv\u00E9e");
		MyResourceBundle.addBundle("Preferences_fr", bundle);
		
		// JOptionPane
		bundle = new MyResourceBundle();
		bundle.add("Yes", "Oui");
		bundle.add("No", "Non");
		bundle.add("Cancel", "Annuler");
		MyResourceBundle.addBundle("JOptionPane_fr", bundle);

		// FrameResultStruct
		bundle = new MyResourceBundle();
		bundle.add("pisymbol", "\u03c0");
		bundle
				.add(
						"resh0",
						"La structure est vide. Utilisez le bouton \"Cr\u00E9er\" dans la barre d'outils \u00E0 gauche, pour d\u00E9finir une structure mol\u00E9culaire \u00E0 base de carbone (cliquer-glisser), et le bouton \"Modifier\" pour modifier le type des atomes (param\u00E8tres des atomes et des liaisons).");
		bundle.add("resh1", "Type de structure : ");
		bundle.add("resh2", "Nombre d'atomes : ");
		bundle.add("resh2a", "Nombre d'atomes de H\u00fcckel : ");
		bundle.add("resh3", "Nombre d'\u00E9lectrons \u03c0 : ");
		bundle.add("resh4", "Charge totale : ");
		bundle.add("resh5", "1- Hamiltonien :");
		bundle.add("resh6", "2- Orbitales : ");
		bundle.add("resh7", "\u025Bi :  ");
		bundle.add("resh8", "Energie totale : ");
		bundle
				.add(
						"resh9",
						"3- Matrice de densit\u00E9 \u00E9lectronique : quantit\u00E9 d'\u00E9lectrons sur les atomes et entre atomes (les densit\u00E9s entre atomes non li\u00E9s ne doivent pas \u00EAtre utilis\u00E9es) : ");
		bundle.add("resh10", "4- Charges atomiques :");
		bundle.add("resh11", "Charge totale  : ");
		bundle.add("resh12", "ni :");
		bundle.add("resh13", "Atome\tCharge :");
		bundle.add("resh131", "<b>Atome</b></td><td><b>Charge</b>");
		bundle.add("ktitleresultstruct", "Resultats pour Huckel");
		bundle.add("kdeloc", "structure d\u00E9localis\u00E9e");
		bundle.add("kloc", "structure localis\u00E9e");
		MyResourceBundle.addBundle("FrameResultStruct_fr", bundle);

		// FrameResultMesomery
		bundle = new MyResourceBundle();
		bundle.add("phitot", "\u03C8tot");
		bundle.add("resmesnbgen", "Nombre  de structures de Lewis :");
		bundle.add("resmesnbdisp", "Nombre de structures de Lewis actives :");
		bundle
				.add("resmes1",
						"Calcul de la m\u00E9som\u00E9rie par interaction de configurations");
		bundle.add("resmes2", " Matrice coeff B utilis\u00E9e :");
		bundle.add("resmes3", "Premi\u00E8re matrice C :");
		bundle.add("resmes4", "Valeurs propres An :");
		bundle.add("resmes5", "Valeur positive unique Bn = -(1 / (An - 1) )");
		bundle.add("resmes6", "Bn Positif :");
		bundle.add("resmes7",
				"1- Interaction des configurations :");
		bundle.add("hlpOverlap", "    Matrice de recouvrement HLP :" );

		bundle
				.add(
						"resmes10",
						"2- Vecteurs propres (Ci) et poids (Wi) de la m\u00E9som\u00E9rie :");
		bundle.add("resmes11", "\u03C8tot = C1.\u03C81 + C2.\u03C82 ...");
		bundle.add("knbostring", "3- Cha\u00EEne NBO pour gaussian :");
		bundle
				.add(
						"resmes111",
						"\u03C8<sub>tot</sub> = C<sub>1</sub>.\u03C8<sub>1</sub> + C<sub>2</sub>.\u03C8<sub>2</sub> ...");
		bundle
				.add(
						"nothingmesomery",
						"Cr\u00E9ez des structures de Lewis avec les boutons \"Cr\u00E9er\" et \"Electrons\".");
		bundle
				.add(
						"kelecerror",
						"{0} : Nombre d''\u00E9lectrons \u03c0 incorrect : vous en avez mis {1} au lieu de {2}");
		bundle.add("tooMuchSingleElec", "Pas plus de deux \u00E9lectrons c\u00E9libataires");
		bundle.add("kelecerror3", "Erreur dans le calcul des orbitales");
		bundle.add("kelecerror4", "dupliqu\u00E9e");
		bundle.add("ktitleresultsmesomery",
				"HuLiS - Resultats pour la mesomerie");
		bundle.add("waitweights","L'autog\u00E9n\u00E9ration des structures de Lewis est en cours...");
		bundle.add(	"waitweights2",	"Le calcul des poids des structures de Lewis est en cours...");
		bundle.add("methoderror","Impossible de calculer {0} : {1}");
		bundle.add("methods",	"M\u00E9thode");
		bundle.add("tau",	"Taux de confiance");
		bundle.add("methoderrorRedundance",	"Impossible de calculer HLP : Redondance d\u00E9tect\u00E9e dans les structures");
		bundle.add("publications",	"Merci de citer ces travaux :\nH\u00FCckel-Lewis-Projection Method: A \"Weights Watcher\" for Mesomeric Structures. Y. Carissan, D. Hagebaum-Reignier, N. Goudard, S. Humbel J. Phys. Chem. A. 2008, 118 (50), 13256–13262.\nH\u00FCckel Theory for Lewis Structures: H\u00FCckel-Lewis Configuration Interaction (HL-CI). D. Hagebaum-Reignier, R. Girardi, Y. Carissan, S. Humbel J. Mol. Struct. (THEOCHEM) 2007, 817, 99-109.");

		MyResourceBundle.addBundle("FrameResultMesomery_fr", bundle);

		// FrameResult
		bundle = new MyResourceBundle();
		bundle.add("kWarNegCharge",
		"Avertissement : Le param\u00E9trage des anions n'est pas optimum");
		bundle.add("kclose", "Fermer");
		bundle.add("krefresh", "Rafra\u00EEchir");
		bundle.add("kbttxt", "texte");
		bundle.add("kbthtml", "html");
		bundle.add("ktitleresults", "HuLiS - Resultats");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("fontsize2", "12");
		bundle.add("align1", StyleConstants.ALIGN_LEFT);
		bundle.add("align2", StyleConstants.ALIGN_LEFT);
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);
		MyResourceBundle.addBundle("FrameResult_fr", bundle);

		// FrameApp
		bundle = new MyResourceBundle();
		bundle.add("kframew", 660);
		bundle.add("kframeh", 520);
		bundle.add("kpanelmolw", 440);
		bundle.add("ktoolbarw", 110);
		bundle.add("kstatusbarh", 25);
		bundle.add("ktoolbarmw", 110);
		bundle.add("kchangelang",
				"Langue fran\u00E7aise s\u00E9lectionn\u00E9e");
		MyResourceBundle.addBundle("FrameApp_fr", bundle);

		// DialogChangeAtom
		bundle = new MyResourceBundle();
		bundle.add("cannotReduceValence", "Impossible de r\u00E9duire la valence");
		bundle.add("kTitleAtomType", "Modifier atome");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Annuler");
		bundle.add("kdefault", "Par d\u00E9faut");
		bundle
				.add(
						"ktoomanyneighbours",
						"Impossible de changer le type d'atome. Supprimer auparavant les atomes voisins en exc\u00E8s.");
		bundle
		.add(
				"katomnull",
				"Le type d'atome doit \u00EAtre renseign\u00E9.");
		bundle.add("knumeric", "hX doit \u00EAtre une valeur num\u00E9rique");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeAtom_fr", bundle);

		// DialogChangeHuckelBond
		bundle = new MyResourceBundle();
		bundle.add("kTitleChangeBonds", "Modifier liaison");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Annuler");
		bundle.add("kdefault", "Par d\u00E9faut");
		bundle.add("knumeric", "hXY doit \u00EAtre une valeur num\u00E9rique");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeHuckelBond_fr", bundle);


		// DialogChangeBloc
		bundle = new MyResourceBundle();
		bundle.add("kchoix1", "Modifier le bloc sélectionné");
		bundle.add("kchoix2", "Supprimer le bloc sélectionné");
		bundle.add("kchoix3", "Modifier le nombre d'électrons");
		bundle.add("kOK", "Ok");
		bundle.add("kundo", "Annuler");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11)); 
		MyResourceBundle.addBundle("DialogChangeBloc_fr", bundle);

		// ActionsApp
		bundle = new MyResourceBundle();
		bundle.add("kbuild1", "Cr\u00E9er des atomes et des liaisons");
		bundle
				.add(
						"kbuild2",
						"Cr\u00E9er de nouveaux atomes par cliquer et cliquer-d\u00E9placer dans le panneau central");
		bundle.add("kchange1",
				"Modifier les param\u00E8tres des atomes ou des liaisons");
		bundle
				.add("kchange2",
						"Cliquer sur les atomes ou les liaisons pour modifier leurs param\u00E8tres");
		bundle.add("kdelete2",
				"Cliquer sur un atome ou une laison \u00E0 supprimer");
		bundle.add("kdelete1", "Supprimer des atomes ou des liaisons");
		bundle
				.add(
						"kaddcharge1",
						"Ajouter un \u00E9lectron sur la mol\u00E9cule<br>(d\u00E9cr\u00E9menter la charge)");
		bundle
				.add("kaddcharge2",
						"La charge de la mol\u00E9cule a \u00E9t\u00E9 d\u00E9cr\u00E9ment\u00E9e ");
		bundle
				.add("kdeletecharge1",
						"Supprimer un \u00E9lectron de la mol\u00E9cule<br>(augmenter la charge)");
		bundle
				.add("kdeletecharge2",
						"La charge de la mol\u00E9cule a \u00E9t\u00E9 incr\u00E9ment\u00E9e");
		bundle.add("krenum1", "Renum\u00E9roter les atomes");
		bundle
				.add("krenum2",
						"Cliquer sur les atomes pour modifier leur num\u00E9ro d'ordre");
		bundle.add("kmove1", "D\u00E9placer un atome<br>ou la mol\u00E9cule");
		bundle
				.add(
						"kmove2",
						"Cliquer-glisser le pointeur de la souris pour d\u00E9placer un atome ou la mol\u00E9cule");
		bundle.add("krotate1", "Tourner la mol\u00E9cule");
		bundle
				.add("krotate2",
						"Cliquer-glisser le pointeur de la souris pour faire tourner la mol\u00E9cule");
		bundle.add("kcenter1","Centrer la mol\u00E9cule");
		bundle.add("kcenter2","La mol\u00E9cule a ete centr\u00E9e.");
		bundle.add("koptimize1","Optimiser la g\u00E9om\u00E9trie");
		bundle.add("koptimize2","La g\u00E9om\u00E9trie a \u00E9t\u00E9 optimis\u009Ee");
		bundle.add("knum1", " Afficher les num\u00E9ros d'ordre des atomes");
		bundle.add("kinfos1", " Afficher les param\u00E8tres de Huckel");
		bundle.add("kcharges1", "Afficher des charges atomiques");
		bundle.add("kresults1", "Afficher les r\u00E9sultats (texte)");
		bundle
				.add("keraseall1",
						"Tout suppimer<br>(structure d\u00E9localis\u00E9e et m\u00E9som\u00E9rie)");
		bundle.add("kquit1", "Quitter l'application");
		bundle.add("kcreate11", "Cr\u00E9er une structure de Lewis");
		bundle.add("kcreate12",
				"La structure de Lewis a \u00E9t\u00E9 cr\u00E9\u00E9e.");
		bundle
				.add(
						"kgen1",
						" Cliquer ici pour g\u00E9n\u00E9rer automatiquement<br>les structures de Lewis");
		bundle.add("kgen2", "G\u00E9n\u00E9ration des structures de Lewis ...");
		bundle
				.add("kgen3",
						"Les structures de Lewis ont \u00E9t\u00E9 g\u00E9n\u00E9r\u00E9es");
		bundle.add("kcouple1", "Coupler deux \u00E9lectrons c\u00E9libataires");
		bundle
				.add(
						"kcouple2",
						"Glisser-d\u00E9placer entre deux atomes pour coupler les \u00E9lectrons c\u00E9libataires");
		bundle.add ("kbloc1", "Cr\u00E9er un bloc d'atomes et de liaisons");
		bundle.add ("kbloc2", "Cliquer sur les atomes ou les liaisons pour cr\u00E9er un bloc ou sur [n] pour modifier ou supprimer un bloc existant. Cliquer sur le bouton 'fin bloc' pour terminer.");
		bundle.add ("kendbloc1", "Terminer le bloc");
		bundle.add ("kendbloc2", "Bloc d\u00E9fini.");
		bundle.add ("kTitleMsgboxEndBloc","Bloc");
		bundle.add ("kMessMsgboxEndBloc", "Nombre d'électrons dans le bloc :");
		bundle.add ("kMessNoModificationBloc", "Aucune modification n'a \u00E9t\u00E9 effectu\u00E9e.");
		bundle.add ("kMessAutoDeleteBloc", "Le bloc vide a \u00E9t\u00E9  supprim\u00E9.");
		bundle.add ("kMessAnotherBloc", "Merci de terminer d''abord le bloc dans {0}.");

		bundle.add("kresults2", "Afficher les r\u00E9sultats (texte)");
		bundle.add("kerase11", "Supprimer<br>la structure de Lewis courante");
		bundle
				.add("kerase12",
						"La structure de lewis courante a \u00E9t\u00E9 supprim\u00E9e");
		bundle.add("kerasem1", "Supprimer toutes<br>les structures de Lewis");
		bundle.add("kerasem2",
				"Les structures de Lewis ont \u00E9t\u00E9 supprim\u00E9es");
		bundle.add("kchangebonds1", "Ajouter des \u00E9lectrons ");
		bundle
				.add("kchangebonds2",
						"Cliquer sur une liaison ou un atome pour y ajouter des \u00E9lectrons");
		bundle.add("kerrgen2",
				"D\u00E9passement de capacit\u00E9. Structure trop complexe ");
		bundle.add("kerrgen",
				"Une erreur est survenue dans l'autog\u00E9n\u00E9ration");
		bundle.add("kerrtitle", "Erreur");
		bundle.add("removeHAtom",
				"Vous ne pouvez pas supprimer un atome d'hydrog\u00E8ne");
		bundle.add("removeHBond",
				"Vous ne pouvez pas supprimer une liaison hydrog\u00E8ne");
		bundle.add("kstatusnoaction", "S\u00E9lectionnez une commande.");
		bundle.add("kerrorseqnum",
				"Vous devez num\u00E9roter les atomes correctement");
		bundle
				.add("kinaccess",
						"Cette option est inaccessible dans la version en ligne de HuLiS");
		bundle.add("krenamefile", "Le fichier sera nomm\u00E9");
		bundle.add("kfilenotfound", "Fichier non trouv\u00E9");
		bundle.add("kinitialorientation", "Voulez-vous conserver l'orientation initiale de la mol\u00E9cule dans le fichier ?");
		bundle.add("knotavalaible", "Non valide");
		MyResourceBundle.addBundle("ActionsApp_fr", bundle);

		// Menu		
		bundle = new MyResourceBundle();
		bundle
		.add("mdefaultlocale",francais );

		bundle
		.add("mnfile","Fichier");
		
		bundle
		.add("mnopen","Ouvrir...");

		bundle.add("mnsave", "Enregistrer sous...");

		bundle.add("mnPreferences", "Pr\u00E9f\u00E9rences...");
		
		bundle.add("mnquit",
				"Quitter");
		
		bundle.add("mnedit",	"Edition");

		bundle.add("mnundo", "Annuler");

		bundle.add("mnredo", "Refaire");

		bundle.add("mnpaste","Coller");

		bundle.add("mndisplay", "Affichage");

		bundle.add("mnexp",
				"Mode expert");
		
		bundle.add("mnoptgeom","Optimiser la g\u00E9ometrie");
		
		bundle.add("mnmanipspin","Manip Spin");

		bundle.add("mnmanipspinCO","Switch singulet CO <-> CF");

		bundle.add("mnpolynomes","Polyn\u00F4mes");

		bundle.add("mnExcitations","Liste des excitations...");
		bundle.add("mnBlocs","Liste des blocs...");

		bundle.add("mnzoomd",
				"Zoom par d\u00E9faut");

		bundle.add("mnzoomp",
				"Zoom +");

		bundle.add("mnzoomm",
				"Zoom -");

		bundle.add("mncompute","Calcul");

		bundle.add("mnaddpolynome","Ajouter au calcul polynomial");


		bundle.add("mntutorials", "Tutoriels");

		bundle.add("mntuto0",
				"Tutoriel 0 : Construire une mol\u00E9cule");

		bundle.add ("mnhelp", "Aide");

		bundle.add("mnguide",
			"Guide d'utilisation...");
		bundle.add("mnlog","Journal d'\u00E9v\u00E9nements...");
		bundle.add("mnabout",
				"A propos de HuLiS...");
		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 12));
		bundle.add("mnfontAr", new Font("Helvetica", Font.PLAIN, 16));
	
		MyResourceBundle.addBundle("Menu_fr", bundle);

		// MenuPop
		bundle = new MyResourceBundle();

		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 12));

		bundle.add("mnpopcut", "Couper");
		bundle.add("mnpopcopy", "Copier");
		bundle.add("mnpoppaste", "Coller");
		bundle.add("mnpopselall", "S\u00E9lectionner tout");
	
		MyResourceBundle.addBundle("MenuPop_fr", bundle);

	
		// About
		bundle = new MyResourceBundle();
		bundle
				.add(
						"kheadhtml",
						"<head><style type=\"text/css\">.couleur {font-family:Helvetica;font-size:10px; color: #000000;} .starterror {font-family:Helvetica;font-size:10px; color: #FF0000;}</style></head>");
		bundle.add("kimgpath", "img/hulis.gif");
		bundle.add("kjdk", "JDK");
		bundle.add("kauthors", "Auteurs :");
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);

		bundle
				.add(
						"kcopyright",
						"(c) Copyright HuLiS contributors 2007. Tout droits r\u00E9serv\u00E9s.<br>Visitez le site <a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a>");
		bundle
				.add(
						"kwarmings",
						"<span class=\"couleur\">Merci de faire vos remarques sur ce logiciel &agrave; </span><a href=\"mailto:stephane.humbel@univ-amu.fr\" target=\"new\">stephane.humbel@univ-amu.fr</a><span class=\"couleur\">.</span>");
		bundle.add("kabouttitle", "A propos de HuLiS");
		bundle.add("kaboutWidth", 400);
		bundle.add("kaboutHeight", 420);
		bundle.add("ksplashWidth", 350);
		bundle.add("ksplashHeight", 410);
		bundle.add("dateformat", "dd/MM/yyyy");
		bundle.add("limit", "Cette version est valide jusqu'au : ");
		bundle.add("countrydate", "dd/MM/yyyy");
		bundle
				.add(
						"avalaibleversion",
						"Une nouvelle version est disponible sur <a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a> : v");
		bundle.add("avalaibleuntil", "Valable jusqu'au :");

		bundle.add("kwarning", "Avertissement");
		bundle
				.add("kstarterror2",
						"<span class=\"starterror\">Impossible de d\u00E9marrer HuLiS.");
		bundle.add("licencetitle", "Licence d'utilisation");
		bundle.add("creditstitle", "Cr\u00E9dits");
		bundle.add("licence", "Licence d'utilisation...");
		bundle.add("credits", "Cr\u00E9dits...");
		bundle.add("close", "Fermer");
		bundle.add("creditsfile", this.getClass().getClassLoader().getResource("files/credits_fr.html"));
		MyResourceBundle.addBundle("About_fr", bundle);

		// StatusBar
		bundle = new MyResourceBundle();
		bundle.add("font",  new Font("Helvetica", Font.PLAIN, 12));
		MyResourceBundle.addBundle("StatusBar_fr", bundle);
		
		// LicenseFrame
		bundle = new MyResourceBundle();
		bundle.add("texthead", "HuLiS est rendu librement disponible par des subventions qui nous amènent à collecter une quantité minimale d’<a href=\"class:views.Informations\">informations</a> anonymes pour pouvoir renseigner le taux d’utilisation et adapter au mieux le service aux besoins de nos utilisateurs. Vous pouvez désactiver l’envoi d’informations par le menu Fichiers / Préférences / Onglet \"vie privée \". En cliquant sur \"Accepter et continuer\" vous acceptez la licence d’utilisation de HuLiS, ci-dessous :");		
		bundle.add("title", "Licence d'utilisation");
		bundle.add("accept", "Accepter et continuer");
		bundle.add("quit", "Quitter HuLiS");
		bundle.add("license", this.getClass().getClassLoader().getResource("files/license_fr.html"));
		MyResourceBundle.addBundle("LicenseFrame_fr", bundle);

		// ErrorReport
		bundle = new MyResourceBundle();
		bundle.add("font2",  new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("title", "Journal d'événements");
		bundle.add("kquit", "Quitter");
		bundle.add("kclose", "Fermer");
		bundle.add("kcopy", "Copier");
		bundle
				.add(
						"kerrtxt",
						"Une erreur critique est survenue. Merci de copier le rapport ci-dessous et de l'envoyer \u00E0 <a href=\"mailto:www.hulis@free.fr\" target=\"new\">www.hulis@free.fr</a>");
		
		
		MyResourceBundle.addBundle("ErrorReport_fr", bundle);

		// Informations
		bundle = new MyResourceBundle() ;
		bundle.add("informations", "Informations");		
		bundle.add("message", "Les informations ci-dessous seront envoy\u00E9es \u00E0 HuLiS.");		
		bundle.add("nb_use", "Nombre d'utilisations mondial : ");		
		bundle.add("country", "Pays : ");		
		bundle.add("type_of_use", "Type d'utilisation : ");		
		bundle.add("total_use", "Nombre total d'utilisations : ");		
		bundle.add("java_lang", "Langue de Java : ");		
		bundle.add("java_version", "Version de Java : ");		
		bundle.add("os", "Syst\u00E8me d’exploitation : ");		
		bundle.add("browser", "Navigateur : ");		
		bundle.add("hulis_version", "Version de HuLiS : ");		
		bundle.add("start_time", "Temps de démarrage (ms) : ");		
		bundle.add("date_applet", "Date et heure d’utilisation (horodatage) : ");		
		bundle.add("duration", "Dur\u00E9e d'utilisation (ms) : ");		
		bundle.add("error", "Erreur : ");		
		bundle.add("last_command", "Derni\u00E8re commande utilis\u00E9e : ");		
		bundle.add("preferences_path", "Chemin du fichier pr\u00E9f\u00E9rences : ");		
		MyResourceBundle.addBundle("informations_fr", bundle);
	
	}



	
	/***************************************************************************
	 * 
	 * Italian language
	 * 
	 **************************************************************************/

	

	public void initItalian() {
		//Threads
		MyResourceBundle bundle = new MyResourceBundle();
		bundle.add("stopComputeWeights", "The computation of the weights of Lewis structures is being stopped...");
		bundle.add("stopAutogen", "The generation of the Lewis structures is being stopped...");
		MyResourceBundle.addBundle("Threads_it", bundle);
		
		// ToolBarMesomery
		bundle = new MyResourceBundle();
		bundle.add("compute", "compute");
		bundle
				.add(
						"kmesomery",
						"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Lewis<br>  <span class=\"Style2\">Mesomeria</span></p></body></html>");
		bundle.add("kcreate1", "Costruire");
		bundle.add("kgenerateall", "Generare tutto");
		bundle.add("kcouple", "Accoppiare");
		bundle.add ("kbloc", "Block");
		bundle.add ("kendbloc", "End block");
		bundle.add("kchangebonds", "Legame / El."); // \u25cf
		bundle.add("kresults", "Risultati");
		bundle.add("kerase1", "Elimina. 1");
		bundle.add("kerasem", "Elimina mes.");
		bundle.add("kenergy", "E");
		bundle.add("phi", "\u03C8");
		bundle.add("kCI", "CI");
		bundle.add("kincomplete", "incompleto");
		bundle.add("exist", "duplicato");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11)); // taille des caracteres sur les boutons
		MyResourceBundle.addBundle("ToolBarMesomery_it", bundle);

		// ToolBarHuckel

		bundle = new MyResourceBundle();
		bundle.add("khuckel",
						"<html><head><style type=\"text/css\">.Style1 {	font-size: 20px;font-family: Helvetica, sans-serif;}.Style2 {font-size: 12px;font-family: Helvetica, sans-serif;}</style></head><body><p align=\"center\" class=\"Style1\">Huckel<br><span class=\"Style2\">&nbsp;</style></p></body></html>");
		bundle.add("kbuild", "Costruire");
		bundle.add("kchange", "Modificare");
		bundle.add("kdelete", "Gomma");
		bundle.add("kmove", "Spostare");
		bundle.add("krotate", "Ruotare");
		bundle.add("koptimize", "Opt.");
		bundle.add("kimgmove", "img/move.png");
		bundle.add("kimgrotate", "img/rotate.png");
		bundle.add("kimgcenter", "img/center.png");
		bundle.add("krenum", "Riordinare");
		bundle.add("kinfos", "Parametri");
		bundle.add("kcharges", "Cariche");
		bundle.add("knum", "Numerare");
		bundle.add("kresults", "Risultati");
		bundle.add("keraseall", "Cancellare");
		bundle.add("kquit", "Uscire");
		bundle.add("ktotalcharge", "Carica totale = {0}");
		bundle.add("kenergy", "E");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11) );
		MyResourceBundle.addBundle("ToolBarHuckel_it", bundle);

		
		// StructureView
		bundle = new MyResourceBundle();

		bundle.add("kwarning", "Avertissement");
		bundle.add("kWarNegCharge",
		"Il parametraggio degli anioni non è ottimale");

		bundle.add("kverticalspacebetweenlines", 12);
		bundle.add("kendbloc", "Please, terminate the block in {0}");
		bundle.add("kselectbloc", "Select an existing bloc or create a new one");

		bundle.add("tooMuchSingleElec", "Ammessi solo due elettroni spaiati");
		bundle.add("knetcharge", "Carica netta");
		bundle.add("kenergy", "E");
		bundle
				.add("kelecerror",
						"Completare la struttura di Lewis mettendo");
		bundle
		.add("kelecerrorb",
				"gli elettroni su o tra gli atomi.");
		bundle.add("kelecerror1",
				"Elettroni rimasti: {0}");
		bundle.add("kelecerror2",
				"Elettrone(i) da eliminare: {0}");
		bundle
				.add("kelecerror4",
						"Questa struttura è già presente");
		bundle
		.add("kelecerror4b",
				"nella mesomeria: ");
		bundle.add("kErrorRedundance",  "Redundance detected in the structures");
		bundle.add("methoderror","Cannot compute {0} : {1}");

		bundle.add("font",  new Font("Helvetica", Font.BOLD, 11));
		MyResourceBundle.addBundle("StructureView_it", bundle);

		// SkelMouseController
		bundle = new MyResourceBundle();
		bundle
				.add("ktoomanyneighbours2",
						"Non è possibile creare un legame. L'atomo ha troppi primi vicini.");
		bundle.add("removeHAtom",
				"Non è possibile cancellare un atomo d'idrogeno");
		bundle.add("removeHBond",
				"Non è possibile cancellare un legame idrogeno");
		bundle.add("changeHBond",
				"Non è possibile modificare un legame idrogeno");
		bundle
				.add("HRadR",
						"Non è possibile cambiare il numero di radicali su un atomo d'idrogeno");
		MyResourceBundle.addBundle("SkelMouseController_it", bundle);

		// Preferences
		bundle = new MyResourceBundle();
		bundle.add("title", "Preferences");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Cancel");
		bundle.add("kdecimal", "Digits : ");
		bundle.add("kprecisionMO", "<html>MO, charge, density,<br>overlap, weigths, coefficients :</html>");
		bundle.add("kprecisionEnergy", "Energies \u03B2, hamiltionians : ");
		bundle.add("kMOratio", "Zoom MO : ");
		bundle.add("prefAutoGen", "Automatic generation of Lewis structures :");
		bundle.add("Nsep", "Max charge : ");
		bundle.add("kAtomDisplay", "Atoms representation :");
		bundle.add("kcircle", "Circles");
		bundle.add("ksymbols", "Symbols");
		bundle.add("kautocouple", "Allow bi-radicals");
		bundle.add("kautolongdistancecouple", "AutoGen couple ld");
		bundle.add("kautosep", "AutoGen sep");
		bundle.add("kautolongdistancesep", "AutoGen sep ld");
		bundle.add("ktitle", "Preferences");
		bundle.add("uptodate", "HuLiS is up to date");
		bundle.add("checkVersion", "Automatically check for updates");
		bundle.add("updates", "Updates");
		bundle.add("sendInformation", "<html>HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs.</html>");
		bundle.add("chkSendInformation", "Allow sending of informations to HuLiS");
		bundle.add("privacy", "Privacy");
		MyResourceBundle.addBundle("Preferences_it", bundle);
		
		// JOptionPane
		bundle = new MyResourceBundle();
		bundle.add("Yes", "Sì");
		bundle.add("No", "No");
		bundle.add("Cancel", "Annullare");
		MyResourceBundle.addBundle("JOptionPane_it", bundle);

		// FrameResultStruct
		bundle = new MyResourceBundle();
		bundle.add("pisymbol", "\u03c0");
		bundle
				.add(
						"resh0",
						"La struttura è vuota. Utilizzare il bottone \"Costruire\" nel pannello a sinistra per definire una struttura molecolare a base di carbonio (cliccare e glissare) e il bottone \"Modificare\" per modificare il tipo di atomo (parametri degli atomi e dei legami).");
		bundle.add("resh1", "Tipo di struttura: ");
		bundle.add("resh2", "Numero d'atomi: ");
		bundle.add("resh2a", "Numero di atomi di H\u00fcckel: ");
		bundle.add("resh3", "Numero di elettroni: ");
		bundle.add("resh4", "Carica totale: ");
		bundle.add("resh5", "1- Hamiltoniano:");
		bundle.add("resh6", "2- Orbitali: ");
		bundle.add("resh7", "\u025Bi :  ");
		bundle.add("resh8", "Energia totale: ");
		bundle
				.add(
						"resh9",
						"3- Matrice densità elettronica: quantità di elettroni sugli atomi e fra gli atomi (le densità tra gli atomi non direttamente legati non deve essere utilizzata): ");
		bundle.add("resh10", "4- Cariche atomiche:");
		bundle.add("resh11", "Carica totale: ");
		bundle.add("resh12", "ni :");
		bundle.add("resh13", "Atome\tCharge :");
		bundle.add("resh131", "<b>Atome</b></td><td><b>Charge</b>");
		bundle.add("ktitleresultstruct", "Risultati Huckel");
		bundle.add("kdeloc", "struttura delocalizzata");
		bundle.add("kloc", "struttura localizzata");
		bundle.add("methoderror","Cannot compute {0} method : {1}");
		bundle.add("methoderrorRedundance",	"Cannot compute HLP : Redundance detected in the structures");
		MyResourceBundle.addBundle("FrameResultStruct_it", bundle);

		// FrameResultMesomery
		bundle = new MyResourceBundle();
		bundle.add("phitot", "\u03C8tot");
		bundle.add("resmesnbgen", "Numero di strutture di Lewis:");
		bundle.add("resmesnbdisp", "Numero di strutture di Lewis attive:");
		bundle
				.add("resmes1",
						"Calcolo della mesomeria per interazione di configurazione");
		bundle.add("resmes2", "Matrice coeff B utilizzata:");
		bundle.add("resmes3", "Prima matrice C:");
		bundle.add("resmes4", "Valori propri An:");
		bundle.add("resmes5", "Valore positivo unico Bn = -(1 / (An - 1) )");
		bundle.add("resmes6", "Bn positivo:");
		bundle.add("resmes7",
				"1- Interazione di configurazione:");
		bundle.add("hlpOverlap", "    Overlap HLP matrix :" );

		bundle
				.add(
						"resmes10",
						"2- Vettori propri (Ci) e pesi (Wi) della mesomeria :");
		bundle.add("resmes11", "\u03C8tot = C1.\u03C81 + C2.\u03C82 ...");
		bundle.add("knbostring", "3- NBO String for gaussian :");
		bundle
				.add(
						"resmes111",
						"\u03C8<sub>tot</sub> = C<sub>1</sub>.\u03C8<sub>1</sub> + C<sub>2</sub>.\u03C8<sub>2</sub> ...");
		bundle
				.add(
						"nothingmesomery",
						"Costruire les strutture di Lewis con i bottoni \"Costruire\" e \"Elettroni\".");
		bundle
				.add(
						"kelecerror",
						"{0} : Il numero di elettroni non è corretto: è indicato {1} invece di {2}");
		bundle.add("tooMuchSingleElec", "Non sono ammessi più di due elettroni spaiati");
		bundle.add("kelecerror3", "Errore nel calcolo degli orbitali");
		bundle.add("kelecerror4", "dupliqu\u00E9e");
		bundle.add("ktitleresultsmesomery",
				"HuLiS - Risultati della mesomeria");
		bundle
				.add("waitweights",
						"L'autogenerazione delle strutture di Lewis è in corso...");
		bundle.add(	"waitweights2",	"The compute of weights of Lewis structures is running... please wait...");
		bundle.add("methoderror","Cannot compute {0} method : {1}");
		bundle.add("methods",	"Method");
		bundle.add("tau",	"Trust factor\t");
		bundle.add("methoderrorRedundance",	"HLP method cannot be computed : Redundance detected in the structures");
		bundle.add("publications",	"Please, cite this work :\nH\u00FCckel-Lewis-Projection Method: A \"Weights Watcher\" for Mesomeric Structures. Y. Carissan, D. Hagebaum-Reignier, N. Goudard, S. Humbel J. Phys. Chem. A. 2008, 118 (50), 13256–13262.\nH\u00FCckel Theory for Lewis Structures: H\u00FCckel-Lewis Configuration Interaction (HL-CI). D. Hagebaum-Reignier, R. Girardi, Y. Carissan, S. Humbel J. Mol. Struct. (THEOCHEM) 2007, 817, 99-109.");
		MyResourceBundle.addBundle("FrameResultMesomery_it", bundle);

		// FrameResult
		bundle = new MyResourceBundle();
		bundle.add("kWarNegCharge",
		"Avertissement : Il parametraggio degli anioni non è ottimale");
		bundle.add("kclose", "Chiudere");
		bundle.add("krefresh", "Aggiornare");
		bundle.add("kbttxt", "texte");
		bundle.add("kbthtml", "html");
		bundle.add("ktitleresults", "HuLiS - Risultati");
		bundle.add("font", new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("fontsize2", "12");
		bundle.add("align1", StyleConstants.ALIGN_LEFT);
		bundle.add("align2", StyleConstants.ALIGN_LEFT);
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);
		MyResourceBundle.addBundle("FrameResult_it", bundle);

		// FrameApp
		bundle = new MyResourceBundle();
		bundle.add("kframew", 660);
		bundle.add("kframeh", 520);
		bundle.add("kpanelmolw", 440);
		bundle.add("ktoolbarw", 110);
		bundle.add("kstatusbarh", 25);
		bundle.add("ktoolbarmw", 110);
		bundle.add("kchangelang",
				"Lingua italiana");
		MyResourceBundle.addBundle("FrameApp_it", bundle);

	
		// DialogChangeAtom
		bundle = new MyResourceBundle();
		bundle.add("cannotReduceValence", "Cannot reduce valence");
		bundle.add("kTitleAtomType", "Modificare l'atomo");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Annullare");
		bundle.add("kdefault", "Per default");
		bundle
				.add(
						"ktoomanyneighbours",
						"Non è possibile cambiare il tipo di atomo. Eliminare gli atomi primi vicini in eccesso.");
		bundle
		.add(
				"katomnull",
				"Il tipo di atomo deve essere noto.");
		bundle.add("knumeric", "hX deve assumere un valore numerico");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeAtom_it", bundle);

		// DialogChangeHuckelBond
		bundle = new MyResourceBundle();
		bundle.add("kTitleChangeBonds", "Modificare il legame");
		bundle.add("kOK", "OK");
		bundle.add("kundo", "Annullare");
		bundle.add("kdefault", "Per default");
		bundle.add("knumeric", "hXY deve assumere un valore numerico");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11));
		MyResourceBundle.addBundle("DialogChangeHuckelBond_it", bundle);

		// DialogChangeBloc
		bundle = new MyResourceBundle();
		bundle.add("kchoix1", "Modify the selected bloc");
		bundle.add("kchoix2", "Delete the selected bloc");
		bundle.add("kchoix3", "Modify the number of electrons");
		bundle.add("kOK", "Ok");
		bundle.add("kundo", "Cancel");
		bundle.add("font1", new Font("Helvetica", Font.PLAIN, 11)); 
		MyResourceBundle.addBundle("DialogChangeBloc_it", bundle);

		// ActionsApp
		bundle = new MyResourceBundle();
		bundle.add("kbuild1", "Creare atomi e legami");
		bundle.add("kbuild2",
				"Creare dei nuovi atomi cliccando e cliccando-spostando nella finestra centrale");
		bundle.add("kchange1",
				"Modificare i parametri degli atomi o dei legami");
		bundle.add("kchange2",
						"Cliccare sugli atomi o sui legami per modificarne i parametri");
		bundle.add("kdelete2",
				"Cliccare sull'atomo o sul legame da cancellare");
		bundle.add("kdelete1", "Eliminare degli atomi o dei legami");
		bundle.add(
						"kaddcharge1",
						"Aggiungere un elettrone sulla molecola<br>(ridurre la carica)");
		bundle.add("kaddcharge2",
						"La carica sulla molecola è stata ridotta");
		bundle.add("kdeletecharge1",
						"Eliminare un elettrone dalla molecola<br>(aumentare la carica)");
		bundle.add("kdeletecharge2",
						"La carica sulla molecola è aumentata");
		bundle.add("krenum1", "Riordinare gli atomi");
		bundle.add("krenum2",
						"Cliccare sugli atomi per modificarne il numero associato");
		bundle.add("kmove1", "Spostare un atomo<br>o la molecola");
		bundle.add(
						"kmove2",
						"Cliccare-spostare la freccia del mouse per spostare un atomo o la molecola");
		bundle.add("krotate1", "Ruotare la molecola");
		bundle.add("krotate2",
						"Cliccare-spostare la freccia del mouse per ruotare la molecola");
		bundle.add("kcenter1","Center the molecule in the panel");
		bundle.add("kcenter2","The molecule has been centered");
		bundle.add("koptimize1","Optimize Geometry");
		bundle.add("koptimize2","The geometry has been optimized");
		bundle.add("knum1", " Mostrare i numeri associati agli atomi");
		bundle.add("kinfos1", " Mostrare i parametri Huckel");
		bundle.add("kcharges1", "Mostrare le cariche atomiche");
		bundle.add("kresults1", "Mostrare i risultati (testo)");
		bundle.add("keraseall1",
						"Cancellare tutto<br>(struttura delocalizzata e mesomeria)");
		bundle.add("kquit1", "Chiudere l'applicazione");
		bundle.add("kcreate11", "Costruire una struttura di Lewis");
		bundle.add("kcreate12",
				"La struttura di Lewis è stata costruita.");
		bundle.add(
						"kgen1",
						"Cliccare qui per generare automaticamente<br>le strutture di Lewis");
		bundle.add("kgen2", "Generazione delle strutture di Lewis...");
		bundle.add("kgen3",
						"Le strutture di Lewis sono state generate");
		bundle.add("kcouple1", "Accoppiare due elettroni spaiati");
		bundle.add(
						"kcouple2",
						"Glissare-spostare due atomi<br>per accoppiare gli elettroni spaiati");
		bundle.add ("kbloc1", "Create a block of atoms and bonds");
		bundle.add ("kbloc2", "Click on atoms and bonds to create a new block or on [n] to modify or delete an existing block. Click on 'End block' button to finish.");
		bundle.add ("kendbloc1", "Terminate the block");
		bundle.add ("kendbloc2", "Block terminated.");
		bundle.add ("kTitleMsgboxEndBloc","Block");
		bundle.add ("kMessMsgboxEndBloc", "Number of electrons in the block :");
		bundle.add ("kMessNoModificationBloc", "No modification has been done.");
		bundle.add ("kMessAutoDeleteBloc", "The empty block has been deleted.");
		bundle.add ("kMessAnotherBloc", "Please, first terminate the bloc in {0}.");

		bundle.add("kresults2", "Mostare i risultati (testo)");
		bundle.add("kerase11", "Cancellare<br>la struttura di Lewis attuale");
		bundle.add("kerase12",
						"La struttura di Lewis è stata cancellata");
		bundle.add("kerasem1", "Cancellare tutte<br>le strutture di Lewis");
		bundle.add("kerasem2",
				"Le strutture di Lewis sono state cancellate");
		bundle.add("kchangebonds1", "Aggiungere degli elettroni");
		bundle.add("kchangebonds2",
						"Cliccare su un legame o su un atomo per aggiungere degli elettroni");
		bundle.add("kerrgen2",
				"Superamento delle capacità. Struttura troppo complessa");
		bundle.add("kerrgen",
				"Un errore è sopravvenuto nella procedura di autogenerazione");
		bundle.add("kerrtitle", "Errore");
		bundle.add("removeHAtom",
				"L'atomo d'idrogeno non puo` essere rimosso");
		bundle.add("removeHBond",
				"Il legame idrogeno non puo` essere cancellato");
		bundle.add("kstatusnoaction", "Selezionare un'operazione.");
		bundle.add("kerrorseqnum",
				"Gli atomi devono essere numerati correttamente");
		bundle.add("kinaccess",
						"Questa opzione non è disponibile nella versione in linea di HuLiS");
		bundle.add("krenamefile", "The file will be named");	
		bundle.add("kfilenotfound", "File not found");
		bundle.add("kinitialorientation", "Do you want to save the file with the initial structure orientation ?");
		bundle.add("knotavalaible", "Non valido");
		MyResourceBundle.addBundle("ActionsApp_it", bundle);

		// Menu
		
		bundle = new MyResourceBundle();
		bundle
		.add("mdefaultlocale",italian );

		bundle
		.add("mnfile","File");
		
		bundle
		.add("mnopen","Aprire...");

		bundle.add("mnsave", "Salvare in...");

		bundle.add("mnPreferences", "Opzioni...");

		
		bundle.add("mnquit",
				"Uscire");
		
		bundle.add("mnedit",	"Edizione");

		
		bundle.add("mnundo", "Annullare");
	

		bundle.add("mnredo", "Rifare");
	
		bundle.add("mnpaste","Incollare");


		bundle.add("mndisplay", "Mostra");

		bundle.add("mnexp",
				"Modo esperto");
		bundle.add("mnoptgeom","Optimize Geometry");
		
		bundle.add("mnmanipspin","Manip Spin");
		bundle.add("mnmanipspinCO","Switch singulet CO <-> CF");
		bundle.add("mnpolynomes","Polynomes");
		bundle.add("mnExcitations","Show excitations list...");
		bundle.add("mnBlocs","Show blocs list...");

		bundle.add("mnzoomd",
				"Zoom default");

		bundle.add("mnzoomp",
				"Zoom +");

		bundle.add("mnzoomm",
				"Zoom -");

		bundle.add("mncompute","Compute");

		bundle.add("mnaddpolynome","Add to polynome calculation");
	
		bundle.add("mntutorials", "Tutoriale");

		bundle.add("mntuto0",
				"Tutoriale 0 : Construire una molecola");

		bundle.add ("mnhelp", "Aiuto");

		bundle.add("mnguide",
			"Manuale d'uso...");
		bundle.add("mnlog","Event log...");
		bundle.add("mnabout",
				"A proposito di HuLiS...");
		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 12));
		bundle.add("mnfontAr", new Font("Helvetica", Font.PLAIN, 16));
	
		MyResourceBundle.addBundle("Menu_it", bundle);

		// MenuPop
		bundle = new MyResourceBundle();

		bundle.add("mnfont", new Font("Helvetica", Font.PLAIN, 12));

		bundle.add("mnpopcut", "Taglia");
		bundle.add("mnpopcopy", "Copia");
		bundle.add("mnpoppaste", "Incolla");
		bundle.add("mnpopselall", "Seleziona tutto");
		

		MyResourceBundle.addBundle("MenuPop_it", bundle);

	
		// About
		bundle = new MyResourceBundle();
		bundle.add(
						"kheadhtml",
						"<head><style type=\"text/css\">.couleur {font-family:Helvetica;font-size:10px; color: #000000;} .starterror {font-family:Helvetica;font-size:10px; color: #FF0000;}</style></head>");
		bundle.add("kimgpath", "img/hulis.gif");
		bundle.add("kjdk", "JDK");
		bundle.add("kauthors", "Auteurs :");
		bundle.add("orientation", ComponentOrientation.LEFT_TO_RIGHT);

		bundle.add(
						"kcopyright",
						"(c) Copyright HuLiS contributors 2007. Tutti i diritti riservati.<br>Visitate il sito <a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a>");
		bundle
				.add(
						"kwarmings",
						"<span class=\"couleur\">Vi ringraziamo per eventuali consigli su questo programma&agrave; </span><a href=\"mailto:stephane.humbel@univ-amu.fr\" target=\"new\">stephane.humbel@univ-amu.fr</a><span class=\"couleur\">.</span>");
		bundle.add("kabouttitle", "A proposito di HuLiS");
		bundle.add("kaboutWidth", 400);
		bundle.add("kaboutHeight", 420);
		bundle.add("ksplashWidth", 350);
		bundle.add("ksplashHeight", 410);
		bundle.add("dateformat", "dd/MM/yyyy");
		bundle.add("limit", "Questa versione è valida fino a: ");
		bundle.add("countrydate", "dd/MM/yyyy");
		bundle
				.add(
						"avalaibleversion",
						"Una nuova versione è disponibile su<a href=\"http://ism2.univ-amu.fr/hulis/\" target=\"new\">http://ism2.univ-amu.fr/hulis/</a> : v");
		bundle.add("avalaibleuntil", "Valida fino a:");

		bundle.add("kwarning", "Avertissement");
		bundle.add("licencetitle", "Software agreement");
		bundle.add("creditstitle", "Credits");
		bundle.add("licence", "Software agreement...");
		bundle.add("credits", "Credits...");
		bundle.add("close", "Close");
		bundle.add("creditsfile", this.getClass().getClassLoader().getResource("files/credits_en.html"));
		MyResourceBundle.addBundle("About_it", bundle);

		// StatusBar
		bundle = new MyResourceBundle();
		bundle.add("font",  new Font("Helvetica", Font.PLAIN, 12));
		MyResourceBundle.addBundle("StatusBar_it", bundle);

		// LicenseFrame
		bundle = new MyResourceBundle();
		bundle.add("texthead", "HuLiS is made freely available by grants that require us to collect a small amount of anonymous <a href=\"class:views.Informations\">informations</a> to help document the amount of use of HuLiS, and to fit better with our users needs. This can be desactivated in the menu File / Preferences / Privacy. By clicking on \"Accept and continue\",  you accept the HuLiS end user licence agreement below :");		
		bundle.add("title", "Software Agreement");
		bundle.add("accept", "Accept and continue");
		bundle.add("quit", "Quit HuLiS");
		bundle.add("license", this.getClass().getClassLoader().getResource("files/license_en.html"));
		MyResourceBundle.addBundle("LicenseFrame_it", bundle);

		
		// ErrorReport
		bundle = new MyResourceBundle();
		bundle.add("font2",  new Font("Helvetica", Font.PLAIN, 11));
		bundle.add("title", "Event log");
		bundle.add("kquit", "Uscrire");
		bundle.add("kclose", "Close");
		bundle.add("kcopy", "Copiare");
		bundle
				.add(
						"kerrtxt",
						"Un errore critico è sopravvenuto. Vi ringraziamo di copiare il messaggio seguente e di spedirlo a <a href=\"mailto:www.hulis@free.fr\" target=\"new\">www.hulis@free.fr</a>");
		MyResourceBundle.addBundle("ErrorReport_it", bundle);

		// Informations
		bundle = new MyResourceBundle() ;
		bundle.add("informations", "Information");		
		bundle.add("message", "The information shown below will be sent to HuLiS.");		
		bundle.add("nb_use", "Number of worldwild use : ");		
		bundle.add("country", "Country  : ");		
		bundle.add("type_of_use", "Type of use : ");		
		bundle.add("total_use", "Number of use : ");		
		bundle.add("java_lang", "Java language : ");		
		bundle.add("java_version", "Java version : ");		
		bundle.add("os", "Operating system : ");		
		bundle.add("browser", "Browser : ");		
		bundle.add("hulis_version", "HuLis version : ");		
		bundle.add("start_time", "Start time (ms) : ");		
		bundle.add("date_applet", "Date and time of use (timestamp) : ");		
		bundle.add("duration", "Duration of use (ms) : ");		
		bundle.add("error", "Error  : ");		
		bundle.add("last_command", "Last command : ");		
		bundle.add("preferences_path", "Path  preferences file : ");		
		MyResourceBundle.addBundle("informations_it", bundle);

	}

	
	
	/***************************************************************************
	 * 
	 * Initialize languages
	 * 
	 **************************************************************************/

	public  void initResources() {
		initEnglish();
		initFrench();
		initArabian();
		initItalian();
	}

	/***************************************************************************
	 * 
	 * Remove languages
	 * 
	 **************************************************************************/

	public  void removeResources() {
		MyResourceBundle.removeBundle("ToolBarMesomery");
		MyResourceBundle.removeBundle("ToolBarMesomery_en");
		MyResourceBundle.removeBundle("ToolBarMesomery_fr");
		MyResourceBundle.removeBundle("ToolBarMesomery_ar");
		MyResourceBundle.removeBundle("ToolBarMesomery_it");
		
		MyResourceBundle.removeBundle("ToolBarHuckel");
		MyResourceBundle.removeBundle("ToolBarHuckel_en");
		MyResourceBundle.removeBundle("ToolBarHuckel_fr");
		MyResourceBundle.removeBundle("ToolBarHuckel_ar");
		MyResourceBundle.removeBundle("ToolBarHuckel_it");
		
		MyResourceBundle.removeBundle("StructureView");
		MyResourceBundle.removeBundle("StructureView_en");
		MyResourceBundle.removeBundle("StructureView_fr");
		MyResourceBundle.removeBundle("StructureView_ar");
		MyResourceBundle.removeBundle("StructureView_it");

		MyResourceBundle.removeBundle("SkelMouseController");
		MyResourceBundle.removeBundle("SkelMouseController_en");
		MyResourceBundle.removeBundle("SkelMouseController_fr");
		MyResourceBundle.removeBundle("SkelMouseController_ar");
		MyResourceBundle.removeBundle("SkelMouseController_it");

		MyResourceBundle.removeBundle("JOptionPane");
		MyResourceBundle.removeBundle("JOptionPane_en");
		MyResourceBundle.removeBundle("JOptionPane_fr");
		MyResourceBundle.removeBundle("JOptionPane_ar");
		MyResourceBundle.removeBundle("JOptionPane_it");

		MyResourceBundle.removeBundle("FrameResult");
		MyResourceBundle.removeBundle("FrameResult_en");
		MyResourceBundle.removeBundle("FrameResult_fr");
		MyResourceBundle.removeBundle("FrameResult_ar");
		MyResourceBundle.removeBundle("FrameResult_it");
		
		MyResourceBundle.removeBundle("FrameResultMesomery");
		MyResourceBundle.removeBundle("FrameResultMesomery_en");
		MyResourceBundle.removeBundle("FrameResultMesomery_fr");
		MyResourceBundle.removeBundle("FrameResultMesomery_ar");
		MyResourceBundle.removeBundle("FrameResultMesomery_it");
		
		MyResourceBundle.removeBundle("FrameResultStruct");
		MyResourceBundle.removeBundle("FrameResultStruct_en");
		MyResourceBundle.removeBundle("FrameResultStruct_fr");
		MyResourceBundle.removeBundle("FrameResultStruct_ar");
		MyResourceBundle.removeBundle("FrameResultStruct_it");
		
		MyResourceBundle.removeBundle("FrameResult");
		MyResourceBundle.removeBundle("FrameResult_en");
		MyResourceBundle.removeBundle("FrameResult_fr");
		MyResourceBundle.removeBundle("FrameResult_ar");
		MyResourceBundle.removeBundle("FrameResult_it");
		
		MyResourceBundle.removeBundle("FrameApp");
		MyResourceBundle.removeBundle("FrameApp_en");
		MyResourceBundle.removeBundle("FrameApp_fr");
		MyResourceBundle.removeBundle("FrameApp_ar");
		MyResourceBundle.removeBundle("FrameApp_it");
		
	
		MyResourceBundle.removeBundle("DialogChangeAtom");
		MyResourceBundle.removeBundle("DialogChangeAtom_en");
		MyResourceBundle.removeBundle("DialogChangeAtom_fr");
		MyResourceBundle.removeBundle("DialogChangeAtom_ar");
		MyResourceBundle.removeBundle("DialogChangeAtom_it");
	
		MyResourceBundle.removeBundle("DialogChangeHuckelBond");
		MyResourceBundle.removeBundle("DialogChangeHuckelBond_en");
		MyResourceBundle.removeBundle("DialogChangeHuckelBond_fr");
		MyResourceBundle.removeBundle("DialogChangeHuckelBond_ar");
		MyResourceBundle.removeBundle("DialogChangeHuckelBond_it");

		MyResourceBundle.removeBundle("DialogChangeBloc");
		MyResourceBundle.removeBundle("DialogChangeBloc_en");
		MyResourceBundle.removeBundle("DialogChangeBloc_fr");
		MyResourceBundle.removeBundle("DialogChangeBloc_ar");
		MyResourceBundle.removeBundle("DialogChangeBloc_it");

		
		MyResourceBundle.removeBundle("ActionsApp");
		MyResourceBundle.removeBundle("ActionsApp_en");
		MyResourceBundle.removeBundle("ActionsApp_fr");
		MyResourceBundle.removeBundle("ActionsApp_ar");
		MyResourceBundle.removeBundle("ActionsApp_it");
		
		MyResourceBundle.removeBundle("About");
		MyResourceBundle.removeBundle("About_en");
		MyResourceBundle.removeBundle("About_fr");
		MyResourceBundle.removeBundle("About_ar");
		MyResourceBundle.removeBundle("About_it");

		MyResourceBundle.removeBundle("MenuPop");
		MyResourceBundle.removeBundle("MenuPop_en");
		MyResourceBundle.removeBundle("MenuPop_fr");
		MyResourceBundle.removeBundle("MenuPop_ar");
		MyResourceBundle.removeBundle("MenuPop_it");

		MyResourceBundle.removeBundle("Menu");
		MyResourceBundle.removeBundle("Menu_en");
		MyResourceBundle.removeBundle("Menu_fr");
		MyResourceBundle.removeBundle("Menu_ar");
		MyResourceBundle.removeBundle("Menu_it");
	}



	/***
	 * resource getter
	 */
	
	public  MyResourceBundle get (String name, Locale locale) {
		return MyResourceBundle.getBundle(name, locale);
	
	}

}
