
package moje;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main
{
	public static int	pocetHladacov	= 100;
	public Mapa			mapa			= new Mapa();
	public Hladac		totalbest		= null;
	public XLS			xls				= new XLS();

	public Main() {
		mapa = new Mapa();
	}

	public void Load() {
		Generacia temp = new Generacia(mapa, pocetHladacov);
		temp.nahodneNapln(pocetHladacov, 64);
		Integer generacia = 0;
		Hladac best;
		// try {
		// System.in.available() == 0 &&
		while (generacia < 500) {
			temp.ohodnotCeluGeneraciu();
			temp.sort();
			best = temp.best();
			System.out.println("Generacia c. " + generacia
					+ " najlepsi fitness " + best.fitness);
			best.vypisCestu();
			xls.addRow(generacia, best.fitness);
			if (totalbest == null || totalbest.fitness < best.fitness) {
				totalbest = best.clone();
			}

			temp = temp.vygenerujGeneraciu();
			generacia++;
			// break;
		}
		/*
		 * }
		 * catch (IOException io) {
		 * io.printStackTrace();
		 * }
		 */
		xls.write();
		if (totalbest != null) {
			System.out.println("Totalne najlepsi bol:");
			totalbest.vypisCestu();
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
		// main.Presmeruj();
		main.mapa.Load();
		main.mapa.print();
		main.Load();
	}

}
