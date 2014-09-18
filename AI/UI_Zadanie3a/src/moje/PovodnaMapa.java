package moje;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PovodnaMapa extends Zahrada
{
	public ArrayList<Integer>	i2pos = null;
	public int					obvodZahrady;
	public int					prazdnePolicka;


	
	private int kamenov() {
		int pocet;
		pocet =  M * N - prazdnePolicka;
		return pocet;
	}
	
	public int limit() {
		int limit;
		limit = (obvodZahrady / 2) + kamenov();
		return limit;
	}



	public void Nacitaj() 
	{
		File f = new File("miro.in");
		try 
		{
			Scanner vstup = new Scanner(f);
			N = vstup.nextInt();
			M = vstup.nextInt();
			
			zahrada = new int[N * M];
			int rozmer = N * M;
	
			for (int i = 0; i < rozmer; i++) {
				int x = vstup.nextInt();
				if (x == 0) prazdnePolicka++;
				zahrada[i] = x;
			}
			int stranaA = N * 2;
			int stranaB = M * 2;
			obvodZahrady = stranaA + stranaB;
			i2pos = getIndexiOkrajov();
		}
		catch (NoSuchElementException exception)
		{
			exception.printStackTrace();
		}
		catch (FileNotFoundException exception1) 
		{
			exception1.printStackTrace();
		}
	}

	private ArrayList<Integer> getIndexiOkrajov() {

		ArrayList<Integer> zoznam = new ArrayList<Integer>();

		// Horny okraj
		for (int i = 0; i < M; i++) {
			if (jeObsadene(i)) continue;
			zoznam.add(new Integer(i));
		}

		// Dolny okraj
		for (int i = M * (N - 1); i < M * N; i++) {
			if (jeObsadene(i)) continue;
			zoznam.add(new Integer(i));
		}

		// lavy Bocny panel
		for (int i = 0; i < (M * N); i += M) {
			if (jeObsadene(i)) continue;
			zoznam.add(new Integer(i));
		}

		// pravy bocny panel
		for (int i = (M - 1); i < (M * N); i += M) {
			if (jeObsadene(i)) continue;
			zoznam.add(new Integer(i));
		}

		return zoznam;
	}

	public Pohyb getPohyb(int index) {
		// podla indexu zisti o aky typ pohybu ma ist
		// index je index v zozname okrajov
		if (index < M) {
			return Pohyb.DOLE;
		} else
			if (index < M + M) {
			return Pohyb.HORE;
		} else
			if (index < M + M + N) {
			return Pohyb.VPRAVO;
		} else 
			if (index < M + M + N + N) {
			return Pohyb.VLAVO;
		}
		return null;
	}

	public static void main(String[] args) {
		PovodnaMapa mapa = new PovodnaMapa();
		mapa.Nacitaj();
		System.out.println(mapa.getIndexiOkrajov());
		mapa.vypis();
	}
}
