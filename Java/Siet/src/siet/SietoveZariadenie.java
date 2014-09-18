package siet;

import java.util.List;

/**
 * Ide o akekolvek zariadenie v sieti. Kazde jedno ma mnozinu prepojeni s inym
 * zariadenim. Potom kazde ma unikatnu adresu. Aby mohlo fungovat musi byt
 * pripojene do sieti.
 */
public abstract class SietoveZariadenie {
	private Integer unikatnaAdresa; // Specialna adresa zariadenia.
	private ISiet siet; // Interface na siet, ku ktorej je zariadenie pripojene.
	private Integer pocetPripojeni; // Pocet pripojeni na toto zariadenie

	public SietoveZariadenie() {
		unikatnaAdresa = null;
		siet = null;
		pocetPripojeni = 0;
	}

	public Integer getUnikatnaAdresa() {
		return unikatnaAdresa;
	}

	public void setUnikatnaAdresa(Integer unikatnaAdresa) {
		this.unikatnaAdresa = unikatnaAdresa;
	}

	/**
	 * Pripoj ku mne zariadenie.
	 * 
	 * @param roz
	 * @throws MaxPocetPripojeni
	 */
	public void pripoj(SietoveZariadenie roz) throws MaxPocetPripojeni {
		pocetPripojeni++;
	}

	/**
	 * Zaraidenie prijalo paket, rozhodni ci je urceny pre mna.
	 * 
	 * @param paket
	 */
	public void Prijal(final Paket paket) {
		if (paket == null) {
			return;
		}

		if (paket.getCiel().equals(this)) {
			paketJeMoj(paket);
		} else {
			paketNieJeMoj(paket);
		}
	}

	/**
	 * Paket nieje urceny pre mna.
	 * 
	 * @param paket
	 */
	protected abstract void paketNieJeMoj(Paket paket);

	/**
	 * Paket je urceny pre mna.
	 * 
	 * @param paket
	 */
	protected abstract void paketJeMoj(Paket paket);

	/**
	 * Vypis informacie o zariadeni. Teda uniaktnu adresu.
	 */
	public String toString() {
		return unikatnaAdresa.toString();
	}

	public List<SietoveZariadenie> getPrepojenie() {
		return siet.getZoznamPripojenychZariadeni(this);
	}

	public void pripojDoSieti(ISiet siet) {
		this.siet = siet;
	}

	protected void posliSpravu(SietoveZariadenie to, final Paket p) {
		siet.posli(this, to, p);
	}

	@Override
	public boolean equals(Object obj) {
		SietoveZariadenie zar = (SietoveZariadenie) obj;
		return zar.unikatnaAdresa.equals(unikatnaAdresa);
	}

	public Integer getPocetPripojeni() {
		return pocetPripojeni;
	}
}
