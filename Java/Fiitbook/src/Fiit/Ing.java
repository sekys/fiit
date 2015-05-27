package Fiit;


public class Ing extends Student {
	public Ing(String meno) {
		super(meno);
	}
	public Ing(String meno, boolean pohlavie) {
		super(meno, pohlavie);
	}
	public Ing(String meno, boolean pohlavie, int vek) {
		super(meno, pohlavie, vek);
	}
	
	public String toString() {
		return "Mgs "+name;
	}
	public int GetStupen() {
		return 2;
	}	
}
