
package moje;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Generacia
{
	public static double		mutationProb	= 0.16;
	public static double		crossoverProb	= 0.16;
	public static double		elitismProb		= 0.40;

	protected ArrayList<Hladac>	hladaci			= new ArrayList<Hladac>(0);
	public Mapa					mapa;

	public Generacia(Mapa mapa, int k) { // k by ma byt viacej ako 10, najlepsie 100
		// Vytvor hladacov, nastav im udaje
		this.mapa = mapa;
		hladaci = new ArrayList<Hladac>(k);
		for (int i = 0; i < k; i++) {
			hladaci.add(new Hladac(mapa));
		}
	}

	public Generacia(Mapa mapa, ArrayList<Hladac> hladaci) {
		// Vytvor haldacov, nastav im udaje
		this.mapa = mapa;
		this.hladaci = hladaci;
	}

	public void nahodneNapln(int prvychLudi, int pocetPametovychBuniek) {
		// vygeneruj nahodne instrukcie pre hladacov
		for (int i = 0; i < prvychLudi; i++) {
			hladaci.get(i).nahodneNapln(pocetPametovychBuniek);
		}
	}
	public Hladac best() {
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
		Collections.sort(hladaci, new Comparator<Hladac>()
		{
			public int compare(Hladac a, Hladac b) {
				return a.fitness > b.fitness ? 0 : 1;
			}
		});

		// pomocny vypis
		//dSystem.out.println(hladaci);
	}

	public ArrayList<Hladac> copy() {
		ArrayList<Hladac> novyhladaci = new ArrayList<Hladac>(hladaci.size());
		for (int i = 0; i < hladaci.size(); i++) {
			novyhladaci.add(hladaci.get(i).clone());
		}
		return novyhladaci;
	}
	// To je vlastne proces mutovania
	// Vyberem z populacie niekolko chromozomov s najmensim fitness a vyhodim ich.
	// Rovnaky pocet vyberem aj chromozomov s najvacsim fitness a rozmnozim ich.
	public Generacia vygenerujGeneraciu() {
		ArrayList<Hladac> novyhladaci = copy();

		int elitismIndex = (int) (elitismProb * hladaci.size());
		int crossoverIndex = (int) (crossoverProb * hladaci.size())
				+ elitismIndex;
		int mutationIndex = (int) (mutationProb * hladaci.size())
				+ crossoverIndex;

		for (int i = 0; i < novyhladaci.size(); i++) {
			if (i < elitismIndex) {
				novyhladaci.set(i, hladaci.get(i).clone());
			} else if (i < crossoverIndex) {
				Hladac clone1 = randomHladac().clone();
				Hladac clone2 = randomHladac().clone();
				clone1.crossover(clone2);
				novyhladaci.set(i, clone1);
				i++;
				if (i < novyhladaci.size()) {
					novyhladaci.set(i, clone2);
				}
			} else if (i < mutationIndex) {
				novyhladaci.set(i, randomHladac().clone().mutate());
			} else {
				novyhladaci.set(i, new Hladac(mapa));
			}
		}

		// Posli novu generaciu
		return new Generacia(mapa, novyhladaci);
	}

	public Hladac randomHladac() {
		return hladaci.get((int) (Math.random() * hladaci.size()));
	}

}
