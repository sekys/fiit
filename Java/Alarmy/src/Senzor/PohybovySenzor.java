package Senzor;

import Prostredie.Prostredie;
import Udalost.InterfaceUdalost;
import Udalost.Pohyb;

/**
 * Tento pohybovy senzor sleduje pohyb v prostredi
 * Trieda PohybovySenzor dedi od Senzor vlastnosti
 */
public class PohybovySenzor extends Senzor
{
	public PohybovySenzor(ISenzorCall ... call) {
		super(call);
	}

	// Tento senzor sa vola inak, takze prepiseme metodu
	// meno() nech sa senzor vola inak
	@Override
	protected String meno() {
		return "Pohybovy senzor";
	}
	
	@Override
	protected void prostredieNarusene(Prostredie p) {
		// Co sa ma stat ked sa prostredie narusilo ?
		System.out.println("Senzor: " + meno() + " nastal pohyb");
		super.prostredieNarusene(p);
	}

	// Priklad pouzitia instanceof
	@Override
	public void sledujemProstredie(Prostredie p, InterfaceUdalost udalost) {
		if( udalost instanceof Pohyb ) {
			prostredieNarusene(p);
		}	
	}
}
