
package Prostredie;

import java.util.ArrayList;
import Senzor.Senzor;
import Udalost.InterfaceUdalost;

/**
 * Toto prostredie ma vlastnost navyse, moze byt sledovane !
 * Pretoze moze mat nainstalovane alarmy.
 */
public class SledovaneProstredie extends Prostredie
{
	private ArrayList<Senzor> nainstalovaneAlarmy;
	
	/**
	 * Konstruktor SledovaneProstredie
	 */
	SledovaneProstredie() {		
		nainstalovaneAlarmy = new ArrayList<Senzor>();
	}

	public void nainstalujSenzor(Senzor s) {
		System.out.println("Do prostredia " + getProstredieMeno() + " som nainstaloval senzor  " + s.toString());
		nainstalovaneAlarmy.add(s);
	}
	
	public void odinstalujmSenzor(Senzor s) {
		System.out.println("V prostredi " + getProstredieMeno() + " som odinstaloval senzor  " + s.toString());
		nainstalovaneAlarmy.remove(s);
	}
	
	protected void nastalaUdalost(InterfaceUdalost udalost) {
		System.out.println();
		System.out.println("Nastala udalost: " + udalost.pomenovanieUdalosti() );		
		System.out.println("Spustam vsetky senzory.");		
		for(Senzor s : nainstalovaneAlarmy) {
			s.sledujemProstredie(this, udalost);
		}
		System.out.println("Vsetky senzory boli aplikovane.");
		System.out.println();
	}
}
