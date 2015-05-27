package Fiit;


public interface OperacieSoStudentom {
	public boolean GetPlagiator();
	public void SetPlagiator(boolean set);
	public int GetVek();
	public void SetVek(int vek);
	public int GetStupen();
	public void Vyhodit();
	public void PridatZnamku(Integer znamka);
	public void PoslatNaKomisiu(Student u);
	public void Oznamkovat(Student u, Integer znamka);
	
	public void Send(String txt);
	public String toString();
}
