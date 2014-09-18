


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main
{
	public Main() {

	}

	public void uloha(Scanner in) {
		Integer M = in.nextInt();
		System.out.println(M);
	}
	
	public void Load() {
		File file = new File("in.in");

		try {		
			Scanner in = new Scanner(file);
			int N = in.nextInt();
			for (Integer i = 1; i <= N; ++i) {
				System.out.print("Case #" + i.toString() + ": ");
				uloha(in);
			}
		}
		catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void Presmeruj() {
		PrintStream out = null;
		try {
			out = new PrintStream(new FileOutputStream("out.out"));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.setOut(out);
	}

	
	public static void main(String[] args) {
		Main main = new Main();
		main.Presmeruj();
		main.Load();
	}
}
