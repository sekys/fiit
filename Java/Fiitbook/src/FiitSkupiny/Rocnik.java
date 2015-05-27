package FiitSkupiny;
import java.util.ArrayList;


public class Rocnik extends ArrayList<Skupina> {
	protected int id;
	private static int ids = 0;
	public Rocnik() {
		id = ids;
		ids++;
	}
		
	void Zoznam() {
		for (Skupina e : this) {
			e.Zoznam();
		}
	}
	void Zoznam(int stupen) {
		for (Skupina e : this) {
			e.Zoznam(stupen);
		}
	}
	void Zoznam(boolean plagiator) {
		for (Skupina e : this) {
			e.Zoznam(plagiator);
		}
	}
	public int GetID() {
		return id;
	}
}
