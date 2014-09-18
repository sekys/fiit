package siet;

/**
 * Iba kazdy PC moze posielat, prijmat informaciu. PC musi byt predtym pripojene
 * do sieti. Kazde ma unikatnu adresu.
 */
public class PC extends SietoveZariadenie {

	/**
	 * PC ak prijal paket co nie je jeho tak ho ingoruje.
	 */
	@Override
	protected void paketNieJeMoj(Paket paket) {
		System.out.println("Ja " + this + " ignorujem " + paket);
	}

	@Override
	protected void paketJeMoj(Paket paket) {
		System.out.println("Ja " + this + " prijal som moj " + paket);
	}

	/**
	 * PC moze mat len jedno prepojenie. V realnom svete ich moze mat viac, mi
	 * to chceme zjednodusit.
	 */
	public void pripoj(SietoveZariadenie roz) throws MaxPocetPripojeni {
		if (getPocetPripojeni() == 1) {
			throw new MaxPocetPripojeni();
		}
	}
}
