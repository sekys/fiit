
package moje;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Main
{
	public static int	pocetHladacov	= 100;
	public PovodnaMapa	mapa;
	public Zen			totalbest		= null;
	public XLS			xls				= new XLS();

	public Main() {
		mapa = new PovodnaMapa();
	}

	public void Load() {
		Zen.mapa_povodna = mapa;
		Hrabanie.povodna = mapa;

		Generacia temp = new Generacia(pocetHladacov);
		temp.nahodneNapln();
		Integer generacia = 0;
		Zen best;
		try {
			while (System.in.available() == 0 && generacia < 500) {
				System.out.println("\n\n");
				temp.ohodnotCeluGeneraciu();
				temp.sort();
				best = temp.best();
				System.out.println("Generacia c. " + generacia
						+ " najlepsi fitness " + best.pohrabanych);
				xls.addRow(generacia, best.pohrabanych);

				best.vypocitajFitness().vypis();
				if (totalbest == null
						|| totalbest.pohrabanych < best.pohrabanych) {
					totalbest = best.clone();
				}

				temp = temp.vygenerujGeneraciu();
				generacia++;
				break;
			}
		}
		catch (IOException io) {
			io.printStackTrace();
		}

		xls.write();
		if (totalbest != null) {
			System.out.println("Totalne najlepsi bol:");
			totalbest.vypocitajFitness().vypis();
		}
	}
	public void Presmeruj() {
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("output.txt"));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.setOut(out);
	}

	public static void main(String[] args) {
		Main main = new Main();
		//main.Presmeruj();
		main.mapa.Nacitaj();
		main.mapa.vypis();
		main.Load();
	}

}
