package Fiit;


public class Mgs extends Student {
	public Mgs(String meno) {
		super(meno);
	}
	public Mgs(String meno, boolean pohlavie) {
		super(meno, pohlavie);
	}
	public Mgs(String meno, boolean pohlavie, int vek) {
		super(meno, pohlavie, vek);
	}
	
	public String toString() {
		return "Mgs "+name;
	}
	public int GetStupen() {
		return 1;
	}	
}

