import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Zadanie
{
	public GlobalVlastnosti	global	= new GlobalVlastnosti();

	public static void main(String[] args) {
		InputStreamReader isr = new InputStreamReader(System.in);
		Scanner vstup = new Scanner(isr);
		Zadanie zadanie = new Zadanie();
		Stav.global = zadanie.global;
		int pocetAut, typ;
		try {
			pocetAut = vstup.nextInt();
			typ = vstup.nextInt();
			byte pociatocneSuradnice[] = new byte[pocetAut];
			Auto car;
			int x, y;
			for (int i = 0; i < pocetAut; i++) {
				car = new Auto();
				car.farba = vstup.next();
				car.velke = (vstup.nextByte() == 3);
				y = vstup.nextByte() - 1; // Prva urcuje riadok
				x = vstup.nextByte() - 1;
				pociatocneSuradnice[i] = (byte) (y * 6 + x);
				car.xORy = (vstup.next().compareTo("h") == 0);
				zadanie.global.auta.add(car);
			}

			Stav zaciatok = new Stav(pociatocneSuradnice);
			System.out.println(zaciatok);
			Stav koniec = zadanie.prehladaj(zaciatok, typ == 0);
			if (koniec == null) {
				System.out.println("Nema riesenie.");
			} else {
				koniec.rekurzivnyVypis();
			}
		}
		catch (NoSuchElementException ne) {
			ne.printStackTrace();
		}
	}
	/*
	 * Operátory sú len štyri:
	 * (VPRAVO stav vozidlo poèet)
	 * (VLAVO stav vozidlo poèet)
	 * (DOLE stav vozidlo poèet)
	 * (HORE stav vozidlo poèet)
	 */
	public Stav VPRAVO(Stav rodic, int vozidlo, byte pocet) {
		Auto car = global.auta.get(vozidlo);
		if (car.xORy == false) return null; // To auto sa nemoze do sirky pohybovat
		int pozicia = car.velke ? 3 : 4;
		byte staraSuradnica = rodic.pozicieAut[vozidlo];
		if (staraSuradnica % 6 == pozicia) return null; // Auto je uz na okraji
		pozicia = car.velke ? 3 : 2;
		byte novasuradnica = (byte) (staraSuradnica + pozicia);
		if (rodic.getCar(novasuradnica) != null) return null; // Nejake auto tam uz je
		novasuradnica = (byte) (staraSuradnica + 1);
		return new Stav(rodic, vozidlo, novasuradnica); // Skopiruj stav a uprav ho
	}
	public Stav VLAVO(Stav rodic, int vozidlo, byte pocet) {
		Auto car = global.auta.get(vozidlo);
		if (car.xORy == false) return null; // To auto sa nemoze do sirky pohybovat
		byte staraSuradnica = rodic.pozicieAut[vozidlo];
		if (staraSuradnica % 6 == 0) return null; // Auto je uz na okraji
		byte novasuradnica = (byte) (staraSuradnica - 1);
		if (rodic.getCar(novasuradnica) != null) return null; // Nejake auto tam uz je
		return new Stav(rodic, vozidlo, novasuradnica); // Skopiruj stav a uprav ho
	}
	public Stav DOLE(Stav rodic, int vozidlo, byte pocet) {
		Auto car = global.auta.get(vozidlo);
		if (car.xORy == true) return null; // To auto sa nemoze do vysky pohybova
		int pozicia = car.velke ? 18 : 24;
		byte staraSuradnica = rodic.pozicieAut[vozidlo];
		if (staraSuradnica >= pozicia) return null; // Auto je uz na okraji
		pozicia = car.velke ? 18 : 12;
		byte novasuradnica = (byte) (staraSuradnica + pozicia);
		if (rodic.getCar(novasuradnica) != null) return null; // Nejake auto tam uz je
		novasuradnica = (byte) (staraSuradnica + 6);
		return new Stav(rodic, vozidlo, novasuradnica); // Skopiruj stav a uprav ho
	}
	public Stav HORE(Stav rodic, int vozidlo, byte pocet) {
		Auto car = global.auta.get(vozidlo);
		if (car.xORy == true) return null; // To auto sa nemoze do vysky pohybovat
		byte staraSuradnica = rodic.pozicieAut[vozidlo];
		if (staraSuradnica <= 5) return null; // Auto je uz na okraji
		byte novasuradnica = (byte) (staraSuradnica - 6);
		if (rodic.getCar(novasuradnica) != null) return null; // Nejake auto tam uz je
		return new Stav(rodic, vozidlo, novasuradnica); // Skopiruj stav a uprav ho
	}

	public Stav prehladaj(Stav zaciatok, boolean doSirky) {
		LinkedList<Stav> zoznamCoTrebaPrejst = new LinkedList<Stav>();
		HashSet<Stav> zoznamPouzitych = new HashSet<Stav>();
		zoznamCoTrebaPrejst.add(zaciatok);
		zoznamPouzitych.add(zaciatok);
		Stav novy;
		int vozidlo, pocet = 0;

		while (!zoznamCoTrebaPrejst.isEmpty()) {
			Stav rodic = zoznamCoTrebaPrejst.poll();
			if (rodic.jeCielovy()) return rodic;

			for (vozidlo = 0; vozidlo < global.auta.size(); vozidlo++) {
				//System.out.println("Auto " + Integer.toString(vozidlo));

				novy = VPRAVO(rodic, vozidlo, (byte) 1);
				if (novy != null) { // Ak Operator vrati null, dany operator nemoze byt aplikovany
					if (zoznamPouzitych.add(novy) == true) {// Ak operator este nieje v zozname
															// pouzitych
						if(doSirky) {
							zoznamCoTrebaPrejst.addLast(novy);
						} else {
							zoznamCoTrebaPrejst.addFirst(novy);
						}
						//System.out.println(novy);
					}
				}
				novy = VLAVO(rodic, vozidlo, (byte) 1);
				if (novy != null) { // Ak Operator vrati null, dany operator nemoze byt aplikovany
					if (zoznamPouzitych.add(novy) == true) {// Ak operator este nieje v zozname
															// pouzitych
						if(doSirky) {
							zoznamCoTrebaPrejst.addLast(novy);
						} else {
							zoznamCoTrebaPrejst.addFirst(novy);
						}
						//System.out.println(novy);
					}
				}
				novy = DOLE(rodic, vozidlo, (byte) 1);
				if (novy != null) { // Ak Operator vrati null, dany operator nemoze byt aplikovany
					if (zoznamPouzitych.add(novy) == true) {// Ak operator este nieje v zozname
															// pouzitych
						if(doSirky) {
							zoznamCoTrebaPrejst.addLast(novy);
						} else {
							zoznamCoTrebaPrejst.addFirst(novy);
						}
						//System.out.println(novy);
					}
				}
				novy = HORE(rodic, vozidlo, (byte) 1);
				if (novy != null) { // Ak Operator vrati null, dany operator nemoze byt aplikovany
					if (zoznamPouzitych.add(novy) == true) {// Ak operator este nieje v zozname
															// pouzitych
						if(doSirky) {
							zoznamCoTrebaPrejst.addLast(novy);
						} else {
							zoznamCoTrebaPrejst.addFirst(novy);
						}
						//System.out.println(novy);
					}
				}			
			}
			pocet++;
		}
		return null;
	}
}
