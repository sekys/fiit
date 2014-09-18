package Senzor;

import Prostredie.Prostredie;
import Udalost.InterfaceUdalost;
import Udalost.Poziar;

/**
 * Tento tepleny senzor zachytava tepelne udalosti v prostredi
 * Trieda PohybovySenzor dedi od Senzor vlastnosti
 */
public class TepelnySenzor extends Senzor
{
	public TepelnySenzor(ISenzorCall ... call) {
		super(call);
	}

	// Tento senzor sa vola inak, takze prepiseme metodu
	// meno() nech sa senzor vola inak
	@Override
	protected String meno() {
		return "Tepelny senzor";
	}
	
	@Override
	protected void prostredieNarusene(Prostredie p) {
		// Co sa ma stat ked sa prostredie narusilo ?
		System.out.println("Senzor: " + meno() + " nastal poziar");
		super.prostredieNarusene(p);
	}

	@Override
	public void sledujemProstredie(Prostredie p, InterfaceUdalost udalost) {
		// Ako tento senzor sleduje prostredie ?
		if( udalost instanceof Poziar ) {
			prostredieNarusene(p);
		}
	}
}
