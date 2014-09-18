package moje;

import java.util.Arrays;

public class Zahrada
{
	public int[]	zahrada;
	public int		N;
	public int		M;

	public enum Pohyb {
		HORE,
		DOLE,
		VPRAVO,
		VLAVO
	}

	public Zahrada() {
		zahrada = null;
	}

	public void vypis() {
		int rozmer = N * M;
		for (int i = 0; i < rozmer; i++) {
			String point;
			if (zahrada[i] == -1) point = "K"; 
			else
			{
				point = Integer.toString(zahrada[i]);
			}
			System.out.print(point + "[" + Integer.toString(i) + "]" + "\t\t");
			int noveM = M - 1;
			int temp = i % M;
			if (temp == noveM) {System.out.println();}
			
		}
	}

	public boolean jeZaciatokRiadka(int pos) {
		int zaciatokR= pos % M;
		return zaciatokR == 0;
	}
	public boolean jeKoniecRiadka(int pos) {
		int koniecR = pos % M;
		return koniecR == M - 1;
	}
	public boolean jeZaciatokStlpca(int pos) {
		int zaciatokS = pos;
		return zaciatokS < M;
	}
	public boolean jeKoniecStlpca(int pos) {
		int koniecS = pos + M ;
		return koniecS >= N * M;
	}

	public int pohyb(Pohyb typ, int pos) {
		switch (typ) {
			case HORE : {
				int nova;
				nova = pos - M;
				return nova;
			}
			case DOLE : {
				int nova;
				nova = pos + M;
				return nova;
			}
			case VPRAVO : {
				int nova;
				nova = pos + 1;
				return nova;
			}
			case VLAVO : {
				int nova;
				nova = pos -1;
				return nova;
			}
			default : {
				System.out.println("nastala chyba");
			}
		}
		return -1;
	}

	public boolean jeObsadene(int pos) {
		return zahrada[pos] != 0;
	}

	public boolean jeOkraj(int pos, Pohyb typ) {
		switch (typ) {
			case HORE : {
				return jeZaciatokStlpca(pos);
			}
			case DOLE : {
				return jeKoniecStlpca(pos);
			}
			case VPRAVO : {
				return jeKoniecRiadka(pos);
			}
			case VLAVO : {
				return jeZaciatokRiadka(pos);
			}
			default : {
				System.out.println("nastala chyba");
			}
		}
		return false;
	}
	
	public boolean jeOkraj(int pos) {
		return jeZaciatokRiadka(pos) || jeKoniecRiadka(pos)
				|| jeZaciatokStlpca(pos) || jeKoniecStlpca(pos);
	}

	public Zahrada clone() {
		Zahrada novy = new Zahrada();
		novy.zahrada = Arrays.copyOf(this.zahrada, this.zahrada.length);
		novy.M = M;
		novy.N = N;
		return novy;
	}
}
