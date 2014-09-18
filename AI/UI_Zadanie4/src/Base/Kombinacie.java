
package Base;

import java.util.ArrayList;
import java.util.List;

public class Kombinacie
{
	/**
	 * Vytvor kombinacie medzi naviazanimy
	 * 
	 * @param pole
	 * @return
	 */
	protected List<List<String>> VytvorKombinacie(List<List<List<String>>> pole) {
		return vycisti(VytvorIbaKombinacie(pole));
	}

	/**
	 * Vytvor kombinacie medzi naviazanimy
	 * @param pole
	 * @return
	 */
	protected List<List<String>> VytvorIbaKombinacie(List<List<List<String>>> pole) {
		int velkost = pole.size();
		if (velkost == 0) return new ArrayList<List<String>>();
		if (velkost == 1) return pole.get(0);
		return multiKombinacie(pole);
	}
	
	/**
	 * SPecialna metoda pre multidimenzionalne naviazanie
	 * 
	 * @param pole
	 * @return
	 */
	private List<List<String>> multiKombinacie(List<List<List<String>>> pole) {
		List<List<String>> kombinacie = new ArrayList<List<String>>();
		List<List<String>> noveKombinacie = new ArrayList<List<String>>();

		for (List<String> zoznam : pole.get(0))
			kombinacie.add(zoznam);

		int a;
		for (a = 1; a < pole.size(); a++) {
			for (List<String> b : pole.get(a)) {
				for (List<String> kombinacia : kombinacie) {
					ArrayList<String> temp = new ArrayList<String>();
					temp.addAll(kombinacia);
					temp.addAll(b);
					noveKombinacie.add(temp);
				}
			}
			kombinacie = noveKombinacie;
			noveKombinacie = new ArrayList<List<String>>();
		}
		return kombinacie;
	}

	/**
	 * Daj mi kombinacie kde je priradena jedna premenna a jedna hodnoda
	 * 
	 * @param stareKombinacie
	 * @return
	 */
	private List<List<String>> vycisti(List<List<String>> stareKombinacie) {
		List<List<String>> noveKombinacie = new ArrayList<List<String>>();
		for (List<String> jednaKombinacia : stareKombinacie) {

			// Pridaj len unikatne naviazania
			List<String> nove = new ArrayList<String>();
			for (String jedna : jednaKombinacia) {
				if (!nove.contains(jedna)) {
					nove.add(jedna);
				}
			}

			// Sprav vyber len premennych
			List<String> novePremenne = new ArrayList<String>();
			for (String veta : nove) {
				String premenna = getPremenna(veta);
				if (!novePremenne.contains(premenna)) {
					novePremenne.add(premenna);
				}
			}
			
			// Skontroluj ci je rovnaky pocet - to musi byt
			if (nove.size() == novePremenne.size()) {
				noveKombinacie.add(nove);
			}
		}
		return noveKombinacie;
	}

	private String getPremenna(String veta) {
		return veta.substring(0, veta.indexOf("="));
	}
}
