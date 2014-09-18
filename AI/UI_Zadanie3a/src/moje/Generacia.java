
package moje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Generacia
{
	public static double		mutationProb	= 0.31;
	public static double		crossoverProb	= 0.39;
	public static double		elitismProb		= 0.1;

	protected ArrayList<Zen>	hladaci			= new ArrayList<Zen>(0);

	public Generacia(int k) {
		// Vytvor hladacov, nastav im udaje
		hladaci = new ArrayList<Zen>(k);
		for (int i = 0; i < k; i++) {
			hladaci.add(new Zen());
		}
	}

	public Generacia(ArrayList<Zen> hladaci) {
		// Vytvor haldacov, nastav im udaje
		this.hladaci = hladaci;
	}

	public void nahodneNapln() {
		// vygeneruj nahodne instrukcie pre hladacov
		for (int i = 0; i < hladaci.size(); i++) {
			hladaci.get(i).nahodneNapln();
		}
	}
	public Zen best() {
		return hladaci.get(0);
	}
	public void ohodnotCeluGeneraciu() {
		// to znamena vypocitaj fitnes pre kzdeho jedneho
		for (int i = 0; i < hladaci.size(); i++) {
			hladaci.get(i).vypocitajFitness();
		}
	}
	public void sort() {
		// vsetkych zorad podla fitnnes ohodnotenia
		Collections.sort(hladaci, new Comparator<Zen>()
		{
			public int compare(Zen a, Zen b) {
				return a.pohrabanych > b.pohrabanych ? 0 : 1;
			}
		});

		// pomocny vypis
		System.out.println(hladaci);
	}

	public ArrayList<Zen> copy() {
		ArrayList<Zen> novyhladaci = new ArrayList<Zen>(hladaci.size());
		for (int i = 0; i < hladaci.size(); i++) {
			novyhladaci.add(hladaci.get(i).clone());
		}
		return novyhladaci;
	}
	// To je vlastne proces mutovania
	// Vyberem z populacie niekolko chromozomov s najmensim fitness a vyhodim ich.
	// Rovnaky pocet vyberem aj chromozomov s najvacsim fitness a rozmnozim ich.
	public Generacia vygenerujGeneraciu() {
		ArrayList<Zen> novyhladaci = copy();

		int elitismIndex = (int) (elitismProb * hladaci.size());
		int crossoverIndex = (int) (crossoverProb * hladaci.size())
				+ elitismIndex;
		int mutationIndex = (int) (mutationProb * hladaci.size())
				+ crossoverIndex;

		for (int i = 0; i < novyhladaci.size(); i++) {
			if (i < elitismIndex) {
				novyhladaci.set(i, hladaci.get(i).clone());
			} else if (i < crossoverIndex) {
				Zen clone1 = randomZen().clone();
				Zen clone2 = randomZen().clone();
				clone1.crossover(clone2);
				novyhladaci.set(i, clone1);
				i++;
				if (i < novyhladaci.size()) {
					novyhladaci.set(i, clone2);
				}
			} else if (i < mutationIndex) {
				novyhladaci.set(i, randomZen().clone().mutate());
			} else {
				novyhladaci.set(i, new Zen().nahodneNapln());
			}
		}

		// Posli novu generaciu
		return new Generacia(novyhladaci);
	}

	public Zen randomZen() {
		return hladaci.get((int) (Math.random() * hladaci.size()));
	}

}
