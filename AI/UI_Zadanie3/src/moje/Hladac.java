
package moje;

import java.util.Arrays;
import java.util.Random;

public class Hladac implements IPohyb
{
	public int[]	zoznamPokladov;
	public int		najdene_poklady;
	public int		pocet_krokov;
	public int		mojaPoziciaNaMape;
	public Mapa		mapa;
	public double	fitness;
	public byte[]	pamBunky;

	public Hladac(Mapa mapa) {
		pamBunky = new byte[64];
		fitness = 0.0;
		this.mapa = mapa;
		zoznamPokladov = new int[mapa.pokladov];
	}

	public void vypocitajFitness() {
		najdene_poklady = 0;
		pocet_krokov = 0;
		mojaPoziciaNaMape = mapa.start;

		VirtualnyStroj stroj = new VirtualnyStroj();
		stroj.vypocitaj(pamBunky, this);
		stroj.run();

		if (DUMP_KROKY) {
			int i = 5;
			i++;
		}
		if (pocet_krokov == 0) {
			fitness = -1.0;
		} else {
			fitness = (double) najdene_poklady + (double) 0.99
					/ (double) pocet_krokov;
		}
	}

	public void nahodneNapln(int k) {
		Random rand = new Random();
		for (int i = 0; i < k;)
			for (int rnd = rand.nextInt(), n = Math.min(k - i, 4); n-- > 0; rnd >>= 8)
				pamBunky[i++] = (byte) rnd;
	}

	public String toString() {
		return Double.toString(fitness);
	}

	public static boolean	DUMP_KROKY	= false;

	public boolean pohyb(int jedniciek) {
		// System.out.print("Pohyb: ");
		pocet_krokov++;
		// DUMP_KROKY = true;
		// int old = mojaPoziciaNaMape;

		switch (jedniciek) {
			case 0 :
			case 1 :
			case 2 : { // H
				if (mapa.canH(mojaPoziciaNaMape)) {
					mojaPoziciaNaMape = mapa.doH(mojaPoziciaNaMape);
					if (DUMP_KROKY) System.out.print(" H");
				} else {
					// if (DUMP_KROKY) System.out.println("Hore ist nemozem, ignorujem.");
					return true;
				}
				break;
			}
			case 3 :
			case 4 : { // D
				if (mapa.canD(mojaPoziciaNaMape)) {
					mojaPoziciaNaMape = mapa.doD(mojaPoziciaNaMape);
					if (DUMP_KROKY) System.out.print(" D");
				} else {
					// if (DUMP_KROKY) System.out.println("Dole ist nemozem, ignorujem.");
					return true;
				}
				break;
			}
			case 5 :
			case 6 : { // P
				if (mapa.canP(mojaPoziciaNaMape)) {
					mojaPoziciaNaMape = mapa.doP(mojaPoziciaNaMape);
					if (DUMP_KROKY) System.out.print(" P");
				} else {
					// if (DUMP_KROKY) System.out.println("Pravo ist nemozem, ignorujem.");
					return true;
				}
				break;
			}
			case 7 :
			case 8 : { // L
				if (mapa.canL(mojaPoziciaNaMape)) {
					mojaPoziciaNaMape = mapa.doL(mojaPoziciaNaMape);
					if (DUMP_KROKY) System.out.print(" L");
				} else {
					// if (DUMP_KROKY) System.out.println("Lavo ist nemozem, ignorujem.");
					return true;
				}
				break;
			}
		}

		/*
		 * if (DUMP_KROKY) {
		 * System.out.println("Moja pozicia: " + Integer.toString(old));
		 * System.out.println("Nova pozicia: " + Integer.toString(mojaPoziciaNaMape));
		 * }
		 */

		try {
			skontrolujCiJeTUPoklad();
		}
		catch (Solved s) {
			return false;
		}
		return true;
	}

	protected boolean nasielUzTenPoklad() {
		for (int i = 0; i < najdene_poklady; i++) {
			if (zoznamPokladov[i] == mojaPoziciaNaMape) {
				// tzv tento poklad on uz nasiel a zobral...
				return true;
			}
		}
		return false;
	}

	protected void skontrolujCiJeTUPoklad() throws Solved {
		// Sme na novej poziicii, skontroluj ci je tu poklad
		if (mapa.nasielPoklad(mojaPoziciaNaMape)) {
			// podla mapy na tejto pozicii je poklad
			if (nasielUzTenPoklad()) return; // tento poklad uz raz nasiel..
			nasielPoklad();
		}
	}

	protected void nasielPoklad() throws Solved {
		if (DUMP_KROKY) {
			int i = 5;
			i++;
		}
		zoznamPokladov[najdene_poklady] = mojaPoziciaNaMape;
		najdene_poklady++;
		if (najdene_poklady == mapa.pokladov) {
			if (DUMP_KROKY)  System.out.println("\nNasiel som vsetky poklady.");
			throw new Solved(this);
		}
	}

	public Hladac clone() {
		Hladac novy = new Hladac(this.mapa);
		novy.pamBunky = Arrays.copyOf(this.pamBunky, this.pamBunky.length);
		return novy;
	}

	public void crossover(Hladac h2) {
		byte temp;
		int indexA, indexB;
		int k = (int) (Math.random() * 3); // kolko genov sa max vymeni ?
		for (int i = 0; i < k; i++) {
			indexA = (int) (Math.random() * 64);
			indexB = (int) (Math.random() * 64);

			if (indexA == indexB) {
				i--; // skus to znovu
				continue;
			}
			// Vymen pam. bunky
			temp = pamBunky[indexA];
			pamBunky[indexA] = h2.pamBunky[indexB];
			h2.pamBunky[indexB] = temp;
		}
	}

	public static double	mutationProb	= 0.01;

	public Hladac mutate() {
		for (int i = 0; i < 64; i++) {
			if (Math.random() < mutationProb) {
				pamBunky[i] = Instrukcie.random();
			}
		}
		return this;
	}

	public void vypisCestu() {
		DUMP_KROKY = true;
		System.out.println("Cesta zaciatok.");
		vypocitajFitness();
		System.out.println("\nCesta koniec.");
		DUMP_KROKY = false;
	}
}
