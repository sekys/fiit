
package Base;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Rules extends ArrayList<Rules.Rule>
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5858149330758924404L;

	public class Rule
	{
		public ArrayList<String>	conditions	= new ArrayList<String>();

		public Rule(String nazov, String[] podmienky, String[] akcie) {
			conditions.addAll(Arrays.asList(podmienky));
			meno = nazov;
			actions.addAll(Arrays.asList(akcie));
		}

		public ArrayList<String>	actions	= new ArrayList<String>();

		public String toString() {
			return "Meno: " + meno + "\n"
					+ // MENO, AK< POTOM
					"AK    " + conditions.toString() + "\n" + "POTOM "
					+ actions.toString();
		}

		public String	meno;
	}

	public Rules() {
		super();
	}

	public void load(String subor) {
		BufferedReader buffer = null;
		String x, y, z = null;
		try {
			buffer = new BufferedReader(new FileReader(subor));
			while ((x = buffer.readLine()) != null) {
				if (x.length() > 0) {
					y = buffer.readLine();
					z = buffer.readLine();
					add(new Rule(x, y.split(";"), z.split(";")));
				}
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dump() {
		System.out.println();
		System.out.println("Obsahujem pravidla:");
		for (Rule p : this) {
			System.out.println(p + "\n\n");
		}
	}
}
