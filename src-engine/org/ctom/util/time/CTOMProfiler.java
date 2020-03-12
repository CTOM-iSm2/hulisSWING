/**
 * 
 */
package org.ctom.util.time;

import java.util.HashMap;
import java.util.Map.Entry;

import org.ctom.util.maths.Maths;

/**
 * @author macbook
 *
 */
public class CTOMProfiler {
	
	private static HashMap<String, Info> listTemps=new HashMap<String, Info>();
	private static CTOMProfiler instance=null;
	
	/**
	 * renvoie l'instance unique du profiler
	 * @return
	 */
	public static CTOMProfiler getInstance() {
		if (instance==null) {
			instance = new CTOMProfiler();
		}
		return instance;
	}
	
	/**
	 * A appeler au debut d'une methode
	 * @param nom de la methode
	 */
	public void debut(String str) {
		Info info;
		if (!listTemps.containsKey(str)) {
			info = new Info();
			listTemps.put(str, info);
		} else {
			info = listTemps.get(str);
		}
		info.setDebut();
	}
	
	/**
	 * A appeler a la fin d'une methode
	 * @param nom de la methode
	 */
	public void fin(String str) {
		Info info = listTemps.get(str);
		info.setFin();
	}
	
	/**
	 * renvoie la liste des appels sous la forme d'une hashmap
	 * @return
	 */
	public HashMap<String, Info> getTimings() {
		return (HashMap<String, Info>) listTemps.clone();
	}
	
	/**
	 * Affiche la liste des appels dans la sortie standard
	 */
	public String toHTML(){
		String buffer ="";
		HashMap<String, Info> map = this.getTimings();
		buffer += "<table>";
		buffer += "<tr><td>nom methode</td><td>nb appels</td><td>tot duree(ms)</td><td>moy duree(ms)</td><td> % duree</td></tr>";
		long dureeTotale=0;
	    for(Entry<String, Info> entry : map.entrySet()) {
			Info info = entry.getValue();
		    dureeTotale += info.getTempsTotal();
		}
		for(Entry<String, Info> entry : map.entrySet()) {
			String routine = entry.getKey();
			Info info = entry.getValue();
			buffer += "<tr><td>"  + routine +  "</td><td class=\"text-align:right\">" + info.getNbAppels() + "</td><td class=\"text-align:right\">" + info.getTempsTotal() + "</td><td class=\"text-align:right\">" + Maths.round(info.getTempsTotal()/info.getNbAppels(),1)
					+ "</td><td class=\"text-align:right\">" +  Maths.round(((double) info.getTempsTotal()/ (double) dureeTotale) * 100,2)  + "</td></tr>";
		}
		buffer += "</table>";
		
		return buffer;
	}

	protected class Info {
		private int nbreAppel=0;
		private long tempsDebut;
		private long tempsFin; // pas necessaire
		private long tempsTotal=0;
		
		void ajoutAppel() {
			nbreAppel++;
		}
		
		void setDebut() {
			ajoutAppel();
			tempsDebut=System.currentTimeMillis();
		}

		void setFin() {
			tempsFin=System.currentTimeMillis();
			tempsTotal += tempsFin-tempsDebut;
		}
		
		public int getNbAppels() {
			return this.nbreAppel;
		}

		public long getTempsTotal() {
			return this.tempsTotal;
		}

				
	}
	
}
