
package Prostredie;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import Udalost.InterfaceUdalost;

/**
 * Toto prostredie ma vlastnost navyse, udalosti v nom sa spustaju pravidelne
 */
public class ProstredieKdeSaUdalostiOpakuju extends SledovaneProstredie
{
	/**
	 * Konstruktor ProstredieKdeSaUdalostiOpakuju prijma argmunt pole Udalosti
	 * ktore mozu nastat, sam rozhodne s akou pravdepodobnostou nastanu.
	 */
	public ProstredieKdeSaUdalostiOpakuju(InterfaceUdalost ... udalosti) {		
		super();
		
		Random generator = new Random();
		for( InterfaceUdalost udalost : udalosti) {
			naplanujUdalost(udalost, 10 + generator.nextInt(20));
		}
	}

	// Pouzitie TimerTask na pravidelne spustanie akcii
	// Je tu pouzity termin final ktory udalost skopiruje
	private void naplanujUdalost(final InterfaceUdalost udalost, Integer cas) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				nastalaUdalost(udalost);				
			}			
		};
		
		cas = cas * 1000;
		Timer timer = new Timer();
		timer.schedule(task, cas, cas);
		System.out.println("Planujem udalost: " + udalost.pomenovanieUdalosti() + " kazdych " + cas + " sek.");
	}
}
