
package Base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import Action.Question;
import Util.Joiner;

public abstract class Compute extends Kombinacie
{
	protected Uloha						uloha;
	protected ArrayList<List<String>>	aplikovaneFakty;

	public Compute(Uloha uloha) {
		this.uloha = uloha;
		aplikovaneFakty = new ArrayList<List<String>>();
	}

	/**
	 * Zisti ci mozme vykonat danu akciu
	 * 
	 * @param Akcie
	 * @return
	 */
	private boolean Mozne(List<String> Akcie) {
		List<String> data = null;
		for (int x = 1; x < Akcie.size(); x++) {
			data = Arrays.asList(Akcie.get(x).split(" "));
			String post = Joiner.joinPost(data);
			if (data.get(0).equals("pridaj")) {
				if (!uloha.getFacts().contains(post)) {
					return true;
				}
			} else if (data.get(0).equals("vymaz")) {
				if (uloha.getFacts().contains(post)) {
					return true;
				}
			} else if (data.get(0).equals("otazka")) {
				if (new Question(uloha.getFacts()).can(data)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * VYkonaj dane akcie
	 * 
	 * @param meno
	 * @param data
	 */
	private void Akcia(String meno, String[] data) {
		String post = Joiner.joinPost(data);
		if (meno.equals("pridaj")) {
			if (!uloha.getFacts().contains(post)) uloha.getFacts().add(post);
		} else if (meno.equals("vymaz")) {
			Facts noveFakty = new Facts();
			for (String fakt : uloha.getFacts()) {
				if (!post.equals(fakt)) noveFakty.add(post);
			}
			uloha.setFacts(noveFakty);
		} else if (meno.equals("otazka")) {
			new Question(uloha.getFacts()).make(data);
		} else {
			System.out.println("AKCIA-MSG " + post);
		}
	}

	/**
	 * Porovnaj podmienky a akcie
	 */
	protected abstract void Zhoda();

	/**
	 * Sprav vypocet
	 */
	public void compute() {
		String[] data;
		ArrayList<List<String>> novePravidla;
		do {
			aplikovaneFakty = new ArrayList<List<String>>();
			novePravidla = new ArrayList<List<String>>();
			Zhoda();
	
			// Prefiltruj a vykonaj akcie
			for (List<String> zoznam : aplikovaneFakty) {
				if (Mozne(zoznam)) novePravidla.add(zoznam);
			}
			aplikovaneFakty = novePravidla;
			if (aplikovaneFakty.size() > 0) {
				for (int x = 1; x < aplikovaneFakty.get(0).size(); x++) {
					data = aplikovaneFakty.get(0).get(x).split(" ");
					Akcia(data[0], data);
				}
			}
		}
		while (aplikovaneFakty.size() > 0);

		// Posledny vypis
		uloha.getFacts().dump();
	}
}
