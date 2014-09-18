/**
 * Heurestika typu:
 * Sucet vzdialenosti od policok od ich cielovych pozicii
 * 
 * @author Lukas Sekerak
 */
public class SucetVzdialenostiHeuristic extends Heuristic
{
	private int	width;

	public SucetVzdialenostiHeuristic(Stav ciel, int width) {
		super(ciel);
		this.width = width;
	}

	private int getYPos(int index) {
		return index / width;// + 1; nemusime pripocitavat kedze vysledok 0-1 a 1-2 je rovnaky
	}

	private int getXPos(int index) {
		return index % width; // + 1;
	}

	public int compareTo(Stav test) {
		byte[] cielData = ciel.getData();
		byte[] testData = test.getData();
		int a, b, sum = 0;
		int x1, y1;
		int x2, y2;

		for (a = 0; a < cielData.length; a++) {
			// if (cielData[a] == 0) continue; // Mame ignorovat medzeru ? => Potom vysledok nebude 0
			if (cielData[a] != testData[a]) {
				x1 = getXPos(a);
				y1 = getYPos(a);
				for (b = 0; b < cielData.length; b++) {
					if (a == b) continue;

					// Nasiel som prvok vypocitaj vzdialenost - Manhatten distance
					if (cielData[a] == testData[b]) {
						x2 = getXPos(b);
						y2 = getYPos(b);
						sum += Math.abs(x1 - x2) + Math.abs(y1 - y2);
						break;
					}
				}
			}

		}
		return sum;
	}
}
