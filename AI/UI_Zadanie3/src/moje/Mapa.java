
package moje;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Mapa
{
	public byte[]	mapa;
	public int		start;
	public int		N, M;
	public int		pokladov;

	public Mapa() {
		mapa = null;
	}

	public void Load() {
		File file = new File("in.in");

		try {
			Scanner in = new Scanner(file);
			N = in.nextInt();
			M = in.nextInt();
			mapa = new byte[N * M];
			pokladov = 0;

			int i;
			byte x;
			for (i = 0; i < N * M; i++) {
				x = in.nextByte();
				if (x == 2) start = i;
				if (x == 1) pokladov++;
				mapa[i] = x;
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void LoadRandom() {
		File file = new File("random.in");

		try {
			Scanner in = new Scanner(file);
			N = in.nextInt();
			M = in.nextInt();
			start = in.nextInt();
			int k = in.nextInt();
			mapa = new byte[N * M];
			pokladov = 0;

			int i, index;
			for (i = 0; i < k; i++) {
				index = (int) (Math.random() * M * N);
				if (index == start) {
					i--; // zopakuj lebo tu je start
					continue;
				}
				if (mapa[index] == 1) {
					i--; // zopakuj lebo tu je uz poklad
					continue;						
				}
				
				mapa[index] = 1;
				pokladov++;
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	public void print() {
		int i;
		for (i = 0; i < N * M; i++) {
			System.out.print(mapa[i] + "(" + Integer.toString(i) + ")" +"\t");
			if (i % M == M - 1) {
				System.out.println();
			}
		}
	}

	public boolean canH(int pos) {
		return pos >= M;
	}
	public boolean canD(int pos) {
		return pos + M < N * M;
	}
	public boolean canP(int pos) {
		return pos % M != M - 1;
	}
	public boolean canL(int pos) {
		return pos % M != 0;
	}

	public int doH(int pos) {
		return pos - M;
	}
	public int doD(int pos) {
		return pos + M;
	}
	public int doP(int pos) {
		return pos + 1;
	}
	public int doL(int pos) {
		return pos - 1;
	}

	public boolean nasielPoklad(int pos) {
		return mapa[pos] == 1;
	}

	public static void main(String[] args) {
		Mapa mapa = new Mapa();
		//mapa.Load();
		mapa.LoadRandom();
		mapa.print();
	}
}
