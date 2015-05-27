package Fiit;
import java.awt.BorderLayout;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.JFrame;

import Server.Server;

/* School project #1
 * @autor: Lukas Sekerak
 * @skupina: 3
 *
 * Tento projekt sa stara o mini komunitnu siet alebo
 * socialnu siet, su tu pouzite rozne triedy, kde kazda
 * jedna ma ine vlastnosti a moznosti.Viac v popise kazdej
 * triedy.
 *
 * Projekt som sa snazil naprogramovat tak aby co najviac
 * vyuzival vyhody OOP.Ale mnohe funkcie by boli urcite
 * efektivnejsie napisane proceduralne.
 */

/* Main trieda.
 * Obsahuje hlavnu spustaciu trieda.
 * + Staticku metodu na prihlasovanie
 */
public class Main {

	public static void main(String[] args) {
		Server.main(null);
		
		Student u[] = { Login("Lukas", "1"), Login("Tomas", "12"),
				Login("Peter", "123") }; // , Login("Mato", "1234")
		
		// Posli nejake spravy
		u[0].Send("Ahoj");
		u[1].Send("Cau");
		u[2].Send("Hi");
		u[3].Send("Hello");
		
		// Uplatni polymorfizmus a posli smailika
		for(int i=0; i < u.length; i++) {
			u[0].Send(":)");
		}
	}

	// Ma uzivatela prihlasit
	public static Student Login(String meno, String heslo) {
		// Testuje ci sedi pouzivatelske meno a heslo
		// Normalne by tu bola nejaka poziadavka na databazu a ona by to overila
		// Zatial len takto primitivne
		// Cez prihlasenie chcem vlastne spravit rozne druhy uzivatelov

		// ~~~ Polymorfizmus ~~~
		if ((heslo == "1")) {
			return new Mgs(meno);
		}
		if ((heslo == "12")) {
			return new Ing(meno);
		}
		if ((heslo == "123")) {
			return new Profesor(meno);
		}
		System.out.println("Zadal si zle heslo !");
		return new UnlogedStudent(); //new UnlogedStudent();
	}
}