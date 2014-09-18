package siet;

import java.awt.Point;

/**
 * Prepojenie medzi 2 vrcholmy. Prepojenie ma viacero atribut.
 * 
 */
public class Prepojenie {
	private Vrchol m_zdroj;
	private Vrchol m_ciel;

	private boolean m_posielamAB; // smer posielania z A -> B alebo opacne ?
	private boolean animacia; // je animacia zapnuta ?
	private float interpolacia; // hodnota pre animaciu je medzi 0-1
	private Paket paket; // aka informacia sa posiela ?

	public Prepojenie() {
		m_zdroj = m_ciel = null;
		paket = null;
		animacia = false;
	}

	public Prepojenie(Vrchol a, Vrchol b) {
		this();
		m_zdroj = a;
		m_ciel = b;
	}

	public void posliPaket(Paket paket) {
		posliPaket(paket, true);
	}

	/**
	 * Ide o zaciatok komunikacie na danom spojeni. Ktorym smerom sa ma poslat
	 * paket ?
	 * 
	 * @param paket
	 */
	public void posliPaket(Paket paket, boolean smer) {
		if (smer) {
			interpolacia = 0.0f;
		} else {
			interpolacia = 1.0f;
		}
		this.paket = paket;
		animacia = true;
		m_posielamAB = smer;
	}

	/**
	 * Animacia nam posle prikaz, ze informacia dorazila do ciela.
	 */
	private void uzatvorSpojenie() {
		if (m_posielamAB) {
			m_ciel.getZariadenie().Prijal(paket);
		} else {
			m_zdroj.getZariadenie().Prijal(paket);
		}
		animacia = false;
	}

	public void posunKomunikaciuDalej(float t) {
		if (!isAnimacia()) {
			return;
		}

		if (m_posielamAB) {
			interpolacia += t;

			// Ak hodnota pre interpolaciu je velka, sprava dosla
			if (interpolacia > 1.0) {
				uzatvorSpojenie();
			}
		} else {
			interpolacia -= t;

			// Ak hodnota pre interpolaciu je velka, sprava dosla
			if (interpolacia < 0.0) {
				uzatvorSpojenie();
			}
		}
	}

	/**
	 * Pretazena metoda equals kde porovname jedno Prepojenie s inym Prepojenie
	 */
	@Override
	public boolean equals(Object obj) {
		Prepojenie prep = (Prepojenie) obj;
		if (this.m_zdroj == prep.m_zdroj && this.m_ciel == prep.m_ciel) {
			return true;
		}
		if (this.m_zdroj == prep.m_ciel && this.m_ciel == prep.m_zdroj) {
			return true;
		}
		return false;
	}

	public Vrchol getZdroj() {
		return m_zdroj;
	}

	public void setZdroj(Vrchol zdroj) {
		this.m_zdroj = zdroj;
	}

	public Vrchol getCiel() {
		return m_ciel;
	}

	public void setCiel(Vrchol ciel) {
		this.m_ciel = ciel;
	}

	public boolean isAnimacia() {
		return animacia;
	}

	public Paket getPaket() {
		return paket;
	}

	/**
	 * Linearna interpolacia medzi 2 bodmy.
	 * Pomocou nej urcime, kde zobrazit kocku.
	 * 
	 * @param a
	 * @param b
	 * @param t
	 * @return
	 */
	public Point getPoziciaPaketu() {
		Point a = getZdroj().getStred();
		Point b = getCiel().getStred();
		int x = (int) ((1 - interpolacia) * a.x + interpolacia * b.x);
		int y = (int) ((1 - interpolacia) * a.y + interpolacia * b.y);
		return new Point(x, y);
	}
}