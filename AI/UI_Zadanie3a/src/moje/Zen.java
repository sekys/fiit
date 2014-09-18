
package moje;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Zen
{
	public static PovodnaMapa	mapa_povodna;

	public byte[]				pociatocnePozicie;
	public byte					pocet_krokov;
	public int					pohrabanych;		// to je aj fitness

	public Zen() {
		pociatocnePozicie = null;
		pocet_krokov = 0;
		pohrabanych = 0;
	}

	public Zen clone() {
		Zen novy = new Zen();
		novy.pociatocnePozicie = new byte[this.pociatocnePozicie.length];
		novy.pociatocnePozicie = Arrays.copyOf(this.pociatocnePozicie, this.pociatocnePozicie.length);
		return novy;
	}

	public Zahrada vypocitajFitness() {
		pocet_krokov = 0;
		pohrabanych = 0;
		Hrabanie hrabanie = new Hrabanie(this);
		for (int i = 0; i < pociatocnePozicie.length; i++) {
			if (!hrabanie.hrabaj(pociatocnePozicie[i])) {
				break; // koniec hry, posli co mas
			}
		}
		hrabanie.moja.vypis();
		System.exit(0);
		return hrabanie.moja;
	}

	public Zen nahodneNapln() {
		pociatocnePozicie = new byte[mapa_povodna.limit()];
		int size = mapa_povodna.i2pos.size();
		ArrayList<Integer> indexi = new ArrayList<Integer>(size);
		for (int i = 0; i < size; i++)
			indexi.add(i);
		Collections.shuffle(indexi);

		for (byte i = 0; i < pociatocnePozicie.length; i++)
			pociatocnePozicie[i] = indexi.get(i).byteValue();

		return this;
	}

	public String toString() {
		return Integer.toString(pohrabanych);
	}

	public void crossover(Zen h2) {
		byte temp;
		int indexA, indexB;
		int k = (int) (Math.random() * 2); // kolko genov sa max vymeni ?
		for (int i = 0; i < k; i++) {
			indexA = (int) (Math.random() * pociatocnePozicie.length);
			indexB = (int) (Math.random() * h2.pociatocnePozicie.length);

			if (indexA == indexB) {
				i--; // skus to znovu
				continue;
			}
			// Vymen pam. bunky
			temp = pociatocnePozicie[indexA];
			pociatocnePozicie[indexA] = h2.pociatocnePozicie[indexB];
			h2.pociatocnePozicie[indexB] = temp;
			// TODO: co ak si vymenia tie iste cisla ?
		}
	}

	public static double	mutationProb	= 0.01;

	public Zen mutate() {
		byte temp;
		int indexA, indexB;
		for (int i = 0; i < pociatocnePozicie.length; i++) {
			if (Math.random() < mutationProb) {
				do {
					indexA = (int) (Math.random() * pociatocnePozicie.length);
					indexB = (int) (Math.random() * pociatocnePozicie.length);
				}
				while (indexA != indexB); // skus to znovu

				// Vymen pam. bunky
				temp = pociatocnePozicie[indexA];
				pociatocnePozicie[indexA] = pociatocnePozicie[indexB];
				pociatocnePozicie[indexB] = temp;
			}
		}
		return this;
	}

	public static void main(String[] args) {
		PovodnaMapa mapa = new PovodnaMapa();
		mapa.Nacitaj();
		mapa.vypis();

		Zen zen = new Zen();
		Zen.mapa_povodna = mapa;
		zen.nahodneNapln();

		Hrabanie.povodna = mapa;
		zen.vypocitajFitness();

		Zen b = zen.clone();
		b.mutate();
		b.mutate();

		zen.crossover(b);
	}
}
