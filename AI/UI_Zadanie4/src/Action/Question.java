
package Action;

import java.util.List;
import java.util.Scanner;
import Base.Facts;
import Util.Joiner;

public class Question implements Action
{
	private Facts	m_facts;

	public Question(Facts a) {
		m_facts = a;
	}

	private String otazka(String x) {
		return x.substring(0, x.indexOf(")"));
	}

	private String odpoved(String x) {
		return x.substring(x.lastIndexOf("(") + 1, x.length() - 1);
	}

	public void make(String[] pom) {
		if ( m_facts.obsahuje(odpoved(Joiner.joinPost(pom)))) {
			String spolu = Joiner.join(pom);
			System.out.println("-----------------------------------");
			System.out.println( otazka(spolu) );
			String mojaOdpoved = new Scanner(System.in).nextLine();
			String vys = nahradOdpoved(odpoved(spolu), mojaOdpoved);
			m_facts.add(vys);
		}
	}
	
	private String nahradOdpoved(String odpoved, String mojaOdpoved) {
		String[] slova = odpoved.split(" ");
		for (int x = 0; x < slova.length; x++) {
			if (slova[x].equals("!")) slova[x] = mojaOdpoved;
		}
		return Joiner.join(slova);
	}
	
	public boolean can(List<String> pom) {
		return m_facts.obsahuje( odpoved(Joiner.joinPost(pom)) );
	}
}
