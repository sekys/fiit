
package Alarm;

import Prostredie.Prostredie;
import Senzor.ISenzorCall;
import Senzor.Senzor;

public class Alarm implements ISenzorCall
{
	// Historia zaznamov pre tento alarm
	private Historia	zaznamy;

	// Je senzor zapnuty ?
	protected boolean	zapnuty;

	// Toto je konstruktor
	public Alarm() {
		zaznamy = new Historia();
		setZapnuty(true);
	}

	public void vypisHistoriu(String subor) {
		zaznamy.vypis(subor);
	}

	// Mame upozornit pouzivatela ze neico sa deje urcitou spravou
	// Prijma jeden argument
	public void upozorniPouzivatela(String sprava) {
		System.out.println(menoAlarmu() + " " + sprava);
	}

	// Metoda je private - vidi ju len tato trieda
	protected String upozornovaciaHlaska() {
		return "Vykonavam akciu.";
	}

	// Zakladna metoda Object je pretazena a bude vypisovat
	// nazov alarmu
	public String toString() {
		return menoAlarmu();
	}

	// Zakladna identifikacia senzora
	public String menoAlarmu() {
		return "Zakladny alarm.";
	}

	// Toto sa spusti ked senzor neico identifikoval
	// Je to pretazene z interfacu ISenzorCall
	// V argumente mi pride senzor , ktory to zahlasil
	public void senzorMaZavolal(Senzor senzor, Prostredie p) { // callback
		if (isZapnuty()) {
			System.out.println("Senzor ma zavolal:" + menoAlarmu() + " z prostredia " + p.getProstredieMeno());
			upozorniPouzivatela(upozornovaciaHlaska());
		} else {
			System.out.println("Senzor ma zavolal:" + menoAlarmu() +" z prostredia " + p.getProstredieMeno() + " ale som vypnuty." );
		}
	}

	/**
	 * V tejto casti ide getter a setter pre ALARM
	 * Tieto metody sa daju neskor pretazit
	 * Ja v nich hlavne zapisuje historiu zapina a vypinania
	 */
	public boolean isZapnuty() {
		return zapnuty;
	}

	// V tejto metode pouzivam termin THIS na odlisenie
	// od zapnuty a this.zapnuty
	public void setZapnuty(boolean zapnuty) {
		if (this.zapnuty == zapnuty) return;
		String operacia = zapnuty ? "zapinam" : "vypinam";
		zaznamy.spravZaznam("Senzor:" + menoAlarmu() + " " + operacia);
		this.zapnuty = zapnuty;
	}
}
