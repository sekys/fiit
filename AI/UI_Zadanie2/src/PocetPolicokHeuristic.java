
/**
 * Heurestika typu:
 * Pocet policok ktore nie su na svojom mieste.
 * @author Lukas Sekerak
 */
public class PocetPolicokHeuristic extends Heuristic
{

	public PocetPolicokHeuristic(Stav ciel) {
		super(ciel);
	}

	public int compareTo(Stav test) {
		byte[] cielData = ciel.getData();
		byte[] testData = test.getData();
		int index, x = 0;
		for (index = 0; index < cielData.length; index++) {
			if (testData[index] != cielData[index]) x++;
		}
		return x;
	}

}
