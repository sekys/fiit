import Base.Podmienky;
import Base.Uloha;

public class Program
{
	public Program() {

	}

	public void make() {
		Uloha uloha = new Uloha();
		//uloha.testRodina();
		uloha.testFiaty();
		uloha.dump();
		
		Podmienky pod = new Podmienky(uloha);
		pod.compute();
	}

	public static void main(String[] args) {
		new Program().make();
	}
}
