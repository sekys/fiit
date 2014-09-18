
package Prostredie;

/**
 * Toto je trieda pre prostredie, obsahuje informacie.
 * Napriklad vysku, sirku prostredia, pripadne
 * v akej budove je to prostredie
 */
public class Prostredie
{
	// Vsetke tieto atributy su private
	// tzv vidi ich len tato trieda a nechceme ich nikomu ukazat
	private int		sirka;
	private int		vyska;
	private String	budova;
	
	/**
	 * V Nasledujucej casti ide 6 metod, ktore su getter a setter.
	 * Teda nastavovace a metody na ziskanie vlastnosti z tochto prostredia.
	 */
	public int getSirka() {
		return sirka;
	}
	public void setSirka(int sirka) {
		this.sirka = sirka;
	}
	public int getVyska() {
		return vyska;
	}
	public void setVyska(int vyska) {
		this.vyska = vyska;
	}
	public String getProstredieMeno() {
		return budova;
	}
	public void setMenoProstredia(String budova) {
		this.budova = budova;
	}
}
