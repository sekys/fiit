package siet;

/**
 * Switch zoberie paket a rozposle ho do svojej podsiete.
 */
public class Switch extends SietoveZariadenie {

	/**
	 * Prisiel mi paket ale ten moj nieje tak ho preposielam dalej
	 */
	@Override
	protected void paketNieJeMoj(Paket paket) {
		for (SietoveZariadenie zar : getPrepojenie()) {
			posliSpravu(zar, paket);
		}
	}

	/**
	 * Paket prisiel mne. Rozhodol som sa ze ked pride mne-switchu tak paket
	 * odosle vsetkych zariadenia ako keby to bolo na nich smerovane. Posle sa
	 * teda N paketov.
	 */
	@Override
	protected void paketJeMoj(Paket paket) {
		for (SietoveZariadenie zar : getPrepojenie()) {
			paket.setCiel(zar);
			posliSpravu(zar, paket); // polymorfizmus
		}
	}
}
