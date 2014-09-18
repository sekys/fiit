package Senzor;

import java.util.ArrayList;
import Prostredie.Prostredie;
import Udalost.InterfaceUdalost;

// Tato trieda je abstract tzv nemoze mat instancie
// a musi byt pretazena
public abstract class Senzor
{
	private ArrayList<ISenzorCall>	coZavolamKedSenzorNiecoZachyti;
	
	public Senzor(ISenzorCall ... calls) {
		coZavolamKedSenzorNiecoZachyti = new ArrayList<ISenzorCall>();
		for(ISenzorCall alarm : calls) {
			coZavolamKedSenzorNiecoZachyti.add(alarm);
		}
	}
	
	// Identifikacia senzora cez meno
	// Moze sa este prepisat takze protected
	protected String meno() {
		return "Zakladny senzor";
	}
	public String toString() {
		return meno();
	}
	
	// Tato metoda sa musi pretazovat tzv. co sa ma
	// stat ked sa prostredie sa narusilo
	// Tieto metody sa budu prepisovat
	protected void prostredieNarusene(Prostredie p) {
		System.out.println("Senzor :" + meno() + " oznamujem to alarmom.");
		for(ISenzorCall alarm : coZavolamKedSenzorNiecoZachyti) {
			alarm.senzorMaZavolal(this, p);
		}
		System.out.println("Senzor :" + meno() + " oznamenie alarmom konci.");
	}
	public abstract void sledujemProstredie(Prostredie p, InterfaceUdalost udalost);
	
	// Trieda dalej obsahuje FACTORY PATTERN - navrhovy vzor
	 public static Senzor fromTepelnyFactory(ISenzorCall ... call) {
         return new TepelnySenzor(call);
     }
     public static Senzor fromPohybovyFactory(ISenzorCall ... call) {
         return new PohybovySenzor(call);
     }
}
