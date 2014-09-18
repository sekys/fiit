package Util;

import java.util.ArrayList;
import java.util.List;

public class Util
{
	public static List<String> NaviazPremenne(List<String> zoznam, List<String> vzory) {
		ArrayList<String> vysl = new ArrayList<String>();
		ArrayList<String> novyfakt;
		String[] vzor;
		String vysledok;
		for (String jedenvzor : vzory) {
			vzor = jedenvzor.split(" ");
			novyfakt = new ArrayList<String>();
			for (String slovo : vzor) {
				if (slovo.contains("?")) {
					String preloz = "";
					for (String prvok : zoznam) {
						String[] data = prvok.split("=");
						if (data[0].equals(slovo)) {
							preloz = data[1];
							break;
						}
					}
					vysledok = preloz;
				} else {
					vysledok = slovo;
				}
						
				novyfakt.add( vysledok );
			}
			vysl.add( Joiner.join(novyfakt) );
		}
		return vysl;
	}
}
