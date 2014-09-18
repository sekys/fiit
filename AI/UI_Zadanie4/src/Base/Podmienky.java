
package Base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Util.Util;

public class Podmienky extends Compute
{
	public Podmienky(Uloha uloha) {
		super(uloha);
	}

	/**
	 * Skontroluj kazde pravidlo
	 */
	protected void Zhoda() {
		for (Rules.Rule p : uloha.getRules()) {
			skontrolujPravidlo(p);
		}
	}

	/**
	 * SKontroluj a spracuj jedno pravidlo
	 * 
	 * @param p
	 */
	private void skontrolujPravidlo(Rules.Rule p) {
		List<List<List<String>>> namapovane = new ArrayList<List<List<String>>>();
		List<String> specialPodmienky = new ArrayList<String>();
		List<List<String>> noveKombinacie = new ArrayList<List<String>>();

		// Prechadzaj vsetky podmienky a porovnaj to s pravidlom
		for (String podmienka : p.conditions) {
			if (podmienka.contains("<>")) {
				specialPodmienky.add(podmienka);
			} else {
				if (!podmienka.contains("?")) {
					if (!uloha.getFacts().contains(podmienka)) return;
				} else {
					// Najdi podobne fakty
					List<List<String>> podobne = new ArrayList<List<String>>();
					for (String fakt : uloha.getFacts()) {
						List<String> pom = PodmienkaAFaktSuPodobne(podmienka, fakt);
						if (pom != null) {
							podobne.add(pom);
						}
					}
					if (podobne.size() == 0) return;
					namapovane.add(podobne);
				}
			}
		}

		// Sprav kombinacie
		List<List<String>> kombinacie = VytvorKombinacie(namapovane);
		if (kombinacie == null) return;
 
		// Skontroluj este specialne podmienky - specialne naviazania
		List<String> naviazanie;
		if (specialPodmienky.size() > 0) {
			for (List<String> kombinacia : kombinacie) {
				naviazanie = Util.NaviazPremenne(kombinacia, specialPodmienky);
				if (rovnakeFakty(naviazanie)) noveKombinacie.add(kombinacia);
			}
			if (noveKombinacie.size() == 0) kombinacie = null;
			kombinacie = noveKombinacie;
		}

		pridajNoveFakty(kombinacie, p);
	}

	/**
	 * SKontroluje vsetky naviazanie a zisti ci fakty v nej su rovnake
	 * 
	 * @param naviazanie
	 * @return
	 */
	private boolean rovnakeFakty(List<String> naviazanie) {
		for (String value : naviazanie) {
			String[] x = value.split(" ");
			if (x[1].equals(x[2])) return false;
		}
		return true;
	}

	// Podmienka sa zhoduje s faktom, ak maju rovnaky pocet slov a nezhoduju
	// sa v nej len take slova, ktore su oznacene ako premenne.
	// Vrati pole dvojic ?premenna=hodnota

	/**
	 * Skontroluj ci fakt a podmienka sa zhoduju
	 * plati to ked maju rovnaky pocet slov a ked premenne ukazuju na slova
	 * 
	 * @param Podmienka
	 * @param Fakt
	 * @return
	 */
	private List<String> PodmienkaAFaktSuPodobne(String Podmienka, String Fakt) {
		String[] p, f;
		p = Podmienka.split(" ");
		f = Fakt.split(" ");
		if (p.length != f.length) return null;
		return PodmienkaAFaktSuPodobne(p, f);
	}

	private List<String> PodmienkaAFaktSuPodobne(String[] p, String[] f) {
		String veta = "";
		int x;
		for (x = 0; x < p.length; x++) {
			if (p[x].equals(f[x])) continue;
			if (!p[x].contains("?")) return null;
			veta = veta + p[x] + "=" + f[x] + " ";
		}
		
		if (veta.length() > 0) return Arrays.asList(veta.split(" "));
		return null;
	}
	/**
	 * Pridaj medzi aplikovane fakty nove pravidlo
	 * 
	 * @param kombinacie
	 * @param Akcie
	 * @param Meno
	 */
	public void pridajNoveFakty(List<List<String>> kombinacie, Rules.Rule p) {
		List<String> nak;
		if (kombinacie.size() == 0) {
			nak = Util.NaviazPremenne(new ArrayList<String>(), p.actions);
			nak.add(0, p.meno);
			aplikovaneFakty.add(nak);
		} else {
			for (List<String> item : kombinacie) {
				nak = Util.NaviazPremenne(item, p.actions);
				nak.add(0, p.meno);
				aplikovaneFakty.add(nak);
			}
		}
	}

}
