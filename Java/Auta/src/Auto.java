public class Auto
{
	public String	farba;
	public boolean	velke;	// true je 3, false je 2
	public boolean	xORy;	// Ktory, smerom sa moze pohybovat true je X

	public boolean otestuj(byte zakladnaPozicia, byte testovaciaPozicia) {
		if(zakladnaPozicia < 0 || zakladnaPozicia > 35) {
			throw new IndexOutOfBoundsException();
		}
		if(testovaciaPozicia < 0 || testovaciaPozicia > 35) {
			throw new IndexOutOfBoundsException();
		}
		// Horizontalne - x suradnica
		if (zakladnaPozicia == testovaciaPozicia) return true;
		if (xORy) {
			if ((zakladnaPozicia + 1) == testovaciaPozicia) return true;
			if (velke && (zakladnaPozicia + 2) == testovaciaPozicia) return true;
		} else {
			if ((zakladnaPozicia + 6) == testovaciaPozicia) return true;
			if (velke && (zakladnaPozicia + 12) == testovaciaPozicia) return true;
		}
		return false;
	}
}
