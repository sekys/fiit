
package Alarm;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Tato trieda sa pouzije pri ALARME alebo hociktorej
 * udalosti ktoru treba uchovat v podobe historie.
 * Tato trieda robi zaznamy.
 * 
 * Moze ich robit do suboru alebodo vystupu
 */
public class Historia
{
	// Cela historia zaznamov sa uklada v podobe pola
	private ArrayList<Zaznam>	historia;

	// Vnutorna trieda pre uchovavanie historie
	protected class Zaznam
	{
		public String	sprava;
		public Date		cas;
	}

	// Konstruktor Historia
	public Historia() {
		this.historia = new ArrayList<Zaznam>();
	}

	// Vloz aktualny zaznam do historie, vloz aj aktualny cas
	public void spravZaznam(String sprava) {
		Zaznam z = new Zaznam();
		z.cas = new Date();
		z.sprava = sprava;
		historia.add(z);
	}

	// Metoda vypis do ktorej vstupuje argument nazov suboru
	// Ak je vyplneny zapise sa vystup do suboru.
	public void vypis(String subor) {
		PrintStream stream = null;

		if (subor == null) {
			stream = System.out; // zapisuj do konzole
		} else { 
			try { 				// zapisuj do suboru
				stream = new PrintStream(new FileOutputStream(subor, true));
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		// Vyuzitie foreach
		for( Zaznam z : historia) {
			stream.println("Cas: " + z.cas + " Sprava: " + z.sprava);
		}
	}
}
