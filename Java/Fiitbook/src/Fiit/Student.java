package Fiit;
import java.util.ArrayList;
import java.util.Date;

import Client.Client;

public class Student extends Client implements OperacieSoStudentom {
	protected int vek = 0;
	protected boolean pohlavie = false;
	protected boolean plagiator;
	protected ArrayList<Integer> znamka = new ArrayList<Integer>();
	protected Date zapisany;
	
	public Student() {
		
	}
	public Student(String meno) {
		super();
		name = meno;
		setTitle();
	}
	public Student(String meno, boolean pohlavie) {
		this(meno);
		this.pohlavie = pohlavie;
	}
	public Student(String meno, boolean pohlavie, int vek) {
		this(meno, pohlavie);
		this.pohlavie = pohlavie;
	}
	public boolean GetPlagiator() {	
		return plagiator;
	}
	public void SetPlagiator(boolean x) {
		plagiator = x;
	}
	public int GetVek() {
		return vek;
	}
	public void SetVek(int vek) {
		this.vek = vek;
	}
	public int GetStupen() {
		return 0;
	}	
	public void Send(String txt) {
		super.Send(this+": "+txt);
	}
	public void PridatZnamku(Integer znamka) {
		this.znamka.add(znamka);
	}
	public void PoslatNaKomisiu(Student u) {
		Alert("Niesi profesor");
	}
	public void Oznamkovat(Student u, Integer znamka) {
		Alert("Niesi profesor");
	}
	public String toString() {
		return name;
	}
	public void Vyhodit() {
		System.exit(-1);
	}
}