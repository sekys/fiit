
package moje;

import java.util.Arrays;

public class VirtualnyStroj implements Runnable
{
	private int		aktualnaInstrukcia;
	public int		vykonanychInstrukcii;
	private int		maxInstrukcii	= 500;
	private byte[]	pamBunky;
	private IPohyb	print;

	public VirtualnyStroj() {
		
	}
	
	public void vypocitaj(byte[] pamet, IPohyb pohyb) {
		print = pohyb;
		pamBunky = Arrays.copyOf(pamet, pamet.length);
	}

	protected boolean vykonavaj(byte aktualnaBunka) {
		byte odkazovanaBunka;
		Instrukcie.Type typ = Instrukcie.getType(aktualnaBunka);
		int adresa = Instrukcie.getValue(aktualnaBunka);
		/*
		 * System.out.println(Integer.toString(vykonanychInstrukcii)
		 * + ". Aktualna bunka: " + Instrukcie.Byte2String(aktualnaBunka)
		 * + " Typ: " + typ + " Hodnota/Adresa:"
		 * + Integer.toString(adresa) + " spravi");
		 */
		if (adresa < 0 || adresa > 63) {
			System.out.println("Velka adresa koncim ?");
			return false;
		}
		odkazovanaBunka = pamBunky[adresa];
		/*
		 * System.out.println("Odkazovana bunka: "
		 * + Instrukcie.Byte2String(odkazovanaBunka));
		 */
		switch (typ) {
			case INC : {
				pamBunky[adresa]++;
				/*
				 * System.out.println("po INC operacii "
				 * + Instrukcie.Byte2String(pamBunky[adresa]));
				 */
				break;
			}
			case DEC : {
				pamBunky[adresa]--;
				/*
				 * System.out.println("po DEC operacii "
				 * + Instrukcie.Byte2String(pamBunky[adresa]));
				 */
				break;
			}
			case JUMP : {
				aktualnaInstrukcia = adresa;
				// System.out.println("JUMP na " + aktualnaInstrukcia);
				break;
			}
			case PRINT : {
				return print.pohyb(Instrukcie.obsahujeJedniciek(odkazovanaBunka));
				// break;
			}
		}
		return true;
	}

	// @Override
	public void run() {
		byte aktualnaBunka;
		// System.out.println("Spustam virt. stroj.");
		aktualnaInstrukcia = 0;
		vykonanychInstrukcii = 0;
		while (vykonanychInstrukcii < maxInstrukcii) {
			vykonanychInstrukcii++;
			aktualnaBunka = pamBunky[aktualnaInstrukcia];
			aktualnaInstrukcia++;

			// Ak program prisiel na adresu na bunku poslednu
			if (aktualnaInstrukcia >= 64) {
				// Program konci
				// System.out.println("Vsetky instrukcie som spracoval.");
				return;
			}

			if (!vykonavaj(aktualnaBunka)) {
				// System.out.println("Predcasne som skoncil.");
				return; // Virtualny stroj sa ma ukonci
			}
		}
		// System.out.println("Vykonal som 500 instrukcii.");
	}

	public static void main(String[] args) {
		VirtualnyStroj stroj = new VirtualnyStroj();
		byte[] pamet = new byte[64];
		
		// 00000000 00011111 00010000 01010000 00000101 11000000 10000100		
		pamet[0] = Instrukcie.String2Byte("00000000");
		pamet[1] = Instrukcie.String2Byte("00011111");
		pamet[2] = Instrukcie.String2Byte("00010000");
		pamet[3] = Instrukcie.String2Byte("01010000");
		pamet[4] = Instrukcie.String2Byte("00000101");
		pamet[5] = Instrukcie.String2Byte("11000000");
		pamet[6] = Instrukcie.String2Byte("10000100");
		stroj.vypocitaj(pamet, new DebugPrint());

		for (int i = 0; i < 7; i++)
			System.out.println(Instrukcie.Byte2String(stroj.pamBunky[i]));

		stroj.run();
	}
}
