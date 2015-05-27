package Fiit;


public class Profesor extends Student {
	public Profesor(String meno) {
		super(meno);
	}
	public Profesor(String meno, boolean pohlavie) {
		super(meno, pohlavie);
	}
	public Profesor(String meno, boolean pohlavie, int vek) {
		super(meno, pohlavie, vek);
	}
	public String toString() {
		return "Profesor "+name;
	}
	public void PoslatNaKomisiu(Student u) {
		u.SetPlagiator(true);
	}
	public void Oznamkovat(Student u, int znamka) {
		u.PridatZnamku(znamka);
	}
	public int GetStupen() {
		return 3;
	}	
}

