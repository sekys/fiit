package siet;

/**
 * Kazdy paket ma zdrojovu, cielovu adresu a prenasanu informaciu.
 */
public class Paket {
	private SietoveZariadenie zdroj;
	private SietoveZariadenie ciel;
	private String prenasanaInformacia;

	public SietoveZariadenie getZdroj() {
		return zdroj;
	}

	public void setZdroj(SietoveZariadenie zdroj) {
		this.zdroj = zdroj;
	}

	public SietoveZariadenie getCiel() {
		return ciel;
	}

	public void setCiel(SietoveZariadenie ciel) {
		this.ciel = ciel;
	}

	public String getPrenasanaInformacia() {
		return prenasanaInformacia;
	}

	public void setPrenasanaInformacia(String prenasanaInformacia) {
		this.prenasanaInformacia = prenasanaInformacia;
	}

	/**
	 * Vypis informacie o pakete
	 * 
	 * @return retazec
	 */
	@Override
	public String toString() {
		return "Paket [zdroj=" + zdroj + ", ciel=" + ciel
				+ ", prenasanaInformacia=" + prenasanaInformacia + "]";
	}
}
