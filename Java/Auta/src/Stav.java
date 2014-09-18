import java.util.Arrays;

public class Stav
{
	public Stav						predchadzajuci;

	/**
	 * Udrziava sa pozicia (index policka) toho najvrchnejsieho
	 * pre H auta a toho najlavejsieho policka pre V auta.
	 */
	public byte[]					pozicieAut;

	/**
	 * Niektore vlastnosti aut, ako farba a moznost pohybu su rovnake
	 * pre vsetke stavy pre to sa tento tieto vlastnosti zdruzujeme.
	 */
	public static GlobalVlastnosti	global;
	public int						hash;

	public Stav(byte[] p) {
		pozicieAut = p;
		predchadzajuci = null;
		hash = Arrays.hashCode(pozicieAut);
	}

	/**
	 * Novy stav vytvorime tak, ze stary stav skopirujeme.
	 * A pozicia jedneho AUTA sa zmeni na NOVUSURADNICU.
	 */
	public Stav(Stav rodic, int vozidlo, byte novasuradnica) {
		if(novasuradnica < 0 || novasuradnica > 35) {
			throw new IndexOutOfBoundsException();
		}
		pozicieAut = rodic.pozicieAut.clone();
		pozicieAut[vozidlo] = novasuradnica;
		predchadzajuci = rodic;
		hash = Arrays.hashCode(pozicieAut);
	}

	/**
	 * Vrati Auto ktore sa nachadza na danom mieste.
	 * Vrat null ked tam auto nie je a je to prazdne meisto.
	 */
	public Auto getCar(byte pos) {
		Auto car;
		for (int i = 0; i < pozicieAut.length; i++) {
			car = global.auta.get(i);
			if (car.otestuj(pozicieAut[i], pos)) return car;
		}
		return null;
	}

	/**
	 * Stav je cielovy vtedy a len vtedy ked CERVENE auto ( teda auto 0) je
	 * na okraji pravej obrazovky. Teda cislo policka, zvysok po deleni je 5.
	 */
	public boolean jeCielovy() {
		int pozicia = global.auta.get(0).velke ? 3 : 4; // Cerve auto ma but sirku 6-3 alebo 6-2
		return pozicieAut[0] % 6 == pozicia;
	}

	/**
	 * Funkcia vypise stav / stav dopravy v stvorcovej sieti.
	 * Namiesto farby vypise len prve pismeno farby.
	 */
	public String toString() {
		byte pos;
		String pole = new String(new StringBuffer(75)); // 73 presne
		Auto car;
		for (pos = 0; pos < 36; pos++) {
			car = getCar(pos);
			if (car == null) {
				pole += 'X';
			} else {
				pole += car.farba.charAt(0);
			}
			if (pos % 6 == 5) {
				pole += '\n';
			} else {
				pole += ' ';
			}
		}
		pole += '\n';
		return pole;
	}

	public void rekurzivnyVypis(Stav dalsi) {
		if (dalsi == null) return;
		rekurzivnyVypis(dalsi.predchadzajuci);
		System.out.println(dalsi);
	}

	public void rekurzivnyVypis() {
		rekurzivnyVypis(this);
	}

	/**
	 * 2 stavy su rovnake ked pozicie vsetkych aut su rovnake.
	 */
	public boolean equals(Object b) {
		Stav druhy = (Stav) b;
		if (druhy.hash != hash) return false;
		for (int i = 0; i < pozicieAut.length; i++) {
			if (pozicieAut[i] != druhy.pozicieAut[i]) return false;
		}
		return true;
	}

	public int hashCode() {
		return hash;
	}
}
