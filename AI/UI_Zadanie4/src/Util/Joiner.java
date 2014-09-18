
package Util;

import java.util.List;

public class Joiner
{

	public static String join(String[] words, int l, String p) {
		int dlzka = words.length;
		if (dlzka == 0) return "";
		String spolu = "";
		for (int x = l; x < dlzka - 1; x++)
			spolu = spolu + words[x] + p;

		return spolu + words[dlzka - 1];
	}
	public static String join(List<String> words, int l, String p) {
		int dlzka = words.size();
		if (dlzka == 0) return "";
		String spolu = "";
		for (int x = l; x < dlzka - 1; x++)
			spolu = spolu + words.get(x) + p;

		return spolu + words.get(dlzka - 1);
	}
	public static String join(String[] words) {
		return join(words, 0, " ");
	}
	public static String join(List<String> words) {
		return join(words, 0, " ");
	}
	public static String joinPost(List<String> words) {
		return join(words, 1, " ");
	}
	public static String joinPost(String[] words) {
		return join(words, 1, " ");
	}
}
