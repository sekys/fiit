/**
 * Heurestika
 * Pocet policok ktore nie su na svojom mieste.
 * 
 * @author Lukas Sekerak
 */
public abstract class Heuristic implements Comparable<Stav>
{
	protected Stav	ciel;

	public Heuristic(Stav ciel) {
		this.ciel = ciel;
	}

	public abstract int compareTo(Stav test);

	public Stav getCiel() {
		return ciel;
	}
}
