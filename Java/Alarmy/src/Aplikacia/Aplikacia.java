
package Aplikacia;

import Alarm.AlarmPostriekovac;
import Alarm.AlarmPoziarny;
import Alarm.AlarmVolajHasicov;
import Prostredie.ProstredieKdeSaUdalostiOpakuju;
import Prostredie.SledovaneProstredie;
import Senzor.Senzor;
import Udalost.InterfaceUdalost;
import Udalost.Pohyb;
import Udalost.Poziar;

/*
 * Cela aplikacia sa spusta v SINGLETON navrhovom vzore.
 * Toto cele dole je SINGLETON.
 */
@SuppressWarnings("serial")
public class Aplikacia extends Okno
{
	/**
	 * Pri vytvoreni instanicie pri konstruktore sa zavola
	 * konstruktor rodica cim sa spusti dalsi kod.
	 */
	private Aplikacia() {
		super();
	}

	/**
	 * TOto drzi celu instanciu v statickej premennej podtriedy
	 * UIHolder. Je to priklad vnorenej triedy.
	 */
	private static class UIHolder
	{
		public static final Aplikacia	instancia	= new Aplikacia();
	}

	/**
	 * Metoda zavola triedu UIHolder a vytvori jednu instanciu.
	 * Ak sa budeme chciet dostat naspat k tejto instancii.
	 * Staci zavolat Aplikacia.getInstance()
	 */
	public static Aplikacia getInstance() {
		return UIHolder.instancia;
	}
	
	protected InterfaceUdalost poziar;
	protected InterfaceUdalost pohyb;
	
	protected SledovaneProstredie pA;
	protected SledovaneProstredie pB;
	
	protected AlarmPoziarny postriekovac;
	protected AlarmPoziarny alarmcoZavolaHasicov;
	
	protected void start() {
		// Vytvor udalosti pre prostredie
		poziar = new Poziar();
		pohyb = new Pohyb();

		// Vytvor prostredie kde sa kazdych X-sec budu opakovat udalost
		// V prostredi sa mzoe spustat N udalosti
		pA = new ProstredieKdeSaUdalostiOpakuju(poziar);
		pA.setMenoProstredia("pA");
		pB = new ProstredieKdeSaUdalostiOpakuju(pohyb);
		pB.setMenoProstredia("pB");

		// Idem vytvorit Alarmy
		// Kazdy alarm ma len jeden TYP senzoru
		postriekovac = new AlarmPostriekovac();
		alarmcoZavolaHasicov = new AlarmVolajHasicov();

		// Vytvor senzory pomocou FACTORY pattern
		// A nainstaluj seznory do prostredia
		// Senzory mozu oznamovat udalost viacerym Alarmom
		pA.nainstalujSenzor(postriekovac.getSenzor());
		pA.nainstalujSenzor(alarmcoZavolaHasicov.getSenzor());
		pA.nainstalujSenzor(postriekovac.getSenzor());

		Senzor multifunkcny = Senzor.fromTepelnyFactory(postriekovac, alarmcoZavolaHasicov);

		postriekovac.setZapnuty(false);
		postriekovac.setZapnuty(true);
		postriekovac.setZapnuty(false);
		postriekovac.setZapnuty(true);
		postriekovac.setZapnuty(false);
		postriekovac.vypisHistoriu("vystup.txt");

		pB.nainstalujSenzor(multifunkcny);
		pB.nainstalujSenzor(alarmcoZavolaHasicov.getSenzor());
		pB.nainstalujSenzor(postriekovac.getSenzor());
		pB.odinstalujmSenzor(multifunkcny);

	}
	
	protected void alarmAClick() {
		postriekovac.setZapnuty( !postriekovac.isZapnuty() );
		System.out.println("Prepinam alarm postriekovac.");
	}
	protected void alarmBClick() {
		alarmcoZavolaHasicov.setZapnuty( !alarmcoZavolaHasicov.isZapnuty() );	
		System.out.println("Prepinam alarm hasicov.");
	}
	protected void vypis() {
		postriekovac.vypisHistoriu("postriekovac.txt");
		alarmcoZavolaHasicov.vypisHistoriu("hasici.txt");
	}
	
	/**
	 * Tu sa spusti program, a vytvori sa jedna a jedinecna
	 * instacia Aplikacia.
	 */
	public static void main(String[] args) {
		Aplikacia.getInstance();
	}
}
