package FiitSkupiny;
import java.util.ArrayList;

import Fiit.Student;


public class SkupinaHviezda extends Skupina {
	public SkupinaHviezda() {
		super();
	}
	
	public void Zoznam(boolean plagiator) {
		for (Student e : this) {
			if(e.GetPlagiator() == plagiator) {
				System.out.println("* "+e+" *\n");
			}
		}
	}
	public void Zoznam() {
		for (Student e : this) {
			System.out.println("* "+e+" *\n");
		}
	}
	public void Zoznam(int stupen) {
		for (Student e : this) {
			if(e.GetStupen() == stupen) {
				System.out.println("* "+e+" *\n");
			}
		}
	}
}