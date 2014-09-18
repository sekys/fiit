package Base;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Facts extends ArrayList<String>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 3063971877511802902L;

	public Facts() {
		super();
	}

	/**
	 * Nacitaj subor
	 * @param subor
	 */
	public void load(String subor) {
		BufferedReader buffer = null;
		String x = null;
		try {
			buffer = new BufferedReader(new FileReader(subor));
			while ((x = buffer.readLine()) != null) {
				add(x);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * VYpis fakty
	 */
	public void dump() {
		System.out.println("");
		System.out.println("Vsetky fakty su:");

		for (String x : this)
			System.out.println(x);

	}

	/**
	 * ZIsti ci mame taky fact
	 * @param Odpoved
	 * @return
	 */
	public boolean obsahuje(String Odpoved) {
		for (String x : this)
			if (similarFacts(Odpoved, x)) return false;

		return true;
	}

	/**
	 * Porovnaj 2 fakty
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean similarFacts(String a, String b) {
		String[] x = a.split(" ");
		String[] y = b.split(" ");
		if (x.length != y.length) 
			return false;
		
		return similarFacts(x, y);
	}
	
	/**
	 * Porovnaj 2 fakty
	 * @param a
	 * @param b
	 * @return
	 */
	private boolean similarFacts(String[] a, String[] b) {
		int x;
		for (x = 0; x < a.length; x++) 
			if (!a[x].contains("!") && !a[x].equals(b[x])) return false;
		
		return true;	
	}
}
