package ui;

import java.util.ArrayList;
import java.util.List;

import siet.ISiet;
import siet.Paket;
import siet.Prepojenie;
import siet.SietoveZariadenie;
import siet.Vrchol;

public class Siet implements ISiet {
	protected ArrayList<Vrchol> vrcholy; // vrcholy
	protected ArrayList<Prepojenie> prepojenia; // hrany

	public Siet() {
		vrcholy = new ArrayList<Vrchol>();
		prepojenia = new ArrayList<Prepojenie>();
	}

	/**
	 * Zisti ci taketo zariadenie mame v systeme ?
	 * 
	 * @param cislo
	 * @return
	 */
	public boolean existujeZariadenie(Integer cislo) {
		for (Vrchol v : vrcholy) {
			if (v.getZariadenie().getUnikatnaAdresa().equals(cislo))
				return true;
		}
		return false;
	}

	/**
	 * Vytvor spojenie medzi 2 zariadeniami.
	 * 
	 * @param a
	 * @param b
	 */
	public boolean prepoj(Vrchol a, Vrchol b) {
		Prepojenie prep = new Prepojenie(a, b);

		// Skontroluj ale najprv ci taketo prepojenie existuje
		if (prepojenia.contains(prep)) {
			return false;
		}

		// Posli este informaciu zariadeniam, ze ich ideme prepojit
		try {
			a.getZariadenie().pripoj(b.getZariadenie());
			b.getZariadenie().pripoj(a.getZariadenie());
		} catch (siet.MaxPocetPripojeni e) {
			e.printStackTrace();
		}

		prepojenia.add(prep);
		return true;
	}

	/**
	 * Touto metodou poskytuje zariadeniam informacie s ktorymi zariadeniami
	 * suedia
	 */
	@Override
	public List<SietoveZariadenie> getZoznamPripojenychZariadeni(
			SietoveZariadenie hladany) {
		List<SietoveZariadenie> zoznam;
		zoznam = new ArrayList<SietoveZariadenie>();

		for (Prepojenie p : prepojenia) {
			SietoveZariadenie zdroj, ciel;
			zdroj = p.getZdroj().getZariadenie();
			ciel = p.getCiel().getZariadenie();
			if (hladany.equals(zdroj)) {
				zoznam.add(ciel);
			}
			if (hladany.equals(ciel)) {
				zoznam.add(zdroj);
			}
		}
		return zoznam;
	}

	/**
	 * Posli paket cez siet od A ku B
	 */
	@Override
	public void posli(SietoveZariadenie a, SietoveZariadenie b, Paket paket) {
		for (Prepojenie p : prepojenia) {
			SietoveZariadenie zdroj, ciel;
			zdroj = p.getZdroj().getZariadenie();
			ciel = p.getCiel().getZariadenie();

			// Posli spravu jednym smerom
			if (zdroj.equals(a) && ciel.equals(b)) {
				p.posliPaket(paket);
				return;
			}

			// Posli spravu opacnym smerom
			if (ciel.equals(a) && zdroj.equals(b)) {
				p.posliPaket(paket, false);
				return;
			}
		}
	}

	/**
	 * Nainstaluj vrchol respektive zariadenie do systemu
	 * 
	 * @param id
	 * @param zariadenie
	 * @param x
	 * @param y
	 * @return
	 */
	public Vrchol install(Integer id, SietoveZariadenie zariadenie, int x, int y) {
		// Nastav vlastnosti vrchola
		zariadenie.setUnikatnaAdresa(id);
		zariadenie.pripojDoSieti(this);
		Vrchol vrchol = new Vrchol(zariadenie);
		vrcholy.add(vrchol);
		return vrchol;
	}

}
