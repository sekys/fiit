
package moje;

public class DebugPrint implements IPohyb
{

	public boolean pohyb(int jedniciek) {
		System.out.print("Vypis: ");
		switch (jedniciek) {
			case 0 :
			case 1 :
			case 2 : {
				System.out.println("H");
				break;
			}
			case 3 :
			case 4 : {
				System.out.println("D");
				break;
			}
			case 5 :
			case 6 : {
				System.out.println("P");
				break;
			}
			case 7 :
			case 8 : {
				System.out.println("L");
				break;
			}
		}
		return true;
	}
}
