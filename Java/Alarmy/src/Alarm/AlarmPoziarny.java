package Alarm;

import Prostredie.Prostredie;
import Senzor.Senzor;
import Senzor.TepelnySenzor;

public class AlarmPoziarny extends Alarm
{
	// Vytvor senzory pomocou FACTORY pattern
	// Senzory mozu oznamovat udalost viacerym Alarmom
	public AlarmPoziarny() {
		senzor = Senzor.fromTepelnyFactory(this);
	}
	
	private Senzor senzor;
	
	
	public Senzor getSenzor() {
		return senzor;		
	}
	public void senzorMaZavolal(Senzor senzor, Prostredie p) { // callback
		if( senzor instanceof TepelnySenzor ) {
			super.senzorMaZavolal(senzor, p);
		}
	}
}
