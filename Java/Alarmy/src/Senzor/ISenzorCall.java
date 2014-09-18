package Senzor;

import Prostredie.Prostredie;


// Priklad pouzitia interfacu
public interface ISenzorCall
{
	// Senzor nieco zachytil nieco sa stalo
	// V argumente mi pride senzor , ktory to zahlasil
	public void senzorMaZavolal(Senzor a, Prostredie p);
}
