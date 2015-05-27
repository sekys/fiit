package FiitSkupiny;
import java.util.ArrayList;

import Fiit.Student;


public class Skupina extends ArrayList<Student> {
	protected int id;
	private static int ids = 0;
	public Skupina() {
		id = ids;
		ids++;
	}
	
	public void Zoznam(boolean plagiator) {
		for (Student e : this) {
			if(e.GetPlagiator() == plagiator) {
				System.out.println(e+"\n");
			}
		}
	}
	public void Zoznam() {
		for (Student e : this) {
			System.out.println(e+"\n");
		}
	}
	public void Zoznam(int stupen) {
		for (Student e : this) {
			if(e.GetStupen() == stupen) {
				System.out.println(e+"\n");
			}
		}
	}
	public int GetID() {
		return id;
	}
}